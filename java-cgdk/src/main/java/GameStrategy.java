import model.Move;

public interface GameStrategy {
	
    void move(final Features features, Move move);
	boolean valid(final Features featues);
}
