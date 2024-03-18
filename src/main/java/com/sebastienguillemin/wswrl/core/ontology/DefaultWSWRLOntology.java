package com.sebastienguillemin.wswrl.core.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.factory.WSWRLInternalFactory;
import com.sebastienguillemin.wswrl.core.parser.WSWRLParser;

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
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        WSWRLRule wswrlRule = this.createWSWRLRule(ruleName, rule, "", true);

        this.wswrlRules.put(ruleName, wswrlRule);

        return wswrlRule;
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {

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
