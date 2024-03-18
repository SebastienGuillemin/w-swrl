package com.sebastienguillemin.wswrl.core.exception;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.swrlapi.exceptions.SWRLRuleException;

public class WSWRLRuleException extends SWRLRuleException{
    public WSWRLRuleException(@NonNull String message)
  {
    super(message);
  }

  public WSWRLRuleException(@NonNull String message, @NonNull Throwable cause)
  {
    super(message, cause);
  }
}
