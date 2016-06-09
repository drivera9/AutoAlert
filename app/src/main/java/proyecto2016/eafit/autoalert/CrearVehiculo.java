package proyecto2016.eafit.autoalert;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CrearVehiculo extends AppCompatActivity {
    EditText editPlaca;
    EditText editRef;
    EditText editMarca;
    EditText editSoat;
    EditText editTecno;
    CheckBox checkTipoC;
    CheckBox checkTipoM;
    String usuario;
    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_vehiculo);

        usuario = getIntent().getExtras().getString("usuario");
        ip = getIntent().getExtras().getString("ip");

        FindViewById();
    }

    public void FindViewById(){
        checkTipoC = (CheckBox) findViewById(R.id.checkCarro);
        checkTipoM = (CheckBox) findViewById(R.id.checkMoto);
        editPlaca = (EditText) findViewById(R.id.editPlaca);
        editRef = (EditText) findViewById(R.id.editRef);
        editMarca = (EditText) findViewById(R.id.editMarca);
        editSoat = (EditText) findViewById(R.id.editSoat);
        editTecno = (EditText) findViewById(R.id.editTecno);
    }

    public void buscarSoat(View v){
        Intent i  = new Intent(CrearVehiculo.this,MostrarSoat.class);
        startActivityForResult(i,1);
    }
    public void buscarTecno(View v){
        Intent i  = new Intent(CrearVehiculo.this,MostrarTecno.class);
        startActivityForResult(i, 1);
    }

    public void guardar(View v){
        String url = "http://" + ip + ":80/AUGuardar.php";

        String tipo = "";

        if (checkTipoM.isChecked()){
            tipo = "M";
        }else{
            if (checkTipoC.isChecked()) {
                tipo = "C";
            }
        }

        if (tipo.equals("")){
            Toast.makeText(CrearVehiculo.this, "Debes seleccionar un tipo de vehiculo!", Toast.LENGTH_SHORT).show();
        }else {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("sTipo", tipo));
            params.add(new BasicNameValuePair("sUsuario", usuario.trim()));
            params.add(new BasicNameValuePair("sPlaca", editPlaca.getText().toString().trim()));
            params.add(new BasicNameValuePair("sMarca", editMarca.getText().toString().trim()));
            params.add(new BasicNameValuePair("sReferencia", editRef.getText().toString().trim()));
            params.add(new BasicNameValuePair("sFechaSoat", editSoat.getText().toString().trim()));
            params.add(new BasicNameValuePair("sFechaTecno", editTecno.getText().toString().trim()));
            params.add(new BasicNameValuePair("sFechaSeguro", "1"));

            String resultServer = getHttpPost(url, params);
            System.out.println(resultServer);

            Toast.makeText(CrearVehiculo.this, "Se guardo correctamente!", Toast.LENGTH_SHORT).show();
            finish();
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        String parametro = data.getStringExtra("parametro");
        String valor = data.getStringExtra("valor");

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (parametro.equals("fechaSoat")) {
                    editSoat.setText(valor.trim());
                }else{
                    if (parametro.equals("fechaTecno")) {
                        editTecno.setText(valor.trim());
                    }
                }
            }
        }
    }

}
