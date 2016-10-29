
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2.Generadores;

import azamonv2.*;
import azamonv2.Generadores.*;
import azamonv2.SortedClasses.*;
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

    
//    private void swapPackages(int numberPackage, int oldNumberOffer, int numberPackage2, int newNumberOffer) {  
//        //we get both packages..
//        Paquete paquete = selectedServices.get(oldNumberOffer).get(numberPackage);
//        Paquete paquete2 = selectedServices.get(newNumberOffer).get(numberPackage2);
//        
//        //update weights from one add package and remove the other..
//        double currentCapacity = availableOfferWeight.get(newNumberOffer);
//        double currentCapacityOld = availableOfferWeight.get(oldNumberOffer);
//        availableOfferWeight.set(oldNumberOffer, currentCapacityOld - paquete.getPeso());
//        availableOfferWeight.set(newNumberOffer, currentCapacity + paquete.getPeso());
//        
//        availableOfferWeight.set(newNumberOffer, currentCapacity - paquete2.getPeso());
//        availableOfferWeight.set(oldNumberOffer, currentCapacityOld + paquete2.getPeso());
//        
//        ArrayList<Paquete> paquetesOrdenados = selectedServices.get(newNumberOffer);
//        paquetesOrdenados.add(paquete);
//        paquetesOrdenados.remove(numberPackage2);
//        selectedServices.set(newNumberOffer, paquetesOrdenados);
//        
//        //update weights from one add package and remove the other..
//        ArrayList<Paquete> oldPaquetesOrdenados = selectedServices.get(oldNumberOffer);
//        oldPaquetesOrdenados.remove(numberPackage);
//        oldPaquetesOrdenados.add(paquete2);
//        selectedServices.set(oldNumberOffer, oldPaquetesOrdenados);
//        
//        //We get the old happiness that the package gives us and the new happiness
//        // and we add the difference of happiness so if we get less happiness we're adding
//        //something < 0 and otherwise we're adding a positive happiness
//        Oferta oldOffer =  parent.getOfferFromSelectedServices(oldNumberOffer);
//        Oferta newOffer =  parent.getOfferFromSelectedServices(newNumberOffer);
//        happiness = happiness + (Estado.happiness(newOffer,paquete)-Estado.happiness(oldOffer,paquete))
//                    +(Estado.happiness(newOffer,paquete2)-Estado.happiness(oldOffer,paquete2));
//        price += (Estado.cost(newOffer,paquete)-Estado.cost(oldOffer,paquete)) 
//               + (Estado.cost(newOffer,paquete2)-Estado.cost(oldOffer,paquete2));
//    }


    /*
    Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
    estado actual
    */
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        Estado oldParent = (Estado)state; //?????
        Estado parent = oldParent;
        
        
        LinkedList<Successor> successors = new LinkedList<>();
        String action = "";
        
        for (int offerIndex1 = 0; offerIndex1 < parent.getSelectedServices().size(); ++offerIndex1) {
            for (int offerIndex2 = 0; offerIndex2 < parent.getSelectedServices().size(); ++offerIndex2) {
                if (offerIndex1 != offerIndex2) {
                    for(int position = 0; position < parent.sSpackagesSize(offerIndex1); ++position) {
                        int packageIndex = parent.getPackage(offerIndex1, position);
                        if(parent.validMovement(packageIndex, offerIndex2)) {
                            parent.movePackage(packageIndex, offerIndex1, offerIndex2, position);
                            successors.add(0, new Successor(action, parent));
                            parent = oldParent;
                        }
                    }
                }
            }
        }  
        
        
        return successors; 
    }  
}
