package com.example.doanandroid.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doanandroid.databinding.ItemContainerRecentConversionBinding;
import com.example.doanandroid.listener.ConversionListener;

import java.util.List;

import ObjectClass.ChatMsg;
import ObjectClass.User;

public class ConversionAdapter extends RecyclerView.Adapter<ConversionAdapter.ConversionViewHolder>{

    public final List<ChatMsg> chatMsgs;
    private final ConversionListener conversionListener;


    public ConversionAdapter(List<ChatMsg> chatMsgs, ConversionListener conversionListener) {
        this.chatMsgs = chatMsgs;
        this.conversionListener = conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversionViewHolder(
                ItemContainerRecentConversionBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatMsgs.get(position));
    }

    @Override
    public int getItemCount() {
        return chatMsgs.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{
        ItemContainerRecentConversionBinding binding;
        ConversionViewHolder(ItemContainerRecentConversionBinding itemContainerRecentConversionBinding){
            super(itemContainerRecentConversionBinding.getRoot());
            binding = itemContainerRecentConversionBinding;
        }
        void setData(ChatMsg chatMsg){
            binding.imgProfileuser.setImageBitmap(getConversionImg(chatMsg.conversionImg));
            binding.txtName.setText(chatMsg.conversionName);
            binding.txtRecentMsg.setText(chatMsg.message);
            binding.getRoot().setOnClickListener(view -> {
                User user = new User();
                user.id = chatMsg.conversionId;
                user.name = chatMsg.conversionName;
                user.image = chatMsg.conversionImg;
                conversionListener.onConversionClick(user);
            });
        }
    }

    private Bitmap getConversionImg (String encodeImg){
        byte[] bytes = Base64.decode(encodeImg, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0,bytes.length);
    }
}
