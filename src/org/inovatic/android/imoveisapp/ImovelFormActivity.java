package org.inovatic.android.imoveisapp;

import java.util.List;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.bd.dao.TipoDao;
import org.inovatic.android.imoveisapp.model.Imovel;
import org.inovatic.android.imoveisapp.model.Tipo;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Activity que contém o formulário de inserção de um novo imóvel
 * no banco de dados.
 * 
 * Caso um Imóvel tenha sido inserido com sucesso, a activity será finalizada
 * com o resultCode de valor RESULT_OK. Isso é feito para que a activity que a 
 * iniciou possa saber disso.
 * 
 * Não é realizada nenhuma validação dos dados inseridos pelo usuário,
 * portanto qualquer informação inserida incorretamente pode fazer a 
 * aplicação explodir.
 * 
 * A realização das validações fica como tarefa.
 * 
 * Uma outra limitação desse formulário é que ele não permite que o usuário
 * insira as coordenadas geográficas do imóvel. Para isso, bastaria adicionar 
 * dois EditTexts à interface e vinculá-los aos atributos 'latitude' e 
 * 'longitude' do imóvel. Uma alternativa ainda mais legal, seria adicionar 
 * um botão que iniciasse uma activity com um mapa e possibilitasse a definição
 * das coordenadas do imóvel pelo próprio mapa, retornando a latitude e longitude
 * como resultado. Isso também fica para os mais aventureiros.
 * 
 * @author carlosgracite
 *
 */
public class ImovelFormActivity extends ActionBarActivity 
	implements View.OnClickListener {
	
	/* Views da interface. */
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
		
		View cancelButton = findViewById(R.id.button2);
		cancelButton.setOnClickListener(this);
		
		populateSpinner();
	}

	/**
	 * Preenche o spinner que permite ao usuário a definição
	 * do tipo de imóvel a ser inserido.
	 */
	private void populateSpinner() {
		List<Tipo> tipos = queryAllTiposFromDatabase();
		
		ArrayAdapter<Tipo> adapter = new ArrayAdapter<>(this, 
				android.R.layout.simple_spinner_item, tipos);
		
		adapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		
		tiposSpinner.setAdapter(adapter);
		
	}

	/**
	 * Recupera todos os Tipos de imóveis (apartamento, casa, etc.) 
	 * armazenados no banco de dados.
	 * @return
	 */
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
		case R.id.button1: // botão 'Salvar'.
			save();
			break;
		case R.id.button2: // botão 'Cancelar'. Apenas finaliza a activity
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * Salva um imóvel no banco de dados com base nos dados
	 * inseridos pelo usuário.
	 * 
	 * ATENÇÃO: Os dados não são validados. Portanto, qualquer
	 * campo inserido de forma incorreta pode acarretar em algum
	 * erro no aplicativo.
	 * 
	 * Efetuar a validação fica como exercício. =)
	 */
	private void save() {
		Imovel imovel = buildImovel();
		
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao dao = new ImovelDao(dbHandler);
		long id = dao.insert(imovel);
		dbHandler.close();
		
		// Se o imóvel foi inserido corretamente, apresenta uma mensagem
		// notificando o usuário e finaliza a activity, definido o resultado
		// como RESULT_OK.
		if (id > 0) {
			Toast.makeText(this, "O imóvel foi inserido com sucesso.", 
					Toast.LENGTH_SHORT).show();
			setResult(RESULT_OK);
			finish();
		} else {
			Toast.makeText(this, "Erro ao inserir imóvel.", 
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Constrói um objeto Imóvel a partir dos dados armazenados
	 * nas Views.
	 * @return
	 */
	private Imovel buildImovel() {
		Imovel imovel = new Imovel();
		
		imovel.nome = nomeEditText.getText().toString();
		imovel.descricao = descricaoEditText.getText().toString();
		imovel.preco = Integer.parseInt(precoEditText.getText().toString());
		imovel.tipo = (Tipo)tiposSpinner.getSelectedItem();
		
		return imovel;
	}

}
