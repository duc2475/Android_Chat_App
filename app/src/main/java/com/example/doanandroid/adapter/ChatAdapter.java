package com.example.doanandroid.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanandroid.databinding.ItemContainerReceivedMessageBinding;
import com.example.doanandroid.databinding.ItemContainerSentMessageBinding;

import java.util.List;

import ObjectClass.ChatMsg;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMsg> chatMsgs;
    private Bitmap receiverProfileImg;
    private final String senderId;

    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public void setReceiverProfileImg(Bitmap bitmap){
        receiverProfileImg = bitmap;
    }


    public ChatAdapter(List<ChatMsg> chatMsgs, Bitmap receiverProfileImg, String senderId) {
        this.chatMsgs = chatMsgs;
        this.receiverProfileImg = receiverProfileImg;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT){
            return new SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
       else {
           return new ReceivedMessageViewHolder(
             ItemContainerReceivedMessageBinding.inflate(
                     LayoutInflater.from(parent.getContext()),
                     parent,
                     false
             )
           );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TYPE_SENT){
            ((SentMessageViewHolder) holder).setData(chatMsgs.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMsgs.get(position),receiverProfileImg);
        }
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    @Override
    public int getItemViewType(int position){
        if(chatMsgs.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder{
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding){
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(ChatMsg chatMsg){
            binding.txtMsg.setText(chatMsg.message);
            binding.txtDate.setText(chatMsg.datetime);
        }
    }
    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

        private final ItemContainerReceivedMessageBinding binding;

        ReceivedMessageViewHolder(ItemContainerReceivedMessageBinding itemContainerReceivedMessageBinding){
            super(itemContainerReceivedMessageBinding.getRoot());
            binding = itemContainerReceivedMessageBinding;
        }

        void setData(ChatMsg chatMsg, Bitmap receiverProfileImg){
            binding.txtMsg.setText(chatMsg.message);
            binding.txtDate.setText(chatMsg.datetime);
            if(receiverProfileImg != null){
                binding.imgUser.setImageBitmap(receiverProfileImg);
            }

        }

    }
}
