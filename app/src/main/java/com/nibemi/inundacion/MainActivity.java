package com.nibemi.inundacion;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView usuario, psw;
    Button inicio;

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
                String correo = usuario.getText().toString();
                String clave = psw.getText().toString();

                if (("vinicioismael1694@gmail.com".equals(correo))&&("12345".equals(clave))){
                    Intent intent = new Intent(getApplicationContext(), Principal.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Credenciales incorrectas.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
