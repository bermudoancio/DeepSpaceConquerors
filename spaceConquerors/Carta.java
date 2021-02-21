package spaceConquerors;

public abstract class Carta {

	private String nombre;
	private int precio;
	
	public Carta(String nombre, int precio) throws InvalidValueException {
		if (nombre.isBlank()) {
			throw new InvalidValueException("El nombre de la carta no puede estar vacío");
		}
		
		if (precio < 0) {
			throw new InvalidValueException("El precio de la carta debe ser positivo o 0");
		}
		
		this.nombre = nombre;
		this.precio = precio;
		
	}

	/**
	 * @return el nombre de la carta
	 */
	public final String getNombre() {
		return nombre;
	}

	/**
	 * @return el precio de la carta en unidades de oro
	 */
	public final int getPrecio() {
		return precio;
	}
	
	

}
