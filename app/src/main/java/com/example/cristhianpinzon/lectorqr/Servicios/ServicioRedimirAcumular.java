package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Cristhian Pinzon on 25/08/2017.
 */

public interface ServicioRedimirAcumular {
    @GET("redeemPoints.php")
    Call<RedimirAcumular> traerResultado(@QueryMap Map<String,String> datos);
}
