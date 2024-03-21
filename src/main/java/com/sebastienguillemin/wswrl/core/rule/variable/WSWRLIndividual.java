package com.sebastienguillemin.wswrl.core.rule.variable;

import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.OWLIndividual;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;

/**
 * Interface for WSWRL individual.
 */
public interface WSWRLIndividual extends OWLIndividual, HasIRI {

    /**
     * Add a class to the indvidual.
     * @param classAtom The class to add.
     */
    public void addClass(WSWRLClassAtom classAtom);
    
    /**
     * Remove a class from the indvidual.
     * @param classAtom The class to remove.
     */
    public void removeClass(WSWRLClassAtom classAtom);

    /**
     * Add an object property to the indvidual.
     * @param objectProperty The object porperty to add.
     */
    public void addObjectProperty(WSWRLObjectPropertyAtom objectProperty);

    /**
     * Remove an object property to the indvidual.
     * @param objectProperty The object porperty to remove.
     */
    public void removeObjectProperty(WSWRLObjectPropertyAtom objectProperty);

    /**
     * Add a data property to the indvidual.
     * @param dataProperty The data porperty to add.
     */
    public void addDataProperty(WSWRLDataPropertyAtom dataProperty);

    /**
     * Remove a data property to the indvidual.
     * @param dataProperty The data porperty to remove.
     */
    public void removeDataProperty(WSWRLDataPropertyAtom dataProperty);
}