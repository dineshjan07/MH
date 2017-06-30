package com.polaris.integrator.msgformatter;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class TestProg {

	public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException {
		// TODO Auto-generated method stub
		byte[] aesKey;

		aesKey = UIDAIEncryptionUtility.getRandomAESSKey();
		String pidXml="<Pid ts=\"2017-05-18T18:10:54\" ver=\"1.0\"><Bios><Bio type=\"FMR\" posh=\"RIGHT_THUMB\">Rk1SACAyMAAAAAFiAAABLAGQAMUAxQEAAAAANkBUABh7AICDADZ1AEB3AD37AIA0AEGAAICJAEp1AEBbAE8AAEBmAFV7AIDLAGHrAECaAGT4AEAXAGeWAEDFAGhxAEDRAHfuAICtAHh1AEA4AHoKAEAlAI+dAIBIAJKOAIBZAJiNAIBGAKcRAIAhAKkcAECVAKt9AEDlAK7nAEBEALeaAECQALf+AEB0ALuNAICBAMaUAICwANFyAICEANIJAECbANN9AEAxAO2sAEC8APJxAECXAPaNAED/APnbAEDzAPvWAICiAQOOAIC3AQNxAEBjAQSuAIALAQWyAEDkAQrRAEDKAQvWAECsARDqAEAYAREwAEC9ARJvAEAwARmzAECTASC9AEEHASfKAEDEATRGAEALATa1AEBFATy4AIB2AT3DAECGAT7NAEDjAVeoAIC4AVz/AIDyAWizAECpAYBnAAAA</Bio><Bio type=\"FMR\" posh=\"RIGHT_THUMB\">Rk1SACAyMAAAAAF6AAABLAGQAMUAxQEAAAAAOkCWABp9AICFABr4AEBUAC59AIDVAD14AEBMAEkDAEBuAEp7AECZAEt7AEEEAFd1AEBNAG3+AEBdAHyGAIB9AIh8AEBbAI0DAEDWAJ57AECTAKX7AEEAAKf0AEB/AK+AAEDDALN9AIC9ANR5AEEBANnxAEBhAN2NAECTAOKAAEBjAOgKAECrAOsAAEAQAO2WAEDJAO54AEAqAPaWAICiAPj5AEBWAQENAEBmAQiKAEDmAQnxAEDMAQl1AEBXAReWAEByARiQAEDtARvoAEDiASBuAEEQASBoAEByASSRAECAASoKAIC/AS30AEAcATWeAEDvAUVnAEBnAUqqAEDRAU5iAIC3AVBrAEBLAVCqAICSAVQBAECkAV3qAEC8AV1sAECUAWO4AEBMAWezAEBXAWkwAICpAWndAEAhAWyuAICGAXTFAIBtAXS9AEApAXaxAECjAXnaAIBoAYG8AAAA</Bio></Bios></Pid>";
		System.out.println(UIDAIEncryptionUtility.encrypt(pidXml.getBytes(),aesKey)); 
	}

}
