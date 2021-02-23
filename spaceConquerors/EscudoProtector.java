package spaceConquerors;

public class EscudoProtector extends Construccion implements IAtacable, IReparable {
	
	public static final int PERSONAS_ASIGNADAS_ESCUDO_PROTECTOR = 15;
	public static final int PRECIO_CARTA_ESCUDO_PROTECTOR = 5;
	private static final int PUNTOS_DEFENSA_INICIAL_ESCUDO_PROTECTOR = 20;
	private static final int PUNTOS_REPARACION_ESCUDO_PROTECTOR = 15;
	
	private int puntosDefensa;

	/**
	 * Consructor de una carta de escudo. 
	 * @param nombre El nombre de la carta
	 * @param defensaExtra Los puntos extras que se sumarán a la protección por defecto del escudo
	 * @throws InvalidValueException Si los puntos de defensa extra son negativos
	 */
	public EscudoProtector(String nombre) throws InvalidValueException {
		super(nombre, EscudoProtector.PRECIO_CARTA_ESCUDO_PROTECTOR, EscudoProtector.PERSONAS_ASIGNADAS_ESCUDO_PROTECTOR);
	}

	@Override
	public void reparar() {
		this.puntosDefensa += EscudoProtector.PUNTOS_REPARACION_ESCUDO_PROTECTOR;		
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
			throw new DestructionException("El escudo " + super.getNombre() + " ha sido destruido");
		}
		
	}

	/**
	 * @param puntosDefensa los puntos de defensa extra al lanzar el dado
	 * @throws InvalidValueException 
	 */
	public void setPuntosDefensa(int puntosDefensa) throws InvalidValueException {
		if (puntosDefensa < 0) {
			throw new InvalidValueException("No puedes restar puntos a la defensa inicial del escudo");
		}
		
		this.puntosDefensa = PUNTOS_DEFENSA_INICIAL_ESCUDO_PROTECTOR + puntosDefensa;
	}
	
	

}
