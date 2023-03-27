/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simuladorsicxe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author rodrigolaforet
 */
public class Executor {
    
    private Memoria memoria;
    private Registradores registradores;
    
    //______________________________CODIGO WESLEY_______________________________________



    @FXML
    private Button buttonExecutarObjeto;

    @FXML
    private Label labelRegisterPC;

    @FXML
    private TextField textFieldNameFile;
    
    @FXML
    private Label valorAcumulador;

    @FXML
    private Label registradorBase;
    
    @FXML
    private TableView<Endereco> table;

    @FXML
    private TableColumn<Endereco, String> endereco;

    @FXML
    private TableColumn<Endereco, String> valor;

    @FXML
    private Label registradorIndice;

    @FXML
    private Label registradorLigacao;

    @FXML
    private Label registradosStatus;

    @FXML
    private Button buttonExecutaObjeto;

    TableView<Endereco> tableView = new TableView<>();
    ObservableList<Endereco> data = FXCollections.observableArrayList();

    
   
    @FXML
    void executarObjetoTeste(ActionEvent event) {

        memoria = new Memoria(2048, textFieldNameFile.getText());

        endereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));

        valor.setCellValueFactory(new PropertyValueFactory<>("valor"));

        this.registradores = new Registradores();
    
        for (int i = 0; i < memoria.getTamMemoria(); i++) {
            Endereco endereco = memoria.getEndereco(i);
            data.add(endereco);
        }
    
        table.setItems(data);
       

       
       

    }

    

   //______________________________CODIGO WESLEY_______________________________________
   /* 
    public Executor(int tamanhoMemoria){
        this.memoria = new Memoria(tamanhoMemoria);
        this.registradores = new Registradores();
        
        registradores.setRegistrador(0, "000000000000000000000000");
        registradores.setRegistrador(1, "000000000000000000000000");
        registradores.setRegistrador(2, "000000000000000000000000");
        registradores.setRegistrador(3, "000000000000000000000000");
        registradores.setRegistrador(4, "000000000000000000000000");
        registradores.setRegistrador(5, "000000000000000000000000");
        registradores.setRegistrador(6, "000000000000000000000000");
        registradores.setRegistrador(7, "000000000000000000000000");
        registradores.setRegistrador(8, "000000000000000000000000");
        registradores.setRegistrador(9, "000000000000000000000000");
        
        memoria.setMemoria(0, "01001001");
        memoria.setMemoria(1, "00000000");
        memoria.setMemoria(2, "00001001");
        memoria.setMemoria(3, "00000011");
        memoria.setMemoria(4, "00000000");
        memoria.setMemoria(5, "00000000");
        memoria.setMemoria(6, "00000000");
        memoria.setMemoria(7, "00000000");
        memoria.setMemoria(8, "00000000");
        memoria.setMemoria(9, "00000111");
        
        System.out.println("POS SETAR REGISTRADORES: ");
        imprimeRegistradoresEmDecimal();
        //imprimeRegistradores();
        
        executaObjeto();
        
        System.out.println();
        System.out.println("ESTADO FINAL DOS REGISTRADORES");
        imprimeRegistradoresEmDecimal();
        //imprimeRegistradores();
 
    } */
  
    //PC = 0
    //TIPO2 = 90h PC = 3
    //vê que tipo é, calcula quantos bytes tem pra poder atualizar o PC pra apontar pra próxima instrução
    // 10010000 00000000 [00000000]


    
    @FXML
    void executaObjeto(ActionEvent event) {

    
   // public void executaObjeto(){
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
        
        //pcEmDecimal = Integer.parseInt(registradores.getRegistrador(8),2); //isso aqui é o PC em decimal
        pcEmDecimal = Integer.parseInt(  labelRegisterPC.getText(),2);
     
        instrucao = memoria.getMemoria(pcEmDecimal);    //opcode com flags
        System.out.println("isntrucao: " + instrucao);
        opcode = instrucao.substring(0,6);              //opcode puro
        opcode = opcode + "00";
        proximoByte = memoria.getMemoria(pcEmDecimal + 1);
        System.out.println("Opcode: " + opcode);

        if(opcode == "00000000"){
            System.out.println("hello world final");
            event.consume();
        }
        
        switch (opcode) {
            case "10010000": //ADDR r1, r2 = 10010000 00010010, é do tipo 2          "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                //varInteira = converteDecimal(registradores.getRegistrador(regDoisIndex)) + converteDecimal(registradores.getRegistrador(regUmIndex));
                varInteira = converteDecimal(registradorLigacao.getText()) + converteDecimal(registradorIndice.getText());
                valorEmString = stringFinalRegistrador(varInteira);
                //registradores.setRegistrador(regDoisIndex, valorEmString); //destino é o registrador 2
                registradorLigacao.setText(valorEmString);
                incrementaPC(pcEmDecimal, 2);
                break;
                
            case "10110100": //CLEAR r1                                           "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                //registradores.setRegistrador(regUmIndex, "000000000000000000000000");
                incrementaPC(pcEmDecimal, 2);
                registradorIndice.setText("000000000000000000000000");
                break;
           
            case "10100000": //COMPR                                                "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"        Obs: nesse CASE tinha faltado o incrementa PC
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                //conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
                conteudoReg1 = converteDecimal(registradorIndice.getText());
                //conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                conteudoReg2 = converteDecimal(registradorLigacao.getText());
                setSW(conteudoReg1, conteudoReg2);
                incrementaPC(pcEmDecimal, 2);
                break;
                   
                
            case "10011100": //DIVR r1, r2 =   (r2) <- r2/r1                       "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"   
                //regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
               
                regUmIndex = retornaIndexDosRegistradores(proximoByte, 1);
                regDoisIndex = retornaIndexDosRegistradores(proximoByte, 2);
                System.out.println("hello world");
               // conteudoReg1 = converteDecimal(registradores.getRegistrador(regUmIndex));
               // conteudoReg2 = converteDecimal(registradores.getRegistrador(regDoisIndex));
                conteudoReg1 = converteDecimal(registradorIndice.getText());
                conteudoReg2 = converteDecimal(registradorLigacao.getText());
                System.out.println(conteudoReg1);
                System.out.println(conteudoReg2);
                conteudoReg1 = 14;
                if(conteudoReg2 != 0){
                    valorEmString = stringFinalRegistrador(conteudoReg2/conteudoReg1);
                    registradorLigacao.setText(valorEmString);
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
                //IMPLEMENTAR
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
                
            case "00110000": //JEQ
                executaOP(instrucao, pcEmDecimal, "JEQ");
                break;
                
            case "00110100": //JGT
                executaOP(instrucao, pcEmDecimal, "JGT");
                break;
            
            case "00111000": //JLT
                executaOP(instrucao, pcEmDecimal, "JLT");
                break;
                
            case "01001000": //JSUB
                executaOP(instrucao, pcEmDecimal, "JSUB");
                break;    
                
                
            case "00000000": //LDA "OKKKKKKKKKKKKKKKKKKKKKKKKKKKKK"
                executaOP(instrucao, pcEmDecimal, "LDA");
                break;
                
            case "01101000": //LDB
                executaOP(instrucao, pcEmDecimal, "LDB");
                System.out.println("LDB 2");
                break;
                
            case "01010000": //LDCH
                executaOP(instrucao, pcEmDecimal, "LDCH");
                break;
                
            case "00001000": //LDL
                executaOP(instrucao, pcEmDecimal, "LDL");
                break;
                
            case "01101100": //LDS
                executaOP(instrucao, pcEmDecimal, "LDS");
                break;
                
            case "01110100": //LDT
                executaOP(instrucao, pcEmDecimal, "LDT");
                break;
                
            case "00000100": //LDX
                executaOP(instrucao, pcEmDecimal, "LDX");
                break;
                
            case "00100000": //MUL
                executaOP(instrucao, pcEmDecimal, "MUL");
                break;
                
            case "01000100": //OR
                executaOP(instrucao, pcEmDecimal, "OR");
                break;
                
            case "01001100": //RSUB
                executaOP(instrucao, pcEmDecimal, "RSUB");
                break;
                
            case "00001100": //STA
                executaOP(instrucao, pcEmDecimal, "STA");
                break;
                
            case "01111000": //STB
                executaOP(instrucao, pcEmDecimal, "STB");
                break;
                
            case "01010100": //STCH
                executaOP(instrucao, pcEmDecimal, "STCH");
                break;
                
            case "00010100": //STL
                executaOP(instrucao, pcEmDecimal, "STL");
                break;
                
            case "01111100": //STS
                executaOP(instrucao, pcEmDecimal, "STS");
                break;
                
            case "10000100": //STT
                executaOP(instrucao, pcEmDecimal, "STT");
                break;
                
            case "00010000": //STX
                executaOP(instrucao, pcEmDecimal, "STX");
                break;
                
            case "00011100": //SUB
                executaOP(instrucao, pcEmDecimal, "SUB");
                break;
                
            case "00101100": //TIX
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
        System.out.println("hello world");
        //registradores.setRegistrador(9, regularizaStringPra24Bits(Integer.toBinaryString(comparacao)));
        registradosStatus.setText(regularizaStringPra24Bits(Integer.toBinaryString(comparacao)));
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
    
    public void imprimeMemoriaEmDecimal(){
        for(int i=0; i<10; i++){
            String conteudo = memoria.getMemoria(i);
            System.out.println("Memoria "+i+": "+converteDecimal(conteudo));
        }
    }
    
    public void incrementaPC(int pcEmDecimal, int formato){
        if(formato == 2){
            pcEmDecimal = pcEmDecimal + 2;
            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
            labelRegisterPC.setText(regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
        }
        if(formato == 3){
            pcEmDecimal = pcEmDecimal + 3;
            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
            labelRegisterPC.setText(regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
        }
        
        if(formato == 4){
            pcEmDecimal = pcEmDecimal + 4;
            registradores.setRegistrador(8, regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
            labelRegisterPC.setText(regularizaStringPra24Bits(Integer.toBinaryString(pcEmDecimal)));
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
    
    // Faz todo o calculo dos endereços de acordo com o modo de endereçamento
    public String buscaEndereco(String proximoByte, String terceiroByte, char flagX, char flagB, char flagP, int modoDeEnderecamento, int tipoInstrucao, String quartoByte){
        int endereco1=0, endereco2, endereco3;
        String resultado="";
        
        if(tipoInstrucao == 3){
            resultado = proximoByte.substring(4,8);
            resultado = resultado + terceiroByte; //tem 12 bits - 0000 00000100
            endereco1 = converteDecimal(resultado); //agora é decimal.
        }
        
        if(tipoInstrucao == 4){
            resultado = proximoByte.substring(4,8);
            resultado = resultado + terceiroByte + quartoByte; //tem 20 bits - 0000 00000000 00000000
            endereco1 = converteDecimal(resultado); //agora é decimal.
       
        }

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
    
    // Faz a operação desejada e set nos registradores de acordo com a mesma.
    public void defineOperacao(int varInteira, String valorEmString, String operacao, int formatoInstrucao, int pcEmDecimal){
        int simboloCC = -1;
        int indexMemoria = -1;
        int valor1 = 0;
        int valor2 = 0;
        String byteA = "";
        String byteB = "";
        String byteC = "";
        String conteudoRegistrador = "";
        System.out.println(operacao);
        
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
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(8, valorEmString);
            formatoInstrucao = 0;
        }
        if(operacao == "JEQ"){
            simboloCC = converteDecimal(registradores.getRegistrador(9));
            if(simboloCC == 0){
                valorEmString = regularizaStringPra24Bits(valorEmString);
                registradores.setRegistrador(8, valorEmString);
                formatoInstrucao = 0;
            }
        }
        if(operacao == "JGT"){
            simboloCC = converteDecimal(registradores.getRegistrador(9));
            if(simboloCC == 1){
                valorEmString = regularizaStringPra24Bits(valorEmString);
                registradores.setRegistrador(8, valorEmString);
                formatoInstrucao = 0;
            }
        }
        if(operacao == "JLT"){
            simboloCC = converteDecimal(registradores.getRegistrador(9));
            if(simboloCC == 2){
                valorEmString = regularizaStringPra24Bits(valorEmString);
                registradores.setRegistrador(8, valorEmString);
                formatoInstrucao = 0;
            }
        }
        if(operacao == "JSUB"){
            byteA = memoria.getMemoria(pcEmDecimal+formatoInstrucao);
            registradores.setRegistrador(2, byteA);
            formatoInstrucao = 0;
            registradores.setRegistrador(8, valorEmString);
            
        }
        if(operacao == "LDA"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(0, valorEmString);
            valorAcumulador.setText(valorEmString);

        }
        if(operacao == "LDB"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(3, valorEmString);
            registradorBase.setText(valorEmString);
        }
        if(operacao == "LDCH"){
            byteA = registradores.getRegistrador(0);
            byteB = byteA.substring(7);
            byteC = valorEmString.substring(0, 7)+byteB;
            registradores.setRegistrador(0, byteC);
        }
        if(operacao == "LDL"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(2, valorEmString);
        }
        if(operacao == "LDS"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(4, valorEmString);
        }
        if(operacao == "LDT"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(5, valorEmString);
        }
        if(operacao == "LDX"){
            valorEmString = regularizaStringPra24Bits(valorEmString);
            registradores.setRegistrador(1, valorEmString);
        }
        if(operacao == "MUL"){
            varInteira = varInteira * converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
        }
        if(operacao == "OR"){
            varInteira = varInteira | converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
        }
        if(operacao == "RSUB"){
            valorEmString = registradores.getRegistrador(2);
            registradores.setRegistrador(8, valorEmString);
            formatoInstrucao = 0;
        }
        if(operacao == "STA"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(0);
            byteA = valorEmString.substring(0, 8);
           // memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
            memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
           // memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "STB"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(3);
            byteA = valorEmString.substring(0, 8);
           // memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
           // memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
           // memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "STCH"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(0);
            byteA = valorEmString.substring(0, 8);
           // memoria.setMemoria(indexMemoria, byteA);
        }
        if(operacao == "STL"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(2);
            byteA = valorEmString.substring(0, 8);
         //   memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
         //   memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
         //   memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "STS"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(4);
            byteA = valorEmString.substring(0, 8);
         //   memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
        //    memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
         //   memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "STT"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(5);
            byteA = valorEmString.substring(0, 8);
          //  memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
           // memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
          //  memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "STX"){
            indexMemoria = converteDecimal(valorEmString);
            valorEmString = registradores.getRegistrador(1);
            byteA = valorEmString.substring(0, 8);
          //  memoria.setMemoria(indexMemoria, byteA);
            indexMemoria = indexMemoria + 1;
            byteB = valorEmString.substring(8, 16);
          //  memoria.setMemoria(indexMemoria, byteB);
            indexMemoria = indexMemoria + 1;
            byteC = valorEmString.substring(16, 24);
          //  memoria.setMemoria(indexMemoria, byteC);
        }
        if(operacao == "SUB"){
            varInteira = varInteira - converteDecimal(valorEmString);
            valorEmString = stringFinalRegistrador(varInteira);
            registradores.setRegistrador(0, valorEmString);
        }
        
        incrementaPC(pcEmDecimal, formatoInstrucao);

    }
    
    // Faz a separação das flags, verifica o formato de instrução e endereçamento dps chama a função necessária p/ prosseguimento
    public void executaOP(String instrucao, int pcEmDecimal, String tipoOperacao){
        int varInteira;
        String proximoByte, terceiroByte, quartoByte;
        String valorEmString = "0000000000000000";
        char flagN,flagI,flagX;
        char flagB,flagP,flagE;
                
        varInteira = converteDecimal(registradores.getRegistrador(0));
        proximoByte = memoria.getMemoria(pcEmDecimal + 1);
        terceiroByte = memoria.getMemoria(pcEmDecimal + 2);
        quartoByte = memoria.getMemoria(pcEmDecimal + 3);
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
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                           
                         
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            //IMPLEMENTAR
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                            
                         
                        }
                        
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP,1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                          
                    
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                            
                         
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                            
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 2, formatoInstrucao, quartoByte);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                       
                        
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 0, formatoInstrucao, quartoByte);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        System.out.println("imediato"); 
                    }
        }
        if(flagE == '1'){
                    int formatoInstrucao = 4;
                    if((flagN == '1') && (flagI == '1')){ //direto
                        if((flagX == '0') && (flagB == '0') && (flagP == '0')){ // disp é endereço
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '0') && (flagB == '0') && (flagP == '1')){
                            //IMPLEMENTAR
                        }
                        if((flagX == '0') && (flagB == '1') && (flagP == '0')){ //(B) + disp é o endereço
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        
                        if((flagX == '1') && (flagB == '0') && (flagP == '0')){ // (X) + disp é o endereço
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP,1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '1') && (flagB == '0') && (flagP == '1')){ //(PC) + (X
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                        if((flagX == '1') && (flagB == '1') && (flagP == '0')){
                            valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 1, formatoInstrucao, quartoByte);
                            defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        }
                    }
                    if((flagN == '1') && (flagI == '0')){//indireto
                        valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 2, formatoInstrucao, quartoByte);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                        
                    }
                    if((flagN == '0') && (flagI == '1')){//imediato
                        valorEmString = buscaEndereco(proximoByte, terceiroByte, flagX, flagB, flagP, 0, formatoInstrucao, quartoByte);
                        defineOperacao(varInteira, valorEmString, tipoOperacao, formatoInstrucao, pcEmDecimal);
                    }
        }
    }
}