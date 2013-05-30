<%@page import="java.util.List"%>
<%@page import="blog.bean.BlogsBean"%>
<%@page contentType="application/json" pageEncoding="UTF-8"%>
<%@page import="com.google.gson.Gson"%>
<%
    BlogsBean blogsBean = (BlogsBean) request.getAttribute("blog");
    String data = new Gson().toJson(blogsBean);
    out.print(data);
%>