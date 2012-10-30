import java.text.DecimalFormat;


public class Visualiseur {

	public static void main(String[] args) {
		JumpConfig config = new JumpConfig();
		config.setJumpHeight(40000);
		config.setJumpPrecision(0.001);
		config.setMass(100);
		config.setDragCoefficient(0.2); // http://en.wikipedia.org/wiki/Drag_coefficient
		config.setSection(0.3* 0.3); // rectangle of 40 cm * 1 m
		
		JumpSimulator simulator = new JumpSimulator(config);
		
		System.out.println("Start computation ...");
		long startJumpComputationTime = System.nanoTime();
		simulator.jump();
		long endJumpComputationTime = System.nanoTime();
		long computationDuration = endJumpComputationTime- startJumpComputationTime;
		
		System.out.println("Computation done in " + (computationDuration/1000000.) + " ms");
		
		JumpResult result = simulator.getJumpResult();
		
		
		DecimalFormat df = new DecimalFormat("#.##");
		
		System.out.println("Jump details");
		System.out.println("============");
		System.out.println("Duration: " + df.format(result.getDuration())+" s");
		System.out.println("Jump step count: " + result.getStepCount());
		System.out.println("Jump final altitude: " + df.format(result.getStep(result.getStepCount() - 1).height)+" m");
		System.out.println("Max speed: " + df.format(result.getMaxSpeedState().speed)+" m/s at "+ df.format(result.getStepTime(result.getMaxSpeedStep()))+" s ("+df.format(result.getMaxSpeedState().speed*3.6)+" km/h)");
	}
}
