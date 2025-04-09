<#import "components/base.ftl" as layout>

<@layout.base title="Parliament Explorer">
    <h1>Multimodal Parliament Explorer 11 3</h1>
    <p class="large">
        Welcome to the Multimodal Parliament Explorer 11 3.<br>
        This site enables you to explore speeches held in the german parliament during the 20th legislative period.<br>
    </p>
    <p class="large">
        The speeches were analysed for...
        <list>
            <li>topics with <a href="https://huggingface.co/chkla/parlbert-german-v2">ParlBERT v2</a>,</li>
            <li>tokens, sentences and more with <a href="https://github.com/explosion/spaCy">spaCy</a>,</li>
            <li>sentiment with <a href="https://vadersentiment.readthedocs.io/en/latest/">GerVaderSentiment</a>,</li>
            <li>transcription of videos with <a href="https://github.com/m-bain/whisperX">WhisperX</a>.</li>
        </list>
    </p>
    <p class="large"> Other tools used in this project:
        <list>
            <li>handling of data <a href="https://www.mongodb.com/">MongoDB</a>,</li>
            <li>containerization <a href="https://www.docker.com/">Docker</a>,</li>
            <li>rest routes <a href="https://javalin.io/">Javalin</a>,</li>
            <li>web rendering <a href="https://freemarker.apache.org/">Freemarker</a>.</li>
            <li>natural language processing (NLP) <a href="https://dkpro.github.io/dkpro-core/">DKPro Core</a>,</li>
            <li>natural language processing (NLP) <a href="https://texttechnologylab.github.io/DockerUnifiedUIMAInterface/">Docker Unified UIMA Interface (DUUI)</a>.</li>
        </list>
    </p>
    <p class="large">
        For more information, please consult the <a href="/handbook">user handbook</a>.
    </p>
</@layout.base>
