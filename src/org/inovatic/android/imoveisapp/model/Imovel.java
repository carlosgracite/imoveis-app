package org.inovatic.android.imoveisapp.model;

/**
 * Representação de um imóvel.
 * 
 * @author carlosgracite
 *
 */
public class Imovel {
	
	public long id;
	public String nome;
	public String descricao;
	public int preco;
	public float latitude;
	public float longitude;
	public Tipo tipo;
	
	@Override
	public String toString() {
		return nome;
	}

}
