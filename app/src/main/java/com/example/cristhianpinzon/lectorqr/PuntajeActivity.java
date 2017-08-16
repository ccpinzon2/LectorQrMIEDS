package com.example.cristhianpinzon.lectorqr;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class PuntajeActivity extends AppCompatActivity {

    private String iduser;
    private final String TAG = "PUNTAJEACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntaje);
        traerDatos();
        beginComponents();
    }

    private void traerDatos() {
        Bundle extras  =  getIntent().getExtras();
        iduser = (extras != null ) ? extras.getString("iduser"): null ;
        Log.d(TAG,"ID USER -> " + iduser);
    }

    private void beginComponents() {

    }
}
