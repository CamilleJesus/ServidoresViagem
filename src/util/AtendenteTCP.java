package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

import controller.Controller;

public class AtendenteTCP extends Thread {

	private Socket sktCli;
	private ConexaoTCP server;
	private Controller cont;
	private LinkedList listaRecebida;
	ObjectOutputStream oo;
	ObjectInputStream oi;

	public AtendenteTCP(Socket sktCli, ConexaoTCP server, Controller controller) throws IOException {
		this.sktCli = sktCli;
		this.server = server;
		cont = controller;
		listaRecebida = new LinkedList();
		this.oo = new ObjectOutputStream(sktCli.getOutputStream());
		this.oi = new ObjectInputStream(sktCli.getInputStream());
	}

	@Override
	public void run() {
		try {
			// cont = Controller.getInstance();

			cont.imprimeGrafo();

			String x = (String) oi.readObject();
			String[] aux = x.split("@");

			System.out.println("Objetos "+ x);
			System.out.println("Recebeu" + aux[0]);

			if (aux[0].equals("MLT")) {

				// Retorna a lista de lugares disponiveis

				String lista = cont.stringVertices();
				System.out.println("Lista para cliente - " + lista);

				oo.writeObject("L@" + lista);
				oo.flush();
			} else if (aux[0].equals("COD")) {
				// retorna a lista de rotas para aquela origem e destino
				// Tem interação com os servidores
				LinkedList<Aresta> lisA = new LinkedList<>();
				String ret = "PT@";
				listaRecebida = cont.caminhos(aux[1], aux[2]);
				for (int i = 0; i < listaRecebida.size(); i++) {
					LinkedList l = (LinkedList) listaRecebida.get(i);
					String[] auxss;
					int cont = 0;
					for (int j = 0; j < 1; j++) {

						boolean tem = false;
						Aresta are = (Aresta) l.getLast();

						for (int y = 0; y < lisA.size(); y++) {
							Aresta tr = lisA.get(y);
							if ((tr.getOrigem().getNome().equals(are.getOrigem().getNome()))
									&& (tr.getDestino().getNome().equals(are.getDestino().getNome()))
									&& (tr.getEmpresa().equals(are.getEmpresa()))) {
								tem = true;
							}
						}
						if (tem == false) {
							lisA.add(are);
							ret += are.getOrigem().getNome() + ">>" + are.getDestino().getNome() + " ("
									+ are.getEmpresa() + ") " + "#";
						}
					}

				}

				oo.writeObject(ret);
				oo.flush();
			} else if (aux[0].equals("PTOT")) {

				System.out.println("Entrou no PTOT");

				LinkedList<Aresta> lisA = new LinkedList<>();
				String ret = "PLOT@";
				listaRecebida = cont.caminhos(aux[1], aux[2]);

				// Vertice ver;
				// ver = cont.buscaVer(aux[3]);

				for (int i = 0; i < listaRecebida.size(); i++) {

					LinkedList l = (LinkedList) listaRecebida.get(i);
					boolean tem = false;
															
					for (int z = 0; z < l.size(); z++) {

						Aresta areAux = (Aresta) l.get(z);												

						if (areAux.getOrigem().getNome().equals(aux[3])) {

							System.err.println("Tem aresta " +areAux.getOrigem().getNome() +areAux.getDestino().getNome() );
							
							for (int w = 0; w < lisA.size(); w++) {

								Aresta aresEnvio = (Aresta) lisA.get(w);
								
								if ((areAux.getOrigem().getNome().equals(aresEnvio.getOrigem().getNome()))
										&& (areAux.getDestino().getNome().equals(aresEnvio.getDestino().getNome()))
										&& (areAux.getEmpresa().equals(aresEnvio.getEmpresa()))) {
									tem = true;

								}
								
							}
							
							if (tem == false) {

								lisA.add(areAux);

								ret += areAux.getOrigem().getNome() + ">>" + areAux.getDestino().getNome() + " ("	+ areAux.getEmpresa() + ") " + "#";
							}
							
							

						}
						

					}

//					int cont = 0;
//					for (int j = 0; j < 1; j++) {
//
//						boolean tem = false;
//						Aresta are = (Aresta) l.removeLast();
//
//						for (int y = 0; y < lisA.size(); y++) {
//
//							Aresta tr = lisA.get(y);
//
//							if ((tr.getOrigem().getNome().equals(are.getOrigem().getNome()))
//									&& (tr.getDestino().getNome().equals(are.getDestino().getNome()))
//									&& (tr.getEmpresa().equals(are.getEmpresa()))) {
//								tem = true;
//							}
//						}
//						if (tem == false) {
//							lisA.add(are);
//							ret += are.getOrigem().getNome() + ">>" + are.getDestino().getNome() + " ("
//									+ are.getEmpresa() + ") " + "#";
//						}
//
//					}

				}

				//
				// for (int i = 0; i < listaRecebida.size(); i++) {
				// LinkedList l = (LinkedList) listaRecebida.get(i);
				// String[] auxss;
				// int cont = 0;
				// for (int j = 0; j < 1; j++) {
				//
				// boolean tem = false;
				// Aresta are = (Aresta) l.getLast();
				//
				// for (int y = Integer.parseInt(aux[4]); y < lisA.size(); y++)
				// {
				//
				// Aresta tr = lisA.get(y);
				// if
				// ((tr.getOrigem().getNome().equals(are.getOrigem().getNome()))
				// &&
				// (tr.getDestino().getNome().equals(are.getDestino().getNome()))
				// &&(tr.getEmpresa().equals(are.getEmpresa()))) {
				// tem = true;
				// }
				// }
				// if (tem == false) {
				// lisA.add(are);
				// ret += are.getOrigem().getNome() + ">>" +
				// are.getDestino().getNome() + " ("
				// + are.getEmpresa() + ") " + "#";
				// }
				// }
				//
				// }
				
				if (cont.getEmpresa().equals("A") && aux[4].equals("A")) {
					System.out.println("Entrou compra local A");
					boolean foi = cont.compra(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[4].equals("B")) {
					System.out.println("Entrou compra local B");
					boolean foi = cont.compra(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[4].equals("C")) {
					System.out.println("Entrou compra local C");
					boolean foi = cont.compra(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("A") && aux[4].equals("B")) {
					System.out.println("Entrou compra remota A para B");
					boolean foi = cont.getSrvX().compraSrvX(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("A") && aux[4].equals("C")) {
					System.out.println("Entrou compra remota A para C");
					boolean foi = cont.getSrvY().compraSrvY(aux[5], aux[3]);
					System.out.println(foi + " A - C");
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[4].equals("A")) {
					System.out.println("Entrou compra remota B para A");
					boolean foi = cont.getSrvX().compraSrvX(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[4].equals("C")) {
					System.out.println("Entrou compra remota B para C");
					boolean foi = cont.getSrvY().compraSrvY(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[4].equals("A")) {
					System.out.println("Entrou compra remota C para A");
					boolean foi = cont.getSrvX().compraSrvX(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[4].equals("B")) {
					System.out.println("Entrou compra remota C para B");
					boolean foi = cont.getSrvY().compraSrvY(aux[5], aux[3]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}
				
				System.out.println("Tá saindo isso daqui: " + ret);

				
			}else if (aux[0].equals("CTF")) {
				
				String ret = "CF@";
				
				
				if (cont.getEmpresa().equals("A") && aux[3].equals("A")) {
					System.out.println("Entrou compra local A");
					boolean foi = cont.compra(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[3].equals("B")) {
					System.out.println("Entrou compra local B");
					boolean foi = cont.compra(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[3].equals("C")) {
					System.out.println("Entrou compra local C");
					boolean foi = cont.compra(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("A") && aux[3].equals("B")) {
					System.out.println("Entrou compra remota A para B");
					boolean foi = cont.getSrvX().compraSrvX(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("A") && aux[3].equals("C")) {
					System.out.println("Entrou compra remota A para C");
					boolean foi = cont.getSrvY().compraSrvY(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[3].equals("A")) {
					System.out.println("Entrou compra remota B para A");
					boolean foi = cont.getSrvX().compraSrvX(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("B") && aux[3].equals("C")) {
					System.out.println("Entrou compra remota B para C");
					boolean foi = cont.getSrvY().compraSrvY(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[3].equals("A")) {
					System.out.println("Entrou compra remota C para A");
					boolean foi = cont.getSrvX().compraSrvX(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}else if (cont.getEmpresa().equals("C") && aux[3].equals("B")) {
					System.out.println("Entrou compra remota C para B");
					boolean foi = cont.getSrvY().compraSrvY(aux[1], aux[2]);
					if (foi) {
						oo.writeObject(ret);
						oo.flush();
					}else{
						oo.writeObject("FLS@"+ foi);
						oo.flush();
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}