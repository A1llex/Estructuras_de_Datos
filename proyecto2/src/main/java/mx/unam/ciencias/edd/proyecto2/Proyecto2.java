package mx.unam.ciencias.edd.proyecto2;

/**
 * Clase Diseñada para el Proyecto1.
 * @author Alex Gerardo Fernandez Aguilar
 *
*/
public class Proyecto2 {

    /**
     * Instrucciones del uso del Ordenador Lexicografico
     */
    public static void uso() {
        System.err.println("--------------------------------------[Uso]-------------------------------------");
        System.err.println("java -jar proyecto2.jar  [Archivo a Leer]");
        System.err.println("java -jar proyecto2.jar [Entrada de consola]");
        System.err.println("En el inicio del archivo se debe encontrar el nombre de una Estructura de Datos valida");
        System.err.println("--------------------------------------------------------------------------------");
        System.exit(1);
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        // GraficadorSVG grafica = new GraficadorSVG();
        // grafica.run(args);
        String[] a = {"D:/Users/Documents/PROGRAMACION/EDD/proyecto2/prueba.txt"};
        System.out.println("supuesta ruta "+a[0]);
        GraficadorSVG grafica = new GraficadorSVG();
        grafica.run(a);
    }

}
