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
public class OfertaOrdenada implements Comparable<OfertaOrdenada>  {

    private Oferta oferta;

    public OfertaOrdenada(Oferta oferta) {
        this.oferta = oferta;
    }

    public Oferta getOferta() {
        return oferta;
    }
    
    @Override
    public int compareTo(OfertaOrdenada o) {
        return Integer.compare(oferta.getDias(), o.oferta.getDias());
    }
    
}
