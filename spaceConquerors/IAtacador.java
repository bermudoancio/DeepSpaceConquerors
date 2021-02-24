package spaceConquerors;

public interface IAtacador {
	/**
	 * Ataca un objetivo que sea atacable
	 * @param objetivo El objetivo al que se ataca
	 * @throws InvalidValueException Si el poder con el que se ataca al objetivo es menor que 0
	 * @throws DestructionException Si el objetivo ha sido destruido tras el ataque
	 * @throws JuegoException Si el objetivo no puede ser atacado en ciertas condiciones
	 */
	public void atacar(IAtacable objetivo) throws InvalidValueException, DestructionException, JuegoException;
}
