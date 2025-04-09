package rest;

import com.google.gson.Gson;
import com.influxdb.client.JSON;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Aggregates;
import database.MongoDatabaseHandler_Impl;
import export.TeXMaking;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import frontend.SpeakerCardRenderer;
import frontend.SpeechCardRenderer;
import helpers.Parsing.General;
import io.javalin.http.Context;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Class to provide the rendering and data for the frontend.
 *
 * @author Ishak Bouaziz
 */
public class PageController implements IRESTEvents {

    private static Configuration ftlConfig;

    private static PageController instance;

    /**
     * Retrieves the PageController instance.
     *
     * @return instance
     * @author Ishak Bouaziz
     */
    public static PageController get() {
        if (instance == null)
            instance = new PageController();

        return instance;
    }

    /**
     * Constructor creates a ftl config and subscribes to the RESTHandler events.
     *
     * @author Ishak Bouaziz
     */
    private PageController() {
        try {
            ftlConfig = new Configuration(Configuration.VERSION_2_3_23);
            ftlConfig.setDirectoryForTemplateLoading(new File("src/main/resources/web/pages"));
            ftlConfig.setDefaultEncoding("UTF-8");
            ftlConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            RESTHandler.get().addEventListener(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * renders a given ftl file in the browser.
     *
     * @param file  path to the file.
     * @param ctx   javalin context.
     * @param model additional data fed to a template.
     * @author Ishak Bouaziz
     */
    private void renderPage(String file, Context ctx, Map<String, Object> model) {
        try {
            Template template = ftlConfig.getTemplate(file);
            StringWriter sw = new StringWriter();

            template.process(model, sw);
            ctx.contentType("text/html");
            ctx.result(sw.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when landing page is opened
     *
     * @author Ishak Bouaziz
     */
    @Override
    public void onUpdateLandingPage(Context ctx) {
        try {
            renderPage("index.ftl", ctx, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when speakers page is opened
     *
     * @author Ishak Bouaziz
     */
    @Override
    public void onUpdateSpeakersPage(Context ctx) {
        try {
            FindIterable<Document> documents;

            String searchString = ctx.queryParam("search") == null ? "" : ctx.queryParam("search");
            String sort = ctx.queryParam("sort");

            if (searchString != null) {
                Document query = new Document("$or", List.of(
                        new Document("firstname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("lastname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("party_name", new Document("$regex", searchString).append("$options", "i"))
                ));
                documents = MongoDatabaseHandler_Impl.getCollectionSpeaker().find(query);
            } else
                documents = MongoDatabaseHandler_Impl.getCollectionSpeaker().find();

            if (sort == null)
                sort = "1";

            switch (sort) {
                case "1":
                    documents.sort(new BasicDBObject("firstname", 1));
                    break;
                case "2":
                    documents.sort(new BasicDBObject("lastname", 1));
                    break;
                case "3":
                    documents.sort(new BasicDBObject("party_name", 1));
                    break;
                default:
                    break;
            }

            //Create speaker cards to be rendered on page
            SpeakerCardRenderer speakerCardRenderer = new SpeakerCardRenderer();
            List<String> speakercards = new ArrayList<>();

            for (Document document : documents)
                speakercards.add(speakerCardRenderer.toHtml(document));

            Map<String, Object> model = new HashMap<>();
            model.put("speakers", speakercards);
            model.put("sort", sort);
            model.put("search", searchString);
            renderPage("speakers.ftl", ctx, model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when a single speaker page is opened
     *
     * @author Ishak Bouaziz
     */
    @Override
    public void onUpdateSingleSpeakerPage(Context ctx) {
        try {
            String id = ctx.pathParam("speaker");
            String searchString = ctx.queryParam("search");
            String sort = ctx.queryParam("sort") == null ? "1" : ctx.queryParam("sort");
            int page = Integer.parseInt(ctx.queryParam("page") == null ? "0" : ctx.queryParam("page"));
            int display = Integer.parseInt(ctx.queryParam("display") == null ? "25" : ctx.queryParam("display"));

            BasicDBObject speakerQuery = new BasicDBObject("_id", id);
            Document speaker = MongoDatabaseHandler_Impl.getCollectionSpeaker().find(speakerQuery).first();
            Document images = MongoDatabaseHandler_Impl.getCollectionPictures().find(speakerQuery).first();

            Document query;
            if (searchString != null) {
                Document q = new Document("$expr", new Document("$regexMatch",
                        new Document("input", new Document("$toString", "$session_nr"))
                                .append("regex", searchString)
                                .append("options", "i")
                ));

                Document searchQuery = new Document("$or", List.of(
                        q,
                        new Document("agenda_item", new Document("$regex", searchString).append("$options", "i"))
                ));

                query = new Document("$and", List.of(
                        new Document("speaker_id", id),
                        searchQuery
                ));
            } else
                query = new Document("speaker_id", id);

            FindIterable<Document> speeches = MongoDatabaseHandler_Impl.getCollectionSpeeches().find(query).skip(page * display).limit(display);

            switch (sort) {
                case "1":
                    speeches.sort(new BasicDBObject("session_nr", 1));
                    break;
                case "2":
                    speeches.sort(new BasicDBObject("agenda_item", 1));
                    break;
            }

            Map<String, Object> model = new HashMap<>();
            model.put("id", id);
            passSpeakerMetaData(model, speaker, images);

            model.put("speeches", new ArrayList<String>());
            List<String> speechCards = new ArrayList<>();
            SpeechCardRenderer renderer = new SpeechCardRenderer();
            Iterator<Document> iterator = speeches.iterator();
            while (iterator.hasNext())
                speechCards.add(renderer.toHtml(iterator.next()));

            model.put("search", searchString);
            model.put("sort", sort);
            model.put("page", page);
            model.put("display", display);
            model.put("speeches", speechCards);

            renderPage("speaker.ftl", ctx, model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to parse speaker meta data.
     *
     * @author Ishak Bouaziz
     */
    private void passSpeakerMetaData(Map<String, Object> model, Document speaker, Document images) {
        LocalDate birthDate = General.parseDate(speaker.getString("date_of_birth"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate deathDate = General.parseDate(speaker.getString("date_of_death"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.put("title", speaker.getString("academic_title") == null ? "" : speaker.getString("academic_title"));
        model.put("lastName", speaker.getString("lastname"));
        model.put("firstName", speaker.getString("firstname"));
        model.put("party", speaker.getString("party_name") == "" ? "Parteilos" : speaker.getString("party_name"));
        model.put("sex", speaker.getString("gender") == null ? "" : speaker.getString("gender"));
        model.put("family", speaker.getString("marital_status") == null ? "" : speaker.getString("marital_status"));
        model.put("religion", speaker.getString("religion") == null ? "" : speaker.getString("religion"));
        model.put("birthplace", speaker.getString("place_of_birth") == null ? "" : speaker.getString("place_of_birth"));
        model.put("job", speaker.getString("profession") == null ? "" : speaker.getString("profession"));
        model.put("birthDate", General.formatDate(birthDate));
        model.put("deathDate", General.formatDate(deathDate));
        model.put("vita", speaker.getString("curriculum_vitae") == null ? "" : speaker.getString("curriculum_vitae"));

        if (images != null) {
            List<String> speakerImages = new ArrayList<>();

            for (Document image : images.getList("images", Document.class)) {
                speakerImages.add(image.getString("url"));
            }

            model.put("currentImage", speakerImages.get(0));
            model.put("images", speakerImages);
        }
    }

    /**
     * Helper method to render the nlp charts
     *
     * @param topics     List of topics
     * @param pos        List of POS
     * @param sentiments List of sentiments
     * @param entities   List of namedEntities
     * @param model      model to pass data to a ftl file
     * @author Ishak Bouaziz
     */
    private void renderCharts(List<Document> topics, List<Document> pos, List<Document> sentiments, List<Document> entities, Map<String, Object> model) {
        Gson gson = new Gson();
        model.put("topics", gson.toJson(topics));
        model.put("pos", gson.toJson(pos));
        model.put("sentiments", gson.toJson(sentiments));
        model.put("entities", gson.toJson(entities));
    }

    /**
     * Helper method to render the nlp charts based on a list of documents
     *
     * @param speeches aggregated speeches
     * @param model    model to pass data to the .ftl
     * @author Ishak Bouaziz
     */
    private void renderCharts(AggregateIterable<Document> speeches, Map<String, Object> model) {
        List<Document> flatTopics = new ArrayList<>();
        List<Document> flatPOS = new ArrayList<>();
        List<Document> flatSentiments = new ArrayList<>();
        List<Document> flatEntities = new ArrayList<>();

        for (Document speech : speeches) {
            Document processed = speech.get("processed_speech", Document.class);
            List<Document> topics = processed.getList("Topics", Document.class);
            List<Document> pos = processed.getList("posTags", Document.class);
            List<Document> sentiments = processed.getList("sentiments", Document.class);
            List<Document> entities = processed.getList("namedEntities", Document.class);

            if (topics != null && !topics.isEmpty())
                flatTopics.addAll(topics);
            if (pos != null && !pos.isEmpty())
                flatPOS.addAll(pos);
            if (sentiments != null && !sentiments.isEmpty())
                flatSentiments.addAll(sentiments);
            if (entities != null && !entities.isEmpty())
                flatEntities.addAll(entities);
        }

        renderCharts(flatTopics, flatPOS, flatSentiments, flatEntities, model);
    }

    /**
     * Invoked when the speeches page is opened
     *
     * @author Ishak Bouaziz
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdateSpeechesPage(Context ctx) {
        try {
            String searchString = ctx.queryParam("search") == null ? "" : ctx.queryParam("search");
            String sort = ctx.queryParam("sort") == null ? "1" : ctx.queryParam("sort");
            int page = Integer.parseInt(ctx.queryParam("page") == null ? "0" : ctx.queryParam("page"));
            int display = Integer.parseInt(ctx.queryParam("display") == null ? "25" : ctx.queryParam("display"));

            Document aggregateObject = new Document("$lookup", new Document("from", "speaker")
                    .append("localField", "speaker_id")
                    .append("foreignField", "_id")
                    .append("as", "speaker_info"));

            List<Bson> aggregatePipeline = new ArrayList<>();
            aggregatePipeline.add(aggregateObject);
            aggregatePipeline.add(new Document("$unwind", "$speaker_info"));

            if (searchString != null) {
                Document q = new Document("$expr", new Document("$regexMatch",
                        new Document("input", new Document("$toString", "$session_nr"))
                                .append("regex", searchString)
                                .append("options", "i")
                ));

                Document query = new Document("$or", List.of(
                        q,
                        new Document("agenda_item", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.lastname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.firstname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.party_name", new Document("$regex", searchString).append("$options", "i"))
                ));

                aggregatePipeline.add(Aggregates.match(query));
            }

            switch (sort) {
                case "1":
                    aggregatePipeline.add(new Document("$sort", new Document("session_nr", 1)));
                    break;
                case "2":
                    aggregatePipeline.add(new Document("$sort", new Document("agenda_item", 1)));
                    break;
                case "3":
                    aggregatePipeline.add(new Document("$sort", new Document("speaker_info.lastname", 1)));
                    break;
                case "4":
                    aggregatePipeline.add(new Document("$sort", new Document("speaker_info.party_name", 1)));
                    break;
                default:
                    break;
            }

            aggregatePipeline.add(new Document("$skip", page * display));
            aggregatePipeline.add(new Document("$limit", display));

            AggregateIterable<Document> aggregatedSpeeches = MongoDatabaseHandler_Impl.aggregateMDB(MongoDatabaseHandler_Impl.getCollectionSpeeches(), aggregatePipeline);

            //Create speech cards to be rendered on page
            List<String> speechCards = new ArrayList<>();

            SpeechCardRenderer renderer = new SpeechCardRenderer();
            Iterator<Document> iterator = aggregatedSpeeches.iterator();
            while (iterator.hasNext())
                speechCards.add(renderer.toHtml(iterator.next()));

            Map<String, Object> model = new HashMap<>();
            model.put("speeches", speechCards);
            model.put("search", searchString);
            model.put("sort", sort);
            model.put("page", page);
            model.put("display", display);
            renderPage("speeches.ftl", ctx, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when a single speech page is opened
     *
     * @author Ishak Bouaziz
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdateSingleSpeechPage(Context ctx) {
        try {
            BasicDBObject query = new BasicDBObject("_id", ctx.pathParam("id"));
            Document speech = MongoDatabaseHandler_Impl.getCollectionSpeeches().find(query).first();
            Document processedSpeech = MongoDatabaseHandler_Impl.getCollectionProcessedSpeeches().find(query).first();
            Document video = MongoDatabaseHandler_Impl.getCollectionVideos().find(query).first();
            Document processedVideo = MongoDatabaseHandler_Impl.getCollectionProcessedVideos().find(query).first();

            Map<String, Object> model = new HashMap<>();

            List<Document> topics = processedSpeech.getList("Topics", Document.class);
            List<Document> pos = processedSpeech.getList("posTags", Document.class);
            List<Document> sentiments = processedSpeech.getList("sentiments", Document.class);
            List<Document> entities = processedSpeech.getList("namedEntities", Document.class);

            renderCharts(topics, pos, sentiments, entities, model);

            model.put("speech", speech);
            model.put("processedSpeech", processedSpeech);
            model.put("video", video);
            model.put("processedVideo", processedVideo);

            renderPage("speech.ftl", ctx, model);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invoked when the diagrams page is opened
     * @author Oliwia Daszczynska
     * @author Ishak Bouaziz
     */
    @Override
    public void onUpdateDiagramsPage(Context ctx) {
        try {
            String speakerSearch = ctx.queryParam("speaker") == null ? "" : ctx.queryParam("speaker");
            String sessionSearch = ctx.queryParam("session") == null ? "" : ctx.queryParam("session");
            String partySearch = ctx.queryParam("party") == null ? "" : ctx.queryParam("party");
            String titleSearch = ctx.queryParam("title") == null ? "" : ctx.queryParam("title");

            int page = Integer.parseInt(ctx.queryParam("page") == null ? "0" : ctx.queryParam("page"));
            int display = Integer.parseInt(ctx.queryParam("display") == null ? "25" : ctx.queryParam("display"));

            List<Bson> aggregatePipeline = new ArrayList<>();

            Document aggregateProcessedSpeech = new Document("$lookup", new Document("from", "processed_speeches")
                    .append("localField", "_id")
                    .append("foreignField", "_id")
                    .append("as", "processed_speech"));

            Document aggregateObject = new Document("$lookup", new Document("from", "speaker")
                    .append("localField", "speaker_id")
                    .append("foreignField", "_id")
                    .append("as", "speaker_info"));

            aggregatePipeline.add(aggregateObject);
            aggregatePipeline.add(aggregateProcessedSpeech);
            aggregatePipeline.add(new Document("$unwind", "$processed_speech"));
            aggregatePipeline.add(new Document("$unwind", "$speaker_info"));

            List<Document> queries = new ArrayList<>();
            if (!speakerSearch.isBlank()) {
                Document speakerQuery = new Document("$or", List.of(
                        new Document("speaker_info.lastname", new Document("$regex", speakerSearch).append("$options", "i")),
                        new Document("speaker_info.firstname", new Document("$regex", speakerSearch).append("$options", "i"))
                ));

                queries.add(speakerQuery);
            }

            if (!sessionSearch.isBlank()) {
                Document sessionQuery = new Document("$expr", new Document("$regexMatch",
                        new Document("input", new Document("$toString", "$session_nr"))
                                .append("regex", "^" + sessionSearch + "$")
                ));

                queries.add(sessionQuery);
            }

            if (!partySearch.isBlank()) {
                Document partyQuery = new Document("speaker_info.party_name", new Document("$regex", partySearch).append("$options", "i"));
                queries.add(partyQuery);
            }

            if (!titleSearch.isBlank()) {
                Document titleQuery = new Document("agenda_item", new Document("$regex", titleSearch).append("$options", "i"));
                queries.add(titleQuery);
            }

            if (queries.size() > 0) {
                Document query = new Document("$and", queries);
                aggregatePipeline.add(Aggregates.match(query));
            }

            aggregatePipeline.add(new Document("$skip", page * display));
            aggregatePipeline.add(new Document("$limit", display));

            AggregateIterable<Document> aggregatedSpeeches = MongoDatabaseHandler_Impl.aggregateMDB(MongoDatabaseHandler_Impl.getCollectionSpeeches(), aggregatePipeline);

            List<String> speechCards = new ArrayList<>();

            SpeechCardRenderer renderer = new SpeechCardRenderer();
            for (Document speech : aggregatedSpeeches)
                speechCards.add(renderer.toHtml(speech));

            Map<String, Object> model = new HashMap<>();
            model.put("speeches", speechCards);

            model.put("speaker", speakerSearch);
            model.put("session", sessionSearch);
            model.put("party", partySearch);
            model.put("title", titleSearch);

            model.put("page", page);
            model.put("display", display);

            renderCharts(aggregatedSpeeches, model);

            renderPage("diagrams.ftl", ctx, model);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdateExportPage(Context ctx) {
        try {
            String searchString = ctx.queryParam("search");
            String sort = ctx.queryParam("sort") == null ? "1" : ctx.queryParam("sort");
            int page = Integer.parseInt(ctx.queryParam("page") == null ? "0" : ctx.queryParam("page"));
            int display = Integer.parseInt(ctx.queryParam("display") == null ? "25" : ctx.queryParam("display"));

            Document aggregateObject = new Document("$lookup", new Document("from", "speaker")
                    .append("localField", "speaker_id")
                    .append("foreignField", "_id")
                    .append("as", "speaker_info"));

            List<Bson> aggregatePipeline = new ArrayList<>();
            aggregatePipeline.add(aggregateObject);
            aggregatePipeline.add(new Document("$unwind", "$speaker_info"));

            if (searchString != null) {
                /*search by nlp topic*/
                Document q = new Document("$expr", new Document("$regexMatch",
                        new Document("input", new Document("$toString", "$session_nr"))
                                .append("regex", searchString)
                                .append("options", "i")
                ));

                Document query = new Document("$or", List.of(
                        q,
                        new Document("agenda_item", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.lastname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.firstname", new Document("$regex", searchString).append("$options", "i")),
                        new Document("speaker_info.party_name", new Document("$regex", searchString).append("$options", "i"))
                ));

                aggregatePipeline.add(Aggregates.match(query));
            }

            switch (sort) {
                case "1":
                    aggregatePipeline.add(new Document("$sort", new Document("session_nr", 1)));
                    break;
                case "2":
                    aggregatePipeline.add(new Document("$sort", new Document("agenda_item", 1)));
                    break;
                case "3":
                    aggregatePipeline.add(new Document("$sort", new Document("speaker_info.lastname", 1)));
                    break;
                case "4":
                    aggregatePipeline.add(new Document("$sort", new Document("speaker_info.party_name", 1)));
                    break;
                case "5":
                    //sort by nlp topic
                    break;
                default:
                    break;
            }

            aggregatePipeline.add(new Document("$skip", page * display));
            aggregatePipeline.add(new Document("$limit", display));

            AggregateIterable<Document> aggregatedSpeeches = MongoDatabaseHandler_Impl.aggregateMDB(
                    MongoDatabaseHandler_Impl.getCollectionSpeeches(), aggregatePipeline);

            List<Document> speechDocuments = new ArrayList<>();
            for (Document aggregatedSpeech : aggregatedSpeeches) {
                Document speaker = MongoDatabaseHandler_Impl.getCollectionSpeaker().find(
                        new Document("_id", aggregatedSpeech.get("speaker_id"))).first();
                assert speaker != null;
                String speakerName = speaker.getString("lastname") + " " + speaker.getString("firstname");

                aggregatedSpeech.append("speakerName", speakerName);
                speechDocuments.add(aggregatedSpeech);
            }

            // user requests export:
            List<String> selectedSpeeches = ctx.queryParams("selectedSpeeches");
            String action = ctx.queryParam("action");

            if (selectedSpeeches != null && !selectedSpeeches.isEmpty()) {
                System.out.println("Selected speeches: " + selectedSpeeches);
                switch (action) {
                    case "export_pdf":
                        System.out.println("User requests PDF export");
                        TeXMaking.makeTex(selectedSpeeches, "pdf");
                        break;
                    case "export_xmi":
                        System.out.println("User requests XMI export");
                        TeXMaking.makeTex(selectedSpeeches, "xmi");
                        break;
                    case "default":
                        break;
                }
            }

            Map<String, Object> model = new HashMap<>();
            model.put("speeches", speechDocuments);
            model.put("search", searchString);
            model.put("sort", sort);
            model.put("page", page);
            model.put("display", display);
            renderPage("export.ftl", ctx, model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdateHandbookPage(Context ctx) {
        renderPage("handbook.ftl", ctx, null);
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdateSearchPage(Context ctx) {
        renderPage("search.ftl", ctx, null);
    }

    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void onUpdatePreviewPage(Context ctx) {
        renderPage("preview.ftl", ctx, null);
    }
}
