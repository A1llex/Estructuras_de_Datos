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
        if(vertice.getClass() != VerticeRojinegro.class )
        	throw new ClassCastException();
        VerticeRojinegro ver = (VerticeRojinegro) vertice;
        return ver.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
    	super.agrega(elemento);
    	VerticeRojinegro ultimo = (VerticeRojinegro)super.ultimoAgregado;
    	ultimo.color = Color.ROJO;
    	balanceaAgrega(ultimo);
    }
   	/**
   	 * Funcion auxiliar de que balancea.
   	 */ 
    private void balanceaAgrega(VerticeRojinegro ver){

    	//Caso 1
    	if(!ver.hayPadre()){
    		ver.color = Color.NEGRO;
    		return;
    	}
    	//Caso 2
    	VerticeRojinegro p = (VerticeRojinegro)ver.padre;
    	if(p.color == Color.NEGRO)
    		return;

    	//Caso 3
    	VerticeRojinegro a = (VerticeRojinegro) p.padre;	
    	VerticeRojinegro t = (a.izquierdo != p) ? (VerticeRojinegro)a.izquierdo : (VerticeRojinegro)a.derecho;

    	if(t != null && t.color == Color.ROJO){
    		t.color = p.color = Color.NEGRO;
    		a.color = Color.ROJO;
    		balanceaAgrega(a);
    		return;
    	}

    	//Caso 4
    	if(a.izquierdo == p && p.derecho == ver){
    		super.giraIzquierda(p);
    		VerticeRojinegro aux = p;
	    	p = ver;
	    	ver = aux;
    	
    	}
    	else if(a.derecho == p && p.izquierdo == ver){
    		super.giraDerecha(p);
	    	VerticeRojinegro aux = p;
	    	p = ver;
	    	ver = aux;
    	}

    	//Caso 5
    	p.color = Color.NEGRO;
    	a.color = Color.ROJO;

    	if(p.izquierdo == ver)
    		super.giraDerecha(a);
    	else
    		super.giraIzquierda(a);

    }

    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {

    	VerticeRojinegro ver = busca((VerticeRojinegro)super.raiz, elemento);
    	if(ver == null)
    		return;
    	VerticeRojinegro aux = null;

    	if(ver.hayIzquierdo()){
    		
    		aux = ver;
    		ver = subMax((VerticeRojinegro)ver.izquierdo);
    		aux.elemento = ver.get();
    		aux = null;
    	}
    	
    	if(!ver.hayIzquierdo() && !ver.hayDerecho()){
    		ver.izquierdo = nuevoVertice(null);
    		aux = (VerticeRojinegro)ver.izquierdo;
    		aux.padre = ver;
    		aux.color = Color.NEGRO;
    	}
    	VerticeRojinegro h = (ver.hayIzquierdo()) ? (VerticeRojinegro)ver.izquierdo : (VerticeRojinegro)ver.derecho; 
    	desconecta(ver, h);


    	if(h.color == Color.ROJO || ver.color == Color.ROJO){
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

    /**
     * Rebalancea el vertice sobre v
     */
    private void balanceaElimina(VerticeRojinegro ver){

    	//Caso 1
    	if(!ver.hayPadre()){
    		ver.color = Color.NEGRO;
    		raiz = ver;
    		return;
    	}

    	VerticeRojinegro p = (VerticeRojinegro)ver.padre;
    	VerticeRojinegro h = verticeHermano(ver); 	

    	//Caso 2
    	if(h.color == Color.ROJO){
    		p.color = Color.ROJO;
    		h.color = Color.NEGRO;
    		if(esHijoDerecho(ver))
    			super.giraDerecha(p);
    		else
    			super.giraIzquierda(p);
    		h = verticeHermano(ver);
    		p = (VerticeRojinegro)ver.padre;
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
    	if(!esHijoDerecho(ver) && !esNegro(hi) && esNegro(hd) && esNegro(h)){
    		h.color = Color.ROJO;
    		hi.color = Color.NEGRO;
    		super.giraDerecha(h);
    		h = verticeHermano(ver);
    		hi = (VerticeRojinegro)h.izquierdo;
    		hd = (VerticeRojinegro)h.derecho;
    	}
    	else if(esHijoDerecho(ver) && esNegro(hi) && !esNegro(hd) && esNegro(h)){
    		h.color = Color.ROJO;
    		hd.color = Color.NEGRO;
    		super.giraIzquierda(h);
    		h = verticeHermano(ver);
    		hi = (VerticeRojinegro)h.izquierdo;
    		hd = (VerticeRojinegro)h.derecho;
    	}

    	//Caso 6
    	h.color = p.color;
    	p.color = Color.NEGRO;
    	if(esHijoDerecho(ver)){
    		hi.color = Color.NEGRO;
    		super.giraDerecha(p);
    	}
    	else{ 
    		hd.color = Color.NEGRO;
    		super.giraIzquierda(p);
    	}
    }

	private boolean esNegro(VerticeRojinegro vertice){
		return (vertice == null || vertice.color == Color.NEGRO);
	}

    private boolean esHijoDerecho(VerticeRojinegro vertice){
    	return (vertice.padre.derecho == vertice);
    } 

    /**
     * Regresa el verticeHermano de v
     */
    private VerticeRojinegro verticeHermano(VerticeRojinegro vertice){
    	if(vertice.padre.derecho == vertice)
    		return (VerticeRojinegro)vertice.padre.izquierdo;
    	else return (VerticeRojinegro)vertice.padre.derecho;
    }

    /**
     * Regresa el primer vértice con elemento e, si no existe
     * regresa null.
     */
    private VerticeRojinegro busca(VerticeRojinegro vertice, T e){
    	if(vertice == null)
    		return null;
    	int r = e.compareTo(vertice.get());
    	if(r == 0)
    		return vertice;
    	else if(r < 0)
    		return busca((VerticeRojinegro)vertice.izquierdo, e);
    	else
    		return busca((VerticeRojinegro)vertice.derecho, e);
    }

    /**
     * Regresa el maximo del subarbol a partir de v
     */
    private VerticeRojinegro subMax(VerticeRojinegro v){

    	if(v.hayDerecho())
    		return subMax((VerticeRojinegro)v.derecho);
    	return v;
    }

    /**
     * Desconecta al nodo y pone a su hijo en su lugar
     */
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
			if(!esHijoDerecho(v))
    			v.padre.izquierdo = v.izquierdo;
    		else
    			v.padre.derecho = v.izquierdo;
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