package util;

import java.util.LinkedList;

public class Vertice {


	private String nome;
	private LinkedList<Aresta> arestas;
	private boolean visitado; 

	public Vertice(String nome) {
		this.visitado = false;
		this.nome = nome;
		arestas = new LinkedList<>();
	}

	public String getNome() {
		return nome;
	}

	public LinkedList<Aresta> getArestas() {
		return arestas;
	}
	public void addAresta(String nome, Vertice destino){
		Aresta a = new Aresta(nome, destino, this);
		arestas.add(a);		
	}

	public boolean isVisitado() {
		return visitado;
	}

	public void setVisitado(boolean visitado) {
		this.visitado = visitado;
	}


}
