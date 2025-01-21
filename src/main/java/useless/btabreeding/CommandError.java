package useless.btabreeding;

public class CommandError extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public CommandError(String message) {
		super(message);
	}
}
