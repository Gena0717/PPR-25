package export.tex_building_blocks;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;

/**
 * represents a tex file that contains a session protocol
 * @author Sophie Kaiser
 */
public class TexSessionProtocol extends Texify {

    /**
     * constructor for a session protocol
     *
     * @param fileName file name
     * @param texPath
     * @param agendaItemMap map mapping all agenda items to the speeches they contain
     * @author Sophie Kaiser
     */
    TexSessionProtocol(
            String fileName,
            String texPath,
            SortedMap<String, LinkedList<TexSpeech>> agendaItemMap
    ) {
        super(fileName, texPath);
        // file name = session number
        this.addToTexText("\\section{Session" + fileName + "}");
        for (String agendaItem : agendaItemMap.keySet()) {
            this.addToTexText("\\subsection{" + agendaItem + "}");
            for (TexSpeech speech : agendaItemMap.get(agendaItem)) {
                this.addToTexText("\\input{" + speech.toTex() + "}");
            }
        }
    }
}
