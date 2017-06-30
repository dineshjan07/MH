
package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenerateTerminalRRN_REQIN
    extends UserRule
{

    private static Logger log = LoggerFactory.getLogger( GenerateTerminalRRN_REQIN.class );

    public GenerateTerminalRRN_REQIN()
    {
    }

    public int Process( HashMap argParsedFldHM, String argInMsg, String argFldId, HashMap argFldFmtHM,
                        ErrorHandler argErrHandler )
    {
        try
        {
        	String s = argInMsg;
        	log.debug("argInMsg:"+argInMsg);
        	log.debug("s:"+s);
        	int fromIndex=s.indexOf("Stan=")+5;
        	int endIndex=s.indexOf("~*",fromIndex);
        	String stan=s.substring(fromIndex,endIndex);
        	log.debug("Stan value "+stan);
        	int transFromIndex=s.indexOf("Transm_Date_time=")+17;
        	int transendIndex=s.indexOf("~*",transFromIndex);
        	String transDate=s.substring(transFromIndex,transendIndex);
        	log.debug("Stan value "+transDate);
            super._cresultMessage = new String[1];
            Date today=new Date();
            String terminalrrn=getLastDigitOfYear(today)+getJulianDate(today)+getHoursFromDateTime(transDate)+stan;
//          String Input = argInMsg.substring( argInMsg.indexOf( "NULL~*" ) + "NULL~*".length(), argInMsg.length() );
            String Input = argInMsg+"terminal_rrn="+terminalrrn+"~*~*"; 
            
            log.debug("Input message "+Input);
            super._bypassMF = false;
			super._cresultMessage[0] = Input;
            super._iresultCode = 0;
            log.debug( "\n Inside UserExit : coutputStr : " + Input );
            log.debug( "\n Inside UserExit returns : _iresultCode : " + _iresultCode );
            log.debug( "\n Leaving UserExit TruncateHeader" );
            return super._iresultCode;
        }
        catch ( Exception exception )
        {
            super._iresultCode = 1000;
            log.error( "Exception in TruncateHeader : " , exception );
        }
        return super._iresultCode;
    }
    
    public static String getLastDigitOfYear(Date date){
		return Integer.toString(Calendar.getInstance().get(Calendar.YEAR)).substring(3);
	}
	public static String getJulianDate(Date date) {
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return String.format("%03d", calendar.get(Calendar.DAY_OF_YEAR));
	}
	public static String getHoursFromDateTime(String dateTime){
	
		return dateTime.substring(4, 6);
	}
    
    /**
     * Encrypts the security pin for a card and gives the Hex representation of the encrypted pin block.
     * @param cardNumber Card number for which the Pin is encrypted
     * @param pin Pin to be encrypted
     * @param key Clear Key to be used for encryption
     * @param keySize Key straight
     * @return The Hex representation of the encrypted pin block bytes
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
    * @throws UnsupportedEncodingException 
     */
    public static String encryptPinBlock(String cardNumber, String pin,
      String key, int keySize) throws NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    byte[] keyBytes = getEncryptionKey(key, keySize);
     byte[] pinBlock = getPinBlock(cardNumber, pin);

     SecretKey secretKey = new SecretKeySpec(keyBytes, "DESede");

     Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
     cipher.init(Cipher.ENCRYPT_MODE, secretKey);
     byte[] encryptedPinBlock = cipher.doFinal(pinBlock);

     return getHexString(encryptedPinBlock);

    }

    /**
     * Takes the Card number and Pin as input and generates the Pin Block Out of it.
     * First get the card padded (16 Char) which when converted to Hex gives an array of 8
     * Get the Pin Padded (16 Char) which when converted to Hex gives an array of 8
     * XOR the resulting arrays to get the pin block 
     * @param cardNumber
     * @param pin
     * @return
     * @throws IllegalBlockSizeException
     */
    private static byte[] getPinBlock(String cardNumber, String pin)
      throws IllegalBlockSizeException {
     int[] paddedPin = padPin(pin);
     int[] paddedCard = padCard(cardNumber);

     byte[] pinBlock = new byte[8];
     
     
     for (int cnt = 0; cnt < 8; cnt++) {
    	  System.out.println("block");
      pinBlock[cnt] = (byte) (paddedPin[cnt] ^ paddedCard[cnt]);
     }

     System.out.println("PIN BLOCK: "+getHexString(pinBlock));
     
     return pinBlock;
    }

    private static final String PIN_PAD = "FFFFFFFFFFFFFF";

    /**
     * Generates a 16 digit block, with following Components
     * Two digit pin length (left padded with zero if length less than 10)
     * Pin Number 
     * Right padded with F to make it 16 char long.
     * FOr example for a 5 digit Pin 12345 the outout would be 
     * 0512 345F FFFF FFFF
     * @param pin
     * @return
     * @throws IllegalBlockSizeException
     */
    private static int[] padPin(String pin) throws IllegalBlockSizeException {
     String pinBlockString = "0" + pin.length() + pin + PIN_PAD;
     pinBlockString = pinBlockString.substring(0, 16);
     
     System.out.println("Pin Block1 : " + pinBlockString);
     return getHexIntArray(pinBlockString);

    }

    private static final String ZERO_PAD = "0000000000000000";

    /**
     * Using the Card Number it generates a 16-digit block with 4 zeroes and and
     * the 12 right most digits of the card number, excluding the check digit
     * (which is the last digit of the card number. 
     * For Example for a Card 5259 5134 8115 5074
     * 4 Will be the check digit
     * Right most 12 digits would be 951348115507
     * Hence the output would be 0000 9513 4811 5507
     * @param cardNumber
     * @return
     * @throws IllegalBlockSizeException
     */
    private static int[] padCard(String cardNumber)
      throws IllegalBlockSizeException {
     cardNumber = ZERO_PAD + cardNumber;
     int cardNumberLength = cardNumber.length();
     int beginIndex = cardNumberLength - 13;
     String acctNumber = "0000"
       + cardNumber.substring(beginIndex, cardNumberLength - 1);
     
     System.out.println( "AccNo : "+acctNumber);
     return getHexIntArray(acctNumber);
    }

    /**
     * Takes Hex representation of the key, validates the length and returns the
     * equivallent bytes
     * 
     * @param keyString
     *            Hex representation of the key. THe allowed length of the
     *            string are 16 (56 bit), 32 (112 bit), 32 or 48 (for 168 bit).
     *            If the key Strength is 168 bit and the key length is 32 the
     *            first 16 chars are repeated.
     * @param keySize
     *            Valid values are 56, 112, 168
     * @return
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     * 
     */
    private static byte[] getEncryptionKey(String keyString, int keySize)
      throws IllegalBlockSizeException, InvalidKeyException {
     int keyLength = keyString.length();
     switch (keySize) {
     case 56:
      if (keyLength != 16)
       throw new InvalidKeyException(
         "Hex Key length should be 16 for a 56 Bit Encryption, found ["
           + keyLength + "]");
      break;
     case 112:
      if (keyLength != 32)
       throw new InvalidKeyException(
         "Hex Key length should be 32 for a 112 Bit Encryption, found["
           + keyLength + "]");
      break;
     case 168:
      if (keyLength != 32 && keyLength != 48)	
       throw new InvalidKeyException(
         "Hex Key length should be 32 or 48 for a 168 Bit Encryption, found["
           + keyLength + "]");
      if (keyLength == 32) {
       keyString = keyString + keyString.substring(0, 16);
      }
      break;
     default:
      throw new InvalidKeyException(
        "Invalid Key Size, expected one of [56, 112, 168], found["
          + keySize + "]");
     }

     System.out.println("key string: "+keyString);
     byte[] keyBytes = getHexByteArray(keyString);
     
     System.out.println("KEYBYTES:"+keyBytes.length);
     return keyBytes;

    }

    /**
     * Takes a byte array as input and provides a Hex String reporesentation
     * 
     * @param input
     * @return
     */
    private static String getHexString(byte[] input) {
     StringBuilder strBuilder = new StringBuilder();
     for (byte hexByte : input) {
      int res = 0xFF & hexByte;
      String hexString = Integer.toHexString(res);
      if (hexString.length() == 1) {
       strBuilder.append(0);
      }
      strBuilder.append(hexString);

     }

     return strBuilder.toString().toUpperCase();
    }

    /**
     * Converts a Hex string representation to an int array
     * 
     * @param input
     *            Every two character of the string is assumed to be
     * @return int array containing the Hex String input
     * @throws IllegalBlockSizeException
     */
    private static int[] getHexIntArray(String input)
      throws IllegalBlockSizeException {
     if (input.length() % 2 != 0) {
      throw new IllegalBlockSizeException(
        "Invalid Hex String, Hex representation length is not a multiple of 2");
     }
     int[] resultHex = new int[input.length() / 2];
     for (int iCnt1 = 0; iCnt1 < input.length(); iCnt1++) {
      String byteString = input.substring(iCnt1, ++iCnt1 + 1);
      int hexOut = Integer.parseInt(byteString, 16);
      resultHex[iCnt1 / 2] = (hexOut & 0x000000ff);
     }
     return resultHex;
    }

    /**
     * Converts a Hex string representation to an byte array
     * 
     * @param input
     *            Every two character of the string is assumed to be
     * @return byte array containing the Hex String input
     * @throws IllegalBlockSizeException
     */
    private static byte[] getHexByteArray(String input)
      throws IllegalBlockSizeException {

     int[] resultHex = getHexIntArray(input);
     byte[] returnBytes = new byte[resultHex.length];
     for (int cnt = 0; cnt < resultHex.length; cnt++) {
      returnBytes[cnt] = (byte) resultHex[cnt];
     }
     return returnBytes;
    }
    
    
	public static void main(String[] args) { HashMap hm = new HashMap();
	GenerateTerminalRRN_REQIN test = new GenerateTerminalRRN_REQIN();
	test.Process( new HashMap(),
	"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*BaseNumber=10565~*CardNum=6080140000000210-2022~*ExtraTag=~*ReferenceID=AAAAAB429917~*PCI_CARD_SERNO=~*IsPINRequired=7890~*PIN=7890~*AccountNumber=null~*~*"
	/*"hdr_Tran_Id=RET_CARD_PIN_VALIDATION~*hdr_Status=NULL~*CardNum=6080140000000210-2022~*PIN=7890~*"*/
	, "", new HashMap(), new ErrorHandler());
	}
   
    
}