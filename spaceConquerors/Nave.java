package spaceConquerors;

public abstract class Nave extends Carta implements IAtacable, IReparable, IMejorable {
	
	protected static final int PUNTOS_REPARACION_NAVE = 10;
	
	protected int puntosDefensa;
	private Jugador jugador;

	/**
	 * Crea la nave
	 * @param nombre El nombre de la carta. Normalmente Nave
	 * @param precio El precio de la carta en unidades de oro
	 * @param puntosDefensa Los puntos de defensa de la nave
	 * @throws InvalidValueException Si el precio es menor que 0
	 */
	public Nave(String nombre, int precio, int puntosDefensa) throws InvalidValueException {
		super(nombre, precio);
		
		this.puntosDefensa = puntosDefensa;
	}

	@Override
	public int getPuntosDefensa() {
		return this.puntosDefensa;
	}

	@Override
	public void serAtacado(int puntosDa�o) throws InvalidValueException, DestructionException {

		if (puntosDa�o < 0) {
			throw new InvalidValueException("El da�o inflingido no puede ser menor a 0");
		}
		
		puntosDefensa -= puntosDa�o;
		
		if (puntosDefensa <= 0) {
			throw new DestructionException("La nave " + super.getNombre() + " ha sido destruida");
		}
		
	}
	
	@Override
	/**
	 * Aumenta los puntos de defensa de la nave. En principio, no hay 
	 * l�mite superior
	 */
	public void reparar() {
		this.puntosDefensa += Nave.PUNTOS_REPARACION_NAVE;
	}
	
	/**
	 * Asigna el due�o de una carta
	 * @param j El due�o de la carta
	 */
	public void asignarAJugador(Jugador j) {
		this.jugador = j;
	}
	
	/**
	 * Devuelve el jugador due�o de la carta, o null si no tiene due�o
	 * @return el jugador due�o de la carta, o null si no tiene due�o
	 */
	public Jugador getJugador() {
		return this.jugador;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("## ");
		sb.append(this.getNombre())
		.append(". Precio: ").append(this.getPrecio())
		.append(". Puntos de defensa: ").append(this.getPuntosDefensa());
		
		return sb.toString();
	}

}
