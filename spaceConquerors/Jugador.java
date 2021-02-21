package spaceConquerors;

public class Jugador implements Comparable<Jugador> {
	
	private static final int UNIDADES_ORO_INICIALES = 3; 

	private int unidadesOro;
	private int puntuacion;
	private String nombre;
	private boolean eliminado;
	
	public Jugador(String nombre) throws InvalidValueException {
		if (nombre.isBlank()) {
			throw new InvalidValueException("El nombre del jugador no puede ser nulo");
		}
		
		this.nombre = nombre;
		this.unidadesOro = Jugador.UNIDADES_ORO_INICIALES;
		this.eliminado = false;
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

}
