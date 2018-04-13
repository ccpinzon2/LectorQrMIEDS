package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Logica.Placa;
import com.example.cristhianpinzon.lectorqr.Logica.RedimirAcumular;
import com.example.cristhianpinzon.lectorqr.Logica.Response_user;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Servicios.Connect;
import com.example.cristhianpinzon.lectorqr.adapters.AdapterPlacas;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("all")
public class RedAcmPtsActivity extends AppCompatActivity {

    private RadioButton _rbRedimir;
    private RadioButton _rbAcumular;
    private RadioButton _rbPtsGlobales;
    private RadioButton _rbPtsFidelizados;
    private Button      _btnRedimirAcumularPts;
    private RadioGroup tipoPuntos;

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

    private TextView tePuntosRegalo;
    private TextView tePuntosFidelizados;
    private Button btnPlaca;

    private SimpleDraweeView _imgUsuario;
    private Button   _btnSalirRedm;
    private EditText _EditValor;
    private DatabaseAccess databaseAccess;

    private List<Placa> placas;

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

        Map<String,String> datos = new HashMap<>();
        //datos.put("tck",getResources().getString(R.string.token));
        datos.put("id_user",iduser);
        datos.put("id_establecimiento",idTienda);
        datos.put("tipo",tipoTienda);

        Log.d("ATRIBUTES USER", "ID: "+iduser+" ID_EST: "+idTienda+"  TIPO: "+tipoTienda);

        Connect.getInstance(this).get_user_qr(iduser, idTienda, tipoTienda, new Callback<Response_user>() {
            @Override
            public void onResponse(Call<Response_user> call, Response<Response_user> response) {
                Log.d(TAG, "onResponse: " + response.body().toString());

                if (response.body().getStatus().equals("OK")) {
                    String idUser = (response.body().getUsuario().getId().equals(""))?"Sin Id": response.body().getUsuario().getId();
                    String nombre = (response.body().getUsuario().getNombre().equals(""))?"Sin Nombre": response.body().getUsuario().getNombre();
                    String email = (response.body().getUsuario().getEmail().equals(""))?"Sin Email": response.body().getUsuario().getEmail();
                    String tel = (response.body().getUsuario().getPhone_user().equals(""))?"Sin Telefono": response.body().getUsuario().getPhone_user();
                    String tipoV = (response.body().getUsuario().getTipo().equals(""))?"Sin Tipo": response.body().getUsuario().getTipo();

                    String url = (response.body().getUsuario().getFoto_perfil() != null ) ? response.body().getUsuario().getFoto_perfil(): "https://cdn4.iconfinder.com/data/icons/small-n-flat/24/user-alt-256.png";

                    if (tipoTienda.equals("E")) {
                        tePuntosRegalo.setVisibility(View.GONE);
                        _txtPtsGlobales.setVisibility(View.GONE);
                        btnPlaca.setVisibility(View.VISIBLE);
                        placas = response.body().getPuntosPlacas();
                    } else {
                        _txtPtsGlobales.setText(String.valueOf(response.body().getPuntosRegalo()));
                    }

                    Log.w(TAG, "fotousuario: " + url );
                    GenericDraweeHierarchy hierarchy =
                            GenericDraweeHierarchyBuilder.newInstance(getResources())
                                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                                    .setProgressBarImage(new ProgressBarDrawable())
                                    .build();
                    _imgUsuario.setHierarchy(hierarchy);
                    Uri uri = Uri.parse(url);
                    _imgUsuario.setImageURI(uri);
                    _txtIdUser.setText(idUser);
                    _txtNombreUsuario.setText(nombre);
                    _txtEmailUser.setText(email);
                    _txtTelefonoUser.setText(tel);
                    _txtTipoVehiculo.setText(tipoV);

                    progressDialog.dismiss();
                } else {
                    Log.d("Error response user: ", ""+response.body().getStatus());
                    Toast.makeText(RedAcmPtsActivity.this, "Ocurrio un problema intente mas tarde", Toast.LENGTH_SHORT).show();
                    finish();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Response_user> call, Throwable t) {
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

                if (tipoTienda.equals("E")) {
                    if (_EditValor.getText().toString().isEmpty() || _EditValor.getText().length() > 7) {
                        _EditValor.setError("Dato invalido");
                    } else if (btnPlaca.getText().toString().equals("Selecciona la placa")) {
                        Toast.makeText(RedAcmPtsActivity.this, "Selecciona una placa de usuario", Toast.LENGTH_SHORT).show();
                    } else {
                        int valor = Integer.parseInt(_EditValor.getText().toString());

                        if (_rbRedimir.isChecked()) {
                            if (placa_redimible(btnPlaca.getText().toString(), valor)) {
                                redimirAcumular();
                            } else {
                                Toast.makeText(RedAcmPtsActivity.this, "El valor no debe superar el total de puntos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            redimirAcumular();
                        }

                    }
                } else {
                    if (_EditValor.getText().toString().isEmpty() || _EditValor.getText().length() > 7) {
                        _EditValor.setError("Dato invalido");
                    } else {
                        if (_rbRedimir.isChecked()) {
                            int valor = Integer.parseInt(_EditValor.getText().toString());
                            int puntos_regalo = Integer.parseInt(_txtPtsGlobales.getText().toString());
                            if (valor <= puntos_regalo ) {
                                redimirAcumular();
                            } else {
                                Toast.makeText(RedAcmPtsActivity.this, "El valor no debe superar el total de puntos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            redimirAcumular();
                        }
                    }
                }
            }
        });

        btnPlaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_placas();
            }
        });
    }

    private void alert_placas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccione la placa del usuario");
        ListView listView = new ListView(this);
        listView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        listView.setDividerHeight(1);
        AdapterPlacas adapterPlacas = new AdapterPlacas(placas, this);
        listView.setAdapter(adapterPlacas);
        builder.setView(listView);
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        listView.setOnItemClickListener(onItemSelectedListener(alertDialog));
        alertDialog.show();
    }

    private AdapterView.OnItemClickListener onItemSelectedListener(final AlertDialog dialog) {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.cancel();
                btnPlaca.setText(placas.get(position).getPlaca());
            }
        };
    }

    private void redimirAcumular() {
        //id_user,valor,tipo_transaccion(A,R),id_redem (id del empelado o tienda), tipo_establecimiento T- tienda E EDs
        //fidelizacion -> (P, G)
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(RedAcmPtsActivity.this);
        alertDialog2.setTitle("Confirmar Transacción...");
        String msg = "";
        String val = _EditValor.getText().toString();

        if (_rbRedimir.isChecked()){
            String puntsType = (tipoTienda.equals("T")) ? "Globales" :  "Fidelizados";
            msg = "Esta seguro que desea redimir "+ val + " puntos " + puntsType + " al usuario " + _txtNombreUsuario.getText()  ;
        }else {
            msg = "Esta seguro que desea acumular $" + val  +" al usuario " +_txtNombreUsuario.getText() ;
        }

        alertDialog2.setMessage(msg);
        alertDialog2.setIcon(R.drawable.ic_warn);
        alertDialog2.setPositiveButton("SI",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        transaccion_puntos();
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Transaccion Cancelada", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        alertDialog2.show();
    }

    private void transaccion_puntos() {
        String tipo_transaccion = (_rbAcumular.isChecked()) ? "A": "R";
        String fidelizacion = (tipoTienda.equals("E")) ? "F" : "G";

        final ProgressDialog progressDialog = new ProgressDialog(RedAcmPtsActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Enviando");
        progressDialog.show();

        Map<String,String> datos = new HashMap<>();
        datos.put("id_user",iduser);
        datos.put("valor",_EditValor.getText().toString());
        datos.put("tipo_tran",tipo_transaccion);
        datos.put("idEmpTi",idRedem);
        datos.put("tipo_estab",tipoTienda);
        datos.put("tipoPunto",fidelizacion);

        if (tipoTienda.equals("E"))
            datos.put("placa", btnPlaca.getText().toString());

        Connect.getInstance(RedAcmPtsActivity.this).manage_points(datos, new Callback<RedimirAcumular>() {
            @Override
            public void onResponse(Call<RedimirAcumular> call, Response<RedimirAcumular> response) {
                Log.d(TAG, "RETROFITACUMULAR: " + response.body().getMessage());
                if (!response.body().getStatus().equals("OK")){
                    Toast.makeText(RedAcmPtsActivity.this, "No se pudo realizar la operacion", Toast.LENGTH_SHORT).show();
                }else {
                    progressDialog.dismiss();
                    try {
                        traerDatosTransaccionFinalizada();
                    }catch (Exception e){
                        Log.w(TAG, "Error trayendo ultima transaccion: " );
                    }
                    finish();
                    cargarActivityAnterior();
                }
            }

            @Override
            public void onFailure(Call<RedimirAcumular> call, Throwable t) {
                progressDialog.dismiss();
                finish();
            }
        });
    }

    private void traerDatosTransaccionFinalizada() {

        Connect.getInstance(this).get_user_qr(iduser, idTienda, tipoTienda, new Callback<Response_user>() {
            @Override
            public void onResponse(Call<Response_user> call, Response<Response_user> response) {
                int toastDurationInMilliSeconds = 6000;
                final Toast mToastToShow;


                String message_store = "Transaccion Realizada para el usuario: "
                        + response.body().getUsuario().getNombre()
                        + "\n Nuevos puntos regalo : "
                        + response.body().getPuntosRegalo();

                String message_station = "Transaccion Realizada para el usuario: "
                        + response.body().getUsuario().getNombre()
                        + "\n Nuevos Puntos placa "+btnPlaca.getText().toString()+" : "
                        + puntos_placa(btnPlaca.getText().toString(), response.body().getPuntosPlacas());

                if (tipoTienda.equals("E")) {
                    mToastToShow = Toast.makeText(getApplication(), message_station, Toast.LENGTH_LONG);
                } else {
                    mToastToShow = Toast.makeText(getApplication(), message_store, Toast.LENGTH_LONG);
                }

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
            }

            @Override
            public void onFailure(Call<Response_user> call, Throwable t) {
                Log.e("Error cargando datos",t.getMessage());
                Toast.makeText(RedAcmPtsActivity.this, "Error, Revise Su conexion ", Toast.LENGTH_SHORT).show();
                finish();
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

        tePuntosFidelizados = (TextView) findViewById(R.id.tePuntosFidelizados);
        tePuntosRegalo = (TextView) findViewById(R.id.tePuntosRegalo);

        tePuntosFidelizados.setVisibility(View.GONE);
        _txtPtsFidelizacion.setVisibility(View.GONE);

        tipoPuntos = (RadioGroup) findViewById(R.id.rbOpcionesTipoPuntos);
        tipoPuntos.setVisibility(View.GONE);

        btnPlaca = (Button) findViewById(R.id.btnPlaca);
        btnPlaca.setVisibility(View.GONE);
    }

    private Boolean placa_redimible(String placa, int valor) {
        Boolean result = false;
        for (int i =0; i < placas.size(); i++) {
            if (placas.get(i).getPlaca().equals(placa)) {
                int puntos = Integer.parseInt(placas.get(i).getPuntos());
                if (valor <= puntos ) {
                    result = true;
                }
            }
        }
        return result;
    }

    private String puntos_placa(String placa, List<Placa> nuevas_placas) {
        String result = "";
        if (nuevas_placas != null) {
            for (Placa placa1 :
                    nuevas_placas) {
                if (placa1.getPlaca().equals(placa)) {
                    result = placa1.getPuntos();
                }
            }
        }
        return result;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbarmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.contactanos:
                cargarContactanosActivity();
                return true;
            case R.id.informacion:
                Intent intent = new Intent(RedAcmPtsActivity.this, Information_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarContactanosActivity() {

        Intent intent = new Intent(getApplicationContext(),ContactanosActivity.class);
        startActivity(intent);

    }
}
