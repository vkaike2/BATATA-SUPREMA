package br.univel.comum;

import java.util.*;
import java.rmi.Remote;
import java.rmi.RemoteException;


import br.univel.comum.ArquivoDiretorio.*;

public interface IServer extends Remote{
	
	public static final String NOME_SERVICO = "Jshare";
	
	public void registrarCliente(Cliente c)throws RemoteException;
	
	public void publicarListaArquivos(Cliente c, List<Arquivo> lista)throws RemoteException;
	
	public Map<Cliente, List<Arquivo>> procurarArquivo(String nome)throws RemoteException;
	
	public byte[] baixarArquivo(Arquivo arq)throws RemoteException;
	
	public void desconectarCliente(Cliente c)throws RemoteException;
}
