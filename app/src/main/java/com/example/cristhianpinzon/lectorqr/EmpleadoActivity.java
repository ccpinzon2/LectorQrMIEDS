package com.example.cristhianpinzon.lectorqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;

public class EmpleadoActivity extends AppCompatActivity {
    private String cedLogueado;
    private static final String TAG = "EmpleadoActivityClass";
    private TextView _txtNombreEds;
    private TextView _txtNombreEmpleadoEds;
    private TextView _txtIdTiendaEmp;
    private TextView _txtTelefonoTiendaEmp;
    private TextView _txtMarcaTiendaEmp;
    private TextView _txtDireccionTiendaEmp;
    private Button _btnCerrarSesionEmp;

    private DatabaseAccess databaseAccess;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        _txtNombreEmpleadoEds.setText(nombreEmp);
        _txtIdTiendaEmp.setText(user.getId_tienda());
        _txtTelefonoTiendaEmp.setText(user.getTelefono_tienda());
        _txtMarcaTiendaEmp.setText(user.getMarca_tienda());
        _txtDireccionTiendaEmp.setText(user.getDireccion_tienda());
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
}
