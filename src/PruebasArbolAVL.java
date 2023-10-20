public class PruebasArbolAVL {
 
    public static void main(String[] args) {
        
        ArbolAVL<Integer> arbol = new ArbolAVL<>();
        for (int i = 0; i < 10; i++) {
            arbol.inserta(i);
        }
        System.out.println("\n\n"+arbol.imprimePorNivel());
        arbol.elimina(7);
        System.out.println("\n\n"+arbol.imprimePorNivel());
        arbol.elimina(8);
        System.out.println("\n\n"+arbol.imprimePorNivel());

    }

}
