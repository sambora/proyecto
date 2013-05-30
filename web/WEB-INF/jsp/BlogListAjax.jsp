<%@page import="blog.bean.BlogsBean"%>
<%@page import="java.util.List"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    List<BlogsBean> listaBlogs = (List<BlogsBean>) request.getAttribute("blogs");
    String data = new Gson().toJson(listaBlogs);
    String rpp = new Gson().toJson(request.getAttribute("rpp").toString());
    String paginaActual = new Gson().toJson(request.getAttribute("currentPage").toString());
    String paginasTotal = new Gson().toJson(request.getAttribute("totalPages").toString());
    out.print("{\"list\":" + data + ",\"rpp\":" + rpp + ",\"currentPage\":" + paginaActual + ",\"totalPages\":" + paginasTotal + "}");
%>