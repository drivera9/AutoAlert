package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CrearVehiculo extends AppCompatActivity {
    EditText editPlaca;
    EditText editRefMarc;
    EditText editSoat;
    EditText editTecno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_vehiculo);
        FindViewById();
    }

    public void FindViewById(){
        editPlaca = (EditText) findViewById(R.id.editPlaca);
        editRefMarc = (EditText) findViewById(R.id.editRefMarc);
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

}
