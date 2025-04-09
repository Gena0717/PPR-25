package helpers;

import database.MongoDatabaseHandler_Impl;
import helpers.Parsing.General;
import org.apache.commons.io.FileUtils;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import parliament.Session;
import parliament.Speaker;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static org.apache.commons.configuration.PropertyConverter.toURL;

/**
 * Methods downloading data from official sources.
 *
 * @author Oliwia Daszczynska
 */
public class Downloader {
    /**
     * download files from bundestag open data using download helper functions.
     *
     * @author Oliwia Daszczynska
     */
    public static void downloadFiles() {
        try {
            Downloader.DownloadDtd();
            Downloader.DownloadMdb();
            Downloader.DownloadProtocols();
            VideoDownloader.fetchVideos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Download dtd file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @author Oliwia Daszczynska
     */
    public static void DownloadDtd() throws IOException {
        if (new File("src/main/resources/speeches/dbtplenarprotokoll.dtd").exists()) {
            System.out.println("dbtplenarprotokoll.dtd found.");
        } else {
            FileUtils.copyURLToFile(
                    toURL("https://www.bundestag.de/resource/blob/575720/100244acc72762143d8056e7d3190a96/dbtplenarprotokoll.dtd"),
                    new File("src/main/resources/speeches/dbtplenarprotokoll.dtd")
            );
            System.out.println("dbtplenarprotokoll.dtd loaded.");
        }
    }

    /**
     * Download MdB Stammdaten.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @author Oliwia Daszczynska
     */
    public static void DownloadMdb() throws IOException {
        if (new File("src/main/resources/members/MdB-Stammdaten.zip").exists()) {
            System.out.println("MdB-Stammdaten.zip found.");
            Utils.unzip("src/main/resources/members/MdB-Stammdaten.zip", "src/main/resources/members");
        } else {
            FileUtils.copyURLToFile(
                    toURL("https://www.bundestag.de/resource/blob/472878/56d0514de78abb261d81ab940b9deed7/MdB-Stammdaten.zip"),
                    new File("src/main/resources/members/MdB-Stammdaten.zip")
            );
            System.out.println("MdB-Stammdaten.zip loaded.");
            Utils.unzip("src/main/resources/members/MdB-Stammdaten.zip", "src/main/resources/members");
        }
    }

    /**
     * Download videos from bundestag mediathek using session number to get video id from mediathek.
     * video id is used to extract video download url. id and url are stored in a map and returned.
     *
     * @param s session number needed for video id extraction
     * @return map of video id and url
     * @author Oliwia Daszczynska
     */
    @Deprecated
    public static Map<String, String> DownloadVideoInfo(Session s) {
        Map<String, String> vidInfo = new HashMap<>();
        int wp = 20;
        int sessionNumber = s.getSessionNr();
        // this url the search result for session number and wp
        String sessionUrl = "https://www.bundestag.de/ajax/filterlist/de/mediathek/plenarsitzungen/" +
                "906296-906296?limit=24&mediaCategory=919336%23" +
                "Plenarsitzungen&noFilterSet=false&s" +
                "itzung=906304%23%22" + sessionNumber +
                "%22&sort=date%20desc&" +
                "wahlperiode=906308%23" + wp;

        try {
            // Select first element and get data-videoid attribute
            org.jsoup.nodes.Document doc = Jsoup.connect(sessionUrl).get();
            Element element = doc.select("[data-videoid]").first();
            String videoId = element.attr("data-videoid");
            vidInfo.put("id", videoId);
            String shareDataUrl = "https://webtv.bundestag.de/player/macros/_x_s-144277506/shareData.json?" +
                    "contentId=" + videoId;
            // connect to json file which holds download link
            URL url = toURL(shareDataUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            String vid = Utils.StreamToString(connection.getInputStream());
            // get download URL (low quality) link by splitting response string
            String[] parts = vid.split("\"downloadUrlLow\"\\s*:\\s*\"");
            String videoUrl = parts[1].split("\"")[0];
            vidInfo.put("url", videoUrl);
            connection.disconnect();

            return vidInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Download protocols by finding them in the bundestag sitemap.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     * @author Oliwia Daszczynska
     */
    public static void DownloadProtocols() throws IOException {
        //check how many protocols the user already has in the directory.
        File directory = new File("src/main/resources/speeches");
        // if directory.list is null, the fileCount is 0.
        // By this point, the dtd file should already be in the directory and fileCount should always be 1.
        int fileCount = directory.list() != null ? Objects.requireNonNull(directory.list()).length : 0;
        System.out.println(fileCount + " files found in speeches directory.");
        // sitemap (for finding xml protocol files)
        FileUtils.copyURLToFile(
                toURL("https://www.bundestag.de/service-sitemap-bundestagde-sitemap1.xml.gz"),
                new File("src/main/resources/speeches/sitemap1.xml.gz")
        );
        // decompress sitemap1.xml.gz to sitemap1.xml
        Utils.decompress("src/main/resources/speeches/sitemap1.xml.gz", "src/main/resources/speeches/sitemap1.xml");
        Set<String> urlSet = General.extractProtocolURL("src/main/resources/speeches/sitemap1.xml");

        System.out.println(urlSet.size() + " plenary protocols found in bundestag database.");
        if (fileCount < (urlSet.size() + 4)) { // accounts for dtd and sitemap files already in directory.
            System.out.println("Loading speech files...");
            // download each protocol using the urls extracted from sitemap
            for (String entry : urlSet) {
                String fileName = entry.substring(entry.lastIndexOf('/') + 3);
                if (new File("src/main/resources/speeches/" + fileName).exists()) {
                    System.out.println(fileName + " found.");
                } else {
                    FileUtils.copyURLToFile(toURL(entry), new File("src/main/resources/speeches/" + fileName));
                    System.out.println("Downloaded: " + fileName);
                }
            }
        } else {
            System.out.println("All plenary protocols accounted for.");
        }
    }

    /**
     * Search for picture of speaker using their first and lastname.
     * Return url and meta-information of found pictures in a map.
     *
     * @param s Speaker whose picture-information is to be downloaded.
     * @return Url and meta information.
     * @throws IOException Signals io exception.
     * @author Oliwia Daszczynska
     * @author Sophie Kaiser
     */
    @Deprecated
    public static Map<String, String> DownloadPicture(Speaker s) throws IOException {
        // this map will be populated with picture information - if such is found - and returned at the end.
        Map<String, String> picInfo = new HashMap<>();
        // build string using this speaker's first and last name
        String url = "https://bilddatenbank.bundestag.de/search/picture-result?filterQuery%5Bname%5D%5B%5D=" +
                s.getLastName() +
                "%2C+" +
                s.getFirstName() +
                "&filterQuery%5Bereignis%5D%5B%5D=Portr%C3%A4t%2FPortrait&sortVal=3";

        String fotoUrlBase = "https://bilddatenbank.bundestag.de";
        org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
        // the anchor element with the attribute "data-fancybox" holds the relevant information.
        Element a = doc.selectFirst("a[data-fancybox]");
        if (a != null) {
            // extract information by splitting the jsoup response using regular expressions.
            String dataCaption = a.attr("data-caption");
            String[] parts = dataCaption.split("<br>");
            for (String part : parts) {
                // extract relevant information fields (location, date, picture number, photographer)
                if (part.contains("Ort:") ||
                        part.contains("Bildnummer:") || part.contains("Fotograf/in:")) {
                    String[] subpart = part.trim().split("[:,]");
                    picInfo.put(subpart[0].trim(), subpart[1].trim());
                } else if (part.contains("Aufgenommen:")) {
                    String[] subpart = part.trim().split(":", 2);
                    picInfo.put(subpart[0].trim(), subpart[1].replace("Uhr", "").trim());
                }
            }
            // the first image tag inside the anchor element holds the source url we must extract.
            String fotoUrl = fotoUrlBase + a.selectFirst("img").attr("src");
            picInfo.put("url", fotoUrl);
        }
        return picInfo;
    }

    /**
     * Search for each picture of a speaker in the DB.
     * Write the object into the picture collection in the MDB.
     *
     * @param speaker Speaker document.
     * @author Ishak Bouaziz
     */
    public static void downloadImages(Document speaker) {
        try {
            String firstName = speaker.getString("firstname");
            String lastName = speaker.getString("lastname");

            String url = "https://bilddatenbank.bundestag.de/search/picture-result?filterQuery%5Bname%5D%5B%5D=" + lastName + "%2C+" + firstName + "&filterQuery%5Bereignis%5D%5B%5D=Portr%C3%A4t%2FPortrait&sortVal=3";
            String fotoUrlBase = "https://bilddatenbank.bundestag.de";

            org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
            Elements imgAnchors = doc.select("a[data-fancybox]");

            if (imgAnchors.size() > 0) {
                // extract information by splitting the jsoup response using regular expressions.

                Document imageDocument = new Document("_id", speaker.getString("_id"));
                List<Document> images = new ArrayList<>();

                for (Element anchor : imgAnchors) {
                    String src = anchor.selectFirst("img").attr("src");
                    String desc = anchor.attr("data-caption");

                    String[] metaData = desc.split("<br>");


                    Document img = new Document("_id", metaData[5].split(":")[1].trim());
                    img.append("url", fotoUrlBase + src);
                    img.append("location", metaData[3].split(":")[1].trim());
                    img.append("date", metaData[4].split(":")[1].trim());
                    img.append("photographer", metaData[6].split(":")[1].trim());

                    images.add(img);
                }

                imageDocument.append("images", images);
                MongoDatabaseHandler_Impl.getCollectionPictures().insertOne(imageDocument);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
