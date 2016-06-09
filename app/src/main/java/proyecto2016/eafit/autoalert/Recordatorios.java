package proyecto2016.eafit.autoalert;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
import java.util.List;

public class Recordatorios extends Activity {

    String usuario = "";

    String[] arrayLista ;

    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordatorios);

        setTitle("Recordatorios");

        ip = getIntent().getExtras().getString("ip");

        usuario = getIntent().getExtras().getString("usuario");

        String url = "http://" + ip + ":80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "Recordatorios"));
        params.add(new BasicNameValuePair("sUsuario", usuario.trim()));
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();

        final ArrayList<String> id = new ArrayList<>();
        ArrayList<String> titulo = new ArrayList<>();
        ArrayList<String> usuario = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                id.add(json.getString("id"));
                titulo.add(json.getString("titulo"));
                usuario.add(json.getString("usuario"));

            }
        }catch (JSONException e ){
            e.printStackTrace();
        }

        arrayLista = new String[titulo.size()];
        for (int i = 0;i<titulo.size();i++){
            arrayLista[i] = titulo.get(i) + "\n" + usuario.get(i);
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.lista, arrayLista);

        final ListView listView = (ListView) findViewById(R.id.lista);
        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent i = new Intent(Recordatorios.this,DetalleRecordatorios.class);
                i.putExtra("ip",ip);
                i.putExtra("posicion",id.get(position));
                startActivity(i);
            }
        });
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
