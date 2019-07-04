<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>

<html>
    <head>
        <%
        String path = request.getRequestURI();
        path = path.substring(0, 1+path.indexOf('/', 1));
        %>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
    </head>
    <body bgcolor="white">
        <div>
            <p class="full_header" style="width:98%">Vybraný objekt</p>
            <%= request.getAttribute("selectedObject") %>
        </div>
    </body>
</html>
