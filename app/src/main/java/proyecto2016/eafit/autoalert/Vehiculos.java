package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class Vehiculos extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_explore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapterVehiculo(getDataSet());
        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapterVehiculo) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapterVehiculo
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent i = new Intent(Vehiculos.this, DetalleVehiculos.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre",((MyRecyclerViewAdapterVehiculo) mAdapter).getObjeto(position).getName());
                i.putExtras(bundle);
                startActivity(i);
                Log.i(LOG_TAG, " Clicked on  " + ((MyRecyclerViewAdapterVehiculo) mAdapter).getObjeto(position).getName());
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> persons = new ArrayList<>();

        persons.add(new DataObject("Carro", "25 years old", R.drawable.fondo_carro, "Vendo", "Vendo paisaje"));
        persons.add(new DataObject("Moto", "35 years old", R.drawable.fondo_moto, "Vendo", "Vendo paisaje"));
        return persons;
    }


}
