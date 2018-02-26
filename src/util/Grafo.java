package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Grafo {

	private LinkedList<Vertice> vertices;
	private LeitorArq leitorArq;
	private String txtOriginal, empresa, arq;
	

	//Inicia a lsita de vertice do grafo e o objeto da leitura de arquivo
	public Grafo() {
		vertices = new LinkedList<>();
		leitorArq = new LeitorArq();
	}

	//Gera o grafo
	public void geraGrafo() {
		//le o arquivo
		String aux = leitorArq.lerarq(arq).trim();

		//Salva nessa variável pra ser usada na atualização dos grafos dos outros servidores
		txtOriginal = aux;

		//Quebra o texto lido em dois - Nome dos vertices/arestas dos vertices
		String[] aux2 = aux.split("@");

		//Separa o nome dos vertices 
		String[] listVertice = aux2[0].split(";");

		//Percorre o vetor dos vertices adicionando na lista
		for (int i = 0; i < listVertice.length; i++) {
			Vertice v = new Vertice(listVertice[i]);
			vertices.add(v);
		}

		//Método pra impressão da lista (contendo apenas vertices
		System.out.println("Imprimindo lista");
		for (Iterator iterator = vertices.iterator(); iterator.hasNext();) {
			Vertice vertice = (Vertice) iterator.next();
			System.out.println(vertice.getNome());

		}

		//Separa as arestas
		String[] lsitAdj = aux2[1].split(";");

		//print aleatório
		System.out.println("Adjacencias");

		//percorre a lista de vertices
		for (int i = 0; i < lsitAdj.length; i++) {
			//separa origem e destino das arestas
			String[] aux4 = lsitAdj[i].split("-");
			//Busca na lista de vertices se há ocorrencia daquele vertice
			int indice = busca(aux4[0]);
			//retorna isso se ja existir
			if (indice == -1) {
				//não pensei em nenhum tratamento ainda
				System.out.println("Naõ existe");
				break;
			} else {
				//se não existir o verticie
				//ele cria o vértice com o nome 
				
				int id2 = 0;
				
				id2 = busca(aux4[1]);
				Vertice b = vertices.get(id2);
				//Cria a areasta com a empresa dona do trecho e o vertice de DESTINO
				
				//Aresta a = new Aresta(empresa, b, vertices.get(indice));
				
				//adiciona a aresta na lista de arestas daquele vertice
				Vertice v = vertices.get(indice);
				v.addAresta(empresa, b);
				//vertices.get(indice).addAresta(empresa, b);
			}

		}

		//print aleatório de teste
		System.out.println("Vertices - ");
		for (Iterator iterator = vertices.iterator(); iterator.hasNext();) {
			Vertice vertice = (Vertice) iterator.next();
			System.out.println("");
			System.out.println("Adjacencias do vertice - " + vertice.getNome());
			for (Iterator iterator2 = vertice.getArestas().iterator(); iterator2.hasNext();) {
				Aresta aresta = (Aresta) iterator2.next();
				System.out.println(
						"Destino - " + aresta.getDestino().getNome() + " Pela empresa - " + aresta.getEmpresa());
			}
		}

	}

	//método para setar a empresa (utilizado na atualização do grafo da rede)
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
	//método aleatório apenas pra facilitar o teste em localhost
	public void setArq(String arq) {
		this.arq = arq;
	}

	//método que recebe o grafo da rede e a lista de vértices de outro servidor e faz a atualização de vertices
	public void atualizaVerticesGrafo(Grafo g, String[] verti) {

		//código idêntico ao do geraGrafo()
		for (int i = 0; i < verti.length; i++) {
			String string = verti[i];
			if (busca(string) == -1) {
				Vertice x = new Vertice(verti[i]);
				g.getVertices().add(x);
			}
		}

	}

	//Método que recebe o grafo da rede, a string de arestas e a empresa que é dona das arestas
	public void atualizaArestasGrafo(Grafo g, String[] arestas, String empresa) {
		//atribuição que facilita o ctrl+c ctrl+v do código de adjacencias de geraGrafo() XD
		String[] lsitAdj = arestas;

		//print aleatorio
		System.out.println("Adjacencias");

		//atualiza a lsita de arestas de cada vértice
		for (int i = 0; i < lsitAdj.length; i++) {
			String[] aux4 = lsitAdj[i].split("-");
			int indice = busca(aux4[0]);
			if (indice == -1) {
				System.out.println("Naõ existe");
				break;
			} else {
                
				int id2;
				
				id2 = busca(aux4[1]);
				Vertice b = vertices.get(id2);
				
				//Aresta a = new Aresta(empresa, b, vertices.get(indice));
				
				Vertice v = g.getVertices().get(indice);
				v.addAresta(empresa, b);
				//g.getVertices().get(indice).addAresta(empresa, b);
											
				
			}
		}
	}

	//método aleatório para impressão do grafo da rede depois da atualização
	public void imprimeGrafoAtualizado(Grafo g) {
		System.out.println("Vertices - ");
		for (Iterator iterator = g.getVertices().iterator(); iterator.hasNext();) {
			Vertice vertice = (Vertice) iterator.next();
			System.out.println("");
			System.out.println("Adjacencias do vertice - " + vertice.getNome());
			for (Iterator iterator2 = vertice.getArestas().iterator(); iterator2.hasNext();) {
				Aresta aresta = (Aresta) iterator2.next();
				System.out.println(
						"Destino - " + aresta.getDestino().getNome() + " Pela empresa - " + aresta.getEmpresa());
			}
		}
	}

	//método de busca da string que gera o grafo
	public String getTxtOriginal() {
		return txtOriginal;
	}

	//mpetodo que retorna a lista de vertiecs
	public LinkedList<Vertice> getVertices() {
		return vertices;
	}

	//método que percorre a lista de vertices em busca de um especifico, se achar retorna o index dele, se não -1
	public int busca(String nome) {
		int cont = 0;
		for (Vertice vertice : vertices) {
			if (vertice.getNome().equals(nome)) {
				return cont;
			}
			cont++;
		}
		return -1;
	}
	
	public String geraStringVertices(){
		String x = "";
		for (Iterator iterator = vertices.iterator(); iterator.hasNext();) {
			Vertice vertice = (Vertice) iterator.next();
//			System.out.println(vertice.getNome());
			x += vertice.getNome()+"#";
		}
		
		return x;
	}
	
	public LinkedList todoCaminho(String origem, String destino){		
		int ori, des;
		LinkedList<Aresta> entrada;
		LinkedList resu;
		entrada = new LinkedList<>();
		resu = new LinkedList();
		ori = busca(origem);
		des = busca(destino);
		
		this.encontraCaminhos(vertices.get(ori), vertices.get(des), entrada, resu);
		
		return resu;
	}
	
	public void encontraCaminhos(Vertice origem, Vertice destino, LinkedList<Aresta> entrada, LinkedList resu){
		
		LinkedList<Aresta> saida = new LinkedList<>();

		for(int i = 0; i < origem.getArestas().size(); i++){
			Aresta ar = origem.getArestas().get(i);
			ar.getOrigem().setVisitado(true);
			if(ar.getDestino().isVisitado() == false){
				ar.getDestino().setVisitado(true);
				
				if(ar.getDestino().getNome().equals(destino.getNome())){
					
					System.out.println("Cheguei 1");
					entrada.addFirst(ar);
					for(int j = 0; j < entrada.size(); j++ ){
						saida.add(entrada.get(j));						
					}
					resu.addFirst(saida);					
				}
				else{
					entrada.addFirst(ar);
					Vertice v = ar.getDestino();
					this.encontraCaminhos(v, destino, entrada, resu);
				}
				
				entrada.removeFirst();
			}
			else{
				boolean tem = false;
				tem = this.estaNaLista(entrada, ar);
				if(tem == false){
					
					if(ar.getDestino().getNome().equals(destino.getNome())){
						entrada.addFirst(ar);
                        for (int j = 0; j < entrada.size(); j++) {
                            saida.add(entrada.get(j));
                        }
                        resu.addFirst(saida);
					}
					else{
						
                        entrada.addFirst(ar);
                        Vertice o = ar.getDestino();
                        this.encontraCaminhos(o, destino, entrada, resu);
					}
					
					entrada.removeFirst();
					
				}
								
			}
			
		}
		
	}
	
	public boolean estaNaLista(LinkedList<Aresta> e, Aresta ar){
		
		if(!e.isEmpty()){
			for(int i = 0; i < e.size(); i++){
				
				Aresta r = e.get(i);
				if(r.getOrigem().getNome().equals(ar.getDestino().getNome())){
					return true;
				}
				
			}
		}
		return false;
	}

}
