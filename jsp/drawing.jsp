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
    /*
    if (configuration==null) {
        configuration = new Configuration(this.getServletContext());
    }*/
    int imageWidth  = configuration.imageWidth;
    int imageHeight = configuration.imageHeight;
%>

<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <title>CAD RE-FX</title>
        <link href="<%= path %>include/style.css" type="text/css" rel="stylesheet" />
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

        function getEvent(e)
        {
            return !e ? window.event : e;
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
                alert(compStyle.getPropertyValue(CSSStyleAttr));
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

        function setSapHref()
        {
            var xmlhttp;
            if (window.XMLHttpRequest)
            {
                // code for IE7+, Firefox, Chrome, Opera, Safari
                xmlhttp=new XMLHttpRequest();
            }
            else if (window.ActiveXObject)
            {
                // code for IE6, IE5
                xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
            }
            else
            {
                alert("Your browser does not support XMLHTTP!");
            }
            xmlhttp.onreadystatechange=function()
            {
                if(xmlhttp.readyState==4)
                {
                    var href = document.getElementById("sapHrefDiv");
                    href.innerHTML=xmlhttp.responseText;
                    <% if (configuration != null && configuration.debugSapEvent) { %>
                    alert(xmlhttp.responseText);
                    <% } %>
                    // simulace kliku na odkaz "SAP"
                    var innerHref = document.getElementById("sap_href");
                    innerHref.click();
                }
            }
            xmlhttp.open("GET", "<%= path %>ResolveSapHref?rnd="+randomString(), true);
            xmlhttp.send(null);
        }

        function onImageClick(obj, e)
        {
            var href = document.getElementById("sapHrefDiv");
            href.innerHTML="...pracuji...";
            var evt = getEvent(e);
            var coords = {x:0, y:0};

            if (evt.pageX)
            {
                coords.x = evt.pageX - obj.offsetLeft;
                coords.y = evt.pageY - obj.offsetTop;
            }
            else if (evt.clientX)
            {
                coords.x = evt.offsetX;
                coords.y = evt.offsetY;
            }
            document.getElementById('drawing').src='<%= path %>/ImageRenderer?coordsx='+coords.x+'&coordsy='+coords.y+'&rnd='+randomString();
            setTimeout("setSapHref()", 500);
        }

        function colorClick(imageId)
        {
            var image = document.getElementById(imageId);
            var src = image.src;
            var isEnabled = "ok.gif" == src.substr(1+src.lastIndexOf('/'));
            var status = "0";
            if (isEnabled)
            {
                image.src = "<%= path %>/img/cancel.gif";
                status = "0";
            }
            else
            {
                image.src = "<%= path %>/img/ok.gif";
                status = "1";
            }
            var href = '<%= path %>/ImageRenderer?changeColor='+imageId+'&status='+status+'&rnd='+randomString();
            document.getElementById('drawing').src=href;
        }
        -->
        </script>
    </head>
    <body>
        <p class="full_header">Výkres</p>
        <div class="tools">
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=zoom_plus&rnd='+randomString(); return false;">
                <img src="<%= path %>img/viewmag_plus.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=zoom_minus&rnd='+randomString(); return false;">
                <img src="<%= path %>img/viewmag_minus.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=zoom_1&rnd='+randomString(); return false;">
                <img src="<%= path %>img/viewmag1.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=zoom_all&rnd='+randomString(); return false;">
                <img src="<%= path %>img/viewmagfit.gif" border="0" />
            </a>
            <img src="<%= path %>img/spacer.gif" width="8" />
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=move_left&rnd='+randomString(); return false;">
                <img src="<%= path %>img/arrow1l.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=move_down&rnd='+randomString(); return false;">
                <img src="<%= path %>img/arrow1d.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=move_up&rnd='+randomString(); return false;">
                <img src="<%= path %>img/arrow1u.gif" border="0" />
            </a>
            <a href="" onclick="document.getElementById('drawing').src='<%= path %>/ImageRenderer?command=move_right&rnd='+randomString(); return false;">
                <img src="<%= path %>img/arrow1r.gif" border="0" />
            </a>
            <img src="<%= path %>img/spacer.gif" width="8" />
            <a href="" onclick="buttonClick('<%= path %>/Info');return false;"><img src="<%= path %>img/info.gif" border="0" /></a>
            <a href="" onclick="buttonClick('<%= path %>/Help');return false;"><img src="<%= path %>img/help.gif" border="0" /></a>
            <img src="<%= path %>img/spacer.gif" width="8" />
        </div>
        <img id="drawing" alt="" border="0" width="<%= imageWidth %>" height="<%= imageHeight %>" src="<%= path %>/ImageRenderer?command=reset_view" onclick="onImageClick(this, event)" />
        <br />
        <div id="sapHrefDiv" style="display:none">&nbsp;</div>
        <img src="<%= path %>img/spacer.gif" width="<%= imageWidth %>" height="1px" />
    </body>
</html>

