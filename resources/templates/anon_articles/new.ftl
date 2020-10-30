<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <h2 style="text-align: center;">익명으로 글쓰기</h2>

    <form method="post" action="/anon_articles/create">
        <div class="form-group">
            <label for="anonArticleTitle">게시글 제목</label>
            <input type="text" class="form-control" id="anonArticleTitle" name="anonArticleTitle">
        </div>
        <div class="form-group">
            <label for="anonArticleTopic">주제 선택</label>
            <select class="form-control" id="anonArticleTopic" name="anonArticleTopic">
                <option value="사이트 문의, 건의">사이트 문의, 건의</option>
                <option value="가정">가정</option>
                <option value="과학A">과학A</option>
                <option value="과학B">과학B</option>
                <option value="국어">국어</option>
                <option value="기술">기술</option>
                <option value="도덕">도덕</option>
                <option value="미술">미술</option>
                <option value="수학">수학</option>
                <option value="스포츠">스포츠</option>
                <option value="역사">역사</option>
                <option value="영어">영어</option>
                <option value="체육">체육</option>
                <option value="창특2">창특2</option>
                <option value="한문">한문</option>
            </select>
            <small id="anonArticleTopicHelp" class="form-text text-muted">사이트 관련은 "사이트 문의, 건의"를, 과목별 질문은 과목을 선택하여 작성해 주세요.</small>
        </div>
        <div class="form-group">
            <label for="anonArticleContent">게시글 내용</label>
            <textarea class="form-control" id="anonArticleContent" name="anonArticleContent" rows="15"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">작성</button>
    </form>
</@layout.display>