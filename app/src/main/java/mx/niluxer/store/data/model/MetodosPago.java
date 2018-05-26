
package mx.niluxer.store.data.model;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetodosPago {

    @SerializedName("id_MetodosPago")
    @Expose
    private Integer idMetodosPago;
    @SerializedName("nombre")
    @Expose
    private String nombre;
    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    public Integer getIdMetodosPago() {
        return idMetodosPago;
    }

    public void setIdMetodosPago(Integer idMetodosPago) {
        this.idMetodosPago = idMetodosPago;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
        /*return "{" +
                "id:'" + id + '\'' +
                '}';*/
    }
}
