package com.example.appsmoviles22;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


public class MainActivity extends AppCompatActivity {
    private EditText numero1;
    private EditText numero2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void proceso(View v) throws InterruptedException, JSONException {
        EditText n1 = findViewById(R.id.numero2);
        EditText n2 = findViewById(R.id.numero1);

        redes llamar = new redes();
        llamar.Setnum(String.valueOf(n1.getText()) , String.valueOf(n2.getText()));
        llamar.start();
        TextView rptg = findViewById(R.id.rptgeneral);
        rptg.setText(llamar.getRespuesta());

        JSONArray arreglo = new JSONArray(llamar.getRespuesta().toString());
        JSONObject drpt = arreglo.getJSONObject(0);

        TextView suma = findViewById(R.id.txtSuma);
        suma.setText(drpt.getString("suma"));

        TextView resta = findViewById(R.id.txtResta);
        resta.setText(drpt.getString("resta"));

        TextView multi = findViewById(R.id.txtMulti);
        multi.setText(drpt.getString("multi"));

        TextView divi = findViewById(R.id.txtDivi);
        divi.setText(drpt.getString("divi"));



    }
    public void imprimir(String s,String r,String x,String d){
        TextView printrpt = findViewById(R.id.rptgeneral);
        printrpt.setText(s);


    }
}

class redes extends Thread {
    public String n1;
    public String n2;

    public String respuesta;
    @Override
    public void run(){
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://thedewq.000webhostapp.com/matematicas.php?");

            Map<String, Object> mapa = new LinkedHashMap<>();
            mapa.put("numero1", this.n1);
            mapa.put ("numero2", this.n2);

            for(Map.Entry<String, Object> param : mapa.entrySet()){
                url.append("&");
                url.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                url.append("=");
                url.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }

            URL link = new URL(String.valueOf(url));

            HttpURLConnection conexion = (HttpURLConnection) link.openConnection();
            conexion.setRequestMethod("GET");
            conexion.connect();

            int rptCode = conexion.getResponseCode();

            if(rptCode != 200){
                throw new RuntimeException("valiste vrga"+rptCode);
            }else{
                StringBuilder rpt = new StringBuilder();
                Scanner scanner = new Scanner(link.openStream());
                while(scanner.hasNext()){
                    rpt.append(scanner.nextLine());
                }

                scanner.close();
                this.setRespuesta(String.valueOf(rpt));
                System.out.println(this.respuesta);
            }




        } catch (IOException e) {
            respuesta = "nomms"+e.getMessage();
        }
    }


    public void Setnum(String n1, String n2){

        this.n1 = n1;
        this.n2 = n2;
    }
    public String getRespuesta(){
        while(respuesta == null){
            System.out.println("espera");
        }
        return respuesta;
    }
    public void setRespuesta(String respuesta){
        this.respuesta = respuesta;

    }


}