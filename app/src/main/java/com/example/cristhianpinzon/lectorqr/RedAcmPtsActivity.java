package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;
import com.example.cristhianpinzon.lectorqr.Logica.UserApp;
import com.example.cristhianpinzon.lectorqr.Logica.Usuario;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioRedimirAcumular;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioUserApp;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

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
    private Button      _btnRedimirAcumularPts;

    private String iduser;
    private String idTienda;
    private String tipoTienda;
    private String idRedem;
    private final String TAG = "REDIMIRPUNTOSACTIVITY";

    private TextView _txtNombreUsuario;
    private TextView _txtEmailUser;
    private TextView _txtIdUser;
    private TextView _txtTelefonoUser;
    private TextView _txtPtsGlobales;
    private TextView _txtPtsFidelizacion;
    private TextView _txtTipoVehiculo;

    private SimpleDraweeView _imgUsuario;
    private Button   _btnSalirRedm;
    private EditText _EditValor;
    private DatabaseAccess databaseAccess;

 //// TODO: 04/09/2017 VALIDAR QUE EL CAMPO NO ESTE VACIO AL REDIMIR/ACUMULAR Y PONER EN BLANCO AL CMABIAR DE RADIO BUTTON 
// TODO: 04/09/2017 EVITAR ERRORERS AL LEER QR INVALIDO 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_red_acm_pts);
        Fresco.initialize(this);
        databaseAccess = new DatabaseAccess(this);
        beginComponents();
        try {
            traerDatos();
            traerDatosUserApp();
        }catch (Exception e){
            Log.e(TAG, "onCreate: Error trayendo datos al leer el qr" + e.getMessage());
            databaseAccess.deleteUser();
            databaseAccess.cerrarSesionEmployees();
            finish();
            Toast.makeText(this, "La EDS no tiene empleados, por favor ingresarlos en el sistema", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }

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
        datos.put("tck",getResources().getString(R.string.token));
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

                String url = (response.body().getFoto_perfil() != null ) ? response.body().getFoto_perfil(): "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-256.png";

                Log.w(TAG, "fotousuario: " + url );
                ScalingUtils.ScaleType scale = ScalingUtils.ScaleType.FIT_CENTER;
                _imgUsuario.getHierarchy().setActualImageScaleType(scale);
                Uri uri = Uri.parse(url);
                _imgUsuario.setImageURI(uri);




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
            idRedem = databaseAccess.getEmployeeLogueado().getCedula();
        }else {
            idRedem = databaseAccess.getUsers().get(0).getId_tienda();
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
                    _EditValor.setHint("Valor en Pesos $");
                    _EditValor.setText("");
                    _btnRedimirAcumularPts.setText("Acumular");
                }else if (_rbRedimir.isChecked()){
                    _rbPtsGlobales.setVisibility(View.VISIBLE);
                    _rbPtsFidelizados.setVisibility(View.VISIBLE);
                    _EditValor.setHint("Puntos");
                    _EditValor.setText("");
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
                    _EditValor.setHint("Valor en Pesos $");
                    _btnRedimirAcumularPts.setText("Acumular");
                }else if (_rbRedimir.isChecked()){
                    _rbPtsGlobales.setVisibility(View.VISIBLE);
                    _rbPtsFidelizados.setVisibility(View.VISIBLE);
                    _EditValor.setHint("Puntos");
                    _btnRedimirAcumularPts.setText("Redimir");
                }
            }
        });
        
        _btnRedimirAcumularPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (_EditValor.getText().toString().isEmpty() || _EditValor.getText().length() > 7 ){
                    _EditValor.setError("Dato invalido");

                }else {
                    int val = Integer.parseInt(_EditValor.getText().toString());

                    int ptsGlobales = Integer.parseInt(_txtPtsGlobales.getText().toString());
                    int ptsFidelizados = Integer.parseInt(_txtPtsFidelizacion.getText().toString());

                    if (val <= ptsGlobales && _rbPtsGlobales.isChecked()) {
                        redimirAcumular();
                    } else if (val <= ptsFidelizados && _rbPtsFidelizados.isChecked()) {
                        redimirAcumular();
                    } else if (_rbAcumular.isChecked()) {
                        redimirAcumular();
                    } else {
                        Toast.makeText(RedAcmPtsActivity.this, "No puede redimir esa cantidad", Toast.LENGTH_SHORT).show();
                    }

                }

            }


        });



    }

    private void redimirAcumular() {
        //id_user,valor,tipo_transaccion(A,R),id_redem (id del empelado o tienda), tipo_establecimiento T- tienda E EDs
        //fidelizacion -> (P, G)

        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                RedAcmPtsActivity.this);

// Setting Dialog Title
        alertDialog2.setTitle("Confirmar Transaccion...");

// Setting Dialog Message
        alertDialog2.setMessage("Esta seguro de realizar esta transaccion ?");

// Setting Icon to Dialog
        alertDialog2.setIcon(R.drawable.ic_warn);

// Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        String tipo_transaccion = (_rbAcumular.isChecked()) ? "A": "R";
                        String fidelizacion = (_rbPtsFidelizados.isChecked()) ? "P" : "G";

                        final ProgressDialog progressDialog = new ProgressDialog(RedAcmPtsActivity.this,R.style.AppTheme_Dark_Dialog);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Enviando");
                        progressDialog.show();

                        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .baseUrl(getResources().getString(R.string.url_server))
                                .build();

                        ServicioRedimirAcumular servicioRedimirAcumular = retrofit.create(ServicioRedimirAcumular.class);

                        Map<String,String> datos = new HashMap<>();
                        datos.put("tck",getResources().getString(R.string.token));
                        datos.put("id_user",iduser);
                        datos.put("valor",_EditValor.getText().toString());
                        datos.put("tipo_transaccion",tipo_transaccion);
                        datos.put("id_redem",idRedem);
                        datos.put("tipo_establecimiento",tipoTienda);
                        datos.put("fidelizacion",fidelizacion);


                        Call<RedimirAcumular> call = servicioRedimirAcumular.traerResultado(datos);
                        call.enqueue(new Callback<RedimirAcumular>() {
                            @Override
                            public void onResponse(Call<RedimirAcumular> call, Response<RedimirAcumular> response) {
                                Log.d(TAG, "RETROFITACUMULAR: " + response.body().getResultado().toString());
                                if (response.body().equals("FALSE")){
                                    Toast.makeText(RedAcmPtsActivity.this, "No se pudo realizar la operacion", Toast.LENGTH_SHORT).show();
                                }else {
                                    progressDialog.dismiss();
                                    //Toast.makeText(RedAcmPtsActivity.this, "Transaccion Realizada", Toast.LENGTH_SHORT).show();
                                    traerDatosTransaccionFinalizada();

                                    finish();
                                    cargarActivityAnterior();
                                }

                                // TODO: 25/08/2017 VALIDACION DE DATOS AL REDIMIR O CUMULAR QUE NO SE PASE
                            }

                            @Override
                            public void onFailure(Call<RedimirAcumular> call, Throwable t) {
                                progressDialog.dismiss();
                                finish();

                            }
                        });
                    }
                });

// Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getApplicationContext(),
                                "Transaccion Cancelada", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });

// Showing Alert Dialog
        alertDialog2.show();



    }

    private void traerDatosTransaccionFinalizada() {



        final ProgressDialog progressDialog = new ProgressDialog(RedAcmPtsActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Realizando Transaccion ..");
        progressDialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getResources().getString(R.string.url_server))
                .build();

        ServicioUserApp servicioUserApp = retrofit.create(ServicioUserApp.class);

        Map<String,String> datos = new HashMap<>();
        datos.put("tck",getResources().getString(R.string.token));
        datos.put("id_user",iduser);
        datos.put("id_establecimiento",idTienda);
        datos.put("tipo",tipoTienda);

        Call<UserApp> call = servicioUserApp.traerUserApp(datos);

        call.enqueue(new Callback<UserApp>() {
            @Override
            public void onResponse(Call<UserApp> call, Response<UserApp> response) {


                int toastDurationInMilliSeconds = 6000;
                final Toast mToastToShow;
                mToastToShow = Toast.makeText(getApplication(),
                        "Transaccion Realizada para el usuario: "
                        + response.body().getNombre()
                        + "\n Nuevos puntos regalo : "
                        + response.body().getPtos_globales()
                        + "\n Nuevos Puntos Fidelizados : "
                        + response.body().getPtos_fidelizados(), Toast.LENGTH_LONG);
                CountDownTimer toastCountDown;
                toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000 /*Tick duration*/) {
                    public void onTick(long millisUntilFinished) {
                        mToastToShow.show();
                    }
                    public void onFinish() {
                        mToastToShow.cancel();
                    }
                };

                // Show the toast and starts the countdown
                mToastToShow.show();
                toastCountDown.start();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<UserApp> call, Throwable t) {
                Log.e("Error cargando datos",t.getMessage());
                Toast.makeText(RedAcmPtsActivity.this, "Error, Revise Su conexion ", Toast.LENGTH_SHORT).show();
                finish();
                progressDialog.dismiss();
            }
        });

    }

    private void cargarActivityAnterior() {
        databaseAccess.open();
        String tipoTienda = databaseAccess.getUsers().get(0).getTipo_tienda();

        this.finish();
        if (tipoTienda.equals("EDS")){
            Intent intent = new Intent(getApplicationContext(), EmpleadoActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
            startActivity(intent);
        }
        databaseAccess.close();
    }

    private void beginComponents() {
        _rbRedimir = (RadioButton) findViewById(R.id.radio_redimir);
        _rbAcumular = (RadioButton) findViewById(R.id.radio_acumular);
        _rbPtsGlobales = (RadioButton) findViewById(R.id.radio_globales);
        _rbPtsGlobales.setVisibility(View.INVISIBLE);
        _rbPtsFidelizados = (RadioButton) findViewById(R.id.radio_Fidelizados);
        _rbPtsFidelizados.setVisibility(View.INVISIBLE);

        _imgUsuario = (SimpleDraweeView) findViewById(R.id.frescoImgUsuario);


        //_txtValorRedimirAcumular = (TextView) findViewById(R.id.txt_valorRedimirAcumular);

        _btnRedimirAcumularPts = (Button) findViewById(R.id.btn_RedimirAcumularPts);
        _btnSalirRedm = (Button) findViewById(R.id.btn_SalirRedm);
        _btnSalirRedm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent intent = new Intent(getApplicationContext(),EmpleadoAc)
            }
        });
        _EditValor = (EditText) findViewById(R.id.edit_valorRedimirAcumular);
        _txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuarioAppRed);
        _txtEmailUser = (TextView) findViewById(R.id.txtEmailUserAppRed);
        _txtIdUser = (TextView) findViewById(R.id.txtIdUserAppRed);
        _txtTelefonoUser = (TextView) findViewById(R.id.txtTelefonoUserAppRed);
        _txtPtsGlobales = (TextView) findViewById(R.id.txtPtsGlobalesUserAppRed);
        _txtPtsFidelizacion = (TextView) findViewById(R.id.txtPtsFidelizacionUserAppRed);
        _txtTipoVehiculo = (TextView) findViewById(R.id.txtTipoVehiculoUserAppRed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent;
        if (tipoTienda.equals("EDS")){
            intent = new Intent(getApplicationContext(), EmpleadoActivity.class);
            intent.putExtra("iduser",iduser);
        }else {
            intent = new Intent(getApplicationContext(), UsuarioActivity.class);
            intent.putExtra("iduser",iduser);
        }


        startActivity(intent);

    }
}
