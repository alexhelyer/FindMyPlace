package com.example.alejandro.findmyplace.saved_places;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alejandro.findmyplace.R;

import java.util.List;

/**
 * Created by diego on 11/04/18.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;
    private OnItemClickListener listener;

    public CategoryAdapter(List<Category> categories, OnItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void OnItemClick(Category category,int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView categpryImage;
        public TextView categoryText;


        public ViewHolder(View itemView){
            super(itemView);
            this.categpryImage = (ImageView) itemView.findViewById(R.id.category_image);
            this.categoryText = (TextView) itemView.findViewById(R.id.category_text);
        }

        public void bind(final Category myCategory, final OnItemClickListener listener){

            categoryText.setText(myCategory.getTitle());
            Glide.with(itemView.getContext())
                    .load("")
                    .error(myCategory.getImage())
                    .into(categpryImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(myCategory,getAdapterPosition());
                }
            });
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category,parent,false); ;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(categories.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
