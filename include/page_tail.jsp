<br clear="all" />
<hr noshade size="1" width="100%" />

<table border="0">
    <tr>
        <td>
            <div class="paticka">Doba zpracování požadavku:
                <%= System.currentTimeMillis()-startTime %></a>&nbsp;ms
            </div>
            <div class="paticka">
                <%= getApplicationParameter( "footer_copyright" ) %><br />
                webmaster: <%= getApplicationParameter( "webmaster" ) %>
                &nbsp; &nbsp; verze: <%= getApplicationParameter( "version" ) %>
                &nbsp; &nbsp; poslední úprava: <%= getApplicationParameter( "modification_date" ) %>
            </div>
        </td>
        <td>
            <img src="img/logo-java.gif"   width="80px" height="80px" alt="Java" />
            <img src="img/logo-tomcat.gif" width="80px" height="80px" alt="Tomcat" />
            <img src="img/logo-vim.gif"    width="80px" height="80px" alt="Vim" />
        </td>
    </tr>
</table>

<%!
private String getApplicationParameter(String parameterName)
{
    return this.getServletConfig().getServletContext().getInitParameter( parameterName );
}
%>

 
<% log(request, "$ORANGE$ip $GRAY$"+request.getRemoteAddr()+" $VIOLET$end page"); %>

</body>
</html>

