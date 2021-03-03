package spaceConquerors;

public interface ITransportador {
	public void transportar(Planeta destino, TMateriales[] materiales, int[] cantidades) throws InvalidValueException, JuegoException;
}
