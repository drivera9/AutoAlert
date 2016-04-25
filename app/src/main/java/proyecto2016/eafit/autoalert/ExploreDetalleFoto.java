package proyecto2016.eafit.autoalert;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class ExploreDetalleFoto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_detalle_foto);

        int imagen  = Integer.parseInt((getIntent().getStringExtra("foto")));
        ImageView ampliado = (ImageView) findViewById(R.id.imageView_detalle_ampliado);
        ampliado.setId(imagen);
    }
}
