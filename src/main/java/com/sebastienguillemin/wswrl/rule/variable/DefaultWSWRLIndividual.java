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
    public Set<OWLObjectPropertyAssertionAxiom> getObjectProperties(IRI iri) {
        Set<OWLObjectPropertyAssertionAxiom> properties = new HashSet<>();

        for (OWLObjectPropertyAssertionAxiom axiom : this.objectProperties)
            if (axiom.getProperty().asOWLObjectProperty().getIRI().equals(iri))
                properties.add(axiom);

        return properties;
    }

    public void removeObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty) {
        this.objectProperties.remove(objectProperty);
    }

    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty) {
        this.dataProperties.add(dataProperty);
    }

    @Override
    public Set<OWLDataPropertyAssertionAxiom> getDataProperties(IRI iri) {
        Set<OWLDataPropertyAssertionAxiom> properties = new HashSet<>();

        for (OWLDataPropertyAssertionAxiom axiom : this.dataProperties)
            if (axiom.getProperty().asOWLDataProperty().getIRI().equals(iri))
                properties.add(axiom);

        return properties;
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
