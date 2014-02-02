import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class JumpGraph extends Component {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2843420786239598060L;
	private static final int MARGIN_LEFT = 40;
	private static final int MARGIN_BOTTOM = 40;
	private static final int MARGIN_RIGHT = 40;
	private static final int MARGIN_TOP = 40;

	private List<JumpResult> jumps = new ArrayList<JumpResult>();
	private Graphics2D g2;
	private double maxHeight;
	private double maxSpeed;
	private double maxDuration;
	private double heightRatio;
	private double speedRatio;
	private double durationRatio;
	private long seed;
	private Random random;
	private int mouseX = 0;
	private int mouseY = 0;
	private boolean mouseOnGraphics;
	private Formatter formatter;
	private double horizontalheightRatio;
	
	
	public JumpGraph() {
		Random r = new Random();
		seed = r.nextLong();
		
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getPoint().x-1;
				mouseY = e.getPoint().y-1;
				checkMouse();
			}
		});
		
		formatter = new Formatter();
	}

	@Override
	public void paint(Graphics g) {

		random = new Random(seed);
		
		
		g2 = (Graphics2D) g;

//		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Background
		g2.setPaint(Color.white);
		g2.fill(new Rectangle2D.Double(0, 0, getSize().width, getSize().height));

		// Jumps
		drawJumps();
		
		// Axis
		g2.setPaint(Color.black);
		BasicStroke plain = new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		g2.setStroke(plain);
		g2.drawLine(MARGIN_LEFT, MARGIN_TOP, MARGIN_LEFT, getSize().height - MARGIN_BOTTOM);
		g2.drawLine(MARGIN_LEFT, getSize().height - MARGIN_BOTTOM, getSize().width - MARGIN_RIGHT, getSize().height - MARGIN_BOTTOM);

		// Mouse
		if(mouseOnGraphics) {
			
			double mouseTime = (mouseX - MARGIN_LEFT) / durationRatio;
			double mouseHeight = (getSize().height - mouseY - MARGIN_BOTTOM) / heightRatio;
			double mouseSpeed = (getSize().height - mouseY - MARGIN_BOTTOM) / speedRatio;
			
			g2.drawString("Time: "+formatter.time(mouseTime), getSize().width - MARGIN_RIGHT - 300, MARGIN_TOP + 10);
			
			g2.drawString("Height: "+formatter.meters(mouseHeight), getSize().width - MARGIN_RIGHT - 300, MARGIN_TOP + 30);
			g2.drawString("Speed: "+formatter.speed(mouseSpeed)+" or "+formatter.speedKmH(mouseSpeed), getSize().width - MARGIN_RIGHT - 300, MARGIN_TOP + 50);
			
			g2.setPaint(Color.gray);
			g2.setStroke(new BasicStroke(0.5f));
			
			g2.drawLine(mouseX, MARGIN_TOP, mouseX, getSize().height - MARGIN_BOTTOM);
			g2.drawLine(MARGIN_LEFT, mouseY, getSize().width - MARGIN_RIGHT, mouseY);
			
		}
	}

	private void drawJumps() {

		// Compute axis max value
		maxHeight = 1;
		maxDuration = 1;
		maxSpeed = 1;

		for (JumpResult jump : jumps) {
			if (jump.getDuration() > maxDuration) {
				maxDuration = jump.getDuration();
			}

			if (jump.getConfig().initialHeight > maxHeight) {
				maxHeight = jump.getConfig().initialHeight;
			}
			
			if (jump.getMaxSpeedState().speed > maxSpeed) {
				maxSpeed = jump.getMaxSpeedState().speed;
			}
		}

		// Apply margin
		maxHeight *= 1.05;
		maxDuration *= 1.05;
		maxSpeed  *= 1.05;
				

		double availableHeight = getSize().height - MARGIN_BOTTOM - MARGIN_TOP;
		double availableWidth = getSize().width - MARGIN_LEFT - MARGIN_RIGHT;
		
		heightRatio = availableHeight / maxHeight;
		speedRatio = availableHeight / maxSpeed;
		durationRatio = availableWidth / maxDuration;
		
		horizontalheightRatio = availableWidth / maxHeight;
		
		for (JumpResult jump : jumps) {
			drawJump(jump);
		}

	}

	private void drawJump(JumpResult jump) {
		// draw GeneralPath (polyline)

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		

		GeneralPath heightPolyline = new GeneralPath(GeneralPath.WIND_NON_ZERO, jump.getStepCount());
		GeneralPath speedPolyline = new GeneralPath(GeneralPath.WIND_NON_ZERO, jump.getStepCount());
		GeneralPath speedPerHeightPolyline = new GeneralPath(GeneralPath.WIND_NON_ZERO, jump.getStepCount());

		JumpState step = jump.getStep(0);
		heightPolyline.moveTo(getTimeOffset(jump.getStepTime(0)), getHeightOffset(step.height));
		speedPolyline.moveTo(getTimeOffset(jump.getStepTime(0)), getSpeedOffset(step.speed));
		speedPerHeightPolyline.moveTo(getHorizontalHeightOffset(step.height), getSpeedOffset(step.speed));

		int indexStep = (int) (1.0 /(jump.getConfig().stepDuration * durationRatio));
		
		for (int index = 1; index < jump.getStepCount(); index+=indexStep) {
			step = jump.getStep(index);
			heightPolyline.lineTo(getTimeOffset(jump.getStepTime(index)), getHeightOffset(step.height));
			speedPolyline.lineTo(getTimeOffset(jump.getStepTime(index)), getSpeedOffset(step.speed));
			speedPerHeightPolyline.lineTo(getHorizontalHeightOffset(step.height), getSpeedOffset(step.speed));
		}

		g2.setStroke(new BasicStroke(2f));

		Color pickColor = pickColor();
		g2.setPaint(pickColor);
		g2.draw(heightPolyline);
		
		g2.setStroke(new BasicStroke(1f));
		
		g2.setPaint(pickColor.brighter());
		g2.draw(speedPolyline);
		
//		Color pickColor2 = pickColor();
		g2.setPaint(pickColor.brighter().brighter());
		g2.draw(speedPerHeightPolyline);
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
	}

	
	private Color pickColor() {

		Queue<Color> colorQueue = new LinkedBlockingQueue<Color>();
		
		Color color = colorQueue.poll();
		
		
		while(color == null || (color.getRed() + color.getGreen() + color.getBlue()) > 400 ) {
			color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
		}
		
		return color;
	}

	private double getSpeedOffset(double speed) {
		return getSize().height - MARGIN_BOTTOM - speedRatio * speed;
	}
	
	private double getHeightOffset(double height) {
		return getSize().height - MARGIN_BOTTOM - heightRatio * height;
	}

	private double getHorizontalHeightOffset(double height) {
		return  MARGIN_LEFT + horizontalheightRatio * height;
	}

	
	private double getTimeOffset(double time) {
		return MARGIN_LEFT + durationRatio * time;
	}

	public void addJump(JumpResult jump) {
		jumps.add(jump);
	}
	
	
	private void checkMouse() {
		mouseOnGraphics = true;
		
		if(mouseX < MARGIN_LEFT || mouseX > getSize().width - MARGIN_RIGHT) {
			mouseOnGraphics = false;
		} else if(mouseY < MARGIN_TOP || mouseY > getSize().height - MARGIN_BOTTOM) {
			mouseOnGraphics = false;
		}
		
		
		repaint();
	}
}
