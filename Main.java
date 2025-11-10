public class Main {

    public static void main(String[] args) {

        ArbolBMas arbol = new ArbolBMas(4);

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(5);
        arbol.insertar(6);
        arbol.insertar(15);
        arbol.insertar(30);
        arbol.insertar(25);

        arbol.imprimirArbol();
    }
}