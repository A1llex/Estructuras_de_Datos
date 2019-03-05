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
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            if (cola.mira().hayIzquierdo()) 
                cola.mete(cola.mira().izquierdo);
            if (cola.mira().hayDerecho()) 
                cola.mete(cola.mira().derecho);    
            return cola.saca().elemento;
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
        Vertice padre;
        if(altura() == 0)
            padre = pUltimo();
        else
            padre = raiz;
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

    //lo que hago es recorrer el arbol conforme su nivel y su lugar en el nivel
    private Vertice pUltimo(){
        Vertice aux = raiz;
        int nivel = aux.altura();
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
        Vertice elim = (Vertice)busca(elemento);
        if(elim == null||esVacia())
            return;
        if(elim.equals(raiz)){
            limpia();
            return;
        }
        Vertice padre = pUltimo();
        elementos--;
        if(padre.hayIzquierdo()){
            elim.elemento = padre.derecho.elemento;
            padre.derecho = null;
        }
        else{
            elim.elemento = padre.izquierdo.elemento;
            padre.izquierdo = null;
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        if(esVacia())
            return -1;
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
        iterator();
        T elem = iterator().next();
        Vertice v = nuevoVertice(elem);
        accion.actua(v);
        while (iterator().hasNext()) {
            v = nuevoVertice(elem);
            accion.actua(v);
            elem = iterator().next();
        }
        /*Cola<Vertice> co = new Cola<>();
        co.mete(raiz);
        while (!co.esVacia()) {
            accion.actua(co.mira());
            if (co.mira().hayIzquierdo())
                co.mete(co.mira().izquierdo);
            if (co.mira().hayDerecho())
                co.mete(co.mira().derecho);
            co.saca();
        }*/
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
