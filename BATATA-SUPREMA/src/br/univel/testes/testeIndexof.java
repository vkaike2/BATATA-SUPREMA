package br.univel.testes;

public class testeIndexof {
	  public static void main(String[] args) {
	        String pro = "teste.txt";
	        int um;
	        int zero;
	        um = pro.indexOf(".");
	        //zero = pro.lastIndexOf(".");
	        System.out.println(pro.substring(0, pro.indexOf(".")));
	    }
}
