package view;

import java.awt.EventQueue;

import javax.swing.JFrame;

import controller.Controller;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;

public class TelaSrv {

	private JFrame frame;
	private Controller controller;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaSrv window = new TelaSrv();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TelaSrv() {
		try {
			initialize();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws RemoteException
	 */
	private void initialize() throws RemoteException {

		controller = new Controller();

		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btnStartRmi = new JButton("Start RMI");
		btnStartRmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.iniciar(controller);
			}
		});
		btnStartRmi.setBounds(58, 34, 95, 23);
		frame.getContentPane().add(btnStartRmi);

		JButton btnSrvX = new JButton("SRV X");
		btnSrvX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (controller.getNome().equals("SRV1")) {
					try {
						controller.conectarSrvX("SRV2");
						// System.out.println(controller.teste());
						btnSrvX.setEnabled(false);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e.printStackTrace();
					}
				} else if (controller.getNome().equals("SRV2")) {
					try {
						controller.conectarSrvX("SRV1");
						// System.out.println(controller.teste());
						btnSrvX.setEnabled(false);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e.printStackTrace();
					}

				} else if (controller.getNome().equals("SRV3")) {
					try {
						controller.conectarSrvX("SRV1");
						// System.out.println(controller.teste());
						btnSrvX.setEnabled(false);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e.printStackTrace();
					}

				}
			}
		});
		btnSrvX.setBounds(58, 88, 95, 23);
		frame.getContentPane().add(btnSrvX);

		JButton btnSrvY = new JButton("SRV Y");
		btnSrvY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (controller.getNome().equals("SRV1")) {
					try {
						controller.conectarSrvY("SRV3");
						// System.out.println(controller.teste());
						btnSrvY.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e1.printStackTrace();
					}
				} else if (controller.getNome().equals("SRV2")) {
					try {
						controller.conectarSrvY("SRV3");
						// System.out.println(controller.teste());
						btnSrvY.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e1.printStackTrace();
					}

				} else if (controller.getNome().equals("SRV3")) {
					try {
						controller.conectarSrvY("SRV2");
						// System.out.println(controller.teste());
						btnSrvY.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						System.out.println("Erro no lookup svrx");
						e1.printStackTrace();
					}

				}

			}
		});
		btnSrvY.setBounds(173, 88, 95, 23);
		frame.getContentPane().add(btnSrvY);

		JButton btnGerarGrafo = new JButton("Gerar Grafo");
		btnGerarGrafo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				controller.geraGrafo();
			}
		});
		btnGerarGrafo.setBounds(291, 34, 103, 23);
		frame.getContentPane().add(btnGerarGrafo);

		JButton btnNewButton = new JButton("Abrir Arquivo");
		btnNewButton.setBounds(163, 34, 118, 23);
		frame.getContentPane().add(btnNewButton);

		JButton btnSyncSrvx = new JButton("Sync SrvX");
		btnSyncSrvx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (controller.getNome().equals("SRV1")) {
					String aux = controller.preparaSync("A");
					System.out.println(aux);
					String[] aux2 = aux.split("!");
					try {
						controller.getSrvX().sincronizarSrvX(aux2[1], aux2[0]);
						btnSyncSrvx.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				} else if (controller.getNome().equals("SRV2")) {
					String[] aux = controller.preparaSync("B").split("!");
					try {
						controller.getSrvX().sincronizarSrvX(aux[1], aux[0]);
						btnSyncSrvx.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				} else if (controller.getNome().equals("SRV3")) {
					String[] aux = controller.preparaSync("C").split("!");
					try {
						controller.getSrvX().sincronizarSrvX(aux[1], aux[0]);
						btnSyncSrvx.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				}

			}
		});
		btnSyncSrvx.setBounds(58, 122, 95, 23);
		frame.getContentPane().add(btnSyncSrvx);

		JButton btnSyncSrvy = new JButton("Sync SrvY");
		btnSyncSrvy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (controller.getNome().equals("SRV1")) {
					String aux = controller.preparaSync("A");
					System.out.println(aux);
					String[] aux2 = aux.split("!");
					try {
						controller.getSrvY().sincronizarSrvY(aux2[1], aux2[0]);
						btnSyncSrvy.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				} else if (controller.getNome().equals("SRV2")) {
					String[] aux = controller.preparaSync("B").split("!");
					try {
						controller.getSrvY().sincronizarSrvY(aux[1], aux[0]);
						btnSyncSrvy.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				} else if (controller.getNome().equals("SRV3")) {
					String[] aux = controller.preparaSync("C").split("!");
					for (int i = 0; i < aux.length; i++) {
						String string = aux[i];
						System.out.println(string);
					}
					try {
						controller.getSrvY().sincronizarSrvY(aux[1], aux[0]);
						btnSyncSrvy.setEnabled(false);
					} catch (RemoteException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("Erro na atualização do grafo");
					}
				}

			}
		});
		btnSyncSrvy.setBounds(173, 122, 95, 23);
		frame.getContentPane().add(btnSyncSrvy);
	}
}
