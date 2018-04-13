package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ServicioRedimirAcumular {
    @FormUrlEncoded
    @POST("Redimir.php")
    Call<RedimirAcumular> traerResultado(@FieldMap Map<String,String> datos);
}
