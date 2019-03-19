package mx.unam.ciencias.edd.proyecto1;

import mx.unam.ciencias.edd.Lista;

import java.util.Comparator;
import java.util.Iterator;

import java.text.Normalizer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import mx.unam.ciencias.edd.Lista;

import java.util.Random;
import java.text.NumberFormat;


public class Proyecto1 {

    private static void uso() {
        System.err.println("Uso: java -jar Proyecto1.jar [bandera] [Archivo a Leer]");
        System.exit(1);
    }
//main
    public static void main(String[] args) {
        Proyecto1 uno = new Proyecto1();
        uno.alpha(args);
    }

    public  void alpha(String[] args) {
        if (args.length == 0)
            uso();

        Lista<String> lista = new Lista<>();
        boolean br=false,bo= false;
        int aux = 0;
        for (int i = 0; i < args.length; i++) {
            if(args[i].equals("-o")){
                bo = true;
                aux = i++; 
            } else if(args[i].equals("-r")){
                br = true;
            }else
                lista = merge(lista,archivo(args[i]));
        }

        System.out.println("la bandera reversa es  "+ br);
        System.out.println("la bandera overrigth es  "+ bo);

        if(br){
            lista = lista.reversa();
            Lista<String> lista1 = lista;
            while(!lista1.esVacia()){
                System.out.println(lista1.eliminaPrimero());
            }
        }

        if(bo){
            if(aux >= args.length)
                throw new IndexOutOfBoundsException("no hay direccion donde escribir");
            else
                overRide(lista,args[++aux]);
        }
    }

    static FileWriter fw;
    public void overRide(Lista<String> lista,String ruta){
        try {
            File f = new File(ruta);
            fw = new FileWriter(f);
            fw.write(lista.getElementos());
            while(!lista.esVacia()){
               fw.write(lista.eliminaPrimero());
            }
            fw.close();
        } catch (IOException ioe) {
            System.err.println("Error no se porque");
        }
    }



    public Lista<String> archivo(String ruta){
        Lista<String> lista = new Lista<>();
        try{
            String cadena;
            FileReader f = new FileReader(ruta);
            BufferedReader b = new BufferedReader(f);
            while((cadena = b.readLine())!=null) {
                lista.agrega(cadena+"\n");
            }
            f.close();
            b.close();
            return lista.mergeSort(comparar);
        } catch (IOException ioe) {
            System.err.println("no existe el archivo");
            return lista;
        }
    }

    public Lista<String> merge(Lista<String> izq,Lista<String> der){
        Lista<String> nueva = new Lista<>();
        while (!izq.esVacia()) {
            if(der.esVacia()){
                while (!izq.esVacia()) {
                    nueva.agregaFinal(izq.eliminaPrimero());
                }
                return nueva;
            }else if ((comparar.compare(izq.getPrimero(), der.getPrimero())) <= 0) {
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

    public static Comparator<String> comparar  = new Comparator<String>() {
        @Override public int compare (String a,String b){
            a = Normalizer.normalize((a.replaceAll("\\s", "")), Normalizer.Form.NFD);
            b = Normalizer.normalize((b.replaceAll("\\s", "")), Normalizer.Form.NFD);
            a = (a.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")).toLowerCase();
            b = (b.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "")).toLowerCase();
            return (a.compareTo(b));
        }
    };
}
