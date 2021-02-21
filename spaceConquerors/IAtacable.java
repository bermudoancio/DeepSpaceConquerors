package spaceConquerors;

public interface IAtacable {
	
	/**
	 * Devuelve los puntos de defensa del objeto
	 * @return los puntos de defensa
	 */
	public int getPuntosDefensa();
	
	//public void setPuntosDefensa(int puntosDefensa);
	
	/**
	 * Resta al objeto los puntos de defensa que le inflige el ataque.
	 * @param <p>puntosDa�o El da�o que le inflingen. Si el resultado
	 * de restar los puntos es negativo, se lanzar� una excepci�n DestructionException
	 */
	public void serAtacado(int puntosDa�o)  throws InvalidValueException, DestructionException, JuegoException;
	
	/* Opci�n con m�todo default. Requiere definir el m�todo setPuntosDefensa como p�blico
	public default void serAtacado(int puntosDa�o)  throws InvalidValueException, DestructionException{
		if (puntosDa�o < 0) {
			throw new InvalidValueException("El da�o inflingido no puede ser menor a 0");
		}
				
		if (getPuntosDefensa() - puntosDa�o <= 0) {
			throw new DestructionException();
		}
		
		setPuntosDefensa(getPuntosDefensa() - puntosDa�o);
	}
	*/
	
}
