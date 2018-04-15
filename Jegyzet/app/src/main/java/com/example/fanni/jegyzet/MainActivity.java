package com.example.fanni.jegyzet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private FloatingActionButton ujJegyzetGomb;
    private File jegyzetekMappa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Android 6.0-tól kérni kell a felhasználótól az engedélyt a tárhely íráshoz
        // https://developer.android.com/training/permissions/requesting.html
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        // Új jegyzet gomb
        ujJegyzetGomb = findViewById(R.id.ujJegyzeGomb);

        // ha még nem létezik az eszköz tárhelyén a Jegyzetek mappa akkor létrehozzuk
        jegyzetekMappa = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Jegyzetek/");
        if(!jegyzetekMappa.exists()) jegyzetekMappa.mkdirs();

        // frissíti a ListView-t amiben vannak a jegyzeteink
        jegyzetListaFrissites();


        // Az új jegyzet gomb-nak beálítjuk a click listner-jét
        ujJegyzetGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kattintáskor létrehozunk egy felugró ablakot amiben megkérdezzük a felhasználótól a jegyzet nevét.
                final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                final EditText jegyzetNev = new EditText(MainActivity.this);
                jegyzetNev.setPadding(70,50,70,50);
                jegyzetNev.setBackgroundColor(Color.TRANSPARENT);
                ad.setView(jegyzetNev);
                ad.setMessage("Add meg a jegyzet nevét");

                // felugró abblak "ok" gombjának a click listenerje
                ad.setPositiveButton("Jegyzet létrehozása", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ha a felhasználó nem ad nevet a jegyzetnek nem hozzuk létre és megjelenítűnk egy értesítést
                        String jegyzetString = jegyzetNev.getText().toString().trim();
                        if(jegyzetString.equals("")){
                            Toast.makeText(MainActivity.this, "Nem adott nevet a jegyzetnek!", Toast.LENGTH_LONG).show();
                        }else{
                            // létrehozzuk a filet
                            File ujFile = new File(jegyzetekMappa, jegyzetNev.getText().toString() + ".txt");
                            // felkészülés arra hogy átváltsunk a szerkesztő képernyőre
                            Intent intent = new Intent(MainActivity.this, JegyzetSzerkesztoActivity.class);
                            intent.putExtra("ujJegyzet", true);
                            intent.putExtra("jegyzetFile", ujFile);
                            // képernyő váltás
                            startActivity(intent);
                        }
                    }
                });

                // felugró abblak mégsem gombjának a click listenerje
                ad.setNegativeButton("Mégse",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // megjelenítjuk a felugró ablakot
                ad.show();
            }
        });

    }



    public void jegyzetListaFrissites(){
        // kilistázzuk a jegyzetek mappa tartalmát
        final File[] jegyzetFileok = jegyzetekMappa.listFiles();
        String[] jegyzetCimek = new String[jegyzetFileok.length];
        String[] jegyzetDatumok = new String[jegyzetFileok.length];

        for (int i = 0; i < jegyzetFileok.length; i++) {
            // jegyzetek címe
            jegyzetCimek[i] = jegyzetFileok[i].getName().substring(0, jegyzetFileok[i].getName().length()-4);

            // kiolvassa a fájl utolsó változtatásának dátumát
            // https://stackoverflow.com/questions/4102193/getting-a-files-last-modified-date
            jegyzetDatumok[i] = new Date(jegyzetFileok[i].lastModified()).toString();
        }

        ListView jegyzetListView = findViewById(R.id.jegyzetListView);

        // beálítjuk az adaptert ami megjelenítti az elemeket a listában
        jegyzetListView.setAdapter(new CustomAdapter(this, jegyzetCimek, jegyzetDatumok));

        // minden lista elemnek adunk click listenert
        jegyzetListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // felkészülés arra hogy átváltsunk a szerkesztő képernyőre
                Intent intent = new Intent(MainActivity.this, JegyzetSzerkesztoActivity.class);
                intent.putExtra("jegyzetFile", jegyzetFileok[position]);
                intent.putExtra("ujJegyzet", false);
                // képernyő váltás
                startActivity(intent);
            }
        });

    }

    // amikor ez az aktivitás vissza tér a háttérből frissítjük a jegyzetek listáját
    @Override
    protected void onResume() {
        super.onResume();
        jegyzetListaFrissites();
    }

}
