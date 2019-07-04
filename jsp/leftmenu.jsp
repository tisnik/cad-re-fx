<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>
<%@ page import="cz.tisnik.cadgfx.Configuration" %>

<%
    // nastaveni hlavicky pro ten pitomy IE, ktery vsechno kesuje
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
        function buttonClick(url)
        {
            var w=window.open(url, "jmeno", 'width=400,height=300,toolbar=no,status=no,titlebar=no,menubar=no,toolbar=no,             resizable=yes,dependent=yes,scrollbars=yes');
            w.focus();
        }

        function buttonClick(url, width, height)
        {
            var w=window.open(url, "jmeno", 'width='+width+',height='+height+',toolbar=no,status=no,titlebar=no,menubar=no,toolbar=no, resizable=yes,dependent=yes,scrollbars=yes');
            w.focus();
        }

        function getElementStyle(elemID, IEStyleAttr, CSSStyleAttr)
        {
            var elem = document.getElementById(elemID);
            if (elem.currentStyle)
            {
                return elem.currentStyle[IEStyleAttr];
            }
            else if (window.getComputedStyle)
            {
                var compStyle = window.getComputedStyle(elem, "");
                //alert(compStyle.getPropertyValue(CSSStyleAttr));
                return compStyle.getPropertyValue(CSSStyleAttr);
            }
            return "";
        }

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

        function colorClick(imageId)
        {
            var image = document.getElementById(imageId);
            var src = image.src;
            var isEnabled = "checkbox_on.png" == src.substr(1+src.lastIndexOf('/'));
            var status = "0";
            if (isEnabled)
            {
                image.src = "<%= path %>/img/checkbox_off.png";
                status = "0";
            }
            else
            {
                image.src = "<%= path %>/img/checkbox_on.png";
                status = "1";
            }
            var href = '<%= path %>/ImageRenderer?changeColor='+imageId+'&status='+status+'&rnd='+randomString();
            parent.frames['drawing'].document.getElementById('drawing').src=href;
        }

        function selectObjects(selection)
        {
            var selections = ["roomtype", "area", "capacity", "occupation", "owner", "availability", "pozadavek"];
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
        <p class="full_header">Výběr objektů</p>
        <ul class='mktree'>
            <%= request.getAttribute("tree") %>
        </ul>
        <img src="<%= path %>img/spacer.gif" width="180px" height="1px" />
        <%= request.getAttribute("attributes") %>
        <%= request.getAttribute("admin_commands") %>
    </body>
</html>

