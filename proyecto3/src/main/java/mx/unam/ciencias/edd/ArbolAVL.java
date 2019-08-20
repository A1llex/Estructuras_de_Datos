package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            super(elemento);
            altura = 0;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            return this.elemento.toString()+" "+altura+"/"+(balance(this));
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            return (altura == vertice.altura && super.equals(objeto));
        }
    }

    /* Convierte el vértice a VerticeAVL */
    private VerticeAVL verticeAVL(VerticeArbolBinario<T> vertice) {
        return (VerticeAVL)vertice;
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeAVL(elemento);
    }
    
    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        balanceoAVL(verticeAVL(getUltimoVerticeAgregado()));
    }


    private void actualizaAltura(VerticeAVL vertice){
        // if(vertice == null)
        //     return;
        if (!vertice.hayIzquierdo()&&!vertice.hayDerecho()){
            vertice.altura = 0;
        }else{
            if(vertice.hayIzquierdo()){
                if(!vertice.hayDerecho())
                    vertice.altura = 1 + verticeAVL(vertice.izquierdo).altura;
                else
                    vertice.altura = 1+(Math.max( verticeAVL(vertice.izquierdo).altura , verticeAVL(vertice.derecho).altura));
            }else{
                vertice.altura = 1 + verticeAVL(vertice.derecho).altura;
            }
        } 
        if (vertice.hayPadre())
            actualizaAltura(verticeAVL(vertice.padre));     
    }

    private int balance(VerticeAVL vertice){
        if(!vertice.hayIzquierdo() && !vertice.hayDerecho())
            return 0;
        if(vertice.hayIzquierdo()){
            if(vertice.hayDerecho())
                return (verticeAVL(vertice.izquierdo).altura() - verticeAVL(vertice.derecho).altura());
            else
                return verticeAVL(vertice.izquierdo).altura() +1 ;
        }else 
            return ( - 1 - verticeAVL(vertice.derecho).altura());
    }
    
    private void balanceoAVL(VerticeAVL vertice){
        actualizaAltura(vertice);
        VerticeAVL der,izq;
        if(balance(vertice) == -2){
            if (balance(der = verticeAVL(vertice.derecho)) == 1 ) {
                super.giraDerecha(der);
                actualizaAltura(der);
            }
            super.giraIzquierda(vertice);
            actualizaAltura(vertice);
        }
        if(balance(vertice) == 2){
            if (balance(izq = verticeAVL(vertice.izquierdo)) == -1 ) {
                super.giraIzquierda(izq);
                actualizaAltura(izq);
            }
            super.giraDerecha(vertice);
            actualizaAltura(vertice);
        }

        if (vertice.hayPadre())
            balanceoAVL(verticeAVL(vertice.padre));
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
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
            VerticeAVL aux = verticeAVL(elim.padre());
            if(esHijoIzquierdo(elim))
                elim.padre.izquierdo = null;
            if(esHijoDerecho(elim))
                elim.padre.derecho = null;
            balanceoAVL(aux);    
            return;
        }

        Vertice inter = intercambiaEliminable(elim);
        VerticeAVL  aux = verticeAVL(inter.padre);
        elim.elemento = inter.elemento;
        eliminaVertice(inter);
        balanceoAVL(aux);
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
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
