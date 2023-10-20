/**
 * Clase que representa un nodo de un arbol AVL
 * @param <T> Tipo de dato que almacena el nodo
 * @author Fernando Retama
 * @created 08/oct/2023
 */
public class NodoAVL<T extends Comparable<T>> {

    private T dato; //información que guarda el nodo
    private NodoAVL<T> izquierda, derecha, arriba; //apuntadores
    private int factorEquilibrio; //indica qué rama está más cargada (izquierda es negativo, derecha es positivo)

    /**
     * Constructor con información del nodo
     * @param dato: dato T que almacena el nodo
     */
    NodoAVL(T dato) {
        this.dato = dato;
        izquierda = derecha = arriba = null;
        factorEquilibrio = 0;
    }

    public T getDato() {
        return dato;
    }
    public void setDato(T dato) {
        this.dato = dato;
    }

    public NodoAVL<T> getIzquierda() {
        return izquierda;
    }
    public NodoAVL<T> getDerecha() {
        return derecha;
    }
    public NodoAVL<T> getArriba() {
        return arriba;
    }
    public void setArriba(NodoAVL<T> arriba) {
        this.arriba = arriba;
    }

    public int getFactorEquilibrio() {
        return factorEquilibrio;
    }
    public void setFactorEquilibrio(int factorEquilibrio) {
        this.factorEquilibrio = factorEquilibrio;
    }
    public void aumentaFactor(){
        factorEquilibrio++;
    }
    public void disminuyeFactor(){
        factorEquilibrio--;
    }
    
    /**
     * Método que cuelga un nodo debajo
     * @param hijo Nodo a colgar
     * @param lado Lado en que se suelga, true es derecha y false es izquierda
     */
    public void cuelga (NodoAVL<T> hijo, boolean ladoDer){
        if (ladoDer)
            derecha = hijo;
        else
            izquierda = hijo;
        if (hijo != null)
            hijo.arriba = this;
    }

    /**
     * Método que cuelga un nodo debajo
     * @param hijo Nodo a colgar
     * @param lado Lado en que se suelga, true es derecha y false es izquierda
     */
    public void cuelga (NodoAVL<T> hijo){
        if (hijo == null)
            throw new IllegalArgumentException("El nodo a colgar no puede ser nulo, agregue parámetro del lado en que se colgará el nodo recibido");
        if (hijo.getDato().compareTo(dato) <= 0)
            izquierda = hijo;
        else
            derecha = hijo;
        hijo.arriba = this;
    }
    
}
