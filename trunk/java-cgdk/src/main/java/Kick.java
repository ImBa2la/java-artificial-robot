import static java.lang.StrictMath.PI;
import static java.lang.StrictMath.abs;
import model.ActionType;
import model.Move;

public class Kick implements GameStrategy {
	
	private static final double STRIKE_ANGLE = 50.0D * PI / 180.0D;


	@Override
	public void move(final Features features, Move move) {
		double angleToNet = features.get(Features.FEATURE_IDS.PLAYER_NET_ANGLE);
        move.setTurn(angleToNet);
        if (abs(angleToNet) < STRIKE_ANGLE) {
            move.setAction(ActionType.STRIKE);
        }
	}

	@Override
	public boolean valid(final Features features) {
        return features.get(Features.FEATURE_IDS.PLAYER_OWN) > 1e-5;
	}

}
