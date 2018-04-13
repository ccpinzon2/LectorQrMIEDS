package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.Response_login;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServicioLogin {
    @POST("LoginGerentes.php")
    @FormUrlEncoded
    Call<Response_login> traerUsuarioLogin(@FieldMap Map<String,String> datos);
}
