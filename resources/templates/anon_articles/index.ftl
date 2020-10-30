<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <h2 style="text-align: center;">익명 게시글 목록</h2>

    <table class="table">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">제목</th>
            <th scope="col">주제</th>
            <th scope="col">작성 시각</th>
        </tr>
        </thead>
        <tbody>
        <#list anonArticleId as article>
            <tr>
                <th scope="row">${article}</th>
                <td><a href="/anon_articles/${article}">${anonArticleTitle[article_index]}</a></td>
                <td>${anonArticleTopic[article_index]}</td>
                <td>${anonArticleTime[article_index]}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@layout.display>