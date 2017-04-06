package br.univel.testes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.univel.jshare.cliente.ServerCliente;

public class TestandoHashMap {
	Map<String, Integer> mapTeste = new HashMap<>();
	
	Map<String, List<Integer>> mapTeste1 = new HashMap<>();
	Map<String, List<Integer>> mapTesteList = new HashMap<>();
	List<Integer> listaInt = new ArrayList<>();
	private List<Integer> listaInt2 = new ArrayList<>();
	
	public void inserirDados(){
		mapTeste.put("a", 1);
		mapTeste.put("a", 2);
		mapTeste.put("b", 2);
		mapTeste.put("c", 3);
		mapTeste.put("d", 4);
		mapTeste.put("e", 3);
	}
	public void mostrarHashMap(){
		//System.out.println(Arrays.asList(mapTeste));
		//System.out.println(Collections.singletonList(mapTeste));
		for (Map.Entry<String, Integer> entry : mapTeste.entrySet()) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
	}
	public void inserirDadosMapaLista(){
		for (int i = 0; i < 10; i++) {
			listaInt.add(i);
		}
		for (int i = 0; i < 25; i++) {
			listaInt2.add(i);
		}
		mapTesteList.put("teste", listaInt);
		mapTesteList.put("teste2", listaInt2);
		
	}
	public void mostrarDadosMapaLista(){
		for (Entry<String, List<Integer>> entry : mapTesteList.entrySet()) {
			List<Integer> nova = new ArrayList<>();
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (entry.getValue().get(i) == 1) {
					nova.add(entry.getValue().get(i));
					
					mapTeste1.put(entry.getKey(),nova);

				}
			}
			
		}
		for (Entry<String, List<Integer>> integer : mapTeste1.entrySet()) {
			System.out.println(integer.getKey()+ " : "+ integer.getValue());
			
		}
	}
	public void testeMostrar(){
		
	}
	public TestandoHashMap(){
		//inserirDados();
		//mostrarHashMap();
		inserirDadosMapaLista();
		mostrarDadosMapaLista();
	}
	public static void main(String[] args) {
		new TestandoHashMap();
		
	}
	
}
