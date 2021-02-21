package spaceConquerors;

public abstract class Construccion extends Carta {
	
	private int personasAsignadas;

	public Construccion(String nombre, int precio, int personasAsignadas) throws InvalidValueException {
		super(nombre, precio);
		
		this.personasAsignadas = personasAsignadas;
	}
	
	public int getPersonasAsignadas() {
		return this.personasAsignadas;
	}

}
