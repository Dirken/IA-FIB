/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamon;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Transporte;
import java.util.Random;

/**
 *
 * @author Ricard
 */

/*
Para Simulated Annealing tendréis que escoger al azar un operador y generar
solo un sucesor aplicando este operador con parámetros también al azar.
*/    
public class GeneradorSimulatedAnneiling {

    private Random random;
    
    public GeneradorSimulatedAnneiling(int semilla) {
        random = new Random(semilla);
        if (random.nextInt(2) == 0) {
            Paquete paquete = null;
            Oferta oferta = null;
            mover(paquete, oferta);
        } 
        else {
            Paquete paquete1 = null;
            Paquete paquete2 = null;
            Oferta oferta = null;
            intercambiar(paquete1, paquete2, oferta);
        }
    }

    private void mover(Paquete paquete, Oferta oferta) {
        
        
    }

    private void intercambiar(Paquete paquete1, Paquete paquete2, Oferta oferta) {
        
    }
    
}
