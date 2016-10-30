
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricard
 */
public class GeneradorHillClimbing implements SuccessorFunction{

    
    /*
    Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
    estado actual
    */
    public List getSuccessors(Object state) {
        //data structures needed in the function:
        Estado oldParent = (Estado)state; //?????
        Estado parent = new Estado(oldParent.getPrice(), oldParent.getHappiness(), oldParent.getSelectedServices());
        
        LinkedList<Successor> successors = new LinkedList<>();
        String action = "";
        
        //SWAP
//        for (int offerIndex1 = 0; offerIndex1 < parent.getSelectedServices().size(); ++offerIndex1) {
//            for (int offerIndex2 = 0; offerIndex2 < parent.getSelectedServices().size(); ++offerIndex2) {
//                if (offerIndex1 != offerIndex2) {
//                    for(int position = 0; position < parent.sSpackagesSize(offerIndex1); ++position) {
//                        int packageIndex = parent.getPackage(offerIndex1, position);
//                        if(parent.validSwap(packageIndex, offerIndex1, packageIndex, offerIndex2)) {
//                            parent.swapPackage(packageIndex, offerIndex1, packageIndex, offerIndex2);
//                            successors.add(0, new Successor(action, parent));
//                            parent = new Estado(oldParent.getPrice(), oldParent.getHappiness(), oldParent.getSelectedServices());
//                        }
//                    }
//                }
//            }
//        }  
        //MOVEMENT
        for (int offerIndex1 = 0; offerIndex1 < parent.getSelectedServices().size(); ++offerIndex1) {
            for (int offerIndex2 = 0; offerIndex2 < parent.getSelectedServices().size(); ++offerIndex2) {
                if (offerIndex1 != offerIndex2) {
                    for(int position = 0; position < parent.sSpackagesSize(offerIndex1); ++position) {
                        int packageIndex = parent.getPackage(offerIndex1, position);
                        if(parent.validMovement(packageIndex, offerIndex2)) {
                            parent.movePackage(packageIndex, offerIndex1, offerIndex2, position);
                            successors.add(0, new Successor(action, parent));
                            parent = new Estado(oldParent.getPrice(), oldParent.getHappiness(), oldParent.getSelectedServices());
                        }
                    }
                }
            }
        }  
        
        
        return successors; 
    }  
}
