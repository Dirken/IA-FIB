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
    
    private ArrayList<Paquetes> serviciosEscogidos; // resultat?
    private ArrayList<Double> pesoOfertaDisponible;
    private ArrayList<PaqueteOrdenado> paquetesOrdenados;
    
    public Estado() {
        pesoOfertaDisponible = new ArrayList<>();
        paquetesOrdenados = new ArrayList<>();
        
        int indexPaquetes = 0;
        int indexOfertas = 0;
        
        llenarArrays(pesoOfertaDisponible, paquetesOrdenados); // O(max(n,m))
        sortPaquetes(paquetesOrdenados);
        
        

    }
    
    /** Fill arrays with cost O(max(n,m)) 
     * where n is paquetes.size() and m is ofertas.size()
     * instead of O(n) + O(m)
     * @param pesoOfertaDisponible
     * @param paquetesOrdenados 
     */
    private void llenarArrays(ArrayList<Double>  pesoOfertaDisponible
                            , ArrayList<PaqueteOrdenado> paquetesOrdenados) {
        
        
        int indexPaquetes = 0;
        int indexOfertas = 0;
        
        while(indexPaquetes < paquetes.size() && indexOfertas < ofertas.size()){
            PaqueteOrdenado paqueteOrdenado = new PaqueteOrdenado(paquetes.get(indexPaquetes));
            paquetesOrdenados.add(indexPaquetes, paqueteOrdenado);
            
            pesoOfertaDisponible.add(indexOfertas, 0.0);
            
            indexPaquetes += 1;
            indexOfertas += 1;
        }
        
        while(indexPaquetes < paquetes.size()) {
            PaqueteOrdenado paqueteOrdenado = new PaqueteOrdenado(paquetes.get(indexPaquetes));
            paquetesOrdenados.add(indexPaquetes, paqueteOrdenado);
            indexPaquetes += 1;
        }
        
        while(indexOfertas < ofertas.size()) {
            pesoOfertaDisponible.add(indexOfertas, 0.0);
            indexOfertas += 1;
        }
    }
    
    
    private void sortPaquetes(ArrayList<PaqueteOrdenado> paquetesOrdenados) {
        paquetesOrdenados.sort((paqueteOrdenado1, paqueteOrdenado2) -> {
            return Integer.compare(paqueteOrdenado1.getPaquete().getPrioridad()
                                 , paqueteOrdenado2.getPaquete().getPrioridad());
        });
    }
    
    private void printPaqueteOrdenadoPriority(ArrayList<PaqueteOrdenado> paquetesOrdenados) {
        for (int i = 0; i < paquetesOrdenados.size(); ++i) {
            System.out.println(paquetesOrdenados.get(i).getPaquete().getPrioridad());
        }
        
    }
    
    public static void setOfertas(Transporte ofertas) {
        Estado.ofertas = ofertas;
    }

    public static void setPaquetes(Paquetes paquetes) {
        Estado.paquetes = paquetes;
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
