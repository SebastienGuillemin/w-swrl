package com.sebastienguillemin.wswrl.rule.atom.builtin;

import java.util.List;

import org.semanticweb.owlapi.model.SWRLPredicate;

import com.sebastienguillemin.wswrl.core.Rank;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLBuiltInAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.rule.atom.AbstractWSWRLAtom;

public abstract class AbstractWSWRLBuiltInAtom extends AbstractWSWRLAtom implements WSWRLBuiltInAtom {
    protected List<WSWRLVariable> arguments;

    protected AbstractWSWRLBuiltInAtom(SWRLPredicate predicate, Rank rank) {
        super(predicate, rank);
    }

    public List<WSWRLVariable> getArguments() {
        return this.arguments;
    }    
}
