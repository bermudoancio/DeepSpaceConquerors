package spaceConquerors;

import java.util.Random;

import utils.Dado;

public class Tablero {
	
	// Aplicamos el patrón singleton
	private static Tablero tablero; 
	
	public static final int MIN_JUGADORES = 2;
	public static final int MAX_JUGADORES = 4;
	public static final int NAVES_A_LA_VENTA = 4;
	
	// Las cartas de naves a la venta
	private Nave[] navesVenta;
	
	// Los jugadores
	private Jugador[] jugadores;
	
	// Los planetas del tablero
	private Planeta[] planetas;
	
	// Los dados
	private Dado dadoA;
	private Dado dadoB;
	private Dado dadoC;

	/**
	 * Constructor del tablero
	 * @param jugadores
	 * @throws InvalidValueException
	 */
	private Tablero(int jugadores) throws InvalidValueException {
		// Inicializamos
		this.navesVenta = new Nave[Tablero.NAVES_A_LA_VENTA];
		
		if (jugadores < 2 || jugadores > Tablero.MAX_JUGADORES) {
			throw new InvalidValueException("Lo siento, este juego es de 2 a " + Tablero.MAX_JUGADORES + " jugadores.");
		}
		
		this.jugadores = new Jugador[jugadores];
		
		// Generamos las cartas aleatorias de naves que estarán a la venta
		this.generaMazoNaves();
		
		// Generamos los planetas basados en el número de jugadores
		this.generaPlanetas();
		
		// Inicializamos los dados
		this.dadoA = new Dado(4, 2);
		this.dadoB = new Dado(12, 1);
		this.dadoC = new Dado(10, 15);
	}
	
	/**
	 * Método que devuelve la única instancia del tablero
	 * @param jugadores el número de jugadores que jugará la partida
	 * @return la única instancia del tablero
	 * @throws InvalidValueException Si el número de jugadores no es válido
	 * @throws JuegoException si se intenta inicializar dos veces el tablero
	 */
	public static Tablero getTablero(int jugadores) throws InvalidValueException, JuegoException {
		if (Tablero.tablero == null) {
			Tablero.tablero = new Tablero(jugadores);
		}
		else {
			throw new JuegoException("No puedes inicializar dos tableros a la vez");
		}
		
		return Tablero.tablero;
	}
	
	
	/**
	 * Añade un jugador al juego
	 * @param j El nuevo jugador
	 * @throws JuegoException Si el jugador no puede ser añadido
	 */
	public void addJugador(Jugador j) throws JuegoException {
		// Vamos a comprobar que no exista ya un jugador con ese nombre
		if (this.getJugador(j.getNombre()) != null) {
			throw new JuegoException("Ya existe un jugador con ese nombre");
		}
		
		boolean encontradoHueco = false;
		
		for (int i = 0; i < this.jugadores.length && !encontradoHueco; i++) {
			if (this.jugadores[i] == null) {
				encontradoHueco = true;
				this.jugadores[i] = j;
			}
		}
		
		if (!encontradoHueco) {
			throw new JuegoException("No se puede añadir otro jugador");
		}
	}
	
	/**
	 * Genera el mazo de cartas de naves a la venta de manera aleatoria
	 */
	private void generaMazoNaves() {
		for (int i = 0; i < this.navesVenta.length; i++) {
			this.navesVenta[i] = generaCartaNaveAleatoria();
		}
		
	}

	/**
	 * Genera una carta de nave de tipo aleatorio
	 * @return Una carta de nave de tipo aleatorio
	 */
	private Nave generaCartaNaveAleatoria() {
		Nave carta = null;
		
		/*
		 * Para generar las cartas de naves, vamos a suponer la siguiente
		 * asignación de número a carta:
		 * 
		 * 		- Naves de ataque: 0
		 * 		- Naves de transporte: 1
		 * 		- Naves de carga: 2
		 */
		
		Random r = new Random();
		int numAleatorio = r.nextInt(3);
		
		try {
			switch (numAleatorio) {
			case 0:
				carta = new NaveAtaque("Nave de ataque");
				break;
				
			case 1:
				carta = new NaveTransporte("Nave de transporte");
				break;
			
			case 2:
				carta = new NaveCarga("Nave de carga");
				break;
			}
		} 
		catch (InvalidValueException e) {
			/*
			 * Como el nombre de la nave lo generamos nosotros, nunca
			 * podrá ser inválido 
			 */
		}
		
		return carta;
	}
	
	/**
	 * Inicializa los planetas en base al número de jugadores
	 */
	private void generaPlanetas() {
		/*
		 * Como método, propondremos por ejemplo que se generen 4 planetas
		 * por cada jugador.		
		 */
		this.planetas = new Planeta[this.jugadores.length * 4];
		
		for (int i = 0; i < this.planetas.length; i++) {
			this.planetas[i] = this.generaPlanetaAleatorio();
		}
	}
	
	/**
	 * Genera un planeta con nombre aleatorio
	 * @return Un planeta con nombre aleatorio tipo "Planeta 324"
	 */
	private Planeta generaPlanetaAleatorio() {
		Random r = new Random();
		Planeta p = null;
		
		try {
			p = new Planeta("Planeta " + r.nextInt(1000));
		} 
		catch (InvalidValueException e) {
			/*
			 * La excepción no se generará pues el nombre del planeta
			 * lo hemos escrito nosotros
			 */
		}
		
		return p;
	}
	
	/**
	 * Asigna aleatoriamente un planeta base a cada jugador
	 */
	public void asignacionInicial() {
		Random r = new Random();
		
		for (Jugador j: this.jugadores) {
			int posicionAleatoriaPlanetas;
			
			/*
			 * Generaremos aleatoriamente números (posiciones del array
			 * de planetas), hasta que encontremos una vacía
			 */
			do {
				posicionAleatoriaPlanetas = r.nextInt(this.planetas.length);
			}
			while (this.planetas[posicionAleatoriaPlanetas].getConquistador() != null);
			
			// El jugador j será el conquistador de su planeta base
			this.planetas[posicionAleatoriaPlanetas].conquistar(j);
			
		}
		
	}
	
	/**
	 * Método que recibe la letra del dado a lanzar y devuelve el resultado de lanzarlo
	 * @param dado el nombre (la letra) del dado a lanzar
	 * @return el resultado de lanzar el dado
	 */
	public int lanzarDado(char dado) {
		int res = 0;
		switch (Character.toUpperCase(dado)) {
		case 'A':
			res = this.dadoA.lanzar();
			break;
			
		case 'B':
			res = this.dadoB.lanzar();
			break;
			
		case 'C':
			res = this.dadoC.lanzar();
			break;
		}
		
		return res;
	}
	
	/**
	 * Devuelve un jugador dado un nombre
	 * @param nombre el nombre buscado
	 * @return el jugador con dicho nombre, o null si no se encuentra
	 */
	public Jugador getJugador(String nombre) {
		Jugador j = null;
		
		for (int i = 0; i < this.jugadores.length && j == null; i++) {
			if (this.jugadores[i] != null && this.jugadores[i].getNombre().equalsIgnoreCase(nombre)) {
				j = this.jugadores[i];
			}
		}
		
		return j;
	}
	
	/**
	 * @return el array de jugadores
	 */
	public Jugador[] getJugadores() {
		return jugadores;
	}

	/**
	 * @return el listado de naves a la venta
	 */
	public Nave[] getNavesVenta() {
		return navesVenta;
	}
	
	/**
	 * @return los planetas del tablero
	 */
	public Planeta[] getPlanetas() {
		return planetas;
	}
	
	/**
	 * @return los planetas del tablero que le pertenecen a un jugador determinado
	 */
	public Planeta[] getPlanetasDeJugador(Jugador j) {
		/*
		 * Recorreremos la lista dos veces: una para contar los plantas
		 * que le perteneces al usuario y así crear un array, y otro
		 * para asignar los planetas a ese array
		 */
		int numPlanetasPosee = 0;
		for (Planeta p: planetas) {
			if (p.getConquistador() == j) {
				//El planeta le pertenece al usuario
				numPlanetasPosee++;
			}
		}
		
		Planeta[] planetasDeUsuario = new Planeta[numPlanetasPosee];
		int indice = 0;
		
		for (Planeta p: planetas) {
			if (p.getConquistador() == j) {
				//El planeta le pertenece al usuario
				planetasDeUsuario[indice++] = p;
			}
		}
		
		return planetasDeUsuario;
	}
	
	/**
	 * Devuelve el planeta con nombre "nombre"
	 * @param nombre El nombre del planeta buscado
	 * @return El planeta si se encuentra o null en caso contrario
	 */
	public Planeta getPlaneta(String nombre) {
		Planeta p= null;
		
		for (int i = 0; i < this.planetas.length && p == null; i++) {
			if (this.planetas[i].getNombre().equalsIgnoreCase(nombre)) {
				p = this.planetas[i];
			}
		}
		
		return p;
	}
	
	/**
	 * Simula coger una carta de nave. Devuelve la carta escogida 
	 * y la reemplaza por una nueva aleatoria
	 * @param nave La carta a "comprar"
	 * @return la carta elegida, o null si la carta no estaba en el array
	 */
	public Nave comprarCartaNave(Nave nave) {
		Nave n = null;
		
		for (int i = 0; i < this.navesVenta.length && n == null; i++) {
			if (this.navesVenta[i] == nave) {
				n = this.navesVenta[i];
				// Eliminamos esa carta y generamos otra
				this.navesVenta[i] = this.generaCartaNaveAleatoria();
			}
		}
		
		return n;
	}
	
	/**
	 * Genera aleatoriamente una carta de material y la devuelve. Como nombre, llevará "Material", 
	 * y como tipo de material, uno escogido al azar. 
	 * @return una carta de material generada aleatoriamente
	 */
	public Material cogerCartaMaterial() {
		Material m = null;
		/*
		 * Tenemos que generar aleatoriamente una carta de material
		 * Para ello, generaremos un número al azar entre el número de materiales que haya
		 */
		Random r = new Random();
		try {
			m = new Material("Material" , TMateriales.getValuesAsString()[r.nextInt(TMateriales.values().length)]);
		} 
		catch (InvalidValueException e) {
			// El tipo de material ha sido escogido al azar entre los valores correctos, por lo que no hay problema
		}
		
		return m;
	}


	@Override
	public String toString() {
		// Información de los planetas
		StringBuilder sb = new StringBuilder();
		
		for (Planeta p: this.planetas) {
			sb.append(p);
		}
		
		return sb.toString();
	}

}
