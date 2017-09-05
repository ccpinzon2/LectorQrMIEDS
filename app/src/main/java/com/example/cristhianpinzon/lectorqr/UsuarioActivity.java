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

public class UsuarioActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private User user;
    private DatabaseAccess databaseAccess;
    private static final String TAG = "UsuarioActivity";

    private TextView _txtNombreTipo;
    private TextView _txtIdTienda;
    private TextView _txtDireccion;
    private TextView _txtTelefono;
    private TextView _txtMarca;
    private ZXingScannerView mScannerView;


    private Button _btnCerrarSesion;
    private Button _btnScaner;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_usuario);
        databaseAccess = new DatabaseAccess(this);
        beginComponets();
        try {
            loadData();
        }catch (Exception e){
            cargarLoginPrincipal();
        }

        _btnScaner = (Button) findViewById(R.id.btn_scan);
    }

    private void cargarLoginPrincipal() {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void beginComponets() {
        _txtNombreTipo = (TextView) findViewById(R.id.txtNombreTipo);
        _txtIdTienda = (TextView) findViewById(R.id.txtIdTienda);
        _txtDireccion = (TextView) findViewById(R.id.txtDireccion);
        _txtTelefono = (TextView) findViewById(R.id.txtTelefono);
        _txtMarca = (TextView) findViewById(R.id.txtMarca);
        _btnCerrarSesion = (Button) findViewById(R.id.btn_CerrarSesion);
        _btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });



    }

    private void cerrarSesion() {
        databaseAccess.open();
        databaseAccess.deleteUser();
        databaseAccess.cerrarSesionEmployees();
        databaseAccess.close();
        cargarLoginPrincipal();
        finish();

    }

    private void loadData() {
        databaseAccess.open();
        user = databaseAccess.getUsers().get(0);
        Log.e(TAG,user.toString(    ));
        _txtNombreTipo.setText(user.getTipo_tienda() + " - " + user.getNombre_tienda());
        _txtIdTienda.setText(user.getId_tienda());
        _txtDireccion.setText(user.getDireccion_tienda());
        _txtTelefono.setText(user.getTelefono_tienda());
        _txtMarca.setText(user.getMarca_tienda());

        Uri uri = Uri.parse(user.getLogo_tienda());
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.frescoImgTienda);
        draweeView.setImageURI(uri);

        databaseAccess.close();
    }

    @Override
    public void handleResult(Result rawResult) {
        Log.w(TAG, "handleResult: " + rawResult.getText() );
        cambiarPuntaje(rawResult.getText());
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
            finish();
        }catch (Exception e){
            Log.e(TAG, "cambiarPuntaje: error leerqr ->  " + e.getMessage() );
            Toast.makeText(this, "Qr Incorrecto", Toast.LENGTH_SHORT).show();
            mScannerView.stopCamera();
            finish();
            Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
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
                Toast.makeText(UsuarioActivity.this, "Permisos obtenidos", Toast.LENGTH_SHORT).show();
                mScannerView = new ZXingScannerView(UsuarioActivity.this);   // Programmatically initialize the scanner view
                setContentView(mScannerView);
                mScannerView.setResultHandler(UsuarioActivity.this); // Register ourselves as a handler for scan results.
                mScannerView.startCamera();         // Start camera
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(UsuarioActivity.this, "Permisos negados", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UsuarioActivity.this, "Ocurrio un error en la peticion de permisos", Toast.LENGTH_SHORT).show();
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
//        mScannerView.stopCamera();
    }
}