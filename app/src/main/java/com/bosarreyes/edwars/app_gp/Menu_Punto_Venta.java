package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu_Punto_Venta extends AppCompatActivity {

    private Button crearPV, administrarPV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__punto__venta);

        crearPV = (Button)findViewById(R.id.btnCrearPuntoVenta);
        administrarPV = (Button)findViewById(R.id.btnAdministrarPuntoVenta);

        crearPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Punto_Venta.this, MapaPuntoVenta.class);
                startActivity(intent);
            }
        });

        administrarPV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_Punto_Venta.this, IngresoAdministracionPV.class);
                startActivity(intent);
            }
        });

    }
}
