package helpers.Parsing;

import com.mongodb.client.MongoCollection;
import database.MongoDatabaseHandler_Impl;
import helpers.Downloader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import parliament.ParliamentFactory;
import parliament.Party;
import parliament.Picture;
import parliament.Speaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static helpers.Parsing.General.getTagContent;
import static helpers.Parsing.General.parseDate;

/**
 * parse members
 *
 * @author Oliwia Daszczynska
 */
public class Members {

    private static final DateTimeFormatter formatterMDB = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter formatterPic = DateTimeFormatter.ofPattern("d. MMMM yyyy, H:mm", Locale.GERMANY);

    /**
     * check if member data exists. parse member data and create speaker objects.
     *
     * @param factory handles object creation.
     * @param enablePictureDownload if true, pictures of members are downloaded as well.
     * @author Oliwia Daszczynksa
     */
    public static void parseMDB(ParliamentFactory factory, boolean enablePictureDownload) {
        try {
            File MDBfile = new File("src/main/resources/members/MDB_STAMMDATEN.XML");
            if (MDBfile.exists()) {
                Document MDBdoc = General.buildXMLDocument(MDBfile);
                Set<String> partyNames = new HashSet<>();

                assert MDBdoc != null;
                NodeList ElementsInMDB = MDBdoc.getElementsByTagName("MDB");

                for (int i = 0; i < ElementsInMDB.getLength(); ++i) {   // for every MDB node
                    Node n = ElementsInMDB.item(i);
                    for (int j = 0; j < ((Element) n).getElementsByTagName("WP").getLength(); j++) { // for every WP
                        int wp = Integer.parseInt(getTagContent(n, "WP", true));

                        if (wp == 20) { // filter 20th election period
                            Speaker s = createSpeakerFromNode(n, factory);
                            assignParty(s, factory, partyNames, n);
                            if (enablePictureDownload) {
                                scrapePictures(s);
                            }
//                            printSpeakerInfo(s);
                            break; // Found WP 20
                        }
                    }
                }
            } else {
                throw new FileNotFoundException("No member data found. " +
                        "Please ensure MDB_STAMMDATEN.XML exists " +
                        "in the resources/members depository.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Extract information about the speaker to create an object using the parliament factory.
     *
     * @param n       node holding information concerning the speaker
     * @param factory handles object creation
     * @return newly created speaker
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    private static Speaker createSpeakerFromNode(Node n, ParliamentFactory factory) {
        Speaker s = factory.createSpeaker(
                getTagContent(n, "ID"),
                getTagContent(n, "NACHNAME", true),
                getTagContent(n, "VORNAME", true)
        );
        s.setMembership(true);
        s.setLocation(getTagContent(n, "ORTSZUSATZ"));
        s.setNobility(getTagContent(n, "ADEL"));
        s.setPrefix(getTagContent(n, "PRAEFIX"));
        s.setAddressTitle(getTagContent(n, "ANREDE_TITEL"));
        s.setAcademicTitle(getTagContent(n, "AKAD_TITEL"));
        s.setDateOfBirth(parseDate(getTagContent(n, "GEBURTSDATUM"), formatterMDB));
        s.setPlaceOfBirth(getTagContent(n, "GEBURTSORT"));
        s.setCountryOfBirth(getTagContent(n, "GEBURTSLAND"));
        s.setDateOfDeath(parseDate(getTagContent(n, "STERBEDATUM"), formatterMDB));
        s.setGender(getTagContent(n, "GESCHLECHT"));
        s.setMaritalStatus(getTagContent(n, "FAMILIENSTAND"));
        s.setReligion(getTagContent(n, "RELIGION"));
        s.setProfession(getTagContent(n, "BERUF"));
        s.setCurriculumVitae(getTagContent(n, "VITA_KURZ"));
        return s;
    }

    /**
     * To associate a party to a speaker, the party must first exist.
     * Therefore, create new parties when such are found in the member data.
     *
     * @param s          speaker whom a party must be assigned to
     * @param factory    handles object creation
     * @param partyNames keep track of created parties
     * @param n          node holding the party name which the speaker belongs to
     * @author Oliwia Daszczynska
     */
    private static void assignParty(Speaker s, ParliamentFactory factory, Set<String> partyNames, Node n) {
        String partyName = getTagContent(n, "PARTEI_KURZ");
        if (factory.getParties().size() == 0) {
            // create first party if no parties exist yet.
            partyNames.add(partyName);
            factory.createParty(partyName);
        }

        for (Party p : factory.getParties()) {
            // create party if it doesn't exist yet.
            if (!partyNames.contains(partyName)) {
                partyNames.add(partyName);
                factory.createParty(partyName);
                break;
            }
        }

        for (Party p : factory.getParties()) {
            // assign member to party and party to member.
            if (p.getName().equals(partyName)) {
                s.setParty(p);
                p.addMember(s);
                break;
            }
        }
    }

    /**
     * scrape the picture of a given speaker. create a picture object in the factory.
     * assign the picture to the speaker.
     *
     * @param s       speaker whose picture is to be scraped
     * @param factory handles object creation
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static void scrapePicture(Speaker s, ParliamentFactory factory) throws IOException {
        Map<String, String> picInfo = Downloader.DownloadPicture(s);
        if (!picInfo.isEmpty()) {
            Picture p = factory.createPicture(Integer.parseInt(picInfo.get("Bildnummer")));
            p.setLocation(picInfo.get("Ort"));
            p.setPhotographer(picInfo.get("Fotograf/in"));
            p.setDateTime(General.parseDateTime(picInfo.get("Aufgenommen"), formatterPic));
            p.setURL(picInfo.get("url"));
            p.setSpeaker(s);
            s.setPicture(p);
        }
    }

    /**
     *
     * @param s speaker whose picture is to be scraped
     * @author Ishak Bouaziz
     */
    private static void scrapePictures(Speaker s){
        MongoCollection<org.bson.Document> speakers = MongoDatabaseHandler_Impl.getCollectionSpeaker();
        org.bson.Document speaker = speakers.find(new org.bson.Document("_id", s.getID())).first();
        Downloader.downloadImages(speaker);
    }

    /**
     * Output for testing purposes.
     *
     * @param s speaker for whom to test the output
     * @author Oliwia Daszczynska
     */
    private static void printSpeakerInfo(Speaker s) {
        System.out.println("New Speaker");
        System.out.println("First name: " + s.getFirstName());
        System.out.println("Last name: " + s.getLastName());
        if (s.getParty() != null) {
            System.out.println("Party: " + s.getParty().getName());
        }
        if (s.getPicture() != null) {
            System.out.println("Picture url: " + s.getPicture().getURL());
            System.out.println("Picture datetime: " + s.getPicture().getDateTime());
        }
    }
}
