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
        
        registradores.setRegistrador(0, "000000000000000000000001");
        registradores.setRegistrador(1, "000000000000000000000000");
        registradores.setRegistrador(2, "000000000000000000000000");
        registradores.setRegistrador(3, "000000000000000000000000");
        registradores.setRegistrador(4, "000000000000000000000000");
        registradores.setRegistrador(5, "000000000000000000000000");
        registradores.setRegistrador(6, "000000000000000000000000");
        registradores.setRegistrador(7, "000000000000000000000000");
        registradores.setRegistrador(8, "000000000000000000000000");
        registradores.setRegistrador(9, "000000000000000000000000");
        
        memoria.setMemoria(0, "00111101");
        memoria.setMemoria(1, "00000000");
        memoria.setMemoria(2, "00000100");
        memoria.setMemoria(3, "00000000");
        memoria.setMemoria(4, "00000000");
        memoria.setMemoria(5, "00000000");
        memoria.setMemoria(6, "00001111");
        memoria.setMemoria(7, "00000000");
        memoria.setMemoria(8, "00000000");
        memoria.setMemoria(9, "00000000");
        memoria.setMemoria(10, "00000000");
        
        System.out.println("POS SET INICIAL");
        imprimeRegistradoresEmDecimal();
        //imprimeRegistradores();
        
        executaObjeto();
        
        System.out.println();
        System.out.println();
        System.out.println("FINAL");
        imprimeRegistradoresEmDecimal();
        //imprimeRegistradores();
        
 
    }
  
    //PC = 0
    //TIPO2 = 90h PC = 3
    //vê que tipo é, calcula quantos bytes tem pra poder atualizar o PC pra apontar pra próxima instrução
    // 10010000 00000000 [00000000]
    public void executaObjeto(){
        //qual é o PC?
        int pcEmDecimal;
        int varInteira;
        int regUmIndex;
        int regDoisIndex;
        int conteudoReg1;
        int conteudoReg2;
        String instrucao;
        String opcode;
        String proximoByte;
        String valorEmString;
        
        pcEmDecimal = Integer.parseInt(registradores.getRegistrador(8),2); //isso aqui é o PC em decimal
        instrucao = memoria.getMemoria(pcEmDecimal);    //opcode com flags
        opcode = instrucao.substring(0,6);              //opcode puro
        opcode = opcode + "00";
        proximoByte = memoria.getMemoria(pcEmDecimal + 1);
        
        
        switch (opcode) {
            case "10010000": //ADDR r1, r2 = 10010000 00010010, é do tipo 2
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                varInteira = converteDecimal(registradores.getRegistrador(regDoisIndex)) + converteDecimal(registradores.getRegistrador(regUmIndex));
                valorEmString = stringFinalRegistrador(varInteira);
                registradores.setRegistrador(regDoisIndex, valorEmString); //destino é o registrador 2
                incrementaPC(pcEmDecimal, 2);
                break;
                
            case "10110100": //CLEAR r1
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                registradores.setRegistrador(regUmIndex, "000000000000000000000000");
                incrementaPC(pcEmDecimal, 2);
                break;
           
            case "10100000": //COMPR r1,r2, ver se tem a questão do complemento de 2 pra números negativos
                //regUmIndex = Integer.parseInt(proximoByte.substring(0,4),2);
                //regDoisIndex = Integer.parseInt(proximoByte.substring(4,8),2);
                
                //deixa pra depois
                //comparação gera um resultado que influencia os JUMPS
                //incrementa pc, não esquecer
                break;
                   
                
            case "10011100": //DIVR r1, r2 =   (r2) <- r2/r1 
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                if(conteudoReg2 != 0){
                    valorEmString = stringFinalRegistrador(conteudoReg2/conteudoReg1);
                    registradores.setRegistrador(regDoisIndex,valorEmString);
                }
                incrementaPC(pcEmDecimal, 2);
                break;
                 
            case "10011000": //MULR r1, r2 =   (r2) <- r2*r1 
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                valorEmString = stringFinalRegistrador(conteudoReg2*conteudoReg1);
                registradores.setRegistrador(regDoisIndex,valorEmString);
                incrementaPC(pcEmDecimal, 2);
                break;
                
            case "10101100": //RMO r2 <- (r1)
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                registradores.setRegistrador(regDoisIndex, registradores.getRegistrador(regUmIndex));
                incrementaPC(pcEmDecimal, 2);
                break;
           
            case "10100100": //SHIFTL r1,n 
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);; //aqui tem o n
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                valorEmString = stringFinalRegistrador(conteudoReg1 << conteudoReg2-1);
                registradores.setRegistrador(regUmIndex, valorEmString);
                incrementaPC(pcEmDecimal, 2);
                break;
              
            case "10101000": //SHIFTR r1, n 
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);; //aqui tem o n
                conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                valorEmString = stringFinalRegistrador(conteudoReg1 >> conteudoReg2-1);
                registradores.setRegistrador(regUmIndex, valorEmString);
                incrementaPC(pcEmDecimal, 2);
                break;
                
            case "10010100": //SUBR r1,r2   (r2) <-r2 -r1
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                varInteira = converteDecimal(registradores.getRegistrador(regDoisIndex)) - converteDecimal(registradores.getRegistrador(regUmIndex));
                valorEmString = stringFinalRegistrador(varInteira);
                registradores.setRegistrador(regDoisIndex, valorEmString); //destino é o registrador 2
                incrementaPC(pcEmDecimal, 2);
                break;
               
            case "10111000":   //TIXR
                break;
                
// AQUI ACABA AS INSTRUÇÕES DO TIPO 2
                
            case "00011000":  // ADD m  (A) <- m...m+2
                executaOP(instrucao, pcEmDecimal, "SOMA");
                break;
             
            case "01000000": //AND m (A) <- m...m+2
                executaOP(instrucao, pcEmDecimal, "AND");
                break;
            
            case "00101000": //COMP m (A) <- m...m+2
                executaOP(instrucao, pcEmDecimal, "COMP");
                break;
            
            case "00100100": //DIV m    A <- (A) / (m..m+2)         LEMBRAR DE TRATAR A DIVISÃO POR ZEROOOOOOOOOOOOOOOO
                executaOP(instrucao, pcEmDecimal, "DIV");
                break;
                
            case "00111100": //J m   (PC) <- m 
                executaOP(instrucao, pcEmDecimal, "JUMP");
                break;
                
            default:
                throw new AssertionError();
        }
    }//"0010" = Inteiro = Decimal = 10 D != 2
    
    //pega String do valorEmStringário, converte de valorEmStringário pra decimal
    public int converteDecimal(String valor){
        return Integer.parseInt(valor, 2);
    }
    
    public String regularizaStringPra24Bits(String valorEmString){
        while(valorEmString.length() != 24){
            valorEmString = "0" + valorEmString;
        }
        return valorEmString;
    }
    
    public String stringFinalRegistrador(int variavel){
        String valorEmString = String.format("%24s", Integer.toBinaryString(variavel)).replace(' ', '0');
        return valorEmString;
    }
    
    public void setSW(int esq, int dir){
        int comparacao = 0;
        
        if(esq == dir){
            comparacao = 0;
        }
        if(esq > dir){
            comparacao = 1;
        }
        if(esq < dir){
            comparacao = 2;
        }
        registradores.setRegistrador(9, regularizaStringPra24Bits(Integer.toBinaryString(comparacao)));
    }
    
    public void imprimeRegistradores(){
        for(int i=0; i<10; i++){
            System.out.println("Registrador "+i+": "+registradores.getRegistrador(i));
        }
    }
    
    public void imprimeRegistradoresEmDecimal(){
        for(int i=0; i<10; i++){
            String conteudo = registradores.getRegistrador(i);
            System.out.println("Registrador "+i+": "+converteDecimal(conteudo));
        }
    }
    
    public void imprimeMemoria(){
        for(int i=0; i<10; i++){
            System.out.println("Memoria "+i+": "+memoria.getMemoria(i));
        }
    }
    
    public void incrementaPC(int pcEmDecimal, int formato){
        if(formato == 2){
            pcEmDecimal = pcEmDecimal + 2;
            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
        }
        if(formato == 3){
            pcEmDecimal = pcEmDecimal + 3;
            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
        }
        else{
            //
        }
    }
    
    // Serve p/ Instruções do tipo 2, retornando o indice de R1 se qualRegistrador = 1 e o de R2 caso qualRegistrador = 2.
    public int retornaIndexDosRegistradores(String proximoByte, int qualRegistrador){
        int indexRegistrador = 0;
        if(qualRegistrador == 1){
            indexRegistrador = Integer.parseInt(proximoByte.substring(0,4),2);
        }
        if(qualRegistrador == 2){
            indexRegistrador = Integer.parseInt(proximoByte.substring(4,8),2);
        }
        return indexRegistrador;
    }
    
    public String buscaEndDo3(String proximoByte, String terceiroByte, char flagX, char flagB, char flagP, int modoDeEnderecamento){
        int endereco1, endereco2, endereco3;
        String resultado, valorEmString = "";
        
        resultado = proximoByte.substring(4,8);
        resultado = resultado + terceiroByte; //tem 12 bits         0000 00000100
        endereco1 = converteDecimal(resultado); //agora é decimal.

        if(modoDeEnderecamento == 1){
            if((flagX == '0') && (flagB == '0') && (flagP =='0')){
                resultado = memoria.getMemoria(endereco1) + memoria.getMemoria(endereco1 + 1) + memoria.getMemoria(endereco1 + 2);
            }

            if((flagX == '0') && (flagB == '1') && (flagP =='0')){
                endereco2 = converteDecimal(registradores.getRegistrador(3)); //registrador B
                endereco3 = endereco1 + endereco2;
                resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
            }

            if((flagX == '1') && (flagB == '0') && (flagP =='0')){
                endereco2 = converteDecimal(registradores.getRegistrador(1)); //registrador X
                endereco3 = endereco1 + endereco2;
                resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
            }

            if((flagX == '1') && (flagB == '0') && (flagP =='1')){
                endereco1 = converteDecimal(registradores.getRegistrador(8));
                endereco2 = converteDecimal(registradores.getRegistrador(1));
                endereco3 = endereco1 + endereco2;
                resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
            }

            if((flagX == '1') && (flagB == '1') && (flagP =='0')){
                endereco1 = converteDecimal(registradores.getRegistrador(3)); 
                endereco2 = converteDecimal(registradores.getRegistrador(1)); 
                endereco3 = endereco1 + endereco2;         
                resultado = memoria.getMemoria(endereco3) + memoria.getMemoria(endereco3 + 1) + memoria.getMemoria(endereco3 + 2);
            }
        }
        if(modoDeEnderecamento == 2){
            endereco2 = converteDecimal(memoria.getMemoria(endereco1));
            resultado = memoria.getMemoria(endereco2) + memoria.getMemoria(endereco2 + 1) + memoria.getMemoria(endereco2 + 2);
        }
        return resultado;
    }
    
    public void defineOperacao(int varInteira, String valorEmString, String operacao, int formatoInstrucao, int pcEmDecimal){
        if(operacao == "SOMA"){
            varInteira = varInteira + converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
            
        }
        if(operacao == "AND"){
            varInteira = varInteira & converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
        }
        if(operacao == "DIV"){
            varInteira = varInteira / converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
        }
        if(operacao == "COMP"){
            setSW(varInteira, converteDecimal(valorEmString));
        }
        if(operacao == "JUMP"){
            registradores.setRegistrador(8, valorEmString);
            formatoInstrucao = 0;
            System.out.println("pc: "+pcEmDecimal);
            System.out.println("valorEmString: "+valorEmString);
        }
        
        incrementaPC(pcEmDecimal, formatoInstrucao);

    }
    
    public void executaOP(String instrucao, int pcEmDecimal, String tipoOperacao){
        int varInteira;
        String proximoByte, terceiroByte;
        String valorEmString;
        char flagN,flagI,flagX;
        char flagB,flagP,flagE;
                
        varInteira = converteDecimal(registradores.getRegistrador(0));
        proximoByte = memoria.getMemoria(pcEmDecimal + 1);
        terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
        flagN = instrucao.charAt(6);
        flagI = instrucao.charAt(7);
        flagX = proximoByte.charAt(0);
        flagB = proximoByte.charAt(1);
        flagP = proximoByte.charAt(2);
        flagE = proximoByte.charAt(3);
        
        
 if(flagE == '0'){
                    //endereçamento... proximoByte = 0000 0000
                    int formatoInstrucao = 3;
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 1);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            //IMPLEMENTAR
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 1);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP,1);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 1);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 1);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 2);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        valorEmString = buscaEndDo3(proximoByte, terceiroByte, flagX, flagB, flagP, 0);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                    }
                }
    }
}