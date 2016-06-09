package proyecto2016.eafit.autoalert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

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
import java.util.jar.Attributes;

public class CardViewExplore extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";

    Dialog customDialog = null;
    String usuario;
    String apellidos;
    String email;
    String desc;
    ArrayList<DataObject> persons = new ArrayList<>();
    String ip = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        setTitle("Explorar");
        ip = getIntent().getExtras().getString("ip");
        usuario = getIntent().getExtras().getString("usuario");
        apellidos = getIntent().getExtras().getString("apellidos");
        email = getIntent().getExtras().getString("email");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_explore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String url = "http://" + ip + ":80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "Muro"));
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();

        ArrayList<String> usuarios = new ArrayList<>();
        ArrayList<String> titulos = new ArrayList<>();
        ArrayList<String> contenidos = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                usuarios.add(json.getString("usuario"));
                titulos.add(json.getString("titulo"));
                contenidos.add(json.getString("contenido"));

            }
        }catch (JSONException e ){
            e.printStackTrace();
        }

        for (int i = 0;i<usuarios.size();i++){
            persons.add(new DataObject(usuarios.get(i).trim() , "", R.drawable.paisaje5,titulos.get(i).trim(),
                    contenidos.get(i).trim()));
        }


        mAdapter = new MyRecyclerViewAdapterExplore(persons);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_explore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CardViewExplore.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final LayoutInflater inflater = getLayoutInflater();

        final View dialoglayout = inflater.inflate(R.layout.activity_dialogo_personalizado, null);

        final EditText etAsunto = (EditText) dialoglayout.findViewById(R.id.et_EmailAsunto);
        final EditText etMensaje = (EditText) dialoglayout.findViewById(R.id.et_EmailMensaje);

        final String[] subject = new String[1];
        final String[] message = new String[1];
        Button btnEnviarMail = (Button) dialoglayout.findViewById(R.id.btnEnviarMail);
        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subject[0] = etAsunto.getText().toString();
                desc = subject[0];
                message[0] = etMensaje.getText().toString();
                persons.add(0, new DataObject(subject[0], "", R.drawable.paisaje4, "", message[0]));
                mAdapter = new MyRecyclerViewAdapterExplore(persons);
                mRecyclerView.setAdapter(mAdapter);

                String url = "http://" + ip + ":80/AUGuardarMuro.php";

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("sUsuario", usuario.trim()));
                params.add(new BasicNameValuePair("sTitulo", subject[0]));
                params.add(new BasicNameValuePair("sContenido", message[0]));

                String resultServer = getHttpPost(url, params);
                System.out.println(resultServer);

                Toast.makeText(CardViewExplore.this, "Se guardo correctamente!", Toast.LENGTH_SHORT).show();

                //finish();
            }
        });


        final com.github.clans.fab.FloatingActionButton floatingActionButton = (com.github.clans.fab.FloatingActionButton)  findViewById(R.id.menu_item);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CardViewExplore.this);
                builder.setView(dialoglayout);
                builder.show();


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapterExplore) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapterExplore
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int foto = (((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getPhoto());
                String foto2 = String.valueOf(foto);
                Intent i = new Intent(CardViewExplore.this, DetalleExplore.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre",((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getName());
                bundle.putString("años",((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getAge());
                bundle.putString("foto",foto2 );
                bundle.putString("email",email );
                bundle.putString("desc",((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getDetalleTitulo() );
                i.putExtras(bundle);
                startActivity(i);
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
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
        persons.add(new DataObject("Lavery Maiss", "25 years old", R.drawable.paisaje4,"Vendo","Vendo paisaje"));
        persons.add(new DataObject("Lillie Watts", "35 years old", R.drawable.paisaje5,"Vendo","Vendo paisaje"));
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
