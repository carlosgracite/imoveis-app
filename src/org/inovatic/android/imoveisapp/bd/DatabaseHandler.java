package org.inovatic.android.imoveisapp.bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;

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
				+ "tipo_id INTEGER REFERENCES tipo (_id));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int older, int newer) {
		db.execSQL("DROP tipo IF EXISTS;");
		db.execSQL("DROP imovel IF EXISTS;");
		
		onCreate(db);
	}

}
