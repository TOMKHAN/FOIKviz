package hr.foi.foikviz.types;

import java.sql.Date;

public class Pobjednik {
	private int id_pobjednika;
	private String ime_prezime;
	private Date datum;
	private long razlika_vremena;

	public Pobjednik(int id_pobjednik, String ime, Date datum2, long razlika_vremena) {
		this.setId_pobjednika(id_pobjednik);
		this.setIme_prezime(ime);
		this.setDatum(datum2);
		this.setRazlika_vremena(razlika_vremena);
	}

	public int getId_pobjednika() {
		return id_pobjednika;
	}

	public void setId_pobjednika(int id_pobjednika) {
		this.id_pobjednika = id_pobjednika;
	}

	public String getIme_prezime() {
		return ime_prezime;
	}

	public void setIme_prezime(String ime_prezime) {
		this.ime_prezime = ime_prezime;
	}

	public Date getDatum() {
		return datum;
	}

	public void setDatum(Date datum2) {
		this.datum = datum2;
	}

	public long getRazlika_vremena() {
		return razlika_vremena;
	}

	public void setRazlika_vremena(long razlika_vremena) {
		this.razlika_vremena = razlika_vremena;
	}
	
	
}
