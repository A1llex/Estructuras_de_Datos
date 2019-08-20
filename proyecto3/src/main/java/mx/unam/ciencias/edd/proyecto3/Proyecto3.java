package mx.unam.ciencias.edd.proyecto3;

/**
 * Clase Diseñada para el Proyecto1.
 * @author Alex Gerardo Fernandez Aguilar
 *
*/
public class Proyecto3 {

    /**
     * Instrucciones del uso del Ordenador Lexicografico
     */
    public static void uso() {
        System.err.println("--------------------------------------[Uso]-------------------------------------");
        System.err.println("java -jar proyecto3.jar  [Archivos a Leer] -o [Ruta de Directorio destino]");
        System.err.println("Se creara o sobreescribira la ruta destino");
        System.err.println("--------------------------------------------------------------------------------");
        System.exit(1);
    }

    /**
     * Main
     */
    public static void main(String[] args) {
        GeneradorDeReportes reporte = new GeneradorDeReportes();
        reporte.run(args);
    }

}
