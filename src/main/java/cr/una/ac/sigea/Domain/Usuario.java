package cr.una.ac.sigea.Domain;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String dni_usuario;
    private String nombre_usuario;
    private String email_usuario;
    private String clave_usuario;

    //crear instancia
    public Usuario(){}

    //usuario logeado
    public Usuario(String dni_usuario, String nombre_usuario, String email_usuario) {
        this.dni_usuario = dni_usuario;
        this.nombre_usuario = nombre_usuario;
        this.email_usuario = email_usuario;
    }

    //usuario login
    public Usuario(String dni_usuario, String clave_usuario) {
        this.dni_usuario = dni_usuario;
        this.clave_usuario = clave_usuario;
    }

    public String getDni_usuario() {
        return dni_usuario;
    }

    public void setDni_usuario(String dni_usuario) {
        this.dni_usuario = dni_usuario;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getClave_usuario() {
        return clave_usuario;
    }

    public void setClave_usuario(String clave_usuario) {
        this.clave_usuario = clave_usuario;
    }

    @Override
    public String toString() {
        return
                "Usuario = " +
                "dni_usuario: " + dni_usuario +
                ", nombre_usuario: " + nombre_usuario +
                ", email_usuario: " + email_usuario +
                ", clave_usuario: " + clave_usuario ;
    }
}
