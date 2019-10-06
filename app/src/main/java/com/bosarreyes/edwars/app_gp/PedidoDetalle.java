package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.adaptadores.ListAdapterCilindro;
import com.bosarreyes.edwars.app_gp.adaptadores.ListaAdapterPedido;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;
import com.bosarreyes.edwars.app_gp.entidades.Cliente;
import com.bosarreyes.edwars.app_gp.entidades.InformacionPedido;
import com.bosarreyes.edwars.app_gp.entidades.Pedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;
import com.bosarreyes.edwars.app_gp.preferencias.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PedidoDetalle extends AppCompatActivity {

    InformacionPedido infoPedido;
    TextView nombreCliente, direccion, telefono, fecha;
    ListView listaPedido;
    Button btnMapa;

    private VolleyRP volley;
    private RequestQueue mRequest;

    JSONArray jsonArray;
    JSONObject js;
    String pedido;
    String cilindros;

    List<Cilindro> list;
    ArrayAdapter<Cilindro> adapter;

    private static String IP= "https://soporteeb92.000webhostapp.com/gas/Consulta_Cilindro.php?pedido=";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.cerrarSesion){
            Preferences.savePreferenceBoolean(PedidoDetalle.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
            Intent intent = new Intent(PedidoDetalle.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_pedido);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        nombreCliente = (TextView)findViewById(R.id.txtNombreCliente);
        direccion = (TextView)findViewById(R.id.txtDireccion);
        telefono = (TextView)findViewById(R.id.txtTelefonoCliente);
        fecha = (TextView)findViewById(R.id.txtFecha);
        btnMapa = (Button)findViewById(R.id.btnMapa);

        listaPedido = (ListView)findViewById(R.id.listViewInfo);

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado!=null) {
            infoPedido = (InformacionPedido) objetoEnviado.getSerializable("detallePedido");
           // Toast.makeText(getApplication(),"Cliente: "+infoPedido.getNombre()+" "+infoPedido.getApellido()+", "+infoPedido.getTelefono()+", "+infoPedido.getFecha()+", "+
            //infoPedido.getPedido()+", "+infoPedido.getLatitud()+", "+infoPedido.getLongitud()+infoPedido.getDireccion(),Toast.LENGTH_SHORT).show();

            pedido = infoPedido.getPedido().toString();

            nombreCliente.setText(infoPedido.getNombre().toString()+" "+infoPedido.getApellido().toString());
            direccion.setText(infoPedido.getDireccion().toString());
            telefono.setText(infoPedido.getTelefono().toString());
            fecha.setText(infoPedido.getFecha());
        }

        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PedidoDetalle.this,MapaUbicacionCliente.class);
                Bundle myBundle = new Bundle();
                myBundle.putSerializable("pedido",infoPedido);
                intent.putExtras(myBundle);
                startActivity(intent);
            }
        });

        SolicitudJSON(IP+pedido);
    }

    private void SolicitudJSON(String URL) {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    cilindros = datos.getString("resultado");
                    jsonArray = new JSONArray(cilindros);
                    cargarListView(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Ocurrio un error, por favor contacte al administrador",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    private void cargarListView(JSONArray jsonArray) {

        list = new ArrayList<Cilindro>();
        ArrayList<Cilindro> list = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                js = jsonArray.getJSONObject(i);
                Cilindro cilindro = new Cilindro();
                cilindro.setCantidad(js.getInt("cantidad"));
                cilindro.setPeso(js.getString("peso"));
                cilindro.setValvula(js.getString("valvula"));
                list.add(cilindro);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new ListaAdapterPedido(PedidoDetalle.this,list);
        listaPedido.setAdapter(adapter);

    }
}
