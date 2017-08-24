package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.UserApp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Cristhian Pinzon on 24/08/2017.
 */

public interface ServicioUserApp {
    @GET("dataUser.php")
    Call<UserApp> traerUserApp(@QueryMap Map<String,String> data);
}
