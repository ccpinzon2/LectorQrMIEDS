package com.example.cristhianpinzon.lectorqr.Servicios;

import android.content.Context;

import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;
import com.example.cristhianpinzon.lectorqr.Logica.Response_login;
import com.example.cristhianpinzon.lectorqr.Logica.Response_user;
import com.example.cristhianpinzon.lectorqr.Logica.TraerGremio;
import com.example.cristhianpinzon.lectorqr.Logica.Transacciones;

import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Connect {

    private static final String URL_HOST = "http://mieds.com/wbserv6/";

    private static Connect instance;
    private Retrofit retrofit;

    private Connect(Context context) {
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(URL_HOST)
                .build();
    }

    public static synchronized Connect getInstance(Context context) {

        if (instance == null) {
            instance = new Connect(context.getApplicationContext());
        }
        return instance;
    }

    public void login_app(Map<String,String> data, Callback<Response_login> response) {
        ServicioLogin servicioLogin = retrofit.create(ServicioLogin.class);
        Call<Response_login> call = servicioLogin.traerUsuarioLogin(data);
        call.enqueue(response);
    }

    public void validate_guild(Map<String,String> datos, Callback<TraerGremio> response) {
        ServicioTraerGremio servicioTraerGremio = retrofit.create(ServicioTraerGremio.class);
        Call<TraerGremio> call = servicioTraerGremio.traerGremio(datos);
        call.enqueue(response);
    }

    public void get_user_qr(String idUser, String idPlace, String type, Callback<Response_user> response) {
        ServicioUserApp servicioUserApp = retrofit.create(ServicioUserApp.class);
        Call<Response_user> call = servicioUserApp.traerUserApp(idUser, idPlace, type);
        call.enqueue(response);
    }

    public void manage_points(Map<String,String> datos, Callback<RedimirAcumular> response) {
        ServicioRedimirAcumular servicioRedimirAcumular = retrofit.create(ServicioRedimirAcumular.class);
        Call<RedimirAcumular> call = servicioRedimirAcumular.traerResultado(datos);
        call.enqueue(response);
    }

    public void get_transactions(Map<String,String> data, Callback<List<Transacciones>> response) {
        ServicioTransaccionesEmpleado servicioTransacciones = retrofit.create(ServicioTransaccionesEmpleado.class);
        Call<List<Transacciones>> call = servicioTransacciones.traerTransacciones(data);
        call.enqueue(response);
    }
}
