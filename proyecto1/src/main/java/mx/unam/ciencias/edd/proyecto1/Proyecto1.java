package mx.unam.ciencias.edd.proyecto1;

/**
 * Clase Diseñada para el Proyecto1.
 * @author Alex Gerardo Fernandez Aguilar
 *
*/
public class Proyecto1 {

    /**
     * Instrucciones del uso del Ordenador Lexicografico
     */
    public static void uso() {
        System.err.println("--------------------------------------[Uso]-------------------------------------");
        System.err.println("java -jar proyecto1.jar [bandera(opcional)] [Archivo a Leer]");
        System.err.println("[metodo de entrada a consola] | java -jar proyecto1.jar [banderas o archivo de volcado]");
        System.err.println("--------------------------------------------------------------------------------");
        System.exit(1);
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        OrdenadorLexicografico ordenador = new OrdenadorLexicografico();
        ordenador.OrdLexico(args);
    }

}
