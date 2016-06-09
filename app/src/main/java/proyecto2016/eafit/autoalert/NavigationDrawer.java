package proyecto2016.eafit.autoalert;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String nombre;
    String url;
    String email;
    String apellidos;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    String ip = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        ip = getIntent().getExtras().getString("ip");


        setTitle("Perfil");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        TextView nombreDrawer = (TextView) findViewById(R.id.nombre);
        TextView emailDrawer = (TextView) findViewById(R.id.email);
        ImageView imagenDrawer = (ImageView) findViewById(R.id.imageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Bundle bundle =  this.getIntent().getExtras();
        nombre = bundle.getString("nombre");
        url  = bundle.getString("url");
        email = bundle.getString("email");
        apellidos = bundle.getString("apellidos");

        toolBarLayout.setTitle( nombre);
        nombreDrawer.setText(nombre);
        emailDrawer.setText(email);

        TextView Nombres = (TextView) findViewById(R.id.textNombres);
        Nombres.setText(nombre.trim() + " " + apellidos.trim() + " - ");

        TextView Email = (TextView) findViewById(R.id.textEmail);
        Email.setText(email);

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationDrawer.this, CrearVehiculo.class);
                i.putExtra("ip" , ip);
                i.putExtra("usuario", nombre);
                startActivity(i);
            }
        });

        com.github.clans.fab.FloatingActionButton fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item_2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationDrawer.this, CrearRecordatorio.class);
                i.putExtra("ip" , ip);
                i.putExtra("usuario", nombre);
                startActivity(i);
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String url = "http://" + ip + ":80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "Vehiculos"));
        params.add(new BasicNameValuePair("sUsuario", nombre));
        String resultServer  = getHttpPost(url,params);
        System.out.println(resultServer);
        ArrayList<String> array = new ArrayList<String>();

        ArrayList<String> marcas = new ArrayList<>();
        ArrayList<String> ref = new ArrayList<>();
        ArrayList<String> soat = new ArrayList<>();
        ArrayList<String> tecno = new ArrayList<>();
        ArrayList<String> seguro = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(resultServer);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                marcas.add(json.getString("marca"));
                ref.add(json.getString("referencia"));
                array.add(json.getString("modelo"));
                soat.add(json.getString("fecha_soat"));
                tecno.add(json.getString("fecha_tecno"));
                seguro.add(json.getString("fecha_seguro"));

            }
        }catch (JSONException e ){
            e.printStackTrace();
        }

        TextView textVehiculos = (TextView) findViewById(R.id.texttNumeroVehiculos);
        String texto = "";

        for (int i = 0; i < marcas.size();i++){
            texto = texto + "El vehiculo " + marcas.get(i).trim() + " " + ref.get(i).trim() + "\n" + "Tiene el soat en la fecha " + soat.get(i).trim() +
                    "\n" + "La tecno en la fecha " + tecno.get(i).trim() + "\n" + " y el seguro en la fecha " + seguro.get(i).trim() + " " +
                    " , No te olvides!" + "\n" + "\n";
        }
        String espacio = "                                                                                                                   " +
                "                                                                                                                                          " +
                "                                                                                                                   .";

        textVehiculos.setText(texto + espacio);



    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Saliendo...")
                    .setMessage("Esta seguro que desea salir?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        String titulo = item.getTitle().toString();

        if (titulo.equals("Calendario")){
            Intent i = new Intent(NavigationDrawer.this, Calendario.class);
            i.putExtra("ip",ip);
            startActivity(i);
        }

        if (titulo.equals("Contactanos")){
            Intent i = new Intent(NavigationDrawer.this, Contactanos.class);
            i.putExtra("ip",ip);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }


        if (titulo.equals("Explorar")){
            Intent i = new Intent(NavigationDrawer.this, CardViewExplore.class);
            i.putExtra("ip",ip);
            i.putExtra("usuario",nombre);
            i.putExtra("apellidos",apellidos);
            i.putExtra("email",email);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        if (titulo.equals("Mis vehiculos")){
            Intent i = new Intent(NavigationDrawer.this, Vehiculos.class);
            i.putExtra("ip",ip);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        if (titulo.equals("Recordatorios")){
            Intent i = new Intent(NavigationDrawer.this,Recordatorios.class);
            i.putExtra("ip",ip);
            i.putExtra("usuario",nombre);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        if (titulo.equals("Ajustes")){
            Intent i = new Intent(NavigationDrawer.this, SettingsActivity.class);
            i.putExtra("ip",ip);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        if (titulo.equals("Compartir")){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Prueba la nueva aplicacion AutoAlert ! la mejor para administrar mis vehiculos !");
            startActivity(Intent.createChooser(intent, "Compartir con.."));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
