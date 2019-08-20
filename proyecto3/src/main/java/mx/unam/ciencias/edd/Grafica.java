package mx.unam.ciencias.edd;

import java.rmi.server.RemoteRef;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
    private class Vertice implements VerticeGrafica<T>,
                          ComparableIndexable<Vertice> {

        /* El elemento del vértice. */
        public T elemento;
        /* El color del vértice. */
        public Color color;
        /* La distancia del vértice. */
        public double distancia;
        /* El índice del vértice. */
        public int indice;
        /* La lista de vecinos del vértice. */
        public Lista<Vecino> vecinos;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            if(elemento == null)
                return;
            this.elemento = elemento;
            color = Color.NINGUNO;
            vecinos = new Lista<>();
            indice = -1 ;
            distancia = -1;
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
            return this.vecinos;
        }

        /* Define el índice del vértice. */
        @Override public void setIndice(int indice) {
            this.indice = indice;
        }

        /* Regresa el índice del vértice. */
        @Override public int getIndice() {
            return indice;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            if (this.distancia == vertice.distancia) {
                return 0;
            }
            if (this.distancia > vertice.distancia) {
                return 1;
            }
            return -1;
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            return vecino.get();
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            return vecino.getGrado();
        }

        /* Regresa el color del vecino. */
        @Override public Color getColor() {
            return vecino.getColor();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            return vecino.vecinos;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(Grafica.Vertice v, Grafica.Vecino a);
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
        va.vecinos.agrega(new Vecino(vb,1));
        vb.vecinos.agrega(new Vecino(va,1));
        aristas++;
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(a == b || peso < 0 ||sonVecinos(a, b))
            throw new IllegalArgumentException();
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        va.vecinos.agrega(new Vecino(vb,peso));
        vb.vecinos.agrega(new Vecino(va,peso));
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
        Vecino vda = buscaVecino(va, vb),vdb = buscaVecino(vb, va);
        va.vecinos.elimina(vda);
        vb.vecinos.elimina(vdb);
        aristas--;
    }

    /**
     * lo que hace es buscar el vertice que sea vecino de otro pero buscandolo apartir de la referencia del vertice
     * @param vertice el vertice del que queremos buscar 
     * @param vecino el vertice que queremos buscar en lalista de vecinos
     * @return el Vecino si lo encuentra
     */
    private Vecino buscaVecino(Vertice vertice, Vertice vecino) {
        for (Vecino v : vertice.vecinos) {
            if(v.vecino.equals(vecino)){
                return v;
            }
        }
        return null;
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
        int u=0;
        for (Vecino vec: ver.vecinos) {
            if(sonVecinos(ver.elemento, vec.get())){
                desconecta(ver.elemento, vec.get());
            }
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
        if (buscaVecino(va, vb) != null && buscaVecino(vb, va) != null){
            return true;
        }else
            return false;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(a == b||!sonVecinos(a,b))
            throw new IllegalArgumentException();
        
        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        Vecino vda = null;
        if ((vda = buscaVecino(va, vb)) == null || ( buscaVecino(vb, va)) == null){
            throw new IllegalArgumentException();
        }
        return vda.peso;
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        if(!contiene(a) || !contiene(b))
            throw new NoSuchElementException();
        if(a == b||!sonVecinos(a,b))
            throw new IllegalArgumentException();

        Vertice va = (Vertice) vertice(a);
        Vertice vb = (Vertice) vertice(b);
        Vecino vda = buscaVecino(va, vb),vdb = buscaVecino(vb, va);
        if (vda  == null || vdb == null){
            throw new IllegalArgumentException();
        }
        vda.peso = peso;
        vdb.peso = peso;
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
        if( vertice == null || (vertice.getClass() !=Vertice.class && vertice.getClass() != Vecino.class)){
            throw	new	IllegalArgumentException("Vértice inválido");				
        }
        if(vertice.getClass()==Vertice.class){
            Vertice v = (Vertice)vertice;
            v.color = color;
        }
        if(vertice.getClass()==Vecino.class){
            Vecino v = (Vecino)vertice;
            v.vecino.color = color;
        }				
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

    private void desColorear(){
        if(vertices == null)
            return;
        for (Vertice ver : vertices) {
            ver.color = Color.NINGUNO;
        }
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
            for (Vecino vec : aux.vecinos) {
                if(vec.getColor() != Color.ROJO){
                    Vertice aux1 = (Vertice) vertice(vec.get());
                    aux1.color = Color.ROJO;
                    estruc.mete(aux1);
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
            for (Vecino vec : ver.vecinos) {
                if(aris == null || !(aris.contiene("("+ver.elemento+","+vec.get()+")")||
                aris.contiene("("+vec.get()+","+ver.elemento+")"))){
                    aris.agrega("("+ver.elemento+","+vec.get()+")");
                    salari += "("+ver.elemento+", "+vec.get()+"), ";
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
            for (Vecino vec : ver.vecinos) {
                Vecino gvec = null;
                for (Vecino aux : gver.vecinos) {
                    if(vec.get().equals(aux.get()))
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
    
    private Lista<VerticeGrafica<T>> trayrctoria(T origen, T destino, boolean dijkstra) {
        if(!contiene(origen)||!contiene(destino))
        throw new NoSuchElementException();
        
        for (Vertice v : vertices) {
            v.distancia = -1;
        }
        Vertice ori = (Vertice)vertice(origen);
        ori.distancia = 0;
        Vertice dest = (Vertice)vertice(destino);

        MonticuloDijkstra<Vertice> mont;
        int n = getElementos();
        if(aristas > (((n*(n-1))/2)-n)){
            mont = new MonticuloArreglo<>(vertices);
        }else{
            mont = new MonticuloMinimo<>(vertices);
        }

        while (!mont.esVacia()) {
            Vertice min = mont.elimina();
            for (Vecino v : min.vecinos) {
                if(v.vecino.distancia == Double.POSITIVE_INFINITY || (min.distancia + v.peso) < v.vecino.distancia ){
                    v.vecino.distancia = min.distancia + (dijkstra ? v.peso : 1);
                    mont.reordena(v.vecino);
                }
            }
        }

        Lista<VerticeGrafica<T>> tray = new Lista<>();
        System.out.println("distancia origen "+ori.distancia+"\t distancia final "+dest.distancia);
        if(dest.distancia != Double.POSITIVE_INFINITY){
            Vertice aux = dest;
            int u=0;
            while (aux != ori) {
                System.out.println("iteracion "+(u++));
                for (Vecino v : aux.vecinos) {
                    if ((aux.distancia - (dijkstra ? v.peso : 1)) == v.vecino.distancia) {
                        tray.agregaInicio(aux);
                        aux = v.vecino;
                        break;
                    }
                }
                if(aux == ori){
                    tray.agregaInicio(ori);
                }            
            }
        }
        System.out.println(" es vacia "+tray.esVacia());
        return tray;
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <tt>a</tt> y
     *         <tt>b</tt>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public Lista<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        return trayrctoria(origen,destino,false);
    }
    

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento de
     * destino.
     * 
     * @param origen  el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <tt>origen</tt> y el
     *         vértice <tt>destino</tt>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en la
     *                                gráfica.
     */
    public Lista<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        return trayrctoria(origen,destino,true);
    }
}
