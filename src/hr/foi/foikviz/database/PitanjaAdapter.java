package hr.foi.foikviz.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PitanjaAdapter {
	private DBHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;
	private String odgovor = "WHOT?";

	public PitanjaAdapter(Context c) {
		context = c;
	}

	public PitanjaAdapter openToRead() throws android.database.SQLException {

		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public PitanjaAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	public int getBrojPitanja(int razina) {
		int brojPitanja = 0;

		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT id_pitanja FROM pitanja where razina = '" + razina
						+ "'", null);
		brojPitanja = cursor.getCount();
		Log.d("BROJ PRETPLATA", "" + brojPitanja);
		cursor.close();
		return brojPitanja;
	}

	public String getPrviOdgovor(int razina, int id_pitanja) {
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT prvi_odgovor FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odgovor = cursor.getString(cursor.getColumnIndex("prvi_odgovor"));
		return odgovor;
	}

	public String getDrugiOdgovor(int razina, int id_pitanja) {
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT drugi_odgovor FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odgovor = cursor.getString(cursor.getColumnIndex("drugi_odgovor"));
		return odgovor;
	}

	public String getTreciOdgovor(int razina, int id_pitanja) {
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT treci_odgovor FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odgovor = cursor.getString(cursor.getColumnIndex("treci_odgovor"));
		return odgovor;
	}

	public String getCetvrtiOdgovor(int razina, int id_pitanja) {
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT cetvrti_odgovor FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odgovor = cursor.getString(cursor.getColumnIndex("cetvrti_odgovor"));
		return odgovor;
	}

	public String getPitanje(int razina, int id_pitanja) {
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT pitanje FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odgovor = cursor.getString(cursor.getColumnIndex("pitanje"));
		return odgovor;
	}

	public int getTocanOdgovor(int razina, int id_pitanja) {
		int odg = 1;
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT tocan_odgovor FROM pitanja where razina = '" + razina
						+ "' and id_pitanja = '" + id_pitanja + "'", null);
		cursor.moveToFirst();
		odg = cursor.getInt(cursor.getColumnIndex("tocan_odgovor"));
		return odg;
	}

	public int getIdPitanja(int razina, int random_broj) {
		int odgovor;
		Cursor cursor = sqLiteDatabase.rawQuery(
				"SELECT id_pitanja FROM pitanja where razina = '" + razina
						+ "'", null);
		cursor.moveToFirst();
		cursor.move(random_broj - 1);
		odgovor = cursor.getInt(cursor.getColumnIndex("id_pitanja"));
		return odgovor;
	}

}