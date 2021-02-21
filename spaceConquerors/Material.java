package spaceConquerors;

public class Material extends Carta {
	
	private static final int PRECIO_CARTA_MATERIALES = 0; 
	
	private TMateriales tipoMaterial;

	/**
	 * Construye el objeto de la carta de Material
	 * @param nombre El nombre de la carta
	 * @param tipoMaterial El tipo de material de la carta
	 * @throws InvalidValueException Si el nombre del material no es válido
	 */
	public Material(String nombre, String tipoMaterial) throws InvalidValueException {
		super(nombre, PRECIO_CARTA_MATERIALES);
		
		try {
			this.tipoMaterial = TMateriales.valueOf(tipoMaterial.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new InvalidValueException("Debes introducir un material válido");
		}
	}

	/**
	 * @return el tipo de material de la carta
	 */
	public TMateriales gettMaterial() {
		return tipoMaterial;
	}
	
	

}
