package spaceConquerors;

public class Mina extends Construccion {
	
	private static final int PERSONAS_ASIGNADAS_MINA = 10;
	private static final int PRECIO_CARTA_MINA = 1;
	
	private TMateriales material;
	private int cantidadExtraidaTurno;

	/**
	 * Constructor de una carta de mina
	 * @param nombre
	 * @param material
	 * @throws InvalidValueException
	 */
	public Mina(String nombre, String material, int cantidadExtraidaTurno) throws InvalidValueException {
		super(nombre, Mina.PRECIO_CARTA_MINA, Mina.PERSONAS_ASIGNADAS_MINA);
		
		try {
			this.material = TMateriales.valueOf(material);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidValueException("El material que pretendes minar no existe");
		}
		
		// En el hipotético caso de que nos pasen un número negativo a minar
		if (cantidadExtraidaTurno <= 0) {
			throw new InvalidValueException("No puedes minar 0 unidades o menos");
		}
		
		this.cantidadExtraidaTurno = cantidadExtraidaTurno;
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
	
	

}
