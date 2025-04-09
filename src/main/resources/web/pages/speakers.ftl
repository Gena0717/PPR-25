<#import "components/base.ftl" as layout>

<@layout.base title="Speakers">
    <h1>Speakers</h1>
    <div class="search-container">
        <div class="horizontal">
            <#assign sort = sort>
            <#assign search = search>
            <form id="changeSort" action="/speakers" method="get">
                <input type="hidden" name="search" value="${search}">
                <label for="sortselect">Sort by</label>
                <select id="sortselect" name="sort" onchange="this.form.submit()">
                    <option value="1" <#if sort =="1"> selected</#if> >Firstname</option>
                    <option value="2" <#if sort =="2"> selected</#if> >Lastname</option>
                    <option value="3" <#if sort =="3"> selected</#if> >Party</option>
                </select>
            </form>
            <form id="searchForm" action="/speakers" method="get">
                <div class="horizontal">
                    <input id="searchInput" name="search" class="searchbar" type="text" maxlength="30"
                           placeholder="Search..." value="${search}"/>
                    <button class="tertiary">Submit</button>
                <input type="hidden" name="sort" value=${sort}>
                </div>
            </form>
        </div>
    </div>
    <div>
        <span class='cards-header'><p>Lastname</p><p>Firstname</p><p>Party</p></span>
        <#list speakers as card>
            ${card}
        </#list>
    </div>
</@layout.base>