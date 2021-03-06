/**********************************************************************
 * Copyright (c) 2012 by the President and Fellows of Harvard College
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA.
 *
 * Contact information
 *
 * Office for Information Systems
 * Harvard University Library
 * Harvard University
 * Cambridge, MA 02138
 * (617)495-3724
 * hulois@hulmail.harvard.edu
 **********************************************************************/
package edu.harvard.lib.librarycloud.items;

import edu.harvard.lib.librarycloud.items.dc.Metadata;
import edu.harvard.lib.librarycloud.items.mods.ModsType;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer.RemoteSolrException;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

/**
 *
 * ItemDAO makes queries to the solr index via solrj methods; item id info or
 * query paramters are parsed and mapped into solr query syntax
 *
 * @author Michael Vandermillen
 *
 */
public class ItemDAO {
	Logger log = Logger.getLogger(ItemDAO.class);
	private int limit = 10;
	private Facet facet = null;

	/**
	 * Returns a MODS record for a given recordIdentifier.
	 * 
	 * @param id a recordIdentifier for a solr document
	 * @return the ModsType for this recordidentifier
	 * @see ModsType
	 */
	public ModsType getMods(String id) throws JAXBException {
		SolrDocumentList docs;
		SolrDocument doc;
		ModsType modsType = new ModsType();
		HttpSolrServer server = null;
		try {
			if (id.contains(":"))
				id = "\"" + id + "\"";
			server = SolrServer.getSolrConnection();
			SolrQuery query = new SolrQuery("recordIdentifier:" + id);
			QueryResponse response = server.query(query);
			docs = response.getResults();
			if (docs.size() == 0)
				throw new LibraryCloudException("Item " + id + " not found", Response.Status.NOT_FOUND);
			else if (docs.size() > 1)
				throw new LibraryCloudException("Internal Server Error", Response.Status.INTERNAL_SERVER_ERROR); 
			else {
				doc = docs.get(0);
				modsType = unmarshallModsType(doc);
			}
		} catch (SolrServerException se) {
			se.printStackTrace();
			log.error(se.getMessage());
			throw new LibraryCloudException("Internal Server Error:" + se.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
		} catch (RemoteSolrException rse) {
		if (rse.getMessage().contains("SyntaxError")) {
			log.error("solr syntax error");
			throw new LibraryCloudException("Incorrect query syntax", Response.Status.BAD_REQUEST);
		} else {
			String msg = rse.getMessage().replace("_keyword", "");
			log.error(msg);
			throw new LibraryCloudException("Incorrect query syntax:" + msg, Response.Status.BAD_REQUEST);
		}
	}
		return modsType;
	}

	/**
	 * Returns a Metadata (DublinCore) record for a given recordIdentifier.
	 * 
	 * @param id a recordIdentifier for a solr document
	 * @return the Metadata (DublinCore record) for this recordidentifier
	 * @see Metadata
	 */
	public Metadata getDublinCore(String id) throws JAXBException {
		ModsType modsType = getMods(id);
		String modsXml = marshallObject(modsType);
		String dcXml = transform(modsXml, Config.getInstance().DC_XSLT); 
		Metadata metadata = unmarshallDublinCore(dcXml);
		return metadata;
	}
	
	
	/**
	 * Returns search results for a given query, in mods format.
	 * 
	 * @param queryParams query parameters to map to a solr query
	 * @return the SearchResultsMods object for this query
	 * @see SearchResultsMods
	 */
	public SearchResultsMods getModsResults(
			MultivaluedMap<String, String> queryParams) throws JAXBException {
		SolrDocumentList docs = doQuery(queryParams);
		SearchResultsMods results = new SearchResultsMods();
		Pagination pagination = getPagination(docs,queryParams);
		ModsGroup modsGroup = getModsGroup(docs);
		results.setItemGroup(modsGroup);
		results.setPagination(pagination);
		if (facet != null)
			results.setFacet(facet);
		return results;
	}
	
	/**
	 * Returns search results for a given query, in dublin core
	 * 
	 * @param queryParams
	 * query parameters to map to a solr query
	 * @return the SearchResultsDC object for this query
	 * @see SearchResultsDC
	 */
	public SearchResultsDC getDCResults(
			MultivaluedMap<String, String> queryParams) throws JAXBException {
		SolrDocumentList docs = doQuery(queryParams);
		SearchResultsDC results = new SearchResultsDC();
		Pagination pagination = getPagination(docs,queryParams);
		DublinCoreGroup dcGroup = getDublinCoreGroup(docs);
		results.setitemGroup(dcGroup);
		results.setPagination(pagination);
		if (facet != null)
			results.setFacet(facet);
		return results;
	}
	
	private Pagination getPagination(SolrDocumentList docs, MultivaluedMap<String, String> queryParams) {
		StringBuffer sb = new StringBuffer();
		int counter = 0;
		for (String key : queryParams.keySet()) {
			String value = queryParams.getFirst(key);
			if (counter > 0)
				sb.append("&");
			sb.append(key + "=" + value);
			counter++;
		}
		String query = sb.toString();
		Pagination pagination = new Pagination();
		pagination.setNumFound(docs.getNumFound());
		pagination.setStart(docs.getStart());
		pagination.setRows(limit);
		pagination.setQuery(query);
		return pagination;
	}

	private ModsGroup getModsGroup(SolrDocumentList docs) {
		ModsGroup modsGroup = new ModsGroup();
		List<ModsType> modsList = new ArrayList<ModsType>();
		for (final SolrDocument doc : docs) {
			ModsType modsType = null;
			try {
				modsType = unmarshallModsType(doc);
			} catch (JAXBException je) {
				log.error(je.getMessage());
				je.printStackTrace();
				throw new LibraryCloudException("Internal Server Error:" + je.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
			}
			modsList.add(modsType);
		}
		modsGroup.setItems(modsList);
		return modsGroup;
	}

	private DublinCoreGroup getDublinCoreGroup(SolrDocumentList docs) {
		DublinCoreGroup dcGroup = new DublinCoreGroup();
		List<Metadata> metadataList = new ArrayList<Metadata>();
		for (final SolrDocument doc : docs) {
			ModsType modsType = null;
			Metadata metadata = null;
			try {
				modsType = unmarshallModsType(doc);
				String modsXml = marshallObject(modsType);
				String dcXml = transform(modsXml, Config.getInstance().DC_XSLT); 
				metadata = unmarshallDublinCore(dcXml);;
			} catch (JAXBException je) {
				log.error(je.getMessage());
				je.printStackTrace();
				throw new LibraryCloudException("Internal Server Error:" + je.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
			}
			metadataList.add(metadata);
		}
		dcGroup.setItems(metadataList);
		return dcGroup;
	}
	
	/**
	 * Returns search results for a given SolrDocumentList, in mods format.
	 * 
	 * @param doc solr document list to build results
	 * @return the SearchResultsMods object for this solr result
	 * @see SearchResultsMods
	 */
	private SearchResultsMods buildModsResults(SolrDocumentList docs) {
		SearchResultsMods results = new SearchResultsMods();
		Pagination pagination = new Pagination();
		pagination.setNumFound(docs.getNumFound());
		pagination.setStart(docs.getStart());
		pagination.setRows(limit);
		ModsGroup modsGroup = new ModsGroup();
		List<ModsType> modsList = new ArrayList<ModsType>();
		for (final SolrDocument doc : docs) {
			ModsType modsType = null;
			try {
				modsType = unmarshallModsType(doc);
			} catch (JAXBException je) {
				log.error(je.getMessage());
				je.printStackTrace();
				throw new LibraryCloudException("Internal Server Error:" + je.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
			}
			modsList.add(modsType);
		}
		modsGroup.setItems(modsList);
		results.setItemGroup(modsGroup);
		results.setPagination(pagination);
		if (facet != null)
			results.setFacet(facet);
		return results;
	}

	
	/**
	 * Returns search results for a given SolrDocumentList, in dublin core
	 * 
	 * @param doc solr document list to build results
	 * @return the SearchResultsDC object for this solr result
	 * @see SearchResultsSlim
	 */
	private SearchResultsDC buildDCResults(SolrDocumentList docs) {
		SearchResultsDC results = new SearchResultsDC();
		Pagination pagination = new Pagination();
		pagination.setNumFound(docs.getNumFound());
		pagination.setStart(docs.getStart());
		pagination.setRows(limit);
		// List<ModsType> modsTypes = new ArrayList<ModsType>();
		DublinCoreGroup dcGroup = new DublinCoreGroup();
		List<Metadata> metadataList = new ArrayList<Metadata>();
		for (final SolrDocument doc : docs) {
			Metadata metadata= new Metadata();
			ModsType modsType = null;
			try {
				modsType = unmarshallModsType(doc);
				String modsXml = marshallObject(modsType);
				String dcXml = transform(modsXml, Config.getInstance().DC_XSLT); 
				metadata = unmarshallDublinCore(dcXml);
			} catch (JAXBException je) {
				log.error(je.getMessage());
				je.printStackTrace();
				throw new LibraryCloudException("Internal Server Error:" + je.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
			}
			metadataList.add(metadata);
		}
		dcGroup.setItems(metadataList);
		results.setitemGroup(dcGroup);
		results.setPagination(pagination);
		if (facet != null)
			results.setFacet(facet);
		return results;
	}
	
	/**
	 * Returns a SolrDocumentList for a given set of search parameters. Search
	 * parameters are parsed and mapped into solr (solrj) query syntax, and solr
	 * query is performed.
	 * 
	 * @param queryParams
	 * query parameters to map to a solr query
	 * @return the SolrDocumentList for this query
	 * @see SolrDocumentList
	 */
	private SolrDocumentList doQuery(MultivaluedMap<String, String> queryParams) {
		SolrDocumentList docs = null;
		HttpSolrServer server = null;
		String queryStr = "";
		SolrQuery query = new SolrQuery();
		System.out.println("queryParams: " + queryParams.size());
		ArrayList<String> queryList = new ArrayList<String>();
		server = SolrServer.getSolrConnection();
		if (queryParams.size() > 0) {
			for (String key : queryParams.keySet()) {
				String value = queryParams.getFirst(key);
				System.out.println(key + " : " + queryParams.getFirst(key)
						+ "\n");
				if (key.equals("start")) {
					int startNo = Integer.parseInt(value);
					if (startNo < 0)
						startNo = 0;
					query.setStart(startNo);
				} else if (key.equals("limit")) {
					limit = Integer.parseInt(value);
					if (limit > 250) {
						limit = 250;
					}
					query.setRows(limit);
				} else if (key.equals("sort.asc") || key.equals("sort"))
					query.setSort(value, ORDER.asc);
				else if (key.equals("sort.desc"))
					query.setSort(value, ORDER.desc);
				else if (key.startsWith("facet")) {
					query.setFacet(true);
					String[] facetArray = value.split(",");
					for (String f : facetArray) {
						query.addFacetField(f);
					}
				} else {
					if (key.endsWith("_exact"))
						queryList.add(key.replace("_exact", "") + ":\"" + value
								+ "\"");
					else {
						value = value.trim();
						if (value.contains(" OR "))
							value = "(" + value.replaceAll(" OR ", "+OR+")
									+ ")";
						if (value.contains(" "))
							value = "( " + value.replaceAll(" ", " AND ") + ")";
						if (value.contains(":"))
							value = "\"" + value + "\"";
						if (key.equals("q")) {
							if (!value.equals("*")) {
								queryList.add("keyword:" + value);
							}
						} else if (!key.equals("callback"))
							queryList.add(key + "_keyword:" + value);
					}
				}
			}
			Iterator<String> it = queryList.iterator();
			while (it.hasNext()) {
				String qTerm = (String) it.next();
				System.out.print("QT: " + qTerm + "\n");
				queryStr += qTerm;
				System.out.print("QS: " + queryStr + "\n");
				if (it.hasNext())
					queryStr += " AND ";
			}
			if (queryList.size() == 0)
				queryStr = "*:*";
		} else {
			queryStr = "*:*";
		}
		System.out.print("queryStr: " + queryStr);
		query.setQuery(queryStr);
		QueryResponse response = null;
		try {
			response = server.query(query);
		} catch (SolrServerException se) {
			log.error(se.getMessage());
			throw new LibraryCloudException(se.getMessage(), Response.Status.BAD_REQUEST);
		} catch (RemoteSolrException rse) {
			if (rse.getMessage().contains("SyntaxError")) {
				log.error("solr syntax error");
				throw new LibraryCloudException("Incorrect query syntax", Response.Status.BAD_REQUEST);
			} else {
				String msg = rse.getMessage().replace("_keyword", "");
				log.error(msg);
				throw new LibraryCloudException("Incorrect query syntax:" + msg, Response.Status.BAD_REQUEST);
			}
		}
		List<FacetField> facets = response.getFacetFields();
		facet = null;
		if (facets != null) {
			facet = new Facet();
			List<FacetType> facetTypes = new ArrayList<FacetType>();
			for (FacetField facetField : facets) {
				List<FacetTerm> facetTerms = new ArrayList<FacetTerm>();
				FacetType facetType = new FacetType();
				facetType.setFacetName(facetField.getName());
				List<FacetField.Count> facetEntries = facetField.getValues();
				for (FacetField.Count fcount : facetEntries) {
					if (fcount.getCount() > 0) {
						FacetTerm facetTerm = new FacetTerm();
						facetTerm.setTermName(fcount.getName());
						facetTerm.setTermCount(fcount.getCount());
						// System.out.println(fcount.getName() + ": " +
						// fcount.getCount());
						facetTerms.add(facetTerm);
					}
				}
				facetType.setFacetTerms(facetTerms);
				facetTypes.add(facetType);
			}
			facet.setFacetTypes(facetTypes);
		}
		docs = response.getResults();
		return docs;
	}
	
	/**
	 *
	 * Returns a mods document, which is embedded in escaped xml form in the
	 * originalMods field of each solr document. This escaped string is
	 * unmarshalled (jaxb) into a loc.gov.mods.v3.ModsType (and all child
	 * elements) and returned as an individual item or added to a search result
	 * set
	 * 
	 * @param doc a SolrDocument from which to extract the mods record
	 * @return the ModsType
	 * @see ModsType
	 */
	protected ModsType unmarshallModsType(SolrDocument doc) throws JAXBException {
		String modsString = (String) doc.getFieldValue("originalMods");
		//System.out.println(modsString);
		Unmarshaller unmarshaller = JAXBHelper.context.createUnmarshaller();
		StringReader reader = new StringReader(modsString);
		ModsType modsType = (ModsType) ((JAXBElement) (unmarshaller
				.unmarshal(reader))).getValue();
		return modsType;
	}

	/**
	 *
	 * Returns a Metadata (DublinCore) document
	 * 
	 * @param dc a String containing DC XML
	 * @return the Metadata object unmarshalled from DC xml string
	 * @see Metadata
	 */	
	protected Metadata unmarshallDublinCore(String dc) throws JAXBException {
		//System.out.println(dc);
		Unmarshaller unmarshaller = JAXBHelper.context.createUnmarshaller();
		StringReader reader = new StringReader(dc);
		//Metadata metadata = (Metadata) ((JAXBElement) (unmarshaller
				//.unmarshal(reader))).getValue();
		Metadata metadata = (Metadata)unmarshaller.unmarshal(reader);
		//JAXBElement<Metadata> m = (JAXBElement<Metadata>)unmarshaller.unmarshal(reader).getValue();
		return metadata;
	}
	
	/**
	 *
	 * Transforms XML using XSLT (currently MODS to DC). 
	 * @param xmlString the XLM string
	 * @param xslFilename the XSLT
	 * @return the transformed XML String
	 */
	protected String transform(String xmlString, String xslFilename) {
		// StringWriter sw = marshallObject(obj);
		String result = null;
		try {
			StringReader reader = new StringReader(xmlString);
			StringWriter writer = new StringWriter();
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Transformer transformer = tFactory
					.newTransformer(new javax.xml.transform.stream.StreamSource(
							this.getClass().getClassLoader()
									.getResourceAsStream(xslFilename)));
			transformer.transform(new javax.xml.transform.stream.StreamSource(
					reader),
					new javax.xml.transform.stream.StreamResult(writer));
			result = writer.toString();
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			throw new LibraryCloudException("Internal Server Error:" + e.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
		}
		return result;
	}

	protected String marshallObject(Object obj) throws JAXBException {
		StringWriter sw = new StringWriter();
		String jsonString = null;
		Marshaller jaxbMarshaller = JAXBHelper.context.createMarshaller();
		jaxbMarshaller.marshal(obj, sw);
		return sw.toString();
	}

	protected String getJsonFromXml(String xml) {
	    XMLSerializer serializer = new XMLSerializer();  
	    serializer.setSkipNamespaces(true);
	    serializer.setRemoveNamespacePrefixFromElements(true);
	    serializer.setTypeHintsEnabled(false);
	    JSON json = serializer.read( xml );
	    return json.toString();
	}
	protected String fixPaginationAndFacets(String resultsString) {
		resultsString = resultsString.replace("<numFound>", "<numFound type=\"number\">");
		resultsString = resultsString.replace("<limit>", "<limit type=\"number\">");
		resultsString = resultsString.replace("<start>", "<start type=\"number\">");
		resultsString = resultsString.replace("<count>", "<count type=\"number\">");
		return resultsString;
	}
	
	// deprecated, keep for now - this is how we would provide access to
	// individual solr fields
	// but we are instead displaying the embedded mods record in 1 solr field;
	// the old Item class (individual fields) has been moved to SolrItem for
	// possible future use
	private Item setItem(SolrDocument doc) {
		Item item = new Item();
		/*
		 * for (Map.Entry<String, Object> entry :
		 * doc.getFieldValueMap().entrySet()) {
		 * System.out.println(entry.getKey() + "/" + entry.getValue()); }
		 */
		// item.setMods((String) doc.getFieldValue("originalMods"));
		/*
		 * item.setOriginal((String) doc.getFieldValue("original"));
		 * item.setAbstractTOC((ArrayList) doc.getFieldValue("abstractTOC"));
		 * item.setClassification((ArrayList)
		 * doc.getFieldValue("classification"));
		 * item.setCopyrightDate((ArrayList)
		 * doc.getFieldValue("copyrightDate")); item.setDateCaptured((ArrayList)
		 * doc.getFieldValue("dateCaptured")); item.setDateCreated((ArrayList)
		 * doc.getFieldValue("dateCreated")); item.setDateIssued((ArrayList)
		 * doc.getFieldValue("dateIssued")); item.setEdition((String)
		 * doc.getFieldValue("edition")); item.setGenre((String)
		 * doc.getFieldValue("genre")); item.setIdentifier((ArrayList)
		 * doc.getFieldValue("identifier")); item.setLanguageCode((ArrayList)
		 * doc.getFieldValue("languageCode")); item.setName((ArrayList)
		 * doc.getFieldValue("name")); item.setOriginDate((String)
		 * doc.getFieldValue("originDate")); item.setOriginPlace((ArrayList)
		 * doc.getFieldValue("originPlace"));
		 * item.setPhysicalDescription((String)
		 * doc.getFieldValue("physicalDescription")); item.setPublisher((String)
		 * doc.getFieldValue("publisher")); item.setRecordIdentifier((String)
		 * doc.getFieldValue("recordIdentifier")); item.setResourceType((String)
		 * doc.getFieldValue("resourceType")); item.setRole((ArrayList)
		 * doc.getFieldValue("role")); item.setSource((String)
		 * doc.getFieldValue("source")); item.setSubjectPlace((ArrayList)
		 * doc.getFieldValue("subjectPlace")); item.setTitle((String)
		 * doc.getFieldValue("title"));
		 */
		return item;
	}

	// deprecated - use unmarshallModsType instead, rewrite and use this if we decide
	// to wrap
	// the mods document in an <item> tag
	public Item getItem(String id) {
		SolrDocumentList docs;
		SolrDocument doc;
		Item item = new Item();
		HttpSolrServer server = null;
		try {
			server = SolrServer.getSolrConnection();
			SolrQuery query = new SolrQuery("recordIdentifier:" + id);
			QueryResponse response = server.query(query);
			docs = response.getResults();
			if (docs.size() == 0)
				item = null;
			else {
				doc = docs.get(0);
				item = setItem(doc);
			}
		} catch (SolrServerException se) {
			log.error(se.getMessage());
			se.printStackTrace();
			throw new LibraryCloudException("Internal Server Error:" + se.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
		}
		return item;
	}
}