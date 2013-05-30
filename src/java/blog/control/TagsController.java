package blog.control;

import blog.bean.EntradasBean;
import blog.dao.TagsDaoInterface;
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
public class TagsController {

    @Autowired
    TagsDaoInterface TagsDAO;

    @RequestMapping({"/tags"})
    public ModelAndView tags(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return new ModelAndView("index", "contenido", "tags.jsp");
    }

    @RequestMapping({"/listarTags/{pag}/{rpp}/{orden}/{condicion}"})
    public ModelAndView listarTags(@PathVariable("pag") int pag, @PathVariable("rpp") int rpp, @PathVariable("orden") String orden, @PathVariable("condicion") String condicion, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {

        List<EntradasBean> lista;
        condicion = "";
        int paginasTotales;
        orden = " tag asc";
        try {
            lista = TagsDAO.getPage("tags", "tag, id", rpp, pag, orden, condicion);
            paginasTotales = TagsDAO.getPages("tags", "tag, id", rpp, condicion);
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
}
