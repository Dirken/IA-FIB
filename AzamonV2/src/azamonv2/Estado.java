/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamonv2;

import IA.Azamon.*;
import java.util.*;
import azamonv2.SortedClasses.*;
/**
 *
 * @author Eironeia
 */
import java.util.Scanner;

public class Estado implements Cloneable {

    
    
    public double price;
    public int happiness;

    
    
    private static Transporte offers;
    private static Paquetes packages;
    
    public  ArrayList<ArrayList<Integer>> selectedServices;// resultat
    //public  ArrayList<Double> offersPrices;
    public  ArrayList<Double> availableWeight;
    private static ArrayList<PaqueteOrdenado> sortedPackages;
    private static ArrayList<OfertaOrdenada> sortedOffers;
    
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getHappiness() {
        return happiness;
    }
    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }
    public ArrayList<ArrayList<Integer>> getSelectedServices() {
        return selectedServices;
    }
    public void setSelectedServices(ArrayList<ArrayList<Integer>> selectedServices) {
        this.selectedServices = selectedServices;
    }
    public static void setOffers(Transporte offers) {
        Estado.offers = offers;
    }
    public static void setPackages(Paquetes packages) {
        Estado.packages = packages;
    }
    public ArrayList<Integer> getSortedPackages(int offerIndex) {
        return this.selectedServices.get(offerIndex);
    }
    public int sSpackagesSize(int offerIndex) {
        return this.selectedServices.get(offerIndex).size();
    }
    public static ArrayList<PaqueteOrdenado> getSortedPackages() {
        return sortedPackages;
    }
    public static ArrayList<OfertaOrdenada> getSortedOffers() {
        return sortedOffers;
    }
//    public ArrayList<Double> getOffersPrices() {
//        return offersPrices;
//    }

    public ArrayList<Double> getAvailableWeight() {
        
        ArrayList<Double> newAvailableWeight = new ArrayList<> ();
        for (int offerIndex = 0; offerIndex < selectedServices.size(); ++offerIndex) {
            int savedPackagesSize = this.sSpackagesSize(offerIndex);
            if (savedPackagesSize == 0) newAvailableWeight.add(offerIndex, 0.0);
            else {
                double currentWeight = 0.0;
                for (int packageIndex = 0; packageIndex < savedPackagesSize; ++packageIndex) {
                    currentWeight += getWeight(packageIndex);
                }
                newAvailableWeight.add(offerIndex, currentWeight);
            }
        }
        return newAvailableWeight;
    }
    public void setAvailableWeight(ArrayList<Double> availableWeight) {
        this.availableWeight = availableWeight;
    }
    

    
    public Estado() {
        this.happiness = 0;
        this.price = 0;
        availableWeight = new ArrayList<>();
        sortedPackages = new ArrayList<>();
        sortedOffers = new ArrayList<>();
        selectedServices = new ArrayList<>(offers.size());

        fillArrays(); // O(max(n,m))
        
        boolean happiness = true; //Per cambiar el tipus de estado inicial prioritat
        
        if (happiness) {
            sortPackagesHappiness();
            if (!canGetAHappinessSolution()) System.out.println("Not a possible solution");
        }
        else {
            sortPackagesCost();
            if (!canGetACostSolution()) System.out.println("Not a possible solution");
        }   
    }

    public Estado(double price, int happiness, ArrayList<ArrayList<Integer>> selectedServices, ArrayList<Double> availableWeight) {
        this.price = price;
        this.happiness = happiness;
        this.selectedServices = selectedServices;
        this.availableWeight = availableWeight;
    }
    
    

    private void fillArrays() {
        
        int packageIndex = 0;
        int offerIndex = 0;
        
        while(packageIndex < packages.size() && offerIndex < offers.size()){
            PaqueteOrdenado sortedPackage = new PaqueteOrdenado(packages.get(packageIndex));
            sortedPackages.add(packageIndex, sortedPackage);
            
            OfertaOrdenada sortedOffer = new OfertaOrdenada(offers.get(offerIndex));
            sortedOffers.add(offerIndex, sortedOffer);
            
            availableWeight.add(offerIndex, 0.0);
  
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
            
            availableWeight.add(offerIndex, 0.0);
            
            offerIndex += 1;
        }
    }
    private void fillResult() {
        for (int offerIndex = 0; offerIndex < sortedOffers.size(); ++offerIndex) {
            ArrayList<Integer> selectedPackages = new ArrayList<>();
            selectedServices.add(offerIndex, selectedPackages);
        }
    }
    
    private void sortPackagesHappiness() {
        sortedPackages.sort((sortedPackage1, sortedPackage2) -> {
            return Integer.compare(sortedPackage1.getPaquete().getPrioridad()
                                 , sortedPackage2.getPaquete().getPrioridad());
        });
    }
    private void sortPackagesCost() {
        sortedPackages.sort((sortedPackage1, sortedPackage2) -> {
            return Double.compare(sortedPackage1.getPaquete().getPeso()
                                 , sortedPackage2.getPaquete().getPeso());
        });
    }
   
    private double getWeight(int packageIndex) {
       return this.sortedPackages.get(packageIndex).getPaquete().getPeso();
   }
    private double getMaxWeight(int offerIndex) {
        return this.sortedOffers.get(offerIndex).getOferta().getPesomax();
    }
    private double currentWeight(int offerIndex) {
        return this.availableWeight.get(offerIndex);
    }
    public int getPackage(int offerIndex, int position) {
        return this.selectedServices.get(offerIndex).get(position);
    }
    
    private int getPackagePriority(int packageIndex) {
        return this.sortedPackages.get(packageIndex).getPaquete().getPrioridad();
    }
    private int getOfferPriority(int offerIndex) {
        return this.sortedOffers.get(offerIndex).getOferta().getDias();
    }
    private double getOfferPrice(int offerIndex) {
        return this.sortedOffers.get(offerIndex).getOferta().getPrecio();
    }
    
    private void updateWeight(int packageIndex, int offerIndex) {
        double newWeight = currentWeight(offerIndex) + getWeight(packageIndex);
        this.availableWeight.set(offerIndex, newWeight);
    }
    private void updateHappiness(int packageIndex, int offerIndex) {
        int packagePriority = getPackagePriority(packageIndex);
        int offerPriority = getOfferPriority(offerIndex);
        this.happiness += happinessGains(packagePriority, offerPriority);
    }
    private int happinessGains(int packagePriority, int offerPriority) {
        int happinessGains = 0;
        if (packagePriority == Paquete.PR2) happinessGains = 3-offerPriority;
        else if (packagePriority == Paquete.PR3) happinessGains = 5-offerPriority;
        if (happinessGains < 0) return 0;
        return happinessGains;
    }
    private double priceGains(int packageIndex, int offerIndex) {
        double packagePrice = 0;
        switch (getOfferPriority(offerIndex)) {
            case 3:
                packagePrice += 0.25*getWeight(packageIndex);
                break;
            case 4:
                packagePrice += 0.25*getWeight(packageIndex);
                break;
            case 5:
                packagePrice += 0.5*getWeight(packageIndex);
                break;
            default:
        }
        packagePrice += getOfferPrice(offerIndex)*getWeight(packageIndex);
        return packagePrice;
    }
    public void updateTotalPrice() {
        this.price = 0;
        for (int offerIndex = 0; offerIndex < this.selectedServices.size(); ++offerIndex) {
            for (int position = 0; position < this.sSpackagesSize(offerIndex); ++position) {
                int packageIndex = this.getPackage(offerIndex, position);
                this.price += priceGains(packageIndex, offerIndex);
            }
        }
    }
    public void updateTotalHappiness() {
        this.happiness = 0;
        for (int offerIndex = 0; offerIndex < this.selectedServices.size(); ++offerIndex) {
            for (int position = 0; position < this.sSpackagesSize(offerIndex); ++ position) {
                int packageIndex = getPackage(offerIndex, position);
                int packagePriority = getPackagePriority(packageIndex);
                int offerPriority = getOfferPriority(offerIndex);
                this.happiness += happinessGains(packagePriority, offerPriority);
            }
        }
    }
    public void updateTotalWeight() {
        for (int offerIndex = 0; offerIndex < this.selectedServices.size(); ++offerIndex) {
            double weight = 0.0;
            for (int position = 0; position < this.sSpackagesSize(offerIndex); ++position) {
                int packageIndex = this.getPackage(offerIndex, position);
                weight += getWeight(packageIndex);
            }
            this.availableWeight.set(offerIndex, weight);
        }
    }

    
    private boolean canGetAHappinessSolution() {
        ArrayList<Boolean> packagesSaved = new ArrayList<>(Collections.nCopies(sortedPackages.size(),false));
        
        for(int offerIndex = 0 ; offerIndex < sortedOffers.size(); ++offerIndex) {
            ArrayList<Integer> selectedPackages = new ArrayList<>();
            for (int packageIndex = 0; packageIndex < sortedPackages.size(); ++packageIndex) {

                if (isWeightAvailable(packageIndex, offerIndex)
                    && isValidPriority(packageIndex, offerIndex)
                    && !packagesSaved.get(packageIndex)) {
                    
                    updateWeight(packageIndex,offerIndex);
                    packagesSaved.set(packageIndex, true);
                    updateHappiness(packageIndex, offerIndex); 
                    double priceGains = priceGains(packageIndex, offerIndex); this.price += priceGains;
                    selectedPackages.add(packageIndex);
                }
            }
            selectedServices.add(offerIndex, selectedPackages);
        }
            
        return allPackageSaved(packagesSaved);
    }
    private boolean canGetACostSolution() {
        ArrayList<Boolean> packagesSaved = new ArrayList<>(Collections.nCopies(sortedPackages.size(),false));
        fillResult();
        for(int offerIndex = sortedOffers.size()-1 ; offerIndex >= 0 ; --offerIndex) {
            ArrayList<Integer> selectedPackages = new ArrayList<>();
            for (int packageIndex = 0; packageIndex < sortedPackages.size(); ++packageIndex) {
                
                if (isWeightAvailable(packageIndex, offerIndex)
                    && isValidPriority(packageIndex, offerIndex)
                    && !packagesSaved.get(packageIndex)) {
                    
                    updateWeight(packageIndex,offerIndex);
                    packagesSaved.set(packageIndex, true);
                    updateHappiness(packageIndex, offerIndex); 
                    double priceGains = priceGains(packageIndex, offerIndex); this.price += priceGains;
                    selectedPackages.add(packageIndex);
                }
            }
            selectedServices.set(offerIndex, selectedPackages);
        }            
        return allPackageSaved(packagesSaved);
    }
    public boolean allPackageSaved(ArrayList<Boolean> packagesSaved) {
        for(int offerIndex = 0; offerIndex < packagesSaved.size(); ++offerIndex){
            if(!packagesSaved.get(offerIndex)) return false;
        }
        return true;
    }
    
    private boolean isWeightAvailable(int packageIndex, int offerIndex) {
        double maxWeight = getMaxWeight(offerIndex);
        double currentWeight = currentWeight(offerIndex);
        double packageWeight = getWeight(packageIndex);
        return (currentWeight+packageWeight) <= maxWeight;
    }
    private boolean isValidPriority(int packageIndex, int offerIndex) {
        int offerPriority = getOfferPriority(offerIndex);
        int packagePriority = getPackagePriority(packageIndex);
        switch (packagePriority) {
            case Paquete.PR1:
                return offerPriority == 1;
            case Paquete.PR2:
                return offerPriority <= 3;
            case Paquete.PR3:
                return offerPriority <= 5;
            default:
                System.out.println("Incorrecct priority");
        }
        return false;
    }
    
    /** Funcions Succesores **/
    
    public boolean validMovement(int packageIndex, int offerIndex) {
        boolean packageFits = isWeightAvailable(packageIndex, offerIndex);
        boolean validPriority = isValidPriority(packageIndex, offerIndex);
        
        return packageFits && validPriority;
    }
    public boolean validSwap(int packageIndex1, int offerIndex1, int packageIndex2, int offerIndex2) {
        
        double currentWeight1 = currentWeight(offerIndex1); double currentWeight2 = currentWeight(offerIndex2);
        double packageWeight1 = getWeight(packageIndex1);   double packageWeight2 = getWeight(packageIndex2);
        double maxWeight1 = getMaxWeight(offerIndex1);      double maxWeight2 = getMaxWeight(offerIndex2);
        
        
        boolean packageFits1 = (currentWeight1 - packageWeight1 + packageWeight2) <= maxWeight1;
        boolean packageFits2 = (currentWeight2 - packageWeight2 + packageWeight1) <= maxWeight2;
        
        boolean validPriority1 = isValidPriority(packageIndex1, offerIndex2); 
        boolean validPriority2 = isValidPriority(packageIndex2, offerIndex1);
        
        
        //System.out.println(packageFits1 + ""+packageFits2 + validPriority1 + validPriority2 + "");
        
        return validPriority1 && validPriority2 & packageFits1 & packageFits2;
    }
    
    public void movePackage(int packageIndex1, int offerIndex1, int offerIndex2, int position) {

        removeSelectedServices(offerIndex1, position);
        updateSelectedServices(packageIndex1, offerIndex2);
        
        updateTotalWeight();
        updateTotalPrice();  //total price
        updateTotalHappiness(); //total happiness
    }
    
    public void moveBackPackage(int packageIndex1, int offerIndex1, int offerIndex2, int position1) {
        int position2 = this.selectedServices.get(offerIndex2).size() - 1;
        
        removeSelectedServices(offerIndex2, position2);

        ArrayList<Integer> newSortedPackages = getSortedPackages(offerIndex1);
        newSortedPackages.add(position1, packageIndex1);
        this.selectedServices.set(offerIndex1, newSortedPackages);
        
        updateTotalWeight();
        updateTotalPrice();  //total price
        updateTotalHappiness(); //total happiness

        
        
    }
    
    public void swapPackage(int packageIndex1, int offerIndex1, int packageIndex2, int offerIndex2, int position1, int position2) {
        movePackage(packageIndex1, offerIndex1, offerIndex2, position1);
        movePackage(packageIndex2, offerIndex2, offerIndex1, position2);
    }
    public void swapBackPackage(int packageIndex1, int offerIndex1, int packageIndex2, int offerIndex2, int position1, int position2) {
        moveBackPackage(packageIndex1, offerIndex1, offerIndex2, position1);
        moveBackPackage(packageIndex2, offerIndex2, offerIndex1, position2);
    
    }
    
    private void updateSelectedServices(int packageIndex, int offerIndex) {
        ArrayList<Integer> sortedPackages = getSortedPackages(offerIndex);
        sortedPackages.add(packageIndex);
        this.selectedServices.set(offerIndex, sortedPackages);
    }
    private void removeSelectedServices(int offerIndex, int position) {
        
        ArrayList<Integer> savedPackages = this.selectedServices.get(offerIndex);
        savedPackages.remove(position);
        this.selectedServices.set(offerIndex, savedPackages);
    }
    
    
    private void print(String s) {
        System.out.println(s);
    }
    
    @Override
    public String toString() {
        
        String s = "";
        s += "#Ofertas Totales: " + offers.size() + " || Felicidad total: " + happiness + " || Precio Total: " + price + "\n";
        for (int i = 0; i < availableWeight.size(); ++i) {
            s += "#Oferta " + i + " con peso " + availableWeight.get(i) + "/" + getMaxWeight(i) + ", con dias de entrega " + getOfferPriority(i) +  " y con precio: " + getOfferPrice(i) + ":\n";
            ArrayList<Integer> selectedPackages = selectedServices.get(i);
            for (int p = 0; p < selectedPackages.size(); ++p) {
                int packageIndex = selectedPackages.get(p);
                s += "\t" + p + " " + sortedPackages.get(packageIndex).getPaquete()  + "\n";
            }
        }
        return s;
    }
    
    
    
}
