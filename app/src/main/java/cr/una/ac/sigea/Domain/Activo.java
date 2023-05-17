package cr.una.ac.sigea.Domain;

import java.io.Serializable;

public class Activo implements Serializable {

    private String n_etiqueta;
    private String marca;
    private String modelo;
    private String serie;
    private String descripcion;
    private int id_ubicacion;
    private String nombre_ubicacion;
    private String valor_libro;
    private String condicion;
    private String clase_activo;
    private int id_funcionario;
    private String dni_funcionario;
    private String nombre_funcionario;

    public Activo (){}

    public Activo(String n_etiqueta, String marca, String modelo, String serie, String descripcion, int id_ubicacion, String nombre_ubicacion, String valor_libro, String condicion, String clase_activo, int id_funcionario, String dni_funcionario, String nombre_funcionario) {
        this.n_etiqueta = n_etiqueta;
        this.marca = marca;
        this.modelo = modelo;
        this.serie = serie;
        this.descripcion = descripcion;
        this.id_ubicacion = id_ubicacion;
        this.nombre_ubicacion = nombre_ubicacion;
        this.valor_libro = valor_libro;
        this.condicion = condicion;
        this.clase_activo = clase_activo;
        this.id_funcionario = id_funcionario;
        this.dni_funcionario = dni_funcionario;
        this.nombre_funcionario = nombre_funcionario;
    }

    public String getN_etiqueta() {
        return n_etiqueta;
    }

    public void setN_etiqueta(String n_etiqueta) {
        this.n_etiqueta = n_etiqueta;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(int id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }

    public String getNombre_ubicacion() {
        return nombre_ubicacion;
    }

    public void setNombre_ubicacion(String nombre_ubicacion) {
        this.nombre_ubicacion = nombre_ubicacion;
    }

    public String getValor_libro() {
        return valor_libro;
    }

    public void setValor_libro(String valor_libro) {
        this.valor_libro = valor_libro;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getClase_activo() {
        return clase_activo;
    }

    public void setClase_activo(String clase_activo) {
        this.clase_activo = clase_activo;
    }

    public int getId_funcionario() {
        return id_funcionario;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public String getDni_funcionario() {
        return dni_funcionario;
    }

    public void setDni_funcionario(String dni_funcionario) {
        this.dni_funcionario = dni_funcionario;
    }

    public String getNombre_funcionario() {
        return nombre_funcionario;
    }

    public void setNombre_funcionario(String nombre_funcionario) {
        this.nombre_funcionario = nombre_funcionario;
    }

    @Override
    public String toString() {
        return "Activo{" +
                "n_etiqueta='" + n_etiqueta + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", serie='" + serie + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", id_ubicacion=" + id_ubicacion +
                ", nombre_ubicacion='" + nombre_ubicacion + '\'' +
                ", valor_libro='" + valor_libro + '\'' +
                ", condicion='" + condicion + '\'' +
                ", clase_activo='" + clase_activo + '\'' +
                ", id_funcionario=" + id_funcionario +
                ", dni_funcionario=" + dni_funcionario +
                ", nombre_funcionario='" + nombre_funcionario + '\'' +
                '}';
    }
}
