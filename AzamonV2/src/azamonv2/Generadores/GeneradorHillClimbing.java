
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
        Estado oldState = (Estado)state; //?????
        parent = new Estado();
        parent.selectedServices = (ArrayList<ArrayList<Integer>>)oldState.selectedServices.clone();
        parent.availableWeight = ( ArrayList<Double>) oldState.getAvailableWeight().clone();
        parent.happiness = oldState.happiness;
        parent.price = oldState.price;

        print(parent.selectedServices.size()+"");
        print(parent.availableWeight+"");
        System.out.println(parent);
        LinkedList<Successor> successors = new LinkedList<>();
        String action = "";
//       for (int offerIndex1 = 0; offerIndex1 < parent.selectedServices.size(); ++offerIndex1) {
//           for (int position1 = 0; position1 < parent.selectedServices.get(offerIndex1).size(); ++position1) {
//               int packageIndex1 = parent.getPackage(offerIndex1, position1);
//               
//               for (int offerIndex2 = offerIndex1+1; offerIndex2 < parent.selectedServices.size(); ++offerIndex2) {
//                   for (int position2 = 0; position2 < parent.selectedServices.get(offerIndex2).size(); ++position2) {
//                       int packageIndex2 = parent.getPackage(offerIndex2, position2);
//                       
//                       if (parent.validSwap(packageIndex1, offerIndex1, packageIndex2, offerIndex2)) {
//                           print("HOLA QUE ASE");
//                           Estado newState = new Estado();
//                           newState.availableWeight = (ArrayList<Double>)parent.getAvailableWeight().clone();
//                           newState.offersPrices = (ArrayList<Double>)parent.getOffersPrices().clone();
//                           newState.selectedServices = (ArrayList<ArrayList<Integer>>) parent.getSelectedServices().clone();
//                           newState.happiness = parent.happiness;
//                           newState.price = parent.price;
//                           newState.swapPackage(packageIndex1, offerIndex1, packageIndex2, offerIndex2, position1, position2);
//                           successors.add(0, new Successor(action, newState));
//                       }
//                   }
//               }
//           }
//       }
//       for (int offerIndex1 = 0; offerIndex1 < parent.selectedServices.size(); ++offerIndex1) {
//           for (int position1 = 0; position1 < parent.selectedServices.get(offerIndex1).size(); ++position1) {
//               int packageIndex1 = parent.getPackage(offerIndex1, position1);
//               for (int offerIndex2 = 0; offerIndex2 < parent.selectedServices.size(); ++offerIndex2) {
//                   if (offerIndex1 != offerIndex2) {
//                       
//                       if (parent.validMovement(packageIndex1, offerIndex1)) {
//                            Estado newState = new Estado();
//                            newState.availableWeight = (ArrayList<Double>)parent.getAvailableWeight().clone();
//                            newState.selectedServices = (ArrayList<ArrayList<Integer>>) parent.getSelectedServices().clone();
//                            newState.happiness = parent.happiness;
//                            newState.price = parent.price;
//                            print(packageIndex1 + " " + offerIndex1 + " " + offerIndex2 + " " + position1);
//                            print(newState.selectedServices+"");
//                            newState.movePackage(packageIndex1, offerIndex1, offerIndex2, position1);
//                            successors.add(0, new Successor(action, newState));
//                        }
//                   }
//               }
//               
//               
//           }
//       }
       
        return successors; 
    }  
    
    private void print(String s) {
        System.out.println(s);
    }
}
