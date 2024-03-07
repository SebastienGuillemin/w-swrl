package com.sebastienguillemin.wswrl.rule;

import java.util.ArrayList;
import java.util.List;

import com.sebastienguillemin.wswrl.rule.literal.Literal;

/**
 * 
 */
public class Rule {
   private List<Literal> body;
   private Literal head;

   public Rule() {
    this.body = new ArrayList<>();
   }
}
