
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
public class GeneradorHillClimbing implements SuccessorFunction{

    private Random random;
    
    private static Transporte offers;
    private static Paquetes packages;
    
    private ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private ArrayList<Double> availableOfferWeight;
    private ArrayList<PaqueteOrdenado> sortedPackages;
    private ArrayList<OfertaOrdenada> sortedOffers;
    
    Estado parent;

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
        Paquete paquete = parent.getPackageFromSelectedServices(oldNumberOffer, numberPackage);
        parent.updateWeightFromAvailableOfferWeight(newNumberOffer, paquete.getPeso());
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        selectedServices.set(newNumberOffer, paquetesOrdenados);
        parent.updateWeightFromAvailableOfferWeight(oldNumberOffer, -paquete.getPeso());
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(numberPackage);
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        //We get the old happiness that the package gives us and the new happiness
        // and we add the difference of happiness so if we get less happiness we're adding
        //something < 0 and otherwise we're adding a positive happiness
        Oferta oldOffer =  parent.getOfferFromSelectedServices(oldNumberOffer);
        Oferta newOffer =  parent.getOfferFromSelectedServices(newNumberOffer);
        int oldHappiness = parent.happiness(oldOffer,paquete);
        int newHappiness = parent.happiness(newOffer,paquete);
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
    Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
    estado actual
    */
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        String movements = "";
        LinkedList<Successor>listaEstadosSucesores = new LinkedList();
        parent = (Estado)state;
        selectedServices = parent.getSelectedServices();        
        PaqueteOrdenado paquete;
        OfertaOrdenada offer;
        
        //SWAP
        for (int offerIndex = 0; offerIndex < selectedServices.size(); ++offerIndex){
            //per tots els paquets de cada oferta,
            for (int packageIndex = 0; packageIndex < parent.getPackagesSizeFromSelectedServices(offerIndex); ++packageIndex){
                for (int offerIndex2 = offerIndex+1; offerIndex2 < selectedServices.size(); ++offerIndex2 ) { //Buscarem els altres paquets
                    for (int packageIndex2 = 0; packageIndex2 < parent.getPackagesSizeFromSelectedServices(offerIndex2); ++packageIndex2){
                        if (validSwap(packageIndex,offerIndex,packageIndex2,offerIndex2)){
//                                movements += "( " + paquete + "," + parent.getOfferFromSelectedServices(numberOffer) + " )" 
//                                                    + "<-> " +
//                                                     "( " + paquete2 + "," + parent.getOfferFromSelectedServices(numberOffer2) +" )" + "\n";
                            swapPackages(packageIndex, offerIndex, packageIndex2, offerIndex2);
                            listaEstadosSucesores.add(new Successor(movements,parent));
                            swapPackages(packageIndex2, offerIndex2, packageIndex, offerIndex);
                        }
                    }
                }
            }
        }
        //MOVE
        for (int offerIndex = 0; offerIndex < selectedServices.size(); ++offerIndex){
            //per tots els paquets de cada oferta,
            for (int packageIndex = 0; packageIndex < parent.getPackagesSizeFromSelectedServices(offerIndex); ++packageIndex){
                for (int offerIndex2 = offerIndex+1; offerIndex2 < selectedServices.size(); ++offerIndex2 ) { //Buscarem els altres paquets
                    if (validMovement(packageIndex,offerIndex2)) {
//                        movements += "Paquete -> " + paquete + " Oferta " + parent.getOfferFromSelectedServices(numberOffer) + "\n";
                        movePackage(packageIndex, offerIndex, offerIndex2);
                        listaEstadosSucesores.add(new Successor(movements,parent));
                        movePackage(packageIndex, offerIndex2, offerIndex);
                    }
                }
            }
        }
        return listaEstadosSucesores; 
    }  
}
