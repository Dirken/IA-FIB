/* Alex
 * To change this license header, choose License Headers in Project Properties
 */
package azamon;
import IA.Azamon.*;
import java.util.Scanner; 

/**
 *
 * @author Eironeia
 */
public class Azamon {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Oferta oferta = new Oferta(12, 12, 12);
        //hola que tal
        //Esta es la prueba dos
        Scanner reader = new Scanner(System.in);  // Reading from System.in
        
        //System.out.println("Semilla: ");
        int semilla = 4; //reader.nextInt();
        
        //System.out.println("Número de paquetes: ");
        int numeroPaquetes = 100; //reader.nextInt(); // Scans the next token of the input as an int.
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        
        //System.out.println("Número de paquetes: ");
        double proporcion = 1.2;
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        System.out.println(transporte);
          
        // Prueba 2 David
        
        System.out.println(paquetes);
        //this is my commit:
    }
    
}
