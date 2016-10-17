package azamon;

import IA.Azamon.*;
import java.util.*; 

public class Estado {

    private double precio;
    private int felicidad;
    
    //private ArrayList<Integer> paquetesAsignados;
    //private ArrayList<Double> ofertasPeso; // Cambiar nombre (?)
    
    private static Transporte ofertas;
    private static Paquetes paquetes;
    
    private ArrayList<Paquetes> servicioEscogido; // resultat?
    
    public Estado(ArrayList<Integer> paquetesAsignados, ArrayList<Double> ofertasPeso, int felicidad, double precio) {
        //this.paquetesAsignados = paquetesAsignados;
        //this.ofertasPeso = ofertasPeso;
        this.felicidad = felicidad;
        this.precio = precio;
    }
    
    /**
     * 
     * Tenim:
     * Paquetes -> Per cada paquete -> pes i una prioritat
     * Oferta -> per cada oferta (camions?) -> pesoMax, preuXkg, dias 
     * 
     * 
     * Volem:
     * 
     * Precio: Preu total que hem obtingut
     * Felicitat: La felicitat total
     * 
     * 1r cas: Paquetes mas pequeñ
     * 
     * Resultat: Vector on index indica el número de la oferta 0 - X, 
     * dintre de cada casella tens els paquets s'envien per aquella ofera
     */
    
    /*
    MaximitzarFelicitat
    
    if oferta.diasEnviament <= paquet.diesQueTarda 
       & oferta.capacitatTotal <= (oferta.capacitatActual + paquet.pes) {
        afegirPaquetALaOfertaQueEstiguemMirant
    }
    else {
        nextOferta
    }
    
    
    MinimitzarPreu
    
    if oferta.dias <= paquet.diesQueTarda
    
    
    */
}
