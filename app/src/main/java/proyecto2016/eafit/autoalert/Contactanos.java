package proyecto2016.eafit.autoalert;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class Contactanos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactanos);

        setTitle("Contactanos!");

        Drawable originalDrawable = getResources().getDrawable(R.drawable.david);
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        ImageView imageView = (ImageView) findViewById(R.id.imagenDavid);

        imageView.setImageDrawable(roundedDrawable);

        Drawable originalDrawable2 = getResources().getDrawable(R.drawable.miguel);
        Bitmap originalBitmap2 = ((BitmapDrawable) originalDrawable2).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable2 =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap2);

        //asignamos el CornerRadius
        roundedDrawable2.setCornerRadius(originalBitmap2.getHeight());

        ImageView imageView2 = (ImageView) findViewById(R.id.imagenMiguel);

        imageView2.setImageDrawable(roundedDrawable2);
    }
}
