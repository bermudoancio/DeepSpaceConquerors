package spaceConquerors;

public interface ITransportable {
	/**
	 * Transporta un n�mero de personas determinado al planeta destino
	 * @param destino el planeta al que ser�n transportadas las personas
	 * @param personas el n�mero de personas a transportar
	 * @throws InvalidValueException Si el n�mero de personas es menor que 0 o mayor que las existentes
	 * @throws JuegoException Si el planeta destino est� conquistado por otro jugador
	 */
	public void transportar(Planeta destino, int personas) throws InvalidValueException, JuegoException;
}
