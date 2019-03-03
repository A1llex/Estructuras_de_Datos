package mx.unam.ciencias.edd;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        public Iterador() {
            cola = new Cola<>();
            if (raiz != null)
                cola.mete(raiz);
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return (!cola.esVacia());
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            Vertice ver  = cola.saca();
            if(ver.hayIzquierdo())
                cola.mete(ver.izquierdo);
            if(ver.hayDerecho())
                cola.mete(ver.derecho);
            return ver.get();
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        Vertice ver = nuevoVertice(elemento);
        if(esVacia()){
            raiz = ver;
            elementos = 1;
            return;
        }
        Vertice padre = pUltimo();
        if(padre.hayIzquierdo()){
            padre.derecho = ver;
            ver.padre = padre;
        }
        else{
            padre.izquierdo = ver;
            ver.padre = padre;
        }
        elementos++;
    }

    private Vertice pUltimo(){
        Vertice aux = raiz;
        int nivel = raiz.altura();
        if((elementos+1) != Math.pow(2, nivel+1)){
            int cont = 1,lugar = (elementos+1)-(int)Math.pow(2, nivel); 
            for (int i = 0; i < (nivel-1); i++) {
                if ((lugar/Math.pow(2, nivel-i-1)) < cont )
                    aux = aux.izquierdo;
                else{
                    aux = aux.derecho;
                    cont= 2*(cont+1);
                }
            }
            return aux;
        }else{
            while (aux.hayIzquierdo()) 
                aux = aux.izquierdo;
            return aux;
        }
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice cambiar = (Vertice)busca(elemento);
        if(cambiar == null||esVacia())
            return;
        Vertice padre = pUltimo();
        elementos--;
        if(elementos == 0){
            limpia();
            return;
        }
        if(cambiar.elemento == null)
            System.out.println("esto no me gusta");
        if(padre.hayIzquierdo()){
            cambiar.elemento = padre.derecho.elemento;
            padre.derecho = null;
        }
        else{
            cambiar.elemento = padre.izquierdo.elemento;
            padre.izquierdo = null;
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        return (int)(Math.floor(Math.log(getElementos())/Math.log(2)));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        if (esVacia())
            return;
        T elem = iterator().next();
        while (iterator().hasNext()) {
            Vertice v = nuevoVertice(elem);
            accion.actua(v);
            elem = iterator().next();
        }
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
