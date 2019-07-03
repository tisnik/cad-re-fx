<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>
<%@ page import="cz.tisnik.cadgfx.Configuration" %>

<%
    response.setHeader( "Pragma", "no-cache" );
    response.addHeader( "Cache-Control", "must-revalidate" );
    response.addHeader( "Cache-Control", "no-cache" );
    response.addHeader( "Cache-Control", "no-store" );
    response.setDateHeader("Expires", 0);

    String path = request.getRequestURI();
    path = path.substring(0, 1+path.indexOf('/', 1));
    String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
    Configuration configuration = (Configuration) session.getAttribute( "configuration" );
%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
        <link href="<%= path %>include/mktree.css" type="text/css" rel="stylesheet" />
        <script type="text/javascript" language="JavaScript" src="<%= path %>include/mktree.js"></script>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
        <meta name="Author" content="Pavel Tisnovsky">
        <meta name="Generator" content="vim">
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <script type="text/javascript">
        <!--

        function randomString()
        {
            var chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
            var string_length = 16;
            var randomstring = "";
            for (var i=0; i != string_length; i++)
            {
                var rnum = Math.floor(Math.random() * chars.length);
                randomstring += chars.substring(rnum,rnum+1);
            }
            return randomstring;
        }

        function onMapClick(map)
        {
            var location = "<%= path %>MapRenderer?map="+map;
            var image = top.frames["drawing"].window.document.getElementById("renderedMap"); 
            //alert(location);
            //alert(image);
            image.src=location;
        }

        function onRegionClick(map, region)
        {
            var location = "<%= path %>MapRenderer?map="+map+"&region="+region;
            var image = top.frames["drawing"].window.document.getElementById("renderedMap"); 
            //alert(location);
            image.src=location;
        }

        function selectObjects()
        {
            var drawing=parent.frames['drawing'].document.getElementById('drawing');
            var selectionName = selection;
            for (i in selections)
            {
                var sel = selections[i];
                var element = document.getElementById("checkbox_" + sel);
                if (sel == selection)
                {
                    var selected = element.src.indexOf("/img/checkbox_on.png") >= 0;
                    if (selected)
                    {
                        selectionName="";
                        element.src="<%= path %>/img/checkbox_off.png";
                    }
                    else
                    {
                        element.src="<%= path %>/img/checkbox_on.png";
                    }
                }
                else
                {
                    element.src="<%= path %>/img/checkbox_off.png";
                }
            }
            drawing.src="<%= path %>/ImageRenderer?select="+selectionName+"&rnd="+randomString();
            return false;
        }

        -->
        </script>
    </head>

    <body>
        <p class="full_header">Výběr map</p>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" summary="" bordercolorlight="black" style="width:100%">
            <%= request.getAttribute("mapList") %>
        </table>
        <img src="<%= path %>img/spacer.gif" width="180px" height="1px" />
        <p class="full_header">Výběr objektů</p>
        <table border="2" frame="border" rules="all" cellspacing="1" cellpadding="1" class="formular" summary="" bordercolorlight="black" style="width:100%">
            <%= request.getAttribute("objList") %>
        </table>
    </body>
</html>
