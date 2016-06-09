package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class DetalleVehiculos extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    String nombre = "";
    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_vehiculos);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ip = getIntent().getExtras().getString("ip");

        setTitle("Mis vehiculos");

        nombre = getIntent().getStringExtra("nombre");



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapterVehiculo(getDataSetCarro());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapterVehiculo) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapterVehiculo
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> persons = new ArrayList<>();
        persons.add(new DataObject("Emma Wilson", "23 years old", R.drawable.paisaje3,"Descripcion","Material is the metaphor.\n\n"+

                "A material metaphor is the unifying theory of a rationalized space and a system of motion."+
                "The material is grounded in tactile reality, inspired by the study of paper and ink, yet "+
                "technologically advanced and open to imagination and magic.\n"+
                "Surfaces and edges of the material provide visual cues that are grounded in reality. The "+
                "use of familiar tactile attributes helps users quickly understand affordances. Yet the "+
                "flexibility of the material creates new affordances that supercede those in the physical "+
                "world, without breaking the rules of physics.\n" +
                "The fundamentals of light, surface, and movement are key to conveying how objects move, "+
                "interact, and exist in space and in relation to each other. Realistic lighting shows "+
                "seams, divides space, and indicates moving parts.\n\n"));
        persons.add(new DataObject("Lavery Maiss", "25 years old", R.drawable.paisaje4, "Vendo", "Vendo paisaje"));
        persons.add(new DataObject("Lillie Watts", "35 years old", R.drawable.paisaje5, "Vendo", "Vendo paisaje"));
        return persons;
    }

    private ArrayList<DataObject> getDataSetCarro() {
        ArrayList<DataObject> persons = new ArrayList<>();

        String url = "http://" + ip + ":80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", nombre));
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                array.add(json.getString("marca"));
                array.add(json.getString("referencia"));
                array.add(json.getString("modelo"));
                array.add(json.getString("fecha_soat"));
                array.add(json.getString("fecha_tecno"));
                array.add(json.getString("fecha_seguro"));

            }
        }catch (JSONException e ){
            e.printStackTrace();
        }


        if (array.size()>0) {

            for (int i = 0; i < array.size() / 6; i++) {
                persons.add(new DataObject(array.get(i * 6).trim()  + " " + array.get((i * 6) + 1).trim(), "23 years old", R.drawable.paisaje3, "Descripcion", ""));
            }
        }else{
            persons.add(new DataObject("No hay vehiculos!", "23 years old", R.drawable.paisaje3, "Descripcion", ""));
        }
        return persons;
    }

    private ArrayList<DataObject> getDataSetMoto() {
        ArrayList<DataObject> persons = new ArrayList<>();
        persons.add(new DataObject("Moto1", "23 years old", R.drawable.paisaje3,"Descripcion","Material is the metaphor.\n\n"+

                "A material metaphor is the unifying theory of a rationalized space and a system of motion."+
                "The material is grounded in tactile reality, inspired by the study of paper and ink, yet "+
                "technologically advanced and open to imagination and magic.\n"+
                "Surfaces and edges of the material provide visual cues that are grounded in reality. The "+
                "use of familiar tactile attributes helps users quickly understand affordances. Yet the "+
                "flexibility of the material creates new affordances that supercede those in the physical "+
                "world, without breaking the rules of physics.\n" +
                "The fundamentals of light, surface, and movement are key to conveying how objects move, "+
                "interact, and exist in space and in relation to each other. Realistic lighting shows "+
                "seams, divides space, and indicates moving parts.\n\n"));
        persons.add(new DataObject("Moto2", "25 years old", R.drawable.paisaje4, "Vendo", "Vendo paisaje"));
        persons.add(new DataObject("Moto3", "35 years old", R.drawable.paisaje5, "Vendo", "Vendo paisaje"));
        return persons;
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
