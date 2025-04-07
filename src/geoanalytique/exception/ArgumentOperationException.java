/**
 *  
 */
package geoanalytique.exception;

/**
 * 
 *
 */
public class ArgumentOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2570434596188760027L;

	/**
	 * 
	 */
	public ArgumentOperationException() {
	}

	/**
	 * @param message
	 */
	public ArgumentOperationException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public ArgumentOperationException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ArgumentOperationException(String message, Throwable cause) {
		super(message, cause);
	}

}
