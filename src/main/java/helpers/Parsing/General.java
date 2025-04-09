package helpers.Parsing;

import org.w3c.dom.*;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/***
 * parsing helpers
 * @author Oliwia Daaszczynska
 */
public class General {
    /***
     * document builder factory
     * @param file file to build
     * @return built document
     * @author Oliwia Daszcynksa
     */
    public static Document buildXMLDocument(File file) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /***
     * read sitemap and extract relevant links to plenary protocols of 20th and 21st legislative period
     * @param path to sitemap
     * @return set of urls - in each one is a xml file
     * @author: Oliwia Daszcnyksa
     */
    public static Set<String> extractProtocolURL(String path) {
        Set<String> xmlUrls = new HashSet<>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean loc = false;

                public void startElement(String uri, String localName, String qName, Attributes attributes) {
                    if (qName.equals("loc")) loc = true; // check for <loc> in sitemap
                }

                public void characters(char[] ch, int start, int length) {
                    if (loc) {
                        String url = new String(ch, start, length);
                        if (url.startsWith("https://www.bundestag.de/resource/blob/") &&
                                url.endsWith(".xml") && // filter out non-xml links
                                url.substring(url.lastIndexOf('/') + 1).matches("20...\\.xml")
                            ) {
                            xmlUrls.add(url);
                        }
                        loc = false;
                    }
                }
            };

            // parse sitemap
            saxParser.parse(new File(path), handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlUrls;
    }

    /***
     * extracts content of the specified occurrence of node.
     * @param node xml tree node
     * @param tag node name
     * @param getLast use true if you want to get the last occurrence. false if you want the first.
     * @return node content
     * @author Oliwia Daszcynksa
     */
    public static String getTagContent(Node node, String tag, boolean getLast) {
        if (node instanceof Document) {
            node = ((Document) node).getDocumentElement();
        }
        NodeList nodeList = ((Element) node).getElementsByTagName(tag);
        int j = (nodeList.getLength()); // occurrences

        if (j > 0) {
            if (getLast) {
                return nodeList.item(j - 1).getTextContent().trim(); // last occurrence
            }
            return nodeList.item(0).getTextContent().trim(); // first occurrence
        }
        return "";
    }

    /***
     * extracts content of specified occurrence of the node.
     * The version without getLast boolean returns the first occurrence always.
     * @param node xml tree node
     * @param tag node name
     * @return node content
     * @author Oliwia Daszcynksa
     */
    public static String getTagContent(Node node, String tag) {
        return getTagContent(node, tag, false); // default to false
    }

    /***
     * extracts first occurrence of tag name and returns attribute for a node.
     * @param node node as node
     * @param tag name as string
     * @param attribute name as string
     * @return attribute content
     * @author Oliwia Daszcynksa
     */
    public static String getAttribute(Node node, String tag, String attribute) {
        if (node instanceof Document) {
            node = ((Document) node).getDocumentElement();
        }
        return ((Element) ((Element) node).getElementsByTagName(tag).item(0)).getAttribute(attribute).trim();
    }

    /**
     * Checks if the given node matches the specified tag name.
     *
     * @param node the Node to be checked
     * @param tag  the String representation of the tag to compare
     * @return true if the node's name matches the tag, otherwise false
     * @author Genadij Vorontsov
     */
    public static Boolean isNodeTag(Node node, String tag) {
        return node.getNodeName().equals(tag);
    }

    /**
     * Retrieves all nodes from the given Document with the specified tag.
     *
     * @param tag the String name of the tag to find
     * @param doc the Document in which to search for the tag
     * @return a NodeList containing all nodes with the specified tag name
     * @author Genadij Vorontsov
     */
    public static NodeList getNodesByTag(String tag, Document doc) {
        return doc.getDocumentElement().getElementsByTagName(tag);
    }

    /**
     * Finds the first node in the given Document matching the specified tag.
     *
     * @param tag the String name of the tag to find
     * @param doc the Document in which to search for the tag
     * @return the first Node that matches the specified tag, or null if no matching node was found
     * @author Genadij Vorontsov
     */
    public static Node findFirstNode(String tag, Document doc) {
        return getNodesByTag(tag, doc).item(0);
    }

    /**
     * Safely retrieves the value of a specific attribute from a NamedNodeMap.
     *
     * @param attributes a NamedNodeMap containing attribute nodes
     * @param attribute  the String name of the attribute to retrieve
     * @return the attribute’s value as a String, or an empty String if the attribute is not found
     * @author Genadij Vorontsov
     */
    public static String getNodeAttribute(NamedNodeMap attributes, String attribute) {
        Node n = attributes.getNamedItem(attribute);
        if (n == null)
            return "";

        return n.getNodeValue();
    }

    /**
     * Generates a List of org.bson.Document objects representing sentence information.
     * Each Document contains the "id", "begin", and "end" attributes extracted from the NodeList.
     *
     * @param sentences the NodeList containing sentence nodes
     * @return a List of org.bson.Document objects representing the processed sentences
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedSentences(NodeList sentences) {
        List<org.bson.Document> docSentences = new ArrayList<org.bson.Document>();
        for (int i = 0; i < sentences.getLength(); i++) {
            NamedNodeMap sentenceAttributes = sentences.item(i).getAttributes();
            org.bson.Document docSentence = new org.bson.Document();

            docSentence.append("id", getNodeAttribute(sentenceAttributes, "xmi:id"));
            docSentence.append("begin", getNodeAttribute(sentenceAttributes, "begin"));
            docSentence.append("end", getNodeAttribute(sentenceAttributes, "end"));
            docSentences.add(docSentence);
        }
        return docSentences;
    }

    /**
     * Generates a List of org.bson.Document objects representing token information.
     * Each Document contains the "id", "begin", "end", "parent", "lemma", and "pos" attributes extracted from the NodeList.
     *
     * @param tokens the NodeList containing token nodes
     * @return a List of org.bson.Document objects representing the processed tokens
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedTokens(NodeList tokens) {
        List<org.bson.Document> docTokens = new ArrayList<org.bson.Document>();
        for (int i = 0; i < tokens.getLength(); i++) {
            NamedNodeMap tokenAttributes = tokens.item(i).getAttributes();
            org.bson.Document docToken = new org.bson.Document();

            docToken.append("id", getNodeAttribute(tokenAttributes, "xmi:id"));
            docToken.append("begin", getNodeAttribute(tokenAttributes, "begin"));
            docToken.append("end", getNodeAttribute(tokenAttributes, "end"));
            docToken.append("parent", getNodeAttribute(tokenAttributes, "parent"));
            docToken.append("lemma", getNodeAttribute(tokenAttributes, "lemma"));
            docToken.append("pos", getNodeAttribute(tokenAttributes, "pos"));
            docTokens.add(docToken);
        }
        return docTokens;
    }

    /**
     * Generates a List of org.bson.Document objects representing Part-of-Speech (POS) tag information.
     * Each Document contains the "id", "begin", "end", "PosValue", and "coarseValue" attributes extracted from the NodeList.
     *
     * @param posTags the NodeList containing POS tag nodes
     * @return a List of org.bson.Document objects representing the processed POS tags
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedPosTags(NodeList posTags) {
        List<org.bson.Document> docPosTags = new ArrayList<org.bson.Document>();
        for (int i = 0; i < posTags.getLength(); i++) {
            NamedNodeMap posTagAttributes = posTags.item(i).getAttributes();
            org.bson.Document docPosTag = new org.bson.Document();

            docPosTag.append("id", getNodeAttribute(posTagAttributes, "xmi:id"));
            docPosTag.append("begin", getNodeAttribute(posTagAttributes, "begin"));
            docPosTag.append("end", getNodeAttribute(posTagAttributes, "end"));
            docPosTag.append("PosValue", getNodeAttribute(posTagAttributes, "PosValue"));
            docPosTag.append("coarseValue", getNodeAttribute(posTagAttributes, "coarseValue"));
            docPosTags.add(docPosTag);
        }
        return docPosTags;
    }

    /**
     * Generates a List of org.bson.Document objects representing lemma information.
     * Each Document contains the "id", "begin", "end", and "value" attributes extracted from the NodeList.
     *
     * @param lemmas the NodeList containing lemma nodes
     * @return a List of org.bson.Document objects representing the processed lemmas
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedLemmas(NodeList lemmas) {
        List<org.bson.Document> lemmaTags = new ArrayList<org.bson.Document>();
        for (int i = 0; i < lemmas.getLength(); i++) {
            NamedNodeMap lemmaAttributes = lemmas.item(i).getAttributes();
            org.bson.Document lemmaTag = new org.bson.Document();

            lemmaTag.append("id", getNodeAttribute(lemmaAttributes, "xmi:id"));
            lemmaTag.append("begin", getNodeAttribute(lemmaAttributes, "begin"));
            lemmaTag.append("end", getNodeAttribute(lemmaAttributes, "end"));
            lemmaTag.append("value", getNodeAttribute(lemmaAttributes, "value"));
            lemmaTags.add(lemmaTag);
        }
        return lemmaTags;
    }

    /**
     * Generates a List of org.bson.Document objects representing dependency information.
     * Each Document contains the "id", "begin", "end", "Governor", "Dependent", and "DependencyType" attributes extracted from the NodeList.
     *
     * @param dependencies the NodeList containing dependency nodes
     * @return a List of org.bson.Document objects representing the processed dependencies
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedDependencies(NodeList dependencies) {
        List<org.bson.Document> dependencyTags = new ArrayList<org.bson.Document>();
        for (int i = 0; i < dependencies.getLength(); i++) {
            NamedNodeMap dependencyAttributes = dependencies.item(i).getAttributes();
            org.bson.Document dependencyTag = new org.bson.Document();

            dependencyTag.append("id", getNodeAttribute(dependencyAttributes, "xmi:id"));
            dependencyTag.append("begin", getNodeAttribute(dependencyAttributes, "begin"));
            dependencyTag.append("end", getNodeAttribute(dependencyAttributes, "end"));
            dependencyTag.append("Governor", getNodeAttribute(dependencyAttributes, "Governor"));
            dependencyTag.append("Dependent", getNodeAttribute(dependencyAttributes, "Dependent"));
            dependencyTag.append("DependencyType", getNodeAttribute(dependencyAttributes, "DependencyType"));
            dependencyTags.add(dependencyTag);
        }
        return dependencyTags;
    }

    /**
     * Generates a List of org.bson.Document objects representing named entities.
     * Each Document contains the "id", "begin", "end", and "value" attributes extracted from the NodeList.
     *
     * @param namedEntities the NodeList containing named entity nodes
     * @return a List of org.bson.Document objects representing the processed named entities
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedNamedEntities(NodeList namedEntities) {
        List<org.bson.Document> namedEntityTags = new ArrayList<org.bson.Document>();
        for (int i = 0; i < namedEntities.getLength(); i++) {
            NamedNodeMap namedEntityAttributes = namedEntities.item(i).getAttributes();
            org.bson.Document namedEntityTag = new org.bson.Document();

            namedEntityTag.append("id", getNodeAttribute(namedEntityAttributes, "xmi:id"));
            namedEntityTag.append("begin", getNodeAttribute(namedEntityAttributes, "begin"));
            namedEntityTag.append("end", getNodeAttribute(namedEntityAttributes, "end"));
            namedEntityTag.append("value", getNodeAttribute(namedEntityAttributes, "value"));
            namedEntityTags.add(namedEntityTag);
        }
        return namedEntityTags;
    }

    /**
     * Generates a List of org.bson.Document objects representing topic information.
     * Each Document contains the "id", "begin", "end", "value", and "score" attributes extracted from the NodeList.
     *
     * @param topics the NodeList containing topic nodes
     * @return a List of org.bson.Document objects representing the processed topics
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedTopics(NodeList topics) {
        List<org.bson.Document> topicTags = new ArrayList<>();
        for (int i = 0; i < topics.getLength(); i++) {
            NamedNodeMap topicAttributes = topics.item(i).getAttributes();
            org.bson.Document topicTag = new org.bson.Document();

            topicTag.append("id", getNodeAttribute(topicAttributes, "xmi:id"));
            topicTag.append("begin", getNodeAttribute(topicAttributes, "begin"));
            topicTag.append("end", getNodeAttribute(topicAttributes, "end"));
            topicTag.append("value", getNodeAttribute(topicAttributes, "value"));
            topicTag.append("score", getNodeAttribute(topicAttributes, "score"));
            topicTags.add(topicTag);
        }
        return topicTags;
    }

    /**
     * Generates a List of org.bson.Document objects representing sentiment information.
     * Each Document contains the "id", "begin", "end", "sentiment", "pos", "neu", and "neg" attributes extracted from the NodeList.
     *
     * @param sentiments the NodeList containing sentiment nodes
     * @return a List of org.bson.Document objects representing the processed sentiment information
     * @author Genadij Vorontsov
     */
    public static List<org.bson.Document> generateProcessedSentiments(NodeList sentiments) {
        List<org.bson.Document> sentimentTags = new ArrayList<>();
        for (int i = 0; i < sentiments.getLength(); i++) {
            NamedNodeMap sentimentAttributes = sentiments.item(i).getAttributes();
            org.bson.Document sentimentTag = new org.bson.Document();

            sentimentTag.append("id", getNodeAttribute(sentimentAttributes, "xmi:id"));
            sentimentTag.append("begin", getNodeAttribute(sentimentAttributes, "begin"));
            sentimentTag.append("end", getNodeAttribute(sentimentAttributes, "end"));
            sentimentTag.append("sentiment", getNodeAttribute(sentimentAttributes, "sentiment"));
            sentimentTag.append("pos", getNodeAttribute(sentimentAttributes, "pos"));
            sentimentTag.append("neu", getNodeAttribute(sentimentAttributes, "neu"));
            sentimentTag.append("neg", getNodeAttribute(sentimentAttributes, "neg"));
            sentimentTags.add(sentimentTag);
        }
        return sentimentTags;
    }

    /**
     * helps to parse date from string
     *
     * @param date date as string
     * @return date as LocalDate
     * @author Oliwia Daszczynska
     */
    public static LocalDate parseDate(String date, DateTimeFormatter formatter) {
        if (date.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * helps to parse date to string
     *
     * @param date date as date
     * @return date as string
     * @author Ishak Bouaziz
     */
    public static String formatDate(LocalDate date) {
        if (date == null) return "";
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    /**
     * helps to parse date and time from string.
     * parses with startOfDay in case no time was given.
     *
     * @param dateTime  date and time as one string
     * @param formatter formatting datetime
     * @return date and time as LocalDateTime
     * @author Sophie Kaiser
     * @author Oliwia Daszczynksa
     */
    public static LocalDateTime parseDateTime(String dateTime, DateTimeFormatter formatter) {
        if (dateTime.isBlank()) {
            return null;
        }

        try {
            return LocalDateTime.parse(dateTime, formatter);
        } catch (Exception e) {
            try {
                formatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMANY);
                return LocalDate.parse(dateTime, formatter).atStartOfDay(); // Parse without time
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
