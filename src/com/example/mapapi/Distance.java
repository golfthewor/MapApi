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
	LatLng startPosition;
	LatLng endPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.distance);
		
		for (int i = 0; i < la.size(); i++) {
			Log.d("Log", "lat: " + la.get(i) + " lng: " + ln.get(i));
			
			startPosition = new LatLng(la.get(0), ln.get(0));
			endPosition = new LatLng(la.get(1), ln.get(1));
		}

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_result)).getMap();

		generate();
		new ProgressTask().execute();

	}

	public void generate() {
		
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		md = new GMapV2Direction();
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map_result)).getMap();


		mMap.addMarker(new MarkerOptions().position(startPosition));
		mMap.addMarker(new MarkerOptions().position(endPosition));

		Document doc = md.getDocument(startPosition, endPosition, mode);

		@SuppressWarnings("unchecked")
		ArrayList<LatLng> directionPoint = md.getDirection(doc);
		PolylineOptions rectLine = new PolylineOptions().width(8).color(
				Color.RED);

		for (int i = 0; i < directionPoint.size(); i++) {
			rectLine.add(directionPoint.get(i));
		}

		int distance_value = md.getDistanceValue(doc);
		String distance_text = md.getDistanceText(doc);
		String duration_text = md.getDurationText(doc);
		
		Log.d("Log", "Distance value: "+distance_value);
		Log.d("Log", "Distance text: "+distance_text);
		Log.d("Log", "Duration text: "+duration_text);
		
		mMap.addPolyline(rectLine);
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
}
