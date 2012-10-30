public class World {

	public static final double g = 9.80665; // Ground gravity in m/s2
	public static final double R = 6371000; // Earth radius

	public static final double nAvo = 6.02214179e23; // Avogadro constant, mol-1 (NIST, CODATA 2006)

	public static final double kBoltz = 1.3806504e-23; // Boltzmann constant, J K-1 (NIST, CODATA 2006)

	public static final double heightTab[] = { 0.0, 11000., 20000., 32000., 47000., 51000., 71000., 84852. }; // Altitude layers
	public static final double temperatureTab[] = { 288.15, 216.65, 216.65, 228.65, 270.65, 270.65, 214.65, 186.946 }; // Temperature layers
	public static final double gradientTab[] = { -6.5, 0.0, 1.0, 2.8, 0.0, -2.8, -2.0, 0.0 }; // Temperature gradient per km
	public static final double presureTab[] = { 101325., 22632.0634575, 5474.88865875, 868.01868888, 110.9063021325, 66.93887278575, 3.95642045505, 0.37338363825 }; // P in Pa

	public static final double mair = 0.0289644; // molar mass of air (dry) in kg/mol
	public static final double runi = 8.31432; // universal gas constant in J mol-1 K-1

	public static double gravity(double height) {
		return g - height * 2 / R;
	}

	public static double airDensity(double height) {

		int n = 0;
		for (int j = 1; j < 8; j++) {
			if (height < heightTab[j]) {
				n = j - 1;
				break;
			}
		}

		double heightDelta = (height - heightTab[n]) / 1000.;
		double temperature = temperatureTab[n] + heightDelta * gradientTab[n];
		double p;
		if (gradientTab[n] == 0.0) {
			p = presureTab[n] * Math.exp(-mair * g / runi / temperatureTab[n] * heightDelta * 1000.);
		} else {
			double beta = -mair * g / runi / gradientTab[n] * 1000.;
			p = presureTab[n] * Math.pow(1. + gradientTab[n] * heightDelta / temperatureTab[n], beta);
		}

		double density = p / nAvo / kBoltz / temperature;
		return density;
	}
}
