package spaceConquerors;

public class NaveTransporte extends Nave {
	
	private static final int MEJORA_NAVE_TRANSPORTE = 2;
	private static final int PRECIO_CARTA_NAVE_TRANSPORTE = 2;
	private static final int PUNTOS_DEFENSA_INICIAL_TRANSPORTE = 10;

	private int capacidad;
	
	/**
	 * Constructor de una carta de nave de transporte.
	 * @param nombre Nombre de la nave
	 * @param capacidad Capacidad de pasajeros
	 * @throws InvalidValueException Si la capacidad de pasajeros introducida
	 * es menor que 0 ó el nombre no es válido
	 */
	public NaveTransporte(String nombre) throws InvalidValueException {
		super(nombre, NaveTransporte.PRECIO_CARTA_NAVE_TRANSPORTE, NaveTransporte.PUNTOS_DEFENSA_INICIAL_TRANSPORTE);
		
	}
	
	public void setCapacidad(int capacidad) throws InvalidValueException {
		if (this.capacidad <= 0) {
			throw new InvalidValueException("La capacidad de la nave debe ser de al menos 1 ocupante");
		}
		
		this.capacidad = capacidad;
	}


	@Override
	/**
	 * Mejora la capacidad de la nave por un factor multiplicador
	 */
	public void mejorar() {
		this.capacidad *= NaveTransporte.MEJORA_NAVE_TRANSPORTE;		
	}

}
