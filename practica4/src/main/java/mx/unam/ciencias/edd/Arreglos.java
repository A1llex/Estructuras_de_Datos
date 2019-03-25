package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        if((arreglo == null)||(arreglo.length<=1))
            return;
        quickSort(arreglo,0,(arreglo.length-1),comparador);
    }

    private static <T> void
    quickSort(T[] arreglo,int ini,int fin, Comparator<T> comparador) {
        if (fin <= ini) 
            return;
        //aqui ini sera el pivote
        int i=(ini+1),j=fin;
        while(i<j){
            if ((comparador.compare(arreglo[i],arreglo[ini]) >0)&&(comparador.compare(arreglo[j], arreglo[ini]) <=0)) {
                T aux = arreglo[i];
                arreglo[i] = arreglo[j];
                arreglo[j] = aux;
                i++;j--;
            } else if (comparador.compare(arreglo[i], arreglo[ini])<=0) {
                i++;
            }else {
                j--;
            }
        }
        if (comparador.compare(arreglo[i], arreglo[ini])>0) 
            i--;
        T aux = arreglo[ini];
        arreglo[ini] = arreglo[i];
        arreglo[i] = aux;
        
        //con un sub arreglo menor al pivote
        quickSort(arreglo,ini,i-1,comparador);
        //un subarreglo mayor que el pivote
        quickSort(arreglo,i+1,fin,comparador);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        int lon = arreglo.length;
        for (int i = 0; i < lon-1; i++) {
            int min = i;
            for (int j = i+1; j < lon; j++) {
                if (comparador.compare(arreglo[j], arreglo[min])<0)
                    min = j;
            }
            T aux = arreglo[min];
            arreglo[min] = arreglo[i];
            arreglo[i] = aux;
        }
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */  
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        if(elemento == null || arreglo == null)
            return -1;
        return busquedaBinaria(arreglo,elemento, 0, (arreglo.length -1),comparador);
    }
     
     
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento,int ini,int fin, Comparator<T> comparador) {
        if(fin<ini)
            return -1;
        if(comparador.compare(arreglo[ini],elemento) == 0) 
            return ini;
        if(comparador.compare(arreglo[fin],elemento) == 0) 
            return fin;
        int mid = (int)((fin-ini)/2);
        if(comparador.compare(arreglo[mid],elemento) == 0) 
            return mid;
        if(comparador.compare(elemento,arreglo[mid]) < 0) 
            return busquedaBinaria(arreglo,elemento, (ini+1), (mid-1),comparador);
        else
        	return busquedaBinaria(arreglo,elemento, (mid+1), (fin-1),comparador);
    }
     

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
