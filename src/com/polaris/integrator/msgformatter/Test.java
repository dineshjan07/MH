package com.polaris.integrator.msgformatter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) {
		String sample =new String("sdcsf");
		System.out.println(sample.getBytes().length);
		System.out.println(sample.substring(0,5));	
		try {
			FileInputStream fin = new FileInputStream("uidai_auth_prod.cer");
			try {
				System.out.println(fin.available());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 String testString="new test String";
//		int stringLength=testString.length();
		byte[] encodedXml=testString.getBytes();
		int length=encodedXml.length;
		int stringLength=Integer.toString(length).length();
		int total_length=length+stringLength+3;
		int no_of_iteration=1;
		final int BYTESPERTAG=6;
		if(total_length>BYTESPERTAG){
			no_of_iteration=total_length/BYTESPERTAG;
		}
		ArrayList<String> data=new ArrayList<String>();
		boolean firstIteration=true;
		int start=0;
		int end=BYTESPERTAG;
		while(no_of_iteration>0){
			if(firstIteration){
				end=end-(stringLength+3);
				 data.add(testString.substring(start,end));
				System.out.println(data);
				firstIteration=false;
			}
			else{
				 data.add(testString.substring(start, end));
				System.out.println(data);
			}
			start=end;
			end=end+BYTESPERTAG;
			no_of_iteration--;
		}
		
		String finalTagValue=new String();
		for(int i=0;i<data.size();i++){
			switch(i){
			case 0:
				finalTagValue="<DE112>"+data.get(i)+"</DE112>";
				break;
			case 1:
				finalTagValue=finalTagValue+"<DE112>"+data.get(i)+"</DE112>";
				break;
			case 2:
				finalTagValue=finalTagValue+"<DE113>"+data.get(i)+"</DE113>";
				break;
			case 3:
				finalTagValue=finalTagValue+"<DE114>"+data.get(i)+"</DE114>";
				break;
			case 4:
				finalTagValue=finalTagValue+"<DE115>"+data.get(i)+"</DE115>";
				break;
			case 5:
				finalTagValue=finalTagValue+"<DE116>"+data.get(i)+"</DE116>";
				break;
			case 6:
				finalTagValue=finalTagValue+"<DE117>"+data.get(i)+"</DE117>";
				break;
			}
		}
		System.out.println(finalTagValue);
		
	}

}
