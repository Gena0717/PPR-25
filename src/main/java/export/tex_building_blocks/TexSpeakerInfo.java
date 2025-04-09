package export.tex_building_blocks;

/**
 * represents a tex file that includes a speaker's full name,
 * party affiliation and, if available, picture
 * @author Sophie Kaiser
 */
public class TexSpeakerInfo extends Texify {

    /**
     * constructor for speaker info
     * @param fileName name for the tex file to be generated
     * @param texPath directory where the tex file will be generated
     * @author Sophie Kaiser
     */
    TexSpeakerInfo(String fileName, String texPath) {
        super(fileName, texPath);
        this.addToTexText("Last Name, Address Title First Name Adel Prefix (PARTY)");
    }
}
