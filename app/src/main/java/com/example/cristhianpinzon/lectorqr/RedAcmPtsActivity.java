package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class RedAcmPtsActivity extends AppCompatActivity {

    private RadioButton _rbRedimir;
    private RadioButton _rbAcumular;
    private RadioButton _rbPtsGlobales;
    private RadioButton _rbPtsFidelizados;
    private TextView    _txtValorRedimirAcumular;
    private Button      _btnRedimirAcumularPts;

    private String iduser;
    private String idTienda;
    private String tipoTienda;
    private final String TAG = "REDIMIRPUNTOSACTIVITY";

    private TextView _txtNombreUsuario;
    private TextView _txtEmailUser;
    private TextView _txtIdUser;
    private TextView _txtTelefonoUser;
    private TextView _txtPtsGlobales;
    private TextView _txtPtsFidelizacion;
    private TextView _txtTipoVehiculo;
    private Button   _btnSalirRedm;
    private DatabaseAccess databaseAccess;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_acm_pts);
        databaseAccess = new DatabaseAccess(this);
        beginComponents();
        traerDatos();
        traerDatosUserApp();
        accionesBotones();

    }

    private void traerDatosUserApp() {
        final ProgressDialog progressDialog = new ProgressDialog(RedAcmPtsActivity.this,R.style.AppTheme_Dark_Dialog);
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
                //try {

                Log.d(TAG, "onResponse: " + response.body().toString());

                String idUser = (response.body().getId().equals(""))?"Sin Id": response.body().getId();
                String nombre = (response.body().getNombre().equals(""))?"Sin Nombre": response.body().getNombre();
                String email = (response.body().getEmail().equals(""))?"Sin Email": response.body().getEmail();
                String tel = (response.body().getPhone_user().equals(""))?"Sin Telefono": response.body().getPhone_user();
                String tipoV = (response.body().getTipo().equals(""))?"Sin Tipo": response.body().getTipo();

                _txtIdUser.setText(idUser);
                _txtNombreUsuario.setText(nombre);
                _txtEmailUser.setText(email);
                _txtTelefonoUser.setText(tel);
                _txtTipoVehiculo.setText(tipoV);
                _txtPtsGlobales.setText(String.valueOf(response.body().getPtos_globales()));
                _txtPtsFidelizacion.setText(String.valueOf(response.body().getPtos_fidelizados()));
                progressDialog.dismiss();
//                }catch (Exception e){
//                    Log.e("Error cargando datos",e.getMessage());
//                    Toast.makeText(PuntajeActivity.this, "Error, Revise Su conexion 1", Toast.LENGTH_SHORT).show();
//                    progressDialog.dismiss();
//                }
            }

            @Override
            public void onFailure(Call<UserApp> call, Throwable t) {
                Log.e("Error cargando datos",t.getMessage());
                Toast.makeText(RedAcmPtsActivity.this, "Error, Revise Su conexion 22", Toast.LENGTH_SHORT).show();
                finish();
                progressDialog.dismiss();
            }
        });
    }

    private void traerDatos() {
        Bundle extras  =  getIntent().getExtras();
        iduser = (extras != null ) ? extras.getString("iduser"): null ;
        Log.d(TAG,"ID USER -> " + iduser);

        databaseAccess.open();
        idTienda = databaseAccess.getUsers().get(0).getId_tienda();
        Log.d(TAG,"ID USER -> " + idTienda);
        if (databaseAccess.getUsers().get(0).getTipo_tienda().equals("EDS")){
            tipoTienda = "E";
        }else {
            tipoTienda ="T";
        }
        databaseAccess.close();
    }

    private void accionesBotones() {

        _rbRedimir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_rbAcumular.isChecked()){
                    _rbPtsGlobales.setVisibility(View.INVISIBLE);
                    _rbPtsFidelizados.setVisibility(View.INVISIBLE);
                    _txtValorRedimirAcumular.setHint("Valor en Pesos $");
                    _btnRedimirAcumularPts.setText("Acumular");
                }else if (_rbRedimir.isChecked()){
                    _rbPtsGlobales.setVisibility(View.VISIBLE);
                    _rbPtsFidelizados.setVisibility(View.VISIBLE);
                    _txtValorRedimirAcumular.setHint("Puntos");
                    _btnRedimirAcumularPts.setText("Redimir");
                }

            }
        });

        _rbAcumular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_rbAcumular.isChecked()){
                    _rbPtsGlobales.setVisibility(View.INVISIBLE);
                    _rbPtsFidelizados.setVisibility(View.INVISIBLE);
                    _txtValorRedimirAcumular.setHint("Valor en Pesos $");
                    _btnRedimirAcumularPts.setText("Acumular");
                }else if (_rbRedimir.isChecked()){
                    _rbPtsGlobales.setVisibility(View.VISIBLE);
                    _rbPtsFidelizados.setVisibility(View.VISIBLE);
                    _txtValorRedimirAcumular.setHint("Puntos");
                    _btnRedimirAcumularPts.setText("Redimir");
                }
            }
        });



    }

    private void beginComponents() {
        _rbRedimir = (RadioButton) findViewById(R.id.radio_redimir);
        _rbAcumular = (RadioButton) findViewById(R.id.radio_acumular);
        _rbPtsGlobales = (RadioButton) findViewById(R.id.radio_globales);
        _rbPtsFidelizados = (RadioButton) findViewById(R.id.radio_Fidelizados);

        _txtValorRedimirAcumular = (TextView) findViewById(R.id.txt_valorRedimirAcumular);

        _btnRedimirAcumularPts = (Button) findViewById(R.id.btn_RedimirAcumularPts);
        _btnSalirRedm = (Button) findViewById(R.id.btn_SalirRedm);
        _txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuarioAppRed);
        _txtEmailUser = (TextView) findViewById(R.id.txtEmailUserAppRed);
        _txtIdUser = (TextView) findViewById(R.id.txtIdUserAppRed);
        _txtTelefonoUser = (TextView) findViewById(R.id.txtTelefonoUserAppRed);
        _txtPtsGlobales = (TextView) findViewById(R.id.txtPtsGlobalesUserAppRed);
        _txtPtsFidelizacion = (TextView) findViewById(R.id.txtPtsFidelizacionUserAppRed);
        _txtTipoVehiculo = (TextView) findViewById(R.id.txtTipoVehiculoUserAppRed);
    }
}
