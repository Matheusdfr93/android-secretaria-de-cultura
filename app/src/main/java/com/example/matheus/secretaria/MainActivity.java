package com.example.matheus.secretaria;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.example.matheus.secretaria.R.id.map;
import static com.example.matheus.secretaria.R.layout.activity_main;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        ResultCallback<Status>,
        GoogleMap.OnMapClickListener{

    //private static final String TAG = MainActivity.class.getSimpleName();
    int aux = 0;
    ArrayList<pontos> dentro = new ArrayList<pontos>();
    protected GoogleApiClient mgoogleApiClient;
    protected ArrayList<Geofence> mGeofenceList;
    private GoogleMap mMap;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationClient;
    LocationCallback mLocationCallback;
    private GeofencingClient mGeofencingClient;
    private Geofence.Builder builder;
    Location lastLocation;
    boolean primeiroacesso = true;
    String tv;
    //private static final String SALVAtipomapa = null;

    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS = 12 * 60 * 60 * 1000;
    public static final float GEOFENCE_RADIUS_IN_METERS = 200;

    // public static final HashMap<String, LatLng> LANDMARKS = new HashMap<String, LatLng>();


    //Activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        SharedPreferences pref = getSharedPreferences("preferences", MODE_PRIVATE);
        boolean tp = (pref.getBoolean("spt", true));
        Sessao.setSpt(tp);
        boolean pi = (pref.getBoolean("spi", true));
        Sessao.setSpi(pi);
        boolean pn = (pref.getBoolean("spn", true));
        Sessao.setSpn(pn);
        boolean art = (pref.getBoolean("art", true));
        Sessao.setSart(art);
        boolean re = (pref.getBoolean("sre", true));
        Sessao.setSre(re);
        boolean ho = (pref.getBoolean("sph", true));
        Sessao.setSho(ho);
        boolean notifi = (pref.getBoolean("snoti", true));
        Sessao.setSnoti(notifi);
        boolean m = (pref.getBoolean("tom", true));
        Sessao.setTpmapa(m);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        //Geofence
        //GeofencingClient mGeofencingClient = new GeofencingClient();
        mGeofenceList = new ArrayList<Geofence>();
        mGeofencingClient = LocationServices.getGeofencingClient(this);
        GoogleApiClient mGoogleApiClient = new GoogleApiClient() {
            @Override
            public boolean hasConnectedApi(@NonNull Api<?> api) {
                return false;
            }

            @NonNull
            @Override
            public ConnectionResult getConnectionResult(@NonNull Api<?> api) {
                return null;
            }

            @Override
            public void connect() {

            }

            @Override
            public ConnectionResult blockingConnect() {
                return null;
            }

            @Override
            public ConnectionResult blockingConnect(long l, @NonNull TimeUnit timeUnit) {
                return null;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void reconnect() {

            }

            @Override
            public PendingResult<Status> clearDefaultAccountAndReconnect() {
                return null;
            }

            @Override
            public void stopAutoManage(@NonNull FragmentActivity fragmentActivity) {

            }

            @Override
            public boolean isConnected() {
                return false;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public void registerConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public boolean isConnectionCallbacksRegistered(@NonNull ConnectionCallbacks connectionCallbacks) {
                return false;
            }

            @Override
            public void unregisterConnectionCallbacks(@NonNull ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public void registerConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public boolean isConnectionFailedListenerRegistered(@NonNull OnConnectionFailedListener onConnectionFailedListener) {
                return false;
            }

            @Override
            public void unregisterConnectionFailedListener(@NonNull OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

            }
        };

        // Get the geofences used. Geofence data is hard coded in this sample.
        buildGoogleApiClient();
        //populateGeofenceList();
        //addGeofencesHandler();
        //getGeofencingRequest();
        // Kick off the request to build GoogleApiClient.

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        initLocationRequest();

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
               if (Sessao.isSnoti()) {
                   for (int i = 0; i < Sessao.ponto.size(); i++) {
                       double lat = Double.parseDouble(Sessao.ponto.get(i).getLatitude());
                       double longi = Double.parseDouble(Sessao.ponto.get(i).getLongitude());
                       String categoria = Sessao.ponto.get(i).categoriadescricao;
                       //double dist = calcDistancia(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude(), lat, longi);
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
                        //dist = dentro[i];
                       if (dist < 0.03){
                           if (!estaDentro(Sessao.ponto.get(i))) {
                               notificaopovo(Sessao.ponto.get(i));
                               insere(Sessao.ponto.get(i));
                           } else {

                           }

                       }else {
                           for (int j=0; j<dentro.size(); j++) {
                               if (Sessao.ponto.get(i).getId() == dentro.get(j).getId()) {
                                   remove(Sessao.ponto.get(i));
                               } else{

                               }
                           }
                       }

                   }
               }
                }


            };


        startLocationUpdates();

    }

    private boolean estaDentro(pontos p){
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

    private void insere (pontos p){
        if (!estaDentro(p)){
            dentro.add(p);
        }

    }

    private void remove (pontos p){
        dentro.remove(p);
    }

    //notifica
    public void notificaopovo(pontos ponto){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(
                        this)
                        .setSmallIcon(R.mipmap.iconedenotificacao)
                        .setContentTitle("Local Próximo")
                        .setContentText("Você está próximo ao ponto "+ ponto.nome);

        Intent resultIntent = new Intent(this, visualiza.class);
        resultIntent.putExtra("titulo", ponto.getNome());
        resultIntent.putExtra("descreve", ponto.getDescricao());
        resultIntent.putExtra("foto", ponto.getFoto());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(visualiza.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setVibrate(new long[] { 500, 500 });
        mBuilder.setColor(getResources().getColor(R.color.colorPrimary));
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //mId allows you to update the notification later on.
        mNotificationManager.notify(0, mBuilder.build());


    }

    //location
    private void initLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
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



/*    public void addGeofencesHandler() {
        if (!mgoogleApiClient.isConnected()) {
            //Toast.makeText(this, "Google API Client not connected!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            LocationServices.GeofencingApi.addGeofences(
                    mgoogleApiClient,
                    getGeofencingRequest(),
                    getGeofencePendingIntent()
            ).setResultCallback(this); // Result processed in onResult().
        } catch (SecurityException securityException) {
            // Catch exception generated if the app does not use ACCESS_FINE_LOCATION permission.
        }
    }*/

    protected synchronized void buildGoogleApiClient() {
        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();

        //mGeofencingClient = new GeofencingClient(this, mgoogleApiClient);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mgoogleApiClient.isConnecting() || !mgoogleApiClient.isConnected()) {
            mgoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mgoogleApiClient.isConnecting() || mgoogleApiClient.isConnected()) {
            mgoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!primeiroacesso) {
            mMap.clear();
            exibePontos();

        }
        primeiroacesso = false;
    }



    // Botões [início]
    public void chamanoti(View v) {
        startActivity(new Intent(this, notifica.class));
    }


    public void chamafiltro(View view) {
        Intent intent = new Intent(this, Ponto.class);
        startActivity(intent);
    }

    public void chamasobre(View view){
     Intent intent = new Intent (this, Sobre.class);
     startActivity(intent);
    }

    //methods implementados
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogleApiClient);
        if (lastLocation!= null) {

        }


    }

    @Override
    public void onConnectionSuspended(int cause) {
        mgoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

/*    private PendingIntent getGeofencePendingIntent() {
        Intent intent = new Intent(this, Servico.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling addgeoFences()
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
*/


    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }



    // Botões [fim]

    // Json -> Reconhecimento dos dados do DB

    private class TaskBusca extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            Map<String, String> parametros = new HashMap<String, String>();

            //---------------------------------------------------------------------------------------
            /*ATENÇÃO PARA O ENDEREÇO*/
            String url = "https://secultcampanha.000webhostapp.com/jsonconnect.php";
            String response = HttpConnection.executeWSteste(HttpConnection.POST, url, parametros);
            //---------------------------------------------------------------------------------------


            return response;
        }

        protected void onPostExecute(String retorno) {

            String nome = "", descricao = "", saida = "", latitude = "", longitude = "";
            int i = 0;
            // int id, categoriadescricao;
            try {
                JSONObject jsonObject = new JSONObject(retorno);
                JSONArray jsonArray = jsonObject.getJSONArray("pontos");

                for (i = 0; i < jsonArray.length(); i++) {
                    JSONObject aux = (JSONObject) jsonArray.get(i);
                    pontos x = new pontos();
                    x.setNome(aux.getString("Nome"));
                    x.setDescricao(aux.getString("descricao"));
                    x.setLatitude(aux.getString("latitude"));
                    x.setLongitude(aux.getString("longitude"));
                    x.setId(aux.getInt("id"));
                    x.setCategoriadescricao(aux.getString("categoriadescricao"));
                    x.setFoto(aux.getString("foto"));
                    Sessao.ponto.add(x);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            exibePontos();
        }

    }


    //Fragmento de Mapa [início];
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //exibePontos();
 //       mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        mMap = googleMap;
        UiSettings mapUiSettings = mMap.getUiSettings();
        mapUiSettings.setZoomControlsEnabled(true);
        mapUiSettings.setCompassEnabled(true);
        mapUiSettings.setMyLocationButtonEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-21.835, -45.399), 17));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        new TaskBusca().execute();
        //getGeofencingRequest();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                boolean r = false;
                String foto = "",desc = "";
                for (int i = 0;i < Sessao.ponto.size();i++){
                    if (Sessao.ponto.get(i).getNome().equals(marker.getTitle())){
                        desc = Sessao.ponto.get(i).getDescricao();
                        foto = Sessao.ponto.get(i).getFoto();

                    }


                }
                Intent tela = new Intent(MainActivity.this, visualiza.class);
                tela.putExtra("titulo", marker.getTitle());
                tela.putExtra("descreve", desc);
                tela.putExtra("foto", foto);
                startActivity(tela);

                return r;
            }

        });


    }


    public void exibePontos() {

        for (int i = 0; i < Sessao.ponto.size(); i++) {
            double lat = Double.parseDouble(Sessao.ponto.get(i).getLatitude());
            double longi = Double.parseDouble(Sessao.ponto.get(i).getLongitude());
            String nome = Sessao.ponto.get(i).getNome();
            String categoria = Sessao.ponto.get(i).categoriadescricao;
            if (Sessao.isTpmapa()) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            } else {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }


            if (categoria.equals("Patrimônio Tombado") && Sessao.isSpt()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            } else if (categoria.equals("Patrimonio Natural") && Sessao.isSpn()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
            } else if (categoria.equals("Patrimônio Inventariado") && Sessao.isSpi()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).
                        title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            } else if (categoria.equals("Artesanato") && Sessao.isSart()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            } else if (categoria.equals("Hoteis") && Sessao.isSho()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
            } else if (categoria.equals("Restaurantes") && Sessao.isSre()) {
                LatLng secult1 = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(secult1).title(nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            }





        }
    }




    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }

    // Localização

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


    protected boolean isRouteDisplayed() {
        return false;
    }

    public class pontos {
        private String nome;
        private String descricao;
        private String latitude;

        public String getFoto() {
            return foto;
        }

        public void setFoto(String foto) {
            this.foto = foto;
        }

        private String foto;

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getCategoriadescricao() {
            return categoriadescricao;
        }

        public void setCategoriadescricao(String categoriadescricao) {
            this.categoriadescricao = categoriadescricao;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        private String longitude;
        private String categoriadescricao;
        private int id;

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

}
