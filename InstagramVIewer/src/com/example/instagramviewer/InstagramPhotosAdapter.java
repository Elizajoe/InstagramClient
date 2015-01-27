package com.example.instagramviewer;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, List<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}

	//Takes a data item at a position , converts it to a row in a listview
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Take data source at position (i.e 0)
		//Get the data item
		InstagramPhoto photo = getItem(position);
		//Check if we are using a recycled view
		if(convertView ==null){
			convertView =LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
		}
		//Lookup the subview template
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		TextView tvUsername =(TextView) convertView.findViewById(R.id.tvUsername);
		TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		//Populate the subviews (textfield,imageview) with the correct data item
		tvCaption.setText(photo.caption);
		tvUsername.setText(photo.username);
		tvLikes.setText(String.valueOf(photo.likes_count)+ " likes");
		//set image height before loading
		imgPhoto.getLayoutParams().height=photo.imageHeight;
		//Reset the image from the recycled view
		imgPhoto.setImageResource(0);
		//Ask for the photo to be added to the imageview based on the photo url
		//Background: Send a network request to the url, download the image bytes, , convert it into bitmap ,resizing the image, insert the bitmap into imageview
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		//Return the view for that data item
		return convertView; 
	}

	// Adapter calls getView method (int position)
	// Default, takes the model (InstagramPhoto).toString()

}
