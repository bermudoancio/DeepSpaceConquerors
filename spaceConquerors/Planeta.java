package spaceConquerors;

public class Planeta implements IAtacable {
	
	public static final int NUM_MAX_MINAS = 10;
	public static final int NUM_MAX_NAVES_ORBITANDO = 100;
	public static final int UNIDADES_PIEDRA_DEFAULT = 5;
	public static final int UNIDADES_HIERRO_DEFAULT = 4;
	public static final int UNIDADES_COMBUSTIBLE_DEFAULT = 2;
	public static final int HABITANTES_DEFAULT = 30;
	private static final int NUEVOS_HABITANTES_POR_RONDA = 2;
	
	
	private String nombre;
	private Jugador conquistador;
	private int numHabitantes;
	
	/*
	 * Hay diferentes formas de guardar las materias primas:
	 * 	- Un enumerado con sobrecarga de par�metros
	 *  - Un mapa (que todav�a no conocemos)
	 *  - Un array con posiciones fijadas
	 *  - Etc.
	 *  
	 *  Para este caso iremos con una soluci�n simple. Como
	 *  tenemos s�lo 3 materias primas que pertenezcan al planeta, 
	 *  las guardaremos en una variable individual que contar� 
	 *  cu�ntas unidades de materia de cada tipo quedan.
	 */
	private int unidadesPiedra;
	private int unidadesHierro;
	private int unidadesCombustible;
	
	// Las posibles construcciones del planeta
	private Mina[] minas;
	private EscudoProtector escudo;
	
	// Las naves que orbitan un planeta
	private Nave[] navesOrbitando;
	
	// Variables necesarias para determinar la primera conquista
	/*
	 * Lista en la que se incluir� a todos aquellos jugadores que,
	 * antes de que el planeta est� conquistado, mueven habitantes
	 * al mismo. Una vez el planeta se conquiste, se ignora
	 */
	private Jugador[] jugadoresMuevenHabitantes;
	
	// TODO: Evitar que se pueda construir si no se ha movido al menos a una persona

	public Planeta(String nombre) throws InvalidValueException {
		if (nombre.isBlank()) {
			throw new InvalidValueException("El nombre del planeta no puede estar en blanco");
		}
		
		this.nombre = nombre;
		
		// Inicializamos con valores por defecto
		this.unidadesPiedra = Planeta.UNIDADES_PIEDRA_DEFAULT;
		this.unidadesHierro = Planeta.UNIDADES_HIERRO_DEFAULT;
		this.unidadesCombustible = Planeta.UNIDADES_COMBUSTIBLE_DEFAULT;
		
		// Ponemos el n�mero de habitantes por defecto
		this.numHabitantes = Planeta.HABITANTES_DEFAULT;
		
		// Inicializamos el array de minas
		this.minas = new Mina[Planeta.NUM_MAX_MINAS];
		
		// Inicializamos el array de naves orbitando
		this.navesOrbitando = new Nave[Planeta.NUM_MAX_NAVES_ORBITANDO];
		
	}

	@Override
	/**
	 * Trasladamos los ataques y los puntos de defensa al 
	 * escudo, en caso de que haya
	 */
	public int getPuntosDefensa() {
		int puntosDefensa = 0;
		if (this.escudo != null) {
			puntosDefensa = this.escudo.getPuntosDefensa();
		}
		
		return puntosDefensa;
	}

	@Override
	/**
	 * Trasladamos el ataque al escudo. Si el planeta no ten�a escudo, directamente se
	 * lanza la excepci�n DestructionException, marcando que ha sido conquistado. Si
	 * tras el ataque los puntos del escudo llegan a 0, tambi�n ocurrir� lo mismo.
	 * Si el escudo se destruye, las personas asignadas al mismo deben volver al total
	 * de habitantes.
	 * @throws DestructionException Si el planeta ha sido conquistado
	 * @throws InvalidValueException si puntosDa�o es 0 o menor que 0
	 * @throws JuegoException Si el planeta no estaba conquistado previamente
	 */
	public void serAtacado(int puntosDa�o) throws InvalidValueException, DestructionException, JuegoException {
		if (this.conquistador == null) {
			throw new JuegoException("Un planeta sin conquistar no puede ser atacado");
		}
		
		// Si el planeta no tiene escudo y es atacado, pasa a ser del jugador atacante
		if (this.escudo == null) {
			// Lanzamos DestructionException para marcar que ha sido conquistado
			throw new DestructionException();
		}
		
		try {
			this.escudo.serAtacado(puntosDa�o);
		}
		catch (DestructionException e) {
			/*
			 * El escudo ha sido destruido. Pasamos las personas asignadas a 
			 * los habitantes y marcamos el escudo como null 
			 */
			this.escudo = null;
			this.numHabitantes += EscudoProtector.PERSONAS_ASIGNADAS_ESCUDO_PROTECTOR;
			
			// Avisamos de la conquista
			throw new DestructionException();
		}
	}
	
	/**
	 * El conquistador del planeta cambia.
	 * @param j El jugador que ha conquistado el planeta
	 */
	public void conquistar(Jugador j) {
		this.conquistador = j;
	}

	/**
	 * @return el nombre del planeta
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @return el jugador conquistador del planeta, o null si no est� conquistado
	 */
	public Jugador getConquistador() {
		return conquistador;
	}

	/**
	 * @return el n�mero de habitantes del planeta
	 */
	public int getNumHabitantes() {
		return numHabitantes;
	}

	/**
	 * @return las unidades de piedra que hay en el planeta
	 */
	public int getUnidadesPiedra() {
		return unidadesPiedra;
	}

	/**
	 * @return las unidades de hierro que hay en el planeta
	 */
	public int getUnidadesHierro() {
		return unidadesHierro;
	}

	/**
	 * @return las unidades de combustible que hay en el planeta
	 */
	public int getUnidadesCombustible() {
		return unidadesCombustible;
	}

	/**
	 * @return las minas del planeta
	 */
	public Mina[] getMinas() {
		return minas;
	}

	/**
	 * @return el escudo protector, si lo hay, y si no null
	 */
	public EscudoProtector getEscudo() {
		return escudo;
	}
	
	/**
	 * Devuelve el n�mero de minas activas en el planeta
	 * @return el n�mero de minas activas en el 
	 */
	public int getNumeroMinasActivas() {
		int minas = 0;
		
		for (Mina m: this.minas) {
			if (m != null) {
				minas++;
			}
		}
		
		return minas;
	}
	
	/**
	 * A�ade una nave a la �rbita del planeta
	 * @param n La nueva nave
	 * @throws JuegoException Si la nave no puede a�adirse a la �rbita
	 */
	public void addNaveOrbitando(Nave n) throws JuegoException {
		boolean added = false;
		
		for (int i = 0; i < this.navesOrbitando.length && !added; i++) {
			if (this.navesOrbitando[i] == null) {
				this.navesOrbitando[i] = n;
				added = true;
			}
		}
		
		if (!added) {
			throw new JuegoException("No se puede a�adir la nave");
		}
	}
	
	/**
	 * Elimina una nave de la �rbita de un planeta
	 * @param n la nave a eliminar
	 * @throws JuegoException Si la nave no orbitaba dicho planeta
	 */
	public void deleteNaveOrbitando(Nave n) throws JuegoException {
		boolean deleted = false;
		
		for (int i = 0; i < this.navesOrbitando.length && !deleted; i++) {
			if (this.navesOrbitando[i] == n) {
				this.navesOrbitando[i] = null;
				deleted = true;
			}
		}
		
		if (!deleted) {
			throw new JuegoException("La nave no est� orbitando este planeta");
		}
	}
	
	/**
	 * Devuelve el array de naves que orbita el planeta, reducido al n�mero concreto de naves que lo orbita.
	 * @return un array con las naves exactas que est�n orbitando un planeta
	 */
	public Nave[] getNavesOrbitando() {
		// Vamos a contar primero las naves que orbitan este planeta
		/*
		 * Creamos un array que contendr� las naves que orbitan dicho planeta.
		 * El tama�o de dicho array ser� por tanto, el n�mero de naves que lo orbita
		 */
		Nave[] navesOrbitanPlaneta = new Nave[this.getNumeroNavesOrbitando()];
		int i = 0;
		for (Nave n: this.navesOrbitando) {
			if (n != null) {
				navesOrbitanPlaneta[i++] = n;
			}
		}
		
		return navesOrbitanPlaneta;
	}
	
	/**
	 * Devuelve el n�mero de naves que orbitan este planeta (no el tama�o del array, sino el n�mero de naves que lo orbita)
	 * @return el n�mero de naves que est� actualmente orbitando este planeta
	 */
	public int getNumeroNavesOrbitando() {
		int naves = 0;
		
		for (Nave n: this.navesOrbitando) {
			if (n != null) {
				naves++;
			}
		}
		
		return naves;
	}
	
	/**
	 * A�ade las unidades pasadas como par�metro a las cantidades existentes del material.
	 * Solo se admiten hierro, piedra y combustible. Cualquier otro valor provoca el 
	 * lanzamiento de la excepci�n InvalidValueException
	 * @param material el material que queremos a�adir
	 * @param unidades la cantidad de unidades de material que a�adiremos
	 * @throws InvalidValueException Si intentamos a�adir 0 � un n�mero negativo de unidades, o un material diferente de los aceptados
	 */
	public void addUnidadesDeMateriaPrima(TMateriales material, int unidades) throws InvalidValueException {
		if (unidades <= 0) {
			throw new InvalidValueException("No puedes a�adir 0 o un n�mero negativo de unidades");
		}
		
		// Vamos a ver qu� material a�adimos
		if (material.equals(TMateriales.HIERRO)) {
			this.unidadesHierro += unidades;
		}
		else if (material.equals(TMateriales.PIEDRA)) {
			this.unidadesPiedra += unidades;
		}
		else if (material.equals(TMateriales.COMBUSTIBLE)) {
			this.unidadesCombustible += unidades;
		}
		else {
			throw new InvalidValueException("No puedes a�adir al planeta este material: " + material.toString());
		}
	}
	
	/**
	 * Aumenta la poblaci�n de este planeta en el n�mero de habitantes marque la constante Planeta.NUEVOS_HABITANTES_POR_RONDA
	 */
	public void aumentarPoblacionTrasRonda() {
		this.numHabitantes += Planeta.NUEVOS_HABITANTES_POR_RONDA;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{").append(System.lineSeparator())
		.append(this.getNombre()).append(System.lineSeparator())
		.append("Conquistador: ").append(this.getConquistador() == null ? "No conquistado" : this.getConquistador().getNombre())
		.append(". Habitantes: ").append(this.getNumHabitantes())
		.append(". Materias primas: ")
		.append("[Piedra: ").append(this.getUnidadesPiedra())
		.append(". Hierro: ").append(this.getUnidadesHierro())
		.append(". Combustible: ").append(this.getUnidadesCombustible())
		.append("]").append(System.lineSeparator())
		.append("Minas: ");
		
		if (this.getNumeroMinasActivas() > 0) {
			sb.append("[[");
			int numMinas = 1;
			for (Mina m: this.minas) {
				if (m != null) {
					sb.append(numMinas++).append("@ Material: ")
					.append(m.getMaterial().toString()).append(". ")
					.append(m.getCantidadExtraidaTurno()).append(" unidades por turno")
					.append(System.lineSeparator());
				}
			}
			
			sb.append("]]").append(System.lineSeparator());
		}
		else {
			sb.append("[[Sin minas activas]]");
		}
		
		// Por �ltimo, la informaci�n del escudo protector
		sb.append(System.lineSeparator()).append("Escudo protector: ");
		
		if (this.getEscudo() != null) {
			sb.append("[[").append(this.getEscudo().getPuntosDefensa())
			.append(" puntos de defensa restantes]]");
		}
		else {
			sb.append("[[Sin escudo protector]]");
		}
		sb.append(System.lineSeparator()).append("}").append(System.lineSeparator());
		
		return sb.toString();
	}

}
