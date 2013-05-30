   

    function DAO(urlBase,entityName,keyPropertyName) {
        this.urlBase=urlBase;
        this.entityName=entityName;
        this.keyPropertyName=keyPropertyName;
    }

    DAO.prototype.getEntity=function(key,successFunction,errorFunction) {
        $.ajax({
            type: 'GET',
            contentType: 'application/json',
            dataType: "json",
            url: this.urlBase + '/' + this.entityName + "/" + key,
            success: successFunction,
            error: errorFunction
        });   
    }

    DAO.prototype.insertEntity=function(entity,successFunction,errorFunction) {
        
    }

    DAO.prototype.updateEntity=function(entity,successFunction,errorFunction) {
    }


    DAO.prototype.deleteEntity=function(entity,successFunction,errorFunction) {
    }


    DAO.prototype.createEntity=function(entity,successFunction,errorFunction) {
    }

    DAO.prototype.findAll=function(entity,successFunction,errorFunction) {
    }  
 


