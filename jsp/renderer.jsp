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
            <img width="640" height="480" src="imagerenderer?rnd=<%= Long.toString(Double.doubleToLongBits(Math.random()))%>" />
        </div>
        Simulace výběru místnosti:<br />
        <a href="SAPEVENT:ROOM_CLICK&intreno=I000100000164">Kancelář 1</a><br />
        <a href="SAPEVENT:ROOM_CLICK&intreno=I000100000176">Kuchyň</a><br />
        <a href="SAPEVENT:ROOM_CLICK&intreno=I000100000179">Schodiště 1-2</a><br />
    </body>
</html>
