package proyecto2016.eafit.autoalert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Transicion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transicion);
    }

    public void back(View button)
    {
        super.onBackPressed();
    }
}
