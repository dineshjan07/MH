import java.io.PrintStream;
import java.util.ResourceBundle;

public class WS_Engine_stub
{

    public WS_Engine_stub()
    {
    }

    public static String getBusinessDate(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getBusinessDate");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getBusinessDate");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String getRelationshipList(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getRelationshipList");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getRelationshipList");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    
    
    public static String getDenomination(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getDenomination");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getDenomination");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    
    public static String processCreditCardPayment(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside processCreditCardPayment");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("processCreditCardPayment");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    //
    public static String BillInq(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside BillInq");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("BillInq");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String BalanceEnquiry(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside BalanceEnquiry");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("BalanceEnquiry");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String getIDLists(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getIDLists");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getIDLists");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }

    public static String getAccountMail(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getAccountMail");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getAccountMail");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }

    public static String getprocessBlockOrUnblockAmountInput(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getprocessBlockOrUnblockAmountInput");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getprocessBlockOrUnblockAmountInput");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }

    public static String getprocessGiftCardPurchase(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getprocessGiftCardPurchase");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getprocessGiftCardPurchase");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }

    public static String getprocessAlAwwalTransfer(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getprocessAlAwwalTransfer");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getprocessAlAwwalTransfer");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    public static String getSewaBillAmount(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside getSewaBillAmount");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("getSewaBillAmount");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    public static String GetexchangeMessage(String sReq)
    {
    	String sRes = null;
        try
        {
            System.out.println("Inside GetexchangeMessage");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("GetexchangeMessage");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
  //processEmailSubscription
    public static String GetprocessEmailSubscription(String sReq)
    {
    	String sRes = null;
        try
        {
            System.out.println("Inside GetprocessEmailSubscription");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("GetprocessEmailSubscription");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    public static String fundpayment(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside fundpayment");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("fundpayment");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String chargerecord(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside chargerecord");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("chargerecord");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String CustomerCreation(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside chargerecord");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("ReplaceCard");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
    public static String BfdVerification(String sReq)
    {
        String sRes = null;
        try
        {
            System.out.println("Inside chargerecord");
            ResourceBundle prop = ResourceBundle.getBundle("response");
            System.out.println((new StringBuilder("Message to the Webservice --->>>")).append(sReq).toString());
            sRes = prop.getString("BfdVerification");
            System.out.println((new StringBuilder("Response from Webservice --->>>")).append(sRes).toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return sRes;
    }
    
}
