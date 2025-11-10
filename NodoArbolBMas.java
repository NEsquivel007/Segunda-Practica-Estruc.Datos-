import java.util.ArrayList;
import java.util.List;

public class NodoArbolBMas {

    //Atributos.
    private boolean esHoja;
    private List<Integer> llaves;
    private List<NodoArbolBMas> hijos; //Vacía si es una hoja.
    //Datos. (Puede ser simplemente un ArrayList de Strings. Debe ser null si es un interno.)
    //Enlace. (Es una instancia de la misma clase. Debe ser null si es un interno.)

    //Métodos.
    //Constructor. Debe modificarse para que el nodo sea capaz de almacenar los datos.
    public NodoArbolBMas(boolean esHoja) {
        this.esHoja = esHoja;
        this.llaves = new ArrayList<>();
        this.hijos = new ArrayList<>();
    }

    //Getters.
    public boolean esHoja() {
        return esHoja;
    }

    public List<Integer> getLlaves() {
        return llaves;
    }

    public List<NodoArbolBMas> getHijos() {
        return hijos;
    }

    //Setters.
    public void setLlaves(List<Integer> llaves) {
        this.llaves = llaves;
    }

    public void setHijos(List<NodoArbolBMas> hijos) {
        this.hijos = hijos;
    }
}