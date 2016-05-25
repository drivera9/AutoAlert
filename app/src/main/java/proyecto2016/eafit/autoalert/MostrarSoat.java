package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

public class MostrarSoat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_soat);

        CalendarView calendario = (CalendarView) findViewById(R.id.calendarView);
        final TextView fecha = (TextView) findViewById(R.id.textFecha);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
               fecha.setText(dayOfMonth + "/" + month  + "/" + year);
            }
        });
    }

    public void aceptar(View v){
        TextView fecha = (TextView) findViewById(R.id.textFecha);
        Intent data = new Intent();
        data.putExtra("parametro", "fechaSoat");
        data.putExtra("valor", fecha.getText().toString());
        setResult(RESULT_OK, data);
        finish();
    }
}
