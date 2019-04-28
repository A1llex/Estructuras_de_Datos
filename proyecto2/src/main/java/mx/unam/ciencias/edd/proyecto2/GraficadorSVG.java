package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;


/**
 * Clase para ordenar los archivos o entrada estandar  junto con banderas para modificar la salida
 * o guardar la salida en un archivo
 */
public class GraficadorSVG {

    Entrada entrada;
    Salida salida;

    /**
     * Constructor para inicializar lo que se utilizara
     */
    public GraficadorSVG(){
        entrada =  new Entrada();
        salida = new Salida();
    }

    /**
     * Run ejecutara la lectura del aentrada estandr o lectura de un archivo para gener codigo SVG si se detecta una estructura valida
     * @param  args  sera de donde se leera el archivo de entrada si se presenta el caso
     */
    public void run(String[] args) {
        if(args.length > 1){
            System.err.println("Se debe enviar un  solo archivo de lectura");
            Proyecto2.uso();
        }
        Lista<Integer> nueva = entrada.entrada(args[0]);
        String estructura  = entrada.getEstructura();
        
        if (nueva == null) {
            System.err.println("No se puede leer de el archivo deseado");
            Proyecto2.uso();
        }        
        salida.imprime(estructura,nueva);
    }
}