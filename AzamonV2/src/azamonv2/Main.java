package azamonv2;

import azamonv2.Heuristiques.*;
import azamonv2.Generadores.*;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Problem;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

import java.util.Iterator;
import java.util.Properties;

public class Main {
    public static void main(String[] argv) {
        System.out.println("ESTO ES EL INICIO DE GLA2, que dominará el mundo");
        System.out.println("Creando paquetes. :D");
        Paquetes paquetes = new Paquetes(10, 1234);
        System.out.println("Creando transportes. :D:D");
        Transporte ofertas = new Transporte(paquetes, 1.2, 1234);
        Estado.setOffers(ofertas);
        Estado.setPackages(paquetes);
        System.out.println("Creando estado inicial. :D:D:D");
        Estado estadoInicial = new Estado();
        System.out.println(estadoInicial);

        GeneradorHillClimbing generadorSucesoresHillClimbing = new GeneradorHillClimbing();
        GeneradorSimulatedAnneiling generadorSucesoresSimulatedAnnealing = new GeneradorSimulatedAnneiling(1234);

        Problem problemH = new Problem(estadoInicial, generadorSucesoresHillClimbing, state -> true, new HeuristicFunctionCostHappiness());
        Problem problemHP = new Problem(estadoInicial, generadorSucesoresHillClimbing, state -> true, new HeuristicFunctionCost());
        Problem problemHF = new Problem(estadoInicial, generadorSucesoresHillClimbing, state -> true, new HeuristicFunctionHappiness());

//        Problem problemA = new Problem(estadoInicial, generadorSucesoresSimulatedAnnealing, state -> true, new HeuristicFunctionCostHappiness());
//        Problem problemAP = new Problem(estadoInicial, generadorSucesoresSimulatedAnnealing, state -> true, new HeuristicFunctionCost());
//        Problem problemAF = new Problem(estadoInicial, generadorSucesoresSimulatedAnnealing, state -> true, new HeuristicFunctionHappiness());

        HillClimbingSearch hillClimbingSearch = new HillClimbingSearch();
//        SimulatedAnnealingSearch simulatedAnnealingSearch = new SimulatedAnnealingSearch(4000, 20, 5, 0.001);
        System.out.println("Estado inicial: felicidad " + estadoInicial.getHappiness() + ", precio " + estadoInicial.getPrice());
        try {
            
            System.out.println("-----------------------------");
            System.out.println("HEURISTICO NORMAL:");
//            System.out.println("Starting Simulated Annealing");
//            SearchAgent agent = new SearchAgent(problemA, simulatedAnnealingSearch);
//            Estado estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            SearchAgent agent; Estado estadoFinal;
            
            
//            System.out.println("Simulated Annealing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice());
//            System.out.println(estadoFinal);
//            
            System.out.println("Hill Climbing");
            agent = new SearchAgent(problemH, hillClimbingSearch);
            estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            
            System.out.println("Hill climbing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice());
            System.out.println(estadoFinal);

            System.out.println("-----------------------------");
            System.out.println("HEURISTICO PRECIO");
//            System.out.println("Simulated Annealing");
            
//            agent = new SearchAgent(problemAP, simulatedAnnealingSearch);
//            estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            
//            System.out.println("Simulated Annealing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice());
//            System.out.println(estadoFinal);
            System.out.println("Hill Climbing");
            long time = System.nanoTime();
            agent = new SearchAgent(problemHP, hillClimbingSearch);
            time = System.nanoTime()-time;
            estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            
            System.out.println("Finished Hill Climbing");
            System.out.println("Hill climbing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice() + ", tiempo " + Math.round(time/1000000));
            System.out.println(estadoFinal);

            System.out.println("-----------------------------");
            System.out.println("HEURISTICO FELICIDAD");
//            System.out.println("Simulated Annealing");
//            agent = new SearchAgent(problemAF, simulatedAnnealingSearch);
//            estadoFinal = (Estado)simulatedAnnealingSearch.getGoalState();
            
//            System.out.println("Simulated Annealing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice());
//            System.out.println(estadoFinal);
            
            System.out.println("Hill Climbing");
            agent = new SearchAgent(problemHF, hillClimbingSearch);
            estadoFinal = (Estado)hillClimbingSearch.getGoalState();
            
            System.out.println("Hill climbing: felicidad: " + estadoFinal.getHappiness() + ", precio " + estadoFinal.getPrice());
            System.out.println(estadoFinal);

            //System.out.println("Estado inicial: felicidad " + estadoInicial.getHappiness() + ", precio " + estadoInicial.getPrice());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }
    }
}

