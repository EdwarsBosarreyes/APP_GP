package com.bosarreyes.edwars.app_gp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bosarreyes.edwars.app_gp.preferencias.Preferences;

import org.json.JSONException;
import org.json.JSONObject;

public class IngresoAdministracionPV extends AppCompatActivity {

    private EditText txUsuario, txClave;
    private Button btnIngresar;
    private RadioButton rbSesion;
    private TextView txRegistrar;

    private VolleyRP volley;
    private RequestQueue mRequest;

    ProgressDialog progreso;

    private static String IP= "https://soporteeb92.000webhostapp.com/gas/Login_GETUSUARIO.php?usuario=";

    private String user = "";
    private String contraseña = "";

    private boolean isActivateRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_administracion_pv);

        if (Preferences.obtenerPreferenceBoolean(getApplication(),Preferences.PREFERENCE_ESTADO_BUTTON)){
            Intent intent= new Intent(IngresoAdministracionPV.this,AdministracionPV.class);
            startActivity(intent);
            finish();
        }

        volley = VolleyRP.getInstance(this);
        mRequest = volley.getRequestQueue();

        txUsuario = (EditText)findViewById(R.id.edUsuario);
        txClave = (EditText)findViewById(R.id.edClave);

        btnIngresar= (Button)findViewById(R.id.btnIngresar);
        rbSesion = (RadioButton)findViewById(R.id.rbSesion);
        txRegistrar = (TextView)findViewById(R.id.txtCrearCuenta);

        isActivateRadioButton = rbSesion.isChecked(); //Desactivadp

        rbSesion.setOnClickListener(new View.OnClickListener() {
            //Activado
            @Override
            public void onClick(View view) {
                if (isActivateRadioButton){
                    rbSesion.setChecked(false);
                }
                isActivateRadioButton = rbSesion.isChecked();
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txUsuario.getText().toString().isEmpty() || txClave.getText().toString().isEmpty()){
                    Toast.makeText(getApplication(),"Debe ingresar todos los campos",Toast.LENGTH_SHORT).show();
                }else{
                    progreso= new ProgressDialog(IngresoAdministracionPV.this);
                    progreso.setMessage("Ingresando...");
                    progreso.show();
                    progreso.dismiss();
                    Verificarlogin(txUsuario.getText().toString(),txClave.getText().toString());
                }
            }
        });

      txRegistrar.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent intent = new Intent(IngresoAdministracionPV.this,MapaPuntoVenta.class);
              startActivity(intent);

          }
      });

    }

    private void Verificarlogin(String usuario, String clave) {
        user=usuario;
        contraseña=clave;
        SolicitudJSON(IP+usuario);
    }

    public void SolicitudJSON(String URL){
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                VerificarPassword(datos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplication(),"Ocurrio un error, por favor contacte al administrador",Toast.LENGTH_SHORT).show();
            }
        });

        VolleyRP.addToQueue(solicitud,mRequest,this,volley);
    }

    public void VerificarPassword(JSONObject datos){
        Preferences.savePreferenceBoolean(getApplication(),rbSesion.isChecked(),Preferences.PREFERENCE_ESTADO_BUTTON);
        try {
            String estado = datos.getString("resultado");
            if (estado.equals("CC")){
                JSONObject jsonDatos = new JSONObject(datos.getString("datos"));
                String usuario = jsonDatos.getString("usuario");
                String password = jsonDatos.getString("password");
                if (usuario.equals(user) && password.equals(contraseña)){
                    progreso.hide();
                    Toast.makeText(getApplication(),"Usuario y Contraseña Correctos",Toast.LENGTH_SHORT).show();
                    Preferences.savePreferenceString(getApplication(),usuario,Preferences.PREFERENCE_USUARIO);
                    Intent intent = new Intent(IngresoAdministracionPV.this,AdministracionPV.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    progreso.hide();
                    Toast.makeText(getApplication()," Usuario o Contraseña Incorrectos",Toast.LENGTH_SHORT).show();}
            }else {
                progreso.hide();
                Toast.makeText(getApplication(),estado,Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
        }
    }
}
