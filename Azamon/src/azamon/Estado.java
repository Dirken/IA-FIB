package azamon;

import IA.Azamon.*;
import java.util.*; 

public class Estado {

    private double price;
    private int hapiness;
    
    private static Transporte offers;
    private static Paquetes packages;
    
    private ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private ArrayList<Double> availableOfferWeight;
    private ArrayList<PaqueteOrdenado> sortedPackages;
    private ArrayList<OfertaOrdenada> sortedOffers;
    
    /**
     * Estado constructora
     */
    public Estado() {
        this.hapiness = 0;
        this.price = 0;
        availableOfferWeight = new ArrayList<>();
        sortedPackages = new ArrayList<>();
        sortedOffers = new ArrayList<>();
        selectedServices = new ArrayList<>(offers.size());

        fillArrays(); // O(max(n,m))
        sortPackages();
        sortOffers(); // NOSE SI CAL (?)
        canGetASolution();
    }
    
    /** Fill arrays with cost O(max(n,m)) 
     * where n is paquetes.size() and m is offers.size()
     * instead of O(n) + O(m)
     * @param availableOfferWeight
     * @param sortedPackages 
     */
    private void fillArrays() {
        
        int packageIndex = 0;
        int offerIndex = 0;
        
        while(packageIndex < packages.size() && offerIndex < offers.size()){
            PaqueteOrdenado sortedPackage = new PaqueteOrdenado(packages.get(packageIndex));
            sortedPackages.add(packageIndex, sortedPackage);
            
            OfertaOrdenada sortedOffer = new OfertaOrdenada(offers.get(offerIndex));
            sortedOffers.add(offerIndex, sortedOffer);
            
            availableOfferWeight.add(offerIndex, 0.0);
  
            packageIndex += 1;
            offerIndex += 1;
        }
        
        while(packageIndex < packages.size()) {
            PaqueteOrdenado sortedPackage = new PaqueteOrdenado(packages.get(packageIndex));
            sortedPackages.add(packageIndex, sortedPackage);
            packageIndex += 1;
        }
        
        while(offerIndex < offers.size()) {
            OfertaOrdenada sortedOffer = new OfertaOrdenada(offers.get(offerIndex));
            sortedOffers.add(offerIndex, sortedOffer);
            
            availableOfferWeight.add(offerIndex, 0.0);
            
            offerIndex += 1;
        }
    }
    
    /**
     * Ordena crecientemente todos los paquetes 
     * que obtiene por el parametro implicito en función de la prioridad
     * @param sortedPackages 
     */
    private void sortPackages() {
        sortedPackages.sort((sortedPackage1, sortedPackage2) -> {
            return Integer.compare(sortedPackage1.getPaquete().getPrioridad()
                                 , sortedPackage2.getPaquete().getPrioridad());
        });
    }
    
    /**
     * Ordena crecientemente todas las ofertas
     * que obtiene por el parametro implícito en función de la prioridad
     * @param sortedOffers 
     */
    private void sortOffers() {
        sortedOffers.sort((sortedOffer1, sortedOffer2) -> {
            return Integer.compare(sortedOffer1.getOferta().getDias()
                                 , sortedOffer2.getOferta().getDias());
        });
    }
    
    /**
     * Printa la prioridad de los paquetes del parametro implícito
     * @param sortedPackages 
     */
    private void printPaqueteOrdenadoPriority() {
        for (int i = 0; i < sortedPackages.size(); ++i) {
            System.out.println(sortedPackages.get(i).getPaquete().getPrioridad());
        }
        
    }
    
    /**
     * Define las ofertas del estado
     * @param ofertas 
     */
    public static void setOfertas(Transporte offers) {
        Estado.offers = offers;
    }

    /**
     * Define los paquetes del estado
     * @param paquetes 
     */
    public static void setPaquetes(Paquetes packages) {
        Estado.packages = packages;
    }
    
    /**
     * Comprueba si se puede obtener una solución válida
     * @return 
     */
    private boolean canGetASolution() {
        ArrayList<Boolean> packagesSaved = new ArrayList<>(Collections.nCopies(sortedPackages.size(),false));
        
        for(int indexOferta = 0 ; indexOferta < sortedOffers.size(); ++indexOferta) {
            ArrayList<Paquete> selectedPackages = new ArrayList<>();
            
            for (int indexPaquete = 0; indexPaquete < sortedPackages.size(); ++indexPaquete) {
                
                Oferta oferta = sortedOffers.get(indexOferta).getOferta();
                Paquete paquete = sortedPackages.get(indexPaquete).getPaquete();
                
                double currentCapacity = availableOfferWeight.get(indexOferta);
                double maxCapacity = oferta.getPesomax();
                double packageWeight = paquete.getPeso();
                
                int offerPriority = oferta.getDias();
                int deliveryDay = paquete.getPrioridad();
                if (availableCapacityToAdd(currentCapacity, maxCapacity, packageWeight)
                    && isValidPriority(offerPriority, deliveryDay)
                    && !packagesSaved.get(indexPaquete)) {
                    //Afegir i update de packagesSaved
                    
                    availableOfferWeight.set(indexOferta, currentCapacity+packageWeight);
                    packagesSaved.set(indexPaquete, true);
                    this.hapiness += hapinessTotal(oferta, paquete);
                    this.price += costeTotal(oferta, paquete);
                    selectedPackages.add(paquete);
                }
            }
            selectedServices.add(indexOferta, selectedPackages);
        }
            
        return allPackageSaved(packagesSaved);
    }
    
    /**
     * Comprueba que todos los paquetes hayans ido assignados
     * y por lo tanto que se haya podido llegar a una solución
     * @param packagesSaved
     * @return 
     */
    private boolean allPackageSaved(ArrayList<Boolean> packagesSaved) {
        for(int indexPaquete = 0; indexPaquete < packagesSaved.size(); ++indexPaquete){
            if(!packagesSaved.get(indexPaquete)) return false;
        }
        return true;
    }
    
    
    /**
     * Comprueba si hay suficiente espacio en una oferta determinada en función del peso del paquete
     * @param currentCapacity
     * @param maxCapacity
     * @param packageWeight
     * @return 
     */
    private boolean availableCapacityToAdd(double currentCapacity, double maxCapacity, double packageWeight) {
        return currentCapacity+packageWeight <= maxCapacity;
    }
    
    /**
     * Comprueba si la prioridad del paquete es menor o igual 
     * que los dias que tardan en enviar la oferta seleccionada
     * @param priority
     * @param deliveryDay
     * @return 
     */
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
    
    /**
     * Devuelve el coste total de añadir un paquete en función 
     * del peso del paquete y el precio por kg de la oferta
     * @param oferta
     * @param paquete
     * @return 
     */
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
    
    /**
     * Devuelve la felicidad total de añadir un paquete en función
     * de la prioridad del paquete y la antelación con la que se envía 
     * en función de los dias de la oferta
     * @param oferta
     * @param paquete
     * @return 
     */
    public static int hapinessTotal(Oferta oferta, Paquete paquete) {
        switch (paquete.getPrioridad()) {
            case Paquete.PR2:
                return 3-oferta.getDias();
            case Paquete.PR3:
                return 5-oferta.getDias();
            default:
                return 0;
        }
    }
    
    /**
     * Nos genera una salida con un string costumizado 
     * para facilitar la capacidad de entender y gestionar la respuesta
     * @return 
     */
    @Override
    public String toString() {
        String s = "";
        s += "Número de ofertas de transporte: " + offers.size() + " || Felicidad: " + hapiness + " || Precio: " + price + "\n";
        for (int i = 0; i < availableOfferWeight.size(); ++i) {
            s += "Oferta número " + i + " con peso " + availableOfferWeight.get(i) + "/" + offers.get(i).getPesomax() + ", con dias de entrega " + offers.get(i).getDias() +  " y con price: " + offers.get(i).getPrecio() + ":\n";
            ArrayList<Paquete> selectedPackages = selectedServices.get(i);
            for (int p = 0; p < selectedPackages.size(); ++p) {
                s += "\tIndice: " + p + " " +  selectedPackages.get(p) + "\n";
            }
        }
        return s;
    }
    
}