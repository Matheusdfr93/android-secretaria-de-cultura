package com.example.matheus.secretaria;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class visualiza extends AppCompatActivity {

    TextView tvnome, tvdescricao;
    ImageView ivfoto;
    String url;

    public visualiza() throws Exception {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualiza);
        //Recebe as Strings da Intent
        String titulo = getIntent().getStringExtra("titulo");
        String descricao = getIntent().getStringExtra("descreve");
        String foto = getIntent().getStringExtra("foto");

        TextView tvnome = (TextView) findViewById(R.id.tvnome);
        tvnome.setText(titulo);
        TextView tvdescricao = (TextView) findViewById(R.id.tvdescricao);
        tvdescricao.setText(descricao);
        Linkify.addLinks(tvdescricao, Linkify.ALL);

        //ImageView ivfoto = (ImageView) findViewById(R.id.ivfoto);
        new DownloadImagemAsyncTask().execute(foto);
        url = foto;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences preferencias = getSharedPreferences("configuracao", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putString("url_imagem", url);
        editor.commit();
    }


    class DownloadImagemAsyncTask extends
            AsyncTask<String, Void, Bitmap> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(
                    visualiza.this,
                    "Aguarde", "Carregando a imagem...");
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String urlString = params[0];

            try {
                URL url = new URL(urlString);
                HttpURLConnection conexao = (HttpURLConnection)
                        url.openConnection();
                conexao.setRequestMethod("GET");
                conexao.setDoInput(true);
                conexao.connect();

                InputStream is = conexao.getInputStream();
                Bitmap imagem = BitmapFactory.decodeStream(is);
                return imagem;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if (result != null){
                ImageView ivfoto = (ImageView)findViewById(R.id.ivfoto);
                ivfoto.setImageBitmap(result);
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(visualiza.this).
                                setTitle("Erro").
                                setMessage("Não foi possivel carregar a imagem, tente novamente mais tarde!").
                                setPositiveButton("OK", null);
                builder.create().show();
            }
        }
    }



    private Object getObjeto(URL url) throws MalformedURLException, IOException {
        Object content = url.getContent();
        return content;
    }
}