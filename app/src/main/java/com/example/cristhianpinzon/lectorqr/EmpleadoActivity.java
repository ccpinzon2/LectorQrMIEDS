package com.example.cristhianpinzon.lectorqr;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Logica.Transacciones;
import com.example.cristhianpinzon.lectorqr.Logica.UserApp;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioTransaccionesEmpleado;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioUserApp;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmpleadoActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private String cedLogueado;
    private static final String TAG = "EmpleadoActivityClass";
    private TextView _txtNombreEds;
    private TextView _txtNombreEmpleadoEds;
    private TextView _txtTelefonoTiendaEmp;
    private TextView _txtMarcaTiendaEmp;
    private TextView _txtDireccionTiendaEmp;
    private TextView _txtUltimaTransaccion;
    private TextView _txtUltimaCliente;

    private Button _btnCerrarSesionEmp;


    private ZXingScannerView mScannerView;

    private DatabaseAccess databaseAccess;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_empleado);
        databaseAccess = new DatabaseAccess(this);
        traerEmpleado();

        beginComponents();
        cargarDatos();

        Log.w(TAG, "Empleado cedula,"  + cedLogueado);

    }

    private void cargarDatos() {
        databaseAccess.open();
        user = databaseAccess.getUsers().get(0);
        Log.e(TAG,user.toString());
        _txtNombreEds.setText(user.getNombre_tienda());
        String nombreEmp = databaseAccess.getEmployee(cedLogueado).getNombre_empleado() + " " + databaseAccess.getEmployee(cedLogueado).getApellido_empleado() ;
        _txtNombreEmpleadoEds.setText(nombreEmp);
        //_txtIdTiendaEmp.setText(user.getId_tienda());
        _txtTelefonoTiendaEmp.setText(user.getTelefono_tienda());
        _txtMarcaTiendaEmp.setText(user.getMarca_tienda());
        _txtDireccionTiendaEmp.setText(user.getDireccion_tienda());
        Log.d(TAG, "logoEds: " + user.getLogo_tienda());

        Uri uri  = Uri.parse(user.getLogo_tienda());
        SimpleDraweeView draweeView =  (SimpleDraweeView) findViewById(R.id.frescoImgEmpleado);
        draweeView.setImageURI(uri);
        databaseAccess.close();

    }

    private void beginComponents() {
        _txtNombreEds = (TextView) findViewById(R.id.txtNombreEdsEmpleado);
        _txtNombreEmpleadoEds = (TextView) findViewById(R.id.txtNombreEmpleado);
        //_txtIdTiendaEmp = (TextView) findViewById(R.id.txtIdTiendaEmp);
        _txtTelefonoTiendaEmp = (TextView) findViewById(R.id.txtTelefonoTiendaEmp);
        _txtMarcaTiendaEmp = (TextView) findViewById(R.id.txtMarcaTiendaEmp);
        _txtDireccionTiendaEmp = (TextView) findViewById(R.id.txtDireccionTiendaEmp);
        _txtUltimaTransaccion = (TextView) findViewById(R.id.txtUltimaTransaccion);
        _txtUltimaCliente = (TextView) findViewById(R.id.txtDatosUltimoCliente);
        try {
            ultimaTransaccion();

        }catch (Exception e){
            Log.e(TAG, "beginComponents:  fallo traer ultima transaccion" + e.getMessage() );
        }
        _btnCerrarSesionEmp = (Button) findViewById(R.id.btn_CerrarSesionEmp);
        _btnCerrarSesionEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesionEmpleado();
            }
        });



    }

    private void ultimoCliente(String id_userapp) {

      //  datos.put("id_user",iduser);
      //  datos.put("id_establecimiento",idTienda);
      //  datos.put("tipo",tipoTienda);
        databaseAccess.open();

//        Log.w(TAG, "ultimoCliente: iduser->" + id_userapp + "idest ->" + databaseAccess.getUsers().get(0).getId_tienda()
//            + "tipo->" + databaseAccess.getUsers().get(0).getTipo_tienda()
//        ) ;

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getResources().getString(R.string.url_server))
                .build();
        ServicioUserApp servicioUserApp = retrofit.create(ServicioUserApp.class);

        Map<String,String> datos = new HashMap<>();

          String idEds = databaseAccess.getUsers().get(0).getId_tienda();
          datos.put("tck",getResources().getString(R.string.token));
          datos.put("id_user",id_userapp);
          datos.put("id_establecimiento",idEds);
          datos.put("tipo","E");

        Call<UserApp> call = servicioUserApp.traerUserApp(datos);

        call.enqueue(new Callback<UserApp>() {
            @Override
            public void onResponse(Call<UserApp> call, Response<UserApp> response) {
                String datos = response.body().getNombre() + "\n " + "Puntos Globales: " + response.body().getPtos_globales()
                        +"\n Puntos Fidelizados: " + response.body().getPtos_fidelizados();
                _txtUltimaCliente.setText(datos);
            }

            @Override
            public void onFailure(Call<UserApp> call, Throwable t) {
                _txtUltimaCliente.setError("Error Cargando Ultimo Cliente");
            }
        });


        databaseAccess.close();



    }

    private void ultimaTransaccion() {
        databaseAccess.open();
        //Toast.makeText(this, "cedula-> " + cedLΩogueado  + "idest-> " +databaseAccess.getUsers().get(0).getId_tienda(), Toast.LENGTH_SHORT).show();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getResources().getString(R.string.url_server))
                .build();

        ServicioTransaccionesEmpleado servicioTransacciones = retrofit.create(ServicioTransaccionesEmpleado.class);

        Map<String,String> datos = new HashMap<>();
        datos.put("tck",getResources().getString(R.string.token));
        datos.put("op","transacciones");
        datos.put("idest",databaseAccess.getUsers().get(0).getId_tienda());
        datos.put("idemp",cedLogueado);

        Call<List<Transacciones>> call = servicioTransacciones.traerTransacciones(datos);

        call.enqueue(new Callback<List<Transacciones>>() {
            @Override
            public void onResponse(Call<List<Transacciones>> call, Response<List<Transacciones>> response) {
                if (!response.body().isEmpty()){
                    String tipo = (response.body().get(0).getTipo_puntaje().equals('A')) ? "Acumulados":"Redimidos" ;
                    String userapp = response.body().get(0).getNombre_usuario();
                    String data = response.body().get(0).getPuntos() + " puntos " + tipo + " al usuario " + userapp;
                    _txtUltimaTransaccion.setText(data);
                    ultimoCliente(response.body().get(0).getId_userapp());
                }
            }

            @Override
            public void onFailure(Call<List<Transacciones>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage() );
                _txtUltimaTransaccion.setText("Sin Ultima Transaccion");
            }
        });

        databaseAccess.close();
    }

    private void cerrarSesionEmpleado() {
        databaseAccess.open();
        databaseAccess.cerrarSesionEmployees();
        cargarActivityListaEpleados();
        databaseAccess.close();
        finish();
    }

    private void cargarActivityListaEpleados() {
        Intent intent = new Intent(getApplicationContext(),LoginEmpleadosActivity.class);
        startActivity(intent);
    }

    private void traerEmpleado() {
        databaseAccess.open();
        cedLogueado = databaseAccess.getEmployeeLogueado().getCedula();
        databaseAccess.close();
    }

    @Override
    public void handleResult(Result result) {
        Log.w(TAG, "handleResult: " + result.getText() );
        cambiarPuntaje(result.getText());
        mScannerView.stopCamera();
        mScannerView.destroyDrawingCache();
    }

    private void cambiarPuntaje(String iduser) {
        // Prints scan results<br />
        try {
            String parts[] = iduser.split("/");
            Intent intent = new Intent(getApplicationContext(),RedAcmPtsActivity.class);
            Log.e("Resultado - > ", parts[1]);
            intent.putExtra("iduser",parts[1]);
            startActivity(intent);
            mScannerView.stopCamera();
            //finish();
        }catch (Exception e){
            Log.e(TAG, "cambiarPuntaje: error leerqr ->  " + e.getMessage() );
            Toast.makeText(this, "Qr Incorrecto", Toast.LENGTH_SHORT).show();
            mScannerView.stopCamera();
            finish();
            Intent intent = new Intent(getApplicationContext(),EmpleadoActivity.class);
            startActivity(intent);

        }

    }


    private void permission_request(){
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(permissionListener())
                .withErrorListener(errorListener())
                .check();
    }

    private PermissionListener permissionListener(){
        return new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Toast.makeText(EmpleadoActivity.this, "Permisos obtenidos", Toast.LENGTH_SHORT).show();
                mScannerView = new ZXingScannerView(EmpleadoActivity.this);   // Programmatically initialize the scanner view
                setContentView(mScannerView);
                mScannerView.setResultHandler(EmpleadoActivity.this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();         // Start camera
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(EmpleadoActivity.this, "Permisos negados", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };
    }

    private PermissionRequestErrorListener errorListener(){
        return new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(EmpleadoActivity.this, "Ocurrio un error en la peticion de permisos", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void QrScanner(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            permission_request();
        }else {
            mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
            setContentView(mScannerView);
            mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
            mScannerView.startCamera();         // Start camera

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            mScannerView.stopCamera();
        }catch (Exception e){
            Log.e(TAG, "onBackPressed: ERROR" );
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            mScannerView.stopCamera();
        }catch (Exception e){
            Log.e(TAG, "onBackPressed: ERROR" );
        }
        Intent intent = new Intent(getApplicationContext(),EmpleadoActivity.class);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cargarContactanosActivity() {

        Intent intent = new Intent(getApplicationContext(),ContactanosActivity.class);
        startActivity(intent);

    }
}