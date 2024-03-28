package com.sebastienguillemin.wswrl.core.ontology;

import java.io.InputStream;

import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;

/**
 * An manager to deal with WSWRL ontology that adapt
 * {@link org.semanticweb.owlapi.model.OWLOntologyManager} to deal with WSWRL
 * ontology.
 * 
 * @see com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology
 */
public interface WSWRLOntologyManager {
    /**
     * Loads a WSWRL ontology from an ontology document obtained from an input
     * stream. The loaded ontology will be assigned an auto-generated document IRI
     * with "inputstream" as its scheme.
     * 
     * <br>
     * <br>
     * 
     * @see org.semanticweb.owlapi.model.OWLOntologyManager#loadOntologyFromOntologyDocument(InputStream)
     * 
     * @param inputStream The input stream used to obtain a representation of an
     *                    ontology.
     * @return The WSWRL ontology parsed from the input stream.
     * @throws OWLOntologyCreationException If there was a problem in creating and
     *                                      loading the
     *                                      ontology.
     */
    WSWRLOntology loadWSWRLOntologyFromOntologyDocument(InputStream inputStream)
            throws OWLOntologyCreationException, WSWRLBuiltInException;

    /**
     * Write the inferred axioms to a WSWRL ontology. The ontology inferred axioms
     * cached will be cleared.
     * 
     * @param ontology The WSWRL ontology whose axioms must be written.
     * 
     * @throws WSWRLBuiltInException If an error occurs while writing a built-in
     *                               axiom. This exception is forwarded and it
     *                               originally thrown by
     *                               {@link com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology#processOntology()}.
     */
    void writeInferredAxiomsToOntology(WSWRLOntology ontology) throws WSWRLBuiltInException;

    /**
     * Save the ontology in the Turtle format. This method must called
     * {@link #writeInferredAxiomsToOntology(WSWRLOntology)} to write new axiom to
     * the ontology. Then it may return an {@link OWLOntologyStorageException} exception.
     * 
     * @param ontology          The WSWRL ontology to save.
     * @param path              The relative path where to save the ontology.
     * @param minimumConfidence The minimum required confidence to keep inferred
     *                          axioms by WSWRL rules.
     * 
     * @throws OWLOntologyStorageException If an error occurs.
     * @throws WSWRLBuiltInException If an error occurs while writing a built-in
     *                               axiom. This exception is forwarded and it
     *                               originally thrown by
     */
    void saveOntologyToTurtle(WSWRLOntology ontology, String path, float minimumConfidence)
            throws OWLOntologyStorageException, WSWRLBuiltInException;

}
