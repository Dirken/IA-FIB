
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2.Generadores;

import azamonv2.Generadores.*;
import azamonv2.*;
import azamonv2.SortedClasses.*;
import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
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
    
    Random random;
    
    public GeneradorSimulatedAnneiling(int seed) {
        random = new Random(seed);
    }
    
    @Override
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        Estado oldState = (Estado)state; //?????
        Estado parent;
        parent = new Estado();
        parent.selectedServices = (ArrayList<ArrayList<Integer>>)oldState.selectedServices.clone();
        parent.availableWeight = ( ArrayList<Double>) oldState.getAvailableWeight().clone();
        parent.happiness = oldState.happiness;
        parent.price = oldState.price;
        

        LinkedList<Successor> successors = new LinkedList<>();
        String action = "";
        
        boolean found = false;
        while (!found) {
            
            int offerSize = Estado.getSortedOffers().size();
            int offerIndex1 =  random.nextInt(offerSize);
            int offerIndex2 = random.nextInt(offerSize);
            while(offerIndex1 == offerIndex2) offerIndex2 = random.nextInt(offerSize);
            boolean arePackageInside1 = !parent.selectedServices.get(offerIndex1).isEmpty();
            boolean arePackageInside2 = !parent.selectedServices.get(offerIndex2).isEmpty();
            if (arePackageInside1) {
                if (random.nextInt(2) == 0) { //move
                    int position1 = random.nextInt(parent.sSpackagesSize(offerIndex1));
                    int packageIndex1 = parent.getPackage(offerIndex1, position1);
                    if(parent.validMovement(packageIndex1, offerIndex2)) {
                        parent.movePackage(packageIndex1, offerIndex1, offerIndex2, position1);   
                        Estado result = new Estado(
                                    parent.price, 
                                    parent.happiness,
                                    (ArrayList<ArrayList<Integer>>) parent.selectedServices.clone()
                                  , (ArrayList<Double>) parent.availableWeight.clone());
                        successors.add(0, new Successor(action, result));
                        parent.moveBackPackage(packageIndex1, offerIndex1, offerIndex2, position1);
                        found = true;
                    }
                }
                else { //swap 
                    if (arePackageInside2) {
                        int position1 = random.nextInt(parent.sSpackagesSize(offerIndex1));
                        int position2 = random.nextInt(parent.sSpackagesSize(offerIndex2));
                        int packageIndex1 = parent.getPackage(offerIndex1, position1);
                        int packageIndex2 = parent.getPackage(offerIndex2, position2);
                        if(parent.validSwap(packageIndex1, offerIndex1, packageIndex2, offerIndex2)) {
                            parent.swapPackage(packageIndex1, offerIndex1, packageIndex2, offerIndex2, position1, position2);
                            Estado result = new Estado(
                                    parent.price, 
                                    parent.happiness,
                                    (ArrayList<ArrayList<Integer>>) parent.selectedServices.clone()
                                  , (ArrayList<Double>) parent.availableWeight.clone());
                            successors.add(0, new Successor(action, result));
                            parent.swapBackPackage(packageIndex1, offerIndex1, packageIndex2, offerIndex2, position1, position2);
                            found = true;
                        }
                    }
                }
            }
        }
        
        return successors;
    }
}
