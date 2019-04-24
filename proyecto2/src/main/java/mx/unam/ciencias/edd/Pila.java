package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        if (esVacia()) 
            return "";
        Nodo aux = cabeza;
        String cadena = "";
        while (aux != null) {
            cadena += String.valueOf(aux.elemento) + "\n";
            aux = aux.siguiente;
        }
        return cadena;
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if (elemento == null) 
            throw new IllegalArgumentException();
        Nodo nuevo = new Nodo(elemento);
        if (esVacia()) {
            cabeza = rabo = nuevo;
        }else {
            nuevo.siguiente= cabeza;
            cabeza = nuevo;
        }
    }
}
