<#macro article_list>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">제목</th>
            <th scope="col">작성자</th>
            <th scope="col">작성 시각</th>
        </tr>
        </thead>
        <tbody>
        <#list fixedArticleId as article>
            <tr class="table-primary">
                <th scope="row">${article}</th>
                <td><a href="/articles/${article}">${fixedArticleTitle[article_index]}</a></td>
                <td>${fixedArticleWriter[article_index]}</td>
                <td>${fixedArticleTime[article_index]}</td>
            </tr>
        </#list>
        <#list articleId as article>
            <tr>
                <th scope="row">${article}</th>
                <td><a href="/articles/${article}">${articleTitle[article_index]}</a></td>
                <td>${articleWriter[article_index]}</td>
                <td>${articleTime[article_index]}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</#macro>