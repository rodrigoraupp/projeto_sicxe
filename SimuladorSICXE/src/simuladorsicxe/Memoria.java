/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorsicxe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 *
 * @author rodrigolaforet
 */
public class Memoria {
    
    private ArrayList<String> memoria;

    private ArrayList<Endereco> enderecos;

    private Endereco endereco;


 
    public Memoria(int tamanho, String nomeArquivo) {
        
     // Lê os dados do arquivo objeto 
        String nomeDoArquivo = "C:/Users/fixte/Videos/Captures/codObjeto.txt" ;//+ nomeArquivo;
        List<String> linhas = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(nomeDoArquivo))) {
        String linha;
        while ((linha = br.readLine()) != null) {
            linhas.add(linha);
        }
        } catch (IOException e) {
        System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

        while(linhas.size() != 2048 )  
        {
        linhas.add("00000000");
        } 

/*      for (String elemento : linhas) {
            System.out.println(elemento);
            
        }  */


        enderecos = new ArrayList<Endereco>(tamanho);

        // inicializa o ArrayList com objetos Endereco vazios
        for (int i = 0; i < tamanho; i++) {
            enderecos.add(new Endereco(i, linhas.get(i)));
        }
    }  
     
        public Endereco getEndereco(int index){
            return enderecos.get(index);
        }

        public String getMemoria(int index){
            return enderecos.get(index).getValor();
        }
        
        public void setMemoria(int posicao, String conteudo){
            enderecos.add(new Endereco(posicao, conteudo));
        }

        public int getTamMemoria(){
            return enderecos.size();
        
     }
}
