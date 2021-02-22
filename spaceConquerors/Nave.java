package spaceConquerors;

public abstract class Nave extends Carta implements IAtacable, IReparable, IMejorable {
	
	protected static final int PUNTOS_REPARACION_NAVE = 10;
	
	protected int puntosDefensa;
	private Jugador jugador;

	/**
	 * Crea la nave
	 * @param nombre
	 * @param precio
	 * @throws InvalidValueException
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
	public void serAtacado(int puntosDaño) throws InvalidValueException, DestructionException {

		if (puntosDaño < 0) {
			throw new InvalidValueException("El daño inflingido no puede ser menor a 0");
		}
		
		puntosDefensa -= puntosDaño;
		
		if (puntosDefensa <= 0) {
			throw new DestructionException("La nave " + super.getNombre() + " ha sido destruida");
		}
		
	}
	
	@Override
	/**
	 * Aumenta los puntos de defensa de la nave. En principio, no hay 
	 * límite superior
	 */
	public void reparar() {
		this.puntosDefensa += Nave.PUNTOS_REPARACION_NAVE;
	}
	
	/**
	 * Asigna el dueño de una carta
	 * @param j El dueño de la carta
	 */
	public void asignarAJugador(Jugador j) {
		this.jugador = j;
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
