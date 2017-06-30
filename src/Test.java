
public class Test {

	
	public static void main(String[] args) {
		try{

			String inputMsg="<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"http://corp.alahli.com/middlewareservices/header/1.0/\" xmlns:oas=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\" xmlns:ns1=\"http://corp.alahli.com/middlewareservices/sadad/1.0/\"><soapenv:Header><header xmlns=\"http://schemas.cordys.com/General/1.0/\"><msg-id>005056a0-013e-11e2-e939-4fd8f0e8966f</msg-id><license>License has expired since 173 day(s)</license></header><bpm xmlns=\"http://schemas.cordys.com/bpm/instance/1.0\"><instance_id>005056a0-013e-11e2-e939-4fd90e0017e9</instance_id></bpm></soapenv:Header><soapenv:Body><BillersInformationResponse xmlns=\"http://corp.alahli.com/middlewareservices/sadad/1.0/\"><success><billerGroups><billerGroup><id>1</id><code>TELCO</code><englishName>Telecom</englishName><arabicName>???????</arabicName><voiceTag>GROUP_TELCO</voiceTag><rank>1</rank></billerGroup></billerGroups><types><type><name/><choice><enum value=\"SABBSARI\" englishName=\"Saudi British\" arabicName=\"?????????\" voiceTag=\"\"/><enum value=\"AAALSARI\" englishName=\"Saudi Hollandi\" arabicName=\"????????\" voiceTag=\"\"/><enum value=\"SIBCSARI\" englishName=\"Saudi Investment\" arabicName=\"????????\" voiceTag=\"\"/><enum value=\"ALBISARI\" englishName=\"Al Bilad\" arabicName=\"??????\" voiceTag=\"\"/><enum value=\"INMASARI\" englishName=\"Al Inma\" arabicName=\"???????\" voiceTag=\"\"/></choice></type></types><typeFilters><typeFilter><name>BodyType_Filter</name><baseType>BodyType_Enum</baseType><filterType>NewRegistrationType_Enum</filterType><filter baseValue=\"4\" filterValue=\"01\"/><filter baseValue=\"5\" filterValue=\"01\"/><filter baseValue=\"6\" filterValue=\"01\"/><filter baseValue=\"5\" filterValue=\"02\"/><filter baseValue=\"6\" filterValue=\"02\"/> </typeFilter></typeFilters><feeServiceErrors><feeServiceError><number/><englishDescription/><arabicDescription/></feeServiceError></feeServiceErrors><sadadErrors><sadadError><number/><englishDescription/><arabicDescription/></sadadError></sadadErrors><sadadBanks><sadadBank><bankCode/><bankBICCode/><englishName/><arabicName/><englishShortName/><arabicShortName/></sadadBank></sadadBanks><refundTypes><refundType><code/><englishName/><arabicName/></refundType></refundTypes><billers><biller><id>126</id><code>MOIDC</code><type>SADAD</type><shortName>Deportation Sentence</shortName><englishName>Deportation Sentences</englishName><arabicName>Deportation Sentences</arabicName><hasPaymentService>false</hasPaymentService><hasVoucherService>false</hasVoucherService><hasFeeService>true</hasFeeService><voiceTag>ACCT_NUM</voiceTag><rank>1</rank><billerGroups><billerGroup id=\"11\"/></billerGroups><paymentServices><paymentService><category/><allowReversal>true</allowReversal><allowRefund>false</allowRefund><allowOverPay>true</allowOverPay><allowUnderPay>true</allowUnderPay><allowAdvancePay/><allowCCPay>true</allowCCPay><allowCCReversal>false</allowCCReversal><allowCCRefund>false</allowCCRefund><serviceTypes><serviceType><type>LLIN</type><englishName>Land Line Phone Service</englishName><arabicName>?????? ????</arabicName><voiceTag>SVC_LLIN</voiceTag></serviceType></serviceTypes><paymentTypes><paymentType><type>POST</type><englishName>Post Paid</englishName><arabicName>?????</arabicName><voiceTag>PMT_TYPE_POST</voiceTag></paymentType></paymentTypes><billCategories><billCategory><id>1</id><type>LLN</type><englishName>Recurring Bill</englishName><arabicName>?????? ??????</arabicName><voiceTag>BCAT_1</voiceTag></billCategory></billCategories><amountRange><type>Currency</type></amountRange><parameter><code>1</code><order>1</order><name>BillingAcct</name><englishName>Account Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>11</minimumLength><maximumLength>11</maximumLength><voiceTag>ACCT_NUM</voiceTag></parameter><parameter><code>1</code><order>1</order><name>BillingAcct</name><englishName>Account Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>8</minimumLength><maximumLength>10</maximumLength><voiceTag>ACCT_NUM</voiceTag></parameter><parameter><code>1</code><order>1</order><name>BillingAcct</name><englishName>Policy Number</englishName><arabicName>??????</arabicName><required>true</required><type>Number</type><minimumLength>5</minimumLength><maximumLength>15</maximumLength><voiceTag>POLICY_NUM</voiceTag></parameter><parameter><code>1</code><order>1</order><name>BillingAcct</name><englishName>Cust A/c No</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>8</minimumLength><maximumLength>8</maximumLength><voiceTag>ACCT_NUM</voiceTag></parameter><parameter><code>1</code><order>1</order><name>BillingAcct</name><englishName>Service Line Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>12</minimumLength><maximumLength>12</maximumLength><voiceTag>SVC_LINE_NUM</voiceTag></parameter><parameter><code>1</code><order>2</order><name>BillingAcct</name><englishName>Customer Account Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>12</minimumLength><maximumLength>15</maximumLength><voiceTag>ACCT_NUM</voiceTag></parameter> </paymentService></paymentServices><voucherServices><voucherService><serviceType/></voucherService></voucherServices><feeServices><feeService><serviceCode>046</serviceCode><englishName>Pay All Deportation Sentences Installments</englishName><arabicName>Pay All Deportation Sentences Installments</arabicName><allowReversal>false</allowReversal><allowRefund>false</allowRefund><allowOverPay>false</allowOverPay><allowUnderPay>false</allowUnderPay><allowAdvancePay>false</allowAdvancePay><allowCCPay>false</allowCCPay><allowCCReversal>false</allowCCReversal><allowCCRefund>false</allowCCRefund><voiceTag/><rank>1</rank><parameter><code>3</code><order>0</order><name>BeneficiaryId</name><englishName>Violator ID</englishName><arabicName>Violator ID</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag>NAT_ID</voiceTag></parameter><parameter><code>3</code><order>0</order><name>BeneficiaryId</name><englishName>Violator ID</englishName><arabicName>Violator ID</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag/></parameter><parameter><code>3</code><order>0</order><name>BeneficiaryId</name><englishName>Runaway Alien ID</englishName><arabicName>??? ??????? ?? ??? ?????? ??????? ??????? ??????</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag/></parameter><parameter><code>3</code><order>1</order><name>BeneficiaryId</name><englishName>Border Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag>BORDER_NUM</voiceTag></parameter><parameter><code>105</code><order>1</order><name>SponsorId</name><englishName>Border Number</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag>BORDER_NUM</voiceTag></parameter><parameter><code>3</code><order>2</order><name>BeneficiaryId</name><englishName>Sponsor ID</englishName><arabicName>??? ??????</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>false</useForRefund><voiceTag>SPONSOR_ID</voiceTag></parameter><parameter><code>122</code><order>3</order><name>IqamaDurationYrs</name><englishName>Iqama Duration in Years</englishName><arabicName>??? ???? ???????</arabicName><required>true</required><type>Number</type><minimumLength>1</minimumLength><maximumLength>2</maximumLength><useForRefund>false</useForRefund><voiceTag>IQAMA_DURATION</voiceTag></parameter><parameter><code>106</code><order>4</order><name>JobCategory</name><englishName>Job Category</englishName><arabicName>??? ??????</arabicName><required>true</required><type>JobCategory_Enum</type><minimumLength>2</minimumLength><maximumLength>2</maximumLength><useForRefund>false</useForRefund><voiceTag>JOB_CATEGORY</voiceTag></parameter><parameter><code>3</code><order>1</order><name>BeneficiaryId</name><englishName>Iqama ID</englishName><arabicName>??? ???????</arabicName><required>true</required><type>Number</type><minimumLength>10</minimumLength><maximumLength>10</maximumLength><useForRefund>true</useForRefund><voiceTag>IQAMA_ID</voiceTag></parameter>  </feeService></feeServices></biller></billers></success></BillersInformationResponse></soapenv:Body></soapenv:Envelope>";
			inputMsg=inputMsg.replaceAll("&lt;","<");
			inputMsg=inputMsg.replaceAll("&gt;",">");
			inputMsg =inputMsg.substring(inputMsg.indexOf("<soapenv:Header", 0));
			System.out.println("inputMsg after removing header ==>"+ inputMsg);
			inputMsg=removeXmlStringNamespaceAndPreamble(inputMsg);
			System.out.println("inputMsg after removing Namespace ==>"+ inputMsg);
			inputMsg=inputMsg.replaceAll("</return>","").replaceAll("</Body>","").replaceAll("</BillersInformationResponse>","").replaceAll("</Envelope>","");
			inputMsg=inputMsg.concat("</BillersInformationResponse>");
			inputMsg=inputMsg.concat("</Body>");
			String[] inputDetails={"<soapenv:Header>","<Header>","<Header >","</Header>","</soapenv:Header>","<header>","<header >","</header>","<BPM>","</BPM>","<soapenv:Body>","<Body>","</Body>","</soapenv:Body>","<BillersInformationResponse>","<BillersInformationResponse >","</BillersInformationResponse>","<success>","</success>","<billerGroups>","</billerGroups>","<billerGroup>","</billerGroup>","<billers>","</billers>","<biller>","</biller>","<paymentServices>","</paymentServices>","<paymentService>","</paymentService>","<feeServices>","</feeServices>","<feeService>","</feeService>","<types>","</types>","<choice>","</choice>","<typeFilters>","</typeFilters>","<typeFilter>","</typeFilter>","<feeServiceErrors>","</feeServiceErrors>","<feeServiceError>","</feeServiceError>","<sadadErrors>","</sadadErrors>","<sadadError>","</sadadError>","<refundTypes>","</refundTypes>","<refundType>","</refundType>","<serviceTypes>","</serviceTypes>","<serviceType>","</serviceType>","<paymentTypes>","</paymentTypes>","<paymentType>","</paymentType>","<billCategories>","</billCategories>","<billCategory>","</billCategory>","<amountRange>","</amountRange>","<voucherServices>","</voucherServices>","<voucherService>","</voucherService>","<fault>","<fault >","</fault>","</ns:BillersInformationResponse>","</Envelope>"};
			String[] outputDetails={"<BILLER_INFO_Header_RESIN>","<BILLER_INFO_Header_RESIN>","<BILLER_INFO_Header_RESIN>","</BILLER_INFO_Header_RESIN>","</BILLER_INFO_Header_RESIN>","<BILLER_INFO_header_RESIN>","<BILLER_INFO_header_RESIN>","</BILLER_INFO_header_RESIN>","<BILLER_INFO_BPM_RESIN>","</BILLER_INFO_BPM_RESIN>","<BI_Body_RESIN>","<BI_Body_RESIN>","</BI_Body_RESIN>","</BI_Body_RESIN>","<BillersInfoRes_RESIN>","<BillersInfoRes_RESIN>","</BillersInfoRes_RESIN>","<BI_Suc_RESIN>","</BI_Suc_RESIN>","<BI_BGS_RESIN>","</BI_BGS_RESIN>","<BI_BG_RESIN>","</BI_BG_RESIN>","<BI_billers_RESIN>","</BI_billers_RESIN>","<BI_biller_RESIN>","</BI_biller_RESIN>","<BI_pymtSers_RESIN>","</BI_pymtSers_RESIN>","<BI_pymtSer_RESIN>","</BI_pymtSer_RESIN>","<BI_feeSers_RESIN>","</BI_feeSers_RESIN>","<BI_feeSer_RESIN>","</BI_feeSer_RESIN>","<BI_Typs_RESIN>","</BI_Typs_RESIN>","<BI_Cho_RESIN>","</BI_Cho_RESIN>","<BI_typeFtrs_RESIN>","</BI_typeFtrs_RESIN>","<BI_typeFtr_RESIN>","</BI_typeFtr_RESIN>","<BI_feeSerErrs_RESIN>","</BI_feeSerErrs_RESIN>","<BI_feeSerErr_RESIN>","</BI_feeSerErr_RESIN>","<BI_sdErrs_RESIN>","</BI_sdErrs_RESIN>","<BI_sdErr_RESIN>","</BI_sdErr_RESIN>","<BI_rfdTyps_RESIN>","</BI_rfdTyps_RESIN>","<BI_rfdTyp_RESIN>","</BI_rfdTyp_RESIN>","<BI_serTyps_RESIN>","</BI_serTyps_RESIN>","<BI_serTyp_RESIN>","</BI_serTyp_RESIN>","<BI_pymtTyps_RESIN>","</BI_pymtTyps_RESIN>","<BI_pymtTyp_RESIN>","</BI_pymtTyp_RESIN>","<BI_billCatgs_RESIN>","</BI_billCatgs_RESIN>","<BI_billCatg_RESIN>","</BI_billCatg_RESIN>","<BI_amtRng_RESIN>","</BI_amtRng_RESIN>","<BI_vouSers_RESIN>","</BI_vouSers_RESIN>","<BI_vouSer_RESIN>","</BI_vouSer_RESIN>","<BILLER_INFO_fault_RESIN>","<BILLER_INFO_fault_RESIN>","</BILLER_INFO_fault_RESIN>","",""};
			for(int i=0;i<inputDetails.length;i++){

				if(inputMsg.indexOf(inputDetails[i])!=-1){

					inputMsg = inputMsg.replaceAll(inputDetails[i],outputDetails[i]);
				}
			}
			//"<parameter>","</parameter>",
			//"<BI_prm_RESIN>","</BI_prm_RESIN>",
			System.out.println("After Replacing ==>"+ inputMsg);
			int startpymtParam =inputMsg.indexOf("<BI_pymtSers_RESIN>");
			System.out.println("startpymtParam ==>"+ startpymtParam);
			int endpymtParam =inputMsg.indexOf("</BI_pymtSers_RESIN>");
			System.out.println("endpymtParam ==>"+ endpymtParam);
			int startfeeParam =inputMsg.indexOf("<BI_feeSers_RESIN>");
			System.out.println("startfeeParam ==>"+ startfeeParam);
			int endfeeParam =inputMsg.indexOf("</BI_feeSers_RESIN>");
			System.out.println("endfeeParam ==>"+ endfeeParam);
			
			String firstString=inputMsg.substring(0, startpymtParam);
			System.out.println("first ==>"+ firstString);
			
			String secString=inputMsg.substring(startpymtParam,endpymtParam);
			System.out.println("2nd==>"+ secString);
			
			String[] pymtinputDetails={"<parameter>","</parameter>"};
			String[] pymtoutputDetails={"<BI_prmpay_RESIN>","</BI_prmpay_RESIN>"};
			for(int i=0;i<pymtinputDetails.length;i++){

				if(secString.indexOf(pymtinputDetails[i])!=-1){

					secString = secString.replaceAll(pymtinputDetails[i],pymtoutputDetails[i]);
				}
			}
			System.out.println("After replace 2nd==>"+ secString);
			
			String thirdString=inputMsg.substring(endpymtParam,startfeeParam);
			System.out.println("3rd==>"+ thirdString);
			
			String fourthString=inputMsg.substring(startfeeParam,endfeeParam);
			System.out.println("4th==>"+ fourthString);
			
			String[] feeinputDetails={"<parameter>","</parameter>"};
			String[] feeoutputDetails={"<BI_prmfee_RESIN>","</BI_prmfee_RESIN>"};
			for(int i=0;i<feeinputDetails.length;i++){

				if(fourthString.indexOf(feeinputDetails[i])!=-1){

					fourthString = fourthString.replaceAll(feeinputDetails[i],feeoutputDetails[i]);
				}
			}
			System.out.println("After replace 4nd==>"+ fourthString);
			
			String fifthString=inputMsg.substring(endfeeParam,inputMsg.length());
			System.out.println("5th==>"+ fifthString);
			
			
			//"<parameter>","</parameter>",
			//"<BI_prm_RESIN>","</BI_prm_RESIN>",
			
			StringBuffer coutputStr =new StringBuffer( inputMsg);

	
	}catch(Exception e){
		System.out.print("test"+e);
	}
	}
		public static String removeXmlStringNamespaceAndPreamble(String xmlString) {
			  return xmlString.replaceAll("(<\\?[^<]*\\?>)?", ""). /* remove preamble */
			  replaceAll(" xmlns.*?(\"|\').*?(\"|\')", "") /* remove xmlns declaration */
			  .replaceAll("(<)(\\w+:)(.*?>)", "$1$3") /* remove opening tag prefix */
			  .replaceAll("(</)(\\w+:)(.*?>)", "$1$3"); /* remove closing tags prefix */
			}

}
