package org.inovatic.android.imoveisapp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.model.Imovel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class ImovelMapActivity extends ActionBarActivity {
	
	private GoogleMap map;
	
	private Map<String, Long> markerImovelMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imovel_map);
		
		SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map);
		
		map = fragment.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent i = new Intent(ImovelMapActivity.this, ImovelDetailActivity.class);
				i.putExtra(ImovelDetailActivity.EXTRA_IMOVEL_ID, markerImovelMap.get(marker.getId()));
				startActivity(i);
			}
		});
		
		populateMap();
	}

	private void populateMap() {
		List<Imovel> imoveis = queryImoveisFromDatabase();
		
		markerImovelMap = new HashMap<>();
		
		for (Imovel imovel: imoveis) {
			MarkerOptions options = new MarkerOptions()
				.title(imovel.nome)
				.snippet(imovel.descricao)
				.position(new LatLng(imovel.latitude, imovel.longitude));
			
			Marker marker = map.addMarker(options);
			markerImovelMap.put(marker.getId(), imovel.id);
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
