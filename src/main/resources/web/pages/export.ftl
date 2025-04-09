<#import "components/base.ftl" as layout>

<@layout.base title="Export">
    <div class="search-container">
        <div class="horizontal">
            <#if search??>
                <#assign search = search>
            </#if>
            <#assign sort = sort>
            <#assign display = display>
            <#assign page = page>
            <form id="changeSort" action="/export" method="get">
            <input type="hidden" name="display" value="${display}">
            <input type="hidden" name="page" value="${page}">
            <#if search?? && search?has_content>
                <input type="hidden" name="search" value="${search}">
            </#if>
            <label for="sort">Sort by</label>
            <select id="sort" name="sort" onchange="this.form.submit()">
                <option value="1" <#if sort =="1"> selected</#if> >Session number</option>
                <option value="2" <#if sort =="2"> selected</#if> >Agenda Title</option>
                <option value="3" <#if sort =="3"> selected</#if> >Speaker</option>
                <option value="4" <#if sort =="4"> selected</#if> >Party</option>
                <option value="5" <#if sort =="4"> selected</#if> >NLP Topic</option>
            </select>
            </form>
            <form id="searchForm" action="/export" method="get">
            <input type="hidden" name="sort" value="${sort}">
            <input type="hidden" name="display" value="${display}">
            <input type="hidden" name="page" value="${0}">
            <div class="horizontal">
            <input id="searchInput" name="search" class="searchbar" type="text" maxlength="30"
            placeholder="Search..."/>
            <button class="tertiary">Submit</button>
            </div>
            </form>
        </div>
    </div>
    <div>
        <form action="/export" method="get">
            <#list speeches as speech>
                <label class="export-card">
                    <input class="export-checkbox" type="checkbox" name="selectedSpeeches" value="${speech._id}">
                        <span>${speech.session_nr}. Sitzung</span><span>${speech.agenda_item!"no agenda title"}</span><span>${speech.speakerName}</span>
                </label>
            </#list>
        <div class="export-window">
            <button class="tertiary" type="submit" name="action" value="export_pdf">Export PDF</button>
            <button class="tertiary" type="submit" name="action" value="export_xmi">Export XMI</button>
            <div class="preview">
                <iframe src="/preview" width="100%" height="300" style="border:none;">
                </iframe>
            </div>
        </div>
        </form>
    </div>
    <div class="horizontal export-pages">
        <form id="changePage" action="/export" method="get">
            <input type="hidden" name="sort" value="${sort}">
            <input type="hidden" name="display" value="${display}">

            <#if search?? && search?has_content>
                <input type="hidden" name="search" value="${search}">
            </#if>

            <input type="hidden" name="page" id="pageInput" value="${page}">
            <label>${page * display} - ${page * display + display}</label>
            <button class="tertiary" type="submit" id="previous"><</button>
            <button class="tertiary" type="submit" id="next">></button>
        </form>

        <form id="changeDisplay" action="/export" method="get">
            <input type="hidden" name="sort" value="${sort}">
            <input type="hidden" name="page" value="${page}">

            <#if search?? && search?has_content>
                <input type="hidden" name="search" value="${search}">
            </#if>

            <label for="display">Display</label>
            <select id="display" name="display" onchange="this.form.submit()">
                <option value="25" <#if display ==25> selected</#if> >25</option>
                <option value="50" <#if display ==50> selected</#if> >50</option>
                <option value="75" <#if display ==75> selected</#if> >75</option>
                <option value="100" <#if display ==100> selected</#if> >100</option>
            </select>
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
