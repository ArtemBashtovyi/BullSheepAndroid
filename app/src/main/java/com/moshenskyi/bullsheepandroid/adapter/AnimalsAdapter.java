package com.moshenskyi.bullsheepandroid.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.moshenskyi.bullsheepandroid.R;

import java.util.List;

public class AnimalsAdapter extends RecyclerView.Adapter<AnimalsAdapter.AnimalsViewHolder> {

    private List<Integer> imageIds;

    public AnimalsAdapter(List<Integer> imageIds) {
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public AnimalsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(viewGroup.getContext(), R.layout.item_animal, null);
        return new AnimalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalsViewHolder animalsViewHolder, int i) {
        final int imageId = imageIds.get(i);
        animalsViewHolder.image.setImageResource(imageId);
        if (i == 0) {
            animalsViewHolder.cardView.setVisibility(View.GONE);
            Log.i("SSS", "" + i);
        } else {
            animalsViewHolder.cardView.setVisibility(View.VISIBLE);
            Log.i("SSS", "" + i);
        }
    }

    @Override
    public int getItemCount() {
        return imageIds != null ? imageIds.size() : 0;
    }

    class AnimalsViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        CardView cardView;

        public AnimalsViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = itemView.findViewById(R.id.item_animal_image);
            this.cardView = itemView.findViewById(R.id.lock_animal);
        }
    }
}
