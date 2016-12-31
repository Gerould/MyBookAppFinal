package com.example.direccio.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Gerould on 28/12/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Book> list;

    public Adapter (List<Book> list) {
        //this.list es el atribut privat, list es el parametre
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView id,author,title,year,publisher,category,personal_evaluation;


        public ViewHolder(View itemView) {
            super(itemView);
           // title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            //year = (TextView) itemView.findViewById(R.id.year);

        }
    }

    @Override
    //representa la vista de cada item
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recycler, parent,false);
        return new ViewHolder(view);
    }

    @Override
    //conte la informacio de cada item del recycler
    public void onBindViewHolder(Adapter.ViewHolder holder, int position) {
        holder.author.setText(list.get(position).getAuthor());
    }

    @Override
    //representa el numero de files
    public int getItemCount() {
        return list.size();
    }
}
