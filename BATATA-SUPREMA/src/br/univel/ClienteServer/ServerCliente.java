package br.univel.ClienteServer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import br.univel.comum.Cliente;
import br.univel.comum.IServer;
import br.univel.comum.ArquivoDiretorio.Arquivo;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.LineNumberInputStream;
import java.lang.reflect.Array;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ServerCliente extends JFrame implements IServer {

	private JPanel contentPane;
	private JTextField textFieldPortaServidor;
	private JTextField textFieldIPServidor;
	private JTextField textFieldPortaCliente;
	private JTextField textFieldIpCliente;
	private JButton btnAbrirServidor;
	private JButton btnFecharServidor;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private IServer servico,servicoCliente;
	private Registry registry,registryCliente;
	Arquivo arq = new Arquivo();
	private List<String> listaArquivos = new ArrayList<>();
	private Cliente cliente;
	private Map<Cliente, List<String>> mapaClientes = new HashMap<>();
	private JScrollPane scrollPaneServidor;
	private JTextArea textAreaServidor;
	private JLabel lblLogViwer;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerCliente frame = new ServerCliente();
					frame.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerCliente() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 590, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JLabel lblPorta = new JLabel("Porta:");
		GridBagConstraints gbc_lblPorta = new GridBagConstraints();
		gbc_lblPorta.insets = new Insets(0, 0, 5, 5);
		gbc_lblPorta.anchor = GridBagConstraints.EAST;
		gbc_lblPorta.gridx = 0;
		gbc_lblPorta.gridy = 0;
		contentPane.add(lblPorta, gbc_lblPorta);
		
		textFieldPortaServidor = new JTextField();
		GridBagConstraints gbc_textFieldPortaServidor = new GridBagConstraints();
		gbc_textFieldPortaServidor.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPortaServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPortaServidor.gridx = 1;
		gbc_textFieldPortaServidor.gridy = 0;
		contentPane.add(textFieldPortaServidor, gbc_textFieldPortaServidor);
		textFieldPortaServidor.setColumns(10);
		
		btnAbrirServidor = new JButton("Abrir Servidor");
		btnAbrirServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abrirServidor();
			}
		});
		GridBagConstraints gbc_btnAbrirServidor = new GridBagConstraints();
		gbc_btnAbrirServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnAbrirServidor.gridx = 2;
		gbc_btnAbrirServidor.gridy = 0;
		contentPane.add(btnAbrirServidor, gbc_btnAbrirServidor);
		
		JLabel lblIp = new JLabel("IP:");
		GridBagConstraints gbc_lblIp = new GridBagConstraints();
		gbc_lblIp.anchor = GridBagConstraints.EAST;
		gbc_lblIp.insets = new Insets(0, 0, 5, 5);
		gbc_lblIp.gridx = 0;
		gbc_lblIp.gridy = 1;
		contentPane.add(lblIp, gbc_lblIp);
		
		textFieldIPServidor = new JTextField();
		GridBagConstraints gbc_textFieldIPServidor = new GridBagConstraints();
		gbc_textFieldIPServidor.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldIPServidor.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldIPServidor.gridx = 1;
		gbc_textFieldIPServidor.gridy = 1;
		contentPane.add(textFieldIPServidor, gbc_textFieldIPServidor);
		textFieldIPServidor.setColumns(10);
		
		btnFecharServidor = new JButton("Fechar Servidor");
		btnFecharServidor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fecharServidor();
			}
		});
		GridBagConstraints gbc_btnFecharServidor = new GridBagConstraints();
		gbc_btnFecharServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnFecharServidor.gridx = 2;
		gbc_btnFecharServidor.gridy = 1;
		contentPane.add(btnFecharServidor, gbc_btnFecharServidor);
		
		lblLogViwer = new JLabel("Log Viwer");
		GridBagConstraints gbc_lblLogViwer = new GridBagConstraints();
		gbc_lblLogViwer.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogViwer.gridx = 0;
		gbc_lblLogViwer.gridy = 2;
		contentPane.add(lblLogViwer, gbc_lblLogViwer);
		
		scrollPaneServidor = new JScrollPane();
		GridBagConstraints gbc_scrollPaneServidor = new GridBagConstraints();
		gbc_scrollPaneServidor.gridwidth = 3;
		gbc_scrollPaneServidor.gridheight = 4;
		gbc_scrollPaneServidor.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneServidor.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneServidor.gridx = 0;
		gbc_scrollPaneServidor.gridy = 3;
		contentPane.add(scrollPaneServidor, gbc_scrollPaneServidor);
		
		textAreaServidor = new JTextArea();
		scrollPaneServidor.setViewportView(textAreaServidor);
		
		JLabel lblPortaCliente = new JLabel("Porta:");
		GridBagConstraints gbc_lblPortaCliente = new GridBagConstraints();
		gbc_lblPortaCliente.anchor = GridBagConstraints.EAST;
		gbc_lblPortaCliente.insets = new Insets(0, 0, 5, 5);
		gbc_lblPortaCliente.gridx = 5;
		gbc_lblPortaCliente.gridy = 7;
		contentPane.add(lblPortaCliente, gbc_lblPortaCliente);
		
		textFieldPortaCliente = new JTextField();
		GridBagConstraints gbc_textFieldPortaCliente = new GridBagConstraints();
		gbc_textFieldPortaCliente.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPortaCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPortaCliente.gridx = 6;
		gbc_textFieldPortaCliente.gridy = 7;
		contentPane.add(textFieldPortaCliente, gbc_textFieldPortaCliente);
		textFieldPortaCliente.setColumns(10);
		
		btnConectar = new JButton("Conectar");
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectarCliente();
				try {
					registrarCliente(cliente);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.insets = new Insets(0, 0, 5, 0);
		gbc_btnConectar.gridx = 7;
		gbc_btnConectar.gridy = 7;
		contentPane.add(btnConectar, gbc_btnConectar);
		
		JLabel lblIpCliente = new JLabel("IP:");
		GridBagConstraints gbc_lblIpCliente = new GridBagConstraints();
		gbc_lblIpCliente.anchor = GridBagConstraints.EAST;
		gbc_lblIpCliente.insets = new Insets(0, 0, 0, 5);
		gbc_lblIpCliente.gridx = 5;
		gbc_lblIpCliente.gridy = 8;
		contentPane.add(lblIpCliente, gbc_lblIpCliente);
		
		textFieldIpCliente = new JTextField();
		GridBagConstraints gbc_textFieldIpCliente = new GridBagConstraints();
		gbc_textFieldIpCliente.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldIpCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldIpCliente.gridx = 6;
		gbc_textFieldIpCliente.gridy = 8;
		contentPane.add(textFieldIpCliente, gbc_textFieldIpCliente);
		textFieldIpCliente.setColumns(10);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectarCliente();
			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.gridx = 7;
		gbc_btnDesconectar.gridy = 8;
		contentPane.add(btnDesconectar, gbc_btnDesconectar);
		
//coisas do Servidor
		
		textFieldIPServidor.setEditable(false);
		btnFecharServidor.setEnabled(false);
		textAreaServidor.setEditable(false);
		
		textFieldIPServidor.setText(mostrarIP());
		textFieldPortaServidor.setText("1818");
		
		
//coisas do Cliente
		
		
		textFieldIpCliente.setText("192.168");
		textFieldPortaCliente.setText("1818");
		
		btnDesconectar.setEnabled(false);
	}
	
	public void abrirServidor(){
		String sPorta = textFieldPortaServidor.getText().trim();
		
		if (!sPorta.matches("[0-9]+") || sPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		}else if(sPorta == null){
			JOptionPane.showMessageDialog(this, "O campo porta precisa conter algum numero");
		}
		
		int iPorta = Integer.parseInt(sPorta);
		if (iPorta < 1024 || iPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {
			servico = (IServer) UnicastRemoteObject.exportObject(this,0);
			registry = LocateRegistry.createRegistry(iPorta);
			registry.rebind(IServer.NOME_SERVICO, servico);
			
			textFieldPortaServidor.setEditable(false);
			btnAbrirServidor.setEnabled(false);
			btnFecharServidor.setEnabled(true);
			
			
			JOptionPane.showMessageDialog(this, "O servidor está aberto");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "erro ao abrir o servidor");
			e.printStackTrace();
		}
		
	}
	public void fecharServidor(){
		
		try {
			UnicastRemoteObject.unexportObject(registry, true);
		    UnicastRemoteObject.unexportObject(this, true);
			
			JOptionPane.showMessageDialog(this, "O servidor foi encerrado");
			                                                                                     
			textFieldPortaServidor.setEditable(true);
			btnAbrirServidor.setEnabled(true);
			btnFecharServidor.setEnabled(false);
			
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "erro ao fechar o servidor");
			e.printStackTrace();
		}
	}
	
	public String mostrarIP(){
		InetAddress IP;
		String IPString = null;
		try {
			IP = InetAddress.getLocalHost();
			IPString = IP.getHostAddress();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return IPString;
	}
	
	public void conectarCliente(){
		String sIp = textFieldIpCliente.getText();
		
		String sPorta = textFieldPortaCliente.getText().trim();
		
		if (!sPorta.matches("[0-9]+") || sPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		}else if(sPorta == null){
			JOptionPane.showMessageDialog(this, "O campo porta precisa conter algum numero");
		}
		
		int iPorta = Integer.parseInt(sPorta);
		if (iPorta < 1024 || iPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}
		
		try {
			registryCliente = LocateRegistry.getRegistry(sIp,iPorta);
			servicoCliente = (IServer) registryCliente.lookup(IServer.NOME_SERVICO);
			
			cliente.setNome("kaike");
			cliente.setId(1);
			cliente.setIp(mostrarIP());
			cliente.setPorta(iPorta);
			
			cliente = (Cliente) UnicastRemoteObject.exportObject(this, 0);
			
			registrarCliente(cliente);
			//cliente = (Cliente) UnicastRemoteObject.exportObject(this, 0);
			
			JOptionPane.showMessageDialog(this, "Você está conectado no servidor");
			
			
			btnConectar.setEnabled(false);
			btnDesconectar.setEnabled(true);
			textFieldIpCliente.setEditable(false);
			textFieldPortaCliente.setEditable(false);
			
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "erro ao conectar no servidor");
			e.printStackTrace();
		}
	}
	
	public void desconectarCliente(){
		try {
			UnicastRemoteObject.unexportObject(this, true);
			servicoCliente = null;
			
			JOptionPane.showMessageDialog(this, "Você se desconectou do servidor");
			
			btnConectar.setEnabled(true);
			btnDesconectar.setEnabled(false);
			textFieldIpCliente.setEditable(true);
			textFieldPortaCliente.setEditable(true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "erro ao desconectar do servidor");
			e.printStackTrace();
		}
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		
		
		
		File dirStart = new File(".\\");
		
		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {
				arq.setNome(file.getName());
				//arq.setTamanho(file.length());
				listaArquivos.add(arq.getNome());
			}
		}
		
		mapaClientes.put(c, listaArquivos);
		
		//mostrando hashmap
		
		for (Entry<Cliente, List<String>> entry : mapaClientes.entrySet()) {
			textAreaServidor.append(entry.getKey()+" : "+entry.getValue()+"/n");
		}
		//System.out.println(Arrays.asList(mapaClientes));
		
	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String nome) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] baixarArquivo(Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void desconectarCliente(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
