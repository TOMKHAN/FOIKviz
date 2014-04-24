package hr.foi.foikviz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foikviz.R;

public class NagradeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nagrade);
		
		ImageButton btn_nazad_nagrade = (ImageButton)findViewById(R.id.btn_nazad_nagrade);
		btn_nazad_nagrade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						PocetniScreenActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				overridePendingTransition(R.drawable.pocetna_nagrade_in,
						R.drawable.pocetna_nagrade_out);
			}
		});
		
		ImageView iv_jojo = (ImageView) findViewById(R.id.jojo);
		ImageView iv_blok = (ImageView) findViewById(R.id.blok);
		ImageView iv_knjizica = (ImageView) findViewById(R.id.knjizica);
		ImageView iv_olovke = (ImageView) findViewById(R.id.olovke);
		ImageView iv_loptica = (ImageView) findViewById(R.id.loptica);
		
		float x1 = (float) -3.00;
		float y1 = 0;
		float x2 = 0;
		float y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(iv_jojo, x1, y1, x2, y2, 1500);
		x1 = 3;
		y1 = (float) 0.75;
		x2 = 0;
		y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(iv_blok, x1, y1, x2, y2, 1500);
		x1 = -3;
		y1 = (float) 0.75;
		x2 = 0;
		y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(iv_knjizica, x1, y1, x2, y2, 1500);
		x1 = 3;
		y1 = (float) 0.75;
		x2 = 0;
		y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(iv_olovke, x1, y1, x2, y2, 1500);
		
		x1 = 0;
		y1 = (float) 5.00;
		x2 = 0;
		y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(btn_nazad_nagrade, x1, y1, x2, y2, 1500);
		
		x1 = 3;
		y1 = (float) 0;
		x2 = 0;
		y2 = 0;
		PocetniScreenActivity.pokreniAnimaciju(iv_loptica, x1, y1, x2, y2, 1500);
		
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}

	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),
				PocetniScreenActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
		overridePendingTransition(R.drawable.pocetna_nagrade_in,
				R.drawable.pocetna_nagrade_out);

	}
}
