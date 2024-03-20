package com.sebastienguillemin.wswrl.engine.target.wrapper;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import com.sebastienguillemin.wswrl.engine.target.DefaultTargetWSWRLRuleEngine;

import lombok.Getter;

/**
 * This class encapsulates OWL individual data to facilitate the infer procedure
 * implementation.
 * 
 * @see DefaultTargetWSWRLRuleEngine
 */
@Getter
public class WSWRLIndividualWrapper extends WSWRLAbastractWrapper {
    private Set<OWLClass> classes;
    private Set<WSWRLObjectPropertyWrapper> objectProperties;
    private Set<WSWRLDataPropertyWrapper> dataProperties;

    public WSWRLIndividualWrapper(OWLNamedIndividual owlNamedIndividual) {
        super(owlNamedIndividual);
        this.classes = new HashSet<>();
        this.objectProperties = new HashSet<>();
        this.dataProperties = new HashSet<>();
    }

    public void addClass(OWLClass owlClass) {
        this.classes.add(owlClass);
    }

    public void removeClass(OWLClass owlClass) {
        this.classes.remove(owlClass);
    }

    public void addObjectProperty(WSWRLObjectPropertyWrapper objectProperty) {
        this.objectProperties.add(objectProperty);
    }

    public void removeObjectProperty(WSWRLObjectPropertyWrapper objectProperty) {
        this.objectProperties.remove(objectProperty);
    }

    public void addDataProperty(WSWRLDataPropertyWrapper dataProperty) {
        this.dataProperties.add(dataProperty);
    }

    public void removeDataProperty(WSWRLDataPropertyWrapper dataProperty) {
        this.dataProperties.remove(dataProperty);
    }

    @Override
    public String toString() {
        return    "Individual " + this.iri.toString()
                + "\n  Classes : " + this.classes
                + "\n  Object properties : " + this.objectProperties
                + "\n  Data properties : " + this.dataProperties;
    }
}
