<#import "components/base.ftl" as layout>

<@layout.base title="Speeches">
    <h1>Speeches</h1>
    <div class="search-container">
        <div class="horizontal">
            <#assign search = search>
            <#assign sort = sort>
            <#assign display = display>
            <#assign page = page>

            <form id="changeSort" action="/speeches" method="get">
                <input type="hidden" name="search" value="${search}">

                <label for="sort">Sort by</label>
                <select id="sort" name="sort" onchange="this.form.submit()">
                    <option value="1" <#if sort =="1"> selected</#if> >Session number</option>
                    <option value="2" <#if sort =="2"> selected</#if> >Agenda Title</option>
                    <option value="3" <#if sort =="3"> selected</#if> >Speaker</option>
                    <option value="4" <#if sort =="4"> selected</#if> >Party</option>
                </select>

                <input type="hidden" name="display" value="${display}">
                <input type="hidden" name="page" value="${page}">
            </form>
            <form id="searchForm" action="/speeches" method="get">
                <div class="horizontal">
                    <input id="searchInput" name="search" class="searchbar" type="text" maxlength="30"
                           placeholder="Search..." value="${search}"/>
                    <button class="tertiary">Submit</button>
                </div>

                <input type="hidden" name="sort" value="${sort}">
                <input type="hidden" name="display" value="${display}">
                <input type="hidden" name="page" value="${0}">
            </form>
        </div>
    </div>
    <div>
        <span class='cards-header'><p>Session number</p><p>Agenda Title</p><p>Speaker</p><p>Party</p></span>
        <#list speeches as card>
            ${card}
        </#list>
        <div class="horizontal">
            <form id="changePage" action="/speeches" method="get">
                <input type="hidden" name="search" value="${search}">
                <input type="hidden" name="sort" value="${sort}">
                <input type="hidden" name="display" value="${display}">

                <input type="hidden" name="page" id="pageInput" value="${page}">
                <label>${page * display} - ${page * display + display}</label>
                <button class="tertiary" type="submit" id="previous"><</button>
                <button class="tertiary" type="submit" id="next">></button>
            </form>

            <form id="changeDisplay" action="/speeches" method="get">
                <input type="hidden" name="search" value="${search}">
                <input type="hidden" name="sort" value="${sort}">

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
    </div>
    <script src="/js/pageSwitch.js"></script>
    <script>
        setChangePageEvents(
            document.getElementById("next"),
            document.getElementById("previous"),
            document.getElementById("pageInput"),
            document.getElementById("changePage")
        );
    </script>
</@layout.base>
