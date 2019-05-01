package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

/**
 * La clase se encarga de la forma de salida del ordenamiento lexicografico 
 * sea la salida por consola o en un archivo de volcado.
 */
public class Salida {

    
    /**
     * Detecta la EStructura de datos que se registro 
     * @param lista la lista que contiene la informacion
     */
    public void imprime(String estructura,Lista<Integer> lista){
        switch (estructura) {
            case "Lista":
                System.out.println(ListaSVG.dibuja(lista));
                break;
                
            case "Cola":
                System.out.println(ColaSVG.dibuja(lista));
                break;

            case "Pila":
                System.out.println(PilaSVG.dibuja(lista));
                break;
            
            case "ArbolBinarioCompleto":
                System.out.println(ArbolCompletoSVG.dibuja(lista));
                break;
            
            case "ArbolBinarioOrdenado":
                System.out.println(ArbolOrdenadoSVG.dibuja(lista));
                break;
            
            case "ArbolRojinegro":
                System.out.println("Sin Terminar ");
                break;

            case "ArbolAVL":
                System.out.println("Sin Terminar ");
                break;
            
            case "Grafica":
                System.out.println("Sin Terminar ");
                break;
            
            case "MonticuloArreglo":
                System.out.println("Sin Terminar ");
                break;

            case "MonticuloMinimo":
                System.out.println("Sin Terminar ");
                break;

            default:
                System.err.println("El Formato del Archivo no es el Adecuado");
                Proyecto2.uso();
                break;
        }

    }

 }