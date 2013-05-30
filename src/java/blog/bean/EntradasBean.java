package blog.bean;

/**
 *
 * @author rafa
 */
public class EntradasBean {

    private String id;
    private String id_blog;
    private String nombre_blog;
    private String autor;
    private String titulo;
    private String texto;
    private String fecha_creacion;
    private String fecha_edicion;
    private String tags;
    private String tagsEnlaces;

    public EntradasBean() {
    }

    public EntradasBean(String id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the texto
     */
    public String getTexto() {
        return texto;
    }

    /**
     * @param texto the texto to set
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * @return the fecha_creacion
     */
    public String getFecha_creacion() {
        return fecha_creacion;
    }

    /**
     * @param fecha_creacion the fecha_creacion to set
     */
    public void setFecha_creacion(String fecha_creacion) {
        this.fecha_creacion = fecha_creacion;
    }

    /**
     * @return the fecha_edicion
     */
    public String getFecha_edicion() {
        return fecha_edicion;
    }

    /**
     * @param fecha_edicion the fecha_edicion to set
     */
    public void setFecha_edicion(String fecha_edicion) {
        this.fecha_edicion = fecha_edicion;
    }

    /**
     * @return the id_blog
     */
    public String getId_blog() {
        return id_blog;
    }

    /**
     * @param id_blog the id_blog to set
     */
    public void setId_blog(String id_blog) {
        this.id_blog = id_blog;
    }

    /**
     * @return the tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * @return the tagsEnlaces
     */
    public String getTagsEnlaces() {
        return tagsEnlaces;
    }

    /**
     * @param tagsEnlaces the tagsEnlaces to set
     */
    public void setTagsEnlaces(String tagsEnlaces) {
        this.tagsEnlaces = tagsEnlaces;
    }

    /**
     * @return the nombre_blog
     */
    public String getNombre_blog() {
        return nombre_blog;
    }

    /**
     * @param nombre_blog the nombre_blog to set
     */
    public void setNombre_blog(String nombre_blog) {
        this.nombre_blog = nombre_blog;
    }

    /**
     * @return the autor
     */
    public String getAutor() {
        return autor;
    }

    /**
     * @param autor the autor to set
     */
    public void setAutor(String autor) {
        this.autor = autor;
    }
}