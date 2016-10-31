package azamonv2.Experiments;

import azamonv2.*;
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

public class Experimento_1 {
    
    static final int semilla = 1234; 
    static int numeroPaquetes = 100;
    static double proporcion = 1.2;
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis();
        
        String ruta = "Experimento_1.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        
        bw.write("EXPERIMENTO 1: Determinar qué conjunto de operadores da mejores resultados.\n\n\n");
        
        String  EstadoHillClimbingCost = null;
        
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        Estado.setPackages(paquetes);
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        Estado.setOffers(transporte);
        Estado estadoInicial = new Estado();
        
        GeneradorHillClimbing generadorHC = new GeneradorHillClimbing();
        
        Problem HillClimbingCost = 
            new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCost());
        
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
        System.out.print("Starting Hill Climbing");
        try{
            SearchAgent agent = new SearchAgent(HillClimbingCost, hillClimbingSearch);
            final long startTime = System.currentTimeMillis();
            Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            final long endTime = System.currentTimeMillis();
            System.out.println(".........finished Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)");
            bw.write("Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoHillClimbingCost = "RESULTADO HILL CLIMBING COSTE\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println(".........Hill climbing finished with errors.");
        }        
          
        bw.write("\n\nRESULTADO: Necesitariamos tener implementados mas operadores, aunque podemos decir perfectamente que nos quedamos move como operador simple y swicht ya qu enos permite mover paquetes de ofertas que ya tienen cupo de peso o que directamente no cabrían con un move normal.\n");
        
        bw.write("\n\n"+ EstadoHillClimbingCost);
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram)/1000.0 + " seconds");
    }
}

