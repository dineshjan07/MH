package com.polaris.integrator.UserExit.Rules;

import com.polaris.integrator.tre.info.NVPHolder;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBXCBS_UtilityPay_Rule
  implements IUserImpl
{

  private static Logger log = LoggerFactory.getLogger(RBXCBS_UtilityPay_Rule.class);
  
  public HashMap checkRule(NVPHolder holdnvp, NVPHolder addlholdnvp)
  {
      
      HashMap ruleRes = new HashMap();
      ruleRes.put("RULE_STATUS", Boolean.valueOf(true));
      ruleRes.put("RULE_RECURSIVE_STATUS", Boolean.valueOf(false));
    try
    {
     
      String request = addlholdnvp.getStringRequest();
      log.debug("Request message: RBXCBS_UtilityPay_Rule_REQ====> " + request);
      
      String response = holdnvp.getStringRequest();
      log.debug("Response from: RBXCBS_UtilityPay_Rule_RES====> " + response);
      
      String rescode = holdnvp.get("res_Result_Code");
      log.debug("rescode--->" + rescode);
      
      HashMap applicationparameter = addlholdnvp.getApplicationParameter();
      
      String finalVar = "";
      if (rescode.equals("00000"))
      {

          StringBuffer req = new StringBuffer( request );
          log.debug( "Request Message : " + req );
          StringBuffer res = new StringBuffer( response );
          log.debug( "Response Message : " + res );
          res =
              res.delete( res.indexOf( "res_Result_Code=00000~*" ), res.indexOf( "res_Result_Code=00000~*" ) + 23 );

          finalVar = res.toString();
          log.debug( "Message from 1st SubTransaction to 2nd SubTransaction1 : " + finalVar );
          finalVar = request;
          log.debug( "Message from 1st SubTransaction to 2nd SubTransaction2 : " + finalVar );
          addlholdnvp.init( finalVar );
      }
      else
      {
          ruleRes.put("RULE_STATUS", Boolean.valueOf(false));
          return ruleRes;
      }
  }
    catch (Exception e)
    {
      ruleRes.put("RULE_STATUS", Boolean.valueOf(false));
      ruleRes.put("RULE_RECURSIVE_STATUS", Boolean.valueOf(false));
      log.debug("Exception in RBXCBS_UtilityPay_Rule: ", e);
    }
   
  return ruleRes;
}
}