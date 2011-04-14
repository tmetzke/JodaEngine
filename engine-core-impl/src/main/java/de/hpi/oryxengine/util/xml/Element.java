package de.hpi.oryxengine.util.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;

import de.hpi.oryxengine.exception.DalmatinaRuntimeException;

/**
 * Represents one XML element. * 
 */
public class Element {

    protected String uri;
    protected String tagName;

    /**
     * This {@link Map} stores the attributes of the XML.
     * 
     * The Key of the {@link Map} consists of the following: nsUri + : + attributeName
     * E.g. xsl:id , see http://www.w3.org/TR/xml-names/#uniqAttrs
     * 
     * If namespace is empty, key is only the attributeName, e.g. id .
     */
    protected Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();

    protected int line;
    protected int column;
    protected StringBuilder text = new StringBuilder();
    protected List<Element> elements = new ArrayList<Element>();

    public Element(String uri, String localName, String qName, Attributes attributes, Locator locator) {

        this.uri = uri;
        if (uri == null || uri.isEmpty()) {
            this.tagName = qName;
        } else {
            this.tagName = localName;
        }

        if (attributes != null) {
            for (int i = 0; i < attributes.getLength(); i++) {

                String attributeUri = attributes.getURI(i);
                String value = attributes.getValue(i);
                String name;
                if (attributeUri == null || attributeUri.isEmpty()) {
                    name = attributes.getQName(i);
                } else {
                    name = attributes.getLocalName(i);
                }
                this.attributeMap.put(composeMapKey(attributeUri, name), new Attribute(name, value, attributeUri));
            }
        }

        if (locator != null) {
            line = locator.getLineNumber();
            column = locator.getColumnNumber();
        }
    }

    public List<Element> getElements(String tagName) {

        return getElementsNS(null, tagName);
    }

    public List<Element> getElementsNS(String nameSpaceUri, String tagName) {

        List<Element> selectedElements = new ArrayList<Element>();
        for (Element element : elements) {
            if (tagName.equals(element.getTagName())) {
                if (nameSpaceUri == null || (nameSpaceUri != null && nameSpaceUri.equals(element.getUri()))) {
                    selectedElements.add(element);
                }
            }
        }
        return selectedElements;
    }

    public Element getElement(String tagName) {

        return getElementNS(null, tagName);
    }

    public Element getElementNS(String nameSpaceUri, String tagName) {

        List<Element> elements = getElementsNS(nameSpaceUri, tagName);
        if (elements.size() == 0) {
            return null;
        } else if (elements.size() > 1) {
            String errorMessage = "Parsing exception: multiple elements with tag name " + tagName + " found.";
            throw new DalmatinaRuntimeException(errorMessage);
        }
        return elements.get(0);
    }

    public void add(Element element) {

        elements.add(element);
    }

    public String getAttribute(String name) {

        if (attributeMap.containsKey(name)) {
            return attributeMap.get(name).getValue();
        }
        return null;
    }

    public Set<String> getAttributes() {

        return attributeMap.keySet();
    }

    public String getAttributeNS(String namespaceUri, String name) {

        return getAttribute(composeMapKey(namespaceUri, name));
    }

    public String getAttribute(String name, String defaultValue) {

        if (attributeMap.containsKey(name)) {
            return attributeMap.get(name).getValue();
        }
        return defaultValue;
    }

    public String getAttributeNS(String namespaceUri, String name, String defaultValue) {

        return getAttribute(composeMapKey(namespaceUri, name), defaultValue);
    }

    protected String composeMapKey(String attributeUri, String attributeName) {

        StringBuilder strb = new StringBuilder();
        if (attributeUri != null && !attributeUri.isEmpty()) {
            strb.append(attributeUri);
            strb.append(":");
        }
        strb.append(attributeName);
        return strb.toString();
    }

    public List<Element> getElements() {

        return elements;
    }

    public String toString() {

        return "<" + tagName + "...";
    }

    public String getUri() {

        return uri;
    }

    public String getTagName() {

        return tagName;
    }

    public int getLine() {

        return line;
    }

    public int getColumn() {

        return column;
    }

    /**
     * Appends a certain {@link String} to the text of this {@link Element}.
     * 
     * Due to the nature of SAX parsing, sometimes the characters of an element are not processed at once. So instead of
     * a setText operation, we need to have an appendText operation.
     *
     * @param textToBeAppended - the {@link String} that will be appended.
     */
    public void appendText(String textToBeAppended) {

        this.text.append(textToBeAppended);
    }

    public String getText() {

        return text.toString();
    }
}
