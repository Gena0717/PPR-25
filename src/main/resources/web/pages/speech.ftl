<#import "components/base.ftl" as layout>

<@layout.base title="Speech ${speech._id}">
    <!--title-->
    <h1>Sitzung ${speech.session_nr}
        ${speech.agenda_item!"no agenda item"}</h1>
    <div class="speech">
        <!--image-->
        <div class="small-speaker-image">
            <a class="speaker-image" href="/speakers/${speech.speaker_id}" target="_blank"></a>
        </div>

        <div class="video-and-nlp">
            <!--video-->
            <#if video??>
                <iframe src="${video.url}" class="video" title="${video.video_id}"></iframe>
            </#if>
            <#if processedVideo??>
                <div class="audio-tokens" style="display:none;">
                    <#list processedVideo.audioTokens as audioToken>
                        ${audioToken.id}, ${audioToken.begin}, ${audioToken.end}, ${audioToken.timeStart}, ${audioToken.timeEnd}, ${audioToken.value}
                        <br>
                    </#list>
                </div>
            </#if>

            <!--nlp buttons-->
            <div class="nlp-buttons">
                <button class="tertiary" data-active="false" data-label="Entities" onclick="getEntities(this)">Show
                    Entities
                </button>
                <span class="entity-legend" style="display:none;">
                <span style="color: rgba(125,255,78,0.3);">org</span>
                <span style="color: rgba(255,78,246,0.3);">pers</span>
                <span style="color: rgba(255,234,78,0.3);">loc</span>
                <span style="color: rgba(255,255,255,0.3);">misc</span>
            </span>
                <button class="tertiary" data-active="false" data-label="Sentiment" onclick="getSentiments(this)">Show
                    Sentiments
                </button>
                <button class="tertiary" data-active="false" data-label="POS" onclick="getPos(this)">Show POS</button>
                <button class="tertiary" onclick="scrollToCharts()">NLP Charts</button>
            </div>

            <br>
        </div>
        <!--speech-->
        <div class="speech-container">
            <span class="full-sentiment"
                  style="display:none;"><h2>Full sentiment: ${processedSpeech.sentiments[0].sentiment}</h2></span>
            <#list speech.contents as content>
                <p id="${content._id}" class="${content.content_tag?lower_case}"
                   data-original="${content.text}">${content.text}</p>
            </#list>
        </div>
        <h2>NLP Charts</h2>
        <div id="charts" class="speech-container">
            <h3>Topics</h3>
            <div id="topics"></div>
            <h3>Parts of Speeches</h3>
            <div id="pos"></div>
            <h3>Sentiments</h3>
            <div id="sentiments"></div>
            <h3>Named Entities</h3>
            <div id="entities"></div>
        </div>
    </div>

    <script src="/js/fetchSpeakerImage.js"></script>
    <script src="/js/spanWrap.js"></script>
    <script src="/js/toggle.js"></script>

    <!--fetch image listener-->
    <script>
        document.addEventListener('DOMContentLoaded', function () {
            fetchSpeakerImage('${speech.speaker_id}');
        });
    </script>

    <!--entities to json-->
    <script>
        function getEntities(button) {
            let entitiesJson = JSON.parse('[<#list processedSpeech.namedEntities as e>{"begin": ${e.begin}, "end": ${e.end}, "value": "${e.value}"}<#if e_has_next>,</#if></#list>]');
            toggle(button, entitiesJson, "Entities");
        }
    </script>

    <!--sentiments to json-->
    <script>
        function getSentiments(button) {
            let sentimentJson = JSON.parse('[<#list processedSpeech.sentiments as s>{"begin": ${s.begin!""}, "end": ${s.end!""}, "sentiment": "${s.sentiment!""}", "pos": "${s.pos!""}", "neu": "${s.neu!""}", "neg": "${s.neg!""}"}<#if s_has_next>,</#if></#list>]');
            toggle(button, sentimentJson, "Sentiment");
        }
    </script>

    <!--pos to json-->
    <script>
        function getPos(button) {
            let posJson = JSON.parse('[<#list processedSpeech.posTags as p>{"begin": ${p.begin!""}, "end": ${p.end!""}, "PosValue": "${p.PosValue!""}", "coarseValue": "${p.coarseValue!""}"}<#if p_has_next>,</#if></#list>]');
            toggle(button, posJson, "POS");
        }
    </script>

    <script>
        function scrollToCharts() {
            document.getElementById("charts").scrollIntoView();
            console.log("executes");
        }
    </script>

    <script src="https://d3js.org/d3.v6.js"></script>
    <script src="/js/createBubbleChart.js"></script>
    <script src="/js/createVerticalBarChart.js"></script>
    <script src="/js/createRadarChart.js"></script>
    <script src="/js/createSunburstChart.js"></script>
    <script>
        var topics = JSON.parse('${topics?js_string}');
        var pos = JSON.parse('${pos?js_string}');
        var sentiments = JSON.parse('${sentiments?js_string}')
        var entities = JSON.parse('${entities?js_string}')
        document.getElementById("topics").append(createBubbleChart(topics, 800, 600));
        document.getElementById("pos").append(createVerticalBarChart(pos, 800, 600));
        document.getElementById("sentiments").append(createRadarChart(sentiments, 800, 600));
        document.getElementById("entities").append(createSunburstChart(entities, 800, 600));
    </script>
</@layout.base>
