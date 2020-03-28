package com.example.colorsplit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>  {

    private Context mCtx;
    private List<Image> imageList;

    public ImageAdapter(Context mCtx, List<Image> imageList) {
        this.mCtx = mCtx;
        this.imageList = imageList;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.image_layout, null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        final Image image = imageList.get(i);

        imageViewHolder.imageView.setImageBitmap(image.getBitmap());
        if(image.getDominantC()!= null){
            imageViewHolder.label.setText(image.getDominantC());
        }
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView label;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            label = itemView.findViewById(R.id.label);
        }

    }


}
