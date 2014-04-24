package hr.foi.foikviz.helper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Used to check network connectivity and other stuff
 * @author Renato
 *
 */
public class NetworkConnectivity {

	Context context;
	
	public NetworkConnectivity(Context context) {
		this.context = context;
	}
	/**
	 * Checks if there is network connectivity.
	 * 
	 * @return True if there is network connectivity, false if there isn't
	 */
	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * Checks if there is active Internet connectivity.
	 * 
	 * @return True if there is active Internet connection, false if there isn't
	 */
	public boolean haveActiveInternetConnection() {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		}
		if (!i.isConnected()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}
	

	/**
	 * Checks if user location is available.
	 * 
	 * @param context
	 *            Activity context
	 * @return True if user location is available, false if it isn't
	 */
	public boolean checkIfUserLocationAvailable(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean networkLocationEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		boolean gpsProviderEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!networkLocationEnabled && !gpsProviderEnabled) {
			return false;
		}
		return true;
	}
	
	/**
	 * Shows no Internet connection warning alert dialog with OK and Open settings button.
	 */
	public void showNoInternetConnectionAlertDialog() {
		AlertDialog alertDialog;
		alertDialog = new AlertDialog.Builder(context).create();

		alertDialog.setTitle("Upozorenje!");
		alertDialog.setMessage("Nemate aktivnu!"
				+ "<br><br>" + "Trebate aktivnu Internet vezu!");

		alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// ok
					}
				});

		alertDialog.setButton(Dialog.BUTTON_NEGATIVE,
				"Otvori postavke!",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						context.startActivity(new Intent(
								Settings.ACTION_WIRELESS_SETTINGS));
					}
				});
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
}
