package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.Response_user;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServicioUserApp {
    @POST("datosUsuario.php")
    @FormUrlEncoded
    Call<Response_user> traerUserApp(@Field("id_user") String idUser, @Field("id_establecimiento") String idPlace, @Field("tipo") String type);
}
