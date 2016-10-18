package azamon;

import IA.Azamon.*;
import java.util.Scanner; 

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Oferta oferta = new Oferta(12, 12, 12);        
        //System.out.println("Semilla: ");
        int semilla = 4; //reader.nextInt();
        
        //System.out.println("Número de paquetes: ");
        int numeroPaquetes = 10; //reader.nextInt(); // Scans the next token of the input as an int.
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        
        //System.out.println("Número de paquetes: ");
        double proporcion = 1.2;
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        
        Estado.setOfertas(transporte);
        Estado.setPaquetes(paquetes);
        Estado estado = new Estado();
    }
    
}
