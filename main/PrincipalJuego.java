package main;

import java.util.Arrays;

import spaceConquerors.*;
import utils.UserDataCollector;

public class PrincipalJuego {
	
	public static final int NUMERO_RONDAS = 9;
	public static final int OPCION_PASAR_TURNO = 12;
	
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
	
	/**
	 * El m�todo principal que llevar� la partida. 
	 * 	- Asignar� los jugadores
	 *  - Les dar� un planeta aleatorio
	 *  - Decidir� qui�n empieza
	 *  - Controlar� las rondas
	 *  - Controlar� si el juego ha terminado
	 *  - Controlar� qui�n tiene que tirar en cada turno
	 *  - Mostrar� el men� de acciones
	 *  - Recoger� la acci�n escogida y llamar� al m�todo correspondiente
	 */
	public void comenzarPartida() {
		// Asignamos los jugadores
		this.asignarJugadores();
		// Asignamos aleatoriamente un planeta a cada jugador
		this.t.asignacionInicial();
		Jugador[] jugadores = this.t.getJugadores();
		
		// Turno de decidir qui�n empieza
		int indiceJugadorEmpieza = this.decideQuienEmpieza();
		int indiceJugador = indiceJugadorEmpieza;
		
		// Esta variable marcar� si un jugador ha ganado
		boolean juegoTerminado = false;
		
		// Mostramos la informaci�n del tablero
		System.out.println(this.t);
		
		while (ronda <= PrincipalJuego.NUMERO_RONDAS && !juegoTerminado) {
			// Cada vuelta del bucle ser� una ronda
			System.out.println();
			System.out.println();
			System.out.println("#########################################################");
			System.out.println("#########################################################");
			System.out.println("Ronda " + this.ronda + " de " + PrincipalJuego.NUMERO_RONDAS);
			System.out.println("#########################################################");
			System.out.println("#########################################################");
			System.out.println();
			System.out.println();
			
			
			/*
			 * Se considerar� que una ronda se acaba cuando todos los jugadores no eliminados
			 * hayan hecho sus acciones
			 */
			boolean finRonda = false;
			
			while (!finRonda && !juegoTerminado) {
				if (!jugadores[indiceJugador].isEliminado()) {
					System.out.println();
					System.out.println();
					System.out.println("#########################################################");
					System.out.println();
					System.out.println();
					System.out.println("Turno del jugador " + jugadores[indiceJugador].getNombre());
					
					int accionesRealizadas = 1;
					int accionElegida = 0;
					
					while (accionesRealizadas <= 2 && accionElegida != PrincipalJuego.OPCION_PASAR_TURNO && !juegoTerminado) {
						System.out.println();
						System.out.println();
						System.out.println("Jugada " + accionesRealizadas + " de 2");
						accionElegida = this.muestraMenuAcciones();
						
						try {
							switch(accionElegida) {
							case 1: // Comprar carta de nave
								this.comprarCartaNave(jugadores[indiceJugador]);
								break;
							case 2: // Comprar carta de construcci�n
								this.comprarCartaConstruccion(jugadores[indiceJugador]);								
								break;
							case 3: // Coger carta de materia prima
								this.cogerCartaMateriaPrima(jugadores[indiceJugador]);
								break;
							case 4: // Construir
								break;
							case 5: // Mover nave de un planeta a otro
								this.moverNave(jugadores[indiceJugador]);
								break;
							case 6: // Atacar
								this.atacar(jugadores[indiceJugador]);
								break;
							case 7: // Transportar carga
								break;
							case 8: // Transportar personas
								break;
							case 9: // Mejorar una nave
								break;
							case 10: // Reparar 
								break;
							case 11: // Mostrar planetas
								System.out.println(this.t);
								break;
							case OPCION_PASAR_TURNO:
								break;
							}
						}
						catch (CancelarException e) {
							// La acci�n no se ha realizado.
							if (e.getMessage() != null && !e.getMessage().isBlank()) {
								System.out.println(e.getMessage());
							}
							// No le contaremos la acci�n
							continue;
						}
						
						juegoTerminado = this.checkJuegoTerminado();
						
						accionesRealizadas++;
					}
					
					// Imprimimos la informaci�n del tablero
					System.out.println(this.t);
					
				}
				
				// Pasamos al siguiente jugador
				indiceJugador = ++indiceJugador % jugadores.length;
				
				if (indiceJugador == indiceJugadorEmpieza) {
					finRonda = true;
				}
			}
			
			// Actualizamos las puntuaciones tras la ronda
			System.out.println("Esta es la puntuaci�n actual");
			this.actualizaPuntuaciones();

			/*
			 * Mostramos los jugadores ordenados seg�n su puntuaci�n (orden natural)
			 * Tenemos que clonar el array, pues si no lo hacemos, referenciar�n 
			 * al mismo objeto, y se ordenar�n los dos. No queremos eso pues si no 
			 * se alterar�a el orden de los jugadores en la segunda ronda.
			 */
			Jugador[] jugadoresOrdenados = jugadores.clone();
			Arrays.sort(jugadoresOrdenados);
			System.out.println(Arrays.toString(jugadoresOrdenados));
			
			// TODO: minar, nacer personas
			try {
				this.accionesDeFinDeRonda();
			}
			catch (JuegoException e) {
				System.out.println(e.getMessage());
			}
			
			// Avanzamos de ronda
			ronda++;
		}
		
		System.out.println("Fin del juego");
		System.out.println("Esta es la clasificaci�n final:");
		
		// Podemos ordenar ya el array original, pues no volver� a usarse
		Arrays.sort(jugadores);
		System.out.println(Arrays.toString(jugadores));
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
	
	/**
	 * Pregunta cu�ntos jugadores van a jugar y devuelve el n�mero introducido 
	 * por el usuario, entre el m�nimo y el m�ximo aceptados
	 * @return el n�mero de jugadores que jugar�n
	 */
	private static int preguntarJugadores() {
		return UserDataCollector.getEnteroMinMax("�Cu�ntos jugadores van a jugar?", Tablero.MIN_JUGADORES, Tablero.MAX_JUGADORES);
	}
	
	/**
	 * Pregunta los datos necesarios para crear un jugador (b�sicamente el nombre) y devuelve un nuevo jugador con ese nombre
	 * @param num el �ndice que ocupar�a este jugador  (jugador 1, jugador 2...
	 * @return un objeto jugador con el nombre introducido
	 */
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
	
	
	/**
	 * Decide qu� jugador empezar� a jugar mediante lanzamientos de dados
	 * @return el �ndice que ocupa el jugador que empieza en el array de jugadores
	 */
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
	 * @return el tablero de juego
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
		System.out.println("7. Transportar carga");
		System.out.println("8. Transportar personas");
		System.out.println("9. Mejorar una nave");
		System.out.println("10. Reparar");
		System.out.println("11. Mostrar planetas");
		System.out.println(PrincipalJuego.OPCION_PASAR_TURNO + ". Pasar turno");
		
		return UserDataCollector.getEnteroMinMax("�Qu� quieres hacer?", 1, PrincipalJuego.OPCION_PASAR_TURNO);
	}
	
	/**
	 * Comprueba si un jugador ha conquistado todos los planetas
	 * @return si alg�n jugador ha conquistado todos los planetas
	 */
	private boolean checkJuegoTerminado() {
		/*
		 * Este m�todo comprobar� si un jugador ha ganado el juego.
		 * Para ello, ha tenido que conquistar todos los planetas.
		 */
		Jugador j = null;
		boolean unicoJugador = true;
		
		for (int i = 0; i < this.t.getPlanetas().length && unicoJugador; i++) {
			
			if (j == null) {
				// j todav�a no tiene valor, le ponemos el del primer planeta
				j = this.t.getPlanetas()[i].getConquistador();
			}
			
			if (this.t.getPlanetas()[i].getConquistador() == null) {
				// El planeta no est� conquistado, seguimos buscando otros jugadores
				continue;
			}
			else {
				// si el jugador no es el conquistador de este planeta
				if (!this.t.getPlanetas()[i].getConquistador().equals(j)) {
					unicoJugador = false;
				}
			}
		}
		
		return unicoJugador;
	}
	
	/*
	 * ###########################################################
	 * #########################ACCIONES##########################
	 * ###########################################################
	 */
	
	/**
	 * Realiza la acci�n de comprar una carta de nave. Tiene en cuenta el 
	 * oro del usuario antes de hacer nada m�s. Si escoge una nave de carga 
	 * o transporte, se le pedir� que tire el dado para decidir la capacidad.
	 * Por �ltimo, se mostrar�n sus planetas y se pedir� que lo asigne a uno
	 * de ellos. 
	 * @param j El jugador que compra la carta
	 * @throws CancelarException Si el usuario cancela o no tiene dinero suficiente
	 */
	private void comprarCartaNave(Jugador j) throws CancelarException {
		// Lo primero ser� comprobar si el jugador tiene oro suficiente
		// Vamos a mostrar al usuario el saldo que tiene
		System.out.println("Ahora mismo tienes " + j.getUnidadesOro() + " unidades de oro disponibles");
		this.mostrarCartasNaveVisibles();
		int cartaEscogida = UserDataCollector.getEnteroMinMax("Elige el n�mero de la carta (0 para cancelar)", 0, this.t.getNavesVenta().length);
		if (cartaEscogida == 0) {
			throw new CancelarException();
		}
		else {
			// El jugador ve los �ndices con base 1. Lo devolvemos a base 0
			cartaEscogida--;
			if (j.getUnidadesOro() < this.t.getNavesVenta()[cartaEscogida].getPrecio()) {
				throw new CancelarException("No tienes suficiente oro para comprar la carta");
			}
			else {
				// Compra la carta: restamos el dinero y preguntamos a qu� planeta asignarla
				Nave nave = null;
				Planeta p = null;
				
				try {
					nave = this.t.getNavesVenta()[cartaEscogida];
										
					/*
					 * Ahora tenemos que asignar la capacidad de carga o pasajeros
					 * si la nave es de transporte o carga
					 */
					if (nave instanceof NaveCarga) {
						UserDataCollector.getTecla("Ahora decidiremos la capacidad de carga. Tira el dado pulsando enter");
						int resultadoLanzarDado = this.t.lanzarDado('A');
						System.out.println("La capacidad de carga de tu nave ser� de " + resultadoLanzarDado);
						((NaveCarga) nave).setCapacidadCarga(resultadoLanzarDado);
					}
					else if (nave instanceof NaveTransporte) {
						UserDataCollector.getTecla("Ahora decidiremos la capacidad de pasajeros. Tira el dado pulsando enter");
						int resultadoLanzarDado = this.t.lanzarDado('C');
						System.out.println("La capacidad de pasajeros de tu nave ser� de " + resultadoLanzarDado);
						((NaveTransporte) nave).setCapacidad(resultadoLanzarDado);
					}
					// Las naves de ataque no tienen elementos aleatorios
					
					// Preguntamos a qu� planeta la mandamos orbitar
					p = this.seleccionaPlanetaPropio("�A qu� planeta quieres asignar la nave?", j);
					
					// Asignamos la nave al nuevo planeta
					p.addNaveOrbitando(nave);
					
					j.pagarOro(this.t.getNavesVenta()[cartaEscogida].getPrecio());
					t.comprarCartaNave(nave);
					// Asignamos la carta al jugador
					nave.asignarAJugador(j);
					
					// Avisamos al jugador de que todo ha ido OK
					System.out.println("Tu nueva nave ya est� orbitando el planeta " + p.getNombre());
				} 
				catch (JuegoException e) {
					throw new CancelarException(e.getMessage());
				} 
				catch (InvalidValueException e) {
					// En este catch entrar�a solo si el resultado de lanzar el dado fuese 0 o negativo
					throw new CancelarException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Realiza la acci�n de comprar una carta de construcci�n. Tiene en cuenta si el 
	 * jugador tiene dinero o no para comprarla. Si escoge una mina, se le pedir� que
	 * decida de qu� material quiere la mina. Si escoge un material distinto al oro, 
	 * se le pedir� que tire el dado para decidir la cantidad de materia que minar�. 
	 * Si escoge un escudo, se le pedir� que tire el dado para ver cu�ntos puntos
	 * extra se a�aden a los puntos de defensa.
	 * Por �ltimo, se a�adir� al mazo de cartas de construcci�n del jugador. 
	 * @param j El jugador que compra la carta
	 * @throws CancelarException Si el usuario cancela o no tiene dinero suficiente
	 */
	private void comprarCartaConstruccion(Jugador j) throws CancelarException {
		// Lo primero ser� comprobar si el jugador tiene oro suficiente
		this.mostrarCartasConstruccion();
		int cartaEscogida = UserDataCollector.getEnteroMinMax("Elige el n�mero de la carta (0 para cancelar)", 0, 2);
		if (cartaEscogida == 0) {
			throw new CancelarException();
		}
		else {
			Construccion c = null;
			int precioCarta = 0;

			try {
				// Ponemos el precio de la carta acorde a la construcci�n
				switch(cartaEscogida) {
				case 1: //Mina
					precioCarta = Mina.PRECIO_CARTA_MINA;
					c = new Mina("Mina");
					break;
				case 2: //Escudo protector
					precioCarta = EscudoProtector.PRECIO_CARTA_ESCUDO_PROTECTOR;
					c = new EscudoProtector("Escudo protector");
					break;
				}
			}
			catch (InvalidValueException e) {
				throw new CancelarException(e.getMessage());
			}
			
			// Si no tiene dinero, cancelamos la acci�n
			if (j.getUnidadesOro() < precioCarta) {
				throw new CancelarException("No tienes suficiente oro para comprar la carta");
			}
			else {
				if (c instanceof Mina) {				
					
					String materia = UserDataCollector.getStringDeOpciones("Selecciona una materia prima", TMateriales.getValuesAsString());
					try {
						((Mina) c).setMaterial(materia);
					} 
					catch (InvalidValueException e) {
						throw new CancelarException(e.getMessage());
					}
					
					int cantidadAMinar = 2;
					if (!materia.equalsIgnoreCase("Oro")) {
						// Si no minamos oro, lanzamos el dado para ver cu�nto minamos
						UserDataCollector.getTecla("Ahora decidiremos la cantidad que minar� en cada turno. Tira el dado pulsando enter");
						cantidadAMinar = this.t.lanzarDado('A');
						try {
							((Mina) c).setCantidadExtraidaTurno(cantidadAMinar);
						} 
						catch (InvalidValueException e) {
							// No entrar� por aqu� porque los dados tienen n�meros positivos
							throw new CancelarException(e.getMessage());
						}
					}					
					
					System.out.println("Una vez que la asignes a un planeta, la mina va a minar en cada turno " + cantidadAMinar + " unidades de " + materia);
				}
				else if (c instanceof EscudoProtector) {
					UserDataCollector.getTecla("Vamos a ver cu�ntos puntos extra de escudo sumamos. Tira el dado pulsando enter");
					int puntosDefensaExtra = this.t.lanzarDado('A');
					try {
						((EscudoProtector) c).setPuntosDefensa(puntosDefensaExtra);
					} 
					catch (InvalidValueException e) {
						// No entrar� por aqu� porque los dados tienen n�meros positivos
						throw new CancelarException(e.getMessage());
					}
					
					System.out.println("El escudo tendr� " + ((EscudoProtector) c).getPuntosDefensa() + " puntos de defensa en total. Pero antes debes asignarlo a un planeta");
				}
				
				// Sea una mina o un escudo, a�adimos la carta al mazo del usuario y la pagamos
				try {
					j.pagarOro(c.getPrecio());
					j.addCartaConstruccion(c);
				} 
				catch (JuegoException e) {
					throw new CancelarException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * Coge una carta de materia prima, que el tablero generar� al azar. Si la carta es
	 * de oro, la a�adir� al "monedero" del usuario. Si es de otra materia prima, preguntar�
	 * a qu� planeta (de los suyos) a�adirla, y sumar� 1 unidad a las reservas de dicha 
	 * materia en ese planeta.
	 * @param jugador el jugador que coge la carta
	 * @throws CancelarException Si el jugador cancela
	 */
	private void cogerCartaMateriaPrima(Jugador jugador) throws CancelarException {
		Material cartaMaterial = t.cogerCartaMaterial();
		
		/*
		 * Hay que hacer cosas diferentes dependiendo del tipo de material
		 * Si es oro, directamente sumamos las unidades que sea (por defecto 1)
		 * a la cantidad de oro del jugador.
		 * Si es otro material, preguntamos el planeta al que quiere enviar 
		 * dicho material.
		 */
		System.out.println("Has obtenido una carta de " + cartaMaterial.gettMaterial().toString());
		
		if (cartaMaterial.gettMaterial().equals(TMateriales.ORO)) {
			// Si es oro, lo sumamos al "monedero" del jugador
			try {
				jugador.addOro(Material.CANTIDAD_MATERIALES_CARTA);
			}
			catch (InvalidValueException e) {
				throw new CancelarException(e.getMessage());
			}
		}
		else {
			Planeta planetaSeleccionado = this.seleccionaPlanetaPropio("Selecciona el �ndice del planeta al que quieres mandar la materia prima (0 para cancelar):", jugador);
			
			try {
				planetaSeleccionado.addUnidadesDeMateriaPrima(cartaMaterial.gettMaterial(), Material.CANTIDAD_MATERIALES_CARTA);
			} 
			catch (InvalidValueException e) {
				throw new CancelarException(e.getMessage());
			}
			
		}
		
	}
	
	/**
	 * Lo primero que haremos ser� mostrar un listado de sus naves.
	 * Si no tiene, directamente mostramos un mensaje de error y cancelamos.
	 * Si las tiene, las mostraremos numeradas, junto al nombre del planeta
	 * en el que est�n orbitando. Se a�adir� una opci�n para cancelar.
	 * Una vez seleccionada, se mostrar� un listado de todos los dem�s planetas,
	 * junto con una opci�n para cancelar.
	 * Una vez que el jugador seleccione el planeta destino, moveremos
	 * la nave y la eliminaremos del planeta actual
	 * @param jugador el jugador que realiza la acci�n
	 * @throws CancelarException si el usuario cancela o no tiene naves
	 */
	private void moverNave(Jugador jugador) throws CancelarException {
		int contadorNaves = 0;
		for (Planeta p: t.getPlanetas()) {
			
			for (Nave n: p.getNavesOrbitando()) {
				if (n.getJugador().equals(jugador)) {
					// la nave pertenece al jugador. La contamos
					contadorNaves++;
				}
			}
		}
		
		if (contadorNaves == 0) {
			throw new CancelarException("El jugador no tiene naves de ataque disponibles");
		}
		
		
		
		
		// Vamos a mostrar un mensaje de cabecera para el listado de sus naves de ataque
		System.out.println();
		System.out.println("Estas son tus naves: ");
		
		
		Nave[] navesDeJugador = new Nave[contadorNaves];
		/*
		 * Para no tener que recorrer una tercera vez, guardaremos los planetas al que 
		 * orbitan las naves en un array con el mismo �ndice
		 */
		Planeta[] planetasQueOrbitanLasNaves = new Planeta[contadorNaves];
		/*
		 * Aunque ineficiente, debido a que utilizamos arrays, tenemos que volver
		 * a recorrer todos los planetas para a�adir ahora las naves al array 
		 */
		contadorNaves = 0;
		for (Planeta p: t.getPlanetas()) {
			
			for (Nave n: p.getNavesOrbitando()) {
				if (n.getJugador().equals(jugador)) {
					// la nave pertenece al jugador. La contamos
					navesDeJugador[contadorNaves] = n;
					planetasQueOrbitanLasNaves[contadorNaves] = p;
					contadorNaves++;
					
					// Adem�s, la mostramos para seleccionar
					StringBuilder sb = new StringBuilder();
					sb.append(contadorNaves)
					.append(": Puntos de defensa: ").append(n.getPuntosDefensa());
					
					
					if (n instanceof NaveAtaque) {
						sb.append(". Poder de ataque: ").append(((NaveAtaque) n).getPoderAtaque());
					}
					else if (n instanceof NaveCarga) {
						sb.append(". Capacidad: ").append(((NaveCarga) n).getCapacidadCarga()).append(" unidades de materia");
					}
					else if (n instanceof NaveTransporte) {
						sb.append(". Capacidad: ").append(((NaveTransporte) n).getCapacidad()).append(" pasajeros");
					}
					
					sb.append(". Orbita el planeta ").append(p.getNombre());
					
					// Mostramos toda la informaci�n de la nave
					System.out.println(sb.toString());
					
				}
			}
		}
		
		// Ahora le pedimos que seleccione una de las naves
		int indiceNaveSeleccionada = UserDataCollector.getEnteroMinMax("Selecciona una nave (0 para cancelar)", 0, navesDeJugador.length);
		if (indiceNaveSeleccionada == 0) {
			throw new CancelarException();
		}
		
		// El usuario ve los �ndices basados en 1, nosotros restamos 1 para basarlos en �ndice 0
		Nave naveSeleccionada = navesDeJugador[indiceNaveSeleccionada - 1];
		Planeta planetaQueOrbita = planetasQueOrbitanLasNaves[indiceNaveSeleccionada - 1];
		
		// Ahora mostramos los planetas (excepto el actual de la nave)
		// Si hay X planetas, tenemos X - 1 posibles destinos, ya que el actual no cuenta
		Planeta[] posiblesDestinos = new Planeta[this.t.getPlanetas().length - 1];
		int contador = 0;
		for (Planeta p: this.t.getPlanetas()) {
			if (p != planetaQueOrbita) {
				posiblesDestinos[contador++] = p;
				System.out.println(contador + ": " + p);
			}
		}
		
		int indicePlanetaSeleccionado = UserDataCollector.getEnteroMinMax("Selecciona un destino (0 para cancelar)", 0, posiblesDestinos.length);
		if (indicePlanetaSeleccionado == 0) {
			throw new CancelarException();
		}
		
		/*
		 * Ya tenemos todo lo necesario: la nave, y el destino. Por tanto, ahora
		 * desligamos la nave de su antiguo planeta y la a�adimos al nuevo
		 */
		try {
			planetaQueOrbita.deleteNaveOrbitando(naveSeleccionada);
			// El usuario ve los �ndices basados en 1, nosotros restamos 1 para basarlos en �ndice 0
			posiblesDestinos[indicePlanetaSeleccionado - 1].addNaveOrbitando(naveSeleccionada);
			
			System.out.println("La nave ya est� orbitando el nuevo planeta " + posiblesDestinos[indicePlanetaSeleccionado - 1].getNombre());
		} 
		catch (JuegoException e) {
			throw new CancelarException(e.getMessage());
		}
		
	}
	
	/**
	 * Lo primero que haremos ser� mostrar un listado de sus naves de ataque.
	 * Si no tiene, directamente mostramos un mensaje de error y cancelamos.
	 * Si las tiene, las mostraremos numeradas, junto al nombre del planeta
	 * en el que est�n orbitando. Se a�adir� una opci�n para cancelar.
	 * Una vez seleccionada, se mostrar�n todas las otras naves que orbitan
	 * el planeta y que no son suyas y, por tanto, son atacables. Adem�s,
	 * si el planeta est� conquistado por alguien que no sea el propio jugador,
	 * se mostrar� una opci�n para atacarlo. Tanto las naves como el planeta,
	 * al ser listados, mostrar�n los puntos de defensa restantes. Aqu� 
	 * volveremos a introducir una opci�n para cancelar.
	 * Una vez seleccionado el objetivo, mostraremos los da�os ocasionados.
	 * Si el planeta hubiese sido conquistado, lo mostraremos por pantalla.
	 * Si a ra�z de esta �ltima opci�n, un jugador queda eliminado, tambi�n
	 * lo mostraremos por pantalla.
	 * @param jugador el jugador que realiza la acci�n
	 * @throws CancelarException si el jugador cancela la acci�n, o no tiene naves de ataque
	 */
	private void atacar(Jugador jugador) throws CancelarException {
		/*
		 * Lo primero que haremos ser� mostrar un listado de sus naves de ataque.
		 * Si no tiene, directamente mostramos un mensaje de error y cancelamos.
		 * Si las tiene, las mostraremos numeradas, junto al nombre del planeta
		 * en el que est�n orbitando. Se a�adir� una opci�n para cancelar.
		 * Una vez seleccionada, se mostrar�n todas las otras naves que orbitan
		 * el planeta y que no son suyas y, por tanto, son atacables. Adem�s,
		 * si el planeta est� conquistado por alguien que no sea el propio jugador,
		 * se mostrar� una opci�n para atacarlo. Tanto las naves como el planeta,
		 * al ser listados, mostrar�n los puntos de defensa restantes. Aqu� 
		 * volveremos a introducir una opci�n para cancelar.
		 * Una vez seleccionado el objetivo, mostraremos los da�os ocasionados.
		 * Si el planeta hubiese sido conquistado, lo mostraremos por pantalla.
		 * Si a ra�z de esta �ltima opci�n, un jugador queda eliminado, tambi�n
		 * lo mostraremos por pantalla.
		 */
		/*
		 * Vamos a mostrar las naves del usuario. Para ello, inspeccionamos todos
		 * los planetas y contamos cu�ntas naves hay, para as� crear un array con
		 * el tama�o justo
		 */
		int contadorNaves = 0;
		for (Planeta p: t.getPlanetas()) {
			
			for (Nave n: p.getNavesOrbitando()) {
				if (n.getJugador().equals(jugador)) {
					if (n instanceof NaveAtaque) {
						// la nave pertenece al jugador. La contamos
						contadorNaves++;
					}
				}
			}
		}
		
		if (contadorNaves == 0) {
			throw new CancelarException("El jugador no tiene naves de ataque disponibles");
		}
		
		// Vamos a mostrar un mensaje de cabecera para el listado de sus naves de ataque
		System.out.println();
		System.out.println("Estas son tus naves de ataque: ");
		
		
		NaveAtaque[] navesAtaqueDeJugador = new NaveAtaque[contadorNaves];
		/*
		 * Para no tener que recorrer una tercera vez, guardaremos los planetas al que 
		 * orbitan las naves en un array con el mismo �ndice
		 */
		Planeta[] planetasQueOrbitanLasNaves = new Planeta[contadorNaves];
		/*
		 * Aunque ineficiente, debido a que utilizamos arrays, tenemos que volver
		 * a recorrer todos los planetas para a�adir ahora las naves al array 
		 */
		contadorNaves = 0;
		for (Planeta p: t.getPlanetas()) {
			
			for (Nave n: p.getNavesOrbitando()) {
				if (n.getJugador().equals(jugador)) {
					if (n instanceof NaveAtaque) {
						// la nave pertenece al jugador. La contamos
						navesAtaqueDeJugador[contadorNaves] = (NaveAtaque) n;
						planetasQueOrbitanLasNaves[contadorNaves] = p;
						contadorNaves++;
						
						// Adem�s, la mostramos para seleccionar
						System.out.println(contadorNaves + ": Puntos de defensa: " + n.getPuntosDefensa() + ". Orbita el planeta " + p.getNombre());
					}
				}
			}
		}
		
		// Ahora le pedimos que seleccione una de las naves
		int indiceNaveSeleccionada = UserDataCollector.getEnteroMinMax("Selecciona una nave (0 para cancelar)", 0, navesAtaqueDeJugador.length);
		if (indiceNaveSeleccionada == 0) {
			throw new CancelarException();
		}
		
		// El usuario ve los �ndices basados en 1, nosotros restamos 1 para basarlos en �ndice 0
		NaveAtaque naveSeleccionada = navesAtaqueDeJugador[indiceNaveSeleccionada - 1];
		Planeta planetaQueOrbita = planetasQueOrbitanLasNaves[indiceNaveSeleccionada - 1];
		
		/*
		 * Lo siguiente es mostrar las dem�s naves que orbitan dicho planeta y que no
		 * pertenecen al jugador 
		 */
		int numeroNavesNoPertenecenAJugador = 0;
		for (Nave n: planetaQueOrbita.getNavesOrbitando()) {
			if (!n.getJugador().equals(jugador)) {
				// Esta nave no le pertenece al jugador
				numeroNavesNoPertenecenAJugador++;
			}
		}
		
		Nave[] navesQueNoPertenecenAlJugador = new Nave[numeroNavesNoPertenecenAJugador];
		
		numeroNavesNoPertenecenAJugador = 0;
		for (Nave n: planetaQueOrbita.getNavesOrbitando()) {
			if (!n.getJugador().equals(jugador)) {
				// A�adimos la nave a un array temporal
				navesQueNoPertenecenAlJugador[numeroNavesNoPertenecenAJugador++] = n;
				System.out.println(numeroNavesNoPertenecenAJugador + ": " + n.getPuntosDefensa() + " puntos de defensa restantes. Pertenece a: " + n.getJugador().getNombre());
			}
		}
		
		// Por �ltimo, si el planeta est� conquistado por un enemigo, lo mostramos tambi�n
		if (planetaQueOrbita.getConquistador() != null && !planetaQueOrbita.getConquistador().equals(jugador)) {
			StringBuilder sb = new StringBuilder();
			sb.append((++numeroNavesNoPertenecenAJugador)) // �ndice para seleccionarlo. Lo aumentamos para aclarar que hay un objetivo m�s
			.append(": ").append(planetaQueOrbita.getNombre()).append(". "); // Nombre del planeta
			
			if(planetaQueOrbita.getEscudo() == null) {
				sb.append("Sin escudo protector. ");
			}
			else {
				sb.append(planetaQueOrbita.getEscudo().getPuntosDefensa())
				.append(" puntos de escudo de defensa restantes. ");
			}
			
			sb.append("Pertenece a ")
			.append(planetaQueOrbita.getConquistador().getNombre());
			
			System.out.println(sb.toString());
		}
			
		// Ahora recogemos el objetivo de su ataque
		int indiceObjetivo = UserDataCollector.getEnteroMinMax("Introduce el n�mero del objetivo (0 para cancelar)", 0, numeroNavesNoPertenecenAJugador);
		if (indiceObjetivo == 0) {
			throw new CancelarException();
		}
		
		IAtacable objetivo;
		
		if (indiceObjetivo > navesQueNoPertenecenAlJugador.length) {
			/*
			 * Ha seleccionado el planeta. �Por qu� lo sabemos? Porque el
			 * array que contiene las naves atacables tiene navesQueNoPertenecenAlJugador.length
			 * tama�o, y el usuario ha seleccionado uno m�s. La �nica forma de que
			 * esto haya podido pasar es que el planeta tambi�n sea atacable y lo
			 * haya seleccionado. Si no, el m�todo getEnteroMinMax hubiese dado error
			 */
			objetivo = planetaQueOrbita;
		}
		else {
			// El objetivo es una nave
			// El usuario ve los �ndices basados en 1, nosotros restamos 1 para basarlos en �ndice 0
			objetivo = navesQueNoPertenecenAlJugador[--indiceObjetivo];
		}
		
		try {
			naveSeleccionada.atacar(objetivo);
			
			// La siguiente l�nea no se mostrar� si el objetivo se destruye, pues saltar� la excepci�n DestructionException
			System.out.println("El objetivo ahora tiene " + objetivo.getPuntosDefensa() + " puntos de defensa restantes");
		} 
		catch (InvalidValueException e) {
			// La nave no atacar� con un valor menor que 0. De todas formas:
			throw new CancelarException(e.getMessage());
		} 
		catch (DestructionException e) {
			System.out.println(e.getMessage());
			if (objetivo instanceof Nave) {
				try {
					planetaQueOrbita.deleteNaveOrbitando((Nave) objetivo);
				} 
				catch (JuegoException e1) {
					// La nave s� orbita el planeta. Este c�digo nunca se alcanzar�
				}
			}
			else {
				/*
				 * Si el objetivo no era una nave, era un planeta. Y si ha sido
				 * lanzada DestructionException, significa que el escudo ha sido
				 * destruido y por tanto el planeta conquistado
				 */
				// Guardamos el conquistador de este planeta por si tras la destrucci�n queda eliminado
				Jugador jugadorConquistadorPlaneta = planetaQueOrbita.getConquistador();
				planetaQueOrbita.conquistar(jugador);
				System.out.println("El planeta " + planetaQueOrbita.getNombre() + " ha sido conquistado por el jugador " + jugador.getNombre());
				
				// Tras perder el planeta, �est� el ex-conquistador del planeta eliminado?
				if (this.t.checkJugadorEliminado(jugadorConquistadorPlaneta)) {
					jugadorConquistadorPlaneta.eliminar();
					System.out.println("#########################################");
					System.out.println("#########################################");
					System.out.println();
					System.out.println("�OOOOOOHHHHHH! El jugador " + jugadorConquistadorPlaneta.getNombre() + " ha sido eliminado. �Buena suerte la pr�xima vez!");
					System.out.println();
					System.out.println("#########################################");
					System.out.println("#########################################");
				}
			}
		} 
		catch (JuegoException e) {
			/*
			 * No llegar� aqu� porque antes nos hemos asegurado que el planeta
			 * estaba conquistado por otro jugador. De todas formas:  
			 */
			throw new CancelarException(e.getMessage());
		}
	}
	
	
	/*
	 * ###########################################################
	 * ####################FIN  ACCIONES##########################
	 * ###########################################################
	 */
	
	/**
	 * Calcula la puntuaci�n actual de todos los jugadores
	 */
	public void actualizaPuntuaciones() {
		for (Jugador j: this.t.getJugadores()) {
			this.t.calculaPuntuacionDeJugador(j);
		}
	}
	
	/**
	 * Hace que aumente la poblaci�n en todos los planetas llamando al m�todo
	 * aumentarPoblacionTrasRonda de los mismos. Adem�s, en aquellos planetas
	 * que est�n conquistados, hace trabajar las minas para recaudar los materiales
	 * @throws JuegoException si ocurre un fallo al minar
	 */
	private void accionesDeFinDeRonda() throws JuegoException {
		for (Planeta p: this.t.getPlanetas()) {
			// En todos los planetas nacer�n nuevas personas
			p.aumentarPoblacionTrasRonda();
			if (p.getConquistador() != null) {
				/*
				 * Si el planeta est� conquistado, las minas que hubiese 
				 * a�adir�n los materiales a las reservas del planeta
				 */
				for (Mina m: p.getMinas()) {
					if (m != null) {
						try {
							p.addUnidadesDeMateriaPrima(m.getMaterial(), m.getCantidadExtraidaTurno());
						} 
						catch (InvalidValueException e) {
							// Si existe una excepci�n, es que lo hemos programado mal
							throw new JuegoException("Ha ocurrido un fallo al minar " + m.getCantidadExtraidaTurno() + " unidades de " + m.getMaterial().toString() + " en el planeta " + p.getNombre());
						}
					}
				}
			}
		}
		
	}
	
	
	/**
	 * Muestra las cartas de nave que hay en venta en este momento
	 */
	private void mostrarCartasNaveVisibles() {
		System.out.println("Estas son las naves a la venta ahora mismo");
		int i = 1;
		for (Nave n: this.t.getNavesVenta()) {
			System.out.println(i++ + ": " + n);
		}
	}
	
	/**
	 * Muestra las cartas de construcci�n
	 */
	private void mostrarCartasConstruccion() {
		System.out.println("Las cartas de construcci�n son las siguientes:");
		try {
			System.out.println("1. " + new Mina("Mina"));
			System.out.println("2. " + new EscudoProtector("Escudo protector"));
		} 
		catch (InvalidValueException e) {
			// El nombre lo hemos escrito nosotros, por lo que la excepci�n no saltar�
		}
		
	}
	
	/**
	 * Muestra al jugador una lista numerada con todos sus planetas conquistados.
	 * Muestra el mensaje pasado como par�metro y permite que seleccione el planeta
	 * que quiere. Se a�ade una opci�n para cancelar la acci�n
	 * @param mensaje Mensaje que se mostrar� al jugador
	 * @param jugador el jugador cuyos planetas se mostrar�n
	 * @return el planeta seleccionado
	 * @throws CancelarException si el usuario escoge la opci�n 0
	 */
	private Planeta seleccionaPlanetaPropio(String mensaje, Jugador jugador) throws CancelarException {
		Planeta planetaSeleccionado = null;
		
		System.out.println(); // L�nea en blanco para mejorar visibilidad
		System.out.println("Estos son tus planetas:");
		// Si no es oro, preguntaremos a qu� planeta quiere a�adirlo
		Planeta[] planetasDeJugador = t.getPlanetasDeJugador(jugador);
		
		int i = 1;
		for (Planeta p: planetasDeJugador) {
			System.out.println(i++ + ": " + p);
		}
		
		int indicePlanetaSeleccionado = UserDataCollector.getEnteroMinMax(mensaje, 0, planetasDeJugador.length);
		
		if (indicePlanetaSeleccionado == 0) {
			// Si es coge la opci�n 0, cancelamos
			throw new CancelarException();
		}
		else {
			// El usuario ve los �ndices basados en 1, nosotros restamos 1 para basarlos en �ndice 0
			planetaSeleccionado = planetasDeJugador[--indicePlanetaSeleccionado];

		}
		
		return planetaSeleccionado;
	}

}
