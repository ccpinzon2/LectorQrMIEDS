package com.example.cristhianpinzon.lectorqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;

public class UsuarioActivity extends AppCompatActivity {

    private User user;
    private DatabaseAccess databaseAccess;
    private static final String TAG = "UsuarioActivity";

    private TextView _txtNombreTipo;
    private TextView _txtIdTienda;
    private TextView _txtDireccion;
    private TextView _txtTelefono;
    private TextView _txtMarca;

    private Button _btnCerrarSesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        databaseAccess = new DatabaseAccess(this);
        beginComponets();
        try {
            loadData();
        }catch (Exception e){
            cargarLoginPrincipal();
        }

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
        databaseAccess.close();
        cargarLoginPrincipal();
        finish();

    }

    private void loadData() {
        databaseAccess.open();
        user = databaseAccess.getUsers().get(0);
        Log.e(TAG,user.toString());
        _txtNombreTipo.setText(user.getTipo_tienda() + " - " + user.getNombre_tienda());
        _txtIdTienda.setText(user.getId_tienda());
        _txtDireccion.setText(user.getDireccion_tienda());
        _txtTelefono.setText(user.getTelefono_tienda());
        _txtMarca.setText(user.getMarca_tienda());
        databaseAccess.close();
    }

}
