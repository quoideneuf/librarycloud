//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-661 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.02.02 at 03:34:01 PM MST 
//


package edu.harvard.lib.librarycloud.items.mods;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for relatedItemType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relatedItemType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;group ref="{http://www.loc.gov/mods/v3}modsGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;attGroup ref="{http://www.w3.org/1999/xlink}simpleLink"/>
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="preceding"/>
 *             &lt;enumeration value="succeeding"/>
 *             &lt;enumeration value="original"/>
 *             &lt;enumeration value="host"/>
 *             &lt;enumeration value="constituent"/>
 *             &lt;enumeration value="series"/>
 *             &lt;enumeration value="otherVersion"/>
 *             &lt;enumeration value="otherFormat"/>
 *             &lt;enumeration value="isReferencedBy"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="displayLabel" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="otherType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relatedItemType", propOrder = {
    "modsGroup"
})
public class RelatedItemType {

    @XmlElements({
        @XmlElement(name = "accessCondition", type = AccessConditionType.class),
        @XmlElement(name = "subject", type = SubjectType.class),
        @XmlElement(name = "relatedItem", type = RelatedItemType.class),
        @XmlElement(name = "abstract", type = AbstractType.class),
        @XmlElement(name = "identifier", type = IdentifierType.class),
        @XmlElement(name = "name", type = NameType.class),
        @XmlElement(name = "extension", type = ExtensionType.class),
        @XmlElement(name = "targetAudience", type = TargetAudienceType.class),
        @XmlElement(name = "note", type = NoteType.class),
        @XmlElement(name = "recordInfo", type = RecordInfoType.class),
        @XmlElement(name = "typeOfResource", type = TypeOfResourceType.class),
        @XmlElement(name = "location", type = LocationType.class),
        @XmlElement(name = "part", type = PartType.class),
        @XmlElement(name = "language", type = LanguageType.class),
        @XmlElement(name = "originInfo", type = OriginInfoType.class),
        @XmlElement(name = "genre", type = GenreType.class),
        @XmlElement(name = "titleInfo", type = TitleInfoType.class),
        @XmlElement(name = "classification", type = ClassificationType.class),
        @XmlElement(name = "tableOfContents", type = TableOfContentsType.class),
        @XmlElement(name = "physicalDescription", type = PhysicalDescriptionType.class)
    })
    protected List<Object> modsGroup;
    @XmlAttribute(name = "type")
    protected String riType;
    @XmlAttribute
    protected String displayLabel;
    @XmlAttribute
    protected String otherType;

    @XmlAttribute(name = "ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String type;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    @XmlSchemaType(name = "anyURI")
    protected String href;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String role;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String arcrole;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String title;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String show;
    @XmlAttribute(namespace = "http://www.w3.org/1999/xlink")
    protected String actuate;

    /**
     * Gets the value of the modsGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the modsGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getModsGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccessConditionType }
     * {@link SubjectType }
     * {@link RelatedItemType }
     * {@link AbstractType }
     * {@link IdentifierType }
     * {@link NameType }
     * {@link ExtensionType }
     * {@link TargetAudienceType }
     * {@link NoteType }
     * {@link RecordInfoType }
     * {@link TypeOfResourceType }
     * {@link LocationType }
     * {@link PartType }
     * {@link LanguageType }
     * {@link OriginInfoType }
     * {@link GenreType }
     * {@link TitleInfoType }
     * {@link ClassificationType }
     * {@link TableOfContentsType }
     * {@link PhysicalDescriptionType }
     * 
     * 
     */
    public List<Object> getModsGroup() {
        if (modsGroup == null) {
            modsGroup = new ArrayList<Object>();
        }
        return this.modsGroup;
    }

    /**
     * Gets the value of the riType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiType() {
        return riType;
    }

    /**
     * Sets the value of the riType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiType(String value) {
        this.riType = value;
    }

    /**
     * Gets the value of the displayLabel property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDisplayLabel() {
        return displayLabel;
    }

    /**
     * Sets the value of the displayLabel property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDisplayLabel(String value) {
        this.displayLabel = value;
    }


    /**
     * Gets the value of the otherType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOtherType() {
        return otherType;
    }

    /**
     * Sets the value of the otherType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOtherType(String value) {
        this.otherType = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        if (type == null) {
            return "simple";
        } else {
            return type;
        }
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the href property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHref() {
        return href;
    }

    /**
     * Sets the value of the href property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHref(String value) {
        this.href = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the arcrole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArcrole() {
        return arcrole;
    }

    /**
     * Sets the value of the arcrole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArcrole(String value) {
        this.arcrole = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the show property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShow() {
        return show;
    }

    /**
     * Sets the value of the show property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShow(String value) {
        this.show = value;
    }

    /**
     * Gets the value of the actuate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActuate() {
        return actuate;
    }

    /**
     * Sets the value of the actuate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActuate(String value) {
        this.actuate = value;
    }

}
