package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.adaptadores.AdapterListaPedidosAsignados;
import com.bosarreyes.edwars.app_gp.entidades.InformacionPedido;
import com.bosarreyes.edwars.app_gp.preferencias.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PedidosAsignados extends AppCompatActivity {

    ListView listaPedidosAsignados;

    private VolleyRP volley;
    private RequestQueue mRequest;

    JSONArray jsonArray;
    JSONObject js;

    ArrayList<InformacionPedido> list;
    String usuario;
    String pedidos;

    ArrayAdapter<InformacionPedido> adapter;

    private static String IP= "https://soporteeb92.000webhostapp.com/gas/Consulta_Pedidos.php?usuario=";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.cerrarSesion){
            Preferences.savePreferenceBoolean(PedidosAsignados.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
            Intent intent = new Intent(PedidosAsignados.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_asignados);

        usuario = Preferences.obtenerPreferenceString(getApplication(),Preferences.PREFERENCE_USUARIO);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        listaPedidosAsignados = (ListView)findViewById(R.id.idListViewPedidos);

        SolicitudJSON(IP+usuario);
    }

    private void SolicitudJSON(String URL) {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    pedidos = datos.getString("resultado");
                    jsonArray = new JSONArray(pedidos);
                    cargarListView(jsonArray);
                    //pedidos.toString();
                    //Toast.makeText(getApplication(),pedidos,Toast.LENGTH_SHORT).show();
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

    private void cargarListView(final JSONArray jsonArray) {
        list = new ArrayList<InformacionPedido>();
        if (jsonArray.length()==0){
            Toast.makeText(getApplication(),"No tiene pedidos asignados",Toast.LENGTH_SHORT).show();
        }
        ArrayList<InformacionPedido> list = new ArrayList<>();
        try {
            for (int i=0;i<jsonArray.length();i++){
                js = jsonArray.getJSONObject(i);
                InformacionPedido infopedido = new InformacionPedido();
                infopedido.setNumero(js.getInt("no"));
                infopedido.setNombre(js.getString("nombre"));
                infopedido.setApellido(js.getString("apellido"));
                infopedido.setTelefono(js.getString("telefono"));
                infopedido.setFecha(js.getString("fecha"));
                infopedido.setPedido(js.getString("pedido"));
                infopedido.setLatitud(js.getDouble("latitud"));
                infopedido.setLongitud(js.getDouble("longitud"));
                infopedido.setDireccion(js.getString("direccion"));
                list.add(infopedido);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        adapter = new AdapterListaPedidosAsignados(list,PedidosAsignados.this);
        listaPedidosAsignados.setAdapter(adapter);

        listaPedidosAsignados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getApplication(),"" + position,Toast.LENGTH_SHORT).show();
                try {
                    for (int i=0;i<jsonArray.length();i++){
                        js = jsonArray.getJSONObject(i);
                        if(i == position){
                            InformacionPedido infoPedidido = new InformacionPedido(js.getInt("no"),js.getString("nombre"),js.getString("apellido"),js.getString("telefono"),js.getString("fecha"),js.getString("pedido"),js.getDouble("latitud"),js.getDouble("longitud"),js.getString("direccion"));
                            //Toast.makeText(getApplication(),"" + position+", "+js.getString("nombre")+" "+js.getString("pedido"),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PedidosAsignados.this,PedidoDetalle.class);
                            Bundle miBundle = new Bundle();
                            miBundle.putSerializable("detallePedido",infoPedidido);
                            intent.putExtras(miBundle);
                            startActivity(intent);
                        }
                    }
                }   catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
