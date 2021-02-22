package spaceConquerors;

public class Jugador implements Comparable<Jugador> {
	
	private static final int UNIDADES_ORO_INICIALES = 5;
	private static final int CARTAS_CONSTRUCCION_MAX = 100;

	private int unidadesOro;
	private int puntuacion;
	private String nombre;
	private boolean eliminado;
	
	private Construccion[] cartasConstruccion;
	
	public Jugador(String nombre) throws InvalidValueException {
		if (nombre.isBlank()) {
			throw new InvalidValueException("El nombre del jugador no puede ser nulo");
		}
		
		this.nombre = nombre;
		this.unidadesOro = Jugador.UNIDADES_ORO_INICIALES;
		this.eliminado = false;
		
		// Inicializamos el array de cartas
		this.cartasConstruccion = new Construccion[Jugador.CARTAS_CONSTRUCCION_MAX];
	}

	/**
	 * @return las unidades de oro que tiene el jugador ahora mismo
	 */
	public int getUnidadesOro() {
		return unidadesOro;
	}
	
	/**
	 * @return la puntuación
	 */
	public int getPuntuacion() {
		return this.puntuacion;
	}
	
	/**
	 * @return el nombre del jugador
	 */
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public int compareTo(Jugador o) {
		if (o != null && o instanceof Jugador) {
			/*
			 * Debe devolver un número < 0 si este jugador tiene MAYOR
			 * puntuación que el otro. 
			 */
			
			return o.getPuntuacion() - this.puntuacion;
		}
		return -1;
	}

	/**
	 * @return Si el jugador está eliminado o no
	 */
	public boolean isEliminado() {
		return eliminado;
	}
	
	/**
	 * Resta las unidades de oro al saldo del jugador
	 * @param unidadesOro unidades de oro a restar al saldo
	 * @throws JuegoException si el jugador no tiene suficientes unidades de oro
	 */
	public void pagarOro(int unidadesOro) throws JuegoException {
		if (this.unidadesOro < unidadesOro) {
			throw new JuegoException("El jugador no tiene oro suficiente");
		}
		
		this.unidadesOro -= unidadesOro;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean eq = true;
		if (obj == null) {
			eq = false;
		}
		else if (obj.getClass() != this.getClass()) {
			eq = false;
		}
		else if (!this.getNombre().equalsIgnoreCase(((Jugador)obj).getNombre())) {
			eq = false;
		}
		
		return eq;
	}
	
	/**
	 * Añade una carta de construcción al mazo del jugador
	 * @param carta la carta a añadir
	 * @throws JuegoException si no se puede añadir la carta
	 */
	public void addCartaConstruccion(Construccion carta) throws JuegoException {
		boolean added = false;
		
		for (int i = 0; i < this.cartasConstruccion.length && !added; i++) {
			if (this.cartasConstruccion[i] == null) {
				this.cartasConstruccion[i] = carta;
				added = true;
			}
		}
		
		if (!added) {
			throw new JuegoException("No se puede añadir la construcción");
		}
		
	}
	
	/**
	 * Añade las unidades de oro indicadas al "monedero" del usuario
	 * @param oro la cantidad de oro a añadir
	 * @throws InvalidValueException Si la cantidad es 0 o un número negativo
	 */
	public void addOro(int oro) throws InvalidValueException {
		if (oro <= 0) {
			throw new InvalidValueException("No puedes añadir una cantidad de oro negativa o 0");
		}
		else {
			this.unidadesOro += oro;
		}
	}

}
