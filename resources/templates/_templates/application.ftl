<#macro content></#macro>

<#macro display>
    <!DOCTYPE html>
    <html lang="ko">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>${title} | Yangjin208</title>

        <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css">
    </head>
    <body>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/2.5.3/umd/popper.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>

    <nav class="navbar navbar-expand-lg sticky-top navbar-dark bg-primary">
        <a class="navbar-brand" href="/">Yangjin208</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
                aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle Navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav">
                <li class="nav-item">
                    <a class="nav-link <#if path == '/anon_articles/new'>active</#if>" href="/anon_articles/new">익명 글쓰기</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link <#if path == '/articles'>active</#if>" href="/articles">게시글 목록</a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                       data-toggle="dropdown"
                       aria-haspopup="true" aria-expanded="false">
                        관리자 메뉴
                    </a>
                    <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                        <#if userSession.id != 0 >
                        <a class="dropdown-item <#if path == '/anon_articles'>active</#if>"
                           href="/anon_articles">익명 게시글 목록</a>
                        <#if userSession.id == 1>
                        <a class="dropdown-item <#if path == '/users'>active</#if>" href="/users">유저
                            목록</a>
                        <a class="dropdown-item <#if path == '/users/new'>active</#if>"
                           href="/users/new">유저
                            추가</a>
                        </#if>
                        <a class="dropdown-item <#if path == '/users/${userSession.id}'>active</#if>"
                           href="/users/${userSession.id}">내 계정</a>
                        <a class="dropdown-item <#if path == '/articles/new'>active</#if>"
                           href="/articles/new">게시글 쓰기</a>
                        <a class="dropdown-item <#if path == '/users/logout'>active</#if>"
                           href="/users/logout">로그아웃</a>
                        </#if>
                        <#if userSession.id == 0>
                        <a class="dropdown-item <#if path == '/users/login'>active</#if>"
                           href="/users/login">로그인</a>
                        </#if>
                    </div>
                </li>
                <li class="nav-item">
                    <a class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true">Made by Janghyub Seo</a>
                </li>
            </ul>
        </div>
    </nav>

    <div style="margin-bottom: 10px;"></div>
    <div class="container container-md">
        <#list flash.tag as tag>
            <div class="alert alert-${tag}">
                ${flash.content[tag_index]}
            </div>
        </#list>
        <#nested>
    </div>
    </body>
    </html>
</#macro>