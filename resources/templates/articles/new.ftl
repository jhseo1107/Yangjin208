<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <form method="post" action="/articles/create">
        <div class="form-group">
            <label for="articleTitle">게시글 제목</label>
            <input class="form-control" type="text" id="articleTitle" name="articleTitle">
        </div>
        <div class="form-group">
            <label for="articleWriter">글쓴이</label>
            <input class="form-control" type="text" id="articleWriter" disabled="disabled" value="${articleWriter}">
        </div>
        <#if userSession.id == 1>
            <div class="form-group">
                <label for="articleFixed">공지로 설정</label>
                <input class="form-control" type="checkbox" id="articleFixed" name="articleFixed">
            </div>
        </#if>
        <div class="form-group">
            <label for="articleContent">게시글 내용</label>
            <textarea class="form-control" id="articleContent" name="articleContent" rows="15"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">작성</button>
    </form>
</@layout.display>