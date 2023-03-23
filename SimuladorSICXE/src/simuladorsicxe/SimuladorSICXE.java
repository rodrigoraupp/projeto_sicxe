/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simuladorsicxe;

/**
 *
 * @author rodrigolaforet
 */
public class SimuladorSICXE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int tamanhoMemoria = 2048;

        //Memoria memoria = new Memoria(tamanhoMemoria);
        //Registradores registradores = new Registradores();
        
        Executor executor = new Executor(tamanhoMemoria); 
        

        /*memoria.setMemoria(7, "11110000");
        memoria.setMemoria(0, "11110000");
        
        for(int x = 0; x<tamanhoRegistradores; x++){
            System.out.println(x + " : "+ registradores.getRegistrador(x));
        }
        */
    }
    
}
