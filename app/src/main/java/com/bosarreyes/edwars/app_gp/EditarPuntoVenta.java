package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.entidades.Cilindro;
import com.bosarreyes.edwars.app_gp.entidades.PuntoVenta;
import com.bosarreyes.edwars.app_gp.preferencias.Preferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditarPuntoVenta extends AppCompatActivity {

    private Button btnEditarPV;
    private EditText edNombre;
    private EditText edEncargado;
    private EditText edTelefono;
    private EditText edUsuario;
    private EditText edPassword;


    String usuario;
    JSONArray jsonArray;
    JSONObject js;
    String data;

    private VolleyRP volley;
    private RequestQueue mRequest;

    private static String IP= "https://soporteeb92.000webhostapp.com/gas/Consulta_PuntoVenta.php?usuario=";
    private static final String IP_EDITAR ="https://soporteeb92.000webhostapp.com/gas/PuntoVenta_UPDATE.php";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.cerrarSesion){
            Preferences.savePreferenceBoolean(EditarPuntoVenta.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
            Intent intent = new Intent(EditarPuntoVenta.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_punto_venta);

        usuario = Preferences.obtenerPreferenceString(getApplication(),Preferences.PREFERENCE_USUARIO);

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        edNombre=(EditText)findViewById(R.id.edNombrePV);
        edEncargado = (EditText)findViewById(R.id.edEncargadoPV);
        edTelefono = (EditText)findViewById(R.id.edTelefono);
        edUsuario=(EditText)findViewById(R.id.edUsuarioPV);
        edPassword=(EditText)findViewById(R.id.edPasswordPV);

        btnEditarPV =(Button)findViewById(R.id.btnEditarPV);

        btnEditarPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edNombre.getText().toString().isEmpty() || edEncargado.getText().toString().isEmpty() || edTelefono.getText().toString().isEmpty()
                        || edUsuario.getText().toString().isEmpty() || edPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Debe Ingresar todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    PuntoVenta puntoVenta = new PuntoVenta();
                    puntoVenta.setNombre(edNombre.getText().toString());
                    puntoVenta.setEncargado(edEncargado.getText().toString());
                    puntoVenta.setTelefono(edTelefono.getText().toString());
                    puntoVenta.setUsuario(edUsuario.getText().toString());
                    puntoVenta.setPassword(edPassword.getText().toString());

                    editar(puntoVenta.getNombre().toString(),puntoVenta.getEncargado().toString(),puntoVenta.getTelefono().toString(),puntoVenta.getUsuario().toString(),puntoVenta.getPassword().toString(),usuario.toString());
                }
            }
        });

        SolicitudJSON(IP+usuario);

    }

    private void editar(String nombre, String encargado, String telefono, String us, String password, String usuario) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nombre",nombre);
        hashMap.put("encargado",encargado);
        hashMap.put("telefono",telefono);
        hashMap.put("usuario",us);
        hashMap.put("password",password);
        hashMap.put("user",usuario);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, IP_EDITAR, new JSONObject(hashMap), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("resultado");
                    if (estado.equals("Se actualizaron los datos correctamente")){
                        Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT ).show();
                        Preferences.savePreferenceBoolean(EditarPuntoVenta.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
                        Intent intent = new Intent(EditarPuntoVenta.this,IngresoAdministracionPV.class);
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

    private void SolicitudJSON(String URL) {
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    //js = new JSONObject(datos.getString("resultado"));
                    data = datos.getString("resultado");
                    jsonArray = new JSONArray(data);
                    try {
                            js = jsonArray.getJSONObject(0);
                            edNombre.setText(js.getString("nombre"));
                            edEncargado.setText(js.getString("encargado"));
                            edTelefono.setText(js.getString("telefono"));
                            edUsuario.setText(js.getString("usuario"));
                            edPassword.setText(js.getString("password"));
                            //Toast.makeText(getApplication(),nombre+", "+encargado+", "+telefono+", "+user+", "+clave,Toast.LENGTH_SHORT).show();
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    //Toast.makeText(getApplication(),jsonArray.toString(),Toast.LENGTH_SHORT).show();
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

}
