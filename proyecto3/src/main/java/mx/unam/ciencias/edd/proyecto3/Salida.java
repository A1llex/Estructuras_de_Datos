package mx.unam.ciencias.edd.proyecto3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de la forma de salida del ordenamiento lexicografico 
 * sea la salida por consola o en un archivo de volcado.
 */
public class Salida {

    String ruta;
    Lista<String> archivos;
    Lista<Diccionario<String,Integer>> a;

    public Salida(String ruta, Lista<String> archivos) {
        this.ruta = ruta;
        this.archivos = archivos;
	}

    public void htmlIndex(Lista<Diccionario<String,Integer>> a) {
        try {
            File f = new File("index.html");
            FileWriter fw = new FileWriter(f);
            fw.write("<!DOCTYPE html>\n<html>\n<<head>\n<style>\nbody{background-color: black;}\na{color: blue;}\nh1{color: white}\n</style>\n</head>\n<body>\n<h1>Indice de Reportes</h1>");
            for (String var : archivos) {
                fw.write("\n<a href="+"ruta"+var+".html>"+var+"</a>");
            }
            Grafica<Integer> graph = new Grafica<>();
            for (int i = 0; i < archivos.getElementos(); i++) {
                graph.agrega(i+1);
            }
            
            fw.write("\n</body>\n</html>");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("No Se pudo escribir bien el idice");
        }
	}
    
  
    public void html(){
        try {
            File f = new File(ruta+".html");
            FileWriter fw = new FileWriter(f);
            fw.write("<!DOCTYPE html>\n<html>\n<<head>\n<style>\nbody {background-color: black;}\na{color: blue;}\nh1{color: white}\n</style>\n</head>\n<body>\n<h1>Indice de Reportes</h1>");

            fw.write("\n</body>\n</html>");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("No Se pudo escribir bien el html ");
        }
    }

    public void svg(){
        try {
            File f = new File(ruta+".html");
            FileWriter fw = new FileWriter(f);
            fw.write("<!DOCTYPE html>\n<html>\n<<head>\n<style>\nbody {background-color: black;}\na{color: blue;}\nh1{color: white}\n</style>\n</head>\n<body>\n<h1>Indice de Reportes</h1>");
            
           
            
            fw.write("\n</body>\n</html>");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("No Se pudo escribir bien el html ");
        }
    }

    public void css(){
        try {
            File f = new File(ruta+".html");
            FileWriter fw = new FileWriter(f);
            fw.write("<!DOCTYPE html>\n<html>\n<<head>\n<style>\nbody {background-color: black;}\na{color: blue;}\nh1{color: white}\n</style>\n</head>\n<body>\n<h1>Indice de Reportes</h1>");
            
           
            
            fw.write("\n</body>\n</html>");
            fw.close();
        } catch (IOException ioe) {
            System.err.println("No Se pudo escribir bien el html ");
        }
    }

	public void html(Lista<Diccionario<String, Integer>> aux) {
	}

 }