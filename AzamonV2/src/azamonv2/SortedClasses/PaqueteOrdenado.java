/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2.SortedClasses;
import IA.Azamon.*;
/**
 *
 * @author Eironeia
 */
public class PaqueteOrdenado implements Comparable<PaqueteOrdenado>{

    private Paquete paquete;
    
    public PaqueteOrdenado(Paquete paquete) {
        this.paquete = paquete;
    }
    
    public Paquete getPaquete() {
        return this.paquete;
    }
    
    @Override
    public int compareTo(PaqueteOrdenado o) {
       return Integer.compare(paquete.getPrioridad(), o.paquete.getPrioridad());
    }
}
