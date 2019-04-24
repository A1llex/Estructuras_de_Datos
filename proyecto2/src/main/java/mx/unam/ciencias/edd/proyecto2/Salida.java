package mx.unam.ciencias.edd.proyecto2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de la forma de salida del ordenamiento lexicografico 
 * sea la salida por consola o en un archivo de volcado.
 */
public class Salida {

    /**
     * Recorrera una Lista imprimiendo sus elementos.
     * @param lista la lista que se recorrerar e imprimira.
     */
    public void imprime(Lista<String> lista){
        try {
            String s;
            System.out.print(s =lista.eliminaPrimero());
            for (String var : lista) {
                System.out.print("\n"+var);
            }
            lista.agregaInicio(s);
        } catch (NoSuchElementException e) {
            System.out.println();
        }

    }

    /**
     * Sobre escribira el contenido de la lista en un archivo
     * @param lista la lista que se recorrera e imprimira en un archivo.
     * @param ruta sera la ruta del archivo de volcado de la lista,
     * de no existir se creara el archivo.
     */
    public void overRide(Lista<String> lista, String ruta){
        try {
            File f = new File(ruta);
            FileWriter fw = new FileWriter(f);
            String s;
            fw.write(s =lista.eliminaPrimero());
            for (String var : lista) {
                fw.write("\n"+var);
            }
            lista.agregaInicio(s);
            fw.close();
        } catch (IOException ioe) {
            System.err.println("No Se puede leer el Archivo "+ruta);
        }catch (NoSuchElementException ioe) {
        }
    }

 }