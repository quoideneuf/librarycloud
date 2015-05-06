package edu.harvard.lib.librarycloud.items;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import edu.harvard.lib.librarycloud.items.Pagination;
import gov.loc.mods.v3.ModsType;

public class TestRecords {

    public TestRecords(){
    }
    
	protected static String recOne = "<?xml version=\"1.0\"?><mods xmlns=\"http://www.loc.gov/mods/v3\" version=\"3.4\"><titleInfo><title>Peanut research</title></titleInfo><name type=\"corporate\"><namePart>National Peanut Council</namePart></name><name type=\"corporate\"><namePart>Georgia Agricultural Commodity Commission for Peanuts</namePart></name><name type=\"corporate\"><namePart>American Peanut Research and Education Association</namePart></name><name type=\"corporate\"><namePart>American Peanut Research and Education Society</namePart></name><typeOfResource>text</typeOfResource><genre authority=\"marcgt\">periodical</genre><originInfo><place><placeTerm type=\"code\" authority=\"marccountry\">gau</placeTerm></place><place><placeTerm type=\"text\">Tifton, Ga</placeTerm></place><publisher>American Peanut Research and Education Society</publisher><dateIssued encoding=\"marc\" point=\"start\">1963</dateIssued><dateIssued encoding=\"marc\" point=\"end\">9999</dateIssued><issuance>serial</issuance><frequency authority=\"marcfrequency\">Quarterly, May/Aug. 1978-</frequency><frequency authority=\"marcfrequency\">Bimonthly, -Mar./Apr. l978</frequency></originInfo><frequency>Quarterly</frequency><language><languageTerm authority=\"iso639-2b\" type=\"code\">eng</languageTerm></language><physicalDescription><form authority=\"marcform\">print</form><extent>v. : ill. ; 28 cm.</extent></physicalDescription><note type=\"statement of responsibility\" altRepGroup=\"00\">APRES.</note><note type=\"date/sequential designation\">Began in May 1963.</note><note>Description based on: Vol. 21, issue 87 (July-Sept. 1983); title from caption.</note><note>Vols. for 1963-July 1972 issued by: National Peanut Council; Sept. 1972-Nov. 1973 by: Georgia Agricultural Commodity Commission for Peanuts, for: American Peanut Research and Educational Association; Jan. 1974-Apr./June 1979 by: American Peanut Research and Education Association; July/Sept. 1979- by: American Peanut Research and Education Society.</note><subject authority=\"lcsh\"><name type=\"corporate\"><namePart>American Peanut Research and Education Society</namePart></name><genre>Periodicals</genre></subject><subject authority=\"lcsh\"><topic>Peanuts</topic><genre>Periodicals</genre></subject><identifier type=\"issn\">0479-7558</identifier><identifier type=\"lccn\">sn 83010504</identifier><recordInfo><descriptionStandard>aacr</descriptionStandard><recordContentSource authority=\"marcorg\">FUL</recordContentSource><recordCreationDate encoding=\"marc\">760830</recordCreationDate><recordChangeDate encoding=\"iso8601\">20110506145932.0</recordChangeDate><recordIdentifier>000373965-1</recordIdentifier><recordOrigin>" + 
			"Converted from MARCXML to MODS version 3.4 using MARC21slim2MODS3-4.xsl (Revision 1.86 2013/06/10)</recordOrigin></recordInfo><location><shelfLocator>PER</shelfLocator><physicalLocation>ECB</physicalLocation></location></mods>";

	protected static String recTwo = "<?xml version=\"1.0\"?><mods xmlns=\"http://www.loc.gov/mods/v3\" version=\"3.4\"><titleInfo><title>Test record 2</title></titleInfo><name type=\"corporate\"><namePart>National Peanut Council</namePart></name><name type=\"corporate\"><namePart>Test Rec 2 name</namePart></name><name type=\"corporate\"><namePart>American Peanut Research and Education Association</namePart></name><name type=\"corporate\"><namePart>American Peanut Research and Education Society</namePart></name><typeOfResource>text</typeOfResource><genre authority=\"marcgt\">periodical</genre><originInfo><place><placeTerm type=\"code\" authority=\"marccountry\">gau</placeTerm></place><place><placeTerm type=\"text\">Tifton, Ga</placeTerm></place><publisher>American Peanut Research and Education Society</publisher><dateIssued encoding=\"marc\" point=\"start\">1963</dateIssued><dateIssued encoding=\"marc\" point=\"end\">9999</dateIssued><issuance>serial</issuance><frequency authority=\"marcfrequency\">Quarterly, May/Aug. 1978-</frequency><frequency authority=\"marcfrequency\">Bimonthly, -Mar./Apr. l978</frequency></originInfo><frequency>Quarterly</frequency><language><languageTerm authority=\"iso639-2b\" type=\"code\">eng</languageTerm></language><physicalDescription><form authority=\"marcform\">print</form><extent>v. : ill. ; 28 cm.</extent></physicalDescription><note type=\"statement of responsibility\" altRepGroup=\"00\">APRES.</note><note type=\"date/sequential designation\">Began in May 1963.</note><note>Description based on: Vol. 21, issue 87 (July-Sept. 1983); title from caption.</note><note>Vols. for 1963-July 1972 issued by: National Peanut Council; Sept. 1972-Nov. 1973 by: Georgia Agricultural Commodity Commission for Peanuts, for: American Peanut Research and Educational Association; Jan. 1974-Apr./June 1979 by: American Peanut Research and Education Association; July/Sept. 1979- by: American Peanut Research and Education Society.</note><subject authority=\"lcsh\"><name type=\"corporate\"><namePart>American Peanut Research and Education Society</namePart></name><genre>Periodicals</genre></subject><subject authority=\"lcsh\"><topic>Peanuts</topic><genre>Periodicals</genre></subject><identifier type=\"issn\">0479-7558</identifier><identifier type=\"lccn\">sn 83010504</identifier><recordInfo><descriptionStandard>aacr</descriptionStandard><recordContentSource authority=\"marcorg\">FUL</recordContentSource><recordCreationDate encoding=\"marc\">760830</recordCreationDate><recordChangeDate encoding=\"iso8601\">20110506145932.0</recordChangeDate><recordIdentifier>000373965-1</recordIdentifier><recordOrigin>" + 
			"Converted from MARCXML to MODS version 3.4 using MARC21slim2MODS3-4.xsl (Revision 1.86 2013/06/10)</recordOrigin></recordInfo><location><shelfLocator>PER</shelfLocator><physicalLocation>ECB</physicalLocation></location></mods>";

	protected static String recThree = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><mods xmlns=\"http://www.loc.gov/mods/v3\" xmlns:ns2=\"http://www.w3.org/1999/xlink\"><titleInfo type=\"uniform\" displayLabel=\"Label Me Good\"><title>A good one.</title><partName>The First Part</partName><partNumber>2</partNumber><nonSort>The</nonSort><subTitle>sub</subTitle></titleInfo><name type=\"personal\"><description>This guy is awesome</description><role><roleTerm type=\"text\">author</roleTerm></role><affiliation>ASU</affiliation><displayForm>Mr. Matty Matt</displayForm><namePart type=\"family\">Cordial</namePart><namePart type=\"given\">Matthew</namePart></name><typeOfResource collection=\"yes\">text</typeOfResource><typeOfResource manuscript=\"yes\" collection=\"yes\">cartographic</typeOfResource><genre>GoodThings</genre><originInfo><place><placeTerm type=\"text\">Phoenix</placeTerm></place><edition>First</edition><issuance>continuing</issuance><frequency>yearly</frequency><publisher>Stinky Feet Press</publisher><dateCreated keyDate=\"yes\" encoding=\"iso8601\">2014-04-18</dateCreated><dateOther type=\"completed!!\" qualifier=\"approximate\">77</dateOther></originInfo><language><languageTerm type=\"code\" authority=\"iso639-3\">en</languageTerm></language><physicalDescription><digitalOrigin>digitized other analog</digitalOrigin><internetMediaType>text/plain</internetMediaType><extent>300 pages</extent><reformattingQuality>access</reformattingQuality><form>Lincoln Logs</form><note>This is a really pretty thing.</note></physicalDescription><abstract>Wow this should be really great.</abstract><tableOfContents displayLabel=\"Mine and Yours\">1) mystuff 2)Your stuff</tableOfContents><targetAudience>Dev Folks</targetAudience><note>This is not a bogus record. Really, it is useful.</note><subject><topic>bears</topic></subject><subject><topic>honey</topic></subject><subject><topic>helmets</topic></subject><subject><name type=\"corporate\"><namePart>Fly By Night Productions</namePart></name><titleInfo><title>2012 The Musical</title></titleInfo></subject><subject><cartographics><scale>feet</scale><coordinates>N: 1250</coordinates><coordinates>E: 468</coordinates><coordinates>S: 34</coordinates><coordinates>W: 6634</coordinates></cartographics></subject><subject><temporal point=\"start\">200</temporal></subject><subject><temporal>modern era</temporal></subject><subject><genre>fishing tales</genre></subject><subject><geographic>Southwestern</geographic><geographic>Arizona</geographic></subject><subject><occupation>Engineer</occupation></subject><subject><geographicCode authority=\"marccountry\">USA</geographicCode><hierarchicalGeographic><country>USA</country><state>Ohio</state><county>Montgomery</county><city>Dayton</city></hierarchicalGeographic></subject><classification>stellar</classification><relatedItem type=\"host\"><genre>scarry</genre><identifier type=\"fedora pid\">asulib:4</identifier><relatedItem><abstract>iiiiiiiinnnnnnnnnnyyyyyyyouuuuu</abstract></relatedItem></relatedItem><identifier>long:34</identifier><location><physicalLocation>My House.</physicalLocation><shelfLocator>dd554</shelfLocator><url usage=\"primary display\">http://drs.asu.edu</url><holdingSimple><copyInformation><form>paper</form><subLocation>the sink</subLocation><enumerationAndChronology>50</enumerationAndChronology></copyInformation></holdingSimple></location><accessCondition>if you touch it I will eat your brain</accessCondition><part type=\"host\"><extent unit=\"pages\"><start>20</start><end>30</end><total>10</total></extent><detail type=\"issue\"><number>3</number><title>The Long Running Series</title></detail><date xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"dateType\" encoding=\"iso8601\">2014-04</date><text>Lots of stuff in this chapter was created by monkeys.</text></part><recordInfo><descriptionStandard>my own</descriptionStandard><languageOfCataloging><languageTerm type=\"code\" authority=\"iso639-2b\">en</languageTerm></languageOfCataloging><recordCreationDate>2008</recordCreationDate><recordOrigin>My BRAIN</recordOrigin><recordIdentifier>hiBrainHi</recordIdentifier></recordInfo></mods>\"			\"Converted from MARCXML to MODS version 3.4 using MARC21slim2MODS3-4.xsl (Revision 1.86 2013/06/10)</recordOrigin></recordInfo><location><shelfLocator>PER</shelfLocator><physicalLocation>ECB</physicalLocation></location></mods>";	
	
    protected Pagination pagination;
   
    public Pagination getMockPagination() {
    	pagination = new Pagination();
    	pagination.setNumFound(2);
    	pagination.setStart(0);
    	pagination.setRows(10);
    	return pagination;
    }
  
    public ModsType getMockModsType() throws JAXBException {
    	JAXBContext jc = JAXBContext.newInstance(ModsType.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringReader reader = new StringReader(recTwo);
        ModsType modsType = (ModsType) ((JAXBElement)(unmarshaller.unmarshal(reader))).getValue();
        return modsType;
    }
	
}