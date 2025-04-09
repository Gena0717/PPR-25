package helpers.Parsing;

import helpers.Downloader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parliament.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

import static helpers.Parsing.General.getAttribute;
import static helpers.Parsing.General.getTagContent;
import static parliament.ContentTag.*;

/**
 * parse protocol
 *
 * @author Oliwia Daszczynska
 */
public class Protocol {
    /**
     * check if protocols exist. parse protocols and create sessions, agenda items, speeches and contents.
     *
     * @param factory handles object creation
     * @author Oliwia Daszczynska
     */
    public static void parseProtocols(ParliamentFactory factory) {
        File[] protocols = new File("src/main/resources/speeches").listFiles(
                (dir, name) -> name.toLowerCase().endsWith(".xml") && name.matches("\\d+\\.xml")
        );
        assert protocols != null;
        if (protocols.length == 0) {
            throw new IllegalArgumentException("No protocols found. " +
                    "Please ensure XML protocols exist in the resources/speeches directory.");
        }
        for (File protocolFile : protocols) {
            if (protocolFile.getName().equals("007.xml")){
            System.out.println("Parsing " + protocolFile.getName() + "...");

            Document protocolDoc = General.buildXMLDocument(protocolFile);
            assert protocolDoc != null;
            Session session = parseSessionData(protocolDoc, factory);
            parseAgendaItems(protocolDoc, session, factory);
            printSessionInfo(session);}
        }
    }

    /**
     * parse protocol to extract information needed for session object construction.
     *
     * @param protocolDoc document holding protocol information
     * @param factory     handles object creation
     * @return newly created Session
     * @author Oliwia Daszczynska
     */
    private static Session parseSessionData(Document protocolDoc, ParliamentFactory factory) {
        int sessionNo = Integer.parseInt(getTagContent(protocolDoc, "sitzungsnr").replaceAll("[^0-9]", "").trim());
        LocalDate date = LocalDate.parse(getAttribute(protocolDoc, "datum", "date"), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        LocalTime startTime = LocalTime.parse(getAttribute(protocolDoc, "sitzungsbeginn", "sitzung-start-uhrzeit").replace('.', ':'), DateTimeFormatter.ofPattern("H:mm"));
        LocalTime endTime = LocalTime.parse(getAttribute(protocolDoc, "sitzungsende", "sitzung-ende-uhrzeit"), DateTimeFormatter.ofPattern("H:mm"));

        Session session = factory.createSession(sessionNo);
        session.setDate(date);
        session.setStartTime(startTime);
        session.setEndTime(endTime);

        return session;
    }

    /**
     * necessary step to create speeches is to first create agenda items.
     * find agenda information and create agenda item objects.
     *
     * @param protocolDoc document holding protocol information
     * @param session     ties to agenda item
     * @param factory     handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseAgendaItems(Document protocolDoc, Session session, ParliamentFactory factory) {
        NodeList agendaNodes = protocolDoc.getElementsByTagName("tagesordnungspunkt");
        for (int i = 0; i < agendaNodes.getLength(); i++) {
            Node agendaNode = agendaNodes.item(i);
            String agendaTitle = ((Element) agendaNode).getAttribute("top-id");
            // agenda item automatically added to session in agenda constructor
            AgendaItem agendaItem = factory.createAgendaItem(agendaTitle, session);
            if (agendaItem.getTitle().isBlank()) {
                agendaItem.setTitle("Ohne Titel"); // to avoid issues with freemarker, hopefully.
            }

            parseSpeeches(agendaNode, agendaItem, factory);
        }
    }

    /**
     * to parse speeches, access node whose name equals "rede"
     *
     * @param agendaItemNode node holding child nodes with speech information
     * @param agendaItem     agenda item which our speeches will be assigned to
     * @param factory        handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseSpeeches(Node agendaItemNode, AgendaItem agendaItem, ParliamentFactory factory) {
        NodeList agendaChildren = agendaItemNode.getChildNodes();
        for (int j = 0; j < agendaChildren.getLength(); j++) {
            Node agendaChild = agendaChildren.item(j);
            if (agendaChild.getNodeType() == Node.ELEMENT_NODE && agendaChild.getNodeName().equals("rede")) {
                // we've found agenda child nodes which equal "rede". now we can parse its contents.
                parseSpeech((Element) agendaChild, agendaItem, factory);
            }
        }
    }

    /**
     * create speech using information extracted from the speech element.
     *
     * @param speechElement holds speech information
     * @param agendaItem    agenda item which our speeches will be assigned to
     * @param factory       handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseSpeech(Element speechElement, AgendaItem agendaItem, ParliamentFactory factory) {
        String speechId = speechElement.getAttribute("id");
        String speakerId = getAttribute(speechElement, "redner", "id");
        Speaker speaker = getSpeaker(speakerId, speechElement, factory);

        Speech speech = factory.createSpeech(speechId, speaker, agendaItem);
        agendaItem.addSpeech(speech);

        parseSpeechContent(speechElement, speech, factory);
    }

    /**
     * get speaker using id found in plenary protocol. alternatively create a new speaker.
     *
     * @param speakerId     from protocol
     * @param speechElement from protocol
     * @param factory       handles object creation
     * @return found or created speaker
     * @author Oliwia Daszczynska
     */
    private static Speaker getSpeaker(String speakerId, Element speechElement, ParliamentFactory factory) {
        Set<Speaker> speakerSet = factory.getSpeakers();
        Speaker speaker = speakerSet.stream().filter(s -> s.getID().equals(speakerId)).findFirst().orElse(null);
        if (speaker == null) {
            // No speaker found. Create new speaker
            speaker = createNewSpeaker(speakerId, speechElement, factory);
        }

        return speaker;
    }

    /**
     * use information found in xml protocol to create a new speaker.
     *
     * @param speakerId     used for speaker creation. extracted from protocol.
     * @param speechElement used for speaker creation. extracted from protocol.
     * @param factory       handles object creation
     * @return newly created speaker
     * @author Oliwia Daszczynska
     */
    private static Speaker createNewSpeaker(String speakerId, Element speechElement, ParliamentFactory factory) {
        String lastName = speechElement.getElementsByTagName("nachname").item(0).getTextContent().trim();
        String firstName = speechElement.getElementsByTagName("vorname").item(0).getTextContent().trim();
        Speaker speaker = factory.createSpeaker(speakerId, lastName, firstName);

        setSpeakerDetails(speaker, speechElement, factory);

        return speaker;
    }

    /**
     * if additional information is found for speakers in the protocol, it is added
     *
     * @param speaker       speaker for whom additional options are to be added
     * @param speechElement where information is extracted from
     * @param factory       handles object creation
     * @author Oliwia Daszczynska
     */
    private static void setSpeakerDetails(Speaker speaker, Element speechElement, ParliamentFactory factory) {
        String title = getTagContent(speechElement, "titel");
        String location = getTagContent(speechElement, "ortszusatz");
        String party = getTagContent(speechElement, "fraktion");

        if (!title.isBlank()) {
            speaker.setAddressTitle(title);
        }
        if (!location.isBlank()) {
            speaker.setLocation(location);
        }
        if (!party.isBlank()) {
            setSpeakerParty(speaker, party, factory);
        }
    }

    /**
     * set a party to the speaker. create a new party if necessary.
     *
     * @param speaker   set this speakers party
     * @param partyName find party using this name
     * @param factory   handles object creation
     * @author Oliwia Daszczynska
     */
    private static void setSpeakerParty(Speaker speaker, String partyName, ParliamentFactory factory) {
        Set<Party> parties = factory.getParties();
        for (Party p : parties) {
            if (p.getName().equals(partyName)) {
                speaker.setMembership(true); // because if the membership is false, i cant add speaker to party
                speaker.setParty(p);
                return;
            }
        }

        // No party found. Create a new one.
        Party p = factory.createParty(partyName);
        speaker.setMembership(true); // because if the membership is false, i cant add speaker to party
        speaker.setParty(p);
        System.out.println("New party found: " + partyName);
    }

    /**
     * traversing child nodes and extracting content tag by accessing child node's name.
     *
     * @param speechElement holds content tag and text
     * @param speech        needed for content creation
     * @param factory       handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseSpeechContent(Element speechElement, Speech speech, ParliamentFactory factory) {
        NodeList speechChildNodes = speechElement.getChildNodes();
        for (int i = 0; i < speechChildNodes.getLength(); i++) {
            Node speechChildNode = speechChildNodes.item(i);
            if (speechChildNode.getNodeType() == Node.ELEMENT_NODE) {
                switch (speechChildNode.getNodeName()) {
                    case "p":
                        parseParagraphContent((Element) speechChildNode, speech, factory);
                        break;
                    case "kommentar":
                        factory.createContent(COMMENT, clean(speechChildNode), speech);
                        break;
                    case "name":
                        factory.createContent(SPEAKER_INTRODUCTION, clean(speechChildNode), speech);
                        break;
                    case "zitat":
                        factory.createContent(QUOTE, clean(speechChildNode), speech);
                        break;
                    case "a":
                    default:
                        break;
                }
            }
        }
    }


    /**
     * paragraphs can hold different information based on attribute.
     * extract attribute and create content accordingly.
     *
     * @param paragraphElement holds attribute
     * @param speech           needed for content creation
     * @param factory          handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseParagraphContent(Element paragraphElement, Speech speech, ParliamentFactory factory) {
        switch (paragraphElement.getAttribute("klasse")) {
            case "redner":
                parseSpeakerIntroduction(paragraphElement, speech, factory);
                break;
            case "J":
            case "J_1":
            case "O":
                factory.createContent(SPEECH_TEXT, clean(paragraphElement), speech);
                break;
            case "Z":
                factory.createContent(QUOTE, clean(paragraphElement), speech);
                break;
            default:
                break;
        }

        // If no attributes are found, treat it as regular speech content
        if (!paragraphElement.hasAttributes()) {
            factory.createContent(SPEECH_TEXT, clean(paragraphElement), speech);
        }
    }

    /**
     * traverse child nodes.
     * only extract the plain text (node type 3) to avoid repeating information from other child nodes.
     *
     * @param paragraphElement holds speaker information
     * @param speech           needed for content creation
     * @param factory          handles object creation
     * @author Oliwia Daszczynska
     */
    private static void parseSpeakerIntroduction(Element paragraphElement, Speech speech, ParliamentFactory factory) {
        NodeList paragraphChildren = paragraphElement.getChildNodes();
        for (int i = 0; i < paragraphChildren.getLength(); i++) {
            Node node = paragraphChildren.item(i);
            if (node.getNodeType() == 3 && !(node.getTextContent().trim().isEmpty())) {
                factory.createContent(SPEAKER_INTRODUCTION, clean(node), speech);
            }
        }
    }

    /**
     * remove whitespace and return text content
     *
     * @param n node to clean and return text content of
     * @return cleaned text content
     * @author Oliwia Daszczynska
     */
    private static String clean(Node n) {
        return n.getTextContent().replaceAll("\\s+", " ").trim();
    }

    /**
     * download video info and create video object
     *
     * @param session necessary for video object creation and video info download.
     * @param factory object creation
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static void downloadVideo(Session session, ParliamentFactory factory) {
        Map<String, String> vidInfo = Downloader.DownloadVideoInfo(session);
        if (vidInfo != null) {
            factory.createVideo(vidInfo.get("id"), session, vidInfo.get("url"));
        }
    }


    /**
     * system output.
     * for testing purposes.
     *
     * @param session session for which output is to be tested
     * @author Oliwia Daszczynska
     */
    private static void printSessionInfo(Session session) {
        System.out.println("session " + session.getSessionNr() +
                " - date: " + session.getDate() +
                " - start: " + session.getStartTime() +
                " - end: " + session.getEndTime());
    }

    /**
     * system output.
     * for testing purposes.
     *
     * @param s speech for which output is to be tested
     * @author Oliwia Daszczynska
     */
    private static void printSpeakerInfo(Speech s) {
        if (s.getSpeaker().getParty() != null) {
            System.out.print(s.getSpeaker().getFirstName() + " " + s.getSpeaker().getLastName() +
                    " (" + s.getSpeaker().getParty().getName() + ") ");
        } else {
            System.out.print(s.getSpeaker().getFirstName() + " " + s.getSpeaker().getLastName() + " ");
        }
    }

    /**
     * system output.
     * for testing purposes.
     *
     * @param s speech for which output is to be tested
     * @author Oliwia Daszczynska
     */
    private static void printSpeechContent(Speech s) {
        for (Content c : s.getContents()) {
            System.out.print(c.getContentTag() + " ");
            System.out.println(c.getText());
        }
    }
}
