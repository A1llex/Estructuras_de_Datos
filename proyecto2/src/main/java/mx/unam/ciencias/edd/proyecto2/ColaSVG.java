package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.Lista;

public class ColaSVG {

    /**
     * Genera a partir de una Lista codigo SVG que representara la Cola
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
        int ancho = lista.getElementos();
        String salida = "\n<svg width='"+(ancho*130+30)+"'height='100'>\n<defs>\n\t<g id='flecha'>\n\t\t<text x='0' y='0' fill='black' font-famili='sans-serif' font-size='30' text-anchor='middle'>⬅</text>\n\t</g>\n</defs>\n\t<g>";
    
        for (int i = 0; i < ancho; i++) {
            salida +="\n\t\t<rect x="+(i*130+30)+" y='30' width='100' height='40' stroke='black' fill='transparent' stroke-width='2'/>" ;
            salida +="\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='"+(i*130+80)+"' y='60'text-anchor='middle'>"+lista.eliminaPrimero()+"</text>";
            if(i+1 < ancho)
                salida+="\n\t\t<use xlink:href ='#flecha' x="+(i*130+145)+" y='60' />";
        }
        salida += "\n\t</g>\n</svg> ";
        return salida;
    }
}
