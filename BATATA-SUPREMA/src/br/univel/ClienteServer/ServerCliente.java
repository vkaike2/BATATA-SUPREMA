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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import br.univel.comum.Cliente;
import br.univel.comum.IServer;
import br.univel.comum.TipoFiltro;
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
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ServerCliente extends JFrame implements IServer, Runnable {

	private JPanel contentPane;
	private JTextField textFieldPortaServidor;
	private JTextField textFieldIPServidor;
	private JTextField textFieldPortaCliente;
	private JTextField textFieldIpCliente;
	private JButton btnAbrirServidor;
	private JButton btnFecharServidor;
	private JButton btnConectar;
	private JButton btnDesconectar;
	private IServer servico, servicoCliente;
	private Registry registry, registryCliente;
	private Arquivo arq = new Arquivo();
	private List<Arquivo> listaArquivos = new ArrayList<>();
	private List<Cliente> listaClientes = new ArrayList<>();
	private Cliente cliente = new Cliente();
	private TipoFiltro tf;
	private Map<Cliente, List<Arquivo>> mapaClientes = new HashMap<>();
	private JScrollPane scrollPaneServidor;
	private JTextArea textAreaServidor;
	private JLabel lblLogEntradas;
	private JScrollPane scrollPaneLogArquivos;
	private JTextArea textAreaLogArquivos;
	private JLabel lblArquivos;
	private JButton btnFiltrar;
	private JComboBox comboBoxFiltro;
	private JTextField textFieldFiltro;
	private JScrollPane scrollPaneCliente;
	private JTextArea textAreaCliente;
	private JLabel lblArquivo;
	private JTextField textFieldArquivo;
	private JButton btnDownload;

	private int iPorta;

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
		setBounds(100, 100, 734, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0, 114, 44, 0, 92, 111, 86, 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		gbc_btnAbrirServidor.fill = GridBagConstraints.BOTH;
		gbc_btnAbrirServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnAbrirServidor.gridx = 2;
		gbc_btnAbrirServidor.gridy = 0;
		contentPane.add(btnAbrirServidor, gbc_btnAbrirServidor);

		JLabel lblPortaCliente = new JLabel("Porta:");
		GridBagConstraints gbc_lblPortaCliente = new GridBagConstraints();
		gbc_lblPortaCliente.anchor = GridBagConstraints.EAST;
		gbc_lblPortaCliente.insets = new Insets(0, 0, 5, 5);
		gbc_lblPortaCliente.gridx = 5;
		gbc_lblPortaCliente.gridy = 0;
		contentPane.add(lblPortaCliente, gbc_lblPortaCliente);

		textFieldPortaCliente = new JTextField();
		GridBagConstraints gbc_textFieldPortaCliente = new GridBagConstraints();
		gbc_textFieldPortaCliente.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldPortaCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldPortaCliente.gridx = 6;
		gbc_textFieldPortaCliente.gridy = 0;
		contentPane.add(textFieldPortaCliente, gbc_textFieldPortaCliente);
		textFieldPortaCliente.setColumns(10);
		textFieldPortaCliente.setText("1818");

		btnConectar = new JButton("Conectar");

		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				conectarCliente();

				try {
					servicoCliente.registrarCliente(cliente);
					servicoCliente.publicarListaArquivos(cliente, listaArquivos);
				} catch (RemoteException e) {

					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		GridBagConstraints gbc_btnConectar = new GridBagConstraints();
		gbc_btnConectar.fill = GridBagConstraints.BOTH;
		gbc_btnConectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnConectar.gridx = 7;
		gbc_btnConectar.gridy = 0;
		contentPane.add(btnConectar, gbc_btnConectar);

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
		gbc_btnFecharServidor.fill = GridBagConstraints.BOTH;
		gbc_btnFecharServidor.insets = new Insets(0, 0, 5, 5);
		gbc_btnFecharServidor.gridx = 2;
		gbc_btnFecharServidor.gridy = 1;
		contentPane.add(btnFecharServidor, gbc_btnFecharServidor);

		JLabel lblIpCliente = new JLabel("IP:");
		GridBagConstraints gbc_lblIpCliente = new GridBagConstraints();
		gbc_lblIpCliente.anchor = GridBagConstraints.EAST;
		gbc_lblIpCliente.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpCliente.gridx = 5;
		gbc_lblIpCliente.gridy = 1;
		contentPane.add(lblIpCliente, gbc_lblIpCliente);

		textFieldIpCliente = new JTextField();
		GridBagConstraints gbc_textFieldIpCliente = new GridBagConstraints();
		gbc_textFieldIpCliente.fill = GridBagConstraints.HORIZONTAL;
		gbc_textFieldIpCliente.anchor = GridBagConstraints.SOUTH;
		gbc_textFieldIpCliente.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldIpCliente.gridx = 6;
		gbc_textFieldIpCliente.gridy = 1;
		contentPane.add(textFieldIpCliente, gbc_textFieldIpCliente);
		textFieldIpCliente.setColumns(10);

		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				desconectarCliente();
			}
		});
		GridBagConstraints gbc_btnDesconectar = new GridBagConstraints();
		gbc_btnDesconectar.fill = GridBagConstraints.BOTH;
		gbc_btnDesconectar.insets = new Insets(0, 0, 5, 5);
		gbc_btnDesconectar.gridx = 7;
		gbc_btnDesconectar.gridy = 1;
		contentPane.add(btnDesconectar, gbc_btnDesconectar);

		lblLogEntradas = new JLabel("Usuários Conectados:");
		GridBagConstraints gbc_lblLogEntradas = new GridBagConstraints();
		gbc_lblLogEntradas.gridwidth = 2;
		gbc_lblLogEntradas.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogEntradas.gridx = 0;
		gbc_lblLogEntradas.gridy = 2;
		contentPane.add(lblLogEntradas, gbc_lblLogEntradas);

		lblArquivos = new JLabel("Arquivos:");
		GridBagConstraints gbc_lblArquivos = new GridBagConstraints();
		gbc_lblArquivos.gridwidth = 2;
		gbc_lblArquivos.insets = new Insets(0, 0, 5, 5);
		gbc_lblArquivos.gridx = 2;
		gbc_lblArquivos.gridy = 2;
		contentPane.add(lblArquivos, gbc_lblArquivos);

		comboBoxFiltro = new JComboBox();
		comboBoxFiltro.setModel(new DefaultComboBoxModel(TipoFiltro.values()));
		GridBagConstraints gbc_comboBoxFiltro = new GridBagConstraints();
		gbc_comboBoxFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_comboBoxFiltro.gridx = 5;
		gbc_comboBoxFiltro.gridy = 2;
		contentPane.add(comboBoxFiltro, gbc_comboBoxFiltro);

		textFieldFiltro = new JTextField();
		GridBagConstraints gbc_textFieldFiltro = new GridBagConstraints();
		gbc_textFieldFiltro.gridwidth = 2;
		gbc_textFieldFiltro.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldFiltro.fill = GridBagConstraints.BOTH;
		gbc_textFieldFiltro.gridx = 6;
		gbc_textFieldFiltro.gridy = 2;
		contentPane.add(textFieldFiltro, gbc_textFieldFiltro);
		textFieldFiltro.setColumns(10);

		btnFiltrar = new JButton("Filtrar");
		btnFiltrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*
				 * 
				 * 
				 * try { mapFiltro.putAll(servicoCliente.procurarArquivo(
				 * textFieldFiltro.getText(), tf,
				 * String.valueOf(comboBoxFiltro.getSelectedItem()))); for
				 * (Entry<Cliente, List<Arquivo>> entry : mapFiltro.entrySet())
				 * {
				 * textAreaCliente.append(entry.getKey()+" : "+entry.getValue())
				 * ; textAreaCliente.append("1"); } } catch (RemoteException e)
				 * { // TODO Auto-generated catch block e.printStackTrace(); }
				 * 
				 */
				Map<Cliente, List<Arquivo>> retorno = new HashMap<>();
				try {
					retorno.putAll(servicoCliente.procurarArquivo(textFieldFiltro.getText(), tf,
							String.valueOf(comboBoxFiltro.getSelectedItem())));
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				for (Entry<Cliente, List<Arquivo>> entry : retorno.entrySet()) {
					System.out.println(entry.getKey() + " : " + entry.getValue());

				}

				// textAreaCliente.append(String.valueOf(comboBoxFiltro.getSelectedItem()));

			}
		});
		GridBagConstraints gbc_btnFiltrar = new GridBagConstraints();
		gbc_btnFiltrar.fill = GridBagConstraints.BOTH;
		gbc_btnFiltrar.insets = new Insets(0, 0, 5, 0);
		gbc_btnFiltrar.gridx = 8;
		gbc_btnFiltrar.gridy = 2;
		contentPane.add(btnFiltrar, gbc_btnFiltrar);

		scrollPaneServidor = new JScrollPane();
		GridBagConstraints gbc_scrollPaneServidor = new GridBagConstraints();
		gbc_scrollPaneServidor.gridwidth = 2;
		gbc_scrollPaneServidor.gridheight = 6;
		gbc_scrollPaneServidor.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneServidor.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneServidor.gridx = 0;
		gbc_scrollPaneServidor.gridy = 3;
		contentPane.add(scrollPaneServidor, gbc_scrollPaneServidor);

		textAreaServidor = new JTextArea();
		scrollPaneServidor.setViewportView(textAreaServidor);

		scrollPaneLogArquivos = new JScrollPane();
		GridBagConstraints gbc_scrollPaneLogArquivos = new GridBagConstraints();
		gbc_scrollPaneLogArquivos.gridwidth = 3;
		gbc_scrollPaneLogArquivos.gridheight = 6;
		gbc_scrollPaneLogArquivos.insets = new Insets(0, 0, 0, 5);
		gbc_scrollPaneLogArquivos.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneLogArquivos.gridx = 2;
		gbc_scrollPaneLogArquivos.gridy = 3;

		contentPane.add(scrollPaneLogArquivos, gbc_scrollPaneLogArquivos);

		textAreaLogArquivos = new JTextArea();
		scrollPaneLogArquivos.setViewportView(textAreaLogArquivos);

		scrollPaneCliente = new JScrollPane();
		GridBagConstraints gbc_scrollPaneCliente = new GridBagConstraints();
		gbc_scrollPaneCliente.gridwidth = 4;
		gbc_scrollPaneCliente.gridheight = 5;
		gbc_scrollPaneCliente.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPaneCliente.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneCliente.gridx = 5;
		gbc_scrollPaneCliente.gridy = 3;
		contentPane.add(scrollPaneCliente, gbc_scrollPaneCliente);

		textAreaCliente = new JTextArea();
		scrollPaneCliente.setViewportView(textAreaCliente);

		lblArquivo = new JLabel("Arquivo: ");
		GridBagConstraints gbc_lblArquivo = new GridBagConstraints();
		gbc_lblArquivo.anchor = GridBagConstraints.EAST;
		gbc_lblArquivo.fill = GridBagConstraints.VERTICAL;
		gbc_lblArquivo.insets = new Insets(0, 0, 0, 5);
		gbc_lblArquivo.gridx = 5;
		gbc_lblArquivo.gridy = 8;
		contentPane.add(lblArquivo, gbc_lblArquivo);

		textFieldArquivo = new JTextField();
		GridBagConstraints gbc_textFieldArquivo = new GridBagConstraints();
		gbc_textFieldArquivo.gridwidth = 2;
		gbc_textFieldArquivo.insets = new Insets(0, 0, 0, 5);
		gbc_textFieldArquivo.fill = GridBagConstraints.BOTH;
		gbc_textFieldArquivo.gridx = 6;
		gbc_textFieldArquivo.gridy = 8;
		contentPane.add(textFieldArquivo, gbc_textFieldArquivo);
		textFieldArquivo.setColumns(10);

		btnDownload = new JButton("Download");
		GridBagConstraints gbc_btnDownload = new GridBagConstraints();
		gbc_btnDownload.gridx = 8;
		gbc_btnDownload.gridy = 8;
		contentPane.add(btnDownload, gbc_btnDownload);

		// coisas do Servidor

		textFieldIPServidor.setEditable(false);
		btnFecharServidor.setEnabled(false);

		textAreaServidor.setEditable(false);
		textAreaLogArquivos.setEditable(false);

		textFieldIPServidor.setText(mostrarIP());
		textFieldPortaServidor.setText("1818");

		// coisas do Cliente
		String username = System.getProperty("user.name"); 
		cliente.setNome(username);
		cliente.setIp(mostrarIP());
		cliente.setPorta(iPorta);

		btnFiltrar.setEnabled(false);
		btnDesconectar.setEnabled(false);
		btnDownload.setEnabled(false);

		comboBoxFiltro.setEnabled(false);

		textFieldIpCliente.setText("192.168");
		textFieldArquivo.setEnabled(false);
		textFieldFiltro.setEnabled(false);
		textAreaCliente.setEditable(false);

	}

	public void abrirServidor() {
		String sPorta = textFieldPortaServidor.getText().trim();

		if (!sPorta.matches("[0-9]+") || sPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		} else if (sPorta == null) {
			JOptionPane.showMessageDialog(this, "O campo porta precisa conter algum numero");
		}

		int iPorta = Integer.parseInt(sPorta);
		if (iPorta < 1024 || iPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {
			servico = (IServer) UnicastRemoteObject.exportObject(this, 0);
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

	public void fecharServidor() {

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

	public String mostrarIP() {
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

	public void conectarCliente() {

		String sIp = textFieldIpCliente.getText();

		String sPorta = textFieldPortaCliente.getText().trim();

		if (!sPorta.matches("[0-9]+") || sPorta.length() > 5) {
			JOptionPane.showMessageDialog(this, "A porta deve ser um valor numérico de no máximo 5 dígitos!");
			return;
		} else if (sPorta == null) {
			JOptionPane.showMessageDialog(this, "O campo porta precisa conter algum numero");
		}

		iPorta = Integer.parseInt(sPorta);
		if (iPorta < 1024 || iPorta > 65535) {
			JOptionPane.showMessageDialog(this, "A porta deve estar entre 1024 e 65535");
			return;
		}

		try {
			registryCliente = LocateRegistry.getRegistry(sIp, iPorta);
			servicoCliente = (IServer) registryCliente.lookup(IServer.NOME_SERVICO);

			// cliente = (Cliente) UnicastRemoteObject.exportObject(this, 0);

			JOptionPane.showMessageDialog(this, "Você está conectado no servidor");

			btnFiltrar.setEnabled(true);
			btnDownload.setEnabled(true);
			textFieldArquivo.setEnabled(true);
			textFieldFiltro.setEnabled(true);
			comboBoxFiltro.setEnabled(true);
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

	public void desconectarCliente() {
		try {
			if (servicoCliente != null) {
				desconectar(cliente);
				// UnicastRemoteObject.unexportObject(this, true);
				servicoCliente = null;
				listaArquivos.clear();
			}

			JOptionPane.showMessageDialog(this, "Você se desconectou do servidor");

			btnFiltrar.setEnabled(false);
			btnDownload.setEnabled(false);
			textFieldArquivo.setEnabled(false);
			textFieldFiltro.setEnabled(false);
			comboBoxFiltro.setEnabled(false);
			btnConectar.setEnabled(true);
			btnDesconectar.setEnabled(false);
			textFieldIpCliente.setEditable(true);
			textFieldPortaCliente.setEditable(true);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(this, "erro ao desconectar do servidor");
			e.printStackTrace();
		}
	}

	@Override
	public void registrarCliente(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		listaClientes.add(c);

		textAreaServidor.append(c.getNome() + " se conectou.\n");

	}

	@Override
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista) throws RemoteException {

		// listaGeralComTodosOsArquivosEClientes.put(c, lista);
		mapaClientes.put(c, lista);
		// TODO Auto-generated method stub

		File dirStart = new File(".\\");

		for (File file : dirStart.listFiles()) {
			if (file.isFile()) {

				Arquivo arq = new Arquivo();
				arq.setNome(file.getName());
				arq.setTamanho(file.length());

				lista.add(arq);
			}
		}

		textAreaLogArquivos.append(c.getNome() + ":\n");
		for (Arquivo arq : lista) {
			textAreaLogArquivos.append("   " + arq.getNome() + "\n");

			// System.out.println("\t" + arq.getTamanho() + "\t" +
			// arq.getNome());
		}
	}

	public void desconectar(Cliente c) throws RemoteException {
		// TODO Auto-generated method stub
		mapaClientes.remove(c);
		textAreaServidor.append(cliente.getNome() + " se desconectou\n");
	}

	@Override
	public Map<Cliente, List<Arquivo>> procurarArquivo(String query, TipoFiltro tipoFiltro, String filtro)
			throws RemoteException {
		List<Arquivo> ListaArquivoFiltrado = new ArrayList<>();
		Map<Cliente,List<Arquivo>> mapaFiltrado = new HashMap<>();
		
		Pattern pat = Pattern.compile(".*" + query + ".*");
		
		if (filtro.equals(tipoFiltro.NOME)) {
			
			
		
			for (Entry<Cliente, List<Arquivo>> entry : mapaClientes.entrySet()) {
				//System.out.println(entry.getKey()+" : "+entry.getValue());
				
				
				for (int i = 0; i < entry.getValue().size(); i++) {
					
					Arquivo arq = entry.getValue().get(i);
					
					String nomeArquivo = arq.getNome();
					
					Matcher m = pat.matcher(nomeArquivo.toLowerCase());
					
					if (m.matches()) {
						ListaArquivoFiltrado.add(entry.getValue().get(i));
					}
					mapaFiltrado.put(entry.getKey(), ListaArquivoFiltrado);
				}
				
	
			}
			
			
			
			
			for (Arquivo arq : listaArquivos) {
				//Matcher m = pat.matcher(arq.getNome().toLowerCase());
				
				
			}
			
			
		}
		return mapaFiltrado;

	}

	@Override
	public byte[] baixarArquivo(Cliente cli, Arquivo arq) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
