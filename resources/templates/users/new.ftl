<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <form method="post" action="/users/create">
        <div class="form-group">
            <label for="userName">이름</label>
            <input type="text" class="form-control" id="userName" name="userName">
        </div>
        <div class="form-group">
            <label for="userMail">이메일</label>
            <input type="text" class="form-control" id="userMail" name="userMail">
        </div>
        <div class="form-group">
            <label for="userPw">임시 비밀번호</label>
            <input type="password" class="form-control" id="userPw" name="userPw">
        </div>
        <button type="submit" class="btn btn-primary">제출</button>
    </form>
</@layout.display>