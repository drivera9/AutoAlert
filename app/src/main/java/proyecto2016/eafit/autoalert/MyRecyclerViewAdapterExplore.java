package proyecto2016.eafit.autoalert;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyRecyclerViewAdapterExplore extends RecyclerView
        .Adapter<MyRecyclerViewAdapterExplore
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;
        ImageView foto;
        TextView titulo;
        TextView detalleTitulo;


        public DataObjectHolder(View itemView) {
            super(itemView);

            label = (TextView) itemView.findViewById(R.id.person_name);
            dateTime = (TextView) itemView.findViewById(R.id.person_age);
            foto = (ImageView) itemView.findViewById(R.id.person_photo);
            titulo = (TextView) itemView.findViewById(R.id.titulo);
            detalleTitulo = (TextView) itemView.findViewById(R.id.detalleTitulo);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }



    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapterExplore(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_explore, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.label.setText(mDataset.get(position).getName());
        holder.dateTime.setText(mDataset.get(position).getAge());
        holder.foto.setBackgroundResource(mDataset.get(position).getPhoto());
        holder.titulo.setText(mDataset.get(position).getTitulo());
        holder.detalleTitulo.setText(mDataset.get(position).getDetalleTitulo());
    }


    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public DataObject getObjeto(int position) {
        return mDataset.get(position);
    }




    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}