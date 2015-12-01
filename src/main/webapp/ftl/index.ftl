<html>
<head>
    <title>Hello</title>
</head>
<body>


${simpleComponent.integerInsideComponent}
<#assign componentPrefix = "${simpleComponent.id}" />
<@s.url method="${componentPrefix}.plusOne" action="SimpleComponent" id="urlId">
</@s.url>
<@s.a href="%{urlId}" title="Sort by " cssClass="">a</@s.a>
</body>
</html>