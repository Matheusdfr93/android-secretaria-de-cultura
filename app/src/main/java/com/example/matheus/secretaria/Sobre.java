package com.example.matheus.secretaria;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class Sobre extends AppCompatActivity {
    TextView tvdados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre);
        TextView tvdados = (TextView) findViewById(R.id.dados);
        Linkify.addLinks(tvdados, Linkify.ALL);
    }
}
