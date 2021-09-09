package com.example.semana4_cris_tarea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText espacioNumero1, espacioNumero2, espacioNumero3, espacioNumero4;
    private Button pingPongBoton, hostaSugarBoton;

    private String espacio1, espacio2, espacio3, espacio4;
    private String ipCompleta, redAtrapaNumeros;
    private boolean espacioLleno;

    private TextView dondeApareceTuIP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        espacioNumero1 = findViewById(R.id.espacio1);
        espacioNumero2 = findViewById(R.id.espacio2);
        espacioNumero3 = findViewById(R.id.espacio3);
        espacioNumero4 = findViewById(R.id.espacio4);

        pingPongBoton = findViewById(R.id.pingpong_boton);
        hostaSugarBoton = findViewById(R.id.sugarDaddy_boton);

        pingPongBoton.setOnClickListener(this);
        hostaSugarBoton.setOnClickListener(this);

        dondeApareceTuIP = findViewById(R.id.dondeApareceTuIP);

        miIP ();
    }

    private void miIP() {

        new Thread(
                ()->{
                    Socket s = null;
                    try {
                        s = new Socket("google.com", 80);
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                    String i = s.getLocalAddress().getHostAddress();
                    runOnUiThread(
                            ()->{
                                dondeApareceTuIP.setText(i);
                            }


                    );

                    try {
                        s.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String [] iph = i.split("\\.");
                    redAtrapaNumeros = iph[0] + "." + iph [1] + "." + iph [2] + ".";
                }
        ).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pingpong_boton:
                ping();

                if (espacioLleno){
                    Intent i = new Intent(this, pantallaPing.class);
                    i.putExtra( "mandarIpCompleto",ipCompleta);
                    i.putExtra("mandarEspacioLLeno", espacioLleno);
                    startActivity(i);
                }

                break;
            case R.id.sugarDaddy_boton:
                espacioLleno = false;
                    Intent i = new Intent(this, pantallaPing.class);
                    i.putExtra( "mandarRedCompleta",redAtrapaNumeros);
                    i.putExtra("mandarEspacioLLeno", espacioLleno);
                    startActivity(i);

                break;
        }
    }

    private void ping() {

        espacioLleno = true;

        espacio1 = espacioNumero1.getText().toString();
        espacio2 = espacioNumero2.getText().toString();
        espacio3 = espacioNumero3.getText().toString();
        espacio4 = espacioNumero4.getText().toString();

        ArrayList < String > IP = new ArrayList<>();
        IP.add(espacio1);
        IP.add(espacio2);
        IP.add(espacio3);
        IP.add(espacio4);

        if (espacioLleno) {
            for ( int i = 0; i < IP.size(); i++){
                if (IP.get(i)==null || IP.get(i).isEmpty()){
                    Toast.makeText(this, "es un valor vacio",Toast.LENGTH_LONG).show();
                    espacioLleno = false;
                } else {
                    int number = Integer.parseInt(IP.get(i));
                    if (number < 0 || number > 255){
                        Toast.makeText(this, "los valores de una ip solo pueden ser de 0 a 255",Toast.LENGTH_LONG).show();
                        espacioLleno = false;
                    }

                }

            }
        }

        ipCompleta = espacio1 + "." + espacio2 + "." + espacio3 + "." + espacio4;

    }
}