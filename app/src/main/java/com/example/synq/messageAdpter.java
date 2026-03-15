package com.example.synq;

import static com.example.synq.chat_Win.reciverIImg;
import static com.example.synq.chat_Win.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class messageAdpter extends RecyclerView.Adapter{
    Context context;
    ArrayList<msgModelclass> messagesAdpterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECIVE = 2;

    public messageAdpter(ArrayList<msgModelclass> messagesAdpterArrayList, Context context) {
        this.messagesAdpterArrayList = messagesAdpterArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType ==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new senderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout,parent,false);
            return new reciverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if(holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtext.setText(messages.getMessage());
            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else{
            reciverViewHolder viewHolder = (reciverViewHolder) holder;
            viewHolder.msgtext.setText(messages.getMessage());
            Picasso.get().load(reciverIImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdpterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        msgModelclass messages = messagesAdpterArrayList.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid())){
            return ITEM_SEND;
        }
        else{
            return ITEM_RECIVE;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtext;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtext = itemView.findViewById(R.id.msgsendertyp);
        }
    }
    class reciverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtext;
        public reciverViewHolder(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.pro);
            msgtext = itemView.findViewById(R.id.recivertextset);
        }
    }
}
