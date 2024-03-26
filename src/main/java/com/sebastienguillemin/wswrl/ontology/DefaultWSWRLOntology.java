package com.sebastienguillemin.wswrl.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLUnaryAtom;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.factory.WSWRLInternalFactory;
import com.sebastienguillemin.wswrl.parser.WSWRLParser;

import lombok.Getter;
import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyAssertionAxiomImpl;

/**
 * This class extends the {@link DefaultSWRLAPIOWLOntology} to adapt it to
 * WSWRL.
 */
public class DefaultWSWRLOntology extends DefaultSWRLAPIOWLOntology implements WSWRLOntology {

    @Getter
    private class InferredAxiom implements Comparable<InferredAxiom> {
        private OWLAxiom axiom;
        private float confidence;

        public InferredAxiom(OWLAxiom axiom, float confidence) {
            this.axiom = axiom;
            this.confidence = confidence;
        }

        @Override
        public String toString() {
            return this.axiom.toString() + ", Confidence : " + this.confidence;
        }

        @Override
        public int compareTo(InferredAxiom other) {
            return this.axiom.compareTo(other.getAxiom());
        }
    }

    private final Map<String, WSWRLRule> wswrlRules;
    private WSWRLDataFactory wswrlDataFactory;
    private Set<InferredAxiom> inferredAxioms;

    /**
     * Constructor.
     * 
     * @param ontology    An OWL ontology (used by the super class
     *                    {@link DefaultSWRLAPIOWLOntology}).
     * @param iriResolver An IRI resolver (used by the super class
     *                    {@link DefaultSWRLAPIOWLOntology}).
     */
    public DefaultWSWRLOntology(OWLOntology ontology, IRIResolver iriResolver) {
        super(ontology, iriResolver);
        this.wswrlRules = new HashMap<>();
        this.wswrlDataFactory = WSWRLInternalFactory.createWSWRLDataFactory(iriResolver);
        this.inferredAxioms = new HashSet<>();
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule)
            throws WSWRLParseException, MissingRankException {
        WSWRLRule wswrlRule = this.createWSWRLRule(ruleName, rule, "", true);

        this.wswrlRules.put(ruleName, wswrlRule);

        return wswrlRule;
    }

    @Override
    public WSWRLRule createWSWRLRule(String ruleName, String rule, String comment, boolean isActive)
            throws WSWRLParseException, MissingRankException {

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

    @Override
    public void addInferredAxiom(Set<WSWRLAtom> atoms, float confidence) {
        InferredAxiom inferredAxiom;
        OWLAxiom owlAxiom;
        for (WSWRLAtom atom : atoms) {
            inferredAxiom = null;
            owlAxiom = null;

            if (atom instanceof WSWRLObjectPropertyAtom) {
                WSWRLObjectPropertyAtom opAtom = (WSWRLObjectPropertyAtom) atom;
                owlAxiom = new OWLObjectPropertyAssertionAxiomImpl(opAtom.getSubject().getValue(),
                        (OWLObjectPropertyExpression) opAtom.getPredicate(), opAtom.getObject().getValue(),
                        new HashSet<>());
            } else if (atom instanceof WSWRLDataPropertyAtom) {
                WSWRLDataPropertyAtom dpAtom = (WSWRLDataPropertyAtom) atom;
                owlAxiom = new OWLDataPropertyAssertionAxiomImpl(dpAtom.getSubject().getValue(),
                        (OWLDataPropertyExpression) dpAtom.getPredicate(), dpAtom.getObject().getValue(),
                        new HashSet<>());
            } else if (atom instanceof WSWRLUnaryAtom) {
                WSWRLClassAtom cAtom = (WSWRLClassAtom) atom;
                owlAxiom = new OWLClassAssertionAxiomImpl(cAtom.getWSWRLArgument().getValue(),
                        (OWLClassExpression) cAtom.getPredicate(), new HashSet<>());
            } // TODO : traiter le cas des built-in et des data ranges dans la tête de la
              // règle.

            inferredAxiom = new InferredAxiom(owlAxiom, confidence);
            this.inferredAxioms.add(inferredAxiom);
        }
    }

    @Override
    public Set<OWLAxiom> getInferredAxioms() {
        Set<OWLAxiom> owlAxioms = new HashSet<>();

        for (InferredAxiom inferredAxiom : this.inferredAxioms)
            owlAxioms.add(inferredAxiom.getAxiom());

        return owlAxioms;
    }

    @Override
    public void clearInferredAxioms() {
        this.inferredAxioms = new HashSet<>();
    }
}