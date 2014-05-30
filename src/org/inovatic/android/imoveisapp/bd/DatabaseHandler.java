package org.inovatic.android.imoveisapp.bd;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int VERSION = 2;

	public DatabaseHandler(Context context) {
		super(context, "imoveis_db", null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE tipo ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nome TEXT);");
		
		db.execSQL("CREATE TABLE imovel ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "nome TEXT,"
				+ "descricao TEXT,"
				+ "preco INTEGER,"
				+ "latitude REAL,"
				+ "longitude REAL,"
				+ "tipo_id INTEGER REFERENCES tipo (_id));");
		
		populateDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int older, int newer) {
		db.execSQL("DROP TABLE IF EXISTS tipo;");
		db.execSQL("DROP TABLE IF EXISTS imovel;");
		
		onCreate(db);
	}
	
	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}
	
	private void populateDatabase(SQLiteDatabase db) {
		ContentValues values1 = new ContentValues();
		values1.put("nome", "Casa");
		
		ContentValues values2 = new ContentValues();
		values2.put("nome", "Apartamento");
		
		long id1 = db.insert("tipo", null, values1);
		long id2 = db.insert("tipo", null, values2);
		
		createImovel1(db, id1);
		createImovel2(db, id2);
	}
	
	private void createImovel1(SQLiteDatabase db, long tipoId) {
		ContentValues values = new ContentValues();
		values.put("nome", "Casa grande super legal");
		values.put("descricao", "10 quartos, 1 banheiro, jardim, piscina, etecéteras e mais, etecéteras.");
		values.put("preco", 1000000);
		values.put("latitude", -25.481634f);
		values.put("longitude", -54.546960f);
		values.put("tipo_id", tipoId);
		
		db.insert("imovel", null, values);
	}
	
	private void createImovel2(SQLiteDatabase db, long tipoId) {
		ContentValues values = new ContentValues();
		values.put("nome", "Ap. no edifício golden deluxe dourado.");
		values.put("descricao", "lala dlasd asdka jsdjkalhd ejuhi usgd fajsdbh ewdhygf aksdba wdega iduas daie.");
		values.put("preco", 12345);
		values.put("latitude", -25.493062f);
		values.put("longitude", -54.566015f);
		values.put("tipo_id", tipoId);
		
		db.insert("imovel", null, values);
	}

}
