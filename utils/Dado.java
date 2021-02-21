package utils;

import java.util.Random;

import spaceConquerors.InvalidValueException;

public class Dado implements ILanzable {

	public static final int NUM_MINIMO_CARAS = 4;
	
	// Número de caras del dado
	private int caras;
	
	// Número de la cara con menor valor del dado
	private int numMin;
	
	/**
	 * <p>Constructor que acepta el número de caras del dado y el valor mínimo
	 * @param caras número de caras del dado
	 * @param numMin valor de la cara más pequeña del dado
	 * @throws InvalidValueException Si caras es menor que 4 ó numMin es menor que 1
	 */
	public Dado(int caras, int numMin) throws InvalidValueException {
		/*
		 * Realizaremos aquí las comprobaciones pues no se permitirá posteriormente
		 * modificar dichos datos
		 */
		if (caras < Dado.NUM_MINIMO_CARAS) {
			throw new InvalidValueException("El dado debe tener como mínimo " + Dado.NUM_MINIMO_CARAS + " caras");
		}
				
		if (numMin < 1) {
			throw new InvalidValueException("El valor más pequeño posible es 1");
		}
		
		this.caras = caras;
		this.numMin = numMin;
		
	}

	@Override
	/**
	 * <p>Lanza el dado y devuelve un valor entre el valor de la cara más pequeña y
	 * la cara más grande, con la misma probabilidad.
	 */
	public int lanzar() {
		Random r = new Random();
		
		return r.nextInt(this.caras) + numMin;
	}

}
