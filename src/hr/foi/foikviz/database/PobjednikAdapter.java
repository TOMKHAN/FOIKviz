package hr.foi.foikviz.database;

import hr.foi.foikviz.types.Pobjednik;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PobjednikAdapter {
	public static final String TABLE = "pobjednik";
	public static final String KEY_ID = "id_pobjednika";
	private DBHelper sqLiteHelper;
	private SQLiteDatabase sqLiteDatabase;
	private Context context;

	public PobjednikAdapter(Context c) {
		context = c;
	}

	public PobjednikAdapter openToRead() throws android.database.SQLException {

		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getReadableDatabase();
		return this;
	}

	public PobjednikAdapter openToWrite() throws android.database.SQLException {
		sqLiteHelper = new DBHelper(context);
		sqLiteDatabase = sqLiteHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		sqLiteHelper.close();
	}

	public int getCount() {
		Cursor cursor = sqLiteDatabase.rawQuery("select id_pobjednika from "
				+ TABLE
				+ " where id_pobjednika=(select max(id_pobjednika) from "
				+ TABLE + ");", null);
		if (cursor.getCount() == 1) {
			cursor.moveToFirst();
			int broj = cursor.getInt(cursor.getColumnIndex("id_pobjednika"));
			cursor.close();
			return broj;
		} else {
			cursor.close();
			return 0;
		}
	}

	public void upisiPobjednika(int id_pobjednika, String ime_prezime,
			long razlika_vremena) {
		sqLiteDatabase
				.execSQL("INSERT INTO pobjednik(id_pobjednika, ime, razlika_vremena) "
						+ "values ('"
						+ id_pobjednika
						+ "','"
						+ ime_prezime
						+ "', '" + razlika_vremena + "');");

	}

	public List<Pobjednik> getListaPobjednika() {
		int id_pobjednika;
		String ime;
		long millis, razlika_vremena;
		Date datum;
		Cursor cursor = sqLiteDatabase
				.rawQuery(
						"SELECT id_pobjednika, ime, (strftime('%s', vrijeme) * 1000) as vrijeme, razlika_vremena FROM pobjednik ORDER BY razlika_vremena ASC;",
						null);
		List<Pobjednik> rezultati = new ArrayList<Pobjednik>();
		cursor.moveToFirst();

		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			id_pobjednika = cursor.getInt(cursor
					.getColumnIndex("id_pobjednika"));
			ime = cursor.getString(cursor.getColumnIndex("ime"));
			millis = cursor.getLong(cursor.getColumnIndexOrThrow("vrijeme"));
			razlika_vremena = cursor.getLong(cursor
					.getColumnIndex("razlika_vremena"));
			datum = new Date(millis);
			Pobjednik pob = new Pobjednik(id_pobjednika, ime, datum,
					razlika_vremena);
			rezultati.add(pob);
		}
		cursor.close();
		return rezultati;
	}

}