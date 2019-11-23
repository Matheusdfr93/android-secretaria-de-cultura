package com.example.matheus.secretaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListadePontosAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    Context context;
    ArrayList <String> nome = new ArrayList<String>();
    ArrayList <String> descricao = new ArrayList<String>();

    public ListadePontosAdapter (Context context, ArrayList nome, ArrayList descricao)
    {
        this.context = context;
        this.nome = nome;
       // this.imagensl = imagensl;
        inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View vista = inflater.inflate(R.layout.lista_ponto,null);

        TextView ponto = (TextView) vista.findViewById(R.id.tvPonto);
        ImageView imagem = (ImageView) vista.findViewById(R.id.imPonto);
        //imagem.setImageResource(imagensl[i]);

        ponto.setText(nome.get(i));
        //imagem.setTag(imagensl[i]);


        return vista;
    }
}
