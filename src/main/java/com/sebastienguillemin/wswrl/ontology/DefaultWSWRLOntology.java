package com.sebastienguillemin.wswrl.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.exceptions.SWRLBuiltInException;

import com.sebastienguillemin.wswrl.core.factory.WSWRLDataFactory;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLAxiom;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLUnaryAtom;
import com.sebastienguillemin.wswrl.exception.MissingRankException;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.exception.WSWRLParseException;
import com.sebastienguillemin.wswrl.factory.WSWRLInternalFactory;
import com.sebastienguillemin.wswrl.parser.WSWRLParser;
import com.sebastienguillemin.wswrl.rule.DefaultWSWRLAxiom;

import uk.ac.manchester.cs.owl.owlapi.OWLClassAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyAssertionAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyAssertionAxiomImpl;

/**
 * This class extends the {@link DefaultSWRLAPIOWLOntology} to adapt it to
 * WSWRL.
 * 
 * {@inheritDoc}
 */
public class DefaultWSWRLOntology extends DefaultSWRLAPIOWLOntology implements WSWRLOntology {
    private final Map<String, WSWRLRule> wswrlRules;
    private WSWRLDataFactory wswrlDataFactory;
    private Set<WSWRLAxiom> inferredAxiomsCache;
    private IRI baseIRI;

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
        this.inferredAxiomsCache = new HashSet<>();
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
    public void addWSWRLInferredAxiom(Set<WSWRLAtom> atoms, float confidence) {
        WSWRLAxiom inferredAxiom;
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
              // règle (?)

            inferredAxiom = new DefaultWSWRLAxiom(owlAxiom, confidence);
            this.inferredAxiomsCache.add(inferredAxiom);
        }
    }

    @Override
    public Set<OWLAxiom> getWSWRLInferredAxiomsAsOWLAxiom() {
        Set<OWLAxiom> owlAxioms = new HashSet<>();

        for (WSWRLAxiom inferredAxiom : this.inferredAxiomsCache)
            owlAxioms.add(inferredAxiom.getAxiom());

        return owlAxioms;
    }

    @Override
    public Set<WSWRLAxiom> getWSWRLInferredAxioms() {
        return this.inferredAxiomsCache;
    }

    @Override
    public void clearInferredAxiomsCache() {
        this.inferredAxiomsCache.clear();
    }

    @Override
    public void processOntology() throws WSWRLBuiltInException {
        try {
            super.processOntology();
        } catch (SWRLBuiltInException e) {
            throw new WSWRLBuiltInException(e.getMessage());
        }
    }

    @Override
    public void filterInferredAxioms(float threshold) {
        WSWRLAxiom inferredAxiom;
        for (Iterator<WSWRLAxiom> i = this.inferredAxiomsCache.iterator(); i.hasNext();) {
            inferredAxiom = i.next();
            if (inferredAxiom.getConfidence() < threshold)
                i.remove();
        }
    }

    @Override
    public IRI getBaseIRI() {
        if (this.baseIRI != null)
            return this.baseIRI;

        String iri = "";
        for (OWLAxiom axiom : this.getOWLAxioms()) {
            if (axiom instanceof OWLClassAssertionAxiom) {
                iri = ((OWLClassAssertionAxiom) axiom).getClassExpression().asOWLClass().getIRI().getNamespace();
            } else if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
                iri = ((OWLObjectPropertyAssertionAxiom) axiom).getSubject().asOWLNamedIndividual().getIRI()
                        .getNamespace();
            } else if (axiom instanceof OWLDataPropertyAssertionAxiom) {
                iri = ((OWLDataPropertyAssertionAxiom) axiom).getSubject().asOWLNamedIndividual().getIRI()
                        .getNamespace();
            } else if (axiom instanceof OWLDeclarationAxiom) {
                iri = ((OWLDeclarationAxiom) axiom).getEntity().getIRI().getNamespace();
            }
        }

        this.baseIRI = IRI.create(iri);
        return this.baseIRI;
    }
}