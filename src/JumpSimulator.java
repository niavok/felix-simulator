import java.util.ArrayList;
import java.util.List;

public class JumpSimulator {

	List<JumpState> states = new ArrayList<JumpState>();
	private JumpState currentState;
	private final JumpConfig config;

	public JumpSimulator(JumpConfig config) {
		this.config = config;
	}

	/**
	 * Perform jump
	 */
	public void jump() {

		currentState = new JumpState(config.initialHeight, 0);
		states.add(currentState);
		while (currentState.height > 0) {
			currentState = step(currentState);
			states.add(currentState);
		}

	}

	public JumpResult getJumpResult() {
		return new JumpResult(states, config);
	}

	private JumpState step(JumpState initialState) {

		// For acceleration and speed Z axis is inverted (positive acceleration and speed lead to a fall)
		
		double weight = config.mass * World.gravity(initialState.height);
		
		double airResistance = config.dragCoefficient * World.airDensity(initialState.height) * config.section * initialState.speed * initialState.speed / 2;
		
		double forceSum = weight - airResistance;
		
		double acceleration = forceSum / config.mass;

		double speed = initialState.speed + acceleration * config.stepDuration;

		double height = initialState.height - speed * config.stepDuration;

		JumpState finalState = new JumpState(height, speed);

		return finalState;
	}

}
