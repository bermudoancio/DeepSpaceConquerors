package spaceConquerors;

public class NaveAtaque extends Nave implements IAtacador {
	
	private static final int PODER_ATAQUE_DEFAULT = 3;
	private static final int PRECIO_CARTA_NAVE_ATAQUE = 5;
	private static final int PUNTOS_DEFENSA_INICIAL_ATAQUE = 20;
	
	private int poderAtaque;

	/**
	 * Constructor de una carta de nave de ataque. Tan solo recoge el nombre, todos 
	 * los demás datos son por defecto
	 * @param nombre Nombre de la nave
	 * @throws InvalidValueException Si el nombre no es válido
	 */
	public NaveAtaque(String nombre) throws InvalidValueException {
		super(nombre, NaveAtaque.PRECIO_CARTA_NAVE_ATAQUE, NaveAtaque.PUNTOS_DEFENSA_INICIAL_ATAQUE);
		
		this.poderAtaque = NaveAtaque.PODER_ATAQUE_DEFAULT;
	}

	@Override
	public void mejorar() {
		this.poderAtaque += NaveAtaque.PODER_ATAQUE_DEFAULT;

	}

	@Override
	public void atacar(IAtacable objetivo) throws InvalidValueException, DestructionException, JuegoException {
		if (objetivo == null) {
			throw new InvalidValueException("El objetivo no existe");
		}
		
		objetivo.serAtacado(this.poderAtaque);		
	}
	
	@Override
	public String toString() {
		return super.toString() + ". Poder de ataque: " + this.poderAtaque;
	}

}
