package com.sebastienguillemin.wswrl.rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;

import uk.ac.manchester.cs.owl.owlapi.OWLNamedIndividualImpl;

/**
 * {@inheritDoc}
 */
public class DefaultWSWRLIndividual extends OWLNamedIndividualImpl implements WSWRLIndividual {
    private Hashtable<IRI, OWLClass> owlClasses;
    private Hashtable<IRI, Set<OWLDataPropertyAssertionAxiom>> dataProperties;
    private Hashtable<IRI, Set<OWLObjectPropertyAssertionAxiom>> objectProperties;

    /**
     * Constructor.
     * 
     * @param owlNamedIndividual An OWL named individual used to create this WSWRL
     *                           individual.
     */
    public DefaultWSWRLIndividual(OWLNamedIndividual owlNamedIndividual) {
        super(owlNamedIndividual.getIRI());
        this.owlClasses = new Hashtable<>();
        this.dataProperties = new Hashtable<>();
        this.objectProperties = new Hashtable<>();
    }

    @Override
    public void addOWLClass(OWLClass classAtom) {
        this.owlClasses.put(classAtom.getIRI(), classAtom);
    }

    @Override
    public OWLClass getOWLClass(IRI iri) {
        return this.owlClasses.get(iri);
    }

    @Override
    public List<OWLClass> getOWLClasses() {
        return new ArrayList<>(this.owlClasses.values());
    }

    public void addObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty) {
        IRI iri = objectProperty.getProperty().asOWLObjectProperty().getIRI();
        Set<OWLObjectPropertyAssertionAxiom> objectPropertiesSet = this.objectProperties.get(iri);

        if (objectPropertiesSet == null) {
            objectPropertiesSet = new HashSet<>();
            this.objectProperties.put(iri, objectPropertiesSet);
        }

        objectPropertiesSet.add(objectProperty);
    }

    @Override
    public Set<OWLObjectPropertyAssertionAxiom> getObjectProperties(IRI iri) {
        return Optional.ofNullable(this.objectProperties.get(iri)).orElse(new HashSet<>());
    }

    @Override
    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty) {
        IRI iri = dataProperty.getProperty().asOWLDataProperty().getIRI();
        Set<OWLDataPropertyAssertionAxiom> dataPropertiesSet = this.dataProperties.get(iri);

        if (dataPropertiesSet == null) {
            dataPropertiesSet = new HashSet<>();
            this.dataProperties.put(iri, dataPropertiesSet);
        }

        dataPropertiesSet.add(dataProperty);
    }

    @Override
    public Set<OWLDataPropertyAssertionAxiom> getDataProperties(IRI iri) {
        return Optional.ofNullable(this.dataProperties.get(iri)).orElse(new HashSet<>());
    }

    @Override
    public String toString() {
        return "Individual " + this.getIRI().toString()
                + "\n  Classes : " + this.owlClasses.values()
                + "\n  Object properties : " + this.objectProperties
                + "\n  Data properties : " + this.dataProperties;
    }
}
