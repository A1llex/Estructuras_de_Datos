package mx.unam.ciencias.edd.proyecto3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;

import mx.unam.ciencias.edd.Conjunto;
import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de las distintas formas de entradas sean por la entrada estandar
 * o por archivos los cuales seran leidos y procesados
 */
public class Entrada {
    
    /**
     * Leera de la ruta especificada las palabras y 
     * @param ruta desde donde se leera el arcivho a analizar
     * @return regresara un diccionario con las palabras como llava y la frecuencia como valor
     */
    public Diccionario<String, Integer> lectura(String ruta){
        Diccionario<String,Integer> palabras = new Diccionario<>();
        try {
            String cadena;
            BufferedReader br = new BufferedReader(new FileReader(ruta));
            while((cadena = br.readLine()) != null ){
                cadena = Normalizer.normalize(cadena.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{L}]","").replaceAll("[^\\p{ASCII}]", "");
                String[] palabra = cadena.split(" ");
                for (String var : palabra) {
                    try {
                        int rep = palabras.get(var)+1;
                        palabras.agrega(var, rep);
                    } catch (IllegalArgumentException e) {
                        palabras.agrega(var, 1);
                    }
                }
            }
            br.close();
            return palabras;
        } catch (IOException | NullPointerException e) {
            System.err.println("No se recivio una entrada adecuada");
            Proyecto3.uso();
            return null;
        }
    }

}