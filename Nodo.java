
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Nodo {

    // ======== Atributos requeridos ========
    public List<Integer> listaClaves = new ArrayList<>();
    public boolean esHoja;
    public List<Nodo> listaHijos = new ArrayList<>(); // vacío si es hoja
    public Nodo siguiente = null;                      // solo válido si es hoja

    // ======== Constructor ========
    public Nodo(boolean esHoja) {
        this.esHoja = esHoja;
    }

    // ======== Métodos estándar mínimos ========
    public boolean esHoja() { return esHoja; }
    public List<Integer> getListaClaves() { return listaClaves; }
    public List<Nodo> getListaHijos() { return listaHijos; }
    public Nodo getSiguiente() { return siguiente; }
    public void setSiguiente(Nodo sig) { this.siguiente = sig; }

    /**
     * Inserta la clave en orden si no existe; si existe no la duplica.
     * @return índice donde quedó la clave (o donde ya estaba)
     */
    public int insertarClave(int clave) {
        int pos = Collections.binarySearch(listaClaves, clave);
        if (pos < 0) {
            pos = -pos - 1;
            listaClaves.add(pos, clave);
        }
        return pos;
    }

    
    /**
     * Divide el nodo en dos, retornando el pivote y los dos nodos resultantes.
     */

    public Division dividir(int orden) {
        if (esHoja) {
            int total = listaClaves.size();
            int medio = (total + 1) / 2; // derecha puede quedar con 1 más

            Nodo derecha = new Nodo(true);
            derecha.listaClaves.addAll(listaClaves.subList(medio, total));

            // recorta izquierda
            this.listaClaves.subList(medio, total).clear();

            // enlazar hojas
            derecha.siguiente = this.siguiente;
            this.siguiente = derecha;

            int pivote = derecha.listaClaves.get(0);
            return new Division(pivote, this, derecha);
        } else {
            int total = listaClaves.size();
            int medio = total / 2;            // pivote en 'medio'
            int pivote = listaClaves.get(medio);

            Nodo derecha = new Nodo(false);
            // mover claves > pivote
            derecha.listaClaves.addAll(listaClaves.subList(medio + 1, total));
            // mover hijos correspondientes a la derecha (medio+1 .. fin)
            derecha.listaHijos.addAll(listaHijos.subList(medio + 1, listaHijos.size()));

            // izquierda se queda con < pivote
            this.listaClaves.subList(medio, total).clear();               // borra pivote y derecha
            this.listaHijos.subList(medio + 1, this.listaHijos.size()).clear();

            return new Division(pivote, this, derecha);
        }
    }

    // ======== Paquete de división ========
    public static class Division {
        public final int pivote;
        public final Nodo izquierda;
        public final Nodo derecha;
        public Division(int pivote, Nodo izquierda, Nodo derecha) {
            this.pivote = pivote;
            this.izquierda = izquierda;
            this.derecha = derecha;
        }
    }
}

