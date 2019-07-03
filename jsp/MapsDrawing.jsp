<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@ page pageEncoding="utf-8" %>
<%
    String path = request.getRequestURI();
    path = path.substring(0, 1+path.indexOf('/', 1));
    String rnd = Long.toString(Double.doubleToLongBits(Math.random()));
%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
    <head>
        <meta http-equiv="Pragma" content="no-cache">
        <meta http-equiv="Expires" content="-1">
        <meta http-equiv="CACHE-CONTROL" content="no-cache">
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

        function getEvent(e)
        {
            return !e ? window.event : e;
        }

        function onImageClick(obj, e)
        {
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
            //alert(coords.x+"  "  +coords.y);
            document.getElementById('renderedMap').src='<%= path %>/MapRenderer?coordsx='+coords.x+'&coordsy='+coords.y+'&rnd='+randomString();
        }
        -->
        </script>

    </head>

    <body>
        <img src="<%= path %>/MapRenderer?command=reset_view?rnd=<%= rnd %>" name="renderedMap" id="renderedMap" onclick="onImageClick(this, event)" />
    </body>
</html>

