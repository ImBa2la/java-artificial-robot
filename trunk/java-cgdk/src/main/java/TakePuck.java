import model.ActionType;
import model.Move;

public class TakePuck implements GameStrategy {

	@Override
	public void move(final Features features, Move move) {
        move.setSpeedUp(1.0D);
        move.setTurn(features.get(Features.FEATURE_IDS.PLAYER_PUCK_ANGLE));
        move.setAction(ActionType.TAKE_PUCK);
	}

	@Override
	public boolean valid(final Features features) {
		return features.get(Features.FEATURE_IDS.TEAM_OWN) < 1e-5;
	}

}
