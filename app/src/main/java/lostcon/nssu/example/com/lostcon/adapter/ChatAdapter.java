package lostcon.nssu.example.com.lostcon.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import lostcon.nssu.example.com.lostcon.R;
import lostcon.nssu.example.com.lostcon.model.Chat;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {


    private ArrayList<Chat> list;
    private Context context;

    public ChatAdapter(Context context, ArrayList<Chat> list) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.user_name.setText(list.get(position).getName());
        holder.chat_content.setText(list.get(position).getMsg());
      //  Glide.with(context).load(list.get(position).get()).into(holder.user_image);
        //보낸이가 내이름일 경우 보여주는 뷰를 다르게 한다!
        if(list.get(position).getName().equals("진수")){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT);
            params.gravity = Gravity.RIGHT;
            holder.chat_content.setLayoutParams(params);
            holder.user_image.setVisibility(View.GONE);
            holder.user_name.setVisibility(View.GONE);
            holder.user_subname.setVisibility(View.GONE);
            holder.chat_content.setGravity(Gravity.RIGHT);
            holder.chat_content.setGravity(Gravity.CENTER_VERTICAL);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image;
        TextView user_name;
        TextView user_subname;
        TextView chat_content;


        public ViewHolder(View itemView) {
            super(itemView);
            user_image = (ImageView)itemView.findViewById(R.id.user_image);
            user_subname = (TextView)itemView.findViewById(R.id.user_subname);
            user_name = (TextView)itemView.findViewById(R.id.user_name);
            chat_content = (TextView)itemView.findViewById(R.id.chat_content);
        }

    }
}
