package azamonv2.Heuristiques;

import azamonv2.*;
import aima.search.framework.HeuristicFunction;

public class HeuristicFunctionHappiness implements HeuristicFunction{
    
    /**
     * Funcion que retorna el heurístico por coste de un estado.
     * Sobreescribimos la clase "HeuristicFunction" del aima.
     * @param state Nuevo estado del que calcular el heurístico
     * @return Heurístico por coste del estado.
     */
    @Override
    public double getHeuristicValue(Object state) {
        return (-1)*((Estado)state).getHappiness();
        //return (-1)*Estado.getHappiness ();
    }
}
