import java.util.ArrayList;
import java.util.List;

import model.*;
import static java.lang.StrictMath.*;

public final class MyStrategy implements Strategy {

	List<GameStrategy> strategies = new ArrayList<GameStrategy>();
	
	{
		strategies.add(new Kick());
		strategies.add(new TakePuck());
//		strategies.add(new Pass());
		
		// take puck
		// take nearest opposite against pass
		
		// go to net
		// go to front
		// fint
		// pass to team
		// pass to front
		// kick to net
		
		
		
	}
	
	
    @Override
    public void move(Hockeyist self, World world, Game game, Move move) {
    	Features features = new Features();
    	features.fillFeatures(self, world, game);
    	for (GameStrategy gs : strategies) {
    		if (gs.valid(features)) {
    			gs.move(features, move);
        		break;
    		}
    	}
    }
    
}
