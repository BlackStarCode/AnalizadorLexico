package com.example.analizadorlexico;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class HelloController implements Initializable {
    @FXML
    private Button btnCal;

    @FXML
    private TableColumn colum1;

    @FXML
    private TableColumn colum2;

    @FXML
    private TableColumn colum3;

    @FXML
    private TableColumn colum4;
    @FXML
    private TableView<Datos> tbl1;

    @FXML
    private TextArea text1;

    //lista palabra reservadas
    ObservableList<String> palabrasReservadas = FXCollections.observableArrayList("byte", "short", "int","long","char","float","double","boolean","void","if","else","switch","case","default"
        ,"while","do","for","break","continue","try","catch","finally","throw","throws","private","protected","public","class","interface","enum","import"
        ,"package","extends","implements","static","final","abstract","default","new","instanceof","this","super","return","var","synchronized","volatile","native","transient","assert","strictfp"
        ,"const","goto","true","false","null");

    //lista de simbolos
    ObservableList<String> simbolos = FXCollections.observableArrayList(
            // Operadores aritméticos
            "+", "-", "*", "/", "%",
            // Operadores de asignación
            "=", "+=", "-=", "*=", "/=", "%=",
            // Operadores relacionales
            "==", "!=", "<", ">", "<=", ">=",
            // Operadores especiales
            "++", "--", ".", "(", ")",
            "&&", "||", "{", "}", ";"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colum1.setCellValueFactory(new PropertyValueFactory<>("palabra"));
        colum2.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colum3.setCellValueFactory(new PropertyValueFactory<>("noColum"));
        colum4.setCellValueFactory(new PropertyValueFactory<>("noFila"));
    }
    public void definirTipo(String cadena, int fila, int columna) {
        // Variables válidas
        Pattern variable = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
        // Números válidos
        Pattern numero = Pattern.compile("-?\\d+(\\.\\d+)?([eE]-?\\d+)?");

        // Verificando tipo
        if (palabrasReservadas.contains(cadena)) {
            tbl1.getItems().add(new Datos(cadena, "Reservada", columna, fila));
        } else if (simbolos.contains(cadena)) {
            tbl1.getItems().add(new Datos(cadena, "Simbolo", columna, fila));
        } else if (variable.matcher(cadena).matches()) {
            tbl1.getItems().add(new Datos(cadena, "variable", columna, fila));
        } else if (numero.matcher(cadena).matches()) {
            tbl1.getItems().add(new Datos(cadena, "numero", columna, fila));
        } else {
            System.out.println("No es fila: " + fila + ", columna: " + columna);
        }
    }

    @FXML
    protected void calcular() {
        // Limpiar tabla
        tbl1.getItems().clear();
        // Iniciar contador (para las filas)
        int contadorFila = 1;

        // Dividir el contenido del TextArea teniendo en cuenta los límites de palabras no alfanuméricas
        String[] lineas = text1.getText().split("\\n");

        // Recorrer cada línea
        for (String linea : lineas) {
            // Dividir la cadena teniendo en cuenta los límites de palabras no alfanuméricas
            String[] elementos = linea.split("(?<=\\W)|(?=\\W)");
            int contadorColumna = 1;

            // Recorrer lista de elementos
            for (String elemento : elementos) {
                // Evitar procesar espacios en blanco vacíos
                if (!elemento.trim().isEmpty()) {
                    System.out.println("Fila: " + contadorFila + ", Columna: " + contadorColumna + ", Elemento: " + elemento);
                    definirTipo(elemento, contadorFila, contadorColumna);
                }
                contadorColumna++;
            }
            contadorFila++;
        }
    }

    //limpiar tabla y textArea
    @FXML
    protected void limpiar(){
        tbl1.getItems().clear();
        text1.clear();
    }
}