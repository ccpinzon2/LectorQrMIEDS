package com.example.cristhianpinzon.lectorqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.Employee;

import java.util.List;

public class LoginEmpleadosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner _spinerEmpleados;
    private List<Employee> employees;
    private DatabaseAccess databaseAccess;
    private Button _btnIniciarEmpleado;
    private Button _btnCerrarSesionEds;
    private EditText _txtPassEmpleado;
    private String[] nombres;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_empleados);
        databaseAccess =  new DatabaseAccess(this);
        cargarEmployees();
        beginComponents();
    }

    private void cargarEmployees() {

        databaseAccess.open();
        employees = databaseAccess.getEmployees();
        nombres = new String[employees.size()];
        Log.w("TAM" , "BD-> " + employees.size() + "arreglo -> " + nombres.length);
        for (int i=0; i < employees.size() ; i++) {
            Log.w("TAM", "Tam I ->  " + i );
            nombres[i] = (employees.get(i).getNombre_empleado() + " " + employees.get(i).getApellido_empleado() );
        }
        databaseAccess.close();

    }

    private void beginComponents() {
        _spinerEmpleados = (Spinner) findViewById(R.id.spinnerLogin);
        _spinerEmpleados.setOnItemSelectedListener(this);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,nombres);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinerEmpleados.setAdapter(adapter);

        _txtPassEmpleado = (EditText) findViewById(R.id.input_password_empleado);
        _btnIniciarEmpleado = (Button) findViewById(R.id.btn_IniciarEmpleado);
        _btnIniciarEmpleado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 22/08/2017  hacer nueva columna en sqlite de logueo del empleado
                //Log.d("pos 1" , " " + employees.get(_spinerEmpleados.getSelectedItemPosition()).getNombre_empleado());

                loginEmpleado(employees.get(_spinerEmpleados.getSelectedItemPosition()).getCedula());

            }
        });
        _btnCerrarSesionEds = (Button) findViewById(R.id.btn_CerrarSesionEds);
        _btnCerrarSesionEds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cerrarSesion();
            }
        });
    }

    private void cargarLoginPrincipal() {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void cerrarSesion() {
        databaseAccess.open();
        databaseAccess.cerrarSesionEmployees();
        databaseAccess.deleteUser();
        databaseAccess.close();
        cargarLoginPrincipal();
        finish();
    }

    private void loginEmpleado(String cedula) {

            databaseAccess.open();
            String ced = databaseAccess.getEmployee(cedula).getCedula();
        if (ced.equals(_txtPassEmpleado.getText().toString())){
            databaseAccess.loguearEmployee(ced);
            Intent intent = new Intent(getApplicationContext(),EmpleadoActivity.class);
            //intent.putExtra("ced",ced);
            startActivity(intent);
        }else {

            Toast.makeText(this, "Sesion Incorrecta", Toast.LENGTH_SHORT).show();
        }

        databaseAccess.close();

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
