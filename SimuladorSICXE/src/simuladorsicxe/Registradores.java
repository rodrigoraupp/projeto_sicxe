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
public class Registradores {
    
    private ArrayList<String> registradores;
    final int tamanho = 10;
    
    public Registradores(){
        this.registradores = new ArrayList<> (Collections.nCopies(tamanho, "00000000"));
    }
    
    public String getRegistrador(int reg){
        return registradores.get(reg);
    }
    
    public void setRegistrador(int reg, String conteudo){
        registradores.add(reg, conteudo);
    }
    
}
