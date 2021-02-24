package spaceConquerors;

public class Mina extends Construccion {
	
	public static final int PERSONAS_ASIGNADAS_MINA = 10;
	public static final int PRECIO_CARTA_MINA = 1;
	
	private TMateriales material;
	private int cantidadExtraidaTurno;

	/**
	 * Constructor de una carta de mina
	 * @param nombre El nombre de la carta. Se recomiendo poner solo Mina, o Mina y algo detrás
	 * @throws InvalidValueException Si las personas asignadas a la mina son 0 o menor, o el precio de la carta es un número negativo
	 */
	public Mina(String nombre) throws InvalidValueException {
		super(nombre, Mina.PRECIO_CARTA_MINA, Mina.PERSONAS_ASIGNADAS_MINA);
	}

	/**
	 * @return el material que mina
	 */
	public TMateriales getMaterial() {
		return material;
	}

	/**
	 * @return la cantidad extraída del material en cada turno
	 */
	public int getCantidadExtraidaTurno() {
		return cantidadExtraidaTurno;
	}

	/**
	 * @param material material de la carta
	 * @throws InvalidValueException si el material no existe
	 */
	public void setMaterial(String material) throws InvalidValueException {
		try {
			this.material = TMateriales.valueOf(material.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new InvalidValueException("El material que pretendes minar no existe");
		}
	}

	/**
	 * @param cantidadExtraidaTurno la cantidad de material que extrae la mina en cada turno
	 * @throws InvalidValueException Si la cantidad a minar es 0 ó negativa
	 */
	public void setCantidadExtraidaTurno(int cantidadExtraidaTurno) throws InvalidValueException {
		// En el hipotético caso de que nos pasen un número negativo a minar
		if (cantidadExtraidaTurno <= 0) {
			throw new InvalidValueException("No puedes minar 0 unidades o menos");
		}
		
		this.cantidadExtraidaTurno = cantidadExtraidaTurno;
	}
	
	

}
