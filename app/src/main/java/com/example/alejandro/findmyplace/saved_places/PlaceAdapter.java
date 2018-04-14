package com.example.alejandro.findmyplace.saved_places;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.example.alejandro.findmyplace.Place;
import com.example.alejandro.findmyplace.R;
import com.google.android.gms.maps.model.LatLng;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Soriano on 11/04/18.
 */

public class PlaceAdapter extends ArrayAdapter<Place> {

    private Context context;
    private OnClickListener listener;
    private LatLng fromLocation;

    public PlaceAdapter(Context context, List<Place> placeList, OnClickListener listener, LatLng fromLocation){
        super(context, R.layout.row_saved_place,placeList);
        this.context = context;
        this.listener = listener;
        this.fromLocation = fromLocation;
    }

    public interface OnClickListener{
        void OnClick(int position);
    }

    static class ViewHolder{
        @BindView(R.id.place_title)
        TextView titleTextView;
        @BindView(R.id.place_image)
        ImageView imageView;
        @BindView(R.id.place_distance) TextView distanceTextView;
        @BindView(R.id.place_location_button)
        ImageButton locationButton;
        @BindView(R.id.place_category)
        CircularImageView categoryImage;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //Binds views
        ViewHolder viewHolder;
        if(convertView != null){
            viewHolder = (ViewHolder) convertView.getTag();
        }
        else {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_saved_place,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        Place currentPlace = getItem(position);


        //Sets fields
        viewHolder.titleTextView.setText(currentPlace.getTitle());
        viewHolder.distanceTextView.setText(currentPlace.getDistance(fromLocation) + " m");
        Glide.with(parent.getContext())
                .load("")
                .error(getCategoryImage(currentPlace.getCategory()))
                .into(viewHolder.categoryImage);
        Glide.with(parent.getContext())
                .load(Uri.parse(currentPlace.getImageUri()))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.imageView);
        viewHolder.locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(position);
            }
        });

        return convertView;
    }

    public static int getCategoryImage(int category){
        int[] categories = {R.drawable.restaurant,R.drawable.hotel,R.drawable.bar,
                R.drawable.gas,R.drawable.cafe,R.drawable.airport};

        return categories[category];
    }
}
