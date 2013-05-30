/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.data;

import java.util.ArrayList;

public interface GenericData {

    public abstract void commitTrans() throws Exception;

    public abstract void conexion() throws Exception;

    public abstract void desconexion() throws Exception;

    public abstract void initTrans() throws Exception;

    public abstract void rollbackTrans() throws Exception;

    public abstract Boolean existsOne(String tabla, String condicion) throws Exception;

    public abstract String getId(String tabla, String campo, String valor, String condicion) throws Exception;

    public abstract String getOne(String tabla, String campo, String condicion) throws Exception;

    public abstract ArrayList<Integer> getPage(String tabla, String seleccion, int rpp, int pagina, String orden, String condicion) throws Exception;

    public abstract int getPages(String tabla, String seleccion, int rpp, String condicion) throws Exception;

    public abstract int insertOne(String tabla, String campos, String valores, String condicion) throws Exception;

    public abstract void removeOne(String tabla, String campo, String valor) throws Exception;

    public abstract void updateOne(String condicion, String tabla, String campo, String valor) throws Exception;

    public abstract String getTags(String condicion) throws Exception;

    public abstract String getTagsEnlaces(String condicion) throws Exception;
}
