package export.tex_building_blocks;

/**
 * represents NLP information that is visualized using the TikZ package
 * @author Sophie Kaiser
 */
public class TexNLPVisualization extends Texify {

    /**
     * basic constructor for a class that has the toTex() method
     *
     * @param fileName the name the tex file will have
     * @param texPath  the directory the tex file will be generated in
     * @author Sophie Kaiser
     */
    TexNLPVisualization(String fileName, String texPath) {
        super(fileName, texPath);
    }
}
