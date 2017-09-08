package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.TraerGremio;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by cristhianpinzon on 8/09/17.
 */

public interface ServicioTraerGremio {
    @GET("points.php")
    Call<TraerGremio> traerGremio(@QueryMap Map<String,String> datos);
}
