package com.polaris.integrator.UserExit.Rules;

import com.polaris.integrator.tre.info.NVPHolder;
import java.util.HashMap;

public abstract interface IUserImpl
{
  public abstract HashMap checkRule(NVPHolder paramNVPHolder1, NVPHolder paramNVPHolder2);
}