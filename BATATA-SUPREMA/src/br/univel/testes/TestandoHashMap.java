package br.univel.testes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TestandoHashMap {
	Map<String, Integer> mapTeste = new HashMap<>();
	Map<String, List<Integer>> mapTesteList = new HashMap<>();
	List<Integer> listaInt = new ArrayList<>();
	
	public void inserirDados(){
		mapTeste.put("a", 1);
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
		mapTesteList.put("teste", listaInt);
	}
	public void mostrarDadosMapaLista(){
		for (Entry<String, List<Integer>> entry : mapTesteList.entrySet()) {
			System.out.println(entry.getKey()+" : "+entry.getValue());
		}
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
