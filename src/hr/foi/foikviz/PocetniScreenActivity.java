package hr.foi.foikviz;

import hr.foi.foikviz.types.Vrijeme;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.foikviz.R;

public class PocetniScreenActivity extends Activity {
	public static Vrijeme vrijeme = new Vrijeme(); 
	
	@SuppressLint("NewApi")
	@Override
	protected void onResume() {
		super.onResume();
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pocetna);
		
		ImageButton ib_oblak3 = (ImageButton) findViewById(R.id.oblak3);
		ib_oblak3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ListaPobjednikaActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(R.drawable.pocetna_high_in,
						R.drawable.pocetna_high_out);
			}
		});

		ImageButton ib_oblak2 = (ImageButton) findViewById(R.id.oblak2);
		ib_oblak2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						NagradeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				overridePendingTransition(R.drawable.pocetna_nagrade_in, R.drawable.pocetna_nagrade_out);
				
			}
		});

		ImageButton btn_animacija = (ImageButton) findViewById(R.id.ponovnaAnimacija);
		btn_animacija.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = getIntent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NO_ANIMATION);
				getWindow().setWindowAnimations(0);
				startActivity(intent);
			}
		});

		ImageButton btn_na_kviz = (ImageButton) findViewById(R.id.button_na_kviz);
		btn_na_kviz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				vrijeme.setPocetak(System.currentTimeMillis());
				Intent i = new Intent(getApplicationContext(),
						KvizMainActivity.class);
				i.putExtra("razina", 1); // mod otvaranja aktivnosti je pregled
				startActivity(i);
				overridePendingTransition(R.drawable.pocetna_kviz_in,
						R.drawable.pocetna_kviz_out);
			}
		});
		float x1 = (float) 4.66;
		float y1 = 0;
		float x2 = 0;
		float y2 = 0;

		// pokretanje start buttona!
		/*
		 * TranslateAnimation translateAnimation = new
		 * TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
		 * Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x1,
		 * Animation.RELATIVE_TO_SELF, 0); translateAnimation.setDuration(1500);
		 * translateAnimation.setRepeatCount(3); //start animation
		 * btn_na_kviz.startAnimation(translateAnimation);
		 */
		pokreniAnimaciju(btn_na_kviz, x1, y1, x2, y2, 1500);
		ImageButton ib_oblak1 = (ImageButton) findViewById(R.id.oblak1);
		ImageButton ib_oblak4 = (ImageButton) findViewById(R.id.oblak4);
		ImageView iv_foi_logo = (ImageView) findViewById(R.id.foi_logo);
		ImageView iv_koliko_znas = (ImageView) findViewById(R.id.iv_otkrij_koliko_znas);

		// pokretanje oblaka1!
		x1 = -2;
		y1 = (float) 0.75;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(ib_oblak1, x1, y1, x2, y2, 1500);
		x1 = -2;
		y1 = (float) 0.55;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(ib_oblak3, x1, y1, x2, y2, 1500);
		x1 = 2;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(ib_oblak2, x1, y1, x2, y2, 1500);
		x1 = 2;
		y1 = (float) 0.66;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(ib_oblak4, x1, y1, x2, y2, 1200);
		// foilogo
		x1 = 0;
		y1 = 2;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(iv_foi_logo, x1, y1, x2, y2, 1500);
		// koliko znas
		x1 = -3;
		y1 = 0;
		x2 = 0;
		y2 = 0;
		pokreniAnimaciju(iv_koliko_znas, x1, y1, x2, y2, 1500);

	}

	public static void pokreniAnimaciju(View v, float x1, float y1, float x2,
			float y2, int duration) {
		TranslateAnimation translateAnimation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, x1, Animation.RELATIVE_TO_SELF, x2,
				Animation.RELATIVE_TO_SELF, y1, Animation.RELATIVE_TO_SELF, y2);
		translateAnimation.setDuration(duration);
		// translateAnimation.setRepeatCount(3);
		// translateAnimation.setRepeatCount(3);
		v.startAnimation(translateAnimation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.kviz_main, menu);
		return true;
	}

}
