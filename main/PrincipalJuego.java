package main;

import spaceConquerors.*;
import utils.UserDataCollector;

public class PrincipalJuego {
	
	public static final int NUMERO_RONDAS = 9;
	
	private Tablero t;
	private int ronda;
	
	public PrincipalJuego() {
		this.ronda = 1;
	}

	public static void main(String[] args) {
		PrincipalJuego juego = new PrincipalJuego();
		
		// Comenzamos la partida
		juego.comenzarPartida();
		
	}
	
	public void comenzarPartida() {
		// Asignamos los jugadores
		this.asignarJugadores();
		Jugador[] jugadores = this.t.getJugadores();
		
		// Turno de decidir qui�n empieza
		int indiceJugadorEmpieza = this.decideQuienEmpieza();
		int indiceJugador = indiceJugadorEmpieza;
		
		// Esta variable marcar� si un jugador ha ganado
		boolean juegoTerminado = false;
		
		while (ronda <= PrincipalJuego.NUMERO_RONDAS && !juegoTerminado) {
			// Cada vuelta del bucle ser� una ronda
			System.out.println("Ronda " + this.ronda + " de " + PrincipalJuego.NUMERO_RONDAS);

			// Imprimimos la informaci�n del tablero
			System.out.println(this.t);
			
			/*
			 * Se considerar� que una ronda se acaba cuando todos los jugadores no eliminados
			 * hayan hecho sus acciones
			 */
			boolean finRonda = false;
			
			while (!finRonda) {
				if (!jugadores[indiceJugador].isEliminado()) {
					System.out.println("Turno del jugador " + jugadores[indiceJugador].getNombre());
					
					int accionesRealizadas = 1;
					int accionElegida = 0;
					
					while (accionesRealizadas <= 2 && accionElegida != 9) {
						System.out.println("Jugada " + accionesRealizadas + " de 2");
						accionElegida = this.muestraMenuAcciones();
						
						switch(accionElegida) {
						case 1: // Comprar carta de nave
							break;
						case 2: // Comprar carta de construcci�n
							break;
						case 3: // Coger carta de materia prima
							break;
						case 4: // Construir
							break;
						case 5: // Mover nave de un planeta a otro
							break;
						case 6: // Atacar
							break;
						case 7: // Mejorar una nave
							break;
						case 8: // Reparar 
							break;
						case 9: // Pasar turno
							break;
							
						}
						
						accionesRealizadas++;
					}
					
				}
				
				indiceJugador = ++indiceJugador % jugadores.length;
				
				if (indiceJugador == indiceJugadorEmpieza) {
					finRonda = true;
				}
			}
			
			ronda++;
		}
		
		System.out.println("Fin del juego");
	}
	
	/**
	 * Pregunta el n�mero de jugadores y sus datos. Luego los a�ade al tablero.
	 */
	private void asignarJugadores() {
		try {
			// Vamos a crear el tablero. Preguntaremos por el n�mero de jugadores
			int numeroJugadores = PrincipalJuego.preguntarJugadores();
			this.setTablero(Tablero.getTablero(numeroJugadores));
			
			// Ahora vamos a a�adir a los jugadores
			for (int i = 0; i < numeroJugadores; i++) {
				boolean jugadorIntroducido = false;
				
				// Si por casualidad el nombre estuviese repetido, preguntamos hasta que no se repita
				while (!jugadorIntroducido) {
					try {
						this.getTablero().addJugador(PrincipalJuego.preguntaDatosJugador(i + 1));
						jugadorIntroducido = true;
					}
					catch (JuegoException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			
			//System.out.println(this.getTablero());
		} 
		catch (InvalidValueException e) {
			System.out.println(e.getMessage());
		} 
		catch (JuegoException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private static int preguntarJugadores() {
		return UserDataCollector.getEnteroMinMax("�Cu�ntos jugadores van a jugar?", Tablero.MIN_JUGADORES, Tablero.MAX_JUGADORES);
	}
	
	private static Jugador preguntaDatosJugador(int num) {
		Jugador j = null;
		try {
			j = new Jugador(UserDataCollector.getString("Introduce el nombre del personaje " + num));
		}
		catch (InvalidValueException e) {
			// El m�todo getString de UserDataCollector nos garantiza un nombre no nulo y no blank
		}
		
		return j;
	}
	
	private int decideQuienEmpieza() {
			
		System.out.println("Vamos a decidir qu� jugador empezar� a jugar");
				
		Jugador[] jugadores = this.t.getJugadores();
		int[] resultados = new int[jugadores.length];
		
		// Indica cu�l es por ahora el m�ximo n�mero conseguido en la ronda actual
		int maximoRondaActual = 0;
		// Indica cu�l fue el m�ximo n�mero conseguido en la pasada ronda
		int maximoRondaAnterior = 0;
		// Indica cu�l es por ahora el jugador que ha sacado el mayor n�mero
		int indiceMaximo = 0;
		// Indica qui�n est� tirando el dado en este momento
		int indiceActual;
		// Indica si estamos en una situaci�n de empate
		boolean empate = false;
		// Indica si una ronda de tiradas ha finalizado
		boolean rondaFinalizada = false;
		
		/*
		 * Se deber�n jugar siempre rondas completas. Una ronda es completa cuando han tirado todos los jugadores
		 * que deb�an tirar en la misma. En la primera ronda, tiran el dado todos. 
		 * Si se produce un empate, se deber� volver a jugar una nueva ronda, donde s�lo tirar�n los jugadores
		 * que sacaron la m�xima puntuaci�n en la ronda anterior.
		 */
		for (int i = 0; !rondaFinalizada || (rondaFinalizada && empate); i++) {
			
			// Creamos esta variable para evitar tener que usar la operaci�n % cada vez que queramos sacar el �ndice actual
			indiceActual = i % jugadores.length;
			// Si es la �ltima tirada, marcamos que la ronda habr� finalizado justo despu�s de esta tirada
			if (indiceActual == (jugadores.length - 1)) {
				rondaFinalizada = true;
			}
			
			// Si hay una segunda, tercera... ronda, solo contaremos aquellos jugadores que hubiesen empatado en la ronda pasada
			if (i >= jugadores.length) {
				// Una nueva ronda. Quitamos el m�ximo actual y lo pasamos al anterior, y reseteamos empate
				if (indiceActual == 0) {
					System.out.println("Ha habido un empate. �Otra ronda!");
					rondaFinalizada = false;
					maximoRondaAnterior = maximoRondaActual;
					maximoRondaActual = 0;
					empate = false;
				}
				
				// �Este usuario debe volver a tirar?
				if (resultados[indiceActual] < maximoRondaAnterior) {
					// Este jugador no fue uno de los que empat�, no participa ya en esta ronda
					// Ponemos su puntuaci�n a 0 por si existe otra ronda m�s y se obtienen resultados m�s bajos en la siguiente.
					resultados[indiceActual] = 0;
					continue;
				}
			}
			
			UserDataCollector.getTecla("Jugador " + jugadores[indiceActual].getNombre() + ": tira el dado pulsando enter");
						
			resultados[indiceActual] = t.lanzarDado('B');
			
			System.out.println("Has sacado un " + resultados[indiceActual]);
			
			// Si es el primero de la ronda, o ha sacado un n�mero mayor que el m�ximo actual, actualizamos datos
			if (resultados[indiceActual] > maximoRondaActual) {
				maximoRondaActual = resultados[indiceActual];
				indiceMaximo = indiceActual;
				// En este caso, ya no habr�a empate porque el jugador ha obtenido un n�mero m�s alto que el anterior m�ximo
				empate = false;
			}
			else if (resultados[indiceActual] == maximoRondaActual) {
				// Si el n�mero es igual, marcamos empate para que, en caso de que la ronda acabe as�, se vuelva a tirar
				empate = true;
			}
			
		}
		
		System.out.println("Comienza sacando el jugador " + jugadores[indiceMaximo].getNombre());
		return indiceMaximo;
	}

	/**
	 * Asigna el tablero
	 * @param t El tablero 
	 */
	public void setTablero(Tablero t) {
		this.t = t;
	}
	
	/**
	 * Devuelve el tablero de juego
	 * @return
	 */
	public Tablero getTablero() {
		return t;
	}
	
	/**
	 * Muestra el men� de las posibles acciones y recoge la acci�n escogida por el usuario
	 * @return la acci�n seleccionada por el usuario
	 */
	private int muestraMenuAcciones() {
		System.out.println("Acciones posibles");
		System.out.println("1. Comprar carta de nave");
		System.out.println("2. Comprar carta de construcci�n");
		System.out.println("3. Coger carta de materia prima");
		System.out.println("4. Construir");
		System.out.println("5. Mover una nave de un planeta a otro");
		System.out.println("6. Atacar");
		System.out.println("7. Mejorar una nave");
		System.out.println("8. Reparar");
		System.out.println("9. Pasar turno");
		
		return UserDataCollector.getEnteroMinMax("�Qu� quieres hacer?", 1, 9);
	}

}
