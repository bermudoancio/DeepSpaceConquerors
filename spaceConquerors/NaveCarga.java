package spaceConquerors;

public class NaveCarga extends Nave {
	
	private static final int MEJORA_NAVE_CARGA = 2;
	private static final int PRECIO_CARTA_NAVE_CARGA = 3;
	private static final int PUNTOS_DEFENSA_INICIAL_CARGA = 10;
	
	private int capacidadCarga;

	/**
	 * Constructor de una carta nave de carga. 
	 * @param nombre Nombre de la nave
	 * @param capacidadCarga Capacidad de materias
	 * @throws InvalidValueException Si la capacidad es negativa o el nombre no válido
	 */
	public NaveCarga(String nombre) throws InvalidValueException {
		super(nombre, NaveCarga.PRECIO_CARTA_NAVE_CARGA, NaveCarga.PUNTOS_DEFENSA_INICIAL_CARGA);		
	}
	
	public void setCapacidadCarga(int capacidadCarga) throws InvalidValueException {
		if (capacidadCarga <= 0) {
			throw new InvalidValueException("La capacidad de carga debe ser positiva");
		}
		
		this.capacidadCarga = capacidadCarga;
	}

	@Override
	public void mejorar() {
		this.capacidadCarga *= NaveCarga.MEJORA_NAVE_CARGA;
	}
	
	/**
	 * @return la capacidad de carga de la nave
	 */
	public int getCapacidadCarga() {
		return this.capacidadCarga;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(". Capacidad de transporte: ");
		
		if (this.capacidadCarga == 0) {
			sb.append("lanzar dado para ver");
		}
		else {
			sb.append(this.capacidadCarga).append(" materias");
		}
		
		return sb.toString();
	}

}
