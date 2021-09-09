package com.example.semana4_cris_tarea;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class pantallaPing extends AppCompatActivity {

    private ListView listaDEpings;
    private Button regresar;

    private String ip, red, iph, mensaje;
    private Boolean conectado, espacioLleno;

    private ArrayList < String > pingArray = new ArrayList<>();

    private ArrayAdapter < String > adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_ping);

        listaDEpings = findViewById(R.id.listaDEpings);
        regresar = findViewById(R.id.regresar_boton);

        espacioLleno = getIntent().getBooleanExtra("mandarEspacioLLeno", false);
        ip = getIntent().getExtras().getString("mandarIpCompleto");
        red = getIntent().getExtras().getString("mandarRedCompleta");

        if (espacioLleno){
            adapter = new ArrayAdapter< String >(this, android.R.layout.simple_list_item_1,pingArray);
            isPing ();
        } else{
            adapter = new ArrayAdapter< String >(this, android.R.layout.simple_list_item_1,pingArray);
            hosts ();
        }

       regresar.setOnClickListener(
               (v)->{
                   finish();
               }
       );
    }

    private void hosts() {
        new Thread(
                ()->{
                    for (int i = 1; i < 255; i++){
                        iph = red + i;
                        try {
                            InetAddress inet = InetAddress.getByName(iph);
                            conectado = inet.isReachable(500);
                            runOnUiThread(
                                    ()->{
                                        if (conectado){
                                            pingArray.add("conectado: " + iph);
                                            adapter.notifyDataSetChanged();
                                            listaDEpings.setAdapter(adapter);
                                        }
                                    }
                            );
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).start();
    }

    private void isPing() {
        new Thread(
                ()->{
                    try {
                        InetAddress inet = InetAddress.getByName(ip);

                        for (int i = 0; i < 5; i++){
                            conectado = inet.isReachable(2000);

                            runOnUiThread(
                                    ()->{

                                        if(conectado){

                                            mensaje = "recibido";
                                            pingArray.add(mensaje);
                                            adapter.notifyDataSetChanged();
                                            listaDEpings.setAdapter(adapter);

                                        }else{

                                            mensaje = "perdido";
                                            pingArray.add(mensaje);
                                            adapter.notifyDataSetChanged();
                                            listaDEpings.setAdapter(adapter);

                                        }
                                    }
                            );
                        }

                    } catch (UnknownHostException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        ).start();
    }
}