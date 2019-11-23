package com.example.matheus.secretaria;

import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLException;

public class HttpConnection {

    //Tipos de métods de envio  http
    public static final int POST = 1;
    public static final int GET = 2;

    //Utilizado para receber a resposta do servidor
    private static InputStream is = null;

    private static String webservice(String urlS, String json, String metodo) {

        StringBuilder response=new StringBuilder();

        try {
            URL url = new URL(urlS);

            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
            String userCredentials = "webappadapter:via2017";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            httpURLConnection.setRequestProperty ("Authorization", basicAuth);
            httpURLConnection.setRequestMethod(metodo); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Keep-Alive", "header");

            if(metodo.equals("POST")) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`
                httpURLConnection.setRequestProperty("Content-Length", String.valueOf(json.length()));

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(json);
                wr.flush();
                wr.close();
            }

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }


    //Envia a requisição HTTP
    //Parâmetros: método (POST ou GET), url (ip, porta e caminho do script que manipulará os dados) e param (lista <NameValuePair> com os dados a serem manipulados)
    public static String execute(int method, String url, Map<String, String> params){
        String response = "";
        try {

            if(method==POST)
                response = webservice(url, new JSONObject(params).toString(), "POST"); // Versão JSON
            else if(method == GET)
                response = webservice(url, null, "GET");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

//----------------------------------------------------------------------------------------------------------------

    public static String executeWSteste(int method, String url, Map<String, String> params){
        String response = "";
        try {
            if(method == POST)
                response = webserviceWSteste(url, new JSONObject(params).toString(), "POST"); // Versão JSON
            else if(method == GET)
                response = webserviceWSteste(url, null, "GET");

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String webserviceWSteste(String urlS, String json, String metodo) {

        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlS);


/*
            HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
//            String userCredentials = "webappadapter:via2017";
//            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
//            httpURLConnection.setRequestProperty ("Authorization", basicAuth);
            httpURLConnection.setRequestMethod(metodo); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Keep-Alive", "header");
*/



            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(metodo); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.

            if(metodo.equals("POST")) {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json`

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());

                wr.writeBytes(json);
                wr.flush();
                wr.close();
            }

            int responseCode = httpURLConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {

                if(httpURLConnection.getContentType().equals("application/pdf")) {

                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    Log.d("ContentType", httpURLConnection.getContentType());

                    File pdfFile = new File(Environment.getExternalStorageDirectory(), "boleto.pdf");
                    FileOutputStream output = new FileOutputStream(pdfFile);
                    int buff = 1024;
                    byte[] infoBytes = new byte[buff];
                    while (in.read(infoBytes) != -1) {
                        output.write(infoBytes, 0, buff);
                    }

                    return pdfFile.getAbsolutePath();
                }
                else {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("Resposta", response.toString());
        return response.toString();
    }
}
