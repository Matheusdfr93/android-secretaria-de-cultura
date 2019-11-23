package com.example.matheus.secretaria;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PontoAdapter extends BaseAdapter{

    //Adapta um array de categorias para os pontos

    private static LayoutInflater inflater = null;

    Context context;
    String [] pontos;
    int [] imagensl;

    public PontoAdapter (Context context, String [] pontos, int [] imagensl)
    {
        this.context = context;
        this.pontos = pontos;
        this.imagensl = imagensl;
        inflater = (LayoutInflater)
                context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final View vista = inflater.inflate(R.layout.lista_ponto,null);

        TextView ponto = (TextView) vista.findViewById(R.id.tvPonto);
        ImageView imagem = (ImageView) vista.findViewById(R.id.imPonto);
        imagem.setImageResource(imagensl[i]);

        ponto.setText(pontos[i]);
        imagem.setTag(imagensl[i]);


        return vista;
    }

    @Override
    public int getCount() {
        return imagensl.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}
