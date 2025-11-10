
import java.util.Collections;
import java.util.List;


public class ArbolBMas {
    // ======== ÚNICOS ATRIBUTOS ========
    private final int orden;
    private Nodo raiz;

    // ======== Comstructor ========
    public ArbolBMas(int orden) {
        if (orden < 3) throw new IllegalArgumentException("El orden (m) debe ser >= 3");
        this.orden = orden;
        this.raiz = new Nodo(true); // inicia como hoja vacía
    }

    // ======== API ========

    /** Inserta una clave (si ya existe, no se duplica). */
    public void insertar(int clave) {
        Nodo.Division div = insertarRec(raiz, clave);
        if (div != null) {
            // nueva raíz interna con dos hijos
            Nodo nueva = new Nodo(false);
            nueva.listaClaves.add(div.pivote);
            nueva.listaHijos.add(div.izquierda);
            nueva.listaHijos.add(div.derecha);
            raiz = nueva;
        }
    }

    /** Retorna true si la clave existe. */
    public boolean buscar(int clave) {
        Nodo hoja = buscarHoja(raiz, clave);
        int i = Collections.binarySearch(hoja.listaClaves, clave);
        return i >= 0;
    }

    /** Elimina la clave si existe. Retorna true si se eliminó algo. */
    public boolean eliminar(int clave) {
        boolean ok = eliminarRec(raiz, clave);
        // Comprimir raíz si quedó con 1 hijo y es interna
        if (!raiz.esHoja && raiz.listaHijos.size() == 1) {
            raiz = raiz.listaHijos.get(0);
        }
        return ok;
    }

    /**
     * Imprime los siguientes n elementos (claves) a partir de claveInicial (incluyéndola si existe).
     */
    public void recorrer(int claveInicial, int n) {
        if (n <= 0) return;
        Nodo hoja = buscarHoja(raiz, claveInicial);
        int pos = lowerBound(hoja.listaClaves, claveInicial);
        int impresos = 0;
        while (hoja != null && impresos < n) {
            while (pos < hoja.listaClaves.size() && impresos < n) {
                System.out.println(hoja.listaClaves.get(pos));
                pos++; impresos++;
            }
            hoja = hoja.siguiente;
            pos = 0;
        }
    }

    // ========  Insertar ========

    private Nodo.Division insertarRec(Nodo nodo, int clave) {
        if (nodo.esHoja) {
            nodo.insertarClave(clave);
            if (nodo.listaClaves.size() <= maxClavesHoja()) return null;
            return nodo.dividir(orden);
        } else {
            int idxHijo = posicionHijo(nodo.listaClaves, clave);
            Nodo.Division d = insertarRec(nodo.listaHijos.get(idxHijo), clave);
            if (d == null) return null;

            nodo.listaClaves.add(idxHijo, d.pivote);
            nodo.listaHijos.set(idxHijo, d.izquierda);
            nodo.listaHijos.add(idxHijo + 1, d.derecha);

            if (nodo.listaClaves.size() <= maxClavesInterno()) return null;
            return nodo.dividir(orden);
        }
    }

    // ======== Buscar ========

    private Nodo buscarHoja(Nodo nodo, int clave) {
        while (!nodo.esHoja) {
            int i = posicionHijo(nodo.listaClaves, clave);
            nodo = nodo.listaHijos.get(i);
        }
        return nodo;
    }

    private int posicionHijo(List<Integer> llaves, int clave) {
        int i = Collections.binarySearch(llaves, clave);
        return (i >= 0) ? i + 1 : -i - 1;
    }

    private int lowerBound(List<Integer> a, int clave) {
        int i = Collections.binarySearch(a, clave);
        return (i >= 0) ? i : -i - 1;
    }

    // ======== Eliminar (con balanceo) ========

    private boolean eliminarRec(Nodo nodo, int clave) {
        if (nodo.esHoja) {
            int i = Collections.binarySearch(nodo.listaClaves, clave);
            if (i < 0) return false;
            nodo.listaClaves.remove(i);
            // Cumplimiento de mínimos: si hoja no es raíz y quedó por debajo, reparar arriba.
            return true;
        } else {
            int idx = posicionHijo(nodo.listaClaves, clave);
            boolean eliminado = eliminarRec(nodo.listaHijos.get(idx), clave);
            if (!eliminado) return false;

            // Reparar sub-ocupación del hijo idx si aplica (salvo casos de raíz)
            balancearDespuesEliminar(nodo, idx);
            // Recalcular separadores (primera clave real del hijo derecho)
            recalcularSeparadores(nodo);
            return true;
        }
    }

    private void balancearDespuesEliminar(Nodo padre, int idxHijo) {
        Nodo hijo = padre.listaHijos.get(idxHijo);

        if (hijo.esHoja) {
            if (padre == raiz && padre.listaClaves.isEmpty()) return; // raíz-hoz especial
            if (esRaiz(hijo) || hijo.listaClaves.size() >= minClavesHoja()) return;

            // Prestar del izquierdo
            if (idxHijo > 0) {
                Nodo izq = padre.listaHijos.get(idxHijo - 1);
                if (izq.listaClaves.size() > minClavesHoja()) {
                    int mov = izq.listaClaves.remove(izq.listaClaves.size() - 1);
                    hijo.listaClaves.add(0, mov);
                    padre.listaClaves.set(idxHijo - 1, hijo.listaClaves.get(0));
                    return;
                }
            }
            // Prestar del derecho
            if (idxHijo + 1 < padre.listaHijos.size()) {
                Nodo der = padre.listaHijos.get(idxHijo + 1);
                if (der.listaClaves.size() > minClavesHoja()) {
                    int mov = der.listaClaves.remove(0);
                    hijo.listaClaves.add(mov);
                    padre.listaClaves.set(idxHijo, der.listaClaves.get(0));
                    return;
                }
            }
            // Fusionar
            if (idxHijo > 0) {
                Nodo izq = padre.listaHijos.get(idxHijo - 1);
                fusionarHojas(izq, hijo);
                padre.listaHijos.remove(idxHijo);
                padre.listaClaves.remove(idxHijo - 1);
            } else {
                Nodo der = padre.listaHijos.get(idxHijo + 1);
                fusionarHojas(hijo, der);
                padre.listaHijos.remove(idxHijo + 1);
                padre.listaClaves.remove(idxHijo);
            }

        } else { // interno
            if (esRaiz(hijo) || hijo.listaHijos.size() >= minHijosInterno()) return;

            // Prestar del izquierdo
            if (idxHijo > 0) {
                Nodo izq = padre.listaHijos.get(idxHijo - 1);
                if (izq.listaHijos.size() > minHijosInterno()) {
                    // rotación derecha
                    int sep = padre.listaClaves.get(idxHijo - 1);
                    Nodo movH = izq.listaHijos.remove(izq.listaHijos.size() - 1);
                    int movK = izq.listaClaves.remove(izq.listaClaves.size() - 1);
                    hijo.listaHijos.add(0, movH);
                    hijo.listaClaves.add(0, sep);
                    padre.listaClaves.set(idxHijo - 1, movK);
                    return;
                }
            }
            // Prestar del derecho
            if (idxHijo + 1 < padre.listaHijos.size()) {
                Nodo der = padre.listaHijos.get(idxHijo + 1);
                if (der.listaHijos.size() > minHijosInterno()) {
                    // rotación izquierda
                    int sep = padre.listaClaves.get(idxHijo);
                    Nodo movH = der.listaHijos.remove(0);
                    int movK = der.listaClaves.remove(0);
                    hijo.listaHijos.add(movH);
                    hijo.listaClaves.add(sep);
                    padre.listaClaves.set(idxHijo, movK);
                    return;
                }
            }
            // Fusionar
            if (idxHijo > 0) {
                Nodo izq = padre.listaHijos.get(idxHijo - 1);
                int sep = padre.listaClaves.remove(idxHijo - 1);
                fusionarInternos(izq, sep, hijo);
                padre.listaHijos.remove(idxHijo);
            } else {
                Nodo der = padre.listaHijos.get(idxHijo + 1);
                int sep = padre.listaClaves.remove(idxHijo);
                fusionarInternos(hijo, sep, der);
                padre.listaHijos.remove(idxHijo + 1);
            }
        }
    }

    private void recalcularSeparadores(Nodo interno) {
        for (int i = 0; i < interno.listaClaves.size(); i++) {
            Integer nueva = primeraClave(interno.listaHijos.get(i + 1));
            if (nueva != null) interno.listaClaves.set(i, nueva);
        }
    }

    private Integer primeraClave(Nodo n) {
        while (!n.esHoja) n = n.listaHijos.get(0);
        return n.listaClaves.isEmpty() ? null : n.listaClaves.get(0);
    }

    private void fusionarHojas(Nodo a, Nodo b) {
        a.listaClaves.addAll(b.listaClaves);
        a.siguiente = b.siguiente;
    }

    private void fusionarInternos(Nodo izq, int sep, Nodo der) {
        izq.listaClaves.add(sep);
        izq.listaClaves.addAll(der.listaClaves);
        izq.listaHijos.addAll(der.listaHijos);
    }

    // ======== Límites por m ========
    private int maxClavesHoja()    { return orden - 1; }
    private int minClavesHoja()    { return (int)Math.ceil((orden - 1) / 2.0); }
    private int maxClavesInterno() { return orden - 1; }
    private int minHijosInterno()  { return (int)Math.ceil(orden / 2.0); }
    private boolean esRaiz(Nodo n) { return n == raiz; }

}

