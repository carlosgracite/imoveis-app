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

public class MainActivity extends ActionBarActivity 
		implements AdapterView.OnItemClickListener {

	public static final int REQUEST_CREATE_IMOVEL = 1;
	
	private ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		listView = (ListView)findViewById(R.id.listView1);
		listView.setOnItemClickListener(this);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ImovelFormActivity.class);
				startActivityForResult(i, REQUEST_CREATE_IMOVEL);
			}
		});
		
		findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, ImovelMapActivity.class);
				startActivity(i);
			}
		});
		
		populateList();
	}

	private void populateList() {
		listView.setAdapter(new ArrayAdapter<>(this, 
				android.R.layout.simple_list_item_1, queryImoveisFromDatabase()));
	}

	private List<Imovel> queryImoveisFromDatabase() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao imovelDao = new ImovelDao(dbHandler);
		List<Imovel> result = imovelDao.queryAll();
		dbHandler.close();
		return result;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CREATE_IMOVEL) {
			if (resultCode == RESULT_OK) {
				populateList();
			}
		}
	}
	
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
