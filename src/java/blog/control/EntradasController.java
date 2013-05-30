package blog.control;

import blog.bean.EntradasBean;
import blog.dao.EntradasDaoInterface;
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
public class EntradasController {

    @Autowired
    EntradasDaoInterface EntradasDAO;

    @RequestMapping({"/entradas/{idEntrada}"})
    public ModelAndView entradas(@PathVariable("idEntrada") String idEntrada, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        request.setAttribute("id", idEntrada);
        return new ModelAndView("index", "contenido", "entradas.jsp");
    }

    @RequestMapping({"/listarEntradas/{pag}/{rpp}/{orden}/{condicion}/{tag}"})
    public ModelAndView listarEntradas(@PathVariable("pag") int pag, @PathVariable("rpp") int rpp, @PathVariable("orden") String orden, @PathVariable("condicion") String condicion, @PathVariable("tag") String tag, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        List<EntradasBean> lista;
        String tablas = "entradas sel";
        if (!condicion.equals("undefined") && !condicion.equals("all")) {
            condicion = " AND sel.id_blog = '" + condicion + "'";
        } else {
            condicion = "";
        }
        if (!tag.equals("undefined")) {
            tag = " AND sel.id = et.entradas_id AND et.tags_id = tags.id AND tags.tag = '" + tag + "'";
            tablas = tablas + ", entradas_tags et, tags";
        } else {
            tag = "";
        }
        System.out.println("cosa = " + orden);
        int paginasTotales;
        orden = "sel.id " + orden;
        try {
            lista = EntradasDAO.getPage(tablas, "sel.id", rpp, pag, orden, condicion + tag);
            paginasTotales = EntradasDAO.getPages(tablas, "sel.id", rpp, condicion + tag);
        } catch (Exception e) {
            throw new Exception("Error");
        }

        Map<String, Object> model = new HashMap<>();
        model.put("entradas", lista);
        model.put("rpp", rpp);
        model.put("currentPage", pag);
        model.put("totalPages", paginasTotales);
        return new ModelAndView("EntradaListAjax", model);
    }

    @RequestMapping({"/crearEntrada"})
    public ModelAndView crearEntrada(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        EntradasBean entrada = new EntradasBean();
        try {
            entrada = new Gson().fromJson(request.getParameter("json"), EntradasBean.class);
            EntradasDAO.save(entrada);
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/entrada/{idEntrada}"})
    public ModelAndView entrada(@PathVariable("idEntrada") String idEntrada, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        EntradasBean entrada = new EntradasBean();
        try {
            entrada = EntradasDAO.get(entrada, " WHERE blo.id_usuario = us.id AND blo.id = en.id_blog AND en.id = " + idEntrada);
        } catch (Exception e) {
            throw new Exception("Error");
        }

        return new ModelAndView("EntradaAjax", "entrada", entrada);
    }

    @RequestMapping({"/borrarEntrada/{idEntrada}"})
    public ModelAndView borrarEntrada(@PathVariable("idEntrada") String idEntrada, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {

        EntradasBean entrada = new EntradasBean();
        try {
            entrada.setId(idEntrada);
            EntradasDAO.delete("entradas", "id", String.valueOf(entrada.getId()));
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }
}
