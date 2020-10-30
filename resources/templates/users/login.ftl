<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <form method="post" action="/users/match">
        <div class="form-group">
            <label for="userMail">이메일</label>
            <input type="email" class="form-control" id="userMail" name="userMail">
        </div>
        <div class="form-group">
            <label for="userPw">비밀번호</label>
            <input type="password" class="form-control" id="userPw" name="userPw">
        </div>
        <button type="submit" class="btn btn-primary">로그인</button>
    </form>
</@layout.display>