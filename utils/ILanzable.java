package utils;

public interface ILanzable {
	/**
	 * Lanza el dado
	 * @return el número generado al azar entre el mínimo y el máximo del dado
	 */
	public int lanzar();
	
	/**
	 * @return el valor mínimo posible del dado
	 */
	public int getMin();
	
	/**
	 * @return el valor máximo posible del dado
	 */
	public int getMax();
}
