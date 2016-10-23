
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
        Oferta oferta = Estado.getSortedOffers().get(newNumberOffer).getOferta();    
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        boolean packageFits = currentCapacity + paquete.getPeso() <= oferta.getPesomax();
        boolean validPriority = parent.isValidPriority(oferta.getDias(), paquete.getPrioridad());
        
        return packageFits && validPriority;
    }
        
    private boolean validSwap(int numberPackage, int numberOffer, int numberPackage2, int numberOffer2) {
        Paquete paquete = selectedServices.get(numberOffer).get(numberPackage);
        Paquete paquete2 = selectedServices.get(numberOffer2).get(numberPackage2);
        Oferta oferta = Estado.getSortedOffers().get(numberOffer).getOferta();  
        Oferta oferta2 = Estado.getSortedOffers().get(numberOffer2).getOferta();
        
        
        double currentCapacityOnOffer = availableOfferWeight.get(numberOffer);
        double currentCapacityOnOffer2 = availableOfferWeight.get(numberOffer2);
        boolean packageFitsOnOffer2 = currentCapacityOnOffer2 - paquete2.getPeso() + paquete.getPeso() <= oferta2.getPesomax();
        boolean packageFitsOnOffer = currentCapacityOnOffer - paquete.getPeso() + paquete2.getPeso() <= oferta.getPesomax();
        boolean validPriorityOnOffer = parent.isValidPriority(oferta.getDias(), paquete2.getPrioridad());
        boolean validPriorityOnOffer2 = parent.isValidPriority(oferta2.getDias(), paquete.getPrioridad());
        
        return packageFitsOnOffer && packageFitsOnOffer2 && validPriorityOnOffer && validPriorityOnOffer2;
    }
    
    private void movePackage(int numberPackage, int oldNumberOffer, int newNumberOffer) {  
        
        Paquete paquete = selectedServices.get(oldNumberOffer).get(numberPackage);
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        double currentCapacityOld = availableOfferWeight.get(oldNumberOffer);
        availableOfferWeight.set(oldNumberOffer, currentCapacityOld - paquete.getPeso());
        availableOfferWeight.set(newNumberOffer, currentCapacity + paquete.getPeso());
        
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        selectedServices.set(newNumberOffer, paquetesOrdenados);
       
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(numberPackage);
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        Oferta oldOffer =  Estado.getSortedOffers().get(oldNumberOffer).getOferta();
        Oferta newOffer =   Estado.getSortedOffers().get(newNumberOffer).getOferta();
        System.out.println("INFELIS "+happiness);
        happiness = happiness + Estado.happiness(newOffer, paquete) - Estado.happiness(oldOffer, paquete);  
        price += Estado.cost(newOffer,paquete) - Estado.cost(oldOffer,paquete);
    }
    
    private void swapPackages(int numberPackage, int oldNumberOffer, int numberPackage2, int newNumberOffer) {  
        //we get both packages..
        Paquete paquete = selectedServices.get(oldNumberOffer).get(numberPackage);
        Paquete paquete2 = selectedServices.get(newNumberOffer).get(numberPackage2);
        
        //update weights from one add package and remove the other..
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        double currentCapacityOld = availableOfferWeight.get(oldNumberOffer);
        availableOfferWeight.set(oldNumberOffer, currentCapacityOld - paquete.getPeso());
        availableOfferWeight.set(newNumberOffer, currentCapacity + paquete.getPeso());
        
        availableOfferWeight.set(newNumberOffer, currentCapacity - paquete2.getPeso());
        availableOfferWeight.set(oldNumberOffer, currentCapacityOld + paquete2.getPeso());
        
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        paquetesOrdenados.remove(numberPackage2);
        selectedServices.set(newNumberOffer, paquetesOrdenados);
        
        //update weights from one add package and remove the other..
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(numberPackage);
        oldPaquetesOrdenados.add(paquete2);
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        //We get the old happiness that the package gives us and the new happiness
        // and we add the difference of happiness so if we get less happiness we're adding
        //something < 0 and otherwise we're adding a positive happiness
        Oferta oldOffer =  parent.getOfferFromSelectedServices(oldNumberOffer);
        Oferta newOffer =  parent.getOfferFromSelectedServices(newNumberOffer);
        happiness = happiness + (Estado.happiness(newOffer,paquete)-Estado.happiness(oldOffer,paquete))
                    +(Estado.happiness(newOffer,paquete2)-Estado.happiness(oldOffer,paquete2));
        price += (Estado.cost(newOffer,paquete)-Estado.cost(oldOffer,paquete)) 
               + (Estado.cost(newOffer,paquete2)-Estado.cost(oldOffer,paquete2));
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
        //System.out.println("Inicial: "+happiness);
        price = parent.getPrice();
        LinkedList<Successor> sucesores = new LinkedList<>(); //rename
        boolean success = false; //rename
        Estado nextEstado = parent;
        String action = "";
        int contador = 0;
        while (!success) {
            ++contador;
            int offerIndex=  random.nextInt(Estado.getOffers().size());
            int offerIndex2 = random.nextInt(Estado.getOffers().size());
            //System.out.println("Again... "+contador);
            
            if (random.nextInt(2) == 0) {
                if (!selectedServices.get(offerIndex).isEmpty() && offerIndex != offerIndex2) {
                    int packageIndex = random.nextInt(selectedServices.get(offerIndex).size());
                    if (validMovement(packageIndex, offerIndex, offerIndex2)) {
                        //System.out.println("MOVE");
                        movePackage(packageIndex, offerIndex, offerIndex2);
                        nextEstado = new Estado(price,happiness, selectedServices, 
                                availableOfferWeight, Estado.getSortedPackages(), Estado.getSortedOffers());
                        success = true;
                    }
                }
            }
            else {
                if (!selectedServices.get(offerIndex).isEmpty() && !selectedServices.get(offerIndex2).isEmpty() && offerIndex != offerIndex2) {
                    int packageIndex = random.nextInt(selectedServices.get(offerIndex).size());
                    int packageIndex2 = random.nextInt(selectedServices.get(offerIndex2).size());
                    if (validSwap(packageIndex,offerIndex,packageIndex2,offerIndex2)) {
                        //System.out.println("SWAP");
                        swapPackages(packageIndex,offerIndex,packageIndex2,offerIndex2);
                        nextEstado = new Estado(price,happiness, selectedServices, 
                                availableOfferWeight, Estado.getSortedPackages(), Estado.getSortedOffers());
                        success = true;
                    }
                }
            }
        }    
        
        
        //action += new HeuristicFunctionCost().getHeuristicValue(nextEstado);
        //System.out.println("Final: "+happiness);
        sucesores.add(new Successor(action, nextEstado));
        return sucesores;
    }
}
