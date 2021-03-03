package spaceConquerors;

public interface ITransportable {
	public void transportar(Planeta destino, int personas) throws InvalidValueException, JuegoException;
}
