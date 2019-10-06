package com.bosarreyes.edwars.app_gp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.bosarreyes.edwars.app_gp.adaptadores.AdaptadorProductos;
import com.bosarreyes.edwars.app_gp.entidades.Producto;

import java.util.ArrayList;

public class CatalogoProductos extends AppCompatActivity {

    ArrayList<Producto> listaProductos;
    RecyclerView recyclerProductos;
    Button realizarPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo_productos);

        listaProductos = new ArrayList<Producto>();
        realizarPedido = (Button)findViewById(R.id.btnRealizarPedido);
        recyclerProductos = (RecyclerView)findViewById(R.id.recyclerCatalogo);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(this));

        realizarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogoProductos.this, CrearCliente.class);
                startActivity(intent);
            }
        });

        llenarPersonajes();

        AdaptadorProductos adapter = new AdaptadorProductos(listaProductos);
        recyclerProductos.setAdapter(adapter);

    }

    private void llenarPersonajes() {
        listaProductos.add(new Producto("20 Libras","Digas y LLave","Q.118.00",R.drawable.veinte));
        listaProductos.add(new Producto("25 Libras","Tropigas y Digas","Q.128.00",R.drawable.veinticinco));
        listaProductos.add(new Producto("35 Libras","Tropigas, Digas y Shellane","Q.180.00",R.drawable.treintaycinco));
        listaProductos.add(new Producto("40 Libras","Digas y LLave","Q.210.00",R.drawable.cuarenta));
        listaProductos.add(new Producto("60 Libras","Digas y LLave","Q.280.00",R.drawable.sesenta));
        listaProductos.add(new Producto("100 Libras","LLave y Shellane","Q.512.00",R.drawable.cien));
    }

}
