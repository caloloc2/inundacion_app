package com.nibemi.inundacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView usuario, psw;
    Button inicio;
    WebService web = new WebService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = (TextView)findViewById(R.id.correo);
        psw = (TextView)findViewById(R.id.password);
        inicio = (Button)findViewById(R.id.inicio);

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String correo = usuario.getText().toString();
                final String clave = psw.getText().toString();

                Thread tr = new Thread(){
                    @Override
                    public void run() {

                        final String datos = web.Inicio(correo, clave);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject elementos = new JSONObject(datos);
                                    String correo = elementos.getString("correo");

                                    Intent intent = new Intent(getApplicationContext(), Principal.class);
                                    startActivity(intent);
                                    finish();

                                }catch(Exception e){
                                    System.out.println(e.getMessage());
                                }
                            }
                        });
                    }
                };
                tr.start();
            }
        });
    }
}
