<!-- Modal -->
<div id="myModalForm" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Usuario</h3>
    </div>
    <div class="modal-body">


        <form id="usuarioForm" class="form-horizontal" name="usuarioForm" action="json">

            <input type="hidden" id="id" name="id" />
            Nombre

            <br />
            <input type="text" id="nombre" name="nombre" />
            <br />
            <br />
            E-mail

            <br />
            <input type="text" id="email" name="email" />
            <br />
            <br />
            Contraseña

            <br />
            <input type="password" id="pass" name="password" />
            <br />
            <br />
            Información

            <br />
            <textarea type="text" id="informacion" name="informacion"></textarea>
            <br />
            <br />


            <button type="submit" id="submitUsuarioForm" class="btn">Aceptar</button>

        </form>

    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Cerrar</button> 
        <div id="result"></div>
    </div>
</div>
