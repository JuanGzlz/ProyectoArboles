/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Funciones;
import Arbol.Arbol;
import Arbol.NodoArbol;
import EDD.HashTable;
import EDD.Lista;
import EDD.Nodo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase JsonDecoder
 * Incluye toda la decodificación del archivo json para convertir las paradas en Strings y así luego en vértices para la creación del grado
 * @author Juan González
 * @version 1.0
 */

public class JsonDecoder {
    /**
    * @param file variable privada de tipo File que indica el archivo seleccionada en la carpeta de archivos del usuario
    * @param NewBufferedReader variable de tipo BufferedReader que permite leer el json por caracteres, matrices y cadenas de manera eficiente
    * @param text variable de tipo String que representa un texto para realizar pruebas de este decodificador (uso no definido)
    */
    
    private File file;
    private BufferedReader NewBufferedReader;
    private String text;
    
    /**
     * Constructor de la clase JsonDecoder, recibiendo el archivo elegido en JsonChooser como parámetro de entrada
     * Automáticamente asigna la variable file al valor de la entrada
     * Los Readers son asignados a la lectura del propio file que representa el archivo ingresado
     */
    
    public JsonDecoder(File file) throws FileNotFoundException{
        this.file = file;
        FileReader NewFileReader = new FileReader(this.file);
        this.NewBufferedReader = new BufferedReader(NewFileReader);
    }
    
    /**
     * Función que entrega una sola línea de texto del archivo json
     * @return variable de tipo String con una única línea de texto (la respectiva parada del archivo json)
     */
    
    public String Read() throws FileNotFoundException, IOException{
        return this.NewBufferedReader.readLine();
    }
    
    /**
     * Método que devuelve la función Read() al inicio, es decir, la primera línea de texto del archivo
     */
    
    public void Reset() throws FileNotFoundException{
        FileReader NewFileReader = new FileReader(this.file);
        this.NewBufferedReader = new BufferedReader(NewFileReader);
    }
    
//    public void Marcar() throws IOException {
//        this.NewBufferedReader.mark(2);
//    }
//    
//    public void Devolverse() throws IOException{
//        this.NewBufferedReader.reset();
//        this.NewBufferedReader.
//    }
    
    /**
     * Método que lee cada línea del json exactamente una vez
     */
    
    public void ReadAll() throws FileNotFoundException, IOException{
        FileReader NewFileReader = new FileReader(this.file);
        BufferedReader MyBuffer = new BufferedReader(NewFileReader);
        String mystr = MyBuffer.readLine();
        while(mystr != null){
            System.out.println(mystr);
            mystr = MyBuffer.readLine();
        }
    }
    
    /**
     * Función que entrega exactamente el nombre del sistema de metro completo al estar siempre en la segunda línea del archivo json
     * @return variable de tipo String con el nombre de la red de transporte
     */
    
    public String CasaNombre() throws FileNotFoundException, IOException{
        this.Reset();
        this.Read();
        String Nombre = this.Read();
        Nombre = Nombre.replace("\"", "").replace(":[", "").trim();
        return Nombre;
    }
    
    /**
     * Función que gestiona la obtención del grafo mediante dos vueltas...
     * 1era Vuelta: Crea los vértices con el nombre de cada parada, quitando todos los caracteres innecesarios y así obteniendo el grafo en sí
     * 2da Vuelta: Crea las conexiones entre los vértices, revisando diversas condiciones según la parada sea una que conecta con otra línea o si es una parada común y fija
     * @return variable de tipo Grafo que representa el grafo del sistema de transporte en su totalidad y ya estructurado gráficamente
     * @throws java.io.IOException
     */
    
    @SuppressWarnings("empty-statement")
   
    public HashTable crearHashTable() throws IOException{
        String N = this.CasaNombre();
        HashTable tabla = new HashTable(N);
        String Iteracion = this.Read();
        int contador = 0;
        boolean Madre = false;
        String[] Atributos = {"", "", "", "", "", "", "", "", "", "", "", ""};
        while (Iteracion != null){
            if (Iteracion.contains("[") && !Iteracion.contains("]")){
                if(!Iteracion.contains("Father to")){
                    Atributos[0] = Iteracion.replace("\"", "").replace("[", "").replace(":", "").trim();
                }
                contador++;                
            }else if (Iteracion.contains("]") && !Iteracion.contains("[")){
                contador--;                
            }
            
            switch (contador) {
                case 0:
                    if(!Iteracion.contains("{") && !Iteracion.contains("}")){
                    NodoArbol newNodoArbol = new NodoArbol(Atributos[0], Atributos[1], Atributos[2], Atributos[3], Atributos[4], Atributos[5], Atributos[6], Atributos[7], Atributos[8], Atributos[9], Atributos[10], Atributos[11]);
                    String Nombre_completo = Atributos[0] + ", " + Atributos[1] + " of his name, son of " + Atributos[2];
                    tabla.insertNodo(newNodoArbol, Nombre_completo);
                    for(int i = 0; i < Atributos.length; i++){
                        Atributos[i] = "";
                        }
                    Madre = false;
                    }
                    break;
                case 1:
                    if(Iteracion.contains("Born to")){
                        if(Madre==false){
                            Atributos[2] = Iteracion.replace("Born to", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                            Madre = true;
                        }else{
                            Atributos[3] = Iteracion.replace("Born to", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                        }
                    }else{
                    Atributos = GuardarDatos(Iteracion, Atributos, Madre);
                    }
                    break;
                case 2:
                    Atributos = GuardarHijos(Iteracion, Atributos);
                    break;
                default:
                    break;
            }
            Iteracion = this.Read();
        }
        return tabla;
    }
    
    private String[] GuardarDatos(String Linea, String[] Array, boolean Madre) throws IOException{
        if (Linea.contains("Of his name")){
            Array[1] = Linea.replace("Of his name", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Known throughout as")){
            Array[4] = Linea.replace("Known throughout as", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Held title")){
            Array[5] = Linea.replace("Held title", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Wed to")){
            Array[6] = Linea.replace("Wed to", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Of eyes")){
            Array[7] = Linea.replace("Of eyes", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Of hair")){
            Array[8] = Linea.replace("Of hair", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Notes")){
            Array[10] = Linea.replace("Notes", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        } else if (Linea.contains("Fate")){
            Array[11] = Linea.replace("Fate", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
        }
              
        return Array;
    }
    
    private String[] GuardarHijos(String Linea, String[] Array){
        if(Linea.contains("Father to")){
            return Array;
        }
        else if(Array[9].equals("")){
            Array[9] = Linea.replace("\"", "").replace(",", "").trim();
        } else {
            Array[9] = Array[9] + ", " + Linea.replace("\"", "").replace(",", "").trim();
        }
        return Array;
    }
    
    public HashTable HashTableMotes(HashTable Tabla) throws IOException{
        this.Reset();
        String N = this.CasaNombre();
        HashTable NuevaTabla = new HashTable(N);
        String Iteracion = this.Read();
        int contador = 0;
        String mote = "";
        String NombreCompleto = "";
        String Nombre = "";
        String Padre = "";
        String Numeral = "";
        boolean SeenPadre = false;
        while (Iteracion != null){
            if (Iteracion.contains("[") && !Iteracion.contains("]")){
                if(!Iteracion.contains("Father to")){
                    Nombre = Iteracion.replace("\"", "").replace("[", "").replace(":", "").trim();
                }
                contador++;                
            }else if (Iteracion.contains("]") && !Iteracion.contains("[")){
                contador--;                
            }
            switch (contador){
                case 0:
                    if (!Iteracion.contains("{") && !Iteracion.contains("}")){
                        NombreCompleto = Nombre + ", " + Numeral + " of his name, son of " + Padre;
                        NodoArbol nodo = Tabla.busquedaHasheo(NombreCompleto);
                        if(!mote.equals("")){
                            NuevaTabla.insertNodo(nodo, mote);
                        }
                        else {
                            NuevaTabla.insertNodo(nodo, NombreCompleto);
                        }
                    }
                    mote = "";
                    NombreCompleto = "";
                    Nombre = "";
                    Padre = "";
                    Numeral = "";
                    SeenPadre = false;
                    break;
                case 1:
                    if (Iteracion.contains("Known throughout as")){
                        mote = Iteracion.replace("Known throughout as", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                    } else if(Iteracion.contains("Of his name")){
                        Numeral = Iteracion.replace("Of his name", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                    }else if(Iteracion.contains("Born to")){
                        if(SeenPadre==false){
                        Padre = Iteracion.replace("Born to", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                        SeenPadre = true;
                        }
                    }
                    break;
            }
        Iteracion = this.Read();
        }
        return NuevaTabla;
    }
    
    public Arbol crearArbol(HashTable tablaNombres, HashTable tablaMotes) throws FileNotFoundException, IOException{
        this.Reset();
        String N = this.CasaNombre();
        String Iteracion = this.Read();
        Arbol newArbol = new Arbol();
        int contador = 0;
        boolean SeenPadre = false;
        String padre = "";
        String NombreCompleto = "";
        String Nombre = "";
        String Numeral = "";
        while (Iteracion != null){
            if (Iteracion.contains("[") && !Iteracion.contains("]")){
                if(!Iteracion.contains("Father to")){
                    Nombre = Iteracion.replace("\"", "").replace("[", "").replace(":", "").trim();
                }
                contador++;                
            }else if (Iteracion.contains("]") && !Iteracion.contains("[")){
                contador--;                
            }
            switch (contador){
                case 0:
                    if (!Iteracion.contains("{") && !Iteracion.contains("}")){
                        NombreCompleto = Nombre + ", " + Numeral + " of his name, son of " + padre;
                        NodoArbol nodo = tablaNombres.busquedaHasheo(NombreCompleto);
                        NodoArbol PadreMote = tablaMotes.busquedaHasheo(padre);
                        if (padre.contains("Unknown")){
                            newArbol.setRaiz(nodo);
                        } else if(PadreMote != null){
                            PadreMote.AgregarHijo(nodo);
                        } else{
                            Lista posibles = tablaNombres.DevolverPosible(padre);
                            Nodo current = posibles.getpFirst();
//                            if(current==null){
//                                System.out.println("Invalido");
//                            }
                            while(current!=null){
                                String[] Hijos = current.getPersona().getHijos().split(", ");
                                for(int i = 0; i < Hijos.length; i++){
                                    if(nodo.getNombre().contains(Hijos[i])){
                                        System.out.println("Llego");
                                       current.getPersona().AgregarHijo(nodo);
                                       break;
                                    }
                                }
                            current = current.getpNext();
                            }
                        }
                                
                    }
                    
                    
                    NombreCompleto = "";
                    Nombre = "";
                    padre = "";
                    SeenPadre = false;
                    break;
                case 1:
                    if(Iteracion.contains("Of his name")){
                        Numeral = Iteracion.replace("Of his name", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();   
                    } else if(Iteracion.contains("Born to")){
                        if(SeenPadre==false){
                        padre = Iteracion.replace("Born to", "").replace("\"", "").replace("{", "").replace("}", "").replace(",", "").replace(":", "").trim();
                        SeenPadre = true;
                        }
                    }
                    break;
            }
    Iteracion = this.Read();
    System.out.println("fin");  
    }  
        return newArbol;
}
}