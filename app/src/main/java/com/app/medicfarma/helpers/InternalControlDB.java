package com.app.medicfarma.helpers;

import android.provider.BaseColumns;

public class InternalControlDB {

    public InternalControlDB(){

    }

    //Inner Class para tabla Token
    public static class TablaToken implements BaseColumns {
        //Estructura tabla Token
        public static final String TABLE_NAME_TOKEN = "token";
        public static final String COLUMN_NAME_ACCESS_TOKEN = "access_token";
        public static final String COLUMN_NAME_TOKEN_TYPE = "token_type";
        public static final String COLUMN_NAME_EXPIRES_IN = "expires_in";
        public static final String COLUMN_NAME_REFRESH_TOKEN = "refresh_token";
    }

    //Inner Class para tabla Usuario
    public static class TablaUsuario implements BaseColumns {
        //Estructura tabla Usuario
        public static final String TABLE_NAME_USUARIO = "usuario";
        public static final String COLUMN_NAME_ID_USUARIO = "id_usuario";
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
                "CREATE TABLE " + TablaToken.TABLE_NAME_TOKEN + " (" +
                        TablaToken._ID + " INTEGER PRIMARY KEY," +
                        TablaToken.COLUMN_NAME_ACCESS_TOKEN + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_TOKEN_TYPE + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_REFRESH_TOKEN  + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaToken.COLUMN_NAME_EXPIRES_IN + SQL.TEXT_TYPE + " )";
        //Drop table Token
        public static final String DELETE_TABLE_TOKEN =
                "DROP TABLE IF EXISTS " + TablaToken.TABLE_NAME_TOKEN;
    }

    //Inner Class para el manejo de consultas a la tabla de usuario
    public static class SQL_Usuario {
        //Create table Usuario
        public static final String CREATE_TABLE_USUARIO =
                "CREATE TABLE " + TablaUsuario.TABLE_NAME_USUARIO + " (" +
                        TablaUsuario._ID + " INTEGER PRIMARY KEY," +
                        TablaUsuario.COLUMN_NAME_ID_USUARIO + SQL.INTEGER_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_NOMBRES + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_APELLIDOS + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_GENERO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_FECHA_NACIMIENTO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_CORREO + SQL.TEXT_TYPE + SQL.COMMA +
                        TablaUsuario.COLUMN_NAME_FACEBOOK_ID + SQL.INTEGER_TYPE + " )";
        //Drop table Usuario
        public static final String DELETE_USUARIOS =
                "DROP TABLE IF EXISTS " + TablaUsuario.TABLE_NAME_USUARIO;
    }





}
