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
		
		// Turno de decidir quién empieza
		int indiceJugadorEmpieza = this.decideQuienEmpieza();
		int indiceJugador = indiceJugadorEmpieza;
		
		// Esta variable marcará si un jugador ha ganado
		boolean juegoTerminado = false;
		
		while (ronda <= PrincipalJuego.NUMERO_RONDAS && !juegoTerminado) {
			// Cada vuelta del bucle será una ronda
			System.out.println("Ronda " + this.ronda + " de " + PrincipalJuego.NUMERO_RONDAS);

			// Imprimimos la información del tablero
			System.out.println(this.t);
			
			/*
			 * Se considerará que una ronda se acaba cuando todos los jugadores no eliminados
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
						case 2: // Comprar carta de construcción
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
	 * Pregunta el número de jugadores y sus datos. Luego los añade al tablero.
	 */
	private void asignarJugadores() {
		try {
			// Vamos a crear el tablero. Preguntaremos por el número de jugadores
			int numeroJugadores = PrincipalJuego.preguntarJugadores();
			this.setTablero(Tablero.getTablero(numeroJugadores));
			
			// Ahora vamos a añadir a los jugadores
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
		return UserDataCollector.getEnteroMinMax("¿Cuántos jugadores van a jugar?", Tablero.MIN_JUGADORES, Tablero.MAX_JUGADORES);
	}
	
	private static Jugador preguntaDatosJugador(int num) {
		Jugador j = null;
		try {
			j = new Jugador(UserDataCollector.getString("Introduce el nombre del personaje " + num));
		}
		catch (InvalidValueException e) {
			// El método getString de UserDataCollector nos garantiza un nombre no nulo y no blank
		}
		
		return j;
	}
	
	private int decideQuienEmpieza() {
			
		System.out.println("Vamos a decidir qué jugador empezará a jugar");
				
		Jugador[] jugadores = this.t.getJugadores();
		int[] resultados = new int[jugadores.length];
		
		// Indica cuál es por ahora el máximo número conseguido en la ronda actual
		int maximoRondaActual = 0;
		// Indica cuál fue el máximo número conseguido en la pasada ronda
		int maximoRondaAnterior = 0;
		// Indica cuál es por ahora el jugador que ha sacado el mayor número
		int indiceMaximo = 0;
		// Indica quién está tirando el dado en este momento
		int indiceActual;
		// Indica si estamos en una situación de empate
		boolean empate = false;
		// Indica si una ronda de tiradas ha finalizado
		boolean rondaFinalizada = false;
		
		/*
		 * Se deberán jugar siempre rondas completas. Una ronda es completa cuando han tirado todos los jugadores
		 * que debían tirar en la misma. En la primera ronda, tiran el dado todos. 
		 * Si se produce un empate, se deberá volver a jugar una nueva ronda, donde sólo tirarán los jugadores
		 * que sacaron la máxima puntuación en la ronda anterior.
		 */
		for (int i = 0; !rondaFinalizada || (rondaFinalizada && empate); i++) {
			
			// Creamos esta variable para evitar tener que usar la operación % cada vez que queramos sacar el índice actual
			indiceActual = i % jugadores.length;
			// Si es la última tirada, marcamos que la ronda habrá finalizado justo después de esta tirada
			if (indiceActual == (jugadores.length - 1)) {
				rondaFinalizada = true;
			}
			
			// Si hay una segunda, tercera... ronda, solo contaremos aquellos jugadores que hubiesen empatado en la ronda pasada
			if (i >= jugadores.length) {
				// Una nueva ronda. Quitamos el máximo actual y lo pasamos al anterior, y reseteamos empate
				if (indiceActual == 0) {
					System.out.println("Ha habido un empate. ¡Otra ronda!");
					rondaFinalizada = false;
					maximoRondaAnterior = maximoRondaActual;
					maximoRondaActual = 0;
					empate = false;
				}
				
				// ¿Este usuario debe volver a tirar?
				if (resultados[indiceActual] < maximoRondaAnterior) {
					// Este jugador no fue uno de los que empató, no participa ya en esta ronda
					// Ponemos su puntuación a 0 por si existe otra ronda más y se obtienen resultados más bajos en la siguiente.
					resultados[indiceActual] = 0;
					continue;
				}
			}
			
			UserDataCollector.getTecla("Jugador " + jugadores[indiceActual].getNombre() + ": tira el dado pulsando enter");
						
			resultados[indiceActual] = t.lanzarDado('B');
			
			System.out.println("Has sacado un " + resultados[indiceActual]);
			
			// Si es el primero de la ronda, o ha sacado un número mayor que el máximo actual, actualizamos datos
			if (resultados[indiceActual] > maximoRondaActual) {
				maximoRondaActual = resultados[indiceActual];
				indiceMaximo = indiceActual;
				// En este caso, ya no habría empate porque el jugador ha obtenido un número más alto que el anterior máximo
				empate = false;
			}
			else if (resultados[indiceActual] == maximoRondaActual) {
				// Si el número es igual, marcamos empate para que, en caso de que la ronda acabe así, se vuelva a tirar
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
	 * Muestra el menú de las posibles acciones y recoge la acción escogida por el usuario
	 * @return la acción seleccionada por el usuario
	 */
	private int muestraMenuAcciones() {
		System.out.println("Acciones posibles");
		System.out.println("1. Comprar carta de nave");
		System.out.println("2. Comprar carta de construcción");
		System.out.println("3. Coger carta de materia prima");
		System.out.println("4. Construir");
		System.out.println("5. Mover una nave de un planeta a otro");
		System.out.println("6. Atacar");
		System.out.println("7. Mejorar una nave");
		System.out.println("8. Reparar");
		System.out.println("9. Pasar turno");
		
		return UserDataCollector.getEnteroMinMax("¿Qué quieres hacer?", 1, 9);
	}

}
