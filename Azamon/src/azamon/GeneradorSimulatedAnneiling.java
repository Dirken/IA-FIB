
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

    private ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private ArrayList<Double> availableOfferWeight;
    
    Estado parent;
    
    private int happiness;
    private double price;

    public GeneradorSimulatedAnneiling(int seed) {
        random = new Random(seed);
    }
    /**
     * Acepta o no un movimiento dependiendo de si el cambio de ese paquete por otro, nos es permitido
     * en caso de que se exceda el peso máximo o que el paquete no llegue, no se considerará válido
    */
    private boolean validMovement(int numberPackage, int oldNumberOffer, int newNumberOffer){
        Paquete paquete = selectedServices.get(oldNumberOffer).get(numberPackage);
        Oferta oferta = parent.getSortedOffers().get(newNumberOffer).getOferta();    
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        boolean packageFits = currentCapacity + paquete.getPeso() <= oferta.getPesomax();
        boolean validPriority = parent.isValidPriority(oferta.getDias(), paquete.getPrioridad());
        
        return packageFits && validPriority;
    }
        
    private boolean validSwap(int numberPackage, int numberOffer, int numberPackage2, int numberOffer2) {
        Paquete paquete = parent.getPackageFromSelectedServices(numberOffer, numberPackage);
        Paquete paquete2 = parent.getPackageFromSelectedServices(numberOffer2, numberPackage2);
        Oferta oferta = parent.getOfferFromSelectedServices(numberOffer);
        Oferta oferta2 = parent.getOfferFromSelectedServices(numberOffer2);
        return parent.availableCapacityToAdd(numberOffer, oferta.getPesomax()-paquete.getPeso(), paquete2.getPeso())
                && parent.isValidPriority(oferta.getDias(),paquete2.getPrioridad())
                
                && parent.availableCapacityToAdd(numberOffer2, oferta2.getPesomax()-paquete2.getPeso(), paquete.getPeso())
                && parent.isValidPriority(oferta2.getDias(),paquete.getPrioridad());
    }
    
    private void movePackage(int numberPackage, int oldNumberOffer, int newNumberOffer) {  
        
        Paquete paquete = selectedServices.get(oldNumberOffer).get(numberPackage);
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        availableOfferWeight.set(oldNumberOffer, currentCapacity - paquete.getPeso());
        availableOfferWeight.set(newNumberOffer, currentCapacity + paquete.getPeso());
        
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        selectedServices.set(newNumberOffer, paquetesOrdenados);
       
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(numberPackage);
        //modificació:
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        
        Oferta oldOffer =  parent.getOfferFromSelectedServices(oldNumberOffer);
        Oferta newOffer =  parent.getOfferFromSelectedServices(newNumberOffer);
        int oldHappiness = parent.happiness(oldOffer,paquete);
        int newHappiness = parent.happiness(newOffer,paquete);
        System.out.println(happiness);
        parent.updateTotalHappiness(newHappiness-oldHappiness);
        
        //we do something similar as with happiness but with price
        double oldPrice = parent.cost(oldOffer,paquete);
        double newPrice = parent.cost(newOffer,paquete);
        parent.updateTotalPrice(newPrice-oldPrice);
    }
    
    private void swapPackages(int numberPackage, int oldNumberOffer, int numberPackage2, int newNumberOffer) {  
        //we get both packages..
        Paquete paquete = parent.getPackageFromSelectedServices(oldNumberOffer, numberPackage);
        Paquete paquete2 = parent.getPackageFromSelectedServices(newNumberOffer, numberPackage2);
        
        //update weights from one add package and remove the other..
        parent.updateWeightFromAvailableOfferWeight(newNumberOffer, paquete.getPeso() - paquete2.getPeso());
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        paquetesOrdenados.remove(numberPackage2);
        //assign changes
        selectedServices.set(newNumberOffer, paquetesOrdenados);
        
        //update weights from one add package and remove the other..
        parent.updateWeightFromAvailableOfferWeight(oldNumberOffer, -paquete.getPeso()+ paquete2.getPeso());
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(numberPackage);
        oldPaquetesOrdenados.add(paquete2);
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        //We get the old happiness that the package gives us and the new happiness
        // and we add the difference of happiness so if we get less happiness we're adding
        //something < 0 and otherwise we're adding a positive happiness
        Oferta oldOffer =  parent.getOfferFromSelectedServices(oldNumberOffer);
        Oferta newOffer =  parent.getOfferFromSelectedServices(newNumberOffer);
        int oldHappiness = parent.happiness(oldOffer,paquete);
        int newHappiness = parent.happiness(newOffer,paquete);
        int oldHappiness2 = parent.happiness(oldOffer,paquete2);
        int newHappiness2 = parent.happiness(newOffer,paquete2);
        parent.updateTotalHappiness((newHappiness-oldHappiness)+(newHappiness2-oldHappiness2));
        
        //we do something similar as with happiness but with price
        double oldPrice = parent.cost(oldOffer,paquete);
        double newPrice = parent.cost(newOffer,paquete);
        double oldPrice2 = parent.cost(oldOffer,paquete2);
        double newPrice2 = parent.cost(newOffer,paquete2);
        parent.updateTotalPrice( (newPrice-oldPrice) + (newPrice2-oldPrice2) );
    }

    /*
    Para Simulated Annealing tendréis que escoger al azar un operador y generar
    solo un sucesor aplicando este operador con parámetros también al azar.
    */
    @Override
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        parent = (Estado)state;
        selectedServices = (ArrayList<ArrayList<Paquete>>) parent.getSelectedServices().clone();
       
        availableOfferWeight = (ArrayList<Double>) parent.getAvailableOfferWeight().clone();
        happiness = parent.getHappiness();
        price = parent.getPrice();
        LinkedList<Successor> sucesores = new LinkedList<>(); //rename
        boolean success = false; //rename
        Estado nextEstado = new Estado(price,happiness, selectedServices, 
                                availableOfferWeight, parent.getSortedPackages(), parent.getSortedOffers());
        String action = "";
        int contador = 0;
        while (!success) {
            ++contador;
            
            System.out.println("Bucle" + contador);
            if (random.nextInt(2) == 0) {
                //move
                int offerIndex = random.nextInt(Estado.getOffers().size());
                int offerIndex2 = random.nextInt(Estado.getOffers().size());
                if (!selectedServices.get(offerIndex).isEmpty() && offerIndex != offerIndex2) {
                    int packageIndex = random.nextInt(selectedServices.get(offerIndex).size());
                    if (validMovement(packageIndex, offerIndex, offerIndex2)) {
                        System.out.println("MOOOOVE");
                        movePackage(packageIndex, offerIndex, offerIndex2);
                        nextEstado = new Estado(price,happiness, selectedServices, 
                                availableOfferWeight, parent.getSortedPackages(), parent.getSortedOffers());
//                        System.out.println(selectedServices.equals(selectedServices2));
//                        System.out.println(selectedServices);
//                        System.out.println(selectedServices2);
                        sucesores.add(new Successor(action, nextEstado));
                        return sucesores;
                    }
                    else {
                        
                    }
                }
            }
        }    
        
        
        //action += new HeuristicFunctionCost().getHeuristicValue(nextEstado);
        sucesores.add(new Successor(action, nextEstado));
        return sucesores;
    }
}
