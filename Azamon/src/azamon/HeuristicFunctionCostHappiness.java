package azamon;
import aima.search.framework.HeuristicFunction;

public class HeuristicFunctionCostHappiness implements HeuristicFunction{
    
    /**
     * Funcion que retorna el heurístico por coste y felicidad de un estado.
     * En este caso valoramos la proporcion Felicidad/coste para determinar un valor para el heurístico.
     * Sobreescribimos la clase "HeuristicFunction" del aima.
     * @param state Nuevo estado del que calcular el heurístico
     * @return Heurístico por coste y felicidad del estado.
     */
    @Override
    public double getHeuristicValue(Object state) {
        double price = ((Estado)state).getPrice();
        int happiness = ((Estado)state).getHappiness();
        return (happiness*10)/price;
    }
}
