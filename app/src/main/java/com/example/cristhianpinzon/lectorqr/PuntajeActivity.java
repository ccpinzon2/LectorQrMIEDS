package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cristhianpinzon.lectorqr.Logica.UserApp;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioUserApp;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PuntajeActivity extends AppCompatActivity {

    private String iduser;
    private String idTienda;
    private String tipoTienda;
    private final String TAG = "PUNTAJEACTIVITY";

    private TextView _txtNombreUsuario;
    private TextView _txtEmailUser;
    private TextView _txtIdUser;
    private TextView _txtTelefonoUser;
    private TextView _txtPtsGlobales;
    private TextView _txtPtsFidelizacion;

    private Button _btnAcmRedPts;
    private Button _btnSalirPts;

    private DatabaseAccess databaseAccess;
    
    // // TODO: 24/08/2017 Enviar una T o una E dependiendo del tipo de establecimiento



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntaje);
        databaseAccess = new DatabaseAccess(this);
        traerDatos();
        beginComponents();
        traerDatosUserApp();
        accionBotones();
    }

    private void traerDatosUserApp() {
        final ProgressDialog progressDialog = new ProgressDialog(PuntajeActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Trayendo Datos");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getResources().getString(R.string.url_server))
                .build();

        ServicioUserApp servicioUserApp = retrofit.create(ServicioUserApp.class);

        Map<String,String> datos = new HashMap<>();
        datos.put("id_user",iduser);
        datos.put("id_establecimiento",idTienda);
        datos.put("tipo",tipoTienda);

        Call<UserApp> call = servicioUserApp.traerUserApp(datos);

        call.enqueue(new Callback<UserApp>() {
            @Override
            public void onResponse(Call<UserApp> call, Response<UserApp> response) {
                Log.d(TAG, "onResponse: " + response.body().toString());
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserApp> call, Throwable t) {
                Log.e("Error cargando datos",t.getMessage());
                progressDialog.dismiss();
            }
        });
    }

    private void accionBotones() {
        _btnAcmRedPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: Redimir Acumular" );
                Intent intent = new Intent(getApplicationContext(),RedAcmPtsActivity.class);
                intent.putExtra("iduser",iduser);
                startActivity(intent);
            }
        });
        _btnSalirPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: SAlir" );
                finish();
            }
        });
    }

    private void traerDatos() {
        Bundle extras  =  getIntent().getExtras();
        iduser = (extras != null ) ? extras.getString("iduser"): null ;
        Log.d(TAG,"ID USER -> " + iduser);

        databaseAccess.open();
        idTienda = databaseAccess.getUsers().get(0).getId_tienda();
        if (databaseAccess.getUsers().get(0).getTipo_tienda().equals("EDS")){
            tipoTienda = "E";
        }else {
            tipoTienda ="T";
        }
        databaseAccess.close();
    }

    private void beginComponents() {

        _txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuarioApp);
        _txtEmailUser = (TextView) findViewById(R.id.txtEmailUserApp);
        _txtIdUser = (TextView) findViewById(R.id.txtIdUserApp);
        _btnAcmRedPts = (Button) findViewById(R.id.btn_RedAcmPtos);
        _btnSalirPts = (Button) findViewById(R.id.btn_SalirPts);


    }
}
