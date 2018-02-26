package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LeitorArq {
	
	private FileReader fr;
	private BufferedReader bf;
	
	public LeitorArq() {
		// TODO Auto-generated constructor stub
	}
	
	public String lerarq(String arq){
		try {
			fr =  new FileReader(arq);
			bf = new BufferedReader(fr);
			
			String x = bf.readLine();
			
//			System.out.println(x);
			
			String aux = x.trim();
			
			return aux;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Não achou o arquivo!!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  "Deu ruim";
	}

}
