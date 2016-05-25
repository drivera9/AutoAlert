package proyecto2016.eafit.autoalert;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

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
        Nombres.setText(nombre + " " + apellidos);

        TextView Email = (TextView) findViewById(R.id.textEmail);
        Email.setText(email);

        com.github.clans.fab.FloatingActionButton fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.menu_item);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NavigationDrawer.this, CrearVehiculo.class);
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

        String url = "http://10.0.2.2:80/AUConsultar.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sParametro", "Perfil"));
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
            startActivity(i);
        }

        if (titulo.equals("Perfil")){
            Intent i = new Intent(NavigationDrawer.this, Transicion.class);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }


        if (titulo.equals("Explorar")){
            Intent i = new Intent(NavigationDrawer.this, CardViewExplore.class);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        if (titulo.equals("Mis vehiculos")){
            Intent i = new Intent(NavigationDrawer.this, Vehiculos.class);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }

        /*if (titulo.equals("Ajustes")){
            Intent i = new Intent(NavigationDrawer.this, SettingsActivity.class);
            startActivity(i);
            overridePendingTransition(R.transition.left_in, R.transition.left_out);
        }*/


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
