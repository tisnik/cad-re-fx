<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
    </head>

<%
    String path = request.getRequestURI();
    path = path.substring(0, 1+path.indexOf('/', 1));
    String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
%>

    <%--
    <frame src="topLevel" name="toplevel" scrolling="none" noresize />
    --%>
    <frameset cols="242px,*" border="2">
        <frame src="<%= path %>/LeftMenu" name="leftmenu" scrolling="auto" />  
        <frame src="jsp/drawing.jsp" name="drawing" scrolling="auto" />
    </frameset>  

<noframes> 
<body>
</body>
</noframes>
</html>

