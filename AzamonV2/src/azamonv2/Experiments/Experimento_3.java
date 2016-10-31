package azamonv2.Experiments;

import azamonv2.*;
import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Experimento_3 {
    
    static final int semilla = 1234; 
    static int numeroPaquetes = 100;
    static double proporcion = 1.2;
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis();
        
        String ruta = "Experimento_3.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        
        bw.write("EXPERIMENTO 3: Determinar los parámetros que dan mejor resultado para el Simulated Annealing.\n\n\n");
        
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        Estado.setPackages(paquetes);
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        Estado.setOffers(transporte);
        Estado estadoInicial = new Estado();
        
        GeneradorSimulatedAnneiling generadorSA = new GeneradorSimulatedAnneiling(semilla);
        
        Problem SimulatedAnneilingCost =            
            new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionCost());
        
        SimulatedAnnealingSearch simulatedAnnealingSearch = new SimulatedAnnealingSearch();
        
        bw.write("ESTADO INICIAL\n"
                + "Número de ofertas de transporte: " +estadoInicial.getSortedOffers().size()+ 
                " || Felicidad: " +estadoInicial.getHappiness()+ 
                " || Precio: " +estadoInicial.getPrice()+ "\n");

        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing () ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        
        int     A = 1000;
        int     B = 10;
        int     C = 1;
        double  D = 0.001;
        simulatedAnnealingSearch = new SimulatedAnnealingSearch(A, B, C, D);

        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing (Iteraciones: "+A/1000+"K | Iteraciones por paso: "+B+" | Parámetros k, λ: "+C+" y "+D+") ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
 
        A = 10000;
        B = 100;
        C = 1;
        D = 0.001;
        simulatedAnnealingSearch = new SimulatedAnnealingSearch(A, B, C, D);
        
        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing (Iteraciones: "+A/1000+"K | Iteraciones por paso: "+B+" | Parámetros k, λ: "+C+" y "+D+") ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        
        A = 100000;
        B = 1000;
        C = 1;
        D = 0.01;
        simulatedAnnealingSearch = new SimulatedAnnealingSearch(A, B, C, D);
        
        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing (Iteraciones: "+A/1000+"K | Iteraciones por paso: "+B+" | Parámetros k, λ: "+C+" y "+D+") ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        
        A = 5000;
        B = 10;
        C = 5;
        D = 0.001;        
        simulatedAnnealingSearch = new SimulatedAnnealingSearch(A, B, C, D);

        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing (Iteraciones: "+A/1000+"K | Iteraciones por paso: "+B+" | Parámetros k, λ: "+C+" y "+D+") ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        
        A = 100000;
        B = 100;
        C = 5;
        D = 0.01;
        simulatedAnnealingSearch = new SimulatedAnnealingSearch(A, B, C, D);

        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            long startTime = System.currentTimeMillis();
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing (Iteraciones: "+A/1000+"K | Iteraciones por paso: "+B+" | Parámetros k, λ: "+C+" y "+D+") ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
                
        bw.write("\n\nRESULTADO: Hasta cierto momento, al augmentar el nº de iteraciones el resultado va mejorando, pero a la vez que augmentamos las iteraciones por paso no profundizamos tanto en el espacio de soluciones en altura En las tiradas con muchas iteraciones pero pocas iteraciones por pasos al no profundizar en lo ancho, la calidad de la solución disminuye. Los parámetros de la función de temperatura los fijaremos en 10 y 0.001\n");
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram)/1000.0 + " seconds");
    }
}

