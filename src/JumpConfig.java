
public class JumpConfig {

	public double initialHeight;
	public double stepDuration;
	public double mass;
	public double dragCoefficient;
	public double section;
	
	/**
	 * Jumper mass, in kilogram
	 * @param mass
	 */
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	/**
	 * Jump height, in meter
	 * @param height
	 */
	public void setJumpHeight(double height) {
		this.initialHeight = height;
	}

	/**
	 * Time between 2 simulation step, in second
	 * @param second
	 */
	public void setJumpPrecision(double stepSize) {
		this.stepDuration = stepSize;
	}
	
	public void setDragCoefficient(double dragCoefficient) {
		this.dragCoefficient = dragCoefficient;
	}
	
	public void setSection(double section) {
		this.section = section;
	}
}
