package blog.dao;


import java.util.List;

public interface GenericDao<objeto> {

    objeto get(objeto entity, String condicion) throws Exception;
    
    void save(objeto entity) throws Exception;

    void delete(String id, String tabla, String campo) throws Exception;

    int getPages(String tablas, String seleccion, int rpp, String condicion) throws Exception;

    List<objeto> getPage(String tablas, String seleccion, int rpp, int page, String order, String condicion) throws Exception;
    
}
