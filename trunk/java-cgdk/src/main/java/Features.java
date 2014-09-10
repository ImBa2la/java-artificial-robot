import static java.lang.StrictMath.*;

import java.util.Arrays;

import model.Game;
import model.Hockeyist;
import model.HockeyistState;
import model.HockeyistType;
import model.Player;
import model.Unit;
import model.World;

public class Features {
	
	enum FEATURE_IDS {
		TEAM_OWN,
		PLAYER_OWN,
		PLAYER_DIST,
		PLAYER_NET_ANGLE,
		PLAYER_PUCK_ANGLE,
	};
	// TEAMMATE ANGLE
	// FREE TEAMMATE 
	
	
	
	
	double[] features = new double[FEATURE_IDS.values().length];
	
	public double get(FEATURE_IDS id) {
		return features[id.ordinal()];
	}
	
	public static final double EPS = 1e-5;
	
	public static double dFun(double x, double y, double sx, double sy, double vx, double vy, double s, double a) {
		return (x-sx) * (s * sin(a)-vy) - (y-sy) * (s * cos(a) - vx);
	}

	public static double derFun(double x, double y, double sx, double sy, double vx, double vy, double s, double a) {
		return (x-sx) * s * cos(a) + (y-sy) * s * sin(a);
		
	}
	
	public static double dT(double x, double y, double sx, double sy, double vx, double vy, double s, double a) {
		return (x - sx) / (s * cos(a) - vx);
	}
	
	public static double canonicalAngle(double x) {
        while (x > PI - EPS) {
            x -= 2.0D * PI;
        }
        while (x < -PI) {
            x += 2.0D * PI;
        }
        return x;
	}

	public static double solve2(double x, double y, double sx, double sy, double vx, double vy, double s, double a) {
		while (true) {
			double der = derFun(x, y, sx, sy, vx, vy, s, a);
			double d = dFun(x, y, sx, sy, vx, vy, s, a);
			if (abs(der) < EPS) {
				break;
			}
			if (abs(d / der) < EPS) {
				break;
			}
			a -= d / der;
		}
		return a;
	}
	
	public static double solve(double x, double y, double sx, double sy, double vx, double vy, double s, double a) {
		double ta = solve2(x, y, sx, sy, vx, vy, s, a);
		if (dT(x, y, sx, sy, vx, vy, s, ta) > EPS) {
			return ta;
		}
		ta = solve2(x, y, sx, sy, vx, vy, s, a + PI); 
		return ta;
	}
	
	
	double goToUnit(final Hockeyist self, final Unit unit, final World world, final Game game) {
		double x = unit.getX();
		double y = unit.getY();
		double sx = self.getX();
		double sy= self.getY();
		
		double vx = unit.getSpeedX();
		double vy = unit.getSpeedY();
		double svx = self.getSpeedX();
		double svy = self.getSpeedY();
		
		
		double ss = hypot(x - sx, y - sy);
		
		double speed = game.getHockeyistMaxSpeed();
		double a = atan2(y - sy, x - sx);
		
		if (ss > self.getRadius() + unit.getRadius() + EPS) { 
			double absoluteAngleTo = solve(x, y, sx, sy, vx, vy, speed, self.getAngle());
			double t = dT(x, y, sx, sy, vx, vy, speed, absoluteAngleTo);
			
			double nx = sx + t * speed * cos(absoluteAngleTo);
			double ny = sy + t * speed * sin(absoluteAngleTo);
			
			double bx = sx + 20 * speed * cos(self.getAngle());
			double by = sy + 20 * speed * sin(self.getAngle());
			
			if (bx < 0 || bx > world.getWidth()) {
				absoluteAngleTo = PI - self.getAngle(); 				
			} 
			if (by < 0 || by > world.getHeight()) {
				absoluteAngleTo = -self.getAngle(); 				
			} 
			if (t < EPS || nx < 0 || nx > world.getWidth() || ny < 0 || ny > world.getHeight()) {
				absoluteAngleTo = a;
			}
			double relativeAngleTo = absoluteAngleTo - self.getAngle();
			return relativeAngleTo;
		}
		return 0;
	}
	
	
	// TODO: calculate (x, y) for player name
	
	public void fillFeatures(Hockeyist self, World world, Game game) {
		Arrays.fill(features, 0.);
		
		
		features[FEATURE_IDS.TEAM_OWN.ordinal()] = world.getPuck().getOwnerPlayerId() == self.getPlayerId()? 1 : 0;
		features[FEATURE_IDS.PLAYER_OWN.ordinal()] = world.getPuck().getOwnerHockeyistId() == self.getId()? 1 : 0;
		
        Player opponentPlayer = world.getOpponentPlayer();
        double netX = 0.5D * (opponentPlayer.getNetBack() + opponentPlayer.getNetFront());
        double netY = 0.5D * (opponentPlayer.getNetBottom() + opponentPlayer.getNetTop());
        netY += (self.getY() < netY ? 0.5D : -0.5D) * game.getGoalNetHeight();
        features[FEATURE_IDS.PLAYER_DIST.ordinal()] = self.getDistanceTo(netX, netY);
        features[FEATURE_IDS.PLAYER_NET_ANGLE.ordinal()] = self.getAngleTo(netX, netY);
//        features[FEATURE_IDS.PLAYER_PUCK_ANGLE.ordinal()] = self.getAngleTo(world.getPuck());
        features[FEATURE_IDS.PLAYER_PUCK_ANGLE.ordinal()] = goToUnit(self, world.getPuck(), world, game);
        
        
        
//        Hockeyist hockeyist = getNearestOpponent();
	}
	
    private static Hockeyist getNearestOpponent(double x, double y, World world) {
        Hockeyist nearestOpponent = null;
        double nearestOpponentRange = 0.0D;

        for (Hockeyist hockeyist : world.getHockeyists()) {
            if (hockeyist.isTeammate() || hockeyist.getType() == HockeyistType.GOALIE
                    || hockeyist.getState() == HockeyistState.KNOCKED_DOWN
                    || hockeyist.getState() == HockeyistState.RESTING) {
                continue;
            }

            double opponentRange = hypot(x - hockeyist.getX(), y - hockeyist.getY());

            if (nearestOpponent == null || opponentRange < nearestOpponentRange) {
                nearestOpponent = hockeyist;
                nearestOpponentRange = opponentRange;
            }
        }

        return nearestOpponent;
    }

}
