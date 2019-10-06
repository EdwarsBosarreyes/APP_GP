package com.bosarreyes.edwars.app_gp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.adaptadores.ListAdapterCilindro;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;
import com.bosarreyes.edwars.app_gp.entidades.Cliente;
import com.bosarreyes.edwars.app_gp.entidades.Pedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatosPedido extends AppCompatActivity {

    EditText edCantidad;
    Spinner spPeso, spValvula;
    String seleccionPeso="";
    String seleccionValvula="";
    String ubicaciones;
    ArrayAdapter adapterValvula;
    private VolleyRP volley;
    private RequestQueue mRequest;
    int cant;
    String pes, valv;

    private static final String IP_REGISTRAR ="https://soporteeb92.000webhostapp.com/gas/Cilindro_INSERT.php";
    private static final String IP_ELIMINAR="https://soporteeb92.000webhostapp.com/gas/Cilindro_ELIMINAR.php";
    private static final String URL_GET_ALLPV ="https://soporteeb92.000webhostapp.com/gas/Consulta_Ubicacion.php";

    Button btnAnyadir, btnEliminar, btnContinuar;

    private ListView listaPedido;

    List<Cilindro> list;
    Ubicacion ubicacion;
    Cliente cliente;
    Pedido pedido;
    String fechaM;

    ArrayAdapter<Cilindro> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_pedido);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        Bundle objetoEnviado = getIntent().getExtras();
        if (objetoEnviado!=null) {
            ubicacion = (Ubicacion) objetoEnviado.getSerializable("ubicacion");
            cliente = (Cliente) objetoEnviado.getSerializable("cliente");
            pedido = (Pedido)objetoEnviado.getSerializable("pedido");
            fechaM = objetoEnviado.getString("fechaM");
        }

        btnContinuar = (Button)findViewById(R.id.btnContinuar);


        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (list.isEmpty()){
                    Toast.makeText(getApplication(),"Debe Seleccionar al menos un producto",Toast.LENGTH_SHORT).show();
                }else{
                    //Toast.makeText(getApplication(),ubicacion.getLatitud()+", "+ubicacion.getLongitud()+", "+nombre+", "+pedido,Toast.LENGTH_SHORT).show();
                    SolicitudJSON();
                }

            }
        });

        spPeso = (Spinner)findViewById(R.id.idspinnerPeso);
        spValvula =(Spinner)findViewById(R.id.idspinnerValvula);


        ArrayAdapter adapterPeso = ArrayAdapter.createFromResource( this, R.array.peso , android.R.layout.simple_spinner_item);
        adapterPeso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeso.setAdapter(adapterPeso);

        spPeso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccionPeso = parent.getItemAtPosition(position).toString();

                switch (seleccionPeso){
                    case "Seleccione":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.seleccione_inicial , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "20":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_20 , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "25":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_25 , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "35":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_35, android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "40":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_40 , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "60":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_60 , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;
                    case "100":
                        adapterValvula = ArrayAdapter.createFromResource( getApplication(), R.array.valvula_100 , android.R.layout.simple_spinner_item);
                        adapterValvula.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spValvula.setAdapter(adapterValvula);
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spValvula.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seleccionValvula = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addView();
        eliminar();

    }

    private void addView() {

        btnAnyadir = (Button)findViewById(R.id.btnAnyadir);
        edCantidad = (EditText)findViewById(R.id.edCantidad);

        btnAnyadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edCantidad.getText().toString().isEmpty() || seleccionPeso.equals("Seleccione") || seleccionValvula.equals("Seleccione")){
                    Toast.makeText(getApplication(),"Debe Ingresar todos los campos",Toast.LENGTH_SHORT).show();
                }
                else{
                    list.add(get(Integer.parseInt(edCantidad.getText().toString()),seleccionPeso,seleccionValvula));
                    registrar(pedido.getPedido().toString(),Integer.parseInt(edCantidad.getText().toString()),seleccionPeso.toString(),seleccionValvula.toString());
                    adapter.notifyDataSetChanged();
                    edCantidad.getText().clear();
                    spPeso.setSelection(0);
                    spValvula.setSelection(0);
                }

            }
        });

        listaPedido = (ListView)findViewById(R.id.idListPedido);
        adapter = new ListAdapterCilindro(DatosPedido.this,getListPedidos());
        listaPedido.setAdapter(adapter);
    }

    private void registrar(String pedido, int cantidad, String seleccionPeso, String seleccionValvula) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pedido",pedido);
        hashMap.put("cantidad", String.valueOf(cantidad));
        hashMap.put("peso",seleccionPeso);
        hashMap.put("valvula",seleccionValvula);


        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_REGISTRAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("El Cilindro se registro exitosamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
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

    private  List<Cilindro> getListPedidos() {
        list=new ArrayList<Cilindro>();
        return list;
    }

    private Cilindro get(int cantidad , String peso, String valvula) {
        return new Cilindro(cantidad, peso, valvula);
    }


    private void eliminar() {
        btnEliminar = (Button)findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=list.size()-1; i>=0; i--) {
                    if (list.get(i).isSelected()) {
                        cant = list.get(i).getCantidad();
                        pes = list.get(i).getPeso().toString();
                        valv = list.get(i).getValvula().toString();
                        eliminarCilindro(pedido.getPedido().toString(),cant,pes,valv);
                        list.remove(i);


                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void eliminarCilindro(String pedido,int cantidad, String peso, String valvula) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("pedido",pedido);
        hashMap.put("cantidad", String.valueOf(cantidad));
        hashMap.put("peso",peso);
        hashMap.put("valvula",valvula);


        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_ELIMINAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("Se elimino el cilindro correctamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                    }else{
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplication(), "No se pudo Eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(), "No se pudo Eliminar", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    private void SolicitudJSON() {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_GET_ALLPV, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                //Toast.makeText(MainActivity.this,datos.toString(),Toast.LENGTH_SHORT).show();
                try {
                    ubicaciones = datos.getString("resultado");
                    ubicaciones.toString();
                    Intent intent = new Intent(DatosPedido.this,MapaSeleccionPV.class);
                    Bundle mibundle = new Bundle();
                    mibundle.putString("ubicaciones", ubicaciones);
                    mibundle.putSerializable("ubicacion",ubicacion);
                    mibundle.putSerializable("cliente",cliente);
                    mibundle.putSerializable("pedido",pedido);
                    mibundle.putString("fechaM",fechaM);
                    intent.putExtras(mibundle);
                    intent.putExtra("lista", (Serializable) list);
                    startActivity(intent);
                    //Toast.makeText(getApplication(),ubicaciones,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(getApplication(),"Ha ocurrido un error: ",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Ha Ocurrido un Error",Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

}
