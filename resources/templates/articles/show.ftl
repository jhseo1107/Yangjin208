<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <table class="table">
        <tr>
            <th scope="row">제목</th>
            <td>${articleTitle}</td>
        </tr>
        <tr>
            <th scope="row">작성자</th>
            <td>${articleWriter}</td>
        </tr>
        <tr>
            <th scope="row">작성일</th>
            <td>${articleTime}</td>
        </tr>
        <tr>
            <th scope="row">공지 여부</th>
            <td>
                ${articleIsFixed?string('O', 'X')}
                <#if userSession.id == 1>
                    <form class="form-inline" method="post" action="/articles/update/${articleId}">
                        <button type="submit" class="btn btn-primary">수정</button>
                    </form>
                </#if>
            </td>
        </tr>
        <tr>
            <td colspan="2">
                ${articleContent}
            </td>
        </tr>
    </table>
</@layout.display>