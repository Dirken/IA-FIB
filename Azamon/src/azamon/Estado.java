package azamon;

import IA.Azamon.*;
import java.util.*; 

public class Estado {

    private double precio;
    private int felicidad;
    
    private static Transporte ofertas;
    private static Paquetes paquetes;
    
    private ArrayList<ArrayList<Paquete>> serviciosEscogidos; // resultat
    private ArrayList<Double> pesoOfertaDisponible;
    private ArrayList<PaqueteOrdenado> paquetesOrdenados;
    private ArrayList<OfertaOrdenada> ofertasOrdenadas;
    
    /**
     * Estado constructora
     */
    public Estado() {
        this.felicidad = 0;
        this.precio = 0;
        pesoOfertaDisponible = new ArrayList<>();
        paquetesOrdenados = new ArrayList<>();
        ofertasOrdenadas = new ArrayList<>();
        serviciosEscogidos = new ArrayList<>(ofertas.size());

        llenarArrays(); // O(max(n,m))
        sortPaquetes();
        sortOfertas(); // NOSE SI CAL (?)
        boolean b = canGetASolution();
    }
    
    /** Fill arrays with cost O(max(n,m)) 
     * where n is paquetes.size() and m is ofertas.size()
     * instead of O(n) + O(m)
     * @param pesoOfertaDisponible
     * @param paquetesOrdenados 
     */
    private void llenarArrays() {
        
        int indexPaquetes = 0;
        int indexOfertas = 0;
        
        while(indexPaquetes < paquetes.size() && indexOfertas < ofertas.size()){
            PaqueteOrdenado paqueteOrdenado = new PaqueteOrdenado(paquetes.get(indexPaquetes));
            paquetesOrdenados.add(indexPaquetes, paqueteOrdenado);
            
            OfertaOrdenada ofertaOrdenada = new OfertaOrdenada(ofertas.get(indexOfertas));
            ofertasOrdenadas.add(indexOfertas, ofertaOrdenada);
            
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
            OfertaOrdenada ofertaOrdenada = new OfertaOrdenada(ofertas.get(indexOfertas));
            ofertasOrdenadas.add(indexOfertas, ofertaOrdenada);
            
            pesoOfertaDisponible.add(indexOfertas, 0.0);
            
            indexOfertas += 1;
        }
    }
    
    /**
     * Ordena crecientemente todos los paquetes 
     * que obtiene por el parametro implicito en función de la prioridad
     * @param paquetesOrdenados 
     */
    private void sortPaquetes() {
        paquetesOrdenados.sort((paqueteOrdenado1, paqueteOrdenado2) -> {
            return Integer.compare(paqueteOrdenado1.getPaquete().getPrioridad()
                                 , paqueteOrdenado2.getPaquete().getPrioridad());
        });
    }
    
    /**
     * Ordena crecientemente todas las ofertas
     * que obtiene por el parametro implícito en función de la prioridad
     * @param ofertasOrdenadas 
     */
    private void sortOfertas() {
        ofertasOrdenadas.sort((ofertaOrdenada1, ofertaOrdenada2) -> {
            return Integer.compare(ofertaOrdenada1.getOferta().getDias()
                                 , ofertaOrdenada2.getOferta().getDias());
        });
    }
    
    /**
     * Printa la prioridad de los paquetes del parametro implícito
     * @param paquetesOrdenados 
     */
    private void printPaqueteOrdenadoPriority() {
        for (int i = 0; i < paquetesOrdenados.size(); ++i) {
            System.out.println(paquetesOrdenados.get(i).getPaquete().getPrioridad());
        }
        
    }
    
    /**
     * Define las ofertas del estado
     * @param ofertas 
     */
    public static void setOfertas(Transporte ofertas) {
        Estado.ofertas = ofertas;
    }

    /**
     * Define los paquetes del estado
     * @param paquetes 
     */
    public static void setPaquetes(Paquetes paquetes) {
        Estado.paquetes = paquetes;
    }
    
    private boolean canGetASolution() {
        ArrayList<Boolean> packagesSaved = new ArrayList<>(Collections.nCopies(paquetesOrdenados.size(),false));
        
        for(int indexOferta = 0 ; indexOferta < ofertasOrdenadas.size(); ++indexOferta) {
            ArrayList<Paquete> paquetesEscogidos = new ArrayList<>();
            
            for (int indexPaquete = 0; indexPaquete < paquetesOrdenados.size(); ++indexPaquete) {
                
                Oferta oferta = ofertasOrdenadas.get(indexOferta).getOferta();
                Paquete paquete = paquetesOrdenados.get(indexPaquete).getPaquete();
                
                double currentCapacity = pesoOfertaDisponible.get(indexOferta);
                double maxCapacity = oferta.getPesomax();
                double packageWeight = paquete.getPeso();
                
                int offerPriority = oferta.getDias();
                int deliveryDay = paquete.getPrioridad();
                if (availableCapacityToAdd(currentCapacity, maxCapacity, packageWeight)
                    && isValidPriority(offerPriority, deliveryDay)
                    && !packagesSaved.get(indexPaquete)) {
                    //Afegir i update de packagesSaved
                    
                    pesoOfertaDisponible.set(indexOferta, currentCapacity+packageWeight);
                    packagesSaved.set(indexPaquete, true);
                    this.felicidad += felicidadTotal(oferta, paquete);
                    this.precio += costeTotal(oferta, paquete);
                    paquetesEscogidos.add(paquete);
                }
            }
            serviciosEscogidos.add(indexOferta, paquetesEscogidos);
        }
            
        return allPackageSaved(packagesSaved);
    }
    
    private boolean allPackageSaved(ArrayList<Boolean> packagesSaved) {
        for(int indexPaquete = 0; indexPaquete < packagesSaved.size(); ++indexPaquete){
            if(!packagesSaved.get(indexPaquete)) return false;
        }
        return true;
    }
    
    private boolean availableCapacityToAdd(double currentCapacity, double maxCapacity, double packageWeight) {
        return currentCapacity+packageWeight <= maxCapacity;
    }
    
    private boolean isValidPriority(int priority, int deliveryDay) {
        //System.out.println("PRIORITY; "+priority+" DELIVERYDAY: "+deliveryDay);
        switch (deliveryDay) {
            case Paquete.PR1:
                return priority == 1;
            case Paquete.PR2:
                return priority == 2 || priority == 3;
            case Paquete.PR3:
                return priority == 4 || priority == 5;
            default:
                System.out.println("Incorrecct priority");
        }
        return false;
    }
    
    public static double costeTotal(Oferta oferta, Paquete paquete) {
        double costeTotal = oferta.getPrecio()*paquete.getPeso();
        switch (oferta.getDias()) {
            case 3:
                costeTotal += 0.25*paquete.getPeso();
            case 4:
                costeTotal += 0.25*paquete.getPeso();
            case 5:
                costeTotal += 0.5*paquete.getPeso();
            default:
        }
        return costeTotal;
    }
    
    public static int felicidadTotal(Oferta oferta, Paquete paquete) {
        switch (paquete.getPrioridad()) {
            case Paquete.PR2:
                return 3-oferta.getDias();
            case Paquete.PR3:
                return 5-oferta.getDias();
            default:
                return 0;
        }
    }
    
    @Override
    public String toString() {
        String s = "";
        s += "Número de ofertas de transporte: " + ofertas.size() + " || Felicidad: " + felicidad + " || Precio: " + precio + "\n";
        for (int i = 0; i < pesoOfertaDisponible.size(); ++i) {
            s += "Oferta número " + i + " con peso " + pesoOfertaDisponible.get(i) + "/" + ofertas.get(i).getPesomax() + ", con dias de entrega " + ofertas.get(i).getDias() +  " y con precio: " + ofertas.get(i).getPrecio() + ":\n";
            ArrayList<Paquete> paquetesEscogidos = serviciosEscogidos.get(i);
            for (int p = 0; p < paquetesEscogidos.size(); ++p) {
                s += "\tIndice: " + p + " " +  paquetesEscogidos.get(p) + "\n";
            }
        }
        return s;
    }
    
}
