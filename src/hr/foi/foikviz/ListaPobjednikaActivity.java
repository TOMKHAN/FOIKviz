package hr.foi.foikviz;

import hr.foi.foikviz.database.PobjednikAdapter;
import hr.foi.foikviz.types.Pobjednik;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.foikviz.R;

public class ListaPobjednikaActivity extends Activity {
	Context context;
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.example.foikviz.R.layout.lista_pobjednika);
		context = this;
		
		TextView tv_ukupni_broj_pobjednika = (TextView)findViewById(R.id.tv_broj_pobjednika);
		PobjednikAdapter pa = new PobjednikAdapter(context);
		pa.openToRead();
		tv_ukupni_broj_pobjednika.setText(Integer.toString(pa.getCount()));
		pa.close();
		
		Button btn_nazad = (Button)findViewById(R.id.pobjeda_btn_nazad);
		btn_nazad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						PocetniScreenActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(R.drawable.high_pocetna_in,
						R.drawable.high_pocetna_out);
			}
		});

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/sturkopf_grotesk_medium.ttf");
		TextView tv_ime = (TextView) findViewById(R.id.lista_pobjednika_tv_ime);
		TextView tv_datum = (TextView) findViewById(R.id.lista_pobjednika_datum);
		TextView tv_vrijeme_rj = (TextView) findViewById(R.id.lista_pobjednika_tv_vrijeme_rjesavanja);
		tv_ime.setTypeface(tf);
		tv_datum.setTypeface(tf);
		tv_vrijeme_rj.setTypeface(tf);
		tv_ime.setText("Ime i prezime:");
		tv_datum.setText("Datum:");
		tv_vrijeme_rj.setText("Za:");

		fillList();

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(getApplicationContext(),
				PocetniScreenActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		overridePendingTransition(R.drawable.high_pocetna_in,
				R.drawable.high_pocetna_out);

	}

	@SuppressLint("SimpleDateFormat")
	private void fillList() {

		List<Pobjednik> pobjednik = null;

		ListView lv; // referenca na ListView objekt

		String[] from; // nazivi atributa koji se mapiraju
		int[] to; // nazivi elemenata predloška u koje se mapira

		HashMap<String, String> map; // veza podataka i predloška za jedan
										// zapisa
		ArrayList<HashMap<String, String>> fillMaps; // lista mapa (veza)

		Iterator<Pobjednik> itr; // standardni iterator
		SimpleAdapter adapter;

		// za dobivanje brojeva dolazaka/izostanaka
		PobjednikAdapter pa = new PobjednikAdapter(context);
		pa.openToRead();
		pobjednik = pa.getListaPobjednika();
		pa.close();
		// definiranje atributa za veze
		from = new String[] { "ime", "vrijeme", "razlika_vremena" };

		to = new int[] { R.id.tv_ime_prezime, R.id.tv_datum,
				R.id.tv_razlika_vremena };

		// kreiranje mapa
		fillMaps = new ArrayList<HashMap<String, String>>();

		itr = pobjednik.iterator();
		while (itr.hasNext()) {
			map = new HashMap<String, String>();
			Pobjednik pob = itr.next();
			map.put("ime", pob.getIme_prezime());
			SimpleDateFormat sdf = new SimpleDateFormat(
					"dd.MM. 'u' HH'h' 'i' mm'min'");
			map.put("vrijeme", sdf.format(pob.getDatum()));

			// vrijeme razlike upis u mapu!
			SimpleDateFormat sdf2 = new SimpleDateFormat("mm'min:'ss'sec'");
			sdf2.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date datum_vremenska_razlika = new Date(pob.getRazlika_vremena());
			String str_vremenska_razlika = sdf2.format(datum_vremenska_razlika);
			map.put("razlika_vremena", str_vremenska_razlika);
			fillMaps.add(map);
		}

		lv = (ListView) findViewById(R.id.datalist_pobjednik);
		lv.invalidateViews();

		// generiarnje podataka za prikaz u list view-u

		adapter = new SpecialAdapter(this, fillMaps,
				R.layout.list_item_pobjednik, from, to);
		lv.setAdapter(adapter);
		registerForContextMenu(lv);
	}

	public class SpecialAdapter extends SimpleAdapter {

		public SpecialAdapter(Context context,
				List<HashMap<String, String>> items, int resource,
				String[] from, int[] to) {
			super(context, items, resource, from, to);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			Typeface tf = Typeface.createFromAsset(getAssets(),
					"fonts/sturkopf_grotesk_medium.ttf");
			TextView tv_ime_prezime = (TextView)view.findViewById(R.id.tv_ime_prezime);
			TextView tv_datum = (TextView)view.findViewById(R.id.tv_datum);
			TextView tv_razlika_vremena = (TextView)view.findViewById(R.id.tv_razlika_vremena);
			tv_ime_prezime.setTypeface(tf);
			tv_datum.setTypeface(tf);
			tv_razlika_vremena.setTypeface(tf);
			tv_ime_prezime.setTextSize(30);
			tv_datum.setTextSize(25);
			tv_razlika_vremena.setTextSize(25);

			return view;
		}// View }
	}

}
