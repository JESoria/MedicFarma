package com.app.medicfarma.ws_app;
/**
 * Created by Soria on 03/2/2019.
 *
 * En esta clase se almacena todas las rutas que apuntan hacia el web service.
 */

public class WSRoutes {


    //Rutas MedicFarma
    public static String baseURL = "http://medicfarma.azurewebsites.net/";
    public static String getToken = "WService";
    public static String makeLogin = "v1/Login";
    public static String makeRegister = "v1/Register";
    public static String makeDrugstoresList = "v1/DrugstoresList";
    public static String makeNearbyDrugstore = "v1/NearbyDrugstore";
    public static String makeProductSearch = "v1/ProductSearch";
    public static String makeProductDetail = "v1/ProductDetail";

    //Rutas Farmacia San Nicolas
    public static String baseURLSanNicolas = "http://medicfarma.azurewebsites.net/";
    public static String getTokenSanNicolas = "WService";

    //Rutas Farmacia FarmaValue
    public static String baseURLFarmaValue = "http://medicfarma.azurewebsites.net/";
    public static String getTokenFarmaValue= "WService";

    //Rutas Faramacia Camila
    public static String baseURLCamila = "http://medicfarma.azurewebsites.net/";
    public static String getTokenCamila= "WService";

}
