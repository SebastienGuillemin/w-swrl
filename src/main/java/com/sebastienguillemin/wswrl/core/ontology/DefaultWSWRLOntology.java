package com.sebastienguillemin.wswrl.core.ontology;

import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.WSWRLRule;
import com.sebastienguillemin.wswrl.core.exception.AlreadyInRankException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.core.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.factory.WSWRLInternalFactory;
import com.sebastienguillemin.wswrl.core.parser.WSWRLParser;

public class DefaultWSWRLOntology extends DefaultSWRLAPIOWLOntology implements WSWRLOntology {
    private WSWRLDataFactory wswrlDataFactory;

    public DefaultWSWRLOntology(@NonNull OWLOntology ontology, @NonNull IRIResolver iriResolver) {
        super(ontology, iriResolver);
        this.wswrlDataFactory = WSWRLInternalFactory.createWSWRLDataFactory(iriResolver);
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
        return this.createWSWRLRule(ruleName, rule, "", true);
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
    public WSWRLDataFactory getWSWRLDataFactory() {
        return this.wswrlDataFactory;
    }
}
