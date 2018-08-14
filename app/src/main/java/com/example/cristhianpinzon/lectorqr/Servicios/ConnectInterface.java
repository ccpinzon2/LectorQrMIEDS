package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;
import com.example.cristhianpinzon.lectorqr.Logica.Response_login;
import com.example.cristhianpinzon.lectorqr.Logica.Response_user;
import com.example.cristhianpinzon.lectorqr.Logica.TraerGremio;
import com.example.cristhianpinzon.lectorqr.Logica.Transacciones;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Ivan on 13/08/18.
 */

public interface ConnectInterface {
    @POST("LoginGerentes.php")
    @FormUrlEncoded
    Call<Response_login> traerUsuarioLogin(@FieldMap Map<String,String> datos);

    @FormUrlEncoded
    @POST("redeemPoints.php")
    Call<RedimirAcumular> traerResultado(@FieldMap Map<String,String> datos);

    @GET("getGremio.php")
    Call<TraerGremio> traerGremio(@QueryMap Map<String,String> datos);

    @GET("GetGremOrTran.php")
    Call<List<Transacciones>> traerTransacciones (@QueryMap Map<String,String> datos);

    @POST("Userdata.php")
    @FormUrlEncoded
    Call<Response_user> traerUserApp(@Field("id_user") String idUser, @Field("id_establecimiento") String idPlace, @Field("tipo") String type);

}
