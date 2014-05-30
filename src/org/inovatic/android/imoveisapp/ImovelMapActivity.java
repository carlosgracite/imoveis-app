package org.inovatic.android.imoveisapp;

import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.model.Imovel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ImovelMapActivity extends ActionBarActivity {
	
	private GoogleMap map;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imovel_map);
		
		SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map);
		
		map = fragment.getMap();
		
		populateMap();
	}

	private void populateMap() {
		List<Imovel> imoveis = queryImoveisFromDatabase();
		
		for (Imovel imovel: imoveis) {
			MarkerOptions options = new MarkerOptions()
				.title(imovel.nome)
				.snippet(imovel.descricao)
				.position(new LatLng(imovel.latitude, imovel.longitude));
			
			map.addMarker(options);
		}
	}

	private List<Imovel> queryImoveisFromDatabase() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao dao = new ImovelDao(dbHandler);
		List<Imovel> result = dao.queryAll();
		dbHandler.close();
		return result;
	}

}
