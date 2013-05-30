/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.dao;

import blog.bean.UsuariosBean;
import blog.data.GenericData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Sambora
 */
public class UsuariosDao implements DaoInterface {

    String tabla = "usuarios";
    @Autowired
    private GenericData Mysql;

    @Override
    public UsuariosBean get(UsuariosBean entity, String condicion) throws Exception {
        try {
            Mysql.conexion();
            entity.setId(Mysql.getOne(tabla, "id", condicion));
            entity.setNombre(Mysql.getOne(tabla, "nombre", condicion));
            entity.setEmail(Mysql.getOne(tabla, "email", condicion));
            entity.setInformacion(Mysql.getOne(tabla, "informacion", condicion));
            entity.setPassword(Mysql.getOne(tabla, "password", condicion));
            Mysql.desconexion();
        } catch (Exception e) {
            throw new Exception("EntradasDao.get: Error: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public void save(UsuariosBean entity) throws Exception {
        try {
            Mysql.conexion();
            Mysql.initTrans();
            if (entity.getId().equals("0")) {
                entity.setId(String.valueOf(Mysql.insertOne("usuarios", "id", "null", "")));
            }
            Mysql.updateOne("WHERE id = " + entity.getId(), "usuarios", "nombre", "'" + entity.getNombre() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "usuarios", "email", "'" + entity.getEmail() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "usuarios", "informacion", "'" + entity.getInformacion() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "usuarios", "password", "SHA('" + entity.getPassword() + "')");
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
    public List<UsuariosBean> getPage(String tablas, String seleccion, int rpp, int page, String order, String condicion) throws Exception {
        ArrayList<Integer> arrId;
        ArrayList<UsuariosBean> arrEntradas = new ArrayList<>();
        try {
            Mysql.conexion();
            arrId = Mysql.getPage(tablas, seleccion, rpp, page, order, condicion);
            Iterator iterador = arrId.listIterator();
            while (iterador.hasNext()) {
                UsuariosBean entradasBean = new UsuariosBean();
                entradasBean.setId(iterador.next().toString());
                arrEntradas.add(this.get(entradasBean, " WHERE id=" + entradasBean.getId()));
            }
            Mysql.desconexion();
            return arrEntradas;
        } catch (Exception e) {
            throw new Exception("EntradasDao.getPage: Error: " + e.getMessage());
        }
    }
}
