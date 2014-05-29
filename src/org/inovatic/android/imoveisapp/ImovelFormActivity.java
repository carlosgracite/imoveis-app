package org.inovatic.android.imoveisapp;

import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.bd.dao.TipoDao;
import org.inovatic.android.imoveisapp.model.Imovel;
import org.inovatic.android.imoveisapp.model.Tipo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ImovelFormActivity extends Activity 
	implements View.OnClickListener {
	
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
		
		View saveButton = findViewById(R.id.button1);
		saveButton.setOnClickListener(this);
		
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

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button1:
			save();
			break;

		default:
			break;
		}
	}

	private void save() {
		Imovel imovel = buildImovel();
		
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao dao = new ImovelDao(dbHandler);
		long id = dao.insert(imovel);
		dbHandler.close();
		
		if (id > 0) {
			Toast.makeText(this, "O imóvel foi inserido com sucesso.", 
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "Erro ao inserir imóvel.", 
					Toast.LENGTH_SHORT).show();
		}
	}

	private Imovel buildImovel() {
		Imovel imovel = new Imovel();
		
		imovel.nome = nomeEditText.getText().toString();
		imovel.descricao = descricaoEditText.getText().toString();
		imovel.preco = Integer.parseInt(precoEditText.getText().toString());
		imovel.tipo = (Tipo)tiposSpinner.getSelectedItem();
		
		return imovel;
	}

}
