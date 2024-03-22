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
    ObservableList<String> palabrasReservadas = FXCollections.observableArrayList("byte", "short", "int","long","chfar","float","double","boolean","void","if","else","switch","case","default"
        ,"while","do","for","break","continue","try","catch","finally","throw","throws","private","protected","public","class","interface","enum","import","Main","main","compile","run","package","extends","implements","static","final","abstract","default","new","instanceof","this","super","return","var","synchronized","volatile","native","string","transient","assert","strictfp","Compile","ObservableList","Observable","ArrayList","ResourceBundle","Pattern","FXML","PropertyValueFactory","URL","FXMLLoader","Scene","Stage","IOException","HelloApplication","Application","launch","HelloController","Initializable","Button","TableColumn","TableView","TextArea","TextField","Label","CheckBox","RadioButton","ComboBox","DatePicker","ProgressBar","ProgressIndicator","Slider","Spinner","SplitPane","ScrollPane","Pagination","Pagination","Accordion","TitledPane","TabPane","ToolBar","MenuBar","Menu","MenuItem","ContextMenu","ContextMenuEvent","Dialog","Alert","FileChooser","DirectoryChooser","FileChooser"
        ,"package","extends","implements","static","final","abstract","default","new","instanceof","this","super","return","var","synchronized","volatile","native","string","transient","assert","strictfp","Out","PrintStream","InputStream","OutputStream","Reader","Writer","File","FileReader","FileWriter","BufferedReader","BufferedWriter","PrintWriter","InputStreamReader","OutputStreamWriter","Print","print","Println","println"
        ,"const","goto","true","false","null","String","System","out","println","print","Scanner","nextInt","nextLine","nextDouble","nextBoolean","nextFloat","nextLong","nextShort","nextByte","next", "getItems","setItems", "getSelectedItem", "setSelectedItem","At","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","at","add","addAll","clear","contains","get","indexOf","isEmpty","remove","removeAll","retainAll","set","size","sort","subList","toArray","toString","equals","hashCode","notify","notifyAll","wait","clone","finalize","getClass","registerNatives","Matches","match","matches","Match");

    //lista de simbolos
    ObservableList<String> simbolos = FXCollections.observableArrayList(
            // Operadores aritméticos
            "+", "-", "*", "/", "%",
            // Operadores de asignación
            "=", "+=", "-=", "*=", "/=", "%=",
            // Operadores relacionales
            "==", "!=", "<", ">", "<=", ">=","@",
            // Operadores especiales
            "++", "--", ".", "(", ")",
            "&&", "||", "{", "}", ";",
            "!", "&", "|", "^", "~",
            "<<", ">>", ">>>", "<<<",
            "++", "--",
            "?", ":", ",", "->",
            // detecta comillas
            "\"", "'"
    );

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colum1.setCellValueFactory(new PropertyValueFactory<>("palabra"));
        colum2.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colum3.setCellValueFactory(new PropertyValueFactory<>("noColum"));
        colum4.setCellValueFactory(new PropertyValueFactory<>("noFila"));
    }

    public void definirTipo(String cadena, int fila, int columna) {
        Pattern variable = Pattern.compile("[a-zA-Z_$][a-zA-Z_$0-9]*");
        Pattern numero = Pattern.compile("-?\\d+(\\.\\d+)?([eE]-?\\d+)?");

        if (cadena.startsWith("\"") && cadena.endsWith("\"")) {
            // Tratar contenido entre comillas como cadena
            tbl1.getItems().add(new Datos(cadena, "Cadena", columna, fila));
        } else if (palabrasReservadas.contains(cadena)) {
            tbl1.getItems().add(new Datos(cadena, "Reservada", columna, fila));
        } else if (simbolos.contains(cadena)) {
            tbl1.getItems().add(new Datos(cadena, "Simbolo", columna, fila));
        } else if (variable.matcher(cadena).matches()) {
            tbl1.getItems().add(new Datos(cadena, "Variable", columna, fila));
        } else if (numero.matcher(cadena).matches()) {
            tbl1.getItems().add(new Datos(cadena, "Numero", columna, fila));
        } else {
            System.out.println("No es fila: " + fila + ", columna: " + columna);
        }
    }

    @FXML
    protected void calcular() {
        tbl1.getItems().clear();
        String[] lineas = text1.getText().split("\\n");
        int contadorFila = 1;

        for (String linea : lineas) {
            int contadorColumna = 1;

            if (linea.contains("\"")) {
                String[] partes = linea.split("\"");
                boolean dentroDeCadena = false;

                for (String parte : partes) {
                    if (!parte.trim().isEmpty()) {
                        if (dentroDeCadena) {
                            definirTipo("\"" + parte + "\"", contadorFila, contadorColumna);
                        } else {
                            String[] elementos = parte.split("(?<=\\W)|(?=\\W)");

                            for (String elemento : elementos) {
                                if (!elemento.trim().isEmpty()) {
                                    definirTipo(elemento, contadorFila, contadorColumna);
                                }
                                contadorColumna++;
                            }
                        }
                    }
                    dentroDeCadena = !dentroDeCadena;
                }
            } else {
                String[] elementos = linea.split("(?<=\\W)|(?=\\W)");

                for (String elemento : elementos) {
                    if (!elemento.trim().isEmpty()) {
                        definirTipo(elemento, contadorFila, contadorColumna);
                    }
                    contadorColumna++;
                }
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