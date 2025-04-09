package export.tex_building_blocks;

import java.io.IOException;
import java.util.LinkedList;

/**
 * represents a full tex document that can be compiled into pdf
 * @author Sophie Kaiser
 */
public class TexDocument extends Texify {

    /**
     * preamble to tex documents created with this class
     */
    private String preamble = """
            \\documentclass[11pt]{article}
            \\usepackage[utf8]{inputenc}
            \\usepackage[a4paper, margin=2.5cm]{geometry}
            """;

    /**
     * constructor for a tex document containing one or multiple session protocols
     *
     * @param fileName the name the tex file will have
     * @param texPath the directory the tex file will be generated in
     * @param sessionProtocols session protocols to be displayed
     * @author Sophie Kaiser
     */
    TexDocument(
            String fileName,
            String texPath,
            LinkedList<TexSessionProtocol> sessionProtocols
    ) throws IOException {
        super(fileName, texPath);
        // add preamble
        this.addToTexText(preamble);
        this.addToTexText("\\begin{document}");
        this.addToTexText("\\tableofcontents");
        // add content
        // toTex() generates the tex file and returns its name
        for (TexSessionProtocol sessionProtocol : sessionProtocols) {
            this.addToTexText("\\input{" + sessionProtocol.toTex() + "}");
        }
        this.addToTexText("\\end{document}");
    }
}
