package com.example.cristhianpinzon.lectorqr.Servicios;

import com.example.cristhianpinzon.lectorqr.Logica.Transacciones;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Cristhian Pinzon on 07/09/2017.
 */

public interface ServicioTransaccionesEmpleado {
    @GET("GetGremOrTran.php")
    Call<List<Transacciones>> traerTransacciones (@QueryMap Map<String,String> datos);
}
