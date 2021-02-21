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
	 * @param <p>puntosDaño El daño que le inflingen. Si el resultado
	 * de restar los puntos es negativo, se lanzará una excepción DestructionException
	 */
	public void serAtacado(int puntosDaño)  throws InvalidValueException, DestructionException, JuegoException;
	
	/* Opción con método default. Requiere definir el método setPuntosDefensa como público
	public default void serAtacado(int puntosDaño)  throws InvalidValueException, DestructionException{
		if (puntosDaño < 0) {
			throw new InvalidValueException("El daño inflingido no puede ser menor a 0");
		}
				
		if (getPuntosDefensa() - puntosDaño <= 0) {
			throw new DestructionException();
		}
		
		setPuntosDefensa(getPuntosDefensa() - puntosDaño);
	}
	*/
	
}
