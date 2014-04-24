package hr.foi.foikviz;

import hr.foi.foikviz.database.PobjednikAdapter;
import hr.foi.foikviz.helper.NetworkConnectivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foikviz.R;

public class PobjedaActivity extends Activity {
	private String pobjednik;
	private Context context;
	private int broj_pobjednika;
	private long razlika_vremena;
	NetworkConnectivity nc;
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pobjeda);
		context = this;
		nc = new NetworkConnectivity(context);

		// postavljanje krajnjeg vremena rješavanja
		PocetniScreenActivity.vrijeme.setZavrsetak(System.currentTimeMillis());

		// dodavanje random fonta
		final EditText et_ime_prezime = (EditText) findViewById(R.id.pobjeda_ime_prezime);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/sturkopf_grotesk_medium.ttf");
		et_ime_prezime.setTypeface(tf);

		//button za nazad koji ne upisuje pobjednika!
		ImageButton ib_natrag = (ImageButton) findViewById(R.id.btn_nazad_pobjeda);
		ib_natrag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				prikaziToast("Nije se upisao pobjednik.");
				Intent i = new Intent(getApplicationContext(),
						PocetniScreenActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(R.drawable.pocetna_kviz_in,
						R.drawable.pocetna_kviz_out);
			}
		});
		
		//button koji ima provjere, upiše pobjednika u bazu i pošalje na net!
		ImageButton btn_upisi_pobjednika = (ImageButton) findViewById(R.id.btn_upisi_pobjednika);
		btn_upisi_pobjednika.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO
				// upisiPobjednika();// OVA NEK BUDE NE METODA PA DA VIDIMO JEL
				// ZNAJAUUUUUUUUU
				if (et_ime_prezime.getText().toString().length() == 0) {
					prikaziToast("Upišite ime da vas možemo razlikovati u gomili!");
				} else {
					if(nc.haveActiveInternetConnection()){
						upisiPobjednika();
						prikaziToast("Podaci poslani na web server!");
						Intent i = new Intent(getApplicationContext(),
								PocetniScreenActivity.class);
						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						overridePendingTransition(R.drawable.pocetna_kviz_in,
								R.drawable.pocetna_kviz_out);
					}else{
						prikaziToast("Trebate aktivnu Internet vezu kako bi se podaci poslali na web server!");
					}
				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		EditText et_ime_prezime = (EditText) findViewById(R.id.pobjeda_ime_prezime);
		if (et_ime_prezime.getText().toString().length() == 0) {
			prikaziToast("Upišite ime da vas možemo razlikovati u gomili!");
		} else {
			super.onBackPressed();
			upisiPobjednika();
			Intent i = new Intent(getApplicationContext(),
					PocetniScreenActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			overridePendingTransition(R.drawable.pocetna_kviz_in,
					R.drawable.pocetna_kviz_out);
		}

	}

	public void upisiPobjednika() {
		//slanje jsona
		new sendJsonData().execute();
		
		EditText et_ime_prezime = (EditText) findViewById(R.id.pobjeda_ime_prezime);
		pobjednik = et_ime_prezime.getText().toString();
		razlika_vremena = (PocetniScreenActivity.vrijeme.getZavrsetak() - PocetniScreenActivity.vrijeme
				.getPocetak());
		PobjednikAdapter pa = new PobjednikAdapter(getApplicationContext());
		pa.openToRead();
		broj_pobjednika = pa.getCount() + 1;
		pa.close();
		pa.openToWrite();
		pa.upisiPobjednika(broj_pobjednika, pobjednik, razlika_vremena);
		pa.close();
	}
	
	public void prikaziToast(String text){
		 LayoutInflater inflater = getLayoutInflater();
         View layout = inflater.inflate(R.layout.custom_toast_message,
                                        (ViewGroup) findViewById(R.id.custom_toast_layout));

         TextView tv = (TextView) layout.findViewById(R.id.textToShow);
         tv.setText(text);

         Toast toast = new Toast(getApplicationContext());
         toast.setGravity(Gravity.BOTTOM, 0, 0);
         toast.setDuration(Toast.LENGTH_SHORT);
         toast.setView(layout);
         toast.show();
	}
	
	public class sendJsonData extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... strings) {
			try {
				postData();
			} catch (JSONException e) {
				Log.w("JSONNNNN", "u send JSONU PROBLEM!");
				e.printStackTrace();
			}
			return "OK";
		}
	}
	
	public void postData() throws JSONException{  
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		//HttpPost httppost = new HttpPost("http://tosulc.comoj.com/primanje_podataka.php");
		//HttpPost httppost = new HttpPost("http://arka.foi.hr/~zantolov/foikviz/primanje_podataka.php");
		//HttpPost httppost = new HttpPost("http://antolovic-zoran.com/foikviz/primanje_podataka.php");
		HttpPost httppost = new HttpPost("http://prijavi-pozar.com/foikviz/primanje_podataka.php");
		
		JSONObject json = new JSONObject();

		try {
			// JSON data:
			json.put("pobjednik", pobjednik);
			json.put("razlika_vremena", razlika_vremena);
			json.put("vrijeme", System.currentTimeMillis());

			JSONArray postjson=new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json",json.toString());
			httppost.getParams().setParameter("jsonpost",postjson);

			// Execute HTTP Post Request
			//System.out.print(json);
			Log.w("JSONNNNN", json.toString());
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if(response != null)
			{
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					Log.w("JSONNNNN", "NOOOOOOO111111");
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						Log.w("JSONNNNN", "NIJE ZATVORIOOOOOOOOOOOOOO");
						e.printStackTrace();
					}
				}
			}


		}catch (ClientProtocolException e) {
			Log.w("JSONNNNN", "NOOOOOOO1");
		} catch (IOException e) {
			Log.w("JSONNNNN", "NOOOOOOO2");
    	}
	}//postData
	
}//class
