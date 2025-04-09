package frontend;

import org.bson.Document;

/**
 * Interface that provides HTML rendering.
 * */
public interface IRenderable {
    /**
     * Outputs a component in HTML format based on a given document.
     * @return String in HTML format.
     * @author Ishak Bouaziz
     * */
    String toHtml(Document document);
}
