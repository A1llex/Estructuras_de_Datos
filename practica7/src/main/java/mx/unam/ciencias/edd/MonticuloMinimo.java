package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends ComparableIndexable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return (indice < elementos) && (arbol[indice] != null);
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            if(hasNext())
                return arbol[indice++];
            throw new NoSuchElementException();
        }
    }

    /* Clase estática privada para adaptadores. */
    private static class Adaptador<T  extends Comparable<T>>
        implements ComparableIndexable<Adaptador<T>> {

        /* El elemento. */
        private T elemento;
        /* El índice. */
        private int indice;

        /* Crea un nuevo comparable indexable. */
        public Adaptador(T elemento) {
            this.elemento = elemento;
            indice = -1;
        }

        /* Regresa el índice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Define el índice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Compara un adaptador con otro. */
        @Override public int compareTo(Adaptador<T> adaptador) {
            return (0);
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new ComparableIndexable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
        elementos = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        elementos = n;
        arbol = nuevoArreglo(n);
        int i = 0;
        for (T var : iterable) {
            arbol[i] = var;
            arbol[i].setIndice(i);
            i++;
        }
        for (int j = n/2 ; j >= 0; j--) {
            reordenaAbajo(arbol[j]);
        }
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        if (arbol.length <= elementos) {
            T[] nArbol = nuevoArreglo(arbol.length * 2);
            for (int i = 0; i < arbol.length; i++) {
                nArbol[i] = arbol[i];
            }
            arbol = nArbol;
        }
        arbol[elementos] = elemento;
        arbol[elementos].setIndice(elementos++);
        if(elementos > 1)
            reordenaArriba(elemento);
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        if(elementos == 0)
            throw new IllegalStateException();
        T aux = arbol[0];
        if(elementos != 1){
            arbol[0] = arbol[elementos-1];
            arbol[0].setIndice(0);
            arbol[--elementos] = null;
            reordenaAbajo(arbol[0]);
        }else{
            limpia();    
        }
        aux.setIndice(-1);
        return aux;
    }


    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        if(elemento.getIndice() < 0||elemento.getIndice() > elementos || !contiene(elemento))
            return;
        if(elementos == 1){
            limpia();
            return;
        }
        int indx=-1;
        for (T var : arbol) {
            if(elemento.equals(var))
                indx = var.getIndice();
        }
        if(indx == -1){
            System.out.println("error");
            return;
        }
        T aux = arbol[elementos-1];
        arbol[indx] = arbol[elementos-1];
        arbol[indx].setIndice(indx);
        //arbol[elementos-1].setIndice(-1);
        arbol[elementos-1] = null;
        elementos--;
        reordenaAbajo(arbol[indx]);
        aux.setIndice(-1);
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (T var : arbol)
            if(var != null && elemento.equals(var))
                return true;
        return false;
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <tt>true</tt> si ya no hay elementos en el montículo,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean esVacia() {
        return (elementos == 0);
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        elementos = 0;
        arbol = nuevoArreglo(100); /* 100 es arbitrario. */
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        if(elemento == null|| elementos <= 1)
            return;
        reordenaArriba(elemento);
        reordenaAbajo(elemento);
    }

    private void reordenaArriba(T elemento){
        if(elemento == null || elemento.getIndice() == 0)
            return;
        int indx = elemento.getIndice(), pindx = (indx-1)/2;
        if (arbol[pindx].compareTo(elemento) > 0) {
            T aux = arbol[indx];
            arbol[indx] = arbol[pindx];
            arbol[indx].setIndice(indx);
            arbol[pindx] = aux;
            arbol[pindx].setIndice(pindx);
            reordenaArriba(arbol[pindx]);
        }
    }
    private void reordenaAbajo(T elemento){
        if(elemento == null || elemento.getIndice() >= elementos)
            return;
        int indx = elemento.getIndice();
        T min;
        if((indx*2+2) >= elementos){
            if((indx*2+1) >= elementos)
                return;
            else
                min = arbol[indx*2+1];
        }else{
            min = (arbol[indx*2+1].compareTo(arbol[indx*2+2]) < 0)? arbol[indx*2+1]: arbol[indx*2+2];
        }
        if(elemento.compareTo(min) > 0){
            int minindx = min.getIndice();
            arbol[minindx] = arbol[indx] ;
            arbol[minindx].setIndice(minindx);
            arbol[indx] = min;
            arbol[indx].setIndice(indx);
            reordenaAbajo(arbol[minindx]);
        }
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        if(i<0 || i >= elementos)
            throw new  NoSuchElementException();
        return arbol[i];
    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        String s="";
        for (T var : arbol) {
            s+= var.toString()+", ";
        }
        return s;
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        if(getElementos() != monticulo.getElementos())
            return false;
        
        for (int i = 0; i < elementos; i++) {
            if(!arbol[i].equals(monticulo.get(i)))
                return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        return null;
    
        /*MonticuloMinimo<Adaptador> mm = new MonticuloMinimo<Adaptador>(coleccion);*/        
    }
}
