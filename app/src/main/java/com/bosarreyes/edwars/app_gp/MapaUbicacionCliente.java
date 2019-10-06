package com.bosarreyes.edwars.app_gp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.bosarreyes.edwars.app_gp.entidades.InformacionPedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;
import com.bosarreyes.edwars.app_gp.preferencias.Preferences;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapaUbicacionCliente extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    InformacionPedido infoPedido;
    private double latitudOrigen,longitudOrigen, latitudDestino, longitudDestino;
    private String direccionOrigen = "";
    private Marker marcadorOrigen, marcadorDestino;
    private Button pedidoEntregado;
    private String pedido;



    private String mensaje1;
    private  String direccionDestino;
    Ubicacion ubicacion = new Ubicacion();

    private VolleyRP volley;
    private RequestQueue mRequest;

    static final String ESTADOPEDIDO = "ENTREGADO";
    private static final String IP_EDITAR ="https://soporteeb92.000webhostapp.com/gas/UPDATE_Pedido.php";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.cerrarSesion){
            Preferences.savePreferenceBoolean(MapaUbicacionCliente.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
            Intent intent = new Intent(MapaUbicacionCliente.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_ubicacion_cliente);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        pedidoEntregado = (Button)findViewById(R.id.btnPedidoEntregado);
        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado!=null) {
            infoPedido = (InformacionPedido) objetoEnviado.getSerializable("pedido");
            latitudDestino = infoPedido.getLatitud();
            longitudDestino = infoPedido.getLongitud();
            direccionDestino = infoPedido.getDireccion().toString();
            pedido = infoPedido.getPedido().toString();
        }

        pedidoEntregado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(MapaUbicacionCliente.this);
                dialogo1.setTitle("Pedido Entregado");
                dialogo1.setMessage("¿ Desea marcar el pedido como entregado ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        aceptar();
                    }
                });
                dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        cancelar();
                    }
                });
                dialogo1.show();
            }

            public void aceptar() {
                editar(ESTADOPEDIDO,pedido);
            }

            public void cancelar() {
                finish();
            }
        });
    }

    private void editar(String estadopedido, String pedido) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("estado",estadopedido);
        hashMap.put("pedido",pedido);


        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_EDITAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("Se actualizaron los datos correctamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                        Preferences.savePreferenceBoolean(MapaUbicacionCliente.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
                        Intent intent = new Intent(MapaUbicacionCliente.this,PedidosAsignados.class);
                        startActivity(intent);
                        finish();

                    }else{
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), "No se pudo Editar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "No se pudo Editar el registro", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);

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


        // Add a marker in Sydney and move the camera
        LatLng ubicacionDestino = new LatLng(latitudDestino, longitudDestino);
        CameraUpdate MiUbica = CameraUpdateFactory.newLatLngZoom(ubicacionDestino, 11);
        if (marcadorDestino != null) marcadorDestino.remove();
        marcadorDestino = mMap.addMarker(new MarkerOptions().position(ubicacionDestino)
                .title("Direccion: " + direccionDestino).icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)));
        mMap.animateCamera(MiUbica);
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
                    direccionOrigen = (DirCalle.getAddressLine(0));
                    ubicacion.setDireccion(direccionOrigen.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Agregar el maracador al mapa
    private void AgregarMarcador(double lat, double lng) {
        LatLng coordenadas = new LatLng(lat, lng);
        if (marcadorOrigen != null) marcadorOrigen.remove();
        marcadorOrigen = mMap.addMarker(new MarkerOptions().position(coordenadas)
                .title("Direccion: " + direccionOrigen).icon(BitmapDescriptorFactory.fromResource(R.drawable.llamita)));
        // marcador.showInfoWindow();
    }

    //Actualizar la ubicacion

    private void ActualizarUbicacion(Location location) {
        if (location != null) {
            latitudOrigen = location.getLatitude();
            longitudOrigen = location.getLongitude();
            ubicacion.setLatitud(latitudOrigen);
            ubicacion.setLongitud(longitudOrigen);
            AgregarMarcador(latitudOrigen, longitudOrigen);
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
