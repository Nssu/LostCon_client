package lostcon.nssu.example.com.lostcon.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private ArrayList<Item> list;
    private Context context;

    public ItemAdapter(Context context, ArrayList<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_name.setText(list.get(position).getName());
        holder.item_distance.setText(list.get(position).getDistance());
        Glide.with(context).load(list.get(position).getIm_url()).into(holder.item_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_distance;


        public ViewHolder(View itemView) {
            super(itemView);
            item_image = (ImageView)itemView.findViewById(R.id.item_image);
            item_name = (TextView)itemView.findViewById(R.id.item_name);
            item_distance = (TextView)itemView.findViewById(R.id.item_distance);
        }

    }
}
