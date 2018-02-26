package util;

public class Aresta {
	
	private String empresa;
	private Vertice destino, origem;
	private int poltronas;
	
	public Aresta(String empresa, Vertice destino, Vertice origem) {
		this.empresa = empresa;
		this.destino = destino;
		this.poltronas = 2;
		this.origem = origem;
	}

	public String getEmpresa() {
		return empresa;
	}

	public Vertice getDestino() {
		return destino;
	}

	public int getPoltronas() {
		return poltronas;
	}

	public void setPoltronas(int poltronas) {
		this.poltronas = poltronas;
	}
	public Vertice getOrigem() {
		return origem;
	}

	public void setOrigem(Vertice origem) {
		this.origem = origem;
	}
	
	public void addPoltrona(){
		this.poltronas ++;
	}
	
	public void buyPoltrona(){
		this.poltronas --;
	}
	
		
	

}
