<#import "../_templates/application.ftl" as layout>
<#import "../_templates/article_list.ftl" as articles>

<@layout.display>
    <h2 style="text-align: center;">게시글 목록</h2>
    <@articles.article_list></@articles.article_list>
</@layout.display>