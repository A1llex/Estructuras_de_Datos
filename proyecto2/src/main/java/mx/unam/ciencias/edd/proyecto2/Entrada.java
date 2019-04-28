package mx.unam.ciencias.edd.proyecto2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de las distintas formas de entradas sean por la entrada estandar
 * o por archivos los cuales seran leidos y procesados
 */
public class Entrada {

    /**
     * sera La estructura de la entrada
     */
    private String estructura;

    /**
     * regresa que tipo de estructura es
     * @return un String que sera la supuesta estructura
     */
    public String getEstructura(){
        return estructura;
    }

    /**
     * Leera la entrada estandar de la consola o la entrada de un archivo 
     * @param ruta sera la ruta
     * @return una lista con el contenido del archivo o la entrada estandar
     */
    public Lista<Integer> entrada(String ruta){
        try {
            Lista<Integer> entrada = new Lista<>();
            String cadena;
            BufferedReader br;

            if(ruta == null)
                br = new BufferedReader(new InputStreamReader(System.in));
            else
                br = new BufferedReader(new FileReader(ruta));
            
            while((cadena=br.readLine()) != null && !cadena.trim().equals("--EOF--")){
                String[] comentarios = cadena.split("#");
                if(comentarios[0] != null && !comentarios[0].equals("")){
                    String[] informacion = comentarios[0].split(" ");
                    for (String var : informacion) {
                        try {
                            if(var != null && !var.equals("")){
                                entrada.agrega(Integer.parseInt(var)); 
                            }
                        } catch (NumberFormatException e) {
                                estructura = var;
                        }
                    }
                }
            }
            br.close();
            return entrada;
        } catch (IOException | NullPointerException e) {
            System.err.println("No se recivio una entrad adecuada");
            Proyecto2.uso();
            return null;
        }
    }

}