package com.app.medicfarma.ws_app;
/**
 * Created by Soria on 03/2/2019.
 *
 * En esta clase se almacena todas las rutas que apuntan hacia el web service.
 */

public class WSRoutes {
    public static String baseURL = "http://2bd72f1d.ngrok.io/";
    public static String getToken = "WService";
    public static String makeLogin = "v1/Login";
    public static String makeRegister = "v1/Register";
    public static String makeDrugstoresList = "v1/DrugstoresList";
    public static String makeNearbyDrugstore = "v1/NearbyDrugstore";
    public static String makeProductSearch = "v1/ProductSearch";
    public static String makeProductDetail = "v1/ProductDetail";
}
