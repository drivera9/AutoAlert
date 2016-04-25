package proyecto2016.eafit.autoalert;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.jar.Attributes;

public class CardViewExplore extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private static String LOG_TAG = "CardViewActivity";

    Dialog customDialog = null;

    ArrayList<DataObject> persons = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);



        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_explore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapterExplore(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_explore);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(CardViewExplore.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        final LayoutInflater inflater = getLayoutInflater();

        final View dialoglayout = inflater.inflate(R.layout.activity_dialogo_personalizado, null);

        final EditText etAsunto = (EditText) dialoglayout.findViewById(R.id.et_EmailAsunto);
        final EditText etMensaje = (EditText) dialoglayout.findViewById(R.id.et_EmailMensaje);

        final String[] subject = new String[1];
        final String[] message = new String[1];
        Button btnEnviarMail = (Button) dialoglayout.findViewById(R.id.btnEnviarMail);
        btnEnviarMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subject[0] = etAsunto.getText().toString();
                message[0] = etMensaje.getText().toString();
                persons.add(0, new DataObject(subject[0], "25 years old", R.drawable.paisaje4, "Vendo", message[0]));
                mAdapter = new MyRecyclerViewAdapterExplore(persons);
                mRecyclerView.setAdapter(mAdapter);
            }
        });


        final com.github.clans.fab.FloatingActionButton floatingActionButton = (com.github.clans.fab.FloatingActionButton)  findViewById(R.id.menu_item);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CardViewExplore.this);
                builder.setView(dialoglayout);
                builder.show();


            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapterExplore) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapterExplore
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                int foto = (((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getPhoto());
                String foto2 = String.valueOf(foto);
                Intent i = new Intent(CardViewExplore.this, DetalleExplore.class);
                Bundle bundle = new Bundle();
                bundle.putString("nombre",((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getName());
                bundle.putString("años",((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getAge());
                bundle.putString("foto",foto2 );
                i.putExtras(bundle);
                startActivity(i);
                Log.i(LOG_TAG, " Clicked on  " + ((MyRecyclerViewAdapterExplore) mAdapter).getObjeto(position).getName());
            }
        });
    }

    private ArrayList<DataObject> getDataSet() {
        persons.add(new DataObject("Emma Wilson", "23 years old", R.drawable.paisaje3,"Descripcion","Material is the metaphor.\n\n"+

                "A material metaphor is the unifying theory of a rationalized space and a system of motion."+
                "The material is grounded in tactile reality, inspired by the study of paper and ink, yet "+
                "technologically advanced and open to imagination and magic.\n"+
                "Surfaces and edges of the material provide visual cues that are grounded in reality. The "+
                "use of familiar tactile attributes helps users quickly understand affordances. Yet the "+
                "flexibility of the material creates new affordances that supercede those in the physical "+
                "world, without breaking the rules of physics.\n" +
                "The fundamentals of light, surface, and movement are key to conveying how objects move, "+
                "interact, and exist in space and in relation to each other. Realistic lighting shows "+
                "seams, divides space, and indicates moving parts.\n\n"));
        persons.add(new DataObject("Lavery Maiss", "25 years old", R.drawable.paisaje4,"Vendo","Vendo paisaje"));
        persons.add(new DataObject("Lillie Watts", "35 years old", R.drawable.paisaje5,"Vendo","Vendo paisaje"));
        return persons;
    }



}
