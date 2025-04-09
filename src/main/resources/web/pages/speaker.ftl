<#import "components/base.ftl" as layout>

<@layout.base title="${lastName}">
    <h1>${title} ${firstName} ${lastName}</h1>
    <div class="speaker">
        <div class="info-container">
            <table>
                <tbody>
                <tr>
                    <td><p class="dim">Partei</p></td>
                    <td><p>${party}</p></td>
                </tr>
                <#if birthplace?has_content>
                    <tr>
                    <td><p class="dim">Eckdaten</p></td>
                    <td>
                        <div class="horizontal">
                            <p>${birthplace}</p>
                            <p>
                                <#if birthDate?has_content>*${birthDate}</#if>
                                <#if deathDate?has_content> - †${deathDate}</#if>
                            </p>
                        </div>
                    </td>
                    </tr></#if>
                <#if sex?has_content>
                    <tr>
                    <td><p class="dim">Geschlecht</p></td>
                    <td><p>${sex}</p></td>
                    </tr></#if>
                <#if family?has_content>
                    <tr>
                    <td><p class="dim">Familienstand</p></td>
                    <td><p>${family}</p></td>
                    </tr></#if>
                <#if religion?has_content>
                    <tr>
                    <td><p class="dim">Religion</p></td>
                    <td><p>${religion}</p></td>
                    </tr></#if>
                <#if job?has_content>
                    <tr>
                    <td><p class="dim">Beruf</p></td>
                    <td><p>${job}</p></td>
                    </tr></#if>
                <#if vita?has_content>
                    <tr>
                    <td><p class="dim">Vita</p></td>
                    <td><p style="text-align: justify;">${vita}</p></td>
                    </tr></#if>
                </tbody>
            </table>
        </div>

        <div class="speaker-img">
            <#if currentImage??>
                <img id="displayedImage" src="${currentImage}" alt="Image"/>
            <#else>
                <span class="fa-solid fa-image-portrait"></span>
            </#if>
            <#if images?? && images?size gt 1>
                <select id="selectImage" name="img" onchange="updateImage()">
                    <#list images as imageElement>
                        <option value="${imageElement}" <#if currentImage == imageElement>selected</#if>>
                            Image ${imageElement?counter}
                        </option>
                    </#list>
                </select>
            </#if>
        </div>
    </div>
    <div>
        <#assign sort = sort>
        <#assign display = display>
        <#assign page = page>

        <br/><br/><br/>

        <div class="search-container">
            <div class="horizontal">
                <form id="changeSort" action="/speakers/${id}" method="get">
                    <input type="hidden" name="display" value="${display}">
                    <input type="hidden" name="page" value="${page}">

                    <#if search?? && search?has_content>
                        <input type="hidden" name="search" value="${search}">
                    </#if>

                    <label for="sort">Sort by</label>
                    <select id="sort" name="sort" onchange="this.form.submit()">
                        <option value="1" <#if sort =="1"> selected</#if> >Session number</option>
                        <option value="2" <#if sort =="2"> selected</#if> >Agenda Title</option>
                    </select>
                </form>

                <form id="searchForm" action="/speakers/${id}" method="get">
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

        <span class='cards-header'><p>Session number</p><p>Agenda Title</p><p>Speaker</p><p>Party</p></span>

        <#list speeches as speech>
            ${speech}
        </#list>

        <div class="horizontal">
            <form id="changePage" action="/speakers/${id}" method="get">
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

            <form id="changeDisplay" action="/speakers/${id}" method="get">
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

    <script src="/js/updateImage.js"></script>
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
