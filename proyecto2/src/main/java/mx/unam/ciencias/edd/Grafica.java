package mx.unam.ciencias.edd;

import java.awt.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

import mx.unam.ciencias.edd.ArbolBinario.Vertice;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class Grafica<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            iterador = vertices.iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            return iterador.next().get();
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La lista de vecinos del vértice. */
        public Lista<Vertice> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            if(elemento == null)
                return;
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            return vecinos.getElementos();
        }

        /* Regresa el color del vértice. */
        @Override public Color getColor() {
            return color;
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecinos;
        }
    }

    /* Vértices. */
    private Lista<Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public Grafica() {
        vertices = new Lista<>();
        aristas = 0;
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        return vertices.getLongitud();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        if(elemento == null||contiene(elemento))
            throw new IllegalArgumentException();
        vertices.agrega(new Vertice(elemento));
    }


	/**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(a == b||sonVecinos(a, b))
            throw new IllegalArgumentException();
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        va.vecinos.agrega(vb);
        vb.vecinos.agrega(va);
        aristas++;
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(a == b||!sonVecinos(a,b))
            throw new IllegalArgumentException();
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        va.vecinos.elimina(vb);
        vb.vecinos.elimina(va);
        aristas--;
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <tt>true</tt> si el elemento está contenido en la gráfica,
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        for (Vertice ver : vertices)
            if (ver.elemento.equals(elemento))
                return true;
        return false;
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        if(!contiene(elemento))
            throw new NoSuchElementException();
        Vertice ver = (Vertice) vertice(elemento);
        for (Vertice vec: ver.vecinos) {
            if(sonVecinos(ver.elemento, vec.elemento))
                desconecta(ver.elemento, vec.elemento);
        }
        vertices.elimina(ver);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <tt>true</tt> si a y b son vecinos, <tt>false</tt> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        if(va.vecinos != null && va.vecinos.contiene(vb))
            if(vb.vecinos != null && vb.vecinos.contiene(va))
                return true;
        return false;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        for (Vertice ver: vertices) {
            if(ver.elemento.equals(elemento))
                return ver;
        }
        throw new NoSuchElementException();
    }

    /**
     * Define el color del vértice recibido.
     * @param vertice el vértice al que queremos definirle el color.
     * @param color el nuevo color del vértice.
     * @throws IllegalArgumentException si el vértice no es válido.
     */
    public void setColor(VerticeGrafica<T> vertice, Color color) {
        if (vertice == null || vertice.getClass() != Vertice.class)
            throw new IllegalArgumentException("Vértice inválido");
        Vertice ver = (Vertice)vertice;
        ver.color = color;
    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        if(vertices == null || vertices.getLongitud() == 1)
            return true;
        fs(vertices.get(0).get(), v -> v.get(), true);
        boolean aux = true;
        for (Vertice ver : vertices) {
            if(ver.color != Color.ROJO)
                aux = false;
        }
        desColorear();
        return aux;
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        if(vertices == null)
            return;
        for (Vertice ver : vertices) {
            accion.actua(ver);
        }
    }

    private void desColorear(){
        if(vertices == null)
            return;
        for (Vertice ver : vertices) {
            ver.color = Color.NINGUNO;
        }
    } 
    
    //metodo auxiliar para el dfs y bfs
    private void fs(T elemento, AccionVerticeGrafica<T> accion, Boolean strc){
        if(vertices == null)
            return;
        if(!contiene(elemento))
            throw new NoSuchElementException();
        
        MeteSaca<Vertice> estruc;
        if (strc)
            estruc = new Cola<>();
        else
            estruc = new Pila<>();
        estruc.mete((Vertice)vertice(elemento));
        (estruc.mira()).color = Color.ROJO;
        while (!estruc.esVacia()){
            Vertice aux = estruc.saca();
            accion.actua(aux);
            for (Vertice vec : aux.vecinos) {
                if(vec.color != Color.ROJO){
                    vec.color = Color.ROJO;
                    estruc.mete(vec);
                }
            }    
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        fs(elemento, accion, true);
        desColorear();
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido. Al terminar el método, todos los vértices tendrán
     * color {@link Color#NINGUNO}.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        fs(elemento, accion, false);
        desColorear();
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return vertices.esVacia();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        aristas = 0;
        vertices = new Lista<>();
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        String salver="",salari = "";
        Lista<String> aris = new Lista<>();
        for (Vertice ver : vertices) {
            salver += ver.elemento+", ";
            for (Vertice vec : ver.vecinos) {
                if(aris == null || !(aris.contiene("("+ver.elemento+","+vec.elemento+")")||
                aris.contiene("("+vec.elemento+","+ver.elemento+")"))){
                    aris.agrega("("+ver.elemento+","+vec.elemento+")");
                    salari += "("+ver.elemento+", "+vec.elemento+"), ";
                }
            }
        }
        return"{"+salver+"}, {"+salari+"}";
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <tt>true</tt> si la gráfica es igual al objeto recibido;
     *         <tt>false</tt> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Grafica<T> grafica = (Grafica<T>)objeto;
        
        if(this.getElementos()!= grafica.getElementos()||this.getAristas() != grafica.getAristas())
            return false;
        for (Vertice ver : vertices) {
            Vertice gver  = null;
            for (Vertice aux : grafica.vertices) {
                if(ver.elemento.equals(aux.elemento))
                    gver = aux;
            }
            if(gver == null)
                return false;
            for (Vertice vec : ver.vecinos) {
                Vertice gvec  = null;
                for (Vertice aux : gver.vecinos) {
                    if(vec.elemento.equals(aux.elemento))
                        gvec = aux;
                }
                if(gvec == null)
                    return false;
            }
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
