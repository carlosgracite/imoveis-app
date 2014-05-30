package org.inovatic.android.imoveisapp.bd.dao;

import java.util.ArrayList;
import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.model.Imovel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ImovelDao {
	
	private DatabaseHandler dbHandler;
	
	public ImovelDao(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}
	
	public long insert(Imovel imovel) {
		SQLiteDatabase db = dbHandler.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("nome", imovel.nome);
		values.put("descricao", imovel.descricao);
		values.put("preco", imovel.preco);
		values.put("latitude", imovel.latitude);
		values.put("longitude", imovel.longitude);
		values.put("tipo_id", imovel.tipo.id);
		
		return db.insert("imovel", null, values);
	}
	
	public List<Imovel> queryAll() {
		List<Imovel> result = new ArrayList<>();
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		
		TipoDao tipoDao = new TipoDao(dbHandler);
		
		Cursor cursor = db.query("imovel", null, null, null, null, null, null);
		
		while (cursor.moveToNext()) {
			Imovel imovel = fromCursor(cursor);
			imovel.tipo = tipoDao.queryById(cursor.getLong(cursor.getColumnIndex("tipo_id")));
			
			result.add(imovel);
		}
		cursor.close();
		
		return result;
	}

	public Imovel queryById(long id) {
		Imovel result = null;
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		TipoDao tipoDao = new TipoDao(dbHandler);
		
		Cursor cursor = db.query("imovel", null, "_id=?", new String[]{String.valueOf(id)}, null, null, null);
		if (cursor.moveToFirst()) {
			result = fromCursor(cursor);
			result.tipo = tipoDao.queryById(cursor.getLong(cursor.getColumnIndex("tipo_id")));
		}
		
		return result;
	}
	
	public int deleteById(long id) {
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		return db.delete("imovel", "_id=?", new String[]{String.valueOf(id)});
	}
	
	public long update(Imovel imovel) {
		ContentValues values = new ContentValues();
		values.put("_id", imovel.id);
		values.put("nome", imovel.nome);
		values.put("descricao", imovel.descricao);
		values.put("preco", imovel.preco);
		values.put("latitude", imovel.latitude);
		values.put("longitude", imovel.longitude);
		values.put("tipo_id", imovel.tipo.id);
		
		SQLiteDatabase db = dbHandler.getReadableDatabase();
		
		return db.update("imovel", values, "_id=?", new String[]{String.valueOf(imovel.id)});
	}
	
	private Imovel fromCursor(Cursor cursor) {
		Imovel imovel = new Imovel();
		imovel.id = cursor.getLong(cursor.getColumnIndex("_id"));
		imovel.nome = cursor.getString(cursor.getColumnIndex("nome"));
		imovel.descricao = cursor.getString(cursor.getColumnIndex("descricao"));
		imovel.preco = cursor.getInt(cursor.getColumnIndex("preco"));
		imovel.latitude = cursor.getFloat(cursor.getColumnIndex("latitude"));
		imovel.longitude = cursor.getFloat(cursor.getColumnIndex("longitude"));
		return imovel;
	}

}
