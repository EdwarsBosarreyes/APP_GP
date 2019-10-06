package com.bosarreyes.edwars.app_gp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.entidades.Cliente;
import com.bosarreyes.edwars.app_gp.entidades.Pedido;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class CrearCliente extends AppCompatActivity {

    private EditText nombreCliente, apellidoCliente, edad, telefono;
    private Button btnGuardarCliente;
    Cliente cli = new Cliente();
    Pedido ped = new Pedido();

    String fechaM;

    private VolleyRP volley;
    private RequestQueue mRequest;
    ProgressDialog progreso;

    private static final String IP_REGISTRAR ="https://soporteeb92.000webhostapp.com/gas/Cliente_INSERTAR.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);


        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        nombreCliente = (EditText)findViewById(R.id.nombreCliente);
        apellidoCliente = (EditText)findViewById(R.id.apellidoCliente);
        edad = (EditText)findViewById(R.id.edadCliente);
        telefono = (EditText)findViewById(R.id.telefonoCliente);

        btnGuardarCliente = (Button)findViewById(R.id.btnGuardar);

        btnGuardarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //Toast.makeText(getApplication(),cli.getId().toString()+", "+cli.getCliente().toString()+", "+pedido,Toast.LENGTH_SHORT).show();
             if (nombreCliente.getText().toString().isEmpty() || apellidoCliente.getText().toString().isEmpty() ||
                     edad.getText().toString().isEmpty() || telefono.getText().toString().isEmpty()){
                 Toast.makeText(getApplication(),"Debe ingresar todos los campos",Toast.LENGTH_SHORT).show();
             }else{
                 ped.setPedido(UUID.randomUUID().toString());
                 ped.setFecha( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                 ped.setEstado("NO ENTREGADO");
                 fechaM =  new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
                 cli.setId(UUID.randomUUID().toString());
                 cli.setNombre(nombreCliente.getText().toString());
                 cli.setApellido(apellidoCliente.getText().toString());
                 cli.setEdad(Integer.parseInt(edad.getText().toString()));
                 cli.setTelefono(telefono.getText().toString());

                 progreso= new ProgressDialog(CrearCliente.this);
                 progreso.setMessage("Registrando...");
                 progreso.show();
                 registrar(cli.getId().toString(),cli.getNombre().toString(),cli.getApellido().toString(),cli.getEdad(),cli.getTelefono().toString(),ped.getPedido().toString(),ped.getFecha().toString(),ped.getEstado().toString());
             }

            }
        });

    }

    private void registrar(String id, String nombre, String apellido, int edad, String telefono, final String pedido, String fecha, String estado) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("id",id);
        hashMap.put("nombre",nombre);
        hashMap.put("apellido",apellido);
        hashMap.put("edad", String.valueOf(edad));
        hashMap.put("telefono",telefono);
        hashMap.put("pedido",pedido);
        hashMap.put("fecha",fecha);
        hashMap.put("estado",estado);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_REGISTRAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {

                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("El Cliente se registro exitosamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(CrearCliente.this,MapaCliente.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cliente",cli);
                        bundle.putSerializable("pedido",ped);
                        bundle.putString("fechaM",fechaM);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        progreso.hide();
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
                progreso.hide();
                Toast.makeText(getApplication(), "No se pudo registrar", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }
}
