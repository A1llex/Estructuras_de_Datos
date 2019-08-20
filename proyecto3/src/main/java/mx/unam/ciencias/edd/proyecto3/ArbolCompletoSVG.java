package mx.unam.ciencias.edd.proyecto3;

import mx.unam.ciencias.edd.ArbolBinarioCompleto;
import mx.unam.ciencias.edd.Lista;

public class ArbolCompletoSVG {

    /**
     * Genera a partir de una Lista codigo SVG que representara la lista
     * @param lista debe ser de un String que sean integers
     * @return retorna codigo SVG del contenido de una lista
     * @throws NumberFormatException si no contiene un String convertible a integer
     */
    static String dibuja(Lista<Integer> lista){
        if(lista == null || lista.getElementos() == 0 ||lista.esVacia()){
            System.err.println("La estructura esta Vacia");
            Proyecto3.uso();
            return null;
        }
            
        ArbolBinarioCompleto<Integer> abc = new ArbolBinarioCompleto<>();
        for (int var : lista) {
            abc.agrega(var);
        }

        int altura = abc.altura();
        int tam = (int)Math.pow(2, altura)*100;
        String salida = "<svg width='"+tam+"'height='"+(altura*100+100)+"'>\n\t<g>";

       
        int i=altura,j=0,elem=0;
        int x=(int)Math.pow(2, i)*50,y = (j*100+50),sep =(int)Math.pow(2, i+1)*50;
        for (int var : abc) {
            if(y+100 < (altura*100+100) && (elem+1)*2 <= abc.getElementos() ){
                if ((elem+1)*2+1 <= abc.getElementos()) {
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x-(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x+(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                } else {
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x-(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                }
            }
            salida += "\n\t\t<circle cx='"+x+"'; cy='"+y+"' r='20' stroke='black' stroke-width='2' fill='white' />";
            salida += "\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='"+x+"' y='"+(y+10)+"'text-anchor='middle'>"+var+"</text>";
            elem++;
            x += sep;
            if( x >= tam){
                sep = (int)Math.pow(2, i)*50;
                x = (int)Math.pow(2, --i)*50;
                y = (++j*100+50);
            }
        }
        
        salida += "\n\t</g>\n</svg> ";
        return salida;
    }

}
