package MyCrypt;

/**
 * This application was built as a wrapper for the SMGCrypt Class.
 * This application is meant to be a command-line utility that uses a
 * given key to encrypt a text message into a HEX payload or decrypt an
 * encrypted HEX payload.
 *  
 * @author Scott Griffis
 *
 */
public class MyCrypt {
	/**
	 * Application's main entry point.
	 * 
	 * @param args - Passed command-line arguments as {@link String}[]
	 */
	public static void main(String[] args) {
		String key = null;
		String message = null;
		Boolean isEncryption = null;
		
		int argsLen = args.length;
		for (int i = 0; i < argsLen; i++) {
			switch (args[i]) {
				case "-k":
					key = args[i + 1];
					break;
				case "-m":
					message = args[i + 1];
					break;
				case "-e":
				case "--encrypt":
					isEncryption = new Boolean(true);
					break;
				case "-d":
				case "--decrypt":
					isEncryption = new Boolean(false);
					break;
				case "-h":
				case "--help":
					printUsage();
					break;
				default:
					// Do Nothing...
					break;
			}
		}
		
		if (key != null && message != null && isEncryption != null) {
			if (isEncryption.booleanValue()) {
				System.out.println("Encrypted Message: " + SMGCrypt.encryptMessage(message, key));
			} else {
				System.out.println("Decrypted Message: " + SMGCrypt.decryptMessage(message, key));
			}
		} else {
			System.out.println("Invalid parameters!!!");
			printUsage();
		}
	}
	
	/**
	 * Prints the usage text for the application.
	 */
	private static void printUsage() {
		String usage = "usage: mycrypt [-k key] [-m message] [option]\n" +
							"\toptions:\n" + 
							"\t\t-e : Encrypts specified message.\n" +
							"\t\t-d : Decrypts specified payload.\n";
		System.out.println(usage);
	}
}
