package com.sebastienguillemin.wswrl.rule.variable;

import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.OWLNamedIndividual;

import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
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
    private Set<WSWRLClassAtom> classes;
    private Set<WSWRLObjectPropertyAtom> objectProperties;
    private Set<WSWRLDataPropertyAtom> dataProperties;

    public DefaultWSWRLIndividual(OWLNamedIndividual owlNamedIndividual) {
        super(owlNamedIndividual.getIRI());
        this.classes = new HashSet<>();
        this.objectProperties = new HashSet<>();
        this.dataProperties = new HashSet<>();
    }

    @Override
    public void addClass(WSWRLClassAtom classAtom) {
        this.classes.add(classAtom);
    }

    @Override
    public void removeClass(WSWRLClassAtom classAtom) {
        this.classes.remove(classAtom);
    }

    public void addObjectProperty(WSWRLObjectPropertyAtom objectProperty) {
        this.objectProperties.add(objectProperty);
    }

    public void removeObjectProperty(WSWRLObjectPropertyAtom objectProperty) {
        this.objectProperties.remove(objectProperty);
    }

    public void addDataProperty(WSWRLDataPropertyAtom dataProperty) {
        this.dataProperties.add(dataProperty);
    }

    public void removeDataProperty(WSWRLDataPropertyAtom dataProperty) {
        this.dataProperties.remove(dataProperty);
    }

    @Override
    public String toString() {
        return    "Individual " + this.getIRI().toString()
                + "\n  Classes : " + this.classes
                + "\n  Object properties : " + this.objectProperties
                + "\n  Data properties : " + this.dataProperties;
    }
}
