package util;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IntRmi extends Remote{
	
//	public String pegaNome() throws RemoteException;
	public void conectarSrvX(String nome) throws RemoteException;
	public void sincronizarSrvX(String txt, String empresa) throws RemoteException;
	public void conectarSrvY(String nome) throws RemoteException;
	public void sincronizarSrvY(String txt, String empresa) throws RemoteException;
	public boolean compraSrvX(String vertice, String destAresta) throws RemoteException;
	public boolean compraSrvY(String vertice, String destAresta) throws RemoteException;
}
