<%@page import="java.util.List"%>
<%@page import="blog.bean.EntradasBean"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    EntradasBean entrada = (EntradasBean) request.getAttribute("entrada");
    String data = new Gson().toJson(entrada);
    out.print(data);
%>