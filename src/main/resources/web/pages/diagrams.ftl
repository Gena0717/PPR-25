<#import "components/base.ftl" as layout>

<@layout.base title="Diagrams">
    <h1>Diagrams</h1>
    <div>
        <#assign speaker = speaker>
        <#assign session = session>
        <#assign party = party>
        <#assign title = title>
        <#assign page = page>
        <#assign display = display>

        <div class="horizontal" style="width:100%;">
            <form id="searchSpeaker" action="/diagrams" method="get">
                <input id="speakerSearch" name="speaker" class="searchbar" type="text" maxlength="30"
                       placeholder="Speaker..." value="${speaker}"/>

                <input type="hidden" name="session" value="${session}">
                <input type="hidden" name="party" value="${party}">
                <input type="hidden" name="title" value="${title}">
                <input type="hidden" name="display" value="${display}">
            </form>

            <form id="searchSession" action="/diagrams" method="get">
                <input type="hidden" name="speaker" value="${speaker}">

                <input id="sessionSearch" name="session" class="searchbar" type="text" maxlength="30"
                       placeholder="Session..." value="${session}"/>

                <input type="hidden" name="party" value="${party}">
                <input type="hidden" name="title" value="${title}">
                <input type="hidden" name="display" value="${display}">
            </form>

            <form id="searchParty" action="/diagrams" method="get">
                <input type="hidden" name="speaker" value="${speaker}">
                <input type="hidden" name="session" value="${session}">

                <input id="partySearch" name="party" class="searchbar" type="text" maxlength="30"
                       placeholder="Party..." value="${party}"/>

                <input type="hidden" name="title" value="${title}">
                <input type="hidden" name="display" value="${display}">
            </form>

            <form id="searchTitle" action="/diagrams" method="get">
                <input type="hidden" name="speaker" value="${speaker}">
                <input type="hidden" name="session" value="${session}">
                <input type="hidden" name="party" value="${party}">

                <input id="titleSearch" name="title" class="searchbar" type="text" maxlength="30"
                       placeholder="Title..." value="${title}"/>

                <input type="hidden" name="display" value="${display}">
            </form>

            <input id="topicSearch" name="title" class="searchbar" type="text" maxlength="30"
                   placeholder="Topic..."/>
        </div>
    </div>

    <div style="background-color: var(--background-color-light-half); margin-bottom:2em;">
        <h2 class="collapsible">Charts</h2>
        <div style="padding:0 3em 3em 3em; display: none">
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

    <div>
        <span class='cards-header'><p>Session number</p><p>Agenda Title</p><p>Speaker</p><p>Party</p></span>
        <#list speeches as card>
            ${card}
        </#list>
    </div>

    <div class="horizontal">
        <form id="changePage" action="/diagrams" method="get">
            <input type="hidden" name="speaker" value="${speaker}">
            <input type="hidden" name="session" value="${session}">
            <input type="hidden" name="party" value="${party}">
            <input type="hidden" name="title" value="${title}">
            <input type="hidden" name="display" value="${display}">

            <input type="hidden" name="page" id="pageInput" value="${page}">
            <label>${page * display} - ${page * display + display}</label>
            <button class="tertiary" type="submit" id="previous"><</button>
            <button class="tertiary" type="submit" id="next">></button>
        </form>

        <form id="changeDisplay" action="/diagrams" method="get">
            <input type="hidden" name="speaker" value="${speaker}">
            <input type="hidden" name="session" value="${session}">
            <input type="hidden" name="party" value="${party}">
            <input type="hidden" name="title" value="${title}">

            <label for="display">Display</label>
            <select id="display" name="display" onchange="this.form.submit()">
                <option value="25" <#if display ==25> selected</#if> >25</option>
                <option value="50" <#if display ==50> selected</#if> >50</option>
                <option value="75" <#if display ==75> selected</#if> >75</option>
                <option value="100" <#if display ==100> selected</#if> >100</option>
            </select>

            <input type="hidden" name="page" value="${page}">
        </form>
    </div>

    <script src="/js/collapsible.js"></script>
    <script src="https://d3js.org/d3.v6.js"></script>
    <script src="/js/createBubbleChart.js"></script>
    <script src="/js/createVerticalBarChart.js"></script>
    <script src="/js/createSunburstChart.js"></script>
    <script src="/js/createRadarChart.js"></script>
    <script src="/js/pageSwitch.js"></script>
    <script>
        collapsible();

        setChangePageEvents(
            document.getElementById("next"),
            document.getElementById("previous"),
            document.getElementById("pageInput"),
            document.getElementById("changePage")
        );

        var topics = JSON.parse('${topics?js_string}')
        var pos = JSON.parse('${pos?js_string}')
        var sentiments = JSON.parse('${sentiments?js_string}')
        var entities = JSON.parse('${entities?js_string}')

        document.getElementById("topics").append(createBubbleChart(topics, 800, 600));
        document.getElementById("pos").append(createVerticalBarChart(pos, 800, 600));
        document.getElementById("sentiments").append(createRadarChart(sentiments, 800, 600));
        document.getElementById("entities").append(createSunburstChart(entities, 800, 600));
    </script>
</@layout.base>
