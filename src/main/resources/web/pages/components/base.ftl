<#macro base title="">
    <!DOCTYPE html>
    <html lang="de">
        <head>
            <meta charset="UTF-8">
            <title>${title}</title>
            <#include "imports.ftl">
        </head>
        <body>
            <#include "header.ftl">
            <div class="inner">
                <#nested/>
            </div>
            <#include "footer.ftl">
        </body>
    </html>
</#macro>