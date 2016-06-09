package proyecto2016.eafit.autoalert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetalleRecordatorios extends AppCompatActivity {
    String id = "";
    String fechaCalendario = "";
    String ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_recordatorios);

        setTitle("Recordatorios");

        id = getIntent().getExtras().getString("posicion");
        ip = getIntent().getExtras().getString("ip");

        String url = "http://" + ip + ":80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "DetalleRecordatorios"));
        params.add(new BasicNameValuePair("sId", id));
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();

        final ArrayList<String> id = new ArrayList<>();
        String titulo = "";
        String usuario = "";
        String fecha = "";
        String desc = "";
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                titulo = (json.getString("titulo"));
                usuario = (json.getString("usuario"));
                fecha = (json.getString("fecha"));
                desc = (json.getString("descripcion"));

            }
        }catch (JSONException e ){
            e.printStackTrace();
        }

        TextView textoFecha = (TextView) findViewById(R.id.textoFecha);
        textoFecha.setText(fecha.trim());
        EditText editTitulo = (EditText) findViewById(R.id.editTitulo);
        EditText editDesc = (EditText) findViewById(R.id.editDesc);
        TextView tituloUsuario = (TextView) findViewById(R.id.tituloUsuario);

        editTitulo.setText(titulo.trim());
        editDesc.setText(desc.trim());
        tituloUsuario.setText(usuario.trim());



    }

    public void borrar(View v){

        new AlertDialog.Builder(DetalleRecordatorios.this)
                .setTitle("Borrar")
                .setMessage("Estas seguro que deseas borrar el recordatorio?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String url = "http://" + ip + ":80/AUBorrarRecordatorio.php";

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("sId", id));

                        String resultServer = getHttpPost(url, params);
                        System.out.println(resultServer);

                        Toast.makeText(DetalleRecordatorios.this, "Se borro correctamente!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();



    }

    public void actualizar(View v){
        EditText editTitulo = (EditText) findViewById(R.id.editTitulo);
        EditText editDesc = (EditText) findViewById(R.id.editDesc);

        String url = "http://" + ip + ":80/AUActualizarRecordatorio.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sId", id));
        params.add(new BasicNameValuePair("sTitulo", editTitulo.getText().toString()));
        params.add(new BasicNameValuePair("sDesc", editDesc.getText().toString()));
        params.add(new BasicNameValuePair("sFecha", fechaCalendario));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        Toast.makeText(DetalleRecordatorios.this, "Se actualizo correctamente!", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void calendario(View v){
        Intent i = new Intent(DetalleRecordatorios.this,CalendarioRecordatorio.class);
        startActivityForResult(i,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        String parametro = data.getStringExtra("parametro");
        String valor = data.getStringExtra("valor");

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (parametro.equals("fechaRecordatorio")) {
                    TextView textoFecha = (TextView) findViewById(R.id.textoFecha);
                    textoFecha.setText(valor.trim());
                    fechaCalendario = valor.trim();
                }
            }
        }
    }

    public String getHttpPost(String url,List<NameValuePair> params) {
        StringBuilder str = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = client.execute(httpPost);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) { // Status OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    str.append(line);
                }
            } else {
                Log.e("Log", "Failed to download result..");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }
}
