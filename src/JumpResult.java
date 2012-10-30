import java.util.List;


public class JumpResult {

	private final List<JumpState> states;
	private final JumpConfig config;
	private JumpState maxSpeedState;
	private int maxSpeedStep;

	public JumpResult(List<JumpState> states, JumpConfig config) {
		this.states = states;
		this.config = config;
		maxSpeedState = null;
		maxSpeedStep = -1;
	}

	/**
	 * Jump duration, in seconds
	 * @return
	 */
	public double getDuration() {
		return states.size() * config.stepDuration;
	}

	public int getStepCount() {
		return states.size();
	}

	public JumpState getStep(int index) {
		return states.get(index);
	}

	public int getMaxSpeedStep() {
		if(maxSpeedState == null) {
			findMaxSpeed();
		}
		return maxSpeedStep;
	}

	public JumpState getMaxSpeedState() {
		if(maxSpeedState == null) {
			findMaxSpeed();
		}
		return maxSpeedState;
	}

	private void findMaxSpeed() {
		double maxSpeed = 0;
		int index = 0;
		for(JumpState state: states) {
			if(state.speed > maxSpeed) {
				maxSpeed = state.speed;
				maxSpeedState = state;
				maxSpeedStep = index;
			}
			index++;
		}
	}

	public double getStepTime(int stepIndex) {
		return (stepIndex+1) * config.stepDuration;
	}

}
