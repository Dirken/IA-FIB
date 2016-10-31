package azamonv2.Experiments;

import azamonv2.*;
import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;
import azamonv2.Main;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Experimento_6 {
    
    static final int semilla = 1234; 
    static int numeroPaquetes = 100;
    static double proporcion = 1.2;
    
    public static void main(String[] argv) throws IOException {
        
        long startTimeProgram = System.currentTimeMillis();
        
        String ruta = "Experimento_6.txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        
        bw.write("EXPERIMENTO 6: Estimad como varían los costes de transporte y almacenamiento y el tiempo de ejecución para hallar la solución cambiando la ponderación que se da a la felicidad en la función heurística.\n\n");
        
        Main.ponderacion = 0;
        
        bw.write("\nHEURISTICO COSTE+FELICIDAD\n");
        
        for (int i = 0; i<10; ++i){
            Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
            Estado.setPackages(paquetes);
            Transporte transporte = new Transporte(paquetes, proporcion , semilla);
            Estado.setOffers(transporte);
            Estado estadoInicial = new Estado();

            GeneradorHillClimbing generadorHC = new GeneradorHillClimbing();

            Problem HillClimbingCostHappiness = 
                new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCostHappiness());

            HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();

            System.out.print("Starting Hill Climbing");
            try{
                long startTime = System.currentTimeMillis();
                SearchAgent agent = new SearchAgent(HillClimbingCostHappiness, hillClimbingSearch);
                Estado estadoFinal = (Estado)hillClimbingSearch.getGoalState();
                long endTime = System.currentTimeMillis();
                System.out.println(".........finished Hill Climbing ("+ (endTime - startTime) + " miliseconds)");
                bw.write("Hill Climbing"+
                    "\t||\t"+ (endTime - startTime) + " miliseconds"+
                    "\t||\tPonderación: " +Main.ponderacion+ 
                    "\t||\tFelicidad: " +estadoFinal.getHappiness()+ 
                    "\t||\tPrecio: " +estadoFinal.getPrice()+ "\n");
            } catch(Exception e){
                System.err.println(".........Hill climbing finished with errors.");
            }
            Main.ponderacion += 2;
        }
        
        bw.write("\n\nRESULTADO: Con una pobnderación de 0, la heurística se comporta como la heurística por precio, ya que el valor de este no se ve afectado por la felicidad y se busca minimizar el precio. A medida que aumentamos la felicidad, el valor del heurístico va disminuyendo, de tal manera que se acerca al heurístico por felicidad que es un valor totalmente negativo. Podemos observar tambien que a partir de cierto valor, la felicidad supera al precio y se acaba comportando como el heurístico de la felicidad.\n");
        
        bw.close();
        
        final long endTimeProgram = System.currentTimeMillis();
        System.out.println("total time: "+ (endTimeProgram - startTimeProgram) + " miliseconds");
    }
}

