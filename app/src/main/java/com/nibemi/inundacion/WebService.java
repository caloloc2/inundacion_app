package com.nibemi.inundacion;

        import android.content.Intent;
        import android.widget.Toast;

        import org.json.JSONArray;

        import java.io.DataOutput;
        import java.io.DataOutputStream;
        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.Scanner;

public class WebService {
    String servidor = "http://inundaciones.nibemi.com";

    public String Datos(String fecha_inicio, String fecha_final){
        String parametros = "fecha_inicio="+fecha_inicio+"&fecha_final="+fecha_final;
        HttpURLConnection conexion = null;
        String respuesta = "";

        try{
            URL url = new URL(servidor+"/php/web_service.php");
            conexion = (HttpURLConnection)url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Length", ""+Integer.toString(parametros.getBytes().length));
            conexion.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conexion.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream = new Scanner(conexion.getInputStream());
            while(inStream.hasNextLine()){
                respuesta +=(inStream.nextLine());
            }
        }catch(Exception e){
            //respuesta = e.getMessage();
            System.out.println(e.getMessage());
        }
        return respuesta.toString();
    }

    public String Alarma(){
        String parametros = "";
        HttpURLConnection conexion = null;
        String respuesta = "";

        try{
            URL url = new URL(servidor+"/php/alarma.php");
            conexion = (HttpURLConnection)url.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Length", ""+Integer.toString(parametros.getBytes().length));
            conexion.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conexion.getOutputStream());
            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream = new Scanner(conexion.getInputStream());
            while(inStream.hasNextLine()){
                respuesta +=(inStream.nextLine());
            }
        }catch(Exception e){
            //respuesta = e.getMessage();
            System.out.println(e.getMessage());
        }
        return respuesta.toString();
    }
}
