package com.app.medicfarma.helpers;

import android.provider.BaseColumns;

public class InternalControlDB {

    public InternalControlDB(){

    }

    //Inner Class para tabla Token
    public static class TablaToken implements BaseColumns {
        //Estructura tabla Token
        public static final String TABLE_NAME = "token";
        public static final String COLUMN_NAME_TOKEN = "access_token";
        public static final String COLUMN_NAME_TOKEN_TYPE = "token_type";
        public static final String COLUMN_NAME_TOKEN_ALIVE = "token_alive";
        public static final String COLUMN_NAME_UTC_EXPIRATION = "expires_in";
    }

    //Inner Class para tabla Usuario
    public static class TablaUsuario implements BaseColumns {
        //Estructura tabla Usuario
        public static final String TABLE_NAME = "usuario";
        public static final String COLUMN_NAME_ID_USUARIO = "id_usuario";
        public static final String COLUMN_NAME_USUARIO = "usuario";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_NOMBRES = "nombres";
        public static final String COLUMN_NAME_APELLIDOS = "apellidos";
        public static final String COLUMN_NAME_GENERO = "genero";
        public static final String COLUMN_NAME_FECHA_NACIMIENTO = "fecha_nacimiento";
        public static final String COLUMN_NAME_CORREO = "correo";
        public static final String COLUMN_NAME_FACEBOOK_ID = "facebook_id";
    }

    //Inner Class para TipoDatos
    public static class SQL {
        //Tipos de datos
        private static final String TEXT_TYPE = " TEXT";
        private static final String INTEGER_TYPE = " INTEGER";
        private static final String REAL_TYPE = " REAL";
        private static final String NUMERIC_TYPE = " NUMERIC";
        private static final String COMMA = ",";
    }

    //Inner Class para el manejo de consultas a la tabla de token
    public static class SQL_Token {
        //Create table Token
        public static final String CREATE_TABLE_TOKEN =
                "CREATE TABLE " + TablaToken.TABLE_NAME + " (" +
                        TablaToken._ID + " INTEGER PRIMARY KEY," +
                        TablaToken.COLUMN_NAME_TOKEN + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_TOKEN_ALIVE +SQL.INTEGER_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_TOKEN_TYPE + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_UTC_EXPIRATION + SQL.TEXT_TYPE + " )";
        //Drop table Token
        public static final String DELETE_TABLE_TOKEN =
                "DROP TABLE IF EXISTS " + TablaToken.TABLE_NAME;
    }

    //Inner Class para el manejo de consultas a la tabla de usuario
    public static class SQL_Usuario {
        //Create table Usuario
        public static final String CREATE_TABLE_USUARIO=
                "CREATE TABLE " + TablaUsuario.TABLE_NAME + " (" +
                        TablaUsuario._ID + " INTEGER PRIMARY KEY," +
                        TablaUsuario.COLUMN_NAME_ID_USUARIO + SQL.INTEGER_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_USUARIO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_PASSWORD + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_NOMBRES + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_APELLIDOS + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_GENERO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_CORREO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_FACEBOOK_ID + SQL.INTEGER_TYPE + " )";
        //Drop table Usuario
        public static final String DELETE_USUARIOS =
                "DROP TABLE IF EXISTS " + TablaUsuario.TABLE_NAME;
    }





}
