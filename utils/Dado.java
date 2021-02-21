package utils;

import java.util.Random;

import spaceConquerors.InvalidValueException;

public class Dado implements ILanzable {

	public static final int NUM_MINIMO_CARAS = 4;
	
	// N�mero de caras del dado
	private int caras;
	
	// N�mero de la cara con menor valor del dado
	private int numMin;
	
	/**
	 * <p>Constructor que acepta el n�mero de caras del dado y el valor m�nimo
	 * @param caras n�mero de caras del dado
	 * @param numMin valor de la cara m�s peque�a del dado
	 * @throws InvalidValueException Si caras es menor que 4 � numMin es menor que 1
	 */
	public Dado(int caras, int numMin) throws InvalidValueException {
		/*
		 * Realizaremos aqu� las comprobaciones pues no se permitir� posteriormente
		 * modificar dichos datos
		 */
		if (caras < Dado.NUM_MINIMO_CARAS) {
			throw new InvalidValueException("El dado debe tener como m�nimo " + Dado.NUM_MINIMO_CARAS + " caras");
		}
				
		if (numMin < 1) {
			throw new InvalidValueException("El valor m�s peque�o posible es 1");
		}
		
		this.caras = caras;
		this.numMin = numMin;
		
	}

	@Override
	/**
	 * <p>Lanza el dado y devuelve un valor entre el valor de la cara m�s peque�a y
	 * la cara m�s grande, con la misma probabilidad.
	 */
	public int lanzar() {
		Random r = new Random();
		
		return r.nextInt(this.caras) + numMin;
	}

}
