package com.sebastienguillemin.wswrl.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.factory.WSWRLInternalFactory;
import com.sebastienguillemin.wswrl.parser.WSWRLParser;

import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;

public class DefaultWSWRLOntology extends DefaultSWRLAPIOWLOntology implements WSWRLOntology {
    private final Map<String, WSWRLRule> wswrlRules;
    private WSWRLDataFactory wswrlDataFactory;

    public DefaultWSWRLOntology(OWLOntology ontology, IRIResolver iriResolver) {
        super(ontology, iriResolver);
        this.wswrlRules = new HashMap<>();
        this.wswrlDataFactory = WSWRLInternalFactory.createWSWRLDataFactory(iriResolver);

        // this.processWSWRLRules();
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, WSWRLBuiltInException, MissingRankException {
        WSWRLRule wswrlRule = this.createWSWRLRule(ruleName, rule, "", true);

        this.wswrlRules.put(ruleName, wswrlRule);

        return wswrlRule;
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, MissingRankException {

        WSWRLParser parser = new WSWRLParser(this);

        Optional<WSWRLRule> wswrlRule = parser.parseWSWRLRule(ruleName, rule, comment, false);

        if (wswrlRule.isPresent()) {
            return wswrlRule.get();
        }

        return null;
    }

    @Override
    public Set<WSWRLRule> getWSWRLRules() {
        return new HashSet<WSWRLRule>(this.wswrlRules.values());
    }

    @Override
    public WSWRLDataFactory getWSWRLDataFactory() {
        return this.wswrlDataFactory;
    }
}
