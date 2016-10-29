/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2;

import IA.Azamon.*;
import azamonv2.Estado;

/**
 *
 * @author Eironeia
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Paquetes paquetes = new Paquetes(10,1234);
        Transporte transporte = new Transporte(paquetes, 1.2 , 1234);
        Estado.setPackages(paquetes);
        Estado.setOffers(transporte);
        Estado inicial = new Estado();
        System.out.println(inicial);
    }
    
}
