package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristhianpinzon.lectorqr.Logica.TraerGremio;
import com.example.cristhianpinzon.lectorqr.Logica.Usuario;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.Employee;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioLogin;
import com.example.cristhianpinzon.lectorqr.Servicios.ServicioTraerGremio;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginEmpleadosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "LoginEmpleados" ;
    private Spinner _spinerEmpleados;
    private List<Employee> employees;
    private DatabaseAccess databaseAccess;
    private Button _btnIniciarEmpleado;
    private Button _btnCerrarSesionEds;
    private EditText _txtPassEmpleado;

    private TextView _txViewNombreEds;
    private TextView _txViewMarcaEds;
    private SimpleDraweeView _imgEds;
    private String[] nombres;

    private ImageView _imgGremio;

    private String m_Text = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_login_empleados);
        databaseAccess =  new DatabaseAccess(this);
        databaseAccess.open();
        if (!databaseAccess.getEmployees().isEmpty()){

            cargarEmployees();
            beginComponents();
            validarGremio();

        }else {
            databaseAccess.deleteUser();
            databaseAccess.cerrarSesionEmployees();
            databaseAccess.close();
            cargarLoginPrincipal();
            finish();

        }


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
        databaseAccess.open();
        _txViewNombreEds = (TextView) findViewById(R.id.txtNombreEdsLoginEmps);
        _txViewMarcaEds = (TextView) findViewById(R.id.txtMarcaEdsLoginEmps);
        _imgEds = (SimpleDraweeView) findViewById(R.id.frescoImgEds);


        _txViewNombreEds.setText(databaseAccess.getUsers().get(0).getNombre_tienda());
        _txViewMarcaEds.setText(databaseAccess.getUsers().get(0).getMarca_tienda());

        Uri uri = Uri.parse(databaseAccess.getUsers().get(0).getLogo_tienda());
        _imgEds.setImageURI(uri);

        databaseAccess.close();


        _imgGremio = (ImageView) findViewById(R.id.imgGremioLoginEmps);
        _imgGremio.setVisibility(View.INVISIBLE);
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
                alertCerrarSesion();
            }
        });
    }

    private void cargarLoginPrincipal() {
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }

    private void validarCerrarSesion(String pass) {

        // TODO: 8/09/17 ALERT CERRAR SESION


        Log.w(TAG, "cerrarSesion: "  + pass);

        databaseAccess.open();
        validarPass(databaseAccess.getUsers().get(0).getName_user(),pass);
        databaseAccess.close();

    }

    private void validarGremio() {
        String idEDS = "";
        databaseAccess.open();
        idEDS = databaseAccess.getUsers().get(0).getId_tienda();
        databaseAccess.close();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(getResources().getString(R.string.url_server))
                .build();

        ServicioTraerGremio servicioTraerGremio = retrofit.create(ServicioTraerGremio.class);

        Map<String,String> datos = new HashMap<>();
        datos.put("tck","$2y$10$zMyeP3ZCUMsYjNgMCDJ9OeE9dZLH");
        datos.put("op","traerGremio");
        datos.put("idest",idEDS);

        Call<TraerGremio> call = servicioTraerGremio.traerGremio(datos);

        call.enqueue(new Callback<TraerGremio>() {
            @Override
            public void onResponse(Call<TraerGremio> call, Response<TraerGremio> response) {

                if(response.body().getId_gremio().equals("304")){
                    _imgGremio.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<TraerGremio> call, Throwable t) {

                _imgGremio.setVisibility(View.INVISIBLE);

            }
        });



    }

    private void validarPass(String name_user, String pass) {



            final ProgressDialog progressDialog = new ProgressDialog(LoginEmpleadosActivity.this,R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Cerrando Sesion");
            progressDialog.show();


            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(getResources().getString(R.string.url_server))
                    .build();
            ServicioLogin servicioLogin = retrofit.create(ServicioLogin.class);

            Map<String,String> datos = new HashMap<>();
            datos.put("tck","$2y$10$zMyeP3ZCUMsYjNgMCDJ9OeE9dZLH");
            datos.put("user",name_user);
            datos.put("pass",pass);

            Call<Usuario> call = servicioLogin.traerUsuarioLogin(datos);

            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    try {
                        if (response.body().getName_user().equals("FALSE") || response.body().getId_user() == null){
                            falloCerrarSesion();
                        }else {

                            cerrarSesion();


                        }
                    }catch (Exception e){
                        Log.e("ErrorRetrofit",e.getMessage());

                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {

                    falloCerrarSesion();
                    progressDialog.dismiss();
                }
            });
        }

    private void cerrarSesion() {
        databaseAccess.open();
        databaseAccess.cerrarSesionEmployees();
        databaseAccess.deleteUser();
        databaseAccess.close();
        cargarLoginPrincipal();
        finish();
    }

    private void falloCerrarSesion() {
        Toast.makeText(LoginEmpleadosActivity.this, "No se pudo cerrar la sesion", Toast.LENGTH_SHORT).show();
    }


    private void alertCerrarSesion() {

        final EditText txtUrl = new EditText(this);


// Set the default text to a link of the Queen
        txtUrl.setHint("*****");
        txtUrl.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Cerrar Sesion - Gerente")
                .setMessage("Ingrese Contraseña para salir de la sesion.")
                .setView(txtUrl)
                .setPositiveButton("Cerrar Sesion", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String pass = txtUrl.getText().toString();

                        validarCerrarSesion(pass);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }

    private void loginEmpleado(String cedula) {

            databaseAccess.open();
            String ced = databaseAccess.getEmployee(cedula).getCedula();
        if (ced.equals(_txtPassEmpleado.getText().toString())){
            databaseAccess.loguearEmployee(ced);
            Intent intent = new Intent(getApplicationContext(),EmpleadoActivity.class);
            //intent.putExtra("ced",ced);
            startActivity(intent);
            finish();
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
