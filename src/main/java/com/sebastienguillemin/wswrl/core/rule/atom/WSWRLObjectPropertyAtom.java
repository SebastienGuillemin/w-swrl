package com.sebastienguillemin.wswrl.core.rule.atom;

import java.util.Hashtable;

import org.semanticweb.owlapi.model.IRI;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;

public interface WSWRLObjectPropertyAtom extends WSWRLPropertyAtom<WSWRLIVariable> {

    /**
     * Set the property object using the ontology individuals.
     * 
     * @see com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology
     * 
     * @param individuals Ontology individuals.
     */
    public void parseObject(Hashtable<IRI, WSWRLIndividual> individuals);
}
