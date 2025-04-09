package database;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import parliament.*;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;


/**
 * insert data extracted from member data and plenary protocols into mdb.
 *
 * @author Oliwia Daszczynska
 */
public class FactoryToMongoDB {
    /**
     * retrieves information from parliament factory and stores it into mongo collections.
     *
     * @author Oliwia Daszczynska
     */
    public static void factoryToMongoDB(ParliamentFactory factory) {
        Set<Picture> pictureSet = factory.getPictures();
        Set<Speaker> speakerSet = factory.getSpeakers();
        Set<Speech> speechSet = factory.getSpeeches();
        Set<Video> videoSet = factory.getVideos();

        speakersToMDB(speakerSet);
        speechesToMDB(speechSet, false);
    }

    /**
     * if there are no documents in the mongo picture collection
     * stores pictures in mongodb
     *
     * @param pictureSet pictures from factory
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static void picturesToMDB(Set<Picture> pictureSet) {
        if (MongoDatabaseHandler_Impl.getDatabase().getCollection("pictures").countDocuments() == 0) {
            for (Picture p : pictureSet) {
                Document entry = new Document()
                        .append("_id", (p.getPictureNumber()) + p.getSpeaker().getID()) // some pic numbers repeat
                        .append("url", p.getURL())
                        .append("speaker_id", p.getSpeaker().getID())
                        .append("datetime", p.getDateTime().toString())
                        .append("photographer", p.getPhotographer())
                        .append("location", p.getLocation()
                        );
                MongoDatabaseHandler_Impl.getDatabase().getCollection("pictures").insertOne(entry);
            }
        }
    }

    /**
     * if there are no documents in the mongo speaker collection
     * stores speakers in mongodb
     *
     * @param speakerSet speakers from factory
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    private static void speakersToMDB(Set<Speaker> speakerSet) {
        MongoCollection<Document> speakerCollection = MongoDatabaseHandler_Impl.getCollectionSpeaker();
        if (speakerCollection.countDocuments() == 0) { // only enables mongo-insertion if no speakers in collection.
            for (Speaker s : speakerSet) {
                Document entry = new Document()
                        .append("_id", s.getID())
                        .append("lastname", s.getLastName())
                        .append("firstname", s.getFirstName())
                        .append("member", s.isMember())
                        .append("party_name", ((s.getParty() != null) ? s.getParty().getName() : ""))
                        .append("location", s.getLocation())
                        .append("nobility", s.getNobility())
                        .append("prefix", s.getPrefix())
                        .append("address_title", s.getAddressTitle())
                        .append("academic_title", s.getAcademicTitle())
                        .append("date_of_birth", (Objects.toString(s.getDateOfBirth(), "")))
                        .append("place_of_birth", s.getPlaceOfBirth())
                        .append("date_of_death", (Objects.toString(s.getDateOfDeath(), "")))
                        .append("gender", s.getGender())
                        .append("marital_status", s.getMaritalStatus())
                        .append("religion", s.getReligion())
                        .append("profession", s.getProfession())
                        .append("curriculum_vitae", s.getCurriculumVitae());
                speakerCollection.insertOne(entry);
            }
            System.out.println("added " + speakerCollection.countDocuments() +
                    " documents to speaker mongoCollection.");
        }
    }

    /**
     * store speeches from factory in MongoDB.
     *
     * @param speechSet       speeches from factory
     * @param insertIntoMongo if true, inserts speeches into MongoDB.
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    private static void speechesToMDB(Set<Speech> speechSet, boolean insertIntoMongo) {
        MongoCollection<Document> speechCollection = MongoDatabaseHandler_Impl.getCollectionSpeeches();
//        System.out.println(speechCollection.countDocuments() + " speeches in mdb");
        if (insertIntoMongo) { // set true to insert speeches
            System.out.println(speechCollection.countDocuments() + " speeches in mdb");
            for (Speech s : speechSet) {
                /* create a linked list of documents for each piece of
                content in the speech */
                LinkedList<Document> contentDocList = new LinkedList<>();
                for (Content c : s.getContents()) {
                    contentDocList.add(new Document()
                            .append("_id", c.getID())
                            .append("content_tag", c.getContentTag().toString())
                            .append("text", c.getText())
                    );
                }
                /* If there is no video recording of the speech,
                s.getVideo() returns null. For speeches without video
                recording an empty string is saved in the MongoDB. */
                String videoID = "";
                if (s.getVideo() != null) {
                    videoID = s.getVideo().getID();
                }
                Document entry = new Document()
                        .append("_id", s.getID())
                        .append("speaker_id", s.getSpeaker().getID())
                        .append("agenda_item", s.getAgendaItem().getTitle())
                        .append("session_nr", s.getSession().getSessionNr())
                        .append("contents", contentDocList)
                        .append("video_id", videoID);

                // only insert if duplicate id can't be found:
                if (speechCollection.countDocuments(new Document("_id", s.getID())) == 0) {
                    speechCollection.insertOne(entry);
                    System.out.println("inserted " + s.getID());
                } else {
                    System.out.println("caught double: " + s.getID());
                }
            }
        }
    }

    /**
     * if there are no documents in the mongo video collection
     * stores videos in mongodb
     *
     * @param vidSet videos from factory
     * @author Oliwia Daszczynska
     */
    @Deprecated
    private static void videosToMDB(Set<Video> vidSet) {
        MongoCollection<Document> videoCollection = MongoDatabaseHandler_Impl.getDatabase().getCollection("videos");
        if (videoCollection.countDocuments() == 0) {
            for (Video v : vidSet) {
                Document entry = new Document()
                        .append("_id", v.getID())
                        .append("url", v.getURL())
                        .append("session_nr", v.getSession().getSessionNr());

                // only insert if duplicate id can't be found:
                if (videoCollection.countDocuments(new Document("_id", v.getID())) == 0) {
                    videoCollection.insertOne(entry);
                    System.out.println("inserted " + v.getID());
                } else {
                    System.out.println("caught double: " + v.getID());
                }
            }
        }
    }
}
