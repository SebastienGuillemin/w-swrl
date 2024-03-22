package com.sebastienguillemin.wswrl.rule.variable;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
import com.sebastienguillemin.wswrl.engine.target.DefaultTargetWSWRLRuleEngine;

import lombok.Getter;
import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

/**
 * This class encapsulates OWL individual data to facilitate the infer procedure
 * implementation.
 * 
 * @see DefaultTargetWSWRLRuleEngine
 */
@Getter
public class DefaultWSWRLIndividual extends OWLNamedIndividualImpl implements WSWRLIndividual {
    private Set<OWLClass> classes;
    private Set<OWLObjectPropertyAssertionAxiom> objectProperties;
    private Set<OWLDataPropertyAssertionAxiom> dataProperties;

    public DefaultWSWRLIndividual(OWLNamedIndividual owlNamedIndividual) {
        super(owlNamedIndividual.getIRI());
        this.classes = new HashSet<>();
        this.objectProperties = new HashSet<>();
        this.dataProperties = new HashSet<>();
    }

    @Override
    public void addOWLClass(OWLClass classAtom) {
        this.classes.add(classAtom);
    }

    @Override
    public OWLClass getOWLClass(IRI iri) {
        for (OWLClass atom : this.classes)
            if (atom.getIRI().equals(iri))
                return atom;

        return null;
    }

    @Override
    public void removeOWLClass(OWLClass classAtom) {
        this.classes.remove(classAtom);
    }

    public void addObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty) {
        // System.out.println(this.getIRI() + " adding : " + objectProperty.getIRI());
        this.objectProperties.add(objectProperty);
    }

    @Override
    public OWLObjectPropertyAssertionAxiom getObjectProperty(IRI iri) {
        for (OWLObjectPropertyAssertionAxiom atom : this.objectProperties)
            if (atom.getProperty().asOWLObjectProperty().getIRI().equals(iri))
                return atom;

        return null;
    }

    public void removeObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty) {
        this.objectProperties.remove(objectProperty);
    }

    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty) {
        this.dataProperties.add(dataProperty);
    }

    @Override
    public OWLDataPropertyAssertionAxiom getDataProperty(IRI iri) {
        for (OWLDataPropertyAssertionAxiom atom : this.dataProperties)
            if (atom.getProperty().asOWLDataProperty().getIRI().equals(iri))
                return atom;

        return null;
    }

    public void removeDataProperty(OWLDataPropertyAssertionAxiom dataProperty) {
        this.dataProperties.remove(dataProperty);
    }

    @Override
    public String toString() {
        return "Individual " + this.getIRI().toString()
                + "\n  Classes : " + this.classes
                + "\n  Object properties : " + this.objectProperties
                + "\n  Data properties : " + this.dataProperties;
    }
}
