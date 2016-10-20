
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
    private boolean validMovement(int numberPackage, int numberOffer){
        Paquete paquete = parent.getPackageFromSelectedServices(numberOffer, numberPackage);
        Oferta oferta = parent.getOfferFromSelectedServices(numberOffer);
        
        return parent.availableCapacityToAdd(numberOffer, oferta.getPesomax(), paquete.getPeso()) 
                && parent.isValidPriority(oferta.getDias(),paquete.getPrioridad());
    }
    
    private void movePackage(int numberPackage, int oldNumberOffer, int newNumberOffer) {

        Paquete paquete = parent.getPackageFromSelectedServices(oldNumberOffer, numberPackage);
        //boolean valido = validMovement(paquete, newNumberOffer);
        //if (valido) {
            parent.updateWeightFromAvailableOfferWeight(newNumberOffer, paquete.getPeso());
            ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
            paquetesOrdenados.add(paquete);
            selectedServices.set(newNumberOffer, paquetesOrdenados);
            
            parent.updateWeightFromAvailableOfferWeight(oldNumberOffer, -paquete.getPeso());
            ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
            oldPaquetesOrdenados.remove(numberPackage);
            selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
            
            updateHappiness(); 
            updatePrice();
        //}
        //return valido;
    }
          
    private boolean validSwap(int numberPackage, int numberOffer, int numberPackage2, int numberOffer2) {
        Paquete paquete = parent.getPackageFromSelectedServices(numberOffer, numberPackage);
        Paquete paquete2 = parent.getPackageFromSelectedServices(numberOffer2, numberPackage2);
                
        return false;
    }
    
    private void swapPackages(int numberPackage, int numberOffer, int numberPackage2, int numberOffer2) {

        movePackage(numberPackage,numberOffer, numberOffer2);
        movePackage(numberPackage2,numberOffer2, numberOffer);
        updateHappiness();
        updatePrice();
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
        int numberOffer2 = random.nextInt(selectedServices.size()); //TODO comprovar que numberOffer != numberOffer2
        int numberPackage = random.nextInt(parent.getPackagesSizeFromSelectedServices(numberOffer));
        int numberPackage2 = random.nextInt(parent.getPackagesSizeFromSelectedServices(numberOffer2)); //TODO comprovar que numberPackage != numberPackage2
        
        Paquete paquete = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
        
        //escogemos al azar el operador:
        if (random.nextInt(2) == 0){ // OJO a lo que han fet
            //while the movement we want to do is not valid, we "backtrack" 
            //with another package and transport. If it's valid we just move.
            while (!validMovement(numberPackage,numberOffer2)) { //TODO tenir en compte que passa si no trobem solució
                numberOffer = random.nextInt(selectedServices.size());
                numberPackage = random.nextInt(parent.getPackagesSizeFromSelectedServices(numberOffer));
            }
            movements += "Paquete -> " + paquete + " Oferta " + parent.getOfferFromSelectedServices(numberOffer) + "\n";
            movePackage(numberPackage, numberOffer, numberOffer2);  
        } 
        else {
            Paquete paquete2 = parent.getPackageFromSelectedServices(numberOffer, 
                        parent.getPackagesSizeFromSelectedServices(numberOffer));
            //while the movement we want to do is not valid, we "backtrack" 
            //with another pair of packages and transports. If it's valid we just move.
            while (!validSwap(numberPackage,numberOffer,numberPackage2,numberOffer2)){
                numberOffer = random.nextInt(selectedServices.size());
                numberOffer2 = random.nextInt(selectedServices.size()); //TODO comprovar que numberOffer != numberOffer2
                numberPackage = random.nextInt(parent.getPackagesSizeFromSelectedServices(numberOffer));
                numberPackage2 = random.nextInt(parent.getPackagesSizeFromSelectedServices(numberOffer2));
            }
            movements += "( " + paquete + "," + parent.getOfferFromSelectedServices(numberOffer) +" )" 
                                    + "<-> " +
                                     "( " + paquete2 + "," + parent.getOfferFromSelectedServices(numberOffer2) +" )" + "\n";

            swapPackages(numberPackage, numberOffer, numberPackage2, numberOffer2);
            
        }
        //listaEstadosSucesores.add(new Successor(movements, 
                //new Estado(selectedServices)));
        //return listaEstadosSucesores;
    }
}
