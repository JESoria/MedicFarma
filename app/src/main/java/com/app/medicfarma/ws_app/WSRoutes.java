package com.app.medicfarma.ws_app;
/**
 * Created by Soria on 03/2/2019.
 *
 * En esta clase se almacena todas las rutas que apuntan hacia el web service.
 */
public class WSRoutes {
    public static String baseURL = "https://wservice20190506093452.azurewebsites.net/";
    public static String getToken = "WService";
    public static String makeLogin = "v1/Login";
    public static String makeRegister = "v1/Register";
    public static String makeDrugstoresList = "v1/DrugstoresList";//Lista de Farmacias
    public static String makeProductsBranchOffice = "v1/NearbyDrugstore"; //Por sucursal
    public static String makeProductsPharmacies = "v1/Nearby"; // Todas
    public static String makeSearchMore = "v1/SearchMore"; //Sucursal Especifica
    public static String makeProductDetail = "v1/Detail";
    public static String makeOrder = "v1/Order";
    public static String makePedidos = "v1/OrdersList";
    public static String makeIncidencia = "v1/Reclam";
}
