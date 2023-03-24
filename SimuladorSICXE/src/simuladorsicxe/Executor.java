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
        String terceiroByte;
        String quartoByte;
        int soma;
        int sub;
        String bin;
        int conteudoReg1;
        int conteudoReg2;
        String resultado;
        char flagN;
        char flagI;
        char flagX;
        char flagB;
        char flagP;
        char flagE;
        int endereco1;
        int endereco2;
        int endereco3;
        
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
                
            case "00011000":  // ADD m  (A) <- m...m+2
                //analisar se é tipo 3 ou 4
                soma = converteDecimal(registradores.getRegistrador(0));
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                flagN = instrucao.charAt(6);
                flagI = instrucao.charAt(7);
                flagX = proximoByte.charAt(0);
                flagB = proximoByte.charAt(1);
                flagP = proximoByte.charAt(2);
                flagE = proximoByte.charAt(3);
                //formato tipo3
                if(flagE == '0'){
                    //endereçamento...
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            resultado = memoria.getMemoria(endereco1) + memoria.getMemoria(endereco1 + 1) + memoria.getMemoria(endereco1 + 2);
                            soma = soma + converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma + converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma + converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1));
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma + converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma + converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        endereco1 = converteDecimal(resultado); //agora é decimal
                        endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                        resultado = memoria.getMemoria(endereco2) + memoria.getMemoria(endereco2 + 1) + memoria.getMemoria(endereco2 + 2);
                        soma = soma + converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        soma = soma + converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                }
                //formato tipo4
                else{
                    
                }
                break;
                
            case "01000000": //AND m (A) <- m...m+2
                soma = converteDecimal(registradores.getRegistrador(0));
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                flagN = instrucao.charAt(6);
                flagI = instrucao.charAt(7);
                flagX = proximoByte.charAt(0);
                flagB = proximoByte.charAt(1);
                flagP = proximoByte.charAt(2);
                flagE = proximoByte.charAt(3);
                //formato tipo3
                if(flagE == '0'){
                    //endereçamento...
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            resultado = memoria.getMemoria(endereco1) + memoria.getMemoria(endereco1 + 1) + memoria.getMemoria(endereco1 + 2);
                            soma = soma & converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){   
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma & converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma & converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1));
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma & converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma & converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        endereco1 = converteDecimal(resultado); //agora é decimal
                        endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                        resultado = memoria.getMemoria(endereco2) + memoria.getMemoria(endereco2 + 1) + memoria.getMemoria(endereco2 + 2);
                        soma = soma & converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        soma = soma & converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                }
                break;
            
            case "00101000": //COMP m (A) : m...m+2
                soma = converteDecimal(registradores.getRegistrador(0));
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                flagN = instrucao.charAt(6);
                flagI = instrucao.charAt(7);
                flagX = proximoByte.charAt(0);
                flagB = proximoByte.charAt(1);
                flagP = proximoByte.charAt(2);
                flagE = proximoByte.charAt(3);
                //formato tipo3
                if(flagE == '0'){
                    //endereçamento...
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            resultado = memoria.getMemoria(endereco1) + memoria.getMemoria(endereco1 + 1) + memoria.getMemoria(endereco1 + 2);
                            setSW(soma, converteDecimal(resultado));
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){   
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            setSW(soma, converteDecimal(resultado));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            setSW(soma, converteDecimal(resultado));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1));
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            setSW(soma, converteDecimal(resultado));
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            setSW(soma, converteDecimal(resultado));
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        endereco1 = converteDecimal(resultado); //agora é decimal
                        endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                        resultado = memoria.getMemoria(endereco2) + memoria.getMemoria(endereco2 + 1) + memoria.getMemoria(endereco2 + 2);
                        setSW(soma, converteDecimal(resultado));
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        setSW(soma, converteDecimal(resultado));
                    }
                }
                break;
            
            case "00100100": //DIV m    A <- (A) / (m..m+2)         LEMBRAR DE TRATAR A DIVISÃO POR ZEROOOOOOOOOOOOOOOO
                soma = converteDecimal(registradores.getRegistrador(0));
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                flagN = instrucao.charAt(6);
                flagI = instrucao.charAt(7);
                flagX = proximoByte.charAt(0);
                flagB = proximoByte.charAt(1);
                flagP = proximoByte.charAt(2);
                flagE = proximoByte.charAt(3);
                //formato tipo3
                if(flagE == '0'){
                    //endereçamento...
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            resultado = memoria.getMemoria(endereco1) + memoria.getMemoria(endereco1 + 1) + memoria.getMemoria(endereco1 + 2);
                            soma = soma / converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma / converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma / converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1));
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma / converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                            endereco3 = endereco1 + endereco2;
                            resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
                            soma = soma / converteDecimal(resultado);
                            registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        endereco1 = converteDecimal(resultado); //agora é decimal
                        endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                        resultado = memoria.getMemoria(endereco2) + memoria.getMemoria(endereco2 + 1) + memoria.getMemoria(endereco2 + 2);
                        soma = soma / converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        soma = soma / converteDecimal(resultado);
                        registradores.setRegistrador(0, regularizaStringPra24Bits(Integer.toBinaryString(soma)));
                    }
                }
                break;
                
            case "00111100": //JEQ m   (PC) <- m 
                proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                flagN = instrucao.charAt(6);
                flagI = instrucao.charAt(7);
                flagX = proximoByte.charAt(0);
                flagB = proximoByte.charAt(1);
                flagP = proximoByte.charAt(2);
                flagE = proximoByte.charAt(3);
                //formato tipo3
                if(flagE == '0'){
                    //endereçamento...
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            registradores.setRegistrador(8, regularizaStringPra24Bits(resultado));
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                            endereco3 = endereco1 + endereco2;
                            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                            endereco3 = endereco1 + endereco2;
                            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X)
                            endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1));
                            endereco3 = endereco1 + endereco2;
                            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                            endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                            endereco3 = endereco1 + endereco2;
                            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        endereco1 = converteDecimal(resultado); //agora é decimal
                        endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                        registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco2)));
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        resultado = proximoByte.substring(4,8);
                        resultado = resultado + terceiroByte; //tem 12 bits
                        registradores.setRegistrador(8, regularizaStringPra24Bits(resultado));
                    }
                }
                break;
                
            case "00110000": //JEQ m   (PC) <- m SE CC set to =
                if(converteDecimal(registradores.getRegistrador(9)) == 0){
                    proximoByte = memoria.getMemoria(pcEmDecimal + 1);
                    terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
                    flagN = instrucao.charAt(6);
                    flagI = instrucao.charAt(7);
                    flagX = proximoByte.charAt(0);
                    flagB = proximoByte.charAt(1);
                    flagP = proximoByte.charAt(2);
                    flagE = proximoByte.charAt(3);
                    //formato tipo3
                    if(flagE == '0'){
                        //endereçamento...
                        if((flagN == '1') && (flagI == '1')){ //direto
                            if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                                resultado = proximoByte.substring(4,8);
                                resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                                registradores.setRegistrador(8, regularizaStringPra24Bits(resultado));
                            }
                            if((flagX == '0') && (flagB == '0') && (flagP == '1')){

                            }
                            if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                                resultado = proximoByte.substring(4,8);
                                resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                                endereco1 = converteDecimal(resultado); //agora é decimal
                                endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                                endereco3 = endereco1 + endereco2;
                                registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                            }
                            if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                                resultado = proximoByte.substring(4,8);
                                resultado = resultado + terceiroByte; //tem 12 bits         0000 00000000
                                endereco1 = converteDecimal(resultado); //agora é decimal
                                endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                                endereco3 = endereco1 + endereco2;
                                registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                            }
                            if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X)
                                endereco1 = converteDecimal(registradores.getRegistrador(8)); 
                                endereco2 = converteDecimal(registradores.getRegistrador(1));
                                endereco3 = endereco1 + endereco2;
                                registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                            }
                            if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                                endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                                endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                                endereco3 = endereco1 + endereco2;
                                registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco3)));
                            }
                        }
                        if((flagN == '1') && (flagI == '0')){//indireto
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits
                            endereco1 = converteDecimal(resultado); //agora é decimal
                            endereco2 = converteDecimal(memoria.getMemoria(endereco1));
                            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(endereco2)));
                        }
                        if((flagN == '0') && (flagI == '1')){//imediato
                            resultado = proximoByte.substring(4,8);
                            resultado = resultado + terceiroByte; //tem 12 bits
                            registradores.setRegistrador(0, regularizaStringPra24Bits(resultado));
                        }
                    }
                }
                else{
                    //incrementa PC normal (3bytes)
                }
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
    
    public void setSW(int esq, int dir){
        int comparacao;
        if(esq == dir){
            comparacao = 0;
        }
        if(esq > dir){
            comparacao = 1;
        }
        else{
            comparacao = 2;
        }
        registradores.setRegistrador(9, regularizaStringPra24Bits(Integer.toBinaryString(comparacao)));
    }
    
}