package com.example.cristhianpinzon.lectorqr;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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
                Uri uri = Uri.parse("smsto:" + "+6288889999");
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.setPackage("com.whatsapp");
                startActivity(Intent.createChooser(i, ""));
            }
        });

        _iconTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = "31123402021";
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", phone, null));
                startActivity(phoneIntent);
            }
        });

        _iconEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , "mieds@mieds.com");
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                emailIntent.putExtra(Intent.EXTRA_TEXT   , "Hola!");

                emailIntent.setType("text/plain"); // <-- HERE
                startActivity(emailIntent); // <-- AND HERE

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
