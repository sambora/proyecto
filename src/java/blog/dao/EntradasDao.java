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
public class EntradasDao implements EntradasDaoInterface {

    String tabla = "entradas en, blogs blo, usuarios us";
    @Autowired
    private GenericData Mysql;

    @Override
    public EntradasBean get(EntradasBean entity, String condicion) throws Exception {
        try {
            Mysql.conexion();
            entity.setId(Mysql.getOne(tabla, "en.id", condicion));
            entity.setTexto(Mysql.getOne(tabla, "en.texto", condicion));
            entity.setTitulo(Mysql.getOne(tabla, "en.titulo", condicion));
            entity.setId_blog(Mysql.getOne(tabla, "en.id_blog", condicion));
            entity.setNombre_blog(Mysql.getOne(tabla, "blo.nombre", condicion));
            entity.setAutor(Mysql.getOne(tabla, "us.nombre", condicion));
            entity.setFecha_creacion(Mysql.getOne(tabla, "en.fecha_creacion", condicion));
            entity.setFecha_edicion(Mysql.getOne(tabla, "en.fecha_edicion", condicion));
            entity.setTags(Mysql.getTags("ta, entradas_tags et WHERE et.tags_id = ta.id AND entradas_id = " + entity.getId()));
            entity.setTagsEnlaces(Mysql.getTagsEnlaces("ta, entradas_tags et WHERE et.tags_id = ta.id AND entradas_id = " + entity.getId()));
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
                entity.setId(String.valueOf(Mysql.insertOne("entradas", "id, id_blog", "null, " + entity.getId_blog(), "")));
            }
            Mysql.updateOne("WHERE id = " + entity.getId(), "entradas", "texto", "'" + entity.getTexto() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "entradas", "titulo", "'" + entity.getTitulo() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "entradas", "fecha_creacion", "'" + entity.getFecha_creacion() + "'");
            Mysql.updateOne("WHERE id = " + entity.getId(), "entradas", "fecha_edicion", "'" + entity.getFecha_edicion() + "'");

            Mysql.removeOne("entradas_tags", "entradas_id", entity.getId());
            if (!entity.getTags().equals("")) {
                String tags = entity.getTags();
                tags = tags.replaceAll(" ", "");
                ArrayList listaTags = new ArrayList();
                for (String numero : tags.split(",")) {
                    listaTags.add(numero);
                }
                for (int i = 0; i < listaTags.size(); i++) {
                    String idTag;
                    if (Mysql.existsOne("tags", "WHERE tag = '" + listaTags.get(i).toString() + "'")) {
                        idTag = Mysql.getId("tags", "tag", listaTags.get(i).toString(), "");
                        if (!Mysql.existsOne("entradas_tags", "WHERE entradas_id = " + entity.getId() + " AND tags_id = '" + idTag + "'")) {
                            Mysql.insertOne("entradas_tags", "entradas_id, tags_id", "'" + entity.getId() + "', '" + idTag + "'", "");
                        }
                    } else {
                        idTag = String.valueOf(Mysql.insertOne("tags", "id, tag", "null,'" + listaTags.get(i).toString() + "'", ""));
                        Mysql.insertOne("entradas_tags", "entradas_id, tags_id", entity.getId() + "," + idTag, "");
                    }
                }
            }
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
                arrEntradas.add(this.get(entradasBean, " WHERE us.id = blo.id_usuario AND blo.id = en.id_blog AND en.id=" + entradasBean.getId()));
            }
            Mysql.desconexion();
            return arrEntradas;
        } catch (Exception e) {
            throw new Exception("EntradasDao.getPage: Error: " + e.getMessage());
        }
    }
}
