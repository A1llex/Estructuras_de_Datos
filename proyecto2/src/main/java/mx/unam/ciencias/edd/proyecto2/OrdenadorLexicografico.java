package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;


/**
 * Clase para ordenar los archivos o entrada estandar  junto con banderas para modificar la salida
 * o guardar la salida en un archivo
 */
public class OrdenadorLexicografico {

    Entrada entrada;
    Salida salida;

    public OrdenadorLexicografico(){
        entrada =  new Entrada();
        salida = new Salida();
    }

    /**
     * Es un Ordenador Lexicografico que funcionara con entrada estandar de consola,
     * o con la ruta de diferentes archvios .
     * al final imprime en consola el resultado del ordenamiento.
     * Puede recibir la bandera -r para revertir el orden en el que se ordena
     * La bandera -o sobre escribira un archivo seleccionado o lo creara.
     * @param  args  obtendra las banderas y o archivos de lectura
     */
    public void OrdLexico(String[] args) {
        Lista<String> lista = new Lista<>();
        boolean br=false,bo= false,ar=false;
        int aux = 0;

        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if(args[i].equals("-o")){
                    bo = true;
                    aux = i++; 
                } else if(args[i].equals("-r")){
                    br = true;
                }else{
                    lista = merge(lista,entrada.archivo(args[i]));
                    ar = true;
                }
            }    
        }

        if(!ar){
            if(bo)
                Proyecto1.uso();        
            else    
                lista = entrada.entradaEstandar();
        }

        if(br)
            lista = lista.reversa();

        salida.imprime(lista);

        if(bo){
            if(aux >= args.length || args[aux+1].equals("-r")){
                System.err.println("no existe el archivo");
                Proyecto1.uso();
            }
            else
                salida.overRide(lista,args[aux+1]);
        }
    }

    
    /**
     * Junta dos listas en una.
     * @param izq sera la lista izquierda.
     * @param der sera la lista derecha.
     * @return regresa una lista ordenada de dos listas iniciales.
     */
    public Lista<String> merge(Lista<String> izq,Lista<String> der){
        Lista<String> nueva = new Lista<>();
        while (!izq.esVacia()) {
            if(der.esVacia()){
                while (!izq.esVacia()) {
                    nueva.agregaFinal(izq.eliminaPrimero());
                }
                return nueva;
            }else if ((Entrada.comparar.compare(izq.getPrimero(), der.getPrimero())) <= 0) {
                nueva.agregaFinal(izq.eliminaPrimero());
            }else {
                nueva.agregaFinal(der.eliminaPrimero());
            } 
        }
        while(!der.esVacia()){
            nueva.agregaFinal(der.eliminaPrimero());
        }
        return nueva;
    }


}