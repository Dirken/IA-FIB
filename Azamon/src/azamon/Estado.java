package azamon;

import IA.Azamon.*;
import java.util.*; 

public class Estado {

    private double price;
    private int happiness;
    
    private static Transporte offers;
    private static Paquetes packages;
    
    private  static ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private  ArrayList<Double> availableOfferWeight;
    private  static ArrayList<PaqueteOrdenado> sortedPackages;
    private  static ArrayList<OfertaOrdenada> sortedOffers;
    
    public Estado() {
        this.happiness = 0;
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

    public Estado(double price, int happiness, ArrayList<ArrayList<Paquete>> selectedServices, ArrayList<Double> availableOfferWeight, ArrayList<PaqueteOrdenado> sortedPackages, ArrayList<OfertaOrdenada> sortedOffers) {
        this.price = price;
        this.happiness = happiness;
        this.selectedServices = selectedServices;
        this.availableOfferWeight = availableOfferWeight;
        this.sortedPackages = sortedPackages;
        this.sortedOffers = sortedOffers;
    }

    
    public static int getIndexPackage(Paquete p){
        for (int i = 0; i < selectedServices.size(); ++i){
            if (selectedServices.get(i).contains(p)) return i;
        }
        System.out.println("JOHN CENA");
        return 999;
    }
    
    

    public double getPrice() {
        return price;
    }

    public int getHappiness() {
        return happiness;
    }

    

    public static Transporte getOffers() {
        return offers;
    }

    public static Paquetes getPackages() {
        return packages;
    }

    public ArrayList<ArrayList<Paquete>> getSelectedServices() {
        return selectedServices;
    }

    public ArrayList<Double> getAvailableOfferWeight() {
        return availableOfferWeight;
    }

    public static ArrayList<PaqueteOrdenado> getSortedPackages() {
        return sortedPackages;
    }

    public static ArrayList<OfertaOrdenada> getSortedOffers() {
        return sortedOffers;
    }

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

    private void sortPackages() {
        sortedPackages.sort((sortedPackage1, sortedPackage2) -> {
            return Integer.compare(sortedPackage1.getPaquete().getPrioridad()
                                 , sortedPackage2.getPaquete().getPrioridad());
        });
    }
    
    private void sortOffers() {
        sortedOffers.sort((sortedOffer1, sortedOffer2) -> {
            return Integer.compare(sortedOffer1.getOferta().getDias()
                                 , sortedOffer2.getOferta().getDias());
        });
    }
    

    private void printPaqueteOrdenadoPriority() {
        for (int i = 0; i < sortedPackages.size(); ++i) {
            System.out.println(sortedPackages.get(i).getPaquete().getPrioridad());
        }
        
    }

    public static void setOfertas(Transporte offers) {
        Estado.offers = offers;
    }

    public static void setPaquetes(Paquetes packages) {
        Estado.packages = packages;
    }
  
    public boolean canGetASolution() {
        ArrayList<Boolean> packagesSaved = new ArrayList<>(Collections.nCopies(sortedPackages.size(),false));
        
        for(int offerIndex = 0 ; offerIndex < sortedOffers.size(); ++offerIndex) {
            ArrayList<Paquete> selectedPackages = new ArrayList<>();
            
            for (int packageIndex = 0; packageIndex < sortedPackages.size(); ++packageIndex) {
                
                Oferta oferta = sortedOffers.get(offerIndex).getOferta();
                Paquete paquete = sortedPackages.get(packageIndex).getPaquete();
                
                double currentCapacity = currentWeightFromAvailableOfferWeight(offerIndex);
                double maxCapacity = oferta.getPesomax();
                double packageWeight = paquete.getPeso();
                
                int offerPriority = oferta.getDias();
                int deliveryDay = paquete.getPrioridad();
                if (availableCapacityToAdd(currentCapacity, maxCapacity, packageWeight)
                    && isValidPriority(offerPriority, deliveryDay)
                    && !packagesSaved.get(packageIndex)) {
                    //Afegir i update de packagesSaved
                    
                    updateWeightFromAvailableOfferWeight(offerIndex,packageWeight);
                    packagesSaved.set(packageIndex, true);
                    updateTotalHappiness(happiness(oferta, paquete));
                    updateTotalPrice(cost(oferta, paquete));
                    selectedPackages.add(paquete);
                }
            }
            selectedServices.add(offerIndex, selectedPackages);
        }
            
        return allPackageSaved(packagesSaved);
    }
    
    public boolean allPackageSaved(ArrayList<Boolean> packagesSaved) {
        for(int offerIndex = 0; offerIndex < packagesSaved.size(); ++offerIndex){
            if(!packagesSaved.get(offerIndex)) return false;
        }
        return true;
    }
    
    
    public boolean availableCapacityToAdd(double currentCapacity, double maxCapacity, double packageWeight) {
        return currentCapacity+packageWeight <= maxCapacity;
    }
    
    public boolean isValidPriority(int priority, int deliveryDay) {
        switch (deliveryDay) {
            case Paquete.PR1:
                return priority == 1;
            case Paquete.PR2:
                return priority <= 3;
            case Paquete.PR3:
                return priority <= 5;
            default:
                System.out.println("Incorrecct priority");
        }
        return false;
    }
    
    public static double cost(Oferta oferta, Paquete paquete) {
        double costeTotal = oferta.getPrecio()*paquete.getPeso();
        switch (oferta.getDias()) {
            case 3:
                costeTotal += 0.25*paquete.getPeso();
                break;
            case 4:
                costeTotal += 0.25*paquete.getPeso();
                break;
            case 5:
                costeTotal += 0.5*paquete.getPeso();
                break;
            default:
        }
        costeTotal += oferta.getPrecio()*paquete.getPeso();
        return costeTotal;
    }
    
    public static int happiness(Oferta oferta, Paquete paquete) {
        switch (paquete.getPrioridad()) {
            case Paquete.PR2:
                return 3-oferta.getDias();
            case Paquete.PR3:
                return 5-oferta.getDias();
            default:
                return 0;
        }
    }            
    
    public Oferta getOfferFromSelectedServices(int offerIndex) {
        return sortedOffers.get(offerIndex).getOferta();
    }

    public Paquete getPackageFromSelectedServices(int offerIndex, int packageIndex) {
        return selectedServices.get(offerIndex).get(packageIndex);
    }
    
    public int getPackagesSizeFromSelectedServices(int offerIndex) {
        return selectedServices.get(offerIndex).size();
    }
    
    public void updateWeightFromAvailableOfferWeight(int offerIndex, double packageWeight) {
        availableOfferWeight.set(offerIndex, currentWeightFromAvailableOfferWeight(offerIndex)+packageWeight);
    }
    
    public double currentWeightFromAvailableOfferWeight(int offerIndex) {
        return availableOfferWeight.get(offerIndex);
    }
    
    public void updateTotalPrice(double price) {
        this.price += price;
    }
    
    public void updateTotalHappiness(int happiness) {
        this.happiness += happiness;
    }

    @Override
    public String toString() {
        String s = "";
        s += "#Ofertas Totales: " + offers.size() + " || Felicidad total: " + happiness + " || Precio Total: " + price + "\n";
        for (int i = 0; i < availableOfferWeight.size(); ++i) {
            s += "#Oferta " + i + " con peso " + availableOfferWeight.get(i) + "/" + offers.get(i).getPesomax() + ", con dias de entrega " + offers.get(i).getDias() +  " y con precio: " + offers.get(i).getPrecio() + ":\n";
            ArrayList<Paquete> selectedPackages = selectedServices.get(i);
            for (int p = 0; p < selectedPackages.size(); ++p) {
                s += "\t" + p + " " +  selectedPackages.get(p) + "\n";
            }
        }
        return s;
    }
    
    
    
}
