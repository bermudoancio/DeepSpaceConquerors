package spaceConquerors;

public interface ITransportador {
	/**
	 * Transporta la cantidad especificada de los materiales al planeta destino
	 * @param destino el planeta al que se va a transportar la carga
	 * @param materiales el array de materiales. Cada elemento del array es una materia prima que se transporta
	 * @param cantidades el array de cantidades de materias primas. Coincide en índices con el array materiales.
	 * @throws InvalidValueException si se quiere transportar una cantidad negativa o mayor que la disponible
	 * @throws JuegoException Si el planeta al que se va a transportar la carga está conquistado por otro jugador
	 */
	public void transportar(Planeta destino, TMateriales[] materiales, int[] cantidades) throws InvalidValueException, JuegoException;
}
