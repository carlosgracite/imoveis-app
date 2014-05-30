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

/**
 * Activity responsável por exibir um mapa contendo os marcadores que 
 * que representam todos os imóveis armazenados no banco de dados.
 * Esses marcadores são posicionados no mapa de acordo com as suas
 * coordenadas geográficas (laitude e longitude).
 * 
 * Se um marcador for selecionado, é exibida uma pequena janela 
 * (InfoWindow) contendo o nome e a descrição do imóvel situado 
 * naquele local.
 * 
 * @author carlosgracite
 *
 */
public class ImovelMapActivity extends ActionBarActivity {
	
	private GoogleMap map;
	
	/**
	 * Map utilizado para que se possa saber qual imóvel está vinculado
	 * a cada marcador.
	 * 
	 * A chave é o id do marcador e o valor é o id do imóvel.
	 */
	private Map<String, Long> markerImovelMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imovel_map);
		
		SupportMapFragment fragment = (SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map);
		
		map = fragment.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		map.setMyLocationEnabled(true);  // apresenta localização atual do usuário no mapa.
		
		// Adiciona listener que é acionado quando uma InfoWindow é pressionada.
		// Isso inicia a activity que contém os detalhes do imóvel correspondente.
		map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			
			@Override
			public void onInfoWindowClick(Marker marker) {
				Intent i = new Intent(ImovelMapActivity.this, ImovelDetailActivity.class);
				
				// 'passa um argumento' à activity contendo o id do imóvel que
				// se deseja visualizar od detalhes.
				i.putExtra(ImovelDetailActivity.EXTRA_IMOVEL_ID, 
						markerImovelMap.get(marker.getId()));
				
				startActivity(i);
			}
		});
		
		populateMap();
	}

	/**
	 * Posiciona os marcadores no mapa com base nas coordenadas
	 * geográficas dos imóveis armazenados no banco de dados.
	 */
	private void populateMap() {
		List<Imovel> imoveis = queryImoveisFromDatabase();
		
		markerImovelMap = new HashMap<>();
		
		for (Imovel imovel: imoveis) {
			// Configuração do marcador a ser adicionado no mapa.
			MarkerOptions options = new MarkerOptions()
				.title(imovel.nome)
				.snippet(imovel.descricao)
				.position(new LatLng(imovel.latitude, imovel.longitude));
			
			// Adiciona o marcador no mapa com as configurações definidas
			// anteriormente.
			Marker marker = map.addMarker(options);
			
			// "Vincula" o id do marcador ao do imóvel. Isso é necessário 
			// para poder saber qual imóvel foi selecionado se houver um 
			// evento de click no marcador ou na InfoWindow dele.
			markerImovelMap.put(marker.getId(), imovel.id);
		}
	}

	/**
	 * Recupera todos os imóveis do banco de dados.
	 * @return
	 */
	private List<Imovel> queryImoveisFromDatabase() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao dao = new ImovelDao(dbHandler);
		List<Imovel> result = dao.queryAll();
		dbHandler.close();
		return result;
	}

}
