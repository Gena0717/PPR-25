package export.tex_building_blocks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * abstract class containing methods to generate tex files that can be exported
 * @author Sophie Kaiser
 */
public abstract class Texify {

    /**
     * file name without ending
     */
    private final String fileName;

    /**
     * path for the file, from root, including the file name and ending
     */
    private final String pathFileName;

    /**
     * the text the tex file will contain once generated
     */
    private LinkedList<String> texText = new LinkedList<>();

    /**
     * basic constructor for a class that has the toTex() method
     *
     * @param fileName the name the tex file will have
     * @param texPath the directory the tex file will be generated in
     * @author Sophie Kaiser
     */
    Texify(String fileName, String texPath) {
        this.fileName = fileName;
        this.pathFileName = texPath + "/" + fileName + ".tex";
    }

    /**
     * get the name of the tex file
     *
     * @return the file name, without ending
     * @author Sophie Kaiser
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * get the text the tex file will contain once generated
     *
     * @return the text of the future tex file
     * @author Sophie Kaiser
     */
    public LinkedList<String> getTexText(){
        return this.texText;
    }

    /**
     * add a line of text to the text of the future tex file,
     * protected so that only classes that extend Texify or that
     * are in the same package can modify the text contents
     *
     * @param text line of text to be added to the text of the tex file
     * @author Sophie Kaiser
     */
    protected void addToTexText(String text){
        this.texText.add(text);
    }

    /**
     * generate a tex file from the text in the class
     *
     * @return path for the file, from root, including the file name and ending
     * @author Sophie Kaiser
     */
    public String toTex(){
        try {
            Files.write(Paths.get(this.pathFileName), this.texText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.pathFileName;
    }
}
