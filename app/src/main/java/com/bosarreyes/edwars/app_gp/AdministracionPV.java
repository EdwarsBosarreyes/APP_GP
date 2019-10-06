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

import com.bosarreyes.edwars.app_gp.preferencias.Preferences;

public class AdministracionPV extends AppCompatActivity {

    private Button pedidosAsignados, btnEditar;
    String usuario;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cerrar_sesion,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.cerrarSesion){
            Preferences.savePreferenceBoolean(AdministracionPV.this,false,Preferences.PREFERENCE_ESTADO_BUTTON);
            Intent intent = new Intent(AdministracionPV.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administracion_pv);
        usuario = Preferences.obtenerPreferenceString(getApplication(),Preferences.PREFERENCE_USUARIO);

        pedidosAsignados = (Button)findViewById(R.id.btnPedidos);
        pedidosAsignados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdministracionPV.this,PedidosAsignados.class);
                startActivity(intent);
            }
        });

        btnEditar = (Button)findViewById(R.id.btnEditar);
         btnEditar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(AdministracionPV.this,EditarPuntoVenta.class);
                 Bundle myBundle = new Bundle();
                 myBundle.getString("usuario",usuario);
                 intent.putExtras(myBundle);
                 startActivity(intent);
             }
         });


    }
}
