package azamonv2;

import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;
import IA.Azamon.*;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.SimulatedAnnealingSearch;
import static java.lang.System.exit;
import java.util.Scanner;

public class Main {

    /**
     * @param args the command line arguments
     */
    static final int semilla = 1234; //Integer.valueOf(""+new Date().getTime()%100000);
    static int numeroPaquetes = 10;
    static double proporcion = 1.2;
    
    public static void main(String[] args) {
        Paquetes paquetes = new Paquetes(numeroPaquetes,semilla);
        Estado.setPackages(paquetes);
        Transporte transporte = new Transporte(paquetes, proporcion , semilla);
        Estado.setOffers(transporte);
        Estado estadoInicial = new Estado();
        
        Scanner scanner = new Scanner (System.in); //Creación de un objeto Scanner
        String answer = "";
        
        System.out.print("¿Printar solucion inicial? (s/n) ");
        answer = scanner.next();
        if (answer.equals("s")||answer.equals("S")){
            System.out.println("+---------------------------------------------------------------------------------+\n"+
                               "|                                SOLUCION INICIAL                                 |\n"+
                               "+---------------------------------------------------------------------------------+");
            System.out.println(estadoInicial);
            System.out.println("+---------------------------------------------------------------------------------+\n"+
                               "+---------------------------------------------------------------------------------+\n");
        }
        
        System.out.print("¿Ejecutar Simulate Annealing? (s/n) ");
        answer = scanner.next();
//        if (answer.equals("s")||answer.equals("S")){
//            // 1 - Declaramos el generador
//            GeneradorSimulatedAnneiling generadorSA = new GeneradorSimulatedAnneiling(semilla);
//            // 2 - Elegimos la Heurística
//            System.out.print("Función Heurística:\n  1 - Coste\n  2 - Felicidad\n  3 - Coste + Felicidad\nSelección: ");
//            answer = scanner.next();
//            Problem problemaSA;
//            switch (answer) {
//                case "1": problemaSA = new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionCost()); break;
//                case "2": problemaSA = new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionHappiness()); break;
//                case "3": //Por defecto se usa la 3
//                default:  problemaSA = new Problem(estadoInicial, generadorSA, state -> true, new HeuristicFunctionCostHappiness()); break; 
//            }
//            try{
//                // 3 - Declaramos las cosas necesarias: El "Search y el "Agent"
//                SimulatedAnnealingSearch SASearch = new SimulatedAnnealingSearch(100, 20, 5, 0.001);
//                System.out.println("\033[33mEmpezando Simulated Annealing\033[30m");
//                SearchAgent agent = new SearchAgent(problemaSA, SASearch);
//                System.out.println("Simulated Annealing terminado");
//                // 4 - "Pedimos" el estado final resultante de usar la estrategia determinada
//                Estado estadoSA = (Estado) SASearch.getGoalState();
//                // 5 - Printamos la solución
//                System.out.println("Simulated Annealing: felicidad: " + estadoSA.getHappiness() + ", precio " + estadoSA.getPrice());
//                System.out.print("¿Printar solucion de Simulate Annealing? (s/n) ");
//                answer = scanner.next();
//                if (answer.equals("s")||answer.equals("S")){
//                System.out.println("+---------------------------------------------------------------------------------+"+"\n"+
//                                   "|                              SIMULATED ANNEALING                                |"+"\n"+
//                                   "+---------------------------------------------------------------------------------+");
//                System.out.println(estadoSA);
//                System.out.println("+---------------------------------------------------------------------------------+\n"+
//                                   "+---------------------------------------------------------------------------------+\n");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                System.err.println("Simulated Annealing no terminado con éxito");
//                exit(1);
//            }
//        }
//        
        System.out.print("¿Ejecutar Hill Climbing? (s/n) ");
        answer = scanner.next();
        if (answer.equals("s")||answer.equals("S")){
            // 1 - Declaramos el generador
            GeneradorHillClimbing generadorHC = new GeneradorHillClimbing();
            // 2 - Elegimos la Heurística
            System.out.print("Función Heurística:\n  1 - Coste\n  2 - Felicidad\n  3 - Coste + Felicidad\nSelección: ");
            answer = scanner.next();
            Problem problemaHC;
            switch (answer) {
                case "1": problemaHC = new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCost()); break;
                case "2": problemaHC = new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionHappiness()); break;
                case "3": //Por defecto se usa la 3
                default:  problemaHC = new Problem(estadoInicial, generadorHC, state -> true, new HeuristicFunctionCostHappiness()); break; 
            }
            try{
                // 3 - Declaramos las cosas necesarias: El "Search y el "Agent"
                SimulatedAnnealingSearch HCSearch = new SimulatedAnnealingSearch(100, 20, 5, 0.001);
                System.out.println("\033[33mEmpezando Hill Climbing\033[30m");
                SearchAgent agent = new SearchAgent(problemaHC, HCSearch);
                System.out.println("Hill Climbing terminado");
                // 4 - "Pedimos" el estado final resultante de usar la estrategia determinada
                Estado estadoHC = (Estado) HCSearch.getGoalState();
                // 5 - Printamos la solución
                System.out.println("Hill Climbing: felicidad: " + estadoHC.getHappiness() + ", precio " + estadoHC.getPrice());
                System.out.print("¿Printar solucion de Hill Climbing? (s/n) ");
                answer = scanner.next();
                if (answer.equals("s")||answer.equals("S")){
                System.out.println("+---------------------------------------------------------------------------------+"+"\n"+
                                   "|                                 HILL CLIMBING                                   |"+"\n"+
                                   "+---------------------------------------------------------------------------------+");
                System.out.println(estadoHC);
                System.out.println("+---------------------------------------------------------------------------------+\n"+
                                   "+---------------------------------------------------------------------------------+\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Hill Climbing no terminado con éxito");
                exit(1);
            }
            
        }
        
        
        
    }
    
}
