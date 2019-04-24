package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.Comparator;

import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de las distintas formas de entradas sean por la entrada estandar
 * o por archivos los cuales seran leidos y procesados
 */
public class Entrada {

    /**
     * Leera la entrada estandar de la consola y la convertira en una lista ordenada.
     * @param elemento el elemento a agregar.
     * @return la Lista ordenada con los elementos de la entrada estandar.
     */
    public Lista<String> entradaEstandar(){
        Lista<String> lista = new Lista<>();
        try {
            String cadena;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while((cadena=br.readLine()) != null && !cadena.trim().equals("--EOF--")){
                lista.agrega(cadena);
            }
            br.close();
            return lista.mergeSort(comparar);
        } catch (IOException e) {
            System.err.println("No existe el archivo del cual leer");
            Proyecto1.uso();
            return lista;
        }catch(NullPointerException e){
            System.err.println("No se ha recibido ningun tipo de entrada");
            Proyecto1.uso();
            return lista;
        }
    }

    /**
     * Leera la ruta, verificara si es valida y ordenara cada linea del archivo en una lista.
     * @param ruta la ruta del archivo de lectura.
     * @return la Lista ordenada con los elementos del archivo dado por la ruta.
     */
    public Lista<String> archivo(String ruta){
        Lista<String> lista = new Lista<>();
        try{
            String cadena;
            FileReader f = new FileReader(ruta);
            BufferedReader br = new BufferedReader(f);
            while((cadena = br.readLine())!=null) {
                lista.agrega(cadena);
            }
            f.close();
            br.close();
            return lista.mergeSort(comparar);
        } catch (IOException ioe) {
            System.err.println("no existe el archivo del cual leer");
            Proyecto1.uso();
            return lista;
        }
    }

    /**
     * Un metodo sobrecargado estatico para una comparacion de las lineas leidas
     * que regresara 0 si a es igual a b , un numero mayor a cero si el primero 
     * es mayor al segundo, un numero menor a cero si el segundo elemento es menor al segundo.
     * @param elem1 linea de caracteres a comparar.
     * @param elem2 linea de caracteres a comparar.
     * @return un Int que define si es mayor menor o igual los elementos comparados.
     */
    public static Comparator<String> comparar  = new Comparator<String>() {
        @Override public int compare (String elem1,String elem2){
            elem1 = Normalizer.normalize(elem1.trim().toLowerCase(), Normalizer.Form.NFD);
            elem2 = Normalizer.normalize(elem2.trim().toLowerCase(), Normalizer.Form.NFD);
            elem1 = elem1.replaceAll("[^\\p{L}]","").replaceAll("[^\\p{ASCII}]", "");
            elem2 = elem2.replaceAll("[^\\p{L}]","").replaceAll("[^\\p{ASCII}]", "");    
            return (elem1.compareTo(elem2));
        }
    };

}