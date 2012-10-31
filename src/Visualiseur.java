import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class Visualiseur extends JFrame {

	public static void main(String[] args) {
		JumpConfig configFelix = new JumpConfig();
		configFelix.setName("Felix Baumgartner");
		configFelix.setJumpHeight(39045);
		configFelix.setJumpEndHeight(2516);
		configFelix.setJumpPrecision(0.001);
		configFelix.setMass(100);
		configFelix.setDragCoefficient(1.1); // http://en.wikipedia.org/wiki/Drag_coefficient
		configFelix.setSection(0.4* 1.4); // rectangle of 40 cm * 1.4 m
		
		JumpConfig configJoseph = new JumpConfig();
		configJoseph.setName("Joseph Kittinger");
		configJoseph.setJumpHeight(31300);
		configJoseph.setJumpEndHeight(5334);
		configJoseph.setJumpPrecision(0.001);
		configJoseph.setMass(100);
		configJoseph.setDragCoefficient(1.5); // http://en.wikipedia.org/wiki/Drag_coefficient
		configJoseph.setSection(0.5* 1.8); // rectangle of 50 cm * 1.8 m
		
		 System.out.println("Start computation ...");
        long startJumpComputationTime = System.nanoTime();
                         
		             
		JumpResult resultFelix = performJump(configFelix);
		JumpResult resultJoe = performJump(configJoseph);
		
		long endJumpComputationTime = System.nanoTime();
        long computationDuration = endJumpComputationTime - startJumpComputationTime;
		System.out.println("Computation done in " + (computationDuration/1000000.) + " ms");

		printResult(resultFelix);
		printResult(resultJoe);
		
		Visualiseur visualiseur = new Visualiseur();
		visualiseur.addJump(resultFelix);
		visualiseur.addJump(resultJoe);
		visualiseur.setVisible(true);
	}
	


	private static JumpResult performJump(JumpConfig config) {
		JumpSimulator simulator = new JumpSimulator(config);
		
		simulator.jump();
		return simulator.getJumpResult();
	}

	private static void printResult(JumpResult result) {
		Formatter f = new Formatter();
		
		
		System.out.println("");
		System.out.println("Jump details: "+result.getConfig().name);
		System.out.println("=============");
		System.out.println("Duration: " + f.time(result.getDuration()));
		System.out.println("Jump step count: " + result.getStepCount());
		System.out.println("Jump final altitude: " + f.meters(result.getStep(result.getStepCount() - 1).height));
		System.out.println("Max speed: " + f.speed(result.getMaxSpeedState().speed)+" at "+ f.time(result.getStepTime(result.getMaxSpeedStep()))+" or "+f.speedKmH(result.getMaxSpeedState().speed));
	}
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3581747593598187334L;
	private JumpGraph jumpGraph;


	public Visualiseur() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 768);
		
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		jumpGraph = new JumpGraph();
		contentPane.add(jumpGraph);
		setContentPane(contentPane);
	}
	
	
	private void addJump(JumpResult jump) {
		jumpGraph.addJump(jump);
	}
	
	

	
	
}
