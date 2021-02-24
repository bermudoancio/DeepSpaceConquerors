package spaceConquerors;

public class NaveTransporte extends Nave {
	
	private static final int MEJORA_NAVE_TRANSPORTE = 2;
	private static final int PRECIO_CARTA_NAVE_TRANSPORTE = 2;
	private static final int PUNTOS_DEFENSA_INICIAL_TRANSPORTE = 4;

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
	
	/**
	 * Asigna la capacidad de transporte después de lanzar el dado.
	 * @param capacidad la capacidad de transporte de la nave
	 * @throws InvalidValueException Si la capacidad es 0 o negativo
	 */
	public void setCapacidad(int capacidad) throws InvalidValueException {
		if (this.capacidad <= 0) {
			throw new InvalidValueException("La capacidad de la nave debe ser de al menos 1 ocupante");
		}
		
		this.capacidad = capacidad;
	}
	
	/**
	 * Resetea la capacidad a 0. Normalmente debido a una acción cancelada
	 */
	public void resetCapacidad() {
		this.capacidad = 0;
	}


	@Override
	/**
	 * Mejora la capacidad de la nave por un factor multiplicador
	 */
	public void mejorar() {
		this.capacidad *= NaveTransporte.MEJORA_NAVE_TRANSPORTE;		
	}
	
	/**
	 * @return la capacidad de pasajeros de la nave
	 */
	public int getCapacidad() {
		return this.capacidad;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(". Capacidad de transporte: ");
		
		if (this.capacidad == 0) {
			sb.append("lanzar dado para ver");
		}
		else {
			sb.append(this.capacidad).append(" pasajeros");
		}
		
		return sb.toString();
	}

}
