package azamonv2;

import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    
    static final int semilla = 1234; 
    static int numeroPaquetes = 100;
    static double proporcion = 1.2;
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis();
        
        String ruta = "result.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        String  EstadoSimulatedAnneilingCost = null, 
                EstadoSimulatedAnneilingHappiness = null, 
                EstadoSimulatedAnneilingCostHappiness = null,
                EstadoHillClimbingCost = null, 
                EstadoHillClimbingHappiness = null, 
                EstadoHillClimbingCostHappiness = null;
        
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        Estado.setPackages(paquetes);
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        Estado.setOffers(transporte);
        Estado estadoInicial = new Estado();
        
        GeneradorHillClimbing generadorHC = new GeneradorHillClimbing();
        GeneradorSimulatedAnneiling generadorSA = new GeneradorSimulatedAnneiling(semilla);
        
        Problem SimulatedAnneilingCost =            
            new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionCost());
        Problem SimulatedAnneilingHappiness =       
            new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionHappiness());
        Problem SimulatedAnneilingCostHappiness =   
            new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionCostHappiness());
        
        Problem HillClimbingCost = 
            new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCost());
        Problem HillClimbingHappiness =
            new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionHappiness());
        Problem HillClimbingCostHappiness = 
            new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCostHappiness());
        
        HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();
        SimulatedAnnealingSearch simulatedAnnealingSearch = new SimulatedAnnealingSearch();
        //SimulatedAnnealingSearch simulatedAnnealingSearch = new SimulatedAnnealingSearch(4000, 20, 5, 0.001); //Semilla?
        
        bw.write("ESTADO INICIAL\n"
                + "Número de ofertas de transporte: " +estadoInicial.getSortedOffers().size()+ 
                " || Felicidad: " +estadoInicial.getHappiness()+ 
                " || Precio: " +estadoInicial.getPrice()+ "\n");
        
        //###########################HEURISTICO COSTE###########################
        System.out.println("HEURISTICO COSTE:");
        bw.write("\nHEURISTICO COSTE\n");
        System.out.print("Starting Simulated Annealing");
        try{
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCost, simulatedAnnealingSearch);
            long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoSimulatedAnneilingCost = "RESULTADO SIMULATED ANNEALING COSTE\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        System.out.print("Starting Hill Climbing");
        try{
            SearchAgent agent = new SearchAgent(HillClimbingCost, hillClimbingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println(".........finished Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ★Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoHillClimbingCost = "RESULTADO HILL CLIMBING COSTE\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println(".........Hill climbing finished with errors.");
        }
        
        //#########################HEURISTICO FELICIDAD#########################
        System.out.println("\nHEURISTICO FELICIDAD:");
        bw.write("\nHEURISTICO FELICIDAD\n");
        System.out.print("Starting Simulated Annealing");
        try{
            SearchAgent agent = new SearchAgent(SimulatedAnneilingHappiness, simulatedAnnealingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoSimulatedAnneilingHappiness = "RESULTADO SIMULATED ANNEALING FELICIDAD\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        System.out.print("Starting Hill Climbing");
        try{
            SearchAgent agent = new SearchAgent(HillClimbingHappiness, hillClimbingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println(".........finished Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ★Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoHillClimbingHappiness = "RESULTADO HILL CLIMBING FELICIDAD\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println(".........Hill climbing finished with errors.");
        }
        
        //#############################HEURISTICO MIXTO#########################
        System.out.println("\nHEURISTICO COSTE-FELICIDAD:");
        bw.write("\nHEURISTICO COSTE-FELICIDAD\n");
        System.out.print("Starting Simulated Annealing");
        try{
            SearchAgent agent = new SearchAgent(SimulatedAnneilingCostHappiness, simulatedAnnealingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println("...finished Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ☆Simulated Annealing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoSimulatedAnneilingCostHappiness = "RESULTADO SIMULATED ANNEALING COSTE-FELICIDAD\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println("...Simulated Annealing finished with errors.");
        }
        System.out.print("Starting Hill Climbing");
        try{
            SearchAgent agent = new SearchAgent(HillClimbingCostHappiness, hillClimbingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println(".........finished Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write(" ★Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoHillClimbingCostHappiness = "RESULTADO HILL CLIMBING COST-FELICIDAD\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println(".........Hill climbing finished with errors.");
        }
        
        bw.write("\n\n"+ EstadoSimulatedAnneilingCost 
                +"\n\n"+ EstadoHillClimbingCost  
                +"\n\n"+ EstadoSimulatedAnneilingHappiness   
                +"\n\n"+ EstadoHillClimbingHappiness
                +"\n\n"+ EstadoSimulatedAnneilingCostHappiness 
                +"\n\n"+ EstadoHillClimbingCostHappiness);
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram)/1000.0 + " seconds");
    }
}

