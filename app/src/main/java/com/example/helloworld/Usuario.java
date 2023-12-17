package com.example.helloworld;

public class Usuario {
    String correo;
    String pass;
    String nombres;
    String apellidos;
    String usrname;
    String nacimiento;
    String tipoUsuario;

    public Usuario(String email, String password, String name, String sureName, String nombreUsuario, String date, String tipo) {
        this.correo = email;
        this.pass = password;
        this.nombres = name;
        this.apellidos = sureName;
        this.usrname = nombreUsuario;
        this.nacimiento = date;
        this.tipoUsuario = tipo;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPass() {
        return pass;
    }

    public String getNombres() {
        return nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getUsrname() {
        return usrname;
    }

    public String getNacimiento() {
        return nacimiento;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }
}
