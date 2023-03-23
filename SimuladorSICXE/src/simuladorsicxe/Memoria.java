/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorsicxe;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author rodrigolaforet
 */
public class Memoria {
    
    private ArrayList<String> memoria;

	public Memoria(int tamanho) {
            this.memoria = new ArrayList<> (Collections.nCopies(tamanho, "00000000"));
	}
        
        public String getMemoria(int index){
            return memoria.get(index);
        }
        
        public void setMemoria(int posicao, String conteudo){
            memoria.add(posicao, conteudo);
        }
}
