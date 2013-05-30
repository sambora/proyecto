/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.dao;

import blog.bean.BlogsBean;
import blog.data.GenericData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Sambora
 */
public class BlogsDao implements BlogsDaoInterface {

    String tabla = "blogs blo, usuarios us";
    @Autowired
    private GenericData Mysql;

    @Override
    public BlogsBean get(BlogsBean entity, String condicion) throws Exception {
        try {
            Mysql.conexion();
            entity.setId(Mysql.getOne(tabla, "blo.id", condicion));
            entity.setNombre(Mysql.getOne(tabla, "blo.nombre", condicion));
            entity.setId_usuario(Mysql.getOne(tabla, "blo.id_usuario", condicion));
            entity.setNombre_usuario(Mysql.getOne(tabla, "us.nombre", " WHERE us.id = " + entity.getId_usuario()));
            entity.setInformacion(Mysql.getOne(tabla, "blo.informacion", condicion));
            Mysql.desconexion();
        } catch (Exception e) {
            throw new Exception("EntradasDao.get: Error: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public void save(BlogsBean entity) throws Exception {
        try {
            Mysql.conexion();
            Mysql.initTrans();
            if (entity.getId() == null || entity.getId().equals("")) {
                entity.setId(String.valueOf(Mysql.insertOne("blogs", "id, id_usuario", "null, " + entity.getId_usuario(), "")));
            }
            Mysql.updateOne("WHERE id = " + entity.getId(), "blogs", "nombre", "'" + entity.getNombre() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "blogs", "informacion", "'" + entity.getInformacion() + "'");
            Mysql.commitTrans();
            Mysql.desconexion();
        } catch (Exception e) {
            Mysql.rollbackTrans();
            throw new Exception("EntradasDao.save: Error: " + e.getMessage());
        }
    }

    @Override
    public void delete(String tabla, String id, String campo) throws Exception {
        try {
            Mysql.conexion();
            Mysql.removeOne(tabla, id, campo);
            Mysql.desconexion();
        } catch (Exception e) {
            throw new Exception("EntradasDao.delete: Error: " + e.getMessage());
        }
    }

    @Override
    public int getPages(String tablas, String seleccion, int rpp, String condicion) throws Exception {
        try {
            Mysql.conexion();
            int pages = Mysql.getPages(tablas, seleccion, rpp, condicion);
            Mysql.desconexion();
            return pages;
        } catch (Exception e) {
            throw new Exception("EntradasDao.getPages: Error: " + e.getMessage());
        }
    }

    @Override
    public List<BlogsBean> getPage(String tablas, String seleccion, int rpp, int page, String order, String condicion) throws Exception {
        ArrayList<Integer> arrId;
        ArrayList<BlogsBean> arrEntradas = new ArrayList<>();
        try {
            Mysql.conexion();
            arrId = Mysql.getPage(tablas, seleccion, rpp, page, order, condicion);
            Iterator iterador = arrId.listIterator();
            while (iterador.hasNext()) {
                BlogsBean blogsBean = new BlogsBean();
                blogsBean.setId(iterador.next().toString());
                arrEntradas.add(this.get(blogsBean, " WHERE us.id = blo.id_usuario AND blo.id=" + blogsBean.getId()));
            }
            Mysql.desconexion();
            return arrEntradas;
        } catch (Exception e) {
            throw new Exception("EntradasDao.getPage: Error: " + e.getMessage());
        }
    }
}
