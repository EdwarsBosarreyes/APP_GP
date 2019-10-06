package com.bosarreyes.edwars.app_gp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapaPuntoVenta extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador;
    double lat = 0.0;
    double lng = 0.0;
    String mensaje1;
    String direccion = "";
    Ubicacion ubicacion = new Ubicacion();

    Button btnCrearPV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_punto_venta);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);

        btnCrearPV = (Button)findViewById(R.id.btnInformacionPV);

        btnCrearPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(direccion.isEmpty()) {
                    Toast.makeText(getApplication(),"Cargando dirección, espere un momento y vuelva a intentarlo",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(MapaPuntoVenta.this, RegistroPuntoVenta.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ubicacion", ubicacion);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CameraUpdate cam = CameraUpdateFactory.newLatLng(new LatLng(
                15.6356088,-89.8988087));
        miUbicacion();


    }

    //Activar Los Servicios de GPS cuando esten apagados
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    public void setLocation(Location loc) {
        //Obetener la direccion de la calle apartir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.00) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = (DirCalle.getAddressLine(0));
                        ubicacion.setDireccion(direccion.toString());
                        if (!direccion.isEmpty()){
                            btnCrearPV.setVisibility(View.VISIBLE);
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Agregar el maracador al mapa
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions().position(coordenadas)
                .title("Direccion: " + direccion).icon(BitmapDescriptorFactory.fromResource(R.drawable.llamita)));
        mMap.animateCamera(MiUbicacion);
       // marcador.showInfoWindow();
    }

    //Actualizar la ubicacion

    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lng = location.getLongitude();
            ubicacion.setLatitud(lat);
            ubicacion.setLongitud(lng);
            AgregarMarcador(lat, lng);
        }
    }


    //Controlar el gps
    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            ActualizarUbicacion(location);
            setLocation(location);
            // Toast.makeText(getApplicationContext(),"Coordenadas "+ lat+", "+ lng+", "+direccion,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };

    //Obtener mi ubicacion

    private static int PETICION_PERMISO_LOCALIZACION = 101;

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new  String[]{Manifest.permission.ACCESS_FINE_LOCATION},PETICION_PERMISO_LOCALIZACION);
            return;
        }
        else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,12000,0,locListener);
        }
    }


    private void Mensaje() {
        Toast toast= Toast.makeText(getApplicationContext(), mensaje1, Toast.LENGTH_LONG);
        toast.show();
    }

}
