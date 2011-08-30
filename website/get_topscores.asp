<%@ LANGUAGE = JScript %>

<%
var xml = Server.CreateObject("Microsoft.XMLDOM");
xml.async = false;
xml.load(Server.MapPath(".") + "\\top_scores.xml");
Response.Write(xml.xml);
%>
