package mx.unam.ciencias.edd.proyecto2;

import mx.unam.ciencias.edd.ArbolBinarioOrdenado;
import mx.unam.ciencias.edd.Lista;
import mx.unam.ciencias.edd.VerticeArbolBinario;

public class ArbolOrdenadoSVG {

    /**
     * Genera a partir de una Lista codigo SVG que representara la lista
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
            
        ArbolBinarioOrdenado<Integer> abo = new ArbolBinarioOrdenado<>();
        for (int var : lista) {
            abo.agrega(var);
        }
        System.out.println(abo.toString());

        int altura = abo.altura();
        int tam = (int)Math.pow(2, altura)*100;
        String salida = "<svg width='"+tam+"'height='"+(altura*100+100)+"'>\n\t<g>";

       
        int i=altura,j=0;
        int x=(int)Math.pow(2, i)*50,y = (j*100+50),sep =(int)Math.pow(2, i+1)*50;
        for (int var : abo) {
            /*el  problema es que lee en dfs*/
            VerticeArbolBinario<Integer> ver = abo.busca(var);
            if(ver.hayIzquierdo()){
                if (ver.hayDerecho()) {
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x-(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x+(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                } else {
                    salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x-(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
                }
            }else if(ver.hayDerecho()){
                salida += "\n\t\t<line x1='"+x+"' y1='"+y+"' x2='"+(x+(int)Math.pow(2, i-1)*50)+"' y2='"+(y+100)+"' stroke='black' stroke-width='2' />";
            }            
            
            boolean bool = true;
            while(bool){
                if(!ver.hayPadre()){
                    salida += "\n\t\t<circle cx='"+x+"'; cy='"+y+"' r='20' stroke='black' stroke-width='2' fill='white' />";
                    salida += "\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='"+x+"' y='"+(y+10)+"'text-anchor='middle'>"+var+"</text>";
                    x += sep;
                    bool = false;
                }else if(ver.padre().hayIzquierdo() && ver.padre().izquierdo().equals(ver)){
                    salida += "\n\t\t<circle cx='"+x+"'; cy='"+y+"' r='20' stroke='black' stroke-width='2' fill='white' />";
                    salida += "\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='"+x+"' y='"+(y+10)+"'text-anchor='middle'>"+var+"</text>";
                    x += sep;
                    bool = false;
                }else if(ver.padre().hayDerecho()&&ver.padre().derecho().equals(ver)){
                    x += sep;
                    salida += "\n\t\t<circle cx='"+x+"'; cy='"+y+"' r='20' stroke='black' stroke-width='2' fill='white' />";
                    salida += "\n\t\t<text fill='black' font-family='sans-serif' font-size='20' x='"+x+"' y='"+(y+10)+"'text-anchor='middle'>"+var+"</text>";
                    bool = false;
                }else{
                    x += sep;
                }
                if( x >= tam){
                    sep = (int)Math.pow(2, i)*50;
                    x = (int)Math.pow(2, --i)*50;
                    y = (++j*100+50);
                }
            }
        }

        salida += "\n\t</g>\n</svg> ";
        return salida;
    }

}
