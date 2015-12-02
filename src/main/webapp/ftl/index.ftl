<html>
<head>
    <title>Struts2 components with Spring</title>
</head>
<body>



<#assign componentPrefix = "${firstComponent.id}" />
<@s.url method="${componentPrefix}.plusOne" action="indexAction" var="urlId">
</@s.url>
Number from First Component:  ${firstComponent.integerInsideComponent} <@s.a href="%{urlId}" title="Increase first number" cssClass="">increase</@s.a>

<br/><br/>

<#assign secondComponentPrefix = "${secondComponent.id}" />
<@s.url method="${secondComponentPrefix}.plusOne" action="indexAction" var="secondUrlId">
</@s.url>

Number from Second Component: ${secondComponent.integerInsideComponent} <@s.a href="%{secondUrlId}" title="Increase second number" cssClass="">increase</@s.a>


</body>
</html>