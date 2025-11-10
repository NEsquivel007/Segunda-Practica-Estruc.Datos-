import java.util.ArrayList;

public class ArbolBMas {

    //Atributos.
    private final int orden;
    private NodoArbolBMas raiz;

    //Métodos.
    //Constructor.
    public ArbolBMas(int orden) {
        this.orden = orden;
        raiz = new NodoArbolBMas(true);
    }

    //Inserción de una llave en el árbol B+.
    public void insertar(int llave) {
        if(this.raiz.getLlaves().size() == orden - 1) {
            NodoArbolBMas nuevaRaiz = new NodoArbolBMas(false);
            nuevaRaiz.getHijos().add(this.raiz);
            dividirHijo(nuevaRaiz, 0);
            this.raiz = nuevaRaiz;
        }
        insertarNoLleno(this.raiz, llave);
    }

    //Inserción privada de una llave en un nodo que no está lleno.
    //Si el nodo no es una hoja, la inserción de una nueva llave puede implicar la división de uno de sus hijos.
    private void insertarNoLleno(NodoArbolBMas nodo, int llave) {
        int i = nodo.getLlaves().size() - 1;
            if(nodo.esHoja()) {
                while(i >= 0 && llave < nodo.getLlaves().get(i)) {
                    i--;
                }
                nodo.getLlaves().add(i + 1, llave);
        } else {
            while(i >= 0 && llave < nodo.getLlaves().get(i)) {
                i--;
            }
            i++;
            NodoArbolBMas hijo = nodo.getHijos().get(i);
            if(hijo.getLlaves().size() == orden - 1) {
                dividirHijo(nodo, i);
                if(llave > nodo.getLlaves().get(i)) {
                    i++;
                }
            }
            insertarNoLleno(nodo.getHijos().get(i), llave);
        }
    }

    //Dividir un nodo que está lleno.
    private void dividirHijo(NodoArbolBMas padre, int indice) {
        NodoArbolBMas nodoLleno = padre.getHijos().get(indice);
        NodoArbolBMas nuevoNodo = new NodoArbolBMas(nodoLleno.esHoja());
        int mitad = orden / 2;

        if (nodoLleno.esHoja()) {
            // En nodos hoja, se mantiene la llave media en ambos lados.
            nuevoNodo.setLlaves(new ArrayList<>(nodoLleno.getLlaves().subList(mitad, nodoLleno.getLlaves().size())));
            nodoLleno.setLlaves(new ArrayList<>(nodoLleno.getLlaves().subList(0, mitad)));

            // El padre recibe como separador la primera llave del nuevo nodo.
            int llaveMedia = nuevoNodo.getLlaves().get(0);
            padre.getLlaves().add(indice, llaveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
        else {
            // En nodos internos, la llave media sube y se elimina del hijo.
            int claveMedia = nodoLleno.getLlaves().get(mitad);
            nuevoNodo.setLlaves(new ArrayList<>(nodoLleno.getLlaves().subList(mitad + 1, nodoLleno.getLlaves().size())));
            nodoLleno.setLlaves(new ArrayList<>(nodoLleno.getLlaves().subList(0, mitad)));

            // Mover los hijos del nodo dividido.
            nuevoNodo.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(mitad + 1, nodoLleno.getHijos().size())));
            nodoLleno.setHijos(new ArrayList<>(nodoLleno.getHijos().subList(0, mitad + 1)));

            padre.getLlaves().add(indice, claveMedia);
            padre.getHijos().add(indice + 1, nuevoNodo);
        }
    }

    //Impresión del árbol como parte de la interfaz pública del árbol.
    public void imprimirArbol() {
        imprimirNodo(raiz, "", true);
    }

    //Impresión recursiva privada de los nodos a partir de uno inicial.
    private void imprimirNodo(NodoArbolBMas nodo, String indentacion, boolean esUltimo) {
        System.out.println(indentacion + "+- " + (nodo.esHoja() ? "Hoja" : "Interno") + " Nodo: " + nodo.getLlaves());
        indentacion += esUltimo ? "   " : "|  ";
        for(int i = 0; i < nodo.getHijos().size(); i++) {
            imprimirNodo(nodo.getHijos().get(i), indentacion, i == (nodo.getHijos().size() - 1));
        }
    }

    //Los métodos de búsqueda pueden retornar el dato en caso de que la búsqueda sea exitosa.
    //Búsqueda de un nodo como parte de la interfaz pública del árbol.
    public boolean buscar(int llave) {
        return buscarNodo(raiz, llave);
    }

    //Búsqueda recursiva privada de un nodo a partir de uno inicial.
    private boolean buscarNodo(NodoArbolBMas nodo, int llave) {
        int i = 0;
        while(i < nodo.getLlaves().size() && llave > nodo.getLlaves().get(i)) {
            i++;
        }
        if(i < nodo.getLlaves().size() && llave == nodo.getLlaves().get(i)) {
            return true;
        }
        if(nodo.esHoja()) {
            return false;
        } else {
            return buscarNodo(nodo.getHijos().get(i), llave);
        }
    }

    //Eliminar. Eliminación sencilla que no implica fusión de los nodos.

    //Recorrido por rango. Muestra los datos correspondientes al rango recuperado.
}