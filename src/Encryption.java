/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//package myfirstjava;

/**
 *
 * @author thivya.panimalar
 */
import javax.crypto.Cipher;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.ByteArrayOutputStream;

public class Encryption {
 private static final String KEY_STRING ="167-42-158-164-248-173-50-74-193-73-82-185-76-145-247-188-167-42-158-164-248-173-50-74";
 public String encrypt( String source )
   {        
    try
    {
      // Get our secret key
      Key key = getKey();
      System.out.println("Key value is       "+key);

      // Create the cipher 
      Cipher desCipher = Cipher.getInstance("DESede");
      
      System.out.println("Cipher value    "+desCipher);

      // Initialize the cipher for encryption
      desCipher.init(Cipher.ENCRYPT_MODE, key);

      // Our cleartext as bytes
      byte[] cleartext = source.getBytes();
        System.out.println("CLEARTEXT value    "+cleartext);
      // Encrypt the cleartext
      byte[] ciphertext = desCipher.doFinal(cleartext);
      
        System.out.println("Ciphertext value    "+ciphertext);

      // Return a String representation of the cipher text
      return getString( ciphertext );
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return null;
  }
  private Key getKey()
  {
    try
    {
      byte[] bytes = getBytes( KEY_STRING );
      DESedeKeySpec pass = new DESedeKeySpec( bytes );
      SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede"); 
      SecretKey s = skf.generateSecret(pass); 
      return s;      
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
    return null;
  }


  private String getString( byte[] bytes )
  {
    StringBuffer sb = new StringBuffer();
    for( int i=0; i<bytes.length; i++ )
    {
      byte b = bytes[ i ];
      sb.append( ( int )( 0x00FF & b ) );
      if( i+1 <bytes.length )
      {
        sb.append( "-" );
      }
    }
    return sb.toString();
  }

  private byte[] getBytes( String str )
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    StringTokenizer st = new StringTokenizer( str, "-", false );
    while( st.hasMoreTokens() )
    {
      int i = Integer.parseInt( st.nextToken() );
      bos.write( ( byte )i );
    }
    return bos.toByteArray();
  }
  

public static void main(String args[])
   {
      try{
      Encryption des = new Encryption();
      System.out.println("Encrypted Password----  "+des.encrypt("intellect123"));  
     
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
   }	

}
