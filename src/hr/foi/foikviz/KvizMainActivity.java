package hr.foi.foikviz;

import hr.foi.foikviz.database.PitanjaAdapter;

import java.util.Random;

import com.example.foikviz.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class KvizMainActivity extends Activity {
	private int razina, broj_pitanja, redni_broj_pitanja, random_broj;
	private String odgovor;

	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_kviz_main);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			razina = bundle.getInt("razina");
			Log.w("bundle", "PROBUĐENNNNNN");
		}

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/sturkopf_grotesk_medium.ttf");
		TextView tv_tekst_pitanja = (TextView) findViewById(R.id.tekst_pitanja);
		tv_tekst_pitanja.setTypeface(tf);

		/*
		 * Resources res = getResources(); ProgressBar pb = (ProgressBar)
		 * findViewById(R.id.progressBar);
		 * pb.setProgressDrawable(res.getDrawable( R.drawable.));
		 */
		updateActivity();

	}// onCreate

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		updateActivity();
	}

	private void updateActivity() {
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);
		pb.setMax(6);
		pb.setProgress(razina);
		TextView tv_progressBar = (TextView) findViewById(R.id.tekst_progressa);
		tv_progressBar.setText(razina + "/6");

		if (razina == 7) {
			Intent pobjedaIntent = new Intent(getApplicationContext(),
					PobjedaActivity.class);
			pobjedaIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(pobjedaIntent);
			overridePendingTransition(R.drawable.kviz_pocetna_in,
					R.drawable.kviz_pocetna_out);
		} else {
			Random r = new Random();
			PitanjaAdapter pa = new PitanjaAdapter(getBaseContext());
			pa.openToRead();
			Log.w("RAZINAAAAAAAAA", "Iznosii: " + razina);
			broj_pitanja = pa.getBrojPitanja(razina);
			Log.w("broj_pitanja brojjjjj", "Iznosii: " + broj_pitanja);
			random_broj = r.nextInt(broj_pitanja) + 1;
			Log.w("random brojjjjjj", "Iznosii: " + random_broj);
			redni_broj_pitanja = pa.getIdPitanja(razina, random_broj);
			Log.w("redni_broj_pitanja", "Iznosii: " + redni_broj_pitanja);
			pa.close();

			TextView tv_pitanje = (TextView) findViewById(R.id.tekst_pitanja);
			tv_pitanje.setText(getPitanje(razina, redni_broj_pitanja));

			Button btn1 = (Button) findViewById(R.id.prvi_odgovor);
			Button btn2 = (Button) findViewById(R.id.drugi_odgovor);
			Button btn3 = (Button) findViewById(R.id.treci_odgovor);
			Button btn4 = (Button) findViewById(R.id.cetvrti_odgovor);
			btn1.setText(getOdgovor(1, razina, redni_broj_pitanja));
			btn2.setText(getOdgovor(2, razina, redni_broj_pitanja));
			btn3.setText(getOdgovor(3, razina, redni_broj_pitanja));
			btn4.setText(getOdgovor(4, razina, redni_broj_pitanja));

			// kad se klikne
			btn1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					provjeriOdgovor(1, v);
				}
			});

			btn2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					provjeriOdgovor(2, v);
				}
			});

			btn3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					provjeriOdgovor(3, v);
				}
			});
			btn4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					provjeriOdgovor(4, v);
				}
			});
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(getApplicationContext(),
				PocetniScreenActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		overridePendingTransition(R.drawable.kviz_pocetna_in,
				R.drawable.kviz_pocetna_out);
	}

	private int provjeriOdgovor(int odgovor_po_redu, View v) {
		PitanjaAdapter pa = new PitanjaAdapter(getBaseContext());
		pa.openToRead();
		if (odgovor_po_redu == pa.getTocanOdgovor(razina, redni_broj_pitanja)) {
			razina += 1;
			// http://developer.android.com/about/dashboards/index.html
			// verzije ispod 16 ne podržavaju programsko mijenjanje resursa
			// slike
			if (Build.VERSION.SDK_INT >= 16)
				v.setBackground(getResources()
						.getDrawable(R.drawable.btn_green));

			Log.w("razinaaaa u provjeri", "Iznosii: " + razina);
			Intent intent = getIntent();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("razina", razina);
			finish();
			startActivity(intent);
			pa.close();
			overridePendingTransition(R.drawable.pocetna_kviz_in,
					R.drawable.pocetna_kviz_out);
		} else {
			prikaziToast("Odgovor nije točan!");
			
			Intent i = new Intent(getApplicationContext(),
					PocetniScreenActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			razina = 1;
			Log.w("razinaaaa u provjeri", "Iznosii: " + razina);
			startActivity(i);
			pa.close();
		}
		return 1;
	}
	
	public void prikaziToast(String text) {
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

	private String getOdgovor(int odgovor_po_redu, int razina, int id_pitanja) {
		PitanjaAdapter pa = new PitanjaAdapter(getBaseContext());
		pa.openToRead();
		switch (odgovor_po_redu) {
		case 1:
			odgovor = pa.getPrviOdgovor(razina, id_pitanja);
			break;
		case 2:
			odgovor = pa.getDrugiOdgovor(razina, id_pitanja);
			break;
		case 3:
			odgovor = pa.getTreciOdgovor(razina, id_pitanja);
			break;
		case 4:
			odgovor = pa.getCetvrtiOdgovor(razina, id_pitanja);
			break;
		}
		pa.close();
		return odgovor;
	}

	private String getPitanje(int razina, int id_pitanja) {
		PitanjaAdapter pa = new PitanjaAdapter(getBaseContext());
		pa.openToRead();
		odgovor = pa.getPitanje(razina, id_pitanja);
		pa.close();
		return odgovor;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.kviz_main, menu);
		return true;
	}

}
