package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class PilaSVG {

    /**
     * Genera a partir de una Lista codigo SVG que representara la Pila
     * @param lista debe ser de un String que sean integers
     * @return retorna codigo SVG del contenido de una lista
     * @throws NumberFormatException si no contiene un String convertible a integer
     */
    static String dibuja(Lista<Integer> lista){
        if(lista == null || lista.getElementos() == 0 ||lista.esVacia()){
            System.err.println("La estructura esta Vacia");
            Proyecto2.uso();
            return null;
        }
        int alto = lista.getElementos();
        int altura = (alto*40+30);
        String salida = "\n<svg width='150'height='"+altura+"'>\n\t<g>";
    
        for (int i = alto; i > 0; i--) {
            salida +="\n\t\t<rect x='30' y='"+(i*40+30)+"' width='100' height='40' stroke='black' fill='transparent' stroke-width='2'/>" ;
            salida +="\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='80' y='"+(i*40+60)+"'text-anchor='middle'>"+lista.eliminaPrimero()+"</text>";
        }
        salida += "\n\t</g>\n</svg> ";
        return salida;
    }
}
