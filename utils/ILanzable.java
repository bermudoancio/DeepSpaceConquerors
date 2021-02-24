package utils;

public interface ILanzable {
	/**
	 * Lanza el dado
	 * @return el n�mero generado al azar entre el m�nimo y el m�ximo del dado
	 */
	public int lanzar();
	
	/**
	 * @return el valor m�nimo posible del dado
	 */
	public int getMin();
	
	/**
	 * @return el valor m�ximo posible del dado
	 */
	public int getMax();
}
