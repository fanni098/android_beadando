package com.example.fanni.jegyzet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class JegyzetSzerkesztoActivity extends AppCompatActivity {

    private File jegyzet;
    private EditText jegyzetSzoveg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jegyzet_szerkeszto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        jegyzet = (File) getIntent().getExtras().get("jegyzetFile");
        jegyzetSzoveg = findViewById(R.id.jegyzetText);


        if(getIntent().getExtras().getBoolean("ujJegyzet")){
            // ha ujJegyzet igaz létrehozzuk a filet
            try {
                FileOutputStream fOut = new FileOutputStream(jegyzet);
                fOut.write("".getBytes());
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            // ha nem igaz akkor beolvassuk a filet és a tartalmát megjelenítjük az edittext-ben
            try {
                BufferedReader olv = new BufferedReader(new InputStreamReader(new FileInputStream(jegyzet)));
                String text = "";
                while (olv.ready()){
                    text += olv.readLine();
                    text += "\n";
                }
                jegyzetSzoveg.setText(text);

                // beállítja a cursor-t az edittext végére
                // https://stackoverflow.com/questions/6217378/place-cursor-at-the-end-of-text-in-edittext
                jegyzetSzoveg.setSelection(jegyzetSzoveg.getText().length());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        // biálítja a címsort a jegyzet nevére
        // https://stackoverflow.com/questions/26486730/in-android-app-toolbar-settitle-method-has-no-effect-application-name-is-shown
        getSupportActionBar().setTitle(jegyzet.getName().substring(0, jegyzet.getName().length()-4));


        // jegyzet mentés gomb
        FloatingActionButton jegyzetMentesGomb = (FloatingActionButton) findViewById(R.id.fab);

        //beálítjuk a jegyzetmentés gombnak a click listnerjété
        jegyzetMentesGomb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // kiirjuk a file-t
                    FileOutputStream fOut = new FileOutputStream(jegyzet);
                    fOut.write(jegyzetSzoveg.getText().toString().getBytes());
                    // megjelenítünk egy értesítést ha a mentés sikeres volt
                    Toast.makeText(JegyzetSzerkesztoActivity.this, "A jegyzet mentve.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
