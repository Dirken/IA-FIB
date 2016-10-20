
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package azamon;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import aima.search.framework.Successor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Ricard
 */
public class GeneradorHillClimbing {
    /*
Para Hill Climbing tendréis que generar todas las posibles aplicaciones de los operadores al
estado actual
    */
    private Random random;
    
    private double precio;
    private int felicidad;
    
    private static Transporte ofertas;
    private static Paquetes paquetes;
    
    private ArrayList<ArrayList<Paquete>> serviciosEscogidos; // result
    private ArrayList<Double> pesoOfertaDisponible;
    private ArrayList<PaqueteOrdenado> paquetesOrdenados;
    private ArrayList<OfertaOrdenada> ofertasOrdenadas;

    /**
     * Acepta o no un movimiento dependiendo de si el cambio de ese paquete por otro, nos es permitido
     * en caso de que se exceda el peso máximo o que el paquete no llegue, no se considerará válido
     */

    /*
    Para Simulated Annealing tendréis que escoger al azar un operador y generar
    solo un sucesor aplicando este operador con parámetros también al azar.
    */
    public List getSuccessors(Object state) {
        String movimientosEjecutado = "";
        LinkedList<Successor>listaEstadosSucesores = new LinkedList();
        
        Paquete paquete = paquetes.get(random.nextInt(paquetes.size()));
        Oferta oferta = ofertas.get(random.nextInt(ofertas.size()));

        for(){
            for(){
                //escogemos un paquete y una oferta aleatorias

                //while the movement we want to do is not valid, we "backtrack" 
                //with another package and transport. If it's valid we just move.
                while (!movimientoValido(paquete,oferta)) {
                    paquete = paquetes.get(random.nextInt(paquetes.size()));
                    oferta = ofertas.get(random.nextInt(ofertas.size()));
                }
                movimientosEjecutado += "Paquete -> " + paquete + " Oferta " + oferta + "\n";
                mover(paquete, oferta);  
            }
        }
        for (){
            for(){
                Paquete paquete2 = paquetes.get(random.nextInt(paquetes.size()));
                Oferta oferta2 = ofertas.get(random.nextInt(ofertas.size()));
                //while the movement we want to do is not valid, we "backtrack" 
                //with another pair of packages and transports. If it's valid we just move.
                while ( (!movimientoValido(paquete,oferta2) && movimientoValido(paquete2,oferta))
                        ||(movimientoValido(paquete,oferta2) && !movimientoValido(paquete2,oferta))){
                    paquete = paquetes.get(random.nextInt(paquetes.size()));
                    paquete2 = paquetes.get(random.nextInt(paquetes.size()));
                    oferta = ofertas.get(random.nextInt(ofertas.size()));
                    oferta2 = ofertas.get(random.nextInt(ofertas.size()));
                }
                movimientosEjecutado += "( " + paquete + "," + oferta +" )" 
                                        + "<-> " +
                                         "( " + paquete2 + "," + oferta2 +" )" + "\n";

                intercambiar(paquete, paquete2, oferta, oferta2);
            }
        }
        listaEstadosSucesores.add(new Successor(movimientosEjecutado, 
                new Estado();));
        return listaEstadosSucesores;
    }
    
}
