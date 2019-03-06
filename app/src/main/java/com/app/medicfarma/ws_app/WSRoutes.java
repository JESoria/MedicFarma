package com.app.medicfarma.ws_app;
/**
 * Created by Soria on 03/2/2019.
 *
 * En esta clase se almacena todas las rutas que apuntan hacia el web service.
 */

public class WSRoutes {


    //Rutas MedicFarma
    public static String baseURL = "https://6eb72ef1.ngrok.io/";
    public static String getToken = "WService";
    public static String makeLogin = "v1/LoginBridge";
    public static String makeRegister = "v1/Register";
    public static String makeDrugstoresList = "v1/DrugstoresList";
    public static String makeProductsBranchOffice = "v1/NearbyDrugstore"; //Por sucursal
    public static String makeProductsPharmacies = "v1/Nearby"; // Todas
    public static String makeProductSearch = "v1/ProductSearch";
    public static String makeProductDetail = "v1/Detail";



}
