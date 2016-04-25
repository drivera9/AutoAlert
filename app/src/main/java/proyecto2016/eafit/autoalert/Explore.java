package proyecto2016.eafit.autoalert;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class Explore extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int foto = (((MyRecyclerViewAdapter) mAdapter).getObjeto(position).getPhoto());
                String foto2 = String.valueOf(foto);
                Intent i = new Intent(Explore.this, DetalleExplore.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre",((MyRecyclerViewAdapter) mAdapter).getObjeto(position).getName());
                bundle.putString("años",((MyRecyclerViewAdapter) mAdapter).getObjeto(position).getAge());
                bundle.putString("foto",foto2 );
                i.putExtras(bundle);
                startActivity(i);
                Log.i(LOG_TAG, " Clicked on  " + ((MyRecyclerViewAdapter) mAdapter).getObjeto(position).getName());
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList<DataObject> persons = new ArrayList<>();
        persons.add(new DataObject("Emma Wilson", "23 years old", R.drawable.paisaje,"Vendo","Vendo paisaje"));
        persons.add(new DataObject("Lavery Maiss", "25 years old", R.drawable.abc_btn_borderless_material,"Vendo","Vendo paisaje"));
        persons.add(new DataObject("Lillie Watts", "35 years old", R.drawable.abc_btn_check_material,"Vendo","Vendo paisaje"));
        return persons;
    }



}
