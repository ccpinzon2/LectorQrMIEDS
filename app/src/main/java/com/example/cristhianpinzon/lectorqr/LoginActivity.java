package com.example.cristhianpinzon.lectorqr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.cristhianpinzon.lectorqr.Logica.Empleado;
import com.example.cristhianpinzon.lectorqr.Logica.Response_login;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.DB.DatabaseAccess;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.Employee;
import com.example.cristhianpinzon.lectorqr.Persistence.logic.User;
import com.example.cristhianpinzon.lectorqr.Servicios.Connect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings("all")
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivityClass";

    private EditText txtUser;
    private EditText txtPassword;
    private Button btnLogin;
    private DatabaseAccess databaseAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        databaseAccess = new DatabaseAccess(this);
        beginComponents();
        validarSesion();
    }

    private void validarSesion() {
        Log.w(TAG, "validarSesion: " + traerUsuario() );
        if (traerUsuario().equals("EDS")){
            //cargarListaEmpleadosActivity();
            databaseAccess.open();
            //Log.w(TAG, "empleadologueado: " + databaseAccess.getEmployeeLogueado().toString() );
            if (databaseAccess.getEmployeeLogueado() != null){
                cargarEmpleadoAcitivy();
            }else {
                cargarListaEmpleadosActivity();
            }
            databaseAccess.close();
            finish();

        }else if (traerUsuario().equals("SINTIENDA")){
            //cargarEmpleadoAcitivy();

        }else {
            cargarUsuarioActivity();
            finish();
        }
    }

    private void cargarEmpleadoAcitivy() {
        Intent intent = new Intent(getApplicationContext(),EmpleadoActivity.class);
        startActivity(intent);
    }


    private void beginComponents() {
        txtUser = (EditText) findViewById(R.id.input_user);
        txtPassword = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();

            }
        });
    }

    private void cargarContactanosActivity() {


        Intent intent = new Intent(getApplicationContext(),ContactanosActivity.class);
        startActivity(intent);
    }

    private void cargarUsuarioActivity() {

        Intent intent = new Intent(getApplicationContext(),UsuarioActivity.class);
        startActivity(intent);
        finish();
    }

    public String traerUsuario (){
        // TODO: VALIDAR EL TIPO DE USUARIO AL CARGAR LA ACTIVITY (eds o tienda)
        //
        String tipoTienda = "";
        try {
            databaseAccess.open();
            if (databaseAccess.getUsers().size() != 0) {
                String id_user = databaseAccess.getUsers().get(0).getId_user();
                String name_user = databaseAccess.getUsers().get(0).getName_user();
                String id_tienda = databaseAccess.getUsers().get(0).getId_tienda();
                String nombre_tienda = databaseAccess.getUsers().get(0).getNombre_tienda();
                String direccion_tienda = databaseAccess.getUsers().get(0).getDireccion_tienda();
                String telefono_tienda = databaseAccess.getUsers().get(0).getTelefono_tienda();
                String tipo_tienda = databaseAccess.getUsers().get(0).getTipo_tienda();
                //Log.d(TAG, "traerUsuario: " + tipo_tienda);
                tipoTienda = tipo_tienda;
                String marca_tienda = databaseAccess.getUsers().get(0).getMarca_tienda();
                String logo_tienda = databaseAccess.getUsers().get(0).getLogo_tienda();
                User usuarioLogueado = new User(id_user, name_user, id_tienda, nombre_tienda, direccion_tienda, telefono_tienda, tipo_tienda, marca_tienda, logo_tienda);
            }else {
                tipoTienda = "SINTIENDA";
            }

                databaseAccess.close();
            return tipoTienda;
        }catch (Exception e){
            Log.e("ERROR","Error trayendo usuario de sqlite"  + e.getMessage());
        }

        return tipoTienda;
    }



    private void login() {
        Log.d(TAG,"LOGIN");
        if (!validateInputs()){
            onLoginFailed();
            return;
        }

        String user = txtUser.getText().toString();
        String pass = txtPassword.getText().toString();

        realizarLogin(user,pass);

    }

    public void realizarLogin(final String user, final String pass){

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Iniciando Sesion");
        progressDialog.show();

        Map<String,String> data = new HashMap<>();
        data.put("user", user);
        data.put("pass", pass);

        Connect.getInstance(this).login_app(data, new Callback<Response_login>() {
            @Override
            public void onResponse(Call<Response_login> call, Response<Response_login> response) {
                try {
                    Log.d("Response login", ""+response.message());
                    Log.d("Response login", ""+response.body().getStatus());
                    if (!response.body().getStatus().equals("OK")){
                        onLoginFailed();
                    } else {
                        User loguearUsuario = new User(
                                response.body().getUser().getId(),
                                response.body().getUser().getNombre(),
                                response.body().getTienda().getId(),
                                response.body().getTienda().getNombre(),
                                response.body().getTienda().getDireccion(),
                                response.body().getTienda().getTelefono(),
                                response.body().getTienda().getTipo(),
                                response.body().getTienda().getMarca(),
                                response.body().getTienda().getLogo_image()
                        );

                        Log.d("Response", response.body().getUser().toString()+"   Tienda  "+response.body().getTienda().toString());

                        loguearUsuarioApp(loguearUsuario ,  response.body().getTienda().getEmployees());

                    }
                }catch (Exception e){
                    Log.e("ErrorRetrofit",e.getMessage());
                    onLoginFailed();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Response_login> call, Throwable t) {
                Log.e("Error login",""+t.getMessage());
                onLoginFailed();
                progressDialog.dismiss();
            }
        });

    }

    private void loguearUsuarioApp(User loguearUsuario, List<Empleado> list_empleado) {
        // // TODO: 09/08/2017 AGREGAR USUARIO A SQLITE
        try {
            databaseAccess.open();
            databaseAccess.addUser(loguearUsuario);
            if (list_empleado == null){
                Log.w("logueo","Lista Nula");
                // entra direcatamente a escanear QR
                if (loguearUsuario.getTipo_tienda().equals("EDS")){
                    Toast.makeText(this, "La EDS no tiene empleados, por favor ingresarlos en el sistema", Toast.LENGTH_SHORT).show();
                    databaseAccess.deleteUser();
                    databaseAccess.cerrarSesionEmployees();
                    finish();

                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                cargarUsuarioActivity();
            }else {




                for (Empleado emp : list_empleado ) {
                    Employee employee =  new Employee(emp.getCedula_empleado(),emp.getNombre(),emp.getApellido(),0);
                    databaseAccess.open();
                    databaseAccess.addEmployee(employee);
                    databaseAccess.close();
                }
                //cargarListaUsuarios
                cargarListaEmpleadosActivity();

            }
            databaseAccess.close();

        }catch (Exception e){
            Log.e("ERROR","Error al agregar en sqlite" +e.getMessage());
            onLoginFailed();
        }

    }

    private void cargarListaEmpleadosActivity() {

        Intent intent = new Intent(getApplicationContext(),LoginEmpleadosActivity.class);
        startActivity(intent);
        finish();

    }

    public void onLoginFailed(){
        Toast.makeText(getBaseContext(),"Error al logearse",Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
    }

    public boolean validateInputs(){
        boolean valid = true;

        String user = txtUser.getText().toString();
        String pass = txtPassword.getText().toString();

        if (user.isEmpty()) {
            txtUser.setError("Ingrese usuario valido");
        }
        else {
            txtUser.setError(null);
        }
        if (pass.isEmpty() || pass.length() < 4 || pass.length() > 14 ){
            txtPassword.setError("Ingrese entre 4 y 10 caracteres alfanumericos");
            valid = false;
        }
        else {
            txtPassword.setError(null);
        }
        return valid;

    }
    private void addUser (User user) {
        databaseAccess.open();
        databaseAccess.addUser(user);
        databaseAccess.close();
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
                Intent intent = new Intent(LoginActivity.this, Information_Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
