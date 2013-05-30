/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blog.data;

/**
 *
 * @author rafa
 */
/**
 *
 * @author rafa
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import blog.connection.GenericConnection;
import org.springframework.beans.factory.annotation.Autowired;

//import com.mysql.jdbc.Connection;
//import com.mysql.jdbc.Statement;
public class MysqlData implements GenericData {

    @Autowired
    GenericConnection connection1;
    Connection conexion;

    @Override
    public void conexion() throws Exception {
        conexion = connection1.crearConexion();
    }

    @Override
    public void desconexion() throws Exception {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new Exception("MySQL.CerrarConexion: Error al cerrar la conexion" + e.getMessage());
        }
    }

    @Override
    public void initTrans() throws Exception {
        try {
            conexion.setAutoCommit(false);
        } catch (SQLException e) {
            throw new Exception("Mysql.initTrans: Error al iniciar transacci�n: " + e.getMessage());
        }
    }

    @Override
    public void commitTrans() throws Exception {
        try {
            conexion.commit();
        } catch (SQLException e) {
            throw new Exception("Mysql.commitTrans: Error en commit: " + e.getMessage());
        }
    }

    @Override
    public void rollbackTrans() throws Exception {
        try {
            conexion.rollback();
        } catch (SQLException e) {
            throw new Exception("Mysql.rollbackTrans: Error en rollback: " + e.getMessage());
        }
    }

    @Override
    public void removeOne(String tabla, String campo, String valor) throws Exception {
        Statement s = null;
        try {
            s = (Statement) conexion.createStatement();
            s.executeUpdate("DELETE FROM " + tabla + " WHERE " + campo + " = " + valor);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.deleteOne: Error al eliminar el registro: " + e.getMessage());
        }
    }

    @Override
    public int insertOne(String tabla, String campos, String valores, String condicion) throws Exception {
        ResultSet rs = null;
        java.sql.PreparedStatement stmt = null;
        int id = 0;
        try {
            String sql = "INSERT INTO " + tabla + " (" + campos + ") VALUES (" + valores + ") " + condicion;
            stmt = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            int returnLastInsertId = stmt.executeUpdate();
            if (returnLastInsertId != -1) {
                if (!tabla.equals("entradas_tags")) {
                    rs = stmt.getGeneratedKeys();
                    rs.next();
                    id = rs.getInt(1);
                }
            }
            return id;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.insertOne: Error al insertar el registro: " + e.getMessage());
        }
    }

    @Override
    public void updateOne(String condicion, String tabla, String campo, String valor) throws Exception {
        Statement st = null;
        try {
            st = (Statement) conexion.createStatement();
            String sql = "UPDATE " + tabla + " SET " + campo + " = " + valor + " " + condicion;
            st.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.removeOne: Error al modificar el registro: " + e.getMessage());
        }
    }

    @Override
    public String getId(String tabla, String campo, String valor, String condicion) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = (Statement) conexion.createStatement();
            String sql = "SELECT id FROM " + tabla + " WHERE " + campo + "='" + valor + "' " + condicion;
            rs = stmt.executeQuery(sql);
            rs.first();
            return rs.getString("id");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getId: No se ha podido realizar la consulta: " + e.getMessage());
        }
    }

    @Override
    public String getOne(String tabla, String campo, String condicion) throws Exception {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = (Statement) conexion.createStatement();
            String sql = "SELECT " + campo + " FROM " + tabla + condicion;
            rs = stmt.executeQuery(sql);
            rs.first();
            return rs.getString(campo);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getOne: No se ha podido realizar la consulta: " + e.getMessage());
        }
    }

    @Override
    public Boolean existsOne(String tabla, String condicion) throws Exception {
        int result = 0;
        Statement stmt = null;
        try {
            stmt = (Statement) conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM " + tabla + " " + condicion);
            while (rs.next()) {
                result = rs.getInt("COUNT(*)");
            }
            return (result > 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.existsOne: Error en la consulta: " + e.getMessage());
        }
    }

    @Override
    public int getPages(String tabla, String seleccion, int rpp, String condicion) throws Exception {
        int result = 0;
        Statement stmt = null;
        try {
            stmt = (Statement) conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT (COUNT(DISTINCT " + seleccion + ")) AS paginas FROM " + tabla + " WHERE 1=1 " + condicion);
            while (rs.next()) {
                result = rs.getInt("paginas") / rpp;
                if ((rs.getInt("paginas") % rpp) > 0) {
                    result++;
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getPages: Error en la consulta: " + e.getMessage());
        }
    }

    @Override
    public ArrayList<Integer> getPage(String tabla, String seleccion, int rpp, int pagina, String orden, String condicion) throws Exception {
        try {
            ArrayList vector = new ArrayList<>();
            int offset;
            Statement stmt;
            stmt = (Statement) conexion.createStatement();
            offset = Math.max(((pagina - 1) * rpp), 0);
            String sql = "SELECT DISTINCT " + seleccion + " FROM " + tabla + " WHERE 1=1 " + condicion;
            if (!orden.equals("")) {
                sql += " ORDER BY " + orden;
            }
            sql += " LIMIT " + offset + " , " + rpp;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                vector.add(rs.getInt("id"));
            }
            return vector;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getPage: Error en la consulta: " + e.getMessage());
        }
    }

    @Override
    public String getTags(String condicion) throws Exception {
        try {
            String tags = "";
            Statement stmt;
            stmt = (Statement) conexion.createStatement();
            String sql = "SELECT * FROM tags " + condicion;
            ResultSet rs = stmt.executeQuery(sql);
            int n = 0;
            while (rs.next()) {
                n = 1;
                tags = tags + rs.getString("tag") + ", ";
            }
            if (n != 0) {
                tags = tags.substring(0, tags.length() - 2);
            }
            return tags;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getPage: Error en la consulta: " + e.getMessage());
        }
    }

    @Override
    public String getTagsEnlaces(String condicion) throws Exception {
        try {
            String tags = "";
            Statement stmt = (Statement) conexion.createStatement();
            String sql = "SELECT * FROM tags " + condicion;
            ResultSet rs = stmt.executeQuery(sql);
            int n = 0;
            while (rs.next()) {
                n = 1;
                tags = tags + "<a href=\"\" class=\"enlacesTags\">" + rs.getString("tag") + "</a>, ";
            }
            if (n != 0) {
                tags = tags.substring(0, tags.length() - 2);
            }
            return tags;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("mysql.getPage: Error en la consulta: " + e.getMessage());
        }
    }
}
