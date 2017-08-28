        package com.example.cristhianpinzon.lectorqr;

        import android.Manifest;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
        import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;
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

        import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class EmpleadoActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    private String cedLogueado;
    private static final String TAG = "EmpleadoActivityClass";
    private TextView _txtNombreEds;
    private TextView _txtNombreEmpleadoEds;
    private TextView _txtIdTiendaEmp;
    private TextView _txtTelefonoTiendaEmp;
    private TextView _txtMarcaTiendaEmp;
    private TextView _txtDireccionTiendaEmp;
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
        _txtNombreEds.setText(user.getTipo_tienda() + " " + user.getNombre_tienda());
        String nombreEmp = databaseAccess.getEmployee(cedLogueado).getNombre_empleado() + " " + databaseAccess.getEmployee(cedLogueado).getApellido_empleado() ;
        _txtNombreEmpleadoEds.setText("Funcionario : " + nombreEmp);
        _txtIdTiendaEmp.setText(user.getId_tienda());
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
        _txtIdTiendaEmp = (TextView) findViewById(R.id.txtIdTiendaEmp);
        _txtTelefonoTiendaEmp = (TextView) findViewById(R.id.txtTelefonoTiendaEmp);
        _txtMarcaTiendaEmp = (TextView) findViewById(R.id.txtMarcaTiendaEmp);
        _txtDireccionTiendaEmp = (TextView) findViewById(R.id.txtDireccionTiendaEmp);
        _btnCerrarSesionEmp = (Button) findViewById(R.id.btn_CerrarSesionEmp);
        _btnCerrarSesionEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesionEmpleado();
            }
        });



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
        cambiarPuntaje(result.getText());
    }

    private void cambiarPuntaje(String iduser) {
        Log.e("Resultado - > ", iduser); // Prints scan results<br />
        Intent intent = new Intent(getApplicationContext(),RedAcmPtsActivity.class);
        intent.putExtra("iduser",iduser);
        startActivity(intent);
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
        //mScannerView.stopCamera();
    }
}