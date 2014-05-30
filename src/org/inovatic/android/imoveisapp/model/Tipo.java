package org.inovatic.android.imoveisapp.model;

/**
 * Representação de um tipo de um imóvel (apartamento, 
 * casa, etc.)
 * 
 * @author carlosgracite
 *
 */
public class Tipo {
	
	public long id;
	public String nome;

	@Override
	public String toString() {
		return nome;
	}
}
