package com.bosarreyes.edwars.app_gp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.adaptadores.ListAdapterCilindro;
import com.bosarreyes.edwars.app_gp.adaptadores.ListaAdapterPedido;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;
import com.bosarreyes.edwars.app_gp.entidades.Cliente;
import com.bosarreyes.edwars.app_gp.entidades.Pedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfirmarPedido extends AppCompatActivity {

    String usuario,nombrePV,fechaM;
    String tipoUsuario = "Cliente";
    Ubicacion ubicacion;
    Cliente  cli;
    Pedido pedido;

    List<Cilindro> list;
    private Context context;
    private ListView listaPedido;
    TextView cliente,direccion,puntoVenta,telefonoCliente,fecha;
    Button btnConfirmarPedido;

    private VolleyRP volley;
    private RequestQueue mRequest;

    ArrayAdapter<Cilindro> adapter;

    private static final String IP_REGISTRAR ="https://soporteeb92.000webhostapp.com/gas/DatosPedido_INSERTAR.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmar_pedido);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();


        Bundle objeto = getIntent().getExtras();
        list = (ArrayList<Cilindro> ) getIntent().getSerializableExtra("lista");
        if (objeto!=null) {
            ubicacion = (Ubicacion) objeto.getSerializable("ubicacion");
            cli = (Cliente)objeto.getSerializable("cliente");
            pedido = (Pedido)objeto.getSerializable("pedido");
            fechaM = objeto.getString("fechaM");
            usuario = objeto.getString("usuario");
            nombrePV = objeto.getString("puntoVenta");

        }

        cliente = (TextView)findViewById(R.id.txtNombreCliente);
        direccion = (TextView)findViewById(R.id.txtDireccion);
        puntoVenta = (TextView)findViewById(R.id.txtNombrePV);
        telefonoCliente = (TextView)findViewById(R.id.txtTelefonoCliente);
        fecha = (TextView)findViewById(R.id.txtFecha);
        btnConfirmarPedido = (Button)findViewById(R.id.btnCofirmar);

        cliente.setText(cli.getNombre().toString()+" "+cli.getApellido().toString());
        direccion.setText(ubicacion.getDireccion().toString());
        telefonoCliente.setText(cli.getTelefono().toString());
        fecha.setText(fechaM.toString());
        puntoVenta.setText(nombrePV);

        listaPedido = (ListView)findViewById(R.id.idlistView);
        adapter = new ListaAdapterPedido(ConfirmarPedido.this,list);
        listaPedido.setAdapter(adapter);

        btnConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar(ubicacion.getLatitud(),ubicacion.getLongitud(),ubicacion.getDireccion().toString(),cli.getId().toString(),tipoUsuario.toString(),pedido.getPedido().toString(),usuario.toString());
            }
        });
    }

    private void registrar(double latitud, double longitud, String direccion, String idCliente,String tipoUsuario, String pedido, String usuario) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("latitud", String.valueOf(latitud));
        hashMap.put("longitud", String.valueOf(longitud));
        hashMap.put("direccion",direccion);
        hashMap.put("idCliente",idCliente);
        hashMap.put("tipoUsuario",tipoUsuario);
        hashMap.put("pedido",pedido);
        hashMap.put("usuario",usuario);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_REGISTRAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {

                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("El Pedido se registro exitosamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(ConfirmarPedido.this,MainActivity.class);
                        ConfirmarPedido.this.finish();
                        startActivity(intent);
                    }else{
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplication(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

}
