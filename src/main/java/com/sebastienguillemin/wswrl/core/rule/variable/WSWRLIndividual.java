package com.sebastienguillemin.wswrl.core.rule.variable;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

/**
 * Interface for WSWRL individual.
 */
public interface WSWRLIndividual extends OWLIndividual, HasIRI {

    /**
     * Add a class to the indvidual.
     * 
     * @param classAtom The class to add.
     */
    public void addOWLClass(OWLClass classAtom);

    /**
     * Return the indivudal object class corresponding to a certain
     * {@link org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return The {@link org.semanticweb.owlapi.model.OWLClass} (possibly null).
     */
    public OWLClass getOWLClass(IRI iri);

    public List<OWLClass> getOWLClasses();

    /**
     * Add an object property to the indvidual.
     * 
     * @param objectProperty The object porperty to add.
     */
    public void addObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty);

    /**
     * Return the individual object properties corresponding to a certain
     * {@link org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return A set (possibly empty) containing
     *         {@link org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom}
     *         instances.
     */
    public Set<OWLObjectPropertyAssertionAxiom> getObjectProperties(IRI iri);

    /**
     * Add a data property to the indvidual.
     * 
     * @param dataProperty The data porperty to add.
     */
    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty);

    /**
     * Return the individual data properties corresponding to a certain{ @link
     * org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return A set (possibly empty) containing
     *         {@link org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom}
     *         instances.
     */
    public Set<OWLDataPropertyAssertionAxiom> getDataProperties(IRI iri);
}