package azamon;

import IA.Azamon.*;
import java.util.*; 

public class Estado {

    private static double price;
    private static int happiness;
    
    private static Transporte offers;
    private static Paquetes packages;
    
    private static ArrayList<ArrayList<Paquete>> selectedServices; // resultat
    private static ArrayList<Double> availableOfferWeight;
    private static ArrayList<PaqueteOrdenado> sortedPackages;
    private static ArrayList<OfertaOrdenada> sortedOffers;
    
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

    public static double getPrice() {
        return price;
    }

    public static int getHappiness() {
        return happiness;
    }

    public static Transporte getOffers() {
        return offers;
    }

    public static Paquetes getPackages() {
        return packages;
    }

    public static ArrayList<ArrayList<Paquete>> getSelectedServices() {
        return selectedServices;
    }

    public static ArrayList<Double> getAvailableOfferWeight() {
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
    
    public static double cost(Oferta oferta, Paquete paquete) {
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
        s += "Número de ofertas de transporte: " + offers.size() + " || Felicidad: " + happiness + " || Precio: " + price + "\n";
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
