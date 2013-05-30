package blog.control;

import blog.bean.BlogsBean;
import blog.dao.BlogsDaoInterface;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BlogsController {

    @Autowired
    BlogsDaoInterface BlogsDAO;

    @RequestMapping({"/blogs/{usuario}"})
    public ModelAndView blogs(@PathVariable("usuario") String usuario, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setAttribute("usuario", usuario);
        return new ModelAndView("index", "contenido", "blogs.jsp");
    }

    @RequestMapping({"/listarBlogs/{pag}/{rpp}/{orden}/{condicion}"})
    public ModelAndView listarBlogs(@PathVariable("pag") int pag, @PathVariable("rpp") int rpp, @PathVariable("orden") String orden, @PathVariable("condicion") String condicion, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {

        List<BlogsBean> lista;
        int paginasTotales;
        if (!condicion.equals("all")) {
            condicion = "AND id_usuario = " + condicion;
        }else{
            condicion = "";
        }
        orden = "id " + orden;
        try {
            lista = BlogsDAO.getPage("blogs", "id", rpp, pag, orden, condicion);
            paginasTotales = BlogsDAO.getPages("blogs", "id", rpp, condicion);
        } catch (Exception e) {
            throw new Exception("Error");
        }

        Map<String, Object> model = new HashMap<>();
        model.put("blogs", lista);
        model.put("rpp", rpp);
        model.put("currentPage", pag);
        model.put("totalPages", paginasTotales);
        return new ModelAndView("BlogListAjax", model);
    }

    @RequestMapping({"/crearBlog"})
    public ModelAndView crearBlog(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        BlogsBean blog = new BlogsBean();
        try {
            blog = new Gson().fromJson(request.getParameter("json"), BlogsBean.class);
            BlogsDAO.save(blog);
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/borrarBlog/{idBlog}"})
    public ModelAndView borrarBlog(@PathVariable("idBlog") String idBlog, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        BlogsBean blog = new BlogsBean();
        try {
            blog.setId(idBlog);
            BlogsDAO.delete("blogs", blog.getId(), "id");
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/blog/{idBlog}"})
    public ModelAndView blog(@PathVariable("idBlog") String idBlog, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        BlogsBean blog = new BlogsBean();
        try {
            blog = BlogsDAO.get(blog, " WHERE blo.id_usuario = us.id AND blo.id = " + idBlog);
        } catch (Exception e) {
            throw new Exception("Error");
        }

        return new ModelAndView("BlogAjax", "blog", blog);
    }

    @RequestMapping({"/editarBlog"})
    public ModelAndView editarBlog(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        BlogsBean blog = new BlogsBean();
        try {
            blog = new Gson().fromJson(request.getParameter("json"), BlogsBean.class);
            BlogsDAO.save(blog);
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }
}