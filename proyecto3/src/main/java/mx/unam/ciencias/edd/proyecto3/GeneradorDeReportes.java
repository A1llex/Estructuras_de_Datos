package mx.unam.ciencias.edd.proyecto3;

import java.util.Iterator;

import mx.unam.ciencias.edd.Diccionario;
import mx.unam.ciencias.edd.Lista;

/**
 * Clase para ordenar los archivos o entrada estandar junto con banderas para
 * modificar la salida o guardar la salida en un archivo
 */
public class GeneradorDeReportes {

    /**
     * el programa leera la entrada estandar en busca de archivos a leer y ruta destino para generar una presentacion
     * sobre las palabras de cada archivo leido
     * @param args la entrada de la consola
     */
    public void run(String[] args) {
        Lista<String> archivos = new Lista<>();
        String ruta = null;
        int bo = -1;

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-o")) {
                    bo = i;
                    ruta = args[++i];
                } else {
                    archivos.agrega(args[i]);
                }
            }
        }

        if (bo == -1) {
            System.err.println("Se debe usar la bandera -o seguido de una ruta de destino");
            Proyecto3.uso();
        }

        if (ruta == null) {
            System.out.println("Error la ruta es null");
            System.exit(1);
        }

        Lista<Diccionario<String, Integer>> aux = new Lista<>();
        Entrada entrada = new Entrada();

        for (String archivo : archivos) {
            aux.agrega(entrada.lectura(archivo));
        }

        Salida salida = new Salida(ruta, archivos);
        salida.htmlIndex(aux);
        salida.html();

        System.out.println("Archivos Generados Exitosamente");
    }
}