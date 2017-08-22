package com.example.cristhianpinzon.lectorqr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PuntajeActivity extends AppCompatActivity {

    private String iduser;
    private final String TAG = "PUNTAJEACTIVITY";

    private TextView _txtNombreUsuario;
    private TextView _txtEmailUser;
    private TextView _txtIdUser;
    private TextView _txtTelefonoUser;
    private TextView _txtPtsGlobales;
    private TextView _txtPtsFidelizacion;

    private Button _btnAcmRedPts;
    private Button _btnSalirPts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntaje);
        traerDatos();
        beginComponents();
        accionBotones();
    }

    private void accionBotones() {
        _btnAcmRedPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d(TAG, "onClick: Redimir Acumular" );
                Intent intent = new Intent(getApplicationContext(),RedAcmPtsActivity.class);
                intent.putExtra("iduser",iduser);
                startActivity(intent);
            }
        });
        _btnSalirPts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: SAlir" );
                finish();
            }
        });
    }

    private void traerDatos() {
        Bundle extras  =  getIntent().getExtras();
        iduser = (extras != null ) ? extras.getString("iduser"): null ;
        Log.d(TAG,"ID USER -> " + iduser);
    }

    private void beginComponents() {

        _txtNombreUsuario = (TextView) findViewById(R.id.txtNombreUsuario);
        _txtEmailUser = (TextView) findViewById(R.id.txtEmailUser);
        _txtIdUser = (TextView) findViewById(R.id.txtIdUser);
        _btnAcmRedPts = (Button) findViewById(R.id.btn_RedAcmPtos);
        _btnSalirPts = (Button) findViewById(R.id.btn_SalirPts);


    }
}
