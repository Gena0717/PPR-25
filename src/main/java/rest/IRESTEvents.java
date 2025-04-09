package rest;

import io.javalin.http.Context;

/**
 * Interface for events invoked by the RESTHandler
 * @author Ishak Bouaziz
 * */
public interface IRESTEvents {
    /**
     * Invoked when landing page is opened
     * @author Ishak Bouaziz
     * */
    void onUpdateLandingPage(Context ctx);
    /**
     * Invoked when speakers page is opened
     * @author Ishak Bouaziz
     * */
    void onUpdateSpeakersPage(Context ctx);
    /**
     * Invoked when a single speaker page is opened
     * @author Ishak Bouaziz
     * */
    void onUpdateSingleSpeakerPage(Context ctx);
    /**
     * Invoked when the speeches page is opened
     * @author Ishak Bouaziz
     * */
    void onUpdateSpeechesPage(Context ctx);
    /**
     * Invoked when a single speech page is opened
     * @author Ishak Bouaziz
     * */
    void onUpdateSingleSpeechPage(Context ctx);
    /**
     * Invoked when the diagrams page is opened
     * @author Oliwia Daszczynska
     * */
    void onUpdateDiagramsPage(Context ctx);
    /**
     * Invoked when the export page is opened
     * @author Oliwia Daszczynska
     * */
    void onUpdateExportPage(Context ctx);
    /**
     * Invoked when the handbook page is opened
     * @author Oliwia Daszczynska
     * */
    void onUpdateHandbookPage(Context ctx);
    /**
     * Invoked when the search page is opened
     * @author Oliwia Daszczynska
     * */
    void onUpdateSearchPage(Context ctx);

    /**
     * Invoked when the preview page is opened
     *  @author Oliwia Daszczynska
     * */
    void onUpdatePreviewPage(Context ctx);
}
