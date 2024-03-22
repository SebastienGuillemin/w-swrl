package com.sebastienguillemin.wswrl.core.rule.variable;

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
     * Return the object class corresponding to a certain
     * {@link org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return The {@link org.semanticweb.owlapi.model.OWLClass} (possibly null).
     */
    public OWLClass getOWLClass(IRI iri);

    /**
     * Remove a class from the indvidual.
     * 
     * @param classAtom The class to remove.
     */
    public void removeOWLClass(OWLClass classAtom);

    /**
     * Add an object property to the indvidual.
     * 
     * @param objectProperty The object porperty to add.
     */
    public void addObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty);

    /**
     * Return the object property corresponding to a certain
     * {@link org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return The
     *         {@link org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom}
     *         (possibly
     *         null).
     */
    public OWLObjectPropertyAssertionAxiom getObjectProperty(IRI iri);

    /**
     * Remove an object property to the indvidual.
     * 
     * @param objectProperty The object porperty to remove.
     */
    public void removeObjectProperty(OWLObjectPropertyAssertionAxiom objectProperty);

    /**
     * Add a data property to the indvidual.
     * 
     * @param dataProperty The data porperty to add.
     */
    public void addDataProperty(OWLDataPropertyAssertionAxiom dataProperty);

    /**
     * Return the data property corresponding to a certain{ @link
     * org.semanticweb.owlapi.model.IRI}.
     * 
     * @param iri The property IRI.
     * @return The
     *         {@link org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom}
     *         (possibly
     *         null).
     */
    public OWLDataPropertyAssertionAxiom getDataProperty(IRI iri);

    /**
     * Remove a data property to the indvidual.
     * 
     * @param dataProperty The data porperty to remove.
     */
    public void removeDataProperty(OWLDataPropertyAssertionAxiom dataProperty);
}