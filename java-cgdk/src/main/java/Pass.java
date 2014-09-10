import model.ActionType;
import model.Move;

public class Pass implements GameStrategy {
	
	@Override
	public void move(final Features features, Move move) {
		// Pass to opened player
//        move.setAction(ActionType.STRIKE);
	}

	@Override
	public boolean valid(final Features features) {
        return features.get(Features.FEATURE_IDS.PLAYER_OWN) > 0;
	}

}
