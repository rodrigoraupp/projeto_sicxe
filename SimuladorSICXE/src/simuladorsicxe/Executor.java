/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorsicxe;

/**
 *
 * @author rodrigolaforet
 */
public class Executor {
    
    private Memoria memoria;
    private Registradores registradores;
    
    public Executor(int tamanhoMemoria){
        this.memoria = new Memoria(tamanhoMemoria);
        this.registradores = new Registradores();
    }
    
    //PC = 0
    //TIPO2 = 90h PC = 3
    //vê que tipo é, calcula quantos bytes tem pra poder atualizar o PC pra apontar pra próxima instrução
    // 10010000 00000000 [00000000]
    public void executaObjeto(){
        //qual é o PC?
        int pcEmDecimal;
        String instrucao;
        String opcode;
        int regUmIndex;
        int regDoisIndex;
        String proximoByte;
        int soma;
        String bin;
        
        pcEmDecimal = Integer.parseInt(registradores.getRegistrador(8),2); //isso aqui é o PC em decimal
        instrucao = memoria.getMemoria(pcEmDecimal);    //opcode com flags
        opcode = instrucao.substring(0,6);              //opcode puro
        opcode = opcode + "00";
        
        switch (opcode) {
            case "10010000": //ADDR r1, r2 = 10010000 00010010, é do tipo 2
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                soma = converteDecimal(registradores.getRegistrador(regUmIndex)) + converteDecimal(registradores.getRegistrador(regDoisIndex));
                bin = Integer.toBinaryString(soma);
                registradores.setRegistrador(regDoisIndex, regularizaStringPraOitoBits(bin)); //destino é o registrador 2
                //incrementa PC
                break;
                
            case "00000100": //CLEAR r1
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                registradores.setRegistrador(regUmIndex, "00000000");
                //incrementa PC, não esquecerrrrrrr
                break;
            
            case "10100000": //COMPR r1,r2, ver se tem a questão do complemento de 2 pra números negativos
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                
                //comparação gera um resultado que influencia os JUMPS
                
            default:
                throw new AssertionError();
        }
    }//"0010" = Inteiro = Decimal = 10 D != 2
    
    //pega String do binário, converte de binário pra decimal
    public int converteDecimal(String valor){
        return Integer.parseInt(valor, 2);
    }
    
    public String regularizaStringPraOitoBits(String bin){
        while(bin.length() != 8){
            bin = "0" + bin;
        }
        return bin;
    }
}