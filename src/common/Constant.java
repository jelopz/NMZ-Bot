package common;

public class Constant {
	public static final String[] OVERLOAD_NAMES = { "Overload (4)", "Overload (3)", "Overload (2)", "Overload (1)" };
	public static final String[] ABSORP_NAMES = { "Absorption (4)", "Absorption (3)", "Absorption (2)",
			"Absorption (1)" };
	public static final String ROCKCAKE = "Dwarven rock cake";

	public enum State {
		PRAYER_FLICKING, PREPARE_OVERLOAD, GUZZLE_ABSORP, GUZZLE_ROCKCAKE, IDLE
	};
}
