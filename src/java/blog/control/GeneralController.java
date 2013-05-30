package blog.control;

import blog.bean.UsuariosBean;
import blog.dao.DaoInterface;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GeneralController {

    @Autowired
    DaoInterface UsuariosDAO;

    @RequestMapping({"/pedirArchivo/{nombre}"})
    public ModelAndView formulario(@PathVariable("nombre") String nombre, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        return new ModelAndView(nombre);
    }

    @RequestMapping({"/index"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }

    @RequestMapping({"/login/{user}/{pass}"})
    public ModelAndView login(@PathVariable("user") String user, @PathVariable("pass") String pass, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, Exception {
        HttpSession sesion = request.getSession();
        ArrayList lista = new ArrayList();
        try {
            int n = UsuariosDAO.getPages("usuarios", "id", 10, " AND email = '" + user + "' AND password = SHA('" + pass + "')");
            if (n == 1) {
                UsuariosBean usuariosBean = new UsuariosBean();
                usuariosBean = UsuariosDAO.get(usuariosBean, " WHERE email = '" + user + "' AND password = SHA('" + pass + "')");
                sesion.setAttribute("user", user);
                sesion.setAttribute("id", usuariosBean.getId());
                lista.add(user);
            }
        } catch (Exception e) {
            throw new Exception("Error");
        }
        return new ModelAndView("AjaxGenerico", "datos", lista);
    }

    @RequestMapping({"/logout"})
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        HttpSession sesion = request.getSession();
        sesion.invalidate();
        return new ModelAndView("index", "contenido", "inicio.jsp");
    }
}
