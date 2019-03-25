package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        public Iterador() {
            pila = new Pila<>();
            if (raiz == null)
                return;
            Vertice ver = raiz;
            while (ver != null) {
                pila.mete(ver);
                ver = ver.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            Vertice ver = pila.saca(),aux;
            if(ver.hayDerecho()){
                aux = ver.derecho;
                while (aux != null) {
                    pila.mete(aux);
                    aux = aux.izquierdo;
                }
            }
            return ver.elemento;
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null)
            throw new IllegalArgumentException();
        Vertice ver = nuevoVertice(elemento);
        if(esVacia()){
            raiz = ver;
            ultimoAgregado = ver;
            elementos = 1;
            return;
        }
        agrega(raiz,elemento);
    }

    private void agrega(Vertice ver, T elemento){
        if(ver.elemento.compareTo(elemento) >= 0){
            if(!ver.hayIzquierdo()){
                ver.izquierdo = nuevoVertice(elemento);
                ver.izquierdo.padre = ver;
                ultimoAgregado = ver.izquierdo;
                elementos++;
                return;
            }
            agrega(ver.izquierdo,elemento);
        } else {
            if(!ver.hayDerecho()){
                ver.derecho = nuevoVertice(elemento);
                ver.derecho.padre = ver;
                ultimoAgregado = ver.derecho;
                elementos++;
                return;
            }
            agrega(ver.derecho,elemento);
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Vertice elim =  vertice(busca(elemento));
        if(elim == null||esVacia())
            return;
        if(elim == raiz && elementos == 1){
            limpia();
            return;
        }
        elementos --;
        if(!elim.hayIzquierdo()&&!elim.hayDerecho()){
            if(esHijoIzquierdo(elim))
                elim.padre.izquierdo = null;
            if(esHijoDerecho(elim))
                elim.padre.derecho = null;
            return;
        }

        Vertice aux = intercambiaEliminable(elim);
        elim.elemento = aux.elemento;
        eliminaVertice(aux);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        Vertice aux;
        if (vertice.hayIzquierdo()) {
            aux = vertice.izquierdo;
            while (aux.hayDerecho()) {
                aux = aux.derecho;
            }
            return aux;
        } else {
            aux = vertice.derecho;
            while (aux.hayIzquierdo()) {
                aux = aux.izquierdo;
            }
            return aux;
        }
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        if(esHijoIzquierdo(vertice)){
            if (vertice.hayIzquierdo()) {
                vertice.izquierdo.padre = vertice.padre;
                vertice.padre.izquierdo =vertice.izquierdo;
            } else if(vertice.hayDerecho()){
                vertice.derecho.padre = vertice.padre;
                vertice.padre.izquierdo =vertice.derecho;
            }else{
                vertice.padre.izquierdo = null;
            }
        }else{
            if (vertice.hayIzquierdo()) {
                vertice.izquierdo.padre = vertice.padre;
                vertice.padre.derecho =vertice.izquierdo;
            } else if(vertice.hayDerecho()){
                vertice.derecho.padre = vertice.padre;
                vertice.padre.derecho =vertice.derecho;
            }else{
                vertice.padre.derecho = null;
            }
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <tt>null</tt>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <tt>null</tt> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        if(elemento == null)
            return null;
        return busca(raiz, elemento);
    }
    
    //funcion auxiliar recursiva para la busqueda
    Vertice busca(Vertice vertice, T elemento) {
        Vertice iz;
        if (vertice == null) {
            return null;
        }
        iz = this.busca(vertice.izquierdo, elemento);
        if (iz != null) {
            return iz;
        }
        if (vertice.elemento.equals(elemento)) {
            return vertice;
        }
        return this.busca(vertice.derecho, elemento);
    }


    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        if(vertice == null||!vertice.hayIzquierdo())
            return;
        Vertice ver = vertice(vertice);
        Vertice izq = ver.izquierdo;
        if(!ver.hayPadre()){
            raiz = izq;
            izq.padre = null;
        }else{
            izq.padre = ver.padre;
            if(ver.padre.izquierdo == ver)
                ver.padre.izquierdo = izq;
            else
                ver.padre.derecho= izq;
        }
        ver.izquierdo = izq.derecho;
        if(ver.izquierdo != null)
            ver.izquierdo.padre = ver;
        ver.padre = izq;
        izq.derecho = ver;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        if(vertice == null||!vertice.hayDerecho())
            return;
        Vertice ver = vertice(vertice);
        Vertice der = ver.derecho;
        if(!ver.hayPadre()){
            raiz = der;
            der.padre = null;
        }else{
            der.padre = ver.padre;
            if(ver.padre.derecho == ver)
                ver.padre.derecho= der;
            else
                ver.padre.izquierdo= der;
        }
        ver.derecho = der.izquierdo;
        if(ver.derecho != null)
            ver.derecho.padre = ver;
        ver.padre = der;
        der.izquierdo = ver;
    }    
    //nos dice si el vertice es hijo izquierdo de vertice
    private boolean esHijoIzquierdo(Vertice ver) {
        if (!ver.hayPadre())
            return false;
        return ver.padre.izquierdo == ver;
    }
    //nos dice si el vertice es hijo derecho de otro vertice
    private boolean esHijoDerecho(Vertice ver) {
        if (!ver.hayPadre())
            return false;
        return ver.padre.derecho == ver;
    }
    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        if (esVacia())
            return;
        dfs(raiz,accion, -1); 
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        if (esVacia())
            return;
        dfs(raiz,accion, 0);
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        if (esVacia())
            return;
        dfs(raiz,accion, 1);
    }

    //recorre en dfs el arbol y ejecuta dependiendo a si es pre,in,post order
    private void dfs(Vertice ver ,AccionVerticeArbolBinario<T> accion,int lug){
        if(lug == -1)
            accion.actua(ver);
        if (ver.hayIzquierdo()) {
            dfs(ver.izquierdo, accion, lug);
        }
        if(lug == 0)
            accion.actua(ver);
        if (ver.hayDerecho()) {
            dfs(ver.derecho, accion, lug);
        }
        if(lug == 1)
            accion.actua(ver);
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
