package com.example.mapapi;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.GoogleMap.OnMapClickListener;

public class MainActivity extends FragmentActivity {

	GoogleMap mMap;
	Marker mMarker;
	LocationManager lm;
	double lat, lng;
	
	Intent intent;
	Button btnClear, bntConfirm;

	public static ArrayList<Double> la = new ArrayList<Double>();
	public static ArrayList<Double> ln = new ArrayList<Double>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		btnClear = (Button) findViewById(R.id.btnClear);
		btnClear.bringToFront();
		btnClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mMap.clear();
				la.clear();
				ln.clear();
				Toast.makeText(MainActivity.this, "Clear all mark",
						Toast.LENGTH_SHORT).show();
				
				onResume();
				new ProgressTask2().execute();

			}
		});

		bntConfirm = (Button) findViewById(R.id.btnConfirm);
		bntConfirm.bringToFront();
		intent = new Intent(getApplicationContext(), Distance.class);
		bntConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(intent);
			}
		});

		new ProgressTask().execute();

		// ------Add Mark----

		mMap.setOnMapClickListener(new OnMapClickListener() {
			public void onMapClick(LatLng arg) {

				if (la.size() <= 1 || ln.size() <= 1) {

					mMap.addMarker(new MarkerOptions().position(arg).title(
							String.valueOf(arg.latitude) + ", "
									+ String.valueOf(arg.longitude)));

					la.add(Double.parseDouble(String.valueOf(arg.latitude)));
					ln.add(Double.parseDouble(String.valueOf(arg.longitude)));


				} else {
					Toast.makeText(MainActivity.this, "Can't add mark",
							Toast.LENGTH_SHORT).show();
				}


			}
		});

		/*
		 * mMap.setOnMarkerClickListener(new OnMarkerClickListener() { public
		 * boolean onMarkerClick(Marker args) {
		 * 
		 * String id = String.valueOf(args.getId()); if (id.equals("m0")) {
		 * Toast.makeText(MainActivity.this, "Can not remove",
		 * Toast.LENGTH_SHORT).show(); } else { args.remove();
		 * Toast.makeText(MainActivity.this, "Remove "+id,
		 * Toast.LENGTH_SHORT).show(); }
		 * 
		 * return true; } });
		 */

	}

	LocationListener listener = new LocationListener() {
		public void onLocationChanged(Location loc) {
			LatLng coordinate = new LatLng(loc.getLatitude(),
					loc.getLongitude());
			lat = loc.getLatitude();
			lng = loc.getLongitude();

			if (mMarker != null)
				mMarker.remove();

			mMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(
					lat, lng)));
			mMap.animateCamera(CameraUpdateFactory
					.newLatLngZoom(coordinate, 14));

			la.add(lat);
			ln.add(lng);
			Log.d("Log", "lat: " + lat + " lng: " + lng);

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
			dialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
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
	
	private class ProgressTask2 extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		protected void onPreExecute() {
			dialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
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
				Thread.sleep(5000);
			} catch (Exception e) {
				return false;
			}

			return true;

		}

	}
}
