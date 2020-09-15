package com.nibemi.inundacion;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Principal extends AppCompatActivity {
    WebService web = new WebService();

    static TextView fecha_inicio, fecha_final;
    static int seleccion = 0;

    Button buscador;
    ListView listado_items;
    JSONArray elementos;

    String[] valores_lista = {};
    String[] valores2_lista = {};
    String[] fechas_lista = {};
    String[] horas_lista = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        fecha_inicio = (TextView)findViewById(R.id.fecha_inicio);
        fecha_final = (TextView)findViewById(R.id.fecha_final);
        buscador = (Button)findViewById(R.id.buscador);
        listado_items = (ListView)findViewById(R.id.lista_datos);

        final Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        fecha_inicio.setText(dateFormat.format(date));
        fecha_final.setText(dateFormat.format(date));

        buscador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Buscador();
            }
        });

        fecha_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccion = 1;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        fecha_final.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleccion = 2;
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return valores_lista.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.items_datos, null);
            TextView valor = (TextView)convertView.findViewById(R.id.valor);
            TextView valor_2 = (TextView)convertView.findViewById(R.id.valor_2);
            TextView fecha_hora = (TextView)convertView.findViewById(R.id.fecha_hora);

            valor.setText("Nivel de Rio: "+valores_lista[position]);
            valor_2.setText("Nivel de lluvia: "+valores2_lista[position]);
            fecha_hora.setText(fechas_lista[position]+" a las "+horas_lista[position]);

            return convertView;
        }
    }

    public static String[] add(String[] originalArray, String newItem){
        int currentSize = originalArray.length;
        int newSize = currentSize + 1;
        String[] tempArray = new String[ newSize ];
        for (int i=0; i < currentSize; i++)
        {
            tempArray[i] = originalArray [i];
        }
        tempArray[newSize- 1] = newItem;
        return tempArray;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.abrir_sitio_web: Abrir_Web();
                break;
            case R.id.salir: finish();
                break;
            case R.id.boton_alarma: Alarma();
                break;
            case R.id.refrescar: Buscador();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Abrir_Web(){
        Uri uriUrl = Uri.parse("http://inundaciones.nibemi.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(intent);
    }

    public void Alarma(){
        Thread tr = new Thread(){
            @Override
            public void run() {

                final String respuesta = web.Alarma();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Toast.makeText(getApplicationContext(), "Estado de la alarma modificado.",Toast.LENGTH_SHORT).show();
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }

                    }
                });
            }
        };
        tr.start();
    }

    public void Buscador(){
        final String fi = fecha_inicio.getText().toString();
        final String ff = fecha_final.getText().toString();
        listado_items.setAdapter(null);

        valores_lista =  new String[0];
        valores2_lista =  new String[0];
        fechas_lista =  new String[0];
        horas_lista =  new String[0];

        Thread tr = new Thread(){
            @Override
            public void run() {

                final String datos = web.Datos(fi, ff);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            elementos = new JSONArray(datos);

                            if (elementos.length()>0) {
                                for (int i = 0; i < elementos.length(); i++) {
                                    JSONObject jsonobject = elementos.getJSONObject(i);
                                    String valor = jsonobject.getString("valor");
                                    String valor2 = jsonobject.getString("valor_2");
                                    String fecha = jsonobject.getString("fecha");
                                    String hora = jsonobject.getString("hora");

                                    valores_lista = add(valores_lista, valor);
                                    valores2_lista = add(valores2_lista, valor2);
                                    fechas_lista = add(fechas_lista, fecha);
                                    horas_lista = add(horas_lista, hora);
                                }

                                CustomAdapter custom = new CustomAdapter();
                                listado_items.setAdapter(custom);
                            }else{
                                Toast.makeText(getApplicationContext(), "No existen valores registrados en el sistema.",Toast.LENGTH_SHORT).show();
                            }

                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }

                    }
                });
            }
        };
        tr.start();
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String fecha = year+"-"+(month+1)+"-"+day;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try{
                Date date = dateFormat.parse(fecha);
                if (seleccion==1){
                    fecha_inicio.setText(dateFormat.format(date));
                }else if (seleccion==2){
                    fecha_final.setText(dateFormat.format(date));
                }
            }catch(Exception e){

            }
        }
    }
}
