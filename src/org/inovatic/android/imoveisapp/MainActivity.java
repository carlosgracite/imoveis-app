package org.inovatic.android.imoveisapp;

import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.model.Imovel;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Activity inicial, onde é apresentada uma lista com todos imóveis e
 * os botões para visualização de mapa e criação de ímovel.
 * 
 * @author carlosgracite
 */
public class MainActivity extends ActionBarActivity 
		implements AdapterView.OnItemClickListener {

	public static final int REQUEST_CREATE_IMOVEL = 1;
	
	/**
	 * View contendo a lista de todos os imóveis.
	 */
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView)findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		
		// Botão que inicia a activity com o formulário de inserção de imóvel.
		// Neto que foi utilizado o método startActivityForResult. Desta forma,
		// estamos iniciando a activity, mas esperando um resultado, que será
		// retornado no momento em que ela for finalizada. Esse resultado é tratado 
		// no método onActivityResult implementado lá embaixo.
		// Neste caso, estamos iniciando a activity ImovelFormActivity, com uma
		// 'requisição' de inserção de imóvel.
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ImovelFormActivity.class);
				startActivityForResult(i, REQUEST_CREATE_IMOVEL);
			}
		});
		
		// Botão que, se pressionado, inicia uma activity com o mapa contendo
		// todos os imóveis em suas coordenadas geográficas correspondentes.
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ImovelMapActivity.class);
				startActivity(i);
			}
		});
		
		populateList();
	}

	/**
	 * Recupera todos os imóveis que estão armazenados no bando de dados e os
	 * adiciona ao ListView.
	 */
	private void populateList() {
		listView.setAdapter(new ArrayAdapter<>(this, 
				android.R.layout.simple_list_item_1, queryImoveisFromDatabase()));
	}

	/**
	 * Recupera todos os imóveis armazenados no banco de dados. 
	 * @return
	 */
	private List<Imovel> queryImoveisFromDatabase() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao imovelDao = new ImovelDao(dbHandler);
		List<Imovel> result = imovelDao.queryAll();
		dbHandler.close();
		return result;
	}
	
	/**
	 * Este método é chamado sempre que uma activity iniciada por esta 
	 * é finalizada.
	 * Neste caso, se a activity ImovelFormActivity for finalizado com um 
	 * resultCode de valor 'RESULT_OK' e tiver sido iniciada com o requestCode
	 * 'REQUEST_CREATE_IMOVEL', a lista de imóveis será atualizada, pois a
	 * inserção de um imóvel foi realizada com sucesso.
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CREATE_IMOVEL) {
			if (resultCode == RESULT_OK) {
				populateList();
			}
		}
	}
	
	/**
	 * Método chamado quando algum item do listview for selecionado.
	 * Caso isso ocorra, é iniciada a activity com os detalhes do imóvel
	 * escolhido. Para isso, é passado o id do imóvel junto com o intent
	 * para que a ImovelDetailActivity saiba a qual a operação se refere.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent i = new Intent(this, ImovelDetailActivity.class);
		i.putExtra(ImovelDetailActivity.EXTRA_IMOVEL_ID, 
				((Imovel)listView.getItemAtPosition(position)).id);
		startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
