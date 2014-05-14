package org.inovatic.android.imoveisapp;

import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.TipoDao;
import org.inovatic.android.imoveisapp.model.Tipo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ImovelFormActivity extends ActionBarActivity {
	
	private EditText nomeEditText;
	private EditText descricaoEditText;
	private EditText precoEditText;
	private Spinner tiposSpinner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imovel_form);
		
		nomeEditText = (EditText)findViewById(R.id.editText1);
		descricaoEditText = (EditText)findViewById(R.id.editText2);
		precoEditText = (EditText)findViewById(R.id.editText3);
		tiposSpinner = (Spinner)findViewById(R.id.spinner1);
		
		populateSpinner();
	}

	private void populateSpinner() {
		List<Tipo> tipos = queryAllTiposFromDatabase();
		
		ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(this, 
				android.R.layout.simple_spinner_item, tipos);
		
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		
		tiposSpinner.setAdapter(adapter);
		
	}

	private List<Tipo> queryAllTiposFromDatabase() {
		DatabaseHandler dbHandler = new DatabaseHandler(this);

		TipoDao dao = new TipoDao(dbHandler);
		List<Tipo> result = dao.queryAll();
		dbHandler.close();
		
		return result;
	}

}
