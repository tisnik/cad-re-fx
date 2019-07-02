<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<%
    long startTime = System.currentTimeMillis();
    // zajistit zmenu titulku stranky, ale pouze v pripade, ze je nastaven parametr "page_title"
    Object title=session.getAttribute("tsk_page_title");
    if ( title == null )
    {
        out.println("<title>CAD RE-FX</title>");
    }
    else
    {
        out.println("<title>TSK: "+title.toString()+"</title>");
    }
    session.removeAttribute("tsk_page_title");
    String path = request.getRequestURI();
    path = path.substring(0, 1+path.indexOf('/', 1));
%>
<meta name="Author" content="Pavel Tisnovsky" />
<meta name="Generator" content="vim" />
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="keywords" content="Telematika, ČD, ČD-Telekomunikace, ČDT, telekomunikace, informační systémy, SAP" />
<link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" language="JavaScript" url="<%= path %>include/scripts.js"></script>
</head>

<%@ include file="log.jsp" %>

<% log(request, "$ORANGE$ip $GRAY$"+request.getRemoteAddr()+" $GREEN$begin page"); %>

<body>

