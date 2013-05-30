package blog.control;

import blog.bean.UsuariosBean;
import blog.dao.DaoInterface;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UsuariosController {

    @Autowired
    DaoInterface UsuariosDAO;

    @RequestMapping({"/usuarios"})
    public ModelAndView usuarios(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return new ModelAndView("index", "contenido", "usuarios.jsp");
    }

    @RequestMapping({"/listarUsuarios/{pag}/{rpp}/{orden}"})
    public ModelAndView listarUsuarios(@PathVariable("pag") int pag, @PathVariable("rpp") int rpp, @PathVariable("orden") String orden, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        List<UsuariosBean> lista;
        int paginasTotales;
        orden = " nombre " + orden;
        try {
            lista = UsuariosDAO.getPage("usuarios", "id", rpp, pag, orden, "");
            paginasTotales = UsuariosDAO.getPages("usuarios", "id", rpp, "");
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

    @RequestMapping({"/usuario"})
    public ModelAndView usuario(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        HttpSession sesion = request.getSession();
        UsuariosBean usuario = new UsuariosBean();
        try {
            usuario = UsuariosDAO.get(usuario, " WHERE id = " + sesion.getAttribute("id").toString());
        } catch (Exception e) {
            throw new Exception("Error");
        }

        return new ModelAndView("UsuarioAjax", "usuario", usuario);
    }

    @RequestMapping({"/crearUsuario"})
    public ModelAndView crearUsuario(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        UsuariosBean usuario = new UsuariosBean();
        try {
            usuario = new Gson().fromJson(request.getParameter("json"), UsuariosBean.class);
            UsuariosDAO.save(usuario);
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/editarUsuario"})
    public ModelAndView editarUsuario(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        UsuariosBean usuario = new UsuariosBean();
        try {
            usuario = new Gson().fromJson(request.getParameter("json"), UsuariosBean.class);
            UsuariosDAO.save(usuario);
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/borrarUsuario/{idUsuario}"})
    public ModelAndView borrarUsuario(@PathVariable("idUsuario") String idUsuario, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        HttpSession sesion = request.getSession();
        sesion.invalidate();
        UsuariosBean usuario = new UsuariosBean();
        try {
            usuario.setId(idUsuario);
            UsuariosDAO.delete("usuarios", usuario.getId(), "id");
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }
}
