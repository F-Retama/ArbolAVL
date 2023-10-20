import java.util.LinkedList;
import java.util.Queue;

/**
 * Clase que implementa un arbol AVL
 * Un arbol AVL es un arbol binario de busqueda: para cada nodo, los datos en nodos de niveles 
 * inferiores que son menores o iguales se encuentran a la izquierda y los mayores a la derecha. 
 * Además, este tipo de árbol se auto-balancea cada vez que se inserta o elimina un elemento para que 
 * el árbol se mantenga corto y así sea más eficiente la búsqueda y manipulación de los datos, específicamente
 * el comportamiento asintótico de las operaciones se mantiene cercano a log(n).
 * @param <T> Tipo de dato que almacena el arbol AVL
 * @author Fernando Retama
 * @created 08/oct/2023
 */
public class ArbolAVL<T extends Comparable <T>> {

    private NodoAVL<T> raiz; //raiz del arbol
    private int numElementos; //número de elementos guardados

    ArbolAVL() {
        raiz = null;
        numElementos = 0;
    }

    /**
     * Indica si el arbol está vacío
     * @return True si está vacío, false si no
     */
    public boolean estaVacio() {
        return raiz == null;
    }

    /**
     * Devuelve la cantidad de nodos del árbol
     * @return número de elementos en el árbol
     */
    public int size() {
        return numElementos;
    }

    /**
     * Método que realiza la rotación pertinente en un segmento del parbol
     * @param alfa Nodo a partir del cual se va a rotar
     */
    private NodoAVL<T> rotacion(NodoAVL<T> alfa){
        NodoAVL<T> beta, gamma, b, c, res;
        if (alfa.getFactorEquilibrio() < 0){//rotación izquierda
            beta = alfa.getIzquierda();
            if (beta.getFactorEquilibrio() <= 0){//rotación izquierda-izquierda
                gamma = beta.getIzquierda();
                c = beta.getDerecha();
                if (alfa == raiz){
                    beta.setArriba(null);
                    raiz = beta;
                }
                else
                    alfa.getArriba().cuelga(beta);
                alfa.cuelga(c, false);
                beta.cuelga(gamma, false);
                beta.cuelga(alfa, true);
                //actualizo factores de equilibro
                if (beta.getFactorEquilibrio() == -1){
                    alfa.setFactorEquilibrio(0);
                    beta.setFactorEquilibrio(0);
                }
                else{
                    alfa.setFactorEquilibrio(-1);
                    beta.setFactorEquilibrio(1);
                }
                res = beta;
            }
            else{//rotación izquierda-derecha
                gamma = beta.getDerecha();
                if (alfa == raiz){//caso especial
                    gamma.setArriba(null);
                    raiz = gamma;
                }
                else//conecto gamma donde antes estaba alfa
                    alfa.getArriba().cuelga(gamma);
                rotacionCruzada(alfa, beta, gamma);
                res = gamma;
            }
        }
        else{//rotación derecha
            beta = alfa.getDerecha();
            if (beta.getFactorEquilibrio() >= 0){//rotación derecha-derecha
                gamma = beta.getDerecha();
                b = beta.getIzquierda();
                if (alfa == raiz){
                    beta.setArriba(null);
                    raiz = beta;
                }
                else
                    alfa.getArriba().cuelga(beta);
                alfa.cuelga(b, true);
                beta.cuelga(alfa, false);
                beta.cuelga(gamma, true);
                //actualizo factores de equilibro
                if (beta.getFactorEquilibrio() == 1){
                    alfa.setFactorEquilibrio(0);
                    beta.setFactorEquilibrio(0);
                }
                else{
                    alfa.setFactorEquilibrio(1);
                    beta.setFactorEquilibrio(-1);
                }
                res = beta;
            }
            else{//rotación derecha-izquierda
                gamma = beta.getIzquierda();
                if (alfa == raiz){//caso especial
                    gamma.setArriba(null);
                    raiz = gamma;
                }
                else//conecto gamma donde antes estaba alfa
                    alfa.getArriba().cuelga(gamma);
                rotacionCruzada(beta, alfa, gamma);
                res = gamma;
            }
        }
        return res;
    }
    
    /**
     * Método que realiza una rotación cruzada (izquierda-derecha o derecha-izquierda)
     * @param alfalfa alfa si se trata de izquierda-derecha, beta si se trata de derecha-izquierda
     * @param betabel beta si se trata de izquierda-derecha, alfa si se trata de derecha-izquierda
     * @param gamma nodo inferior de la rotación
     */
    private void rotacionCruzada(NodoAVL<T> alfalfa,NodoAVL<T> betabel,NodoAVL<T> gamma){
        NodoAVL<T> b, c;
        b = gamma.getIzquierda();
        c = gamma.getDerecha();
        //reconexiones
        betabel.cuelga(b, true);
        alfalfa.cuelga(c, false);
        gamma.cuelga(betabel, false);
        gamma.cuelga(alfalfa, true);
        //actualizaciones de factores de equilibro
        betabel.setFactorEquilibrio(0);
        alfalfa.setFactorEquilibrio(0);
        if (gamma.getFactorEquilibrio() == 1)
            betabel.setFactorEquilibrio(-1);
        else if (gamma.getFactorEquilibrio() == -1)
            alfalfa.setFactorEquilibrio(1);
        gamma.setFactorEquilibrio(0);
    }

    /**
     * Método para insertar un elemento en el árbol
     * @param dato Elemento a insertar
     */
    public void inserta(T dato){
        if (dato == null)
            throw new IllegalArgumentException("El dato no puede ser null");
        if (estaVacio()){
            raiz = new NodoAVL<>(dato);
        }
        else{
            NodoAVL<T> actual = raiz;
            boolean colgado = false;
            while (!colgado){
                if (dato.compareTo(actual.getDato()) <= 0){//dato a insertar menor o igual que el del nodo actual
                    if (actual.getIzquierda() == null){
                        //se inserta el dato a la izquerda de actual
                        actual.cuelga(new NodoAVL<T>(dato), false);
                        colgado = true;
                    }
                    actual = actual.getIzquierda();
                }
                else{//dato a insertar mayor que el del nodo actual
                    if (actual.getDerecha() == null){
                        //se inserta el dato a la derecha de actual
                        actual.cuelga(new NodoAVL<T>(dato), true);
                        colgado = true;
                    }
                    actual = actual.getDerecha();
                }
            }
            numElementos ++; //actualizamos la cantidad de datos almacenados
            /*
            se deben actualizar los factores de equilibro de los nodos desde actual hasta la raiz 
            o hasta que el factor del nodo superior resulte 0 tras la modificación
            si el factor de un nodo resulta 2 o -2 tras modificarlo, se debe balancear a partir de este
            */
            NodoAVL<T> arriba = actual.getArriba();
            while (arriba != null){
                if (actual == arriba.getIzquierda())//"actual" cuelga a la izquierda de "arriba"
                    arriba.disminuyeFactor();
                else //"actual" cuelga a la derecha de "arriba"
                    arriba.aumentaFactor();
                if (arriba.getFactorEquilibrio() == 1 || arriba.getFactorEquilibrio() == -1){//se continua subiendo
                    actual = arriba;
                    arriba = actual.getArriba();
                }
                else{
                    if(arriba.getFactorEquilibrio() == 2 || arriba.getFactorEquilibrio() == -2)//rebalanceo
                        rotacion (arriba);
                    arriba = null;
                }
            }
        }
    }
    
    /**
     * Método para encontrar el nodo que contiene a un elemento
     * @param dato
     * @return El nodo que está al elemento o null si no se encuentra
     */
    private NodoAVL<T> encuentra (T dato){
        if (dato == null)
            throw new IllegalArgumentException("El dato no puede ser null");
        NodoAVL<T> actual = raiz;
        while (actual != null && !actual.getDato().equals(dato)){
            if (dato.compareTo(actual.getDato()) < 0) //el dato es menor que el del nodo actual
                actual = actual.getIzquierda();
            else //el dato es mayor que el del nodo actual
                actual = actual.getDerecha();
        }
        return actual;
    }

    /**
     * Método para buscar un elemento en el árbol
     * @param dato elemento a buscar
     * @return True si se encuentra, false si no
     */
    public boolean contiene(T dato){
        return encuentra(dato) != null;
    }

    /**
     * Método para eliminar un elemento del árbol
     * @param dato elemento a eliminar
     * @return El elemento si se encuentra, null si no
     */
    public T elimina (T dato){
        T res;
        NodoAVL<T> actual = encuentra(dato);
        if (actual == null){//no se encontró el dato
            res = null;
        }
        else{//sí se encontró el dato
            res = actual.getDato();
            if (actual.getIzquierda() == null && actual.getDerecha() == null){//caso hoja
                if (actual == raiz)//se vacía el árbol
                    raiz = null;
                else{//se elimina la conexión del nodo "actual" con el nodo arriba de él
                    NodoAVL<T> arriba = actual.getArriba();
                    if (actual == arriba.getIzquierda())//"actual" cuelga a la izquierda de "arriba"
                        arriba.cuelga(null, false);
                    else//"actual" cuelga a la derecha de "arriba"
                        arriba.cuelga(null, true);
                }
            }
            else if(actual.getIzquierda() != null && actual.getDerecha() != null){//caso 2 hijos
                //se busca el sucesor in-order del nodo "actual"
                NodoAVL<T> sucesor = actual.getDerecha();
                while (sucesor.getIzquierda() != null)
                    sucesor = sucesor.getIzquierda();
                actual.setDato(sucesor.getDato());
                //se elimina el nodo sucesor
                sucesor.getArriba().cuelga(sucesor.getDerecha(), sucesor == sucesor.getArriba().getDerecha());
                if (sucesor.getArriba() == actual){//caso especial muy conflictivo
                    actual.disminuyeFactor();
                    if (actual.getFactorEquilibrio() == -1)
                        return res;
                    else if (actual.getFactorEquilibrio() == -2){
                        actual = rotacion(actual);
                        if (actual.getFactorEquilibrio() != 0)
                            return res;
                    }
                }
                else
                    actual = sucesor;
            }else{//caso 1 hijo (rama)
                //se obtiene el hijo de "actual"
                NodoAVL<T> hijo = actual.getIzquierda();
                if (hijo == null)
                    hijo = actual.getDerecha();
                //caso especial
                if (actual == raiz){
                    raiz = hijo;
                    raiz.setArriba(null);
                }
                else
                    actual.getArriba().cuelga(hijo);
            }
            /*
             * se actualizan los factores de equilibrio de los nodos desde actual hasta la raiz
             * o hasta que el factor del nodo superior sea 0 antes de la modificación
             * si el factor de un nodo resulta 2 o -2 tras modificarlo, se debe balancear a partir de este
             */
            NodoAVL<T> arriba = actual.getArriba();
            while (arriba != null){
                if (actual.getDato().compareTo(arriba.getDato()) <= 0)//"actual" colgaba a la izquierda de "arriba"
                    arriba.aumentaFactor();
                else //"actual" cuelga a la derecha de "arriba"
                    arriba.disminuyeFactor();
                if (arriba.getFactorEquilibrio() == 0){//se continua subiendo por el árbol
                    actual = arriba;
                    arriba = actual.getArriba();
                }
                else if (arriba.getFactorEquilibrio() == 1 || arriba.getFactorEquilibrio() == -1)//termina el bucle
                    arriba = null;
                else{
                    actual = rotacion(arriba);
                    if (actual.getFactorEquilibrio() != 0)
                        arriba = null;
                    else
                        arriba = actual.getArriba();
                }
            }
            numElementos --;
        }
        return res;
    }

    /**
     * Método que imprime el árbol por niveles
     * @return String con el árbol impreso y el factor de equilibro de cada dato
     */
    public String imprimePorNivel(){
        StringBuilder sb = new StringBuilder();
        Queue<NodoAVL<T>> cola = new LinkedList<>();
        cola.add(raiz);
        int cont = 0;
        while(!cola.isEmpty()){
            cont ++;
            NodoAVL<T> nodo = cola.poll();
            if(nodo != null){
                sb.append(nodo.getDato() + " ("+nodo.getFactorEquilibrio() +") ");
                cola.add(nodo.getIzquierda());
                cola.add(nodo.getDerecha());
            }else
                sb.append(" -.-  ");
            if ((cont & (cont+1)) == 0)
                sb.append("\n");
        }
        return sb.toString();
    }

}
