<#import "components/base.ftl" as layout>

<@layout.base title="Handbook">
    <h1>Multimodal Parliament Explorer - Handbook</h1>
    <p class="large">
        How to use: Use the navigation at the top to navigate to<br>
        http://localhost:27020/diagrams,<br>
        http://localhost:27020/speakers,<br>
        http://localhost:27020/speeches or<br>
        http://localhost:27020/export.<br>
        <br>
        /diagrams: Enter keywords or characters to filter speeches by, and click the result to view a speech.<br>
        <br>
        /speakers: Use the search field to filter and the dropdown to sort speakers and click the result to view a speaker profile.<br>
        On the speaker profile, you may change speaker pictures by choosing from a dropdown, if more than one image is available.<br>
        <br>
        /speeches: Use the search field to filter and the dropdown to sort speeches and click the result to view a speech.<br>
        <br>
        /speeches/(variable speech-id): The title of a speech page consists of the session number and agenda point.<br>
        At the top left corner of the speech container is the image of the speeches' speaker. Click it to navigate to the speakers' profile.<br>
        Speaker introductions are displayed in <b>bold font</b>.<br>
        Comments are displayed in <i><span style="color:#76808c">(parentheses, italics and dimmed color)</span></i>.<br>
        Quoutes also are <span style="color:#76808c">dimmed</span><, but also have a border on the left side.<br>
        <br>
        Click on the video in the top right corner, to play the video which corresponds with the speech you're viewing.<br>
        Beneath the video, you will find three buttons which highlight the speech text:<br>
        Click "Show Entities" to toggle on Entity highlighting and view the legend - of which color means what.<br>
        Click "Show Sentiment" to toggle on Sentiment highlighting and to display the overall speech sentiment above the speech..<br>
        Click "Show POS" to toggle on POS highlighting.<br>
        <br>
        At the bottom of the page, you will find Topics, Parts of Speeches, Sentiments and Named Entity charts.<br>
        <br>
        /export: select one or multiple speeches which you would like to export. You may also search for and sort speeches.<br>
        to the left, click export pdf or xmi. It will send the speech information to the backend and a new file will be created.<br>
        Information about the new file will be displayed in the white rectangle on the page.

    </p>
</@layout.base>
