<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <table class="table">
        <tr>
            <th scope="row">제목</th>
            <td>${anonArticleTitle}</td>
        </tr>
        <tr>
            <th scope="row">주제</th>
            <td>${anonArticleTopic}</td>
        </tr>
        <tr>
            <th scope="row">작성일</th>
            <td>${anonArticleTime}</td>
        </tr>
        <tr>
            <td colspan="2">
                ${anonArticleContent}
            </td>
        </tr>
    </table>
</@layout.display>