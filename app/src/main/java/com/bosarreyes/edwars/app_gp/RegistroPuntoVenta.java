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
import com.bosarreyes.edwars.app_gp.entidades.PuntoVenta;
import com.bosarreyes.edwars.app_gp.entidades.Ubicacion;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class RegistroPuntoVenta extends AppCompatActivity  {

    private Button btnCrearPV;
    private EditText nombre;
    private EditText encargado;
    private EditText telefono;
    private EditText usuario;
    private EditText password;
    String tipoUsuario = "Punto de Venta";

    private VolleyRP volley;
    private RequestQueue mRequest;
    ProgressDialog progreso;

    //private static final String IP_REGISTRAR ="http://192.168.1.5/gas/Login_INSERT.php";
    private static final String IP_REGISTRAR ="https://soporteeb92.000webhostapp.com/gas/Ubicacion_insert.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_punto_venta);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        nombre=(EditText)findViewById(R.id.edNombrePV);
        encargado = (EditText)findViewById(R.id.edEncargadoPV);
        telefono = (EditText)findViewById(R.id.edTelefono);
        usuario=(EditText)findViewById(R.id.edUsuarioPV);
        password=(EditText)findViewById(R.id.edPasswordPV);

        btnCrearPV =(Button)findViewById(R.id.btnCrearPV);

        btnCrearPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle objetoEnviado = getIntent().getExtras();
                Ubicacion ubicacion = null;

                if (objetoEnviado!=null){
                    ubicacion= (Ubicacion) objetoEnviado.getSerializable("ubicacion");
                }

                PuntoVenta puntoVenta = new PuntoVenta();
                puntoVenta.setNombre(nombre.getText().toString());
                puntoVenta.setEncargado(encargado.getText().toString());
                puntoVenta.setTelefono(telefono.getText().toString());
                puntoVenta.setUsuario(usuario.getText().toString());
                puntoVenta.setPassword(password.getText().toString());

                progreso= new ProgressDialog(RegistroPuntoVenta.this);
                progreso.setMessage("Registrando...");
                progreso.show();
                registrar(puntoVenta.getNombre().toString(),puntoVenta.getEncargado().toString(),puntoVenta.getTelefono().toString(),puntoVenta.getUsuario().toString(),puntoVenta.getPassword().toString(),
                        ubicacion.getLatitud(),ubicacion.getLongitud(),ubicacion.getDireccion().toString(),tipoUsuario.toString());
            }
        });
    }

    private void registrar(String nombre,String encargado, String telefono, String usuario, String password, double lat, double lng, String dir, String tipoUsuario) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nombre",nombre);
        hashMap.put("encargado",encargado);
        hashMap.put("telefono",telefono);
        hashMap.put("usuario",usuario);
        hashMap.put("password",password);
        hashMap.put("latitud", String.valueOf(lat));
        hashMap.put("longitud", String.valueOf(lng));
        hashMap.put("direccion",dir);
        hashMap.put("tipoUsuario",tipoUsuario);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_REGISTRAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("El usuario se registro exitosamente")){
                        progreso.hide();
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                        Intent intent = new Intent(RegistroPuntoVenta.this,IngresoAdministracionPV.class);
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
                progreso.hide();
            }
        });
        VolleyRP.addToQueue(solicitud,mRequest,this,volley);

    }


}
