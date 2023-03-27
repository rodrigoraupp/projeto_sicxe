/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simuladorsicxe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rodrigolaforet
 */
public class SimuladorSICXE extends Application{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        /* 
        int tamanhoMemoria = 2048;
        Executor executor = new Executor(tamanhoMemoria); 
        */

    //______________________________CODIGO WESLEY_______________________________________
    launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = fxmlLoader.load();
        Scene tela = new Scene(root);

        primaryStage.setScene(tela);
        
        primaryStage.show();
    }
    
}
