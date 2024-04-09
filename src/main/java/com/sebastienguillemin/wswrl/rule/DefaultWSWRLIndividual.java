package com.sebastienguillemin.wswrl.rule;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

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
    private Hashtable<IRI, List<OWLDataPropertyAssertionAxiom>> dataProperties;
    private Hashtable<IRI, List<OWLObjectPropertyAssertionAxiom>> objectProperties;

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
        List<OWLObjectPropertyAssertionAxiom> objectPropertiesList = this.objectProperties.get(iri);

        if (objectPropertiesList == null) {
            objectPropertiesList = new ArrayList<>();
            this.objectProperties.put(iri, objectPropertiesList);
        }

        objectPropertiesList.add(objectProperty);
    }

    @Override
    public List<OWLObjectPropertyAssertionAxiom> getObjectProperties(IRI iri) {
        return Optional.ofNullable(this.objectProperties.get(iri)).orElse(new ArrayList<>());
    }

    @Override
    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty) {
        IRI iri = dataProperty.getProperty().asOWLDataProperty().getIRI();
        List<OWLDataPropertyAssertionAxiom> dataPropertiesList = this.dataProperties.get(iri);

        if (dataPropertiesList == null) {
            dataPropertiesList = new ArrayList<>();
            this.dataProperties.put(iri, dataPropertiesList);
        }

        dataPropertiesList.add(dataProperty);
    }

    @Override
    public List<OWLDataPropertyAssertionAxiom> getDataProperties(IRI iri) {
        return Optional.ofNullable(this.dataProperties.get(iri)).orElse(new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Individual " + this.getIRI().toString()
                + "\n  Classes : " + this.owlClasses.values()
                + "\n  Object properties : " + this.objectProperties
                + "\n  Data properties : " + this.dataProperties;
    }
}
