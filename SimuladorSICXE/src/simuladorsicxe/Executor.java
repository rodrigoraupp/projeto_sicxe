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
        int sub;
        String bin;
        int conteudoReg1;
        int conteudoReg2;
        String resultado;
        
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
                registradores.setRegistrador(regDoisIndex, regularizaStringPra24Bits(bin)); //destino é o registrador 2
                //incrementa PC
                break;
                
            case "10110100": //CLEAR r1
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                registradores.setRegistrador(regUmIndex, "000000000000000000000000");
                //incrementa PC, não esquecerrrrrrr
                break;
            
            case "10100000": //COMPR r1,r2, ver se tem a questão do complemento de 2 pra números negativos
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                
                //deixa pra depois
                //comparação gera um resultado que influencia os JUMPS
                //incrementa pc, não esquecer
                break;
                
            case "10011100": //DIVR r1, r2 =   (r2) <- r2*r1 
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                registradores.setRegistrador(regDoisIndex,regularizaStringPra24Bits(Integer.toBinaryString(conteudoReg2/conteudoReg1)));
                //incrementar pc
                break;
                
            case "10011000": //MULR r1, r2 =   (r2) <- r2*r1 
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                registradores.setRegistrador(regDoisIndex, regularizaStringPra24Bits(Integer.toBinaryString(conteudoReg2*conteudoReg1)));
                //incrementar pc
                break;
                
            case "10101100": //RMO r2 <- (r1)
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                registradores.setRegistrador(regDoisIndex, registradores.getRegistrador(regUmIndex));
                //incrementar pc
                break;
            
            case "10100100": //SHIFTL r1,n 
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2); //aqui tem o n
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                //regDoisIndex é n, aqui faz-se o deslocamento
                registradores.setRegistrador(regUmIndex, regularizaStringPra24Bits(Integer.toBinaryString(conteudoReg1 << (regDoisIndex-1))));
                //incrementar pc
                break;
                
            case "10101000": //SHIFTR r1, n 
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2); //aqui tem o n
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                resultado = Integer.toBinaryString(conteudoReg1 >> (regDoisIndex-1));
                while(resultado.length() != 24){
                    resultado = resultado.charAt(0) + resultado;
                   }
                registradores.setRegistrador(regUmIndex, resultado);
                //incrementar pc
                break;
                
            case "10010100": //SUBR r1,r2   (r2) <-r2 -r1
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                sub = converteDecimal(registradores.getRegistrador(regDoisIndex)) - converteDecimal(registradores.getRegistrador(regUmIndex));
                bin = Integer.toBinaryString(sub);
                registradores.setRegistrador(regDoisIndex, regularizaStringPra24Bits(bin)); //destino é o registrador
                //incrementar pc
                break;
                
            case "10111000":   //TIXR soma 1 no r1
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                soma = converteDecimal(registradores.getRegistrador(regUmIndex)) + 1;
                registradores.setRegistrador(1, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                //incrementar pc
                break;
                                
            default:
                throw new AssertionError();
        }
    }//"0010" = Inteiro = Decimal = 10 D != 2
    
    //pega String do binário, converte de binário pra decimal
    public int converteDecimal(String valor){
        return Integer.parseInt(valor, 2);
    }
    
    public String regularizaStringPra24Bits(String bin){
        while(bin.length() != 24){
            bin = "0" + bin;
        }
        return bin;
    }
}