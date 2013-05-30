/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.dao;

import blog.bean.EntradasBean;
import blog.data.GenericData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Sambora
 */
public class TagsDao implements TagsDaoInterface {

    String tabla = "entradas";
    @Autowired
    private GenericData Mysql;

    @Override
    public EntradasBean get(EntradasBean entity, String condicion) throws Exception {
        try {
            Mysql.conexion();
            entity.setTags(Mysql.getTags(" WHERE id = " + entity.getId()));
            System.out.println(entity.getTags());
            entity.setTagsEnlaces(Mysql.getTagsEnlaces(" WHERE id = " + entity.getId()));
            Mysql.desconexion();
        } catch (Exception e) {
            throw new Exception("EntradasDao.get: Error: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public void save(EntradasBean entity) throws Exception {
        try {
            Mysql.conexion();
            Mysql.initTrans();
            if (entity.getId() == null || entity.getId().equals("")) {
                entity.setId(String.valueOf(Mysql.insertOne(tabla, "id, id_blog", "null, " + entity.getId_blog(), "")));
            }
            Mysql.updateOne("WHERE id = " + entity.getId(), tabla, "texto", "'" + entity.getTexto() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), tabla, "titulo", "'" + entity.getTitulo() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), tabla, "fecha_creacion", "'" + entity.getFecha_creacion() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), tabla, "fecha_edicion", "'" + entity.getFecha_edicion() + "'");
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
    public List<EntradasBean> getPage(String tablas, String seleccion, int rpp, int page, String order, String condicion) throws Exception {
        ArrayList<Integer> arrId;
        ArrayList<EntradasBean> arrEntradas = new ArrayList<>();
        try {
            Mysql.conexion();
            arrId = Mysql.getPage(tablas, seleccion, rpp, page, order, condicion);
            Iterator iterador = arrId.listIterator();
            while (iterador.hasNext()) {
                EntradasBean entradasBean = new EntradasBean();
                entradasBean.setId(iterador.next().toString());
                arrEntradas.add(this.get(entradasBean, ""));
            }
            Mysql.desconexion();
            return arrEntradas;
        } catch (Exception e) {
            throw new Exception("EntradasDao.getPage: Error: " + e.getMessage());
        }
    }
}
