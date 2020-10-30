<#import "../_templates/application.ftl" as layout>

<@layout.display>
    <table class="table">
        <thead>
        <tr>
            <th scope="col">#</th>
            <th scope="col">이름</th>
            <th scope="col">이메일</th>
        </tr>
        </thead>
        <tbody>
        <#list userId as user>
            <tr>
                <th scope="row">${user}</th>
                <td><a href="/users/${user}">${userName[user_index]}</a></td>
                <td>${userMail[user_index]}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</@layout.display>