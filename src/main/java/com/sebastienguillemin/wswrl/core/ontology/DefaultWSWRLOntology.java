package com.sebastienguillemin.wswrl.core.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;

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
    public WSWRLRule createWSWRLRule(String ruleName, String rule) throws WSWRLParseException, WSWRLBuiltInException, AlreadyInRankException {
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

    // private void processWSWRLRules() throws SWRLBuiltInException {
        // int ruleNameIndex = 0;

        // this.wswrlRules.clear();

        // for (SWRLRule owlapiRule : getOWLOntology().getAxioms(AxiomType.SWRL_RULE, Imports.INCLUDED)) {
        //     Optional<String> ruleName = getRuleName(owlapiRule);
        //     boolean isActive = getIsRuleEnabled(owlapiRule);
        //     String comment = getRuleComment(owlapiRule);
        //     String finalRuleName = ruleName.isPresent() ? ruleName.get() : "S" + ++ruleNameIndex;

        //     owlapiRule

        //     SWRLAPIRule swrlapiRule = convertOWLAPIRule2SWRLAPIRule(owlapiRule, finalRuleName, comment, isActive);

        //     this.wswrlRules.put(finalRuleName, swrlapiRule);

        //     if (swrlapiRule.isSQWRLQuery()) {
        //         SQWRLQuery query = createSQWRLQueryFromSWRLRule(swrlapiRule);
        //         this.sqwrlQueries.put(finalRuleName, query);
        //     }
        //     // TODO Do we want to add axioms to OWLAPI rule that does not have them?
        //     // generateRuleAnnotations(ruleName, comment, true)
        //     // ontologyManager.removeAxiom(ontology, owlapiRule); // Remove the original
        //     // annotated rule
        //     // ontologyManager.addAxiom(ontology, annotatedOWLAPIRule); // Replace with
        //     // annotated rule
        // }
    // }

    private Optional<String> getRuleName(SWRLRule owlapiRule) {
        OWLAnnotationProperty labelAnnotation = getOWLDataFactory()
                .getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_LABEL.getIRI());

        for (OWLAnnotation annotation : owlapiRule.getAnnotations(labelAnnotation)) {
            if (annotation.getValue() instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) annotation.getValue();
                return Optional.of(literal.getLiteral()); // We pick the first one
            }
        }
        return Optional.<String>empty();
    }

    // private boolean getIsRuleEnabled(SWRLRule owlapiRule) {
        // OWLAnnotationProperty enabledAnnotationProperty = getOWLDataFactory()
        //         .getOWLAnnotationProperty(
        //                 IRI.create("http://swrl.stanford.edu/ontologies/3.3/swrla.owl#isRuleEnabled"));

        // for (OWLAnnotation annotation : owlapiRule.getAnnotations(enabledAnnotationProperty)) {
        //     if (annotation.getValue() instanceof OWLLiteral) {
        //         OWLLiteral literal = (OWLLiteral) annotation.getValue();
        //         if (literal.isBoolean())
        //             return literal.parseBoolean();
        //     }
        // }
    //     return true;
    // }

    private String getRuleComment(SWRLRule owlapiRule) {
        OWLAnnotationProperty commentAnnotationProperty = getOWLDataFactory()
                .getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI());

        for (OWLAnnotation annotation : owlapiRule.getAnnotations(commentAnnotationProperty)) {
            if (annotation.getValue() instanceof OWLLiteral) {
                OWLLiteral literal = (OWLLiteral) annotation.getValue();
                return literal.getLiteral(); // We pick the first one
            }
        }
        return "";
    }
}
