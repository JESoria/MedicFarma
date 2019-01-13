package com.app.medicfarma.models;
/**
 * @author soria
 * */
public class UsuarioModel {

    private int idUsuario;
    private String password;
    private String nombres;
    private String apellidos;
    private String genero;
    private String fechaNacimiento;
    private String correo;
    private String facebookId;
    private int estado;

    public UsuarioModel() {

    }

    public UsuarioModel(int idUsuario, String password, String nombres, String apellidos, String genero, String fechaNacimiento, String correo, String facebookId, int estado) {
        this.idUsuario = idUsuario;
        this.password = password;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.genero = genero;
        this.fechaNacimiento = fechaNacimiento;
        this.correo = correo;
        this.facebookId = facebookId;
        this.estado=estado;

    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public int getEstado() {return estado;}

    public void setEstado(int estado) {this.estado = estado;}


}
