package com.example.matheus.secretaria;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.widget.ToggleButton;

import static com.example.matheus.secretaria.R.layout.notifica;

public class notifica extends AppCompatActivity {

    Button LocationsRequestButton;
    private final int LOCATION_REQUEST_CODE = 2;

    //Checkboxes
    public CheckBox checkBox,checkBox2, checkBox6,checkBox3,checkBox4,checkBox5,checkBox8;
    public Button salva;
    public ToggleButton toggleButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(notifica);
        Button salva = (Button)findViewById(R.id.salva);
        LocationsRequestButton = (Button) findViewById(R.id.GPSbton);
        LocationsRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askpermission (Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
            }
        });

        checkBox  = (CheckBox) findViewById(R.id.checkBox);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        checkBox4 = (CheckBox) findViewById(R.id.checkBox4);
        checkBox5 = (CheckBox) findViewById(R.id.checkBox5);
        checkBox6 = (CheckBox) findViewById(R.id.checkBox6);
        checkBox8 = (CheckBox) findViewById(R.id.checkBox8);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        checkBox.setChecked(Sessao.isSpt());
        checkBox3.setChecked(Sessao.isSpi());
        checkBox4.setChecked(Sessao.isSpn());
        checkBox5.setChecked(Sessao.isSho());
        checkBox6.setChecked(Sessao.isSre());
        checkBox8.setChecked(Sessao.isSart());
        checkBox2.setChecked(Sessao.isSnoti());
        toggleButton.setChecked(Sessao.isTpmapa());
    }
    //quando clicar no checkbox, verificar se está checado:

    public void salvatudo (View view){
        Sessao.setSpt(checkBox.isChecked());
        Sessao.setSpi(checkBox3.isChecked());
        Sessao.setSpn(checkBox4.isChecked());
        Sessao.setSho(checkBox5.isChecked());
        Sessao.setSre(checkBox6.isChecked());
        Sessao.setSart(checkBox8.isChecked());
        Sessao.setSnoti(checkBox2.isChecked());
        Sessao.setTpmapa(toggleButton.isChecked());

        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Shared Preferences
        SharedPreferences.Editor editor = getSharedPreferences("preferences", Context.MODE_PRIVATE).edit();
        editor.putBoolean("spt",Sessao.isSpt());
        editor.putBoolean("spn",Sessao.isSpn());
        editor.putBoolean("spi",Sessao.isSpi());
        editor.putBoolean("art",Sessao.isSart());
        editor.putBoolean("sph",Sessao.isSho());
        editor.putBoolean("sre",Sessao.isSre());
        editor.putBoolean("tom",Sessao.isTpmapa());
        editor.putBoolean("snoti",Sessao.isSnoti());
        editor.commit();

    }


    private void askpermission (String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
            //sem Permissão
            ActivityCompat.requestPermissions(this,new String[]{permission}, requestCode);

        }else{
            //Permissão
            Toast.makeText(this, "Permissão Concedida", Toast.LENGTH_SHORT).show();
        }

    }


}
