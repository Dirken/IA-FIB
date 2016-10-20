
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamon;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ricard
 */


public class GeneradorSimulatedAnneiling implements SuccessorFunction{

    private Random random;
    
    private double price;
    private int hapiness;
    
    private static Transporte offers;
    private static Paquetes packages;
    
    private ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private ArrayList<Double> availableOfferWeight;
    private ArrayList<PaqueteOrdenado> sortedPackages;
    private ArrayList<OfertaOrdenada> sortedOffers;
    
    Estado parent;

    public GeneradorSimulatedAnneiling(int seed) {
        random = new Random(seed);
    }

    private void updateHappiness(){
        
    }
    
    private void updatePrice(){
    
    }
    /**
     * Acepta o no un movimiento dependiendo de si el cambio de ese paquete por otro, nos es permitido
     * en caso de que se exceda el peso máximo o que el paquete no llegue, no se considerará válido
     */
    private boolean validMovement(Paquete paquete, Oferta oferta){
        return parent.availableCapacityToAdd(, oferta.getPesomax(), paquete.getPeso()) 
                && parent.isValidPriority(oferta.getDias(),paquete.getPrioridad());
    }
    
    private boolean movePackage(Paquete paquete, Oferta oferta) {
        boolean valido = validMovement(paquete, oferta);
        if (valido) {
            pesoOfertas.set(indiceOfertaActual, pesoOfertas.get(indiceOfertaActual) - paquete.getPeso());
            pesoOfertas.set(indiceOferta, pesoOfertas.get(indiceOferta) + paquete.getPeso());
            asignacionPaquetes.set(indicePaquete, indiceOferta);
            updateHappiness();
            updatePrice();
        }
        return valido;
    }
          
    private boolean swapPackages(Paquete paquete, Oferta oferta, Paquete paquete2, Oferta oferta2) {
        boolean valido, valido2;
        valido = validMovement(paquete,oferta2);
        valido2 = validMovement(paquete2,oferta);

        if (valido && valido2){
            pesoOfertas.set(indiceOferta1, pesoOfertas.get(indiceOferta1) - paquete1.getPeso() + paquete2.getPeso());
            pesoOfertas.set(indiceOferta2, pesoOfertas.get(indiceOferta2) + paquete1.getPeso() - paquete2.getPeso());
            asignacionPaquetes.set(indicePaquete1, indiceOferta2);
            asignacionPaquetes.set(indicePaquete2, indiceOferta1);
            updateHappiness();
            updatePrice();
        }
        return (valido && valido2);
    }

    /*
    Para Simulated Annealing tendréis que escoger al azar un operador y generar
    solo un sucesor aplicando este operador con parámetros también al azar.
    */
    
    @Override
    public List getSuccessors(Object state) {
        String movements = "";
        LinkedList<Successor>listaEstadosSucesores = new LinkedList();
        parent = (Estado)state;
        
        selectedServices = parent.getSelectedServices();
        
        //escogemos un paquete y una oferta aleatorias
        int numberOffer = random.nextInt(selectedServices.size());
        Oferta oferta = parent.getOfferFromSelectedServices(numberOffer);
        Paquete paquete = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
        
        //escogemos al azar el operador:
        if (random.nextInt(2) == 0){
            //while the movement we want to do is not valid, we "backtrack" 
            //with another package and transport. If it's valid we just move.
            while (!validMovement(paquete,oferta)) {
                paquete = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
                oferta = parent.getOfferFromSelectedServices(numberOffer);
            }
            movements += "Paquete -> " + paquete + " Oferta " + oferta + "\n";
            movePackage(paquete, oferta);  
        } 
        else {
            Paquete paquete2 = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
            Oferta oferta2 = parent.getOfferFromSelectedServices(numberOffer);
            //while the movement we want to do is not valid, we "backtrack" 
            //with another pair of packages and transports. If it's valid we just move.
            while ( (!validMovement(paquete,oferta2) && validMovement(paquete2,oferta))
                    ||(validMovement(paquete,oferta2) && !validMovement(paquete2,oferta))){
                paquete = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
                paquete2 = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
                oferta = parent.getOfferFromSelectedServices(numberOffer);
                oferta2 = parent.getOfferFromSelectedServices(numberOffer);
            }
            movements += "( " + paquete + "," + oferta +" )" 
                                    + "<-> " +
                                     "( " + paquete2 + "," + oferta2 +" )" + "\n";

            swapPackages(paquete, oferta, paquete2, oferta2);
            
        }
        listaEstadosSucesores.add(new Successor(movements, 
                new Estado(selectedServices)));
        return listaEstadosSucesores;
    }
}
