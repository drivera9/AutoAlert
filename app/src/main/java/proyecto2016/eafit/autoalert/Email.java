package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class Email extends AppCompatActivity {

    String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        email = getIntent().getExtras().getString("email");
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        etEmail.setText(email.trim());
    }

    public void enviar(View v){
        EditText etEmail = (EditText) findViewById(R.id.etEmail);
        EditText etSubject = (EditText) findViewById(R.id.etSubject);
        EditText etBody = (EditText) findViewById(R.id.etBody);



        //Instanciamos un Intent del tipo ACTION_SEND
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        //Definimos la tipologia de datos del contenido dle Email en este caso text/html
        emailIntent.setType("text/html");
        // Indicamos con un Array de tipo String las direcciones de correo a las cuales
        //queremos enviar el texto
        emailIntent.putExtra(Intent.EXTRA_EMAIL, etEmail.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_CC, "AutoAlarm");
        // Definimos un titulo para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_TITLE, etSubject.getText().toString());
        // Definimos un Asunto para el Email
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, etSubject.getText().toString());
        // Obtenemos la referencia al texto y lo pasamos al Email Intent
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, etBody.getText().toString());

        try {
            //Enviamos el Correo iniciando una nueva Activity con el emailIntent.
            startActivity(Intent.createChooser(emailIntent, "Enviar E-mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Email.this, "No hay ningun cliente de correo instalado.", Toast.LENGTH_SHORT).show();
        }
    }
}
