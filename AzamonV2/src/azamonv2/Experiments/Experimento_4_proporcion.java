package azamonv2.Experiments;

import azamonv2.*;
import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Experimento_4_proporcion {
    
    static final int semilla = 1234; 
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis(); 
        
        String ruta = "Experimento_4_proporcion.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        
        bw.write("EXPERIMENTO 4: Estudiar como evoluciona el tiempo de ejecución para hallar la solución en función la proporción de peso transportable.\n\n");
        
        int numeroPaquetes = 100;
        double proporcion = 1.2;
        
        bw.write("\nHEURISTICO COSTE\n");
        
        for (int i = 0; i<10; i++){
        
            Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
            Estado.setPackages(paquetes);
            Transporte transporte = new Transporte(paquetes, proporcion , semilla);
            Estado.setOffers(transporte);
            Estado estadoInicial = new Estado();

            GeneradorHillClimbing generadorHC = new GeneradorHillClimbing();

            Problem HillClimbingCost = 
            new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCost());

            HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();
        
            System.out.print("Starting Hill Climbing");
            try{
                long startTime = System.currentTimeMillis();
                SearchAgent agent = new SearchAgent(HillClimbingCost, hillClimbingSearch);
                Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
                final long endTime = System.currentTimeMillis();
                System.out.println(".........finished Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos)");
                bw.write(" ★Hill Climbing ("+ (endTime - startTime)/1000.0 + " segundos, paquetes: "+ numeroPaquetes +", proporción: "+ proporcion +")\n");
            } catch(Exception e){
                System.err.println(".........Hill climbing finished with errors.");
            }

            numeroPaquetes = 100;
            proporcion = Math.round((proporcion + 0.2) * 100.0) / 100.0;;
            
        
        }
        
        bw.write("\n\nRESULTADO: La proporción no augmenta ni disminuye el tiempo puesto que al iterar se hace sobre el volumen de ofertas y paquetes.\n");
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram)/1000.0 + " seconds");
    }
}

