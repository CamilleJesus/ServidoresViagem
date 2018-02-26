package controller;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.LinkedList;

import util.Aresta;
import util.ConexaoTCP;
import util.Grafo;
import util.IntRmi;
import util.Vertice;

public class Controller extends UnicastRemoteObject implements IntRmi {

	private String nome, empresa;
	private IntRmi srvX, srvY;
	private int porta;
	private static Controller instancia;
	private Grafo g, rede;
	private ConexaoTCP con;
	
	// Aqui se tu quiser testar com mais de um servidor precisa ir
	// comentando/descomentando cada bloco abaixo
	public Controller() throws RemoteException {
		super();

		g = new Grafo();
		rede = new Grafo();

		// Pra rodar o servidor 1 descomenta esse bloco:

//		 nome = "SRV1";
//		 porta = 1099;
//		 g.setEmpresa("A");
//		 empresa = "A";
//		 g.setArq("teste.txt");
//		 con = new ConexaoTCP(5000, this);
//		 con.start();

		// Pra rodar o servidor 2 descomenta esse bloco:

//		 nome = "SRV2";
//		 porta = 1098;
//		 g.setEmpresa("B");
//		 empresa = "B";
//		 g.setArq("teste2.txt");
//		 con = new ConexaoTCP(5001, this);
//		 con.start();

		// Pra rodar o servidor 3 descomenta esse bloco:

		 nome = "SRV3";
		 porta = 1099;
		 g.setEmpresa("C");
		 empresa = "C";
		 g.setArq("teste3.txt");
		 con = new ConexaoTCP(5002, this);
		 con.start();

	}

	// m�todo singleton (ainda acho que n�o vai usar)
	public static synchronized Controller getInstance() throws RemoteException {
		if (instancia == null) {
			instancia = new Controller();
		}

		return instancia;
	}

	// M�todo que faz o registro do servidor no RMI
	public void iniciar(Controller controller) {
		try {
			LocateRegistry.createRegistry(porta);
			Naming.rebind(nome, controller);
			System.err.println("Nasci :), sou o " + nome);
		} catch (MalformedURLException | RemoteException e) {
			System.err.println("Erro: ");
			e.printStackTrace();
		}
	}

	// M�todo que retorna qual servidor � (SRV1, SRV2, SRV3)
	public String getNome() {
		return nome;
	}
	
	

	// public String teste(){
	// try {
	// return srvX.pegaNome();
	// } catch (RemoteException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// System.out.println("erro no pega nome");
	// }
	// return "falha";
	// }

	// m�todo que cria o objeto RMI do servidor X
	@Override
	public void conectarSrvX(String nome) throws RemoteException {
		try {
			String url = "rmi://192.168.43.65/" + nome;
			System.out.println(url);
			srvX = (IntRmi) Naming.lookup(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// m�todo que cria o objeto RMI do servidor X
	@Override
	public void conectarSrvY(String nome) throws RemoteException {
		try {
			String url = "rmi://192.168.43.134/" + nome;
			System.out.println(url);
			srvY = (IntRmi) Naming.lookup(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// m�todo que cria o grafo interno e faz o grafo da rede
	public void geraGrafo() {
		g.geraGrafo();
		rede = g;
	}

	public void imprimeGrafo() {
		System.err.println("Imprimindo o grafo");
		for (Iterator iterator = g.getVertices().iterator(); iterator.hasNext();) {
			Vertice type = (Vertice) iterator.next();
			System.out.println(type.getNome());
		}
	}

	// m�todo que sincroniza o grafo do servidor X com o da rede (interno)
	@Override
	public void sincronizarSrvX(String txt, String empresa) throws RemoteException {
		System.out.println("txt - " + txt);
		System.out.println("Empresa -" + empresa);
		// Splita a string que recebe do outro servidor
		String[] aux = txt.trim().split("@");
		// sepra os vertices e arestas
		String[] vertices = aux[0].split(";");
		// atualiza os vertices
		rede.atualizaVerticesGrafo(rede, vertices);
		// separa as arestas
		String[] arestas = aux[1].split(";");
		// atualiza as arestas
		rede.atualizaArestasGrafo(rede, arestas, empresa);
		// imprime o grafo atualizado
		rede.imprimeGrafoAtualizado(rede);

	}

	// m�todo que sincroniza o grafo do servidor Y com o da rede (interno)
	// re-l� os mesmos comentarios internos do metodo acima, to com pregui�a de
	// digitar de novo XD
	@Override
	public void sincronizarSrvY(String txt, String empresa) throws RemoteException {
		System.out.println("txt - " + txt);
		System.out.println("Empresa -" + empresa);
		String[] aux = txt.trim().split("@");
		String[] vertices = aux[0].split(";");
		rede.atualizaVerticesGrafo(rede, vertices);
		String[] arestas = aux[1].split(";");
		rede.atualizaArestasGrafo(rede, arestas, empresa);

		rede.imprimeGrafoAtualizado(rede);

	}

	// M�todo que prepra a string que gera o grafo interno para mandar para
	// outros servidores
	public String preparaSync(String empresa) {
		System.out.println(g.getTxtOriginal());
		String txt = empresa + "!" + g.getTxtOriginal();
		return txt;
	}

	// m�todo que retorna o objeto RMI do servidor X
	public IntRmi getSrvX() {
		return srvX;
	}

	// m�todo que retorna o objeto RMI do servidor Y
	public IntRmi getSrvY() {
		return srvY;
	}

	public String stringVertices() {
		if (srvX != null) {
			return rede.geraStringVertices();
		}
		return g.geraStringVertices();
	}

	public LinkedList caminhos(String origem, String destino) {
		LinkedList l = rede.todoCaminho(origem, destino);
		// System.err.println("tamanho da lista " + l.size());
		//
		// for (Iterator iterator = l.iterator(); iterator.hasNext();) {
		// LinkedList object = (LinkedList) iterator.next();
		// System.out.println("\nCaminho ");
		// for (Iterator iterator2 = object.iterator(); iterator2.hasNext();) {
		// Aresta object2 = (Aresta) iterator2.next();
		//
		//
		// System.out.println(object2.getOrigem().getNome() + " >> "
		// +object2.getDestino().getNome());
		//
		//
		// }
		// }
		return l;
	}

	public Vertice buscaVer(String nome) {
		Vertice ver;
		int n;

		n = g.busca(nome);
		ver = g.getVertices().get(n);

		return ver;
	}

	public String getEmpresa() {
		return empresa;
	}
	
	public synchronized boolean compra(String vertice, String destAresta){
		System.out.println("Compra local");
		for (Iterator iterator = g.getVertices().iterator(); iterator.hasNext();) {
			Vertice vert = (Vertice) iterator.next();
			if (vert.getNome().equals(vertice)) {
				System.out.println("Achou o vertice -" + vert.getNome());
				for (Iterator iterator2 = vert.getArestas().iterator(); iterator2.hasNext();) {
					System.out.println("entrou no for de arestas");
					System.out.println("to procurando esse - "+ destAresta);
					Aresta aresta = (Aresta) iterator2.next();
					if (aresta.getDestino().getNome().equals(destAresta)) {
						System.out.println("Achou a aresta -"+ aresta.getDestino().getNome());
						System.out.println("Num de poltronas - "+ aresta.getPoltronas());
						if (aresta.getPoltronas() > 0) {
							aresta.buyPoltrona();
							return true;
						} else if (aresta.getPoltronas() == 0) {
							System.out.println("N�o pode vender");
							return false;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean compraSrvX(String verticeI, String destArestaI) throws RemoteException {
		System.out.println("Compra remota X");
		return compra(verticeI, destArestaI);
	}

	@Override
	public boolean compraSrvY(String verticeI, String destArestaI) throws RemoteException {
		System.out.println("Compra remota Y");
		return compra(verticeI, destArestaI);

	}

	// @Override
	// public String pegaNome() throws RemoteException {
	// // TODO Auto-generated method stub
	// return nome;
	// }

}
