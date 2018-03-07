package com.firebirdcss.tools.mycrypt;

import java.util.Formatter;

/**
 * This class allows for the simple encryption and decryption of a string using a key.
 * <p>
 * This encryption is not strong and can likely be easily broken. It was created
 * more for fun and to try out some theories that I had. This encryption should
 * not be relied on for encrypting sensitive information.
 * 
 * @author Scott Griffis
 *
 */
public class SMGCrypt {
	/**
	 * Encrypts a {@link String} using a specified private key.<br>
	 * The result of encryption is a string of HEX twice the length
	 * of the original message.
	 * 
	 * @param message as {@link String}
	 * @param key as {@link String} 
	 * @return Returns the encrypted message as {@link String}
	 */
	public static String encryptMessage(String message, String key) {
		String result = "";
		
		byte[] messageArray = message.getBytes().clone();
		byte[] seedArray = key.getBytes().clone();
		int seedLength = seedArray.length;
		
		for (int i = 1; i <= 5 ; i ++) {
			messageArray = xorMessage(messageArray, seedArray);
			messageArray = rotateBytesLeft(messageArray, (seedLength / 2));
			messageArray = xorMessage(messageArray, seedArray);
			messageArray = rotateBytesLeft(messageArray, (seedLength / 3));
			messageArray = xorMessage(messageArray, seedArray);
		}
		
		result = bytesToHex(messageArray);
		
		return result;
	}
	
	/**
	 * Decrypts a HEX message (using a specified private key), that was generated by using the encryptMessage
	 * method of this same class.
	 * 
	 * @param hexMessage as {@link String}
	 * @param key as {@link String}
	 * @return Returns original message as {@link String}
	 */
	public static String decryptMessage(String hexMessage, String key) {
		StringBuilder result = new StringBuilder();
		
		byte[] messageArray = hexToBytes(hexMessage);
		byte[] seedArray = key.getBytes().clone();
		int seedLength = seedArray.length;
		
		for (int i = 1; i <= 5 ; i ++) {
			messageArray = xorMessage(messageArray, seedArray);
			messageArray = rotateBytesRight(messageArray, (seedLength / 3));
			messageArray = xorMessage(messageArray, seedArray);
			messageArray = rotateBytesRight(messageArray, (seedLength / 2));
			messageArray = xorMessage(messageArray, seedArray);
		}
		
		for (byte b : messageArray) {
			result.append((char) b);
		}
		
		return result.toString();
	}
	
	/**
	 * XORs the data contained in the messageArray byte by byte with
	 * the data contained in the seedArray repeated end to end for the
	 * length of the messageArray.
	 * 
	 * @param messageArray as {@link byte}[]
	 * @param seedArray as {@link byte}[]
	 * @return Returns result as {@link byte}[]
	 */
	private static byte[] xorMessage(byte[] messageArray, byte[] seedArray) {
		byte[] result = messageArray.clone();
		
		int seedIndex = -1;
		for (int messageIndex = 0; messageIndex < result.length; messageIndex++) {
			if (seedIndex < (seedArray.length - 1)) {
				seedIndex ++;
			} else {
				seedIndex = 0;
			}
			result[messageIndex] = (byte) (result[messageIndex] ^ seedArray[seedIndex]);
		}
		
		return result;
	}
	
	/**
	 * Rotates the bytes of the given byteArray to the left the number
	 * of times specified by count.
	 * 
	 * @param byteArray as {@link byte}[]
	 * @param count as {@link int}
	 * @return Returns the result as {@link byte}[]
	 */
	private static byte[] rotateBytesLeft(byte[] byteArray, int count) {
		byte[] result = byteArray.clone();
		int arrayLength = byteArray.length;
		
		for (int n = 1; n <= count; n++) {
			byte carryByte = result[0];
			for (int i = 0; i < arrayLength - 1; i++) {
				result[i] = result[i + 1];
			}
			result[arrayLength - 1] = carryByte;
		}
		
		return result;
	}
	
	/**
	 * Rotates the bytes of the given byteArray to the right the number
	 * of times specified by count.
	 * 
	 * @param byteArray as {@link byte}[]
	 * @param count as {@link int}
	 * @return Returns the result as {@link byte}[]
	 */
	private static byte[] rotateBytesRight(byte[] byteArray, int count) {
		byte[] result = byteArray.clone();
		int arrayLength = byteArray.length;
		
		for (int n = 1; n <= count; n++) {
			byte carryByte = result[arrayLength - 1];
			for (int i = arrayLength - 1; i > 0; i--) {
				result[i] = result[i - 1];
			}
			result[0] = carryByte;
		}
		
		return result;
	}
	
	/**
	 * Converts the supplied array of bytes to a HEX String.
	 * 
	 * @param byteArray as {@link byte}[]
	 * @return Returns result as {@link String}
	 */
	private static String bytesToHex(byte[] byteArray) {
		StringBuilder sb = new StringBuilder(byteArray.length * 2);
		
		@SuppressWarnings("resource")
		Formatter formatter = new Formatter(sb);
		for (byte b : byteArray) {
			formatter.format("%02x",  b);
		}
		
		return sb.toString();
	}
	
	/**
	 * Converts the supplied String of HEX to an array of bytes.
	 * 
	 * @param hexString as {@link String}
	 * @return Returns result as {@link byte}[]
	 */
	private static byte[] hexToBytes(String hexString) {
	    int stringLength = hexString.length();
	    byte[] result = new byte[stringLength / 2];
	    for (int i = 0; i < stringLength; i += 2) {
	        result[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16));
	    }
	    
	    return result;
	}
}