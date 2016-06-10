package proyecto2016.eafit.autoalert;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import android.widget.CalendarView;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearRecordatorio extends AppCompatActivity {
    EditText editTitulo;
    EditText editDesc;
    CalendarView calendarRecordatorio;
    CheckBox checkDia;
    CheckBox checkHora;
    String fecha = "";
    String usuario;
    String date;
    String ip = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_recordatorio);

        FindViewById();

        usuario = getIntent().getExtras().getString("usuario");

        ip = getIntent().getExtras().getString("ip");


        if (checkHora.isChecked()){
            checkDia.setChecked(false);
        }else{
            if (checkDia.isChecked()){
                checkHora.setChecked(false);
            }
        }


    }

    public void FindViewById(){
        editTitulo = (EditText) findViewById(R.id.editTitulo);
        editDesc = (EditText) findViewById(R.id.editDesc);
        checkDia = (CheckBox) findViewById(R.id.checkTodoElDia);
        checkHora = (CheckBox) findViewById(R.id.checkHora);
    }

    public void calendario(View v){
        Intent i = new Intent(CrearRecordatorio.this,CalendarioRecordatorio.class);
        startActivityForResult(i,1);
    }

    public void cancelar(View v){
        finish();
    }

    public void guardar(View v){

        String checkDiaString = "";
        String checkHoraString = "";

        if (checkDia.isChecked()){
            checkDiaString = "S";
            checkHoraString = "N";
        }else{
            if (checkHora.isChecked()){
                checkHoraString = "S";
                checkDiaString = "N";
            }
        }

        String url = "http://" + ip + ":80/AUGuardarRecordatorio.php";

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sUsuario", usuario));
        params.add(new BasicNameValuePair("sTitulo", editTitulo.getText().toString()));
        params.add(new BasicNameValuePair("sDesc", editDesc.getText().toString()));
        params.add(new BasicNameValuePair("sFecha",fecha));
        params.add(new BasicNameValuePair("sDia", checkDiaString));
        params.add(new BasicNameValuePair("sHora", checkHoraString));

        String resultServer = getHttpPost(url, params);
        System.out.println(resultServer);

        Intent notificationIntent = new Intent(getApplicationContext(), CrearRecordatorio.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                1, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = getApplicationContext().getResources();
        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setContentIntent(contentIntent)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.icono))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("AutoAlert")
                .setContentText("Recuerda tus fechas, no las dejes pasar !");
        Notification n = builder.build();

        nm.notify(1, n);

        Toast.makeText(CrearRecordatorio.this, "Se guardo correctamente!", Toast.LENGTH_SHORT).show();
        finish();

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
                if (parametro.equals("fechaRecordatorio")) {
                    String valorDate = data.getStringExtra("valorDate");
                    TextView textoFecha = (TextView) findViewById(R.id.textoFecha);
                    textoFecha.setText(valor.trim());
                    fecha = valor.trim();
                    date = valorDate.trim();
                }
            }
        }
    }
}
