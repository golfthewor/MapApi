package com.example.mapapi;

import java.util.ArrayList;

import org.w3c.dom.Document;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

public class Distance extends FragmentActivity {

	GMapV2Direction md;
	GoogleMap mMap;
	LocationManager lm;
	double lat, lng;
	Intent intent;
	
	Button btnClear, bntConfirm;

	private ArrayList<Double> la = MainActivity.la;
	private ArrayList<Double> ln = MainActivity.ln;

	String mode = "walking";
	LatLng position1 = null;
	LatLng position2 = null;
	LatLng position3 = null;
	LatLng position4 = null;
	//LatLng position5 = null;
	//LatLng position6 = null;
	
	Document doc1 = null;
	Document doc2 = null;
	Document doc3 = null;
	//Document doc4 = null;
	//Document doc5 = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.distance);
		
		Log.d("Log",la.size()+" "+ln.size());
			//Log.d("Log", "lat: " + la.get(i) + " lng: " + ln.get(i));
			
			position1 = new LatLng(la.get(0), ln.get(0));
			Log.d("Log","position1 created");
			position2 = new LatLng(la.get(1), ln.get(1));
			Log.d("Log","position2 created");
			if(la.size() >= 3){
				position3 = new LatLng(la.get(2), ln.get(2));
				Log.d("Log","position3 created");
			}
			if(la.size() >= 4){
				position4 = new LatLng(la.get(3), ln.get(3));
				Log.d("Log","position4 created");
			}
			/*if(la.size() >= 5){
				position5 = new LatLng(la.get(4), ln.get(4));
				Log.d("Log","position5 created");
			}
			if(la.size() == 6){
				position6 = new LatLng(la.get(5), ln.get(5));
				Log.d("Log","position6 created");
			}*/
		

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_result)).getMap();

		generate();
		new ProgressTask().execute();

	}

	@SuppressWarnings("unchecked")
	public void generate() {
		
		int distance_value1 = 0;
		int distance_value2 = 0;
		int distance_value3 = 0;
		int distance_value4 = 0;
		int distance_value5 = 0;
		
		int duration_value1 = 0;
		int duration_value2 = 0;
		int duration_value3 = 0;
		int duration_value4 = 0;
		int duration_value5 = 0;
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		md = new GMapV2Direction();
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_result)).getMap();

		//----Add mark-----
		mMap.addMarker(new MarkerOptions().position(position1));
		mMap.addMarker(new MarkerOptions().position(position2));
		if(position3 != null){
			mMap.addMarker(new MarkerOptions().position(position3));
		}
		if(position4 != null){
			mMap.addMarker(new MarkerOptions().position(position4));
		}
		/*if(position5 != null){
			mMap.addMarker(new MarkerOptions().position(position5));
		}
		if(position6 != null){
			mMap.addMarker(new MarkerOptions().position(position6));
		}*/
		
		//-----Call direction------
		doc1 = md.getDocument(position1, position2, mode);
		if(position3 != null){
			doc2 = md.getDocument(position2, position3, mode);
		}
		if(position4 != null){
			doc3 = md.getDocument(position3, position4, mode);
		}
		/*if(position5 != null){
			doc4 = md.getDocument(position4, position5, mode);
		}
		if(position6 != null){
			doc5 = md.getDocument(position5, position6, mode);
		}*/
			
		//-----Gen direction------
		ArrayList<LatLng> directionPoint1 = md.getDirection(doc1);
		PolylineOptions rectLine1 = new PolylineOptions().width(8).color(Color.RED);
		for (int i = 0; i < directionPoint1.size(); i++) {
			rectLine1.add(directionPoint1.get(i));
		}	
		mMap.addPolyline(rectLine1);
		distance_value1 = md.getDistanceValue(doc1);
		duration_value1 = md.getDurationValue(doc1);
		
		if(doc2 != null){
			ArrayList<LatLng> directionPoint2 = md.getDirection(doc2);
			PolylineOptions rectLine2 = new PolylineOptions().width(8).color(Color.GREEN);
			for (int i = 0; i < directionPoint2.size(); i++) {
				rectLine2.add(directionPoint2.get(i));
			}	
			mMap.addPolyline(rectLine2);
			distance_value2 = md.getDistanceValue(doc2);
			duration_value2 = md.getDurationValue(doc2);
		}
		if(doc3 != null){
			ArrayList<LatLng> directionPoint3 = md.getDirection(doc3);
			PolylineOptions rectLine3 = new PolylineOptions().width(8).color(Color.BLUE);
			for (int i = 0; i < directionPoint3.size(); i++) {
				rectLine3.add(directionPoint3.get(i));
			}	
			mMap.addPolyline(rectLine3);
			distance_value3 = md.getDistanceValue(doc3);
			duration_value3 = md.getDurationValue(doc3);
		}
		/*if(doc4 != null){
			ArrayList<LatLng> directionPoint4 = md.getDirection(doc4);
			PolylineOptions rectLine4 = new PolylineOptions().width(8).color(Color.RED);
			for (int i = 0; i < directionPoint4.size(); i++) {
				rectLine4.add(directionPoint4.get(i));
			}	
			mMap.addPolyline(rectLine4);
			distance_value4 = md.getDistanceValue(doc4);
			duration_value4 = md.getDurationValue(doc4);
		}
		if(doc5 != null){
			ArrayList<LatLng> directionPoint5 = md.getDirection(doc5);
			PolylineOptions rectLine5 = new PolylineOptions().width(8).color(Color.RED);
			for (int i = 0; i < directionPoint5.size(); i++) {
				rectLine5.add(directionPoint5.get(i));
			}	
			mMap.addPolyline(rectLine5);
			distance_value5 = md.getDistanceValue(doc5);
			duration_value5 = md.getDurationValue(doc5);
		}*/
		
		Log.d("Log", "Distance value: "+distance_value1+distance_value2
				+distance_value3+distance_value4+distance_value5);
		Log.d("Log", "Duration value: "+duration_value1+duration_value2
				+duration_value3+duration_value4+duration_value5);
	}

	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			LatLng coordinate = new LatLng(loc.getLatitude(),
					loc.getLongitude());
			lat = loc.getLatitude();
			lng = loc.getLongitude();

			mMap.animateCamera(CameraUpdateFactory
					.newLatLngZoom(coordinate, 14));

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onProviderDisabled(String provider) {
		}
	};

	public void onResume() {
		super.onResume();

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		boolean isNetwork = lm
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (isNetwork) {
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000,
					10, listener);
			Location loc = lm
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
		}

		if (isGPS) {
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10,
					listener);
			Location loc = lm
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (loc != null) {
				lat = loc.getLatitude();
				lng = loc.getLongitude();
			}
		}
	}

	public void onPause() {
		super.onPause();
		lm.removeUpdates(listener);
	}

	// --------Wait------------------------------------

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(Distance.this, "", "Loading...");
		}

		@Override
		protected void onPostExecute(final Boolean success) {

			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			if (success) {
				Log.d("Log", "Complete wait");
			} else {
			}
		}

		@Override
		protected Boolean doInBackground(final String... args) {

			try {
				Thread.sleep(8000);
			} catch (Exception e) {
				return false;
			}

			return true;

		}

	}
	@Override
	public void onBackPressed() {
	    //Include the code here
		intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
}
