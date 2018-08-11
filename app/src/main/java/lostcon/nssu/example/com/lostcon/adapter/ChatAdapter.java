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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private ArrayList<Item> list;
    private Context context;

    public ChatAdapter(Context context, ArrayList<Item> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_user_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.user_name.setText(list.get(position).getName());
        holder.chat_content.setText(list.get(position).getDistance());
        Glide.with(context).load(list.get(position).getIm_url()).into(holder.user_image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name;
        TextView chat_content;


        public ViewHolder(View itemView) {
            super(itemView);
            user_image = (ImageView)itemView.findViewById(R.id.user_image);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            chat_content = (TextView)itemView.findViewById(R.id.chat_content);
        }

    }
}
