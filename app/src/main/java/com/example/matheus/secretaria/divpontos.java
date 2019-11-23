package com.example.matheus.secretaria;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class divpontos extends AppCompatActivity {

    ArrayList<String> nome = new ArrayList<String>();
    ArrayList<String> descricao = new ArrayList<String>();
    ArrayList<String> foto = new ArrayList<>();
    boolean primeiroacesso = true;


    //Essa Função lista os objetos pela categoria selecionada na classe anterior
    private void listaporcategoria() {
        for (int i=0;Sessao.ponto.size()>i;i++){
            if (Sessao.getCategoriaselecionada().equals(Sessao.ponto.get(i).getCategoriadescricao())) {
            nome.add(Sessao.ponto.get(i).getNome());
            descricao.add(Sessao.ponto.get(i).getDescricao());
            foto.add(Sessao.ponto.get(i).getFoto());
            }
        }
    }

    private ListView pontosporcategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divpontos);
        pontosporcategoria = (ListView) findViewById(R.id.pontosporcategoria);
        listaporcategoria();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nome);
        pontosporcategoria.setAdapter(adapter);
        pontosporcategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent tela = new Intent(divpontos.this, visualiza.class);
                tela.putExtra("titulo", nome.get(i));
                tela.putExtra("descreve", descricao.get(i));
                tela.putExtra("foto", foto.get(i));
                startActivity(tela);

            }
        });

    }
}
