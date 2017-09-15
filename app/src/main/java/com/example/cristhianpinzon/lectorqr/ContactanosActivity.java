package com.example.cristhianpinzon.lectorqr;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class ContactanosActivity extends AppCompatActivity {

    private ImageView  _iconFb;
    private ImageView  _iconWhatsaap;
    private ImageView  _iconTel;
    private ImageView  _iconEmail;
    private ImageView  _iconInstagram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactanos);

        beginComponents();
    }

    private void beginComponents() {

        _iconFb = (ImageView) findViewById(R.id.iconFb);
        _iconWhatsaap = (ImageView) findViewById(R.id.iconWhatsaap);
        _iconTel = (ImageView) findViewById(R.id.iconTel);
        _iconEmail = (ImageView) findViewById(R.id.iconEmail);
        _iconInstagram = (ImageView) findViewById(R.id.iconInstagram);


        _iconFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fbIntent = getOpenFacebookIntent(getApplicationContext());
                startActivity(fbIntent);
            }
        });

        _iconInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("http://instagram.com/_u/miedsgasolina");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://instagram.com/miedsgasolina")));
                }
            }
        });

        _iconWhatsaap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + "+573164458560");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            }
        });

        _iconTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "+573164458560";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                startActivity(phoneIntent);
            }
        });

        _iconEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Send email", "");

                String[] TO = {"info@mieds.com"};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contacto de Puntos MiEds");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Enviar email..."));

                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactanosActivity.this,
                            "No tiene Cliente Email", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0); //Checks if FB is even installed.
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("fb://profile/100018273609809")); //Trys to make intent with FB's URI
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/profile.php?id=100018273609809&hc_ref=ARQTkvlbZSsHIWz9dgDlag6CoUnKY52Mo6SCF-q5ytYXyhNF_VwpNpln0iRNFEyj0wQ&fref=nf&post_id=702132356643686")); //catches and opens a url to the desired page
        }
    }
}
