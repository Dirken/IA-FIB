
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

    private ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private ArrayList<Double> availableOfferWeight;
    
    Estado parent;
    
    private int happiness;
    private double price;

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
    private boolean validMovement2(Paquete paquete, int newNumberOffer){
//        System.out.println("fuck this bullshit omfg");
        Oferta oferta = Estado.getSortedOffers().get(newNumberOffer).getOferta();    
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        boolean packageFits = currentCapacity + paquete.getPeso() <= oferta.getPesomax();
        boolean validPriority = parent.isValidPriority(oferta.getDias(), paquete.getPrioridad());
        System.out.println("AND HIS NAME IS...." + (packageFits && validPriority));
        
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
    private void movePackage2(Paquete paquete, int newNumberOffer) {  
        int oldNumberOffer = Estado.getIndexPackage(paquete);
        
        double currentCapacity = availableOfferWeight.get(newNumberOffer);
        double currentCapacityOld = availableOfferWeight.get(oldNumberOffer);
        availableOfferWeight.set(oldNumberOffer, currentCapacityOld - paquete.getPeso());
        availableOfferWeight.set(newNumberOffer, currentCapacity + paquete.getPeso());
        
        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
        paquetesOrdenados.add(paquete);
        selectedServices.set(newNumberOffer, paquetesOrdenados);
       
        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
        oldPaquetesOrdenados.remove(paquete);
        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
        
        Oferta oldOffer =  Estado.getSortedOffers().get(oldNumberOffer).getOferta();
        Oferta newOffer =   Estado.getSortedOffers().get(newNumberOffer).getOferta();

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
    Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
    estado actual
    */
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
        //SWAP
//        for (int offerIndex = 0; offerIndex < selectedServices.size(); ++offerIndex){
//            //per tots els paquets de cada oferta,
//            for (int packageIndex = 0; packageIndex < parent.getPackagesSizeFromSelectedServices(offerIndex); ++packageIndex){
//                for (int offerIndex2 = offerIndex+1; offerIndex2 < selectedServices.size(); ++offerIndex2 ) { //Buscarem els altres paquets
//                    for (int packageIndex2 = 0; packageIndex2 < parent.getPackagesSizeFromSelectedServices(offerIndex2); ++packageIndex2){
//                        if (validSwap(packageIndex,offerIndex,packageIndex2,offerIndex2)){
//                            swapPackages(packageIndex, offerIndex, packageIndex2, offerIndex2);
//                            nextEstado = new Estado(price,happiness, selectedServices, 
//                                availableOfferWeight, Estado.getSortedPackages(), Estado.getSortedOffers());
//                            sucesores.add(new Successor(action,nextEstado));
////                            swapPackages(packageIndex2, offerIndex2, packageIndex, offerIndex);
//                        }
//                    }
//                }
//            }
//        }
        for (ArrayList<Paquete> s : selectedServices){
            for (Paquete  p : s){
                for(int indiceServicio = 0; indiceServicio < selectedServices.size() 
                        && s != selectedServices.get(indiceServicio); ++indiceServicio){
                      if (validMovement2(p,indiceServicio)){
                          System.out.println("fuck this bullshit omfg");
                          int oldNumberOffer = Estado.getIndexPackage(p);
                          movePackage2(p, indiceServicio);

                          nextEstado = new Estado(price,happiness, selectedServices, 
                                  availableOfferWeight, Estado.getSortedPackages(), Estado.getSortedOffers());
                          sucesores.add(new Successor(action,nextEstado));
                          movePackage2(p, oldNumberOffer);
                      }
                }
            }
        }
        System.out.println(sucesores);
        return sucesores; 
    }  
}
