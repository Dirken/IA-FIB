
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2.Generadores;

import azamonv2.*;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Ricard
 */
public class GeneradorHillClimbing implements SuccessorFunction{

    
    /*
    Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
    estado actual
    */
    
    Estado parent;
    
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        parent = (Estado)state; //?????

        LinkedList<Successor> successors = new LinkedList<>();
        String action = "";
        
        for (int offerIndex1 = 0; offerIndex1 < parent.getSelectedServices().size(); ++offerIndex1) {
            for (int offerIndex2 = 0; offerIndex2 < parent.getSelectedServices().size(); ++offerIndex2) {
                if (offerIndex1 != offerIndex2) {
                    for(int position1 = 0; position1 < parent.sSpackagesSize(offerIndex1); ++position1) {
                        int packageIndex1 = parent.getPackage(offerIndex1, position1);
                        //SWAP
                        for (int position2 = 0; position2 < parent.sSpackagesSize(offerIndex2); ++position2) {
                            
                            int packageIndex2 = parent.getPackage(offerIndex2, position2);
                            if(parent.validSwap(packageIndex1, offerIndex1, packageIndex2, offerIndex2)) {
                                
                                Estado newState = new Estado();
                                newState.selectedServices = (ArrayList<ArrayList<Integer>>)parent.selectedServices.clone();
                                newState.availableWeight = ( ArrayList<Double>) parent.getAvailableWeight();
                                newState.happiness = parent.happiness;
                                newState.price = parent.price;
                                newState.swapPackage(packageIndex1, offerIndex1, packageIndex2, offerIndex2, position1, position2);
                                
                                successors.add(0, new Successor(action, newState));
                            }
                            
                        }
                        //MOVE
                        if(parent.validMovement(packageIndex1, offerIndex2)) {
                            
                            Estado newState = new Estado();
                            newState.selectedServices = (ArrayList<ArrayList<Integer>>)parent.selectedServices.clone();
                            newState.availableWeight = ( ArrayList<Double>) parent.getAvailableWeight();
                            newState.happiness = parent.happiness;
                            newState.price = parent.price;
                            newState.movePackage(packageIndex1, offerIndex1, offerIndex2, position1);
                            
                            successors.add(0, new Successor(action, newState));
                        }
                    }
                }
            }
        }  
        return successors; 
    }  
}
