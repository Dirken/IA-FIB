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

public class Experimento_2 {
    
    static final int semilla = 1234; 
    static int numeroPaquetes = 100;
    static double proporcion = 1.2;
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis();
        
        String ruta = "Experimento_2.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        
        bw.write("EXPERIMENTO 2: Determinar qué estrategia de generación de la solución inicial da mejores resultados.\n\n\n");
        
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
            System.out.println(".........finished Hill Climbing ("+ (endTime - startTime) + " miliseconds)");
            bw.write("Hill Climbing ("+ (endTime - startTime) + " miliseconds)\n"
                + "Número de ofertas de transporte: " +estadoFinal.getSortedOffers().size()+ 
                " || Felicidad: " +estadoFinal.getHappiness()+ 
                " || Precio: " +estadoFinal.getPrice()+ "\n");
            EstadoHillClimbingCost = "RESULTADO HILL CLIMBING COSTE\n" + estadoFinal.toString();
        } catch(Exception e){
            System.err.println(".........Hill climbing finished with errors.");
        }
        
        bw.write("\n\nRESULTADO: Esto no se contestarlo. ¿Por que la estrategia de generación de la solución inicial ha sido la que tenemos actualmente? ¿Es muy rápida y sencilla? ¿Nos da ventajas respecto a otras a la hora de empezar a calcular? ¿Una convinación de las dos? o por el contrario, ¿Otras nos penalizarían más por tiempo o a la hora de colocar el problema de cierta manera?.\n");
        
        bw.write("\n\n"+ EstadoHillClimbingCost);
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram) + " miliseconds");
    }
}

