<#import "_templates/application.ftl" as layout>
<#import "_templates/article_list.ftl" as articles>

<@layout.display>
    <@articles.article_list></@articles.article_list>
</@layout.display>