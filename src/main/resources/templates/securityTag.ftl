<#assign security=JspTaglibs["http://www.springframework.org/security/tags"] />

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<@security.authorize access="hasRole('ROLE_TEST')">
    this is welcome page
</@security.authorize>
</body>
</html>