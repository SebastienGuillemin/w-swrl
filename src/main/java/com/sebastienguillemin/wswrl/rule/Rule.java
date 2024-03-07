package com.sebastienguillemin.wswrl.rule;

import java.util.ArrayList;
import java.util.List;

import com.sebastienguillemin.wswrl.rule.literal.Literal;
import com.sebastienguillemin.wswrl.rule.literal.Rank;

import lombok.Getter;

@Getter
public class Rule {
   private List<Literal> body;
   private Literal head;
   private List<Rank> ranks;

   public Rule() {
    this.body = new ArrayList<>();
    this.ranks = new ArrayList<>();
   }
}
