import java.text.DecimalFormat;


public class Formatter {

	private DecimalFormat df;

	public Formatter() {
		df = new DecimalFormat("#.##");
	}

	public String seconds(double duration) {
		return df.format(duration)+" s";
	}

	public String minutes(double duration) {
		int minutes = (int) (duration /60);
		double seconds = duration - 60 * minutes;
		
		return ""+minutes+" min "+seconds(seconds);
	}

	public String meters(double height) {
		return df.format(height)+" m";
	}

	public String speed(double speed) {
		return  df.format(speed)+" m/s";
	}

	public String speedKmH(double speed) {
		return df.format(speed*3.6)+" km/h";
	}

	public String time(double duration) {
		if(duration > 60) {
			return seconds(duration)+ " ("+minutes(duration)+")";
		} else {
			return seconds(duration);
		}
	}
}
