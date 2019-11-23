package com.example.matheus.secretaria;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


// Essa Acivity aparece ao clicrmos "Ponto" na Activity principal. Ela lista as categorias dos pontos.


public class Ponto extends Activity {

    private ListView listapontos;
    String[] pontos = {"Patrimônio Tombado", "Patrimônio Inventariado", "Patrimônio Natural", "Artesanato", "Hoteis", "Restaurantes"};
    int[] imagensl = {R.mipmap.ic_tomabado,R.mipmap.ic_inventariado, R.mipmap.ic_natural,R.mipmap.ic_artesanato,R.mipmap.ic_hoteis,R.mipmap.ic_restaurantes};
    //int[] click1 = {R};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);

        listapontos = (ListView) findViewById(R.id.listadepontos);
        //Criando um adaptador para utilizar as Strings do Array

       listapontos.setAdapter(new PontoAdapter(this, pontos, imagensl));

        listapontos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Sessao.setCategorias(pontos[i]);
               if (i==0){
                   Sessao.setCategoriaselecionada("Patrimônio Tombado");
               }else if(i==1){
                   Sessao.setCategoriaselecionada("Patrimônio Inventariado");
               } else if(i==2){
                   Sessao.setCategoriaselecionada("Patrimonio Natural");
               }else if(i==3){
                   Sessao.setCategoriaselecionada("Artesanato");
               }else if(i==4){
                   Sessao.setCategoriaselecionada("Hoteis");
               }else if(i==5){
                   Sessao.setCategoriaselecionada("Restaurantes");
               }
                Intent it = new Intent(Ponto.this, divpontos.class);
                startActivity(it);

            }
        });

    }




}

