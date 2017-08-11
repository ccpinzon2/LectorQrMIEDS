package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.Usuario;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Cristhian Pinzon on 09/08/2017.
 */

public interface ServicioLogin {
    @GET("loginGerente.php")
    Call<Usuario> traerUsuarioLogin(@QueryMap Map<String,String> datos);
}
