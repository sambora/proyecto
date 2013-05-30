
<form id="entradaForm" class="form-horizontal" name="entradaForm" action="json">

    <input type="hidden" id="id" name="id" />
    <input type="hidden" id="fecha_creacion" name="fecha_creacion" />
    <input type="hidden" id="fecha_edicion" name="fecha_edicion" />
    Título

    <br />
    <textarea type="text" id="titulo" name="titulo"></textarea>
    <br />
    <br />

    Texto

    <br />
    <textarea type="text" id="texto" name="texto"></textarea>
    <br />
    <br />

    Tags (sin espacios y separados por comas)

    <br />
    <textarea type="text" id="tags" name="tags"></textarea>
    <br />

    <a href="" id="mostrarTags">Sugerencias</a>

    <br />
    <br />
    
    Blog perteneciente:<br />
    <select id="comboBlogs" name="id_blog"></select>
    
    <br />
    <br />

    <button type="submit" id="submitEntradaForm" class="btn">Submit</button>

</form>

