package lostcon.nssu.example.com.lostcon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.activity.MainActivity;
import lostcon.nssu.example.com.lostcon.activity.RegistActivity;
import lostcon.nssu.example.com.lostcon.model.Item;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {


    private ArrayList<Item> list;
    private Context context;
    public static Intent intent;

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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.item_name.setText(list.get(position).getName());
        holder.item_distance.setText(list.get(position).getDistance());
        Glide.with(context).load(list.get(position).getIm_url()).into(holder.item_image);
        //범위벗어남(찾고잇음파랑/그외회색)
        if(list.get(position).getDistance().equals("범위를 벗어났습니다")){
            holder.speaker.setBackgroundResource(R.drawable.search_blue);
        }else if(list.get(position).getAlarm().equals("1")) {
            holder.speaker.setBackgroundResource(R.drawable.speaker_gray);
        } else {
            holder.speaker.setBackgroundResource(R.drawable.speaker_blue);
        }

        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(context, RegistActivity.class);
                intent.putExtra("type","edit");
                intent.putExtra("img",list.get(position).getIm_url());
                context.startActivity(intent);
            }
        });

        holder.speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //범위벗어남(찾고잇음파랑/그외회색)
                if(list.get(position).getDistance().equals("범위를 벗어났습니다")){
                    v.setBackgroundResource(R.drawable.search_blue);
                        if(!((MainActivity)context).popupWindow_search.isShowing()){
                            ((MainActivity)context).popupWindow_search
                                    .showAtLocation(((MainActivity)context).popupView_search, Gravity.CENTER, 0, 0);
                    }
                }else if(list.get(position).getAlarm().equals("1")) {
                    v.setBackgroundResource(R.drawable.speaker_gray);
                    list.get(position).setAlarm("0");
                    notifyDataSetChanged();
                }
                else {
                    v.setBackgroundResource(R.drawable.speaker_blue);
                    list.get(position).setAlarm("1");
                    notifyDataSetChanged();
                }


           }
        });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView item_image;
        TextView item_name;
        TextView item_distance;
        LinearLayout speaker;
        Button edit_button;

        public ViewHolder(View itemView) {
            super(itemView);
            speaker = (LinearLayout)itemView.findViewById(R.id.speaker);
            item_image = (ImageView)itemView.findViewById(R.id.item_image);
            item_name = (TextView)itemView.findViewById(R.id.item_name);
            item_distance = (TextView)itemView.findViewById(R.id.item_distance);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);

        }

    }
}
