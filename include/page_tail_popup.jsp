<div class="help">&nbsp;&nbsp;&nbsp;<a href="javascript:window.close()">Uzavřít okno</a></div>

<br /><div class="paticka">Doba zpracování požadavku:
<%= System.currentTimeMillis()-startTime %></a>&nbsp;ms
</div><br />

<%--!
private String getApplicationParameter(String parameterName)
{
    return this.getServletConfig().getServletContext().getInitParameter( parameterName );
}
%>

<div class="paticka">
<%= getApplicationParameter( "footer_copyright" ) %><br />
<%= getApplicationParameter( "webmaster" ) %>
&nbsp; &nbsp; verze: <%= getApplicationParameter( "version" ) %>
&nbsp; &nbsp; poslední úprava: <%= getApplicationParameter( "modification_date" ) %></div>
--%>
<% log(request, "$ORANGE$ip $GRAY$"+request.getRemoteAddr()+" $VIOLET$end page"); %>

</body>
</html>

