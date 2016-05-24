package proyecto2016.eafit.autoalert;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.Toast;

public class MostrarSoat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_soat);

        CalendarView calendario = (CalendarView) findViewById(R.id.calendarView);

        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getBaseContext(),
                        "Selected Date is\n\n" + dayOfMonth + " / " + month
                                + " / " + year, Toast.LENGTH_LONG).show();
            }
        });
    }
}
