package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<tt>null</tt>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            super(elemento);
            if(elemento == null)
                color = Color.NEGRO;
            else
                color = Color.ROJO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        public String toString() {
            return ((esNegro(this)) ? "N":"R") + "{" +  ((this.elemento == null) ? "null":this.elemento.toString()) + "}";
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            if (altura() == vertice.altura()) {
                if(color != vertice.color)
                    return false;
                if(!elemento.equals(vertice.elemento))
                    return false;
                if(hayIzquierdo())
                    if(!vertice.hayIzquierdo())
                        return false;
                    else if(!izquierdo.equals(vertice.izquierdo))
                        return false;
                if(hayDerecho())
                    if(!vertice.hayDerecho())
                        return false;
                    else if(!derecho.equals(vertice.derecho))
                        return false;
                return true;
            }
            return false;
        }
    }

    
    private boolean esNegro(VerticeRojinegro ver) {
        return (ver == null || ver.color == Color.NEGRO);
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        return verticeRojinegro(vertice).color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        super.agrega(elemento);
        VerticeRojinegro ultimo = verticeRojinegro(getUltimoVerticeAgregado());
        ultimo.color = Color.ROJO;
        balanceaAgrega(ultimo);
    }

    //metodo auxiliar para vlancear despues de agregar un vertice
    private void balanceaAgrega(VerticeRojinegro ver){
        VerticeRojinegro padre,tio,abuelo;

        if(!ver.hayPadre()){
            ver.color = Color.NEGRO;
            return;
        } 
        padre = verticeRojinegro(ver.padre);
        if(padre.color == Color.NEGRO){
            return;
        } 

        abuelo = verticeRojinegro(ver.padre.padre);
        if (esHijoIzquierdo(padre)) {
            tio = verticeRojinegro(ver.padre.padre.derecho);
            if ( tio != null && tio.color == Color.ROJO) {
                tio.color = Color.NEGRO;
                padre.color = Color.NEGRO;
                abuelo.color = Color.ROJO;
                balanceaAgrega(abuelo);
                return;
            }
        } else {
            tio = verticeRojinegro(ver.padre.padre.izquierdo);
            if ( tio != null && tio.color == Color.ROJO) {
                tio.color = Color.NEGRO;
                padre.color = Color.NEGRO;
                abuelo.color = Color.ROJO;
                balanceaAgrega(abuelo);
                return;
            }
        }

        if (esHijoIzquierdo(ver) != esHijoIzquierdo(padre)) {
            if (esHijoIzquierdo(padre)) {
                super.giraIzquierda(padre);
            } else {
                super.giraDerecha(padre);
            }
            VerticeRojinegro aux;
            aux = padre;
            padre = ver;
            ver = aux;
        }

        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        if (esHijoIzquierdo(ver)) {
            super.giraDerecha(abuelo);
        } else {
            super.giraIzquierda(abuelo);
        }
    }

    //hace un cast al vertice para poder operar con el como un Vertice de arbol Rojinegro
    private VerticeRojinegro verticeRojinegro(VerticeArbolBinario<T> ver) {
        return (VerticeRojinegro)ver;
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

    //regresa la direccion del hermano de un vertice
    private VerticeRojinegro veticeHermano(VerticeRojinegro ver){
        if(esHijoIzquierdo(ver))
            return verticeRojinegro(ver.padre.derecho);
        else
            return verticeRojinegro(ver.padre.izquierdo);
    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        VerticeRojinegro v = verticeRojinegro(busca(elemento));
    	if(v == null)
    		return;
    	VerticeRojinegro aux = null;

    	if(v.hayIzquierdo()){
    		
    		aux = v;
    		v = subMax((VerticeRojinegro)v.izquierdo);
    		aux.elemento = v.get();
    		aux = null;
    	}
    	
    	if(!v.hayIzquierdo() && !v.hayDerecho()){
    		v.izquierdo = nuevoVertice(null);
    		aux = (VerticeRojinegro)v.izquierdo;
    		aux.padre = v;
    		aux.color = Color.NEGRO;
    	}
    	VerticeRojinegro h = (v.hayIzquierdo()) ? (VerticeRojinegro)v.izquierdo : (VerticeRojinegro)v.derecho; 
    	desconecta(v, h);


    	if(h.color == Color.ROJO || v.color == Color.ROJO){
    		h.color = Color.NEGRO;
    	}
    	else {
    		h.color = Color.NEGRO;
    		balanceaElimina(h);
    	}

    	if(aux != null){
    		if(!aux.hayPadre()){
    			super.raiz = null;
    			ultimoAgregado = aux = null;
    		}
    		else if(esHijoDerecho(aux)){
    			aux.padre.derecho = null;
    		}
    		else 
    			aux.padre.izquierdo = null;
    	}
    	elementos--;

    }

    private VerticeRojinegro subMax(VerticeRojinegro v){

    	if(v.hayDerecho())
    		return subMax((VerticeRojinegro)v.derecho);
    	return v;
    }

    private void desconecta(VerticeRojinegro v, VerticeRojinegro h){
    
    	if(!v.hayPadre()){
    		raiz = h;
    		raiz.padre = null;
    		return;
    	}

		h.padre = v.padre;
    	if(v.derecho == h){
    		if(!esHijoDerecho(v))
    			v.padre.izquierdo = v.derecho;
    		else
    			v.padre.derecho = v.derecho;
    	}
    	if(v.izquierdo == h){
			if(!esHijoIzquierdo(v))
    			v.padre.izquierdo = v.izquierdo;
    		else
    			v.padre.derecho = v.izquierdo;
    	}

    }

    private void balanceaElimina(VerticeRojinegro v){
        
    	//Caso 1
    	if(!v.hayPadre()){
    		v.color = Color.NEGRO;
    		raiz = v;
    		return;
    	}

    	VerticeRojinegro p = (VerticeRojinegro)v.padre;
    	VerticeRojinegro h = veticeHermano(v); 	

    	//Caso 2
    	if(h.color == Color.ROJO){
    		p.color = Color.ROJO;
    		h.color = Color.NEGRO;
    		if(esHijoDerecho(v))
    			super.giraDerecha(p);
    		else
    			super.giraIzquierda(p);
    		h = veticeHermano(v);
    		p = (VerticeRojinegro)v.padre;
    	}

    	VerticeRojinegro hi = (VerticeRojinegro)h.izquierdo;
    	VerticeRojinegro hd = (VerticeRojinegro)h.derecho;
 		

    	//Caso 3
    	if(esNegro(hi) && esNegro(hd) && esNegro(p) && esNegro(h)){
    		h.color = Color.ROJO;
    		balanceaElimina(p);
    		return;
    	}

    	//Caso 4
    	if(esNegro(hi) && esNegro(hd) && !esNegro(p) && esNegro(h)){
    		h.color = Color.ROJO;
    		p.color = Color.NEGRO;
    		return;
    	}

    	//Caso 5
    	if(!esHijoDerecho(v) && !esNegro(hi) && esNegro(hd) && esNegro(h)){
    		h.color = Color.ROJO;
    		hi.color = Color.NEGRO;
    		super.giraDerecha(h);
    		h = veticeHermano(v);
    		hi = (VerticeRojinegro)h.izquierdo;
    		hd = (VerticeRojinegro)h.derecho;
    	}
    	else if(esHijoDerecho(v) && esNegro(hi) && !esNegro(hd) && esNegro(h)){
    		h.color = Color.ROJO;
    		hd.color = Color.NEGRO;
    		super.giraIzquierda(h);
    		h = veticeHermano(v);
    		hi = (VerticeRojinegro)h.izquierdo;
    		hd = (VerticeRojinegro)h.derecho;
    	}

    	//Caso 6
    	h.color = p.color;
    	p.color = Color.NEGRO;
    	if(esHijoDerecho(v)){
    		hi.color = Color.NEGRO;
    		super.giraDerecha(p);
    	}
    	else{ 
    		hd.color = Color.NEGRO;
    		super.giraIzquierda(p);
    	}
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
