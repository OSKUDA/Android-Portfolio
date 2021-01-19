package io.github.oskuda.mydoes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DoesAdapter extends RecyclerView.Adapter<DoesAdapter.MyViewHolder>{

    Context context;
    ArrayList<MyDoes> myDoes;

    public DoesAdapter(Context c, ArrayList<MyDoes> p) {
        context = c;
        myDoes = p;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does, viewGroup, false));
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, int i) {
        myViewHolder.titleDoes.setText(myDoes.get(i).getTitleDoes());
        myViewHolder.descDoes.setText(myDoes.get(i).getDescDoes());
        myViewHolder.dateDoes.setText(myDoes.get(i).getDateDoes());

        final String getTitleDoes = myDoes.get(i).getTitleDoes();
        final String getDescDoes = myDoes.get(i).getDescDoes();
        final String getDateDoes = myDoes.get(i).getDateDoes();
        final String getKeyDoes = myDoes.get(i).getKeyDoes();

        myViewHolder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context,EditTaskDetails.class);
            intent.putExtra("titleDoes",getTitleDoes);
            intent.putExtra("descDoes",getDescDoes);
            intent.putExtra("dateDoes",getDateDoes);
            intent.putExtra("keyDoes",getKeyDoes);
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return myDoes.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleDoes, descDoes, dateDoes;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleDoes = (TextView) itemView.findViewById(R.id.titleDoes);
            descDoes = (TextView) itemView.findViewById(R.id.descDoes);
            dateDoes = (TextView) itemView.findViewById(R.id.dateDoes);
        }
    }
}