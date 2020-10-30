<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <form method="post" action="/users/update/${userId}">
        <div class="form-group">
            <label for="userName">이름</label>
            <input type="text" class="form-control" id="userName" name="userName" value="${userName}">
        </div>
        <div class="form-group">
            <label for="userMail">이메일</label>
            <input type="text" class="form-control" id="userMail" name="userMail" value="${userMail}" disabled="disabled">
        </div>
        <div class="form-group">
            <label for="userPw">기존 비밀번호</label>
            <input type="password" class="form-control" id="userPw" name="userPw">
        </div>
        <div class="form-group">
            <label for="userNewPw">신규 비밀번호</label>
            <input type="password" class="form-control" id="userNewPw" name="userNewPw">
            <small id="userNewPwHelp" class="form-text text-muted">비밀번호를 변경하실 때에만 입력하시면 됩니다.</small>
        </div>
        <button type="submit" class="btn btn-primary">제출</button>
    </form>
</@layout.display>