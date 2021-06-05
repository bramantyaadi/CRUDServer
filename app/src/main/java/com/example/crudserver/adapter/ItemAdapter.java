package com.example.crudserver.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crudserver.AddItemActivity;
import com.example.crudserver.R;
import com.example.crudserver.model.Item;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    final ArrayList<Item> listItems = new ArrayList<Item>();
    private Context context;

    public ItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, final int position) {
        holder.tvName.setText(listItems.get(position).getName() );
        holder.tvBrand.setText(listItems.get(position).getBrand() );
        holder.tvPrice.setText(""+listItems.get(position).getPrice() );

        holder.cvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddItemActivity.class);

                i.putExtra("position",position);
                i.putExtra("item",listItems.get(position));
                context.startActivity(i);
            }
        });

    }

    public void setListItems(ArrayList<Item> items){
        if(items.size() > 0){
            this.listItems.clear();
        }

        this.listItems.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return listItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvBrand, tvPrice;
        CardView cvItem;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_name);
            tvBrand = itemView.findViewById(R.id.tv_brand);
            tvPrice = itemView.findViewById(R.id.tv_price);

            cvItem = itemView.findViewById(R.id.cv_item);

        }
    }
}
