package azamon;
import aima.search.framework.HeuristicFunction;

public class HeuristicFunctionCost implements HeuristicFunction{
    
    /**
     * Funcion que retorna el heurístico por coste de un estado.
     * Sobreescribimos la clase "HeuristicFunction" del aima.
     * @param state Nuevo estado del que calcular el heurístico
     * @return Heurístico por coste del estado.
     */
    @Override
    public double getHeuristicValue(Object state) {
        return ((Estado)state).getPrice ();
    }
}
