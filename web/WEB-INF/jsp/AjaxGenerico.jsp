<%@page import="java.util.List"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    List datos = (List) request.getAttribute("datos");
    String data = new Gson().toJson(datos);
    out.print("{\"list\":" + data + "}");
%>