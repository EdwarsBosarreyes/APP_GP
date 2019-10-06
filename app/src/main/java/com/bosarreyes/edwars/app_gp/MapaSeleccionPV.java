package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bosarreyes.edwars.app_gp.entidades.Cilindro;
import com.bosarreyes.edwars.app_gp.entidades.Cliente;
import com.bosarreyes.edwars.app_gp.entidades.Pedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;
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

import java.io.Serializable;
import java.util.ArrayList;

public class MapaSeleccionPV extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    JSONArray jsonArray;
    JSONObject js;

    Ubicacion ubicacion;
    Cliente cliente;
    Pedido pedido;
    String fechaM;
    ArrayList<Cilindro> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_seleccion_pv);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Bundle objeto = getIntent().getExtras();
        list = (ArrayList<Cilindro> ) getIntent().getSerializableExtra("lista");
        if (objeto!=null){
            ubicacion = (Ubicacion) objeto.getSerializable("ubicacion");
            cliente = (Cliente) objeto.getSerializable("cliente");
            pedido = (Pedido)objeto.getSerializable("pedido");
            fechaM = objeto.getString("fechaM");

            LatLng markerCliente = new LatLng(ubicacion.getLatitud(),ubicacion.getLongitud());
            mMap.addMarker(new MarkerOptions().position(markerCliente).title(ubicacion.getDireccion().toString()).icon(BitmapDescriptorFactory.fromResource(R.drawable.casa)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerCliente,12));

            String ubicaciones = objeto.getString("ubicaciones");
            try {
                jsonArray = new JSONArray(ubicaciones);
                for (int i=0;i<jsonArray.length();i++){
                    js = jsonArray.getJSONObject(i);
                    /*Toast.makeText(getApplication(),"El usuario "+js.getString("usuario")+" lat"+js.getDouble("latitud"
                    )+" long"+js.getDouble("longitud")+" dir"+js.getString("direccion"),Toast.LENGTH_SHORT).show();*/
                    LatLng marker = new LatLng(js.getDouble("latitud"), js.getDouble("longitud"));
                    mMap.addMarker(new MarkerOptions().position(marker).title(js.getString("nombre")).snippet("Telefono: "+js.getString("telefono")).icon(BitmapDescriptorFactory.fromResource(R.drawable.llamita)));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (marker.equals(marker)) {
            try {
                obtenerPosicion(marker.getPosition());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void obtenerPosicion(LatLng position) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            js = jsonArray.getJSONObject(i);
            LatLng marker = new LatLng(js.getDouble("latitud"), js.getDouble("longitud"));
            if (position.equals(marker)) {
                // Toast.makeText(getApplication(),marker.toString()+" "+js.getString("usuario"),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MapaSeleccionPV.this, ConfirmarPedido.class);
                Bundle miBundle = new Bundle();
                miBundle.putString("usuario", js.getString("usuario"));
                miBundle.putString("puntoVenta",js.getString("nombre"));
                miBundle.putSerializable("ubicacion",ubicacion);
                miBundle.putSerializable("cliente",cliente);
                miBundle.putSerializable("pedido",pedido);
                miBundle.putString("fechaM",fechaM);
                intent.putExtra("lista", (Serializable) list);
                intent.putExtras(miBundle);
                startActivity(intent);
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(marker)){
            Toast toast = Toast.makeText(getApplication(),"Presione sobre la etiqueta de información para seleccionar el Punto de Venta",Toast.LENGTH_LONG);
            LinearLayout layout = (LinearLayout) toast.getView();
            if (layout.getChildCount() > 0) {
                TextView tv = (TextView) layout.getChildAt(0);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            }
            toast.show();
        }
        return false;
    }
}

