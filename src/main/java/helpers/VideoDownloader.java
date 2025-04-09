package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import database.MongoDatabaseHandler_Impl;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

/**
* Class to map, download from the bundestag website and upload video data to the DB
 * @author Ishak Bouaziz
* */
public class VideoDownloader {
    /**
     * Upload new videos to the MDB
     * @author Ishak Bouaziz
     * */
    public static void fetchVideos() {
        try {
            HashMap<String, String> speakerMap = mapSpeakersToWebID();
            String baseVideoURL = "https://webtv.bundestag.de/pservices/player/embed/nokey?e=bt-od&ep=69&a=144277506&c=";
            String extension = "&t=https%3A%2F%2Fdbtg.tv%2Fcvid%2F7551672";

            for (Document speaker : MongoDatabaseHandler_Impl.getCollectionSpeaker().find()) {
                String key = speaker.getString("lastname") + ", " + speaker.getString("firstname");

                if (!speakerMap.containsKey(key))
                    continue;

                FindIterable<Document> speeches = MongoDatabaseHandler_Impl.getCollectionSpeeches().find(new Document("speaker_id", speaker.getString("_id")));
                for (Document speech : speeches) {
                    if(MongoDatabaseHandler_Impl.getCollectionVideos().find(new BasicDBObject("_id", speech.getString("_id"))).first() != null)
                        continue;

                    Integer sessionID = speech.getInteger("session_nr");
                    String preVideoURL = getSpeechURL(speakerMap.get(key), sessionID.toString());

                    org.jsoup.nodes.Document doc = Jsoup.connect(preVideoURL).get();
                    Element pageUrl = doc.selectFirst("a");

                    if (pageUrl == null)
                        continue;

                    String videoPageURL = pageUrl.attr("href");

                    String[] splits = videoPageURL.split("videoid=")[1].split("#url");
                    String url = baseVideoURL + splits[0] + extension;

                    MongoDatabaseHandler_Impl.getCollectionVideos().insertOne(createVideoDocument(speech.getString("_id"), splits[0], url));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a video document based on the speech id, video id and the embedded web url
     * @param speechID id of the corresponding speech
     * @param videoID id of the video object assigned by the bundestag website
     * @param videoURL url of the video
     * @return Document of the video object
     * @author Ishak Bouaziz
     * */
    private static Document createVideoDocument(String speechID, String videoID, String videoURL) {
        Document videoDocument = new Document("_id", speechID);
        videoDocument.append("video_id", videoID);
        videoDocument.append("url", videoURL);
        return videoDocument;
    }

    /**
     * Maps speaker names to the web id assigned by the bundestag website
     * @return HashMap Key: Name, Value: Web ID of a speaker
     * @author Ishak Bouaziz
     * */
    private static HashMap<String, String> mapSpeakersToWebID() throws Exception {
        URL url = new URL("https://www.bundestag.de/static/appdata/filter/rednerNamen.json");
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(url);

        HashMap<String, String> speakers = new HashMap<>();

        Iterator<JsonNode> elements = root.elements();
        while (elements.hasNext()) {
            JsonNode node = elements.next();

            Iterator<JsonNode> deps = node.get("dep").elements();
            while (deps.hasNext()) {
                Iterator<JsonNode> electionPeriods = deps.next().get("wahlperiode").elements();

                while (electionPeriods.hasNext()) {
                    int electionPeriod = electionPeriods.next().asInt();
                    if (electionPeriod == 20 || electionPeriod == 21) {
                        String name = pruneName(node.get("label").asText());
                        String id = node.get("value").asText();
                        speakers.put(name, id);
                        break;
                    }
                }
            }
        }
        return speakers;
    }

    /**
     * Removes the titles, address forms and other additional parts of a given name
     * @param name the name of a given speaker
     * @return String "Lastname, Firstname"
     * */
    private static String pruneName(String name) {
        return name.replaceAll("\\b(Prof\\.|Dr\\.|von)\\s", "").trim();
    }

    /**
     * Constructs the final video URL for a video based on the id of the speaker and the session number of the given speech
     * @param speakerID ID of the speaker as given in the MDB
     * @param  sessionID Number of the session the given speech was held at
     * @return String final URL to the video object
     * */
    private static String getSpeechURL(String speakerID, String sessionID) {
        String baseUrl = "https://www.bundestag.de/ajax/filterlist/de/mediathek/536668-536668?documenttype=442354%23BTFaisSpeechRecord&limit=500&mediaCategory=442350%23Plenarsitzungen&noFilterSet=false&rednerIds=442354%23";
        String filter = "&sitzung=536680%23" + sessionID;

        return baseUrl + speakerID + filter;
    }
}
