<%@ page import="java.util.Date" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="java.text.*" %>
<%@ page import="java.io.*" %>

<%!
    String logFileName;
    void log(HttpServletRequest request, String str) {
        HashMap<String, String> colors = new HashMap<String, String>(10);
        colors.put("RED", "31");
        colors.put("GREEN", "32");
        colors.put("ORANGE", "33");
        colors.put("BLUE", "34");
        colors.put("VIOLET", "35");
        colors.put("LIGHT_BLUE", "36");
        colors.put("GRAY", "37");

        StringBuffer output=new StringBuffer();

        Format formatter=new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        output.append("\033[0;" + colors.get("GREEN")+"mcadgfx  \033[0;"+colors.get("LIGHT_BLUE")+"m");
        output.append(formatter.format(new Date()));
        output.append("  ");
        //output.append("\033[0;34;40m" + request.getServletPath());
        output.append("\033[0;34m" + request.getServletPath());

        int len=output.length();
        for (int i=len; i<70; i++)
            output.append(' ');
        output.append("  \033[0m");

        String[] pieces = str.split("\\$");

        for (String p : pieces) {
            if (colors.containsKey(p.toUpperCase()))  {
                output.append("\033[0;" + colors.get(p.toUpperCase()) + "m");
            }
            else {
                output.append(p);
            }
        }
        try {
            if (logFileName!=null)
            {
                FileWriter writer=new FileWriter(logFileName, true);
                if (writer!=null) {
                    output.append("\033[0m\n");
                    writer.write(output.toString());
                    writer.flush();
                    writer.close();
                }
                else {
                    System.out.println("\033[0;31;40mCAD-RE-FX: error writing to log file");
                    System.out.println("\033[0;33;40m"+str);
                }
            }
            else {
                output.append("\033[0m");
                System.out.println(output);
            }
        }
        catch(IOException e) {
            System.out.println(e.getMessage());
        }
        //System.out.println(output.append("\033[0m"));
    }

%>

