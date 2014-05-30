package org.inovatic.android.imoveisapp.bd.dao;

import java.util.ArrayList;
import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.model.Tipo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Classe a ser utilizada para manter os dados de um 
 * Tipo no banco de dados.
 * 
 * @author carlosgracite
 *
 */
public class TipoDao {
	
	private DatabaseHandler dbHandler;
	
	public TipoDao(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	
	public long insert(Tipo tipo) {
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("nome", tipo.nome);
		
		return db.insert("tipo", null, values);
	}
	
	public List<Tipo> queryAll() {
		List<Tipo> result = new ArrayList<>();
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		
		// SELECT * FROM tipo ORDER BY nome;
		Cursor cursor = db.query("tipo", null, null, null, null, null, "nome");
		while (cursor.moveToNext()) {
			Tipo tipo = fromCursor(cursor);
			
			result.add(tipo);
		}
		cursor.close();
		
		return result;
	}

	public Tipo queryById(long id) {
		Tipo result = null;
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		
		String[] selectionArgs = {String.valueOf(id)};
		
		// SELECT * FROM tipo WHERE id=id;
		Cursor c = db.query("tipo", null, "_id=?", selectionArgs, null, null, null);
		if (c.moveToFirst()) {
			result = fromCursor(c);
		}
		c.close();
		
		return result;
		
	}
	
	public int deleteById(long id) {
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		return db.delete("tipo", "_id=?", new String[]{String.valueOf(id)});
	}
	
	public long update(Tipo tipo) {
		ContentValues values = new ContentValues();
		values.put("_id", tipo.id);
		values.put("nome", tipo.nome);
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		
		return db.update("tipo", values, "_id=?", new String[]{String.valueOf(tipo.id)});
	}
	
	private Tipo fromCursor(Cursor cursor) {
		Tipo tipo = new Tipo();
		tipo.id = cursor.getLong(cursor.getColumnIndex("_id"));
		tipo.nome = cursor.getString(cursor.getColumnIndex("nome"));
		return tipo;
	}

}
