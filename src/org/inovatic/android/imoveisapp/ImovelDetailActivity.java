package org.inovatic.android.imoveisapp;

import org.inovatic.android.imoveisapp.bd.DatabaseHandler;
import org.inovatic.android.imoveisapp.bd.dao.ImovelDao;
import org.inovatic.android.imoveisapp.model.Imovel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class ImovelDetailActivity extends ActionBarActivity {
	
	public static final String EXTRA_IMOVEL_ID = "imovel_id";
	
	private TextView nomeTextView;
	private TextView descricaoTextView;
	private TextView tipoTextView;
	private TextView precoTextView;
	
	private Imovel imovel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imovel_detail);
		
		nomeTextView = (TextView)findViewById(R.id.textView1);
		descricaoTextView = (TextView)findViewById(R.id.textView4);
		tipoTextView = (TextView)findViewById(R.id.textView3);
		precoTextView = (TextView)findViewById(R.id.textView5);
		
		Intent i = getIntent();
		if (i.hasExtra(EXTRA_IMOVEL_ID)) {
			populateInterface(i.getLongExtra(EXTRA_IMOVEL_ID, 0));
		}
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startMaps(imovel.latitude, imovel.longitude);
			}
		});
	}
	
	private void startMaps(float latitude, float longitude) {
		Intent intent = new Intent(Intent.ACTION_VIEW, 
				Uri.parse("http://maps.google.com/maps?daddr="
						+ latitude + ","
						+ longitude));
		startActivity(intent);
	}

	private void populateInterface(long id) {
		imovel = queryImovelFromDatabase(id);
		
		nomeTextView.setText(imovel.nome);
		descricaoTextView.setText(imovel.descricao);
		tipoTextView.setText(imovel.tipo.nome);
		precoTextView.setText(imovel.preco + "");
	}

	private Imovel queryImovelFromDatabase(long id) {
		DatabaseHandler dbHandler = new DatabaseHandler(this);
		ImovelDao dao = new ImovelDao(dbHandler);
		Imovel result = dao.queryById(id);
		dbHandler.close();
		return result;
	}

}
