package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleExplore extends AppCompatActivity {
    public final static String ID = "ID";
    public String mContact;
    public String mAge;
    public int foto ;
    ImageView imagen;
    String Email;
    String desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_explore);

        mContact = getIntent().getStringExtra("nombre");
        Email = getIntent().getStringExtra("email");
        desc = getIntent().getStringExtra("desc");
        mAge = "";
        foto = Integer.parseInt(getIntent().getStringExtra("foto"));
        TextView nombre = (TextView) findViewById(R.id.DETAILS_name);
        nombre.setText(mContact);

        TextView años = (TextView) findViewById(R.id.DETAILS_phone);
        años.setText(mAge);

        TextView email = (TextView) findViewById(R.id.DETAILS_email);
        email.setText(Email.trim());

        imagen= (ImageView) findViewById(R.id.imagen_detalle);
        imagen.setImageResource(foto);

        TextView texto = (TextView) findViewById(R.id.texto);

        texto.setText(desc + "\n" + "Puedes comunicarte con este email! " + Email);


    }

    public void email(View v){
        Intent i = new Intent(DetalleExplore.this,Email.class);
        i.putExtra("email",Email);
        startActivity(i);
    }

    public void aumentar (View v){
        Intent i = new Intent(DetalleExplore.this, ExploreDetalleFoto.class);
        Bundle bundle = new Bundle();
        bundle.putString("foto", String.valueOf(imagen.getId()));
        i.putExtras(bundle);
        startActivity(i);
    }
}

