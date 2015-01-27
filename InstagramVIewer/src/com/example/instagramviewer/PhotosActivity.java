package com.example.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;


public class PhotosActivity extends Activity {

	
	public static final String CLIENT_ID ="af84b832b5de46cfb85828978ee70315";
	private ArrayList<InstagramPhoto> photos;
	private InstagramPhotosAdapter aPhotos;
	
	
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        fetchPopularPhotos();
    }


    private void fetchPopularPhotos() {
    	photos = new ArrayList<InstagramPhoto>();//initialize arraylist
    	
    	//Create adapter bind it to data arraylist
    	aPhotos = new InstagramPhotosAdapter(this, photos);
    	
    	//Populate the data into listview
    	ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
    	
    	//Set adapter to the listview
    	lvPhotos.setAdapter(aPhotos);
    	
    	
		//https://api.instagram.com/v1/media/popular?client_id=af84b832b5de46cfb85828978ee70315
    	// data => [x] =>images => standard_resolution => url 
    	
    	//Setup popular url endpoint
    	
    	String popularUrl ="https://api.instagram.com/v1/media/popular?client_id="+ CLIENT_ID;
    	
    	//Create the newtwork client
    	AsyncHttpClient client = new AsyncHttpClient();
    	
    	//Triger network request
    	client.get(popularUrl, new JsonHttpResponseHandler(){
    		// define success and failure callbacks
    		//Handle the successful response	
    		@Override
    		public void onSuccess(int statusCode, Header[] headers,JSONObject response) {
    			//fired once successful response comes back
    			//response is == popular photos json
				// url, height, username, caption
    			//data => [x] =>images => standard_resolution => url 
    			//data => [x] =>images => standard_resolution => height
    			//data => [x] =>user => "username"
    			//data => [x] => caption => text 
    			
    			JSONArray photosJSON= null;
    			try{
    				photos.clear();
    				photosJSON=response.getJSONArray("data");
    				for(int i=0;i<photosJSON.length();i++){
    					
    					JSONObject photoJSON = photosJSON.getJSONObject(i); //1,2,3,4 photos
    					 InstagramPhoto photo = new InstagramPhoto();
    					 if(photoJSON.getJSONObject("user")!= null){
    					 photo.username = photoJSON.getJSONObject("user").getString("username");
    					 }
    					 if(photoJSON.getJSONObject("caption")!= null){
    					 photo.caption = photoJSON.getJSONObject("caption").getString("text");
    					 }else{
    						 photo.caption ="";
    					 }
    					 photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
    					 photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
    					 photo.likes_count = photoJSON.getJSONObject("likes").getInt("count");
    					 //Log.i("DEBUG",photo.toString());
    					 photos.add(photo);
    					
    				}
    				
    				//Notified the adapter that it should populate new changes into the list view
    				aPhotos.notifyDataSetChanged();
    				
    			}catch(JSONException e ){
    				e.printStackTrace();
    			}
    		}
    		
    		
    		@Override
    		public void onFailure(int statusCode, Header[] headers,String responseString, Throwable throwable) {
    			// TODO Auto-generated method stub
    			super.onFailure(statusCode, headers, responseString, throwable);
    		}
    		
    		
    	});
    	
    	//Handle the successful response	
    	
		
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
