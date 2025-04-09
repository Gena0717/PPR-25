package rest;

import helpers.Config;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to manage a REST API through a simple event system.
 *
 * @author Ishak Bouaziz
 */
public class RESTHandler implements RESTInterface {
    private final List<IRESTEvents> eventListeners;

    private static RESTHandler s_Instance;

    private RESTHandler() {
        eventListeners = new ArrayList<>();
    }

    /**
     * Retrieves the RESTHandler instance.
     *
     * @return instance
     * @author Ishak Bouaziz
     */
    public static RESTHandler get() {
        if (s_Instance == null)
            s_Instance = new RESTHandler();

        return s_Instance;
    }
    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void post() {
    }
    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void put() {
    }
    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void delete() {
    }
    /**
     * @author Oliwia Daszczynska
     */
    @Override
    public void patch() {
    }

    /**
     * Creates the endpoints for the application
     *
     * @author Ishak Bouaziz
     * @author Oliwia Daszczynska
     */
    public void CreateRoutes() {
        try {
            Config config = Config.get();
            Javalin app = Javalin.create(cfg -> {
                cfg.bundledPlugins.enableCors(cors -> {
                    cors.addRule(it -> {
                        it.anyHost();
                    });
                });
                cfg.staticFiles.add(staticFiles -> {
                    staticFiles.directory = config.getStaticFilesPath();
                });

            });

            app.get("/", ctx -> {
                updateLandingPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/diagrams", ctx -> {
                updateDiagramsPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/export", ctx -> {
                updateExportPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/preview", ctx -> {
                updatePreviewPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/handbook", ctx -> {
                updateHandbookPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/search", ctx -> {
                updateSearchPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/speakers", ctx -> {
                updateSpeakersPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/speakers/{speaker}", ctx -> {
                updateSingleSpeakerPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/speeches", ctx -> {
                updateSpeechesPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });

            app.get("/speeches/{id}", ctx -> {
                updateSingleSpeechPage(ctx);
                System.out.println("Request Status: " + ctx.status());
            });


            app.start(config.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds an event listener.
     *
     * @author Ishak Bouaziz
     */
    public void addEventListener(IRESTEvents listener) {
        eventListeners.add(listener);
    }

    /**
     * Removes an event listener.
     *
     * @author Ishak Bouaziz
     */
    public void removeEventListener(IRESTEvents listener) {
        eventListeners.remove(listener);
    }

    /**
     * Invokes the IRESTEvents.onUpdateLandingPage event when the endpoint is called.
     *
     * @author Ishak Bouaziz
     */
    private void updateLandingPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateLandingPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateDiagramsPage event when the endpoint is called.
     * @author Oliwia Daszczynska
     */
    private void updateDiagramsPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateDiagramsPage(ctx);
    }

    /**
     *  Invokes the IRESTEvents.onUpdateExportPage event when the endpoint is called.
     * @author Oliwia Daszczynska
     */
    private void updateExportPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateExportPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateHandbookPage event when the endpoint is called.
     * @author Oliwia Daszczynska
     */
    private void updateHandbookPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateHandbookPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateSearchPage event when the endpoint is called.
     * @author Oliwia Daszczynska
     */
    private void updateSearchPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateSearchPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdatePreviewPage event when the endpoint is called.
     * @author Oliwia Daszczynska
     */
    private void updatePreviewPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdatePreviewPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateSpeakersPage event when the endpoint is called.
     *
     * @author Ishak Bouaziz
     */
    private void updateSpeakersPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateSpeakersPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateSingleSpeakerPage event when the endpoint is called.
     *
     * @author Ishak Bouaziz
     */
    private void updateSingleSpeakerPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateSingleSpeakerPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateSpeechesPage event when the endpoint is called.
     *
     * @author Ishak Bouaziz
     */
    private void updateSpeechesPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateSpeechesPage(ctx);
    }

    /**
     * Invokes the IRESTEvents.onUpdateSingleSpeechPage event when the endpoint is called.
     *
     * @author Ishak Bouaziz
     */
    private void updateSingleSpeechPage(Context ctx) {
        for (IRESTEvents listener : eventListeners)
            listener.onUpdateSingleSpeechPage(ctx);
    }
}
