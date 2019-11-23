package com.example.matheus.secretaria;

import android.Manifest;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

public class Servico extends IntentService {

    protected static final String TAG = "GeofenceTransitionsIS";
    public Servico() {
        super(TAG);  // use TAG to name the IntentService worker thread
    }

    ArrayList<MainActivity.pontos> dentro = new ArrayList<MainActivity.pontos>();
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    int aux = 0;
    ArrayList<MainActivity.pontos> ponto = new ArrayList<MainActivity.pontos>();

    @Override
    protected void onHandleIntent(Intent intent) {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initLocationRequest();

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                String s = "";
                if (Sessao.isSnoti()) {
                    for (int i = 0; i < ponto.size(); i++) {
                        double lat = Double.parseDouble(ponto.get(i).getLatitude());
                        double longi = Double.parseDouble(ponto.get(i).getLongitude());
                        String categoria = ponto.get(i).getCategoriadescricao();
                        //double dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        s = s + " ";
                        double dist = 10000;
                        if (categoria.equals("Patrimônio Tombado") && Sessao.isSpt()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        } else if (categoria.equals("Patrimonio Natural") && Sessao.isSpn()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        } else if (categoria.equals("Patrimônio Inventariado") && Sessao.isSpi()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        } else if (categoria.equals("Artesanato") && Sessao.isSart()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        } else if (categoria.equals("Hoteis") && Sessao.isSho()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        } else if (categoria.equals("Restaurantes") && Sessao.isSre()) {
                            dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
                        }

                        if (dist < 0.03){
                            if (!estaDentro(ponto.get(i))) {
                                notificaopovo(ponto.get(i));
                                insere(ponto.get(i));
                            } else {

                            }

                        }else {
                            for (int j=0; j<dentro.size(); j++) {
                                if (ponto.get(i).getId() == dentro.get(j).getId()) {
                                    remove(ponto.get(i));
                                } else{

                                }
                            }
                        }
                    }
                    s += "\n";
                }
            }


        };


        startLocationUpdates();

    }

    public void notificaopovo(MainActivity.pontos ponto){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.iconedenotificacao)
                        .setContentTitle("Local Próximo")
                        .setContentText(ponto.getNome());
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, visualiza.class);
        resultIntent.putExtra("titulo", ponto.getNome());
        resultIntent.putExtra("descreve", ponto.getDescricao());
        resultIntent.putExtra("foto", ponto.getFoto());

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(visualiza.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setVibrate(new long[] { 500, 500 });
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());


    }
    private double calcDistancia(double lat_inicial, double long_inicial, double lat_final, double long_final) {

        double d2r = 0.017453292519943295769236;

        double dlong = (long_final - long_inicial) * d2r;
        double dlat = (lat_final - lat_inicial) * d2r;

        double temp_sin = Math.sin(dlat/2.0);
        double temp_cos = Math.cos(lat_inicial * d2r);
        double temp_sin2 = Math.sin(dlong/2.0);

        double a = (temp_sin * temp_sin) + (temp_cos * temp_cos) * (temp_sin2 * temp_sin2);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return 6368.1 * c;
    }

    //location
    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        //Confere se possui a permissão FINE_LOCATION
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            //tv.append("\nProblema de permissão");
            Toast.makeText(this,"Problema na transmissão",Toast.LENGTH_SHORT).show();
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback,null /* Looper */);
    }

    private void lista(){
        for (int i = 0; i<Sessao.ponto.size(); i++){
            ponto.add(Sessao.ponto.get(i));
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();
        lista();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private boolean estaDentro(MainActivity.pontos p){
        boolean r = false;
        for (int i = 0; i < dentro.size();i++){
            if(dentro.get(i).getId() == p.getId()){
                r = true;
            }
            else {
                r = false;
            }
        }
        return r;
    }

    private void insere (MainActivity.pontos p){
        if (!estaDentro(p)){
            dentro.add(p);
        }

    }

    private void remove (MainActivity.pontos p){
        dentro.remove(p);
    }




}