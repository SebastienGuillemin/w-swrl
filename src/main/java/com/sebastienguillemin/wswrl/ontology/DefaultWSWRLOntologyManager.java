package com.sebastienguillemin.wswrl.ontology;

import java.io.File;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.formats.TurtleDocumentFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntologyManager;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;
import com.sebastienguillemin.wswrl.factory.WSWRLFactory;

public class DefaultWSWRLOntologyManager implements WSWRLOntologyManager {
    private OWLOntologyManager owlOntologyManager;

    public DefaultWSWRLOntologyManager(OWLOntologyManager owlOntologyManager) {
        this.owlOntologyManager = owlOntologyManager;
    }

    @Override
    public WSWRLOntology loadWSWRLOntologyFromOntologyDocument(InputStream inputStream)
            throws OWLOntologyCreationException, WSWRLBuiltInException {
        OWLOntology owlOntology = this.owlOntologyManager.loadOntologyFromOntologyDocument(inputStream);
        
        WSWRLOntology wswrlOntology = new DefaultWSWRLOntology(owlOntology, WSWRLFactory.createIRIResolver());
        wswrlOntology.processOntology();

        return wswrlOntology;
    }

    @Override
    public void writeInferredAxiomsToOntology(WSWRLOntology wswrlOntology) throws WSWRLBuiltInException {
        Set<OWLAxiom> axiomsToAdd = new HashSet<>();
        
        for(OWLAxiom axiom : wswrlOntology.getWSWRLInferredAxiomsAsOWLAxiom()) {
            if(!wswrlOntology.getOWLAxioms().contains(axiom))
                axiomsToAdd.add(axiom);
        }
        this.owlOntologyManager.addAxioms(wswrlOntology.getOWLOntology(), axiomsToAdd);
        wswrlOntology.processOntology();

        wswrlOntology.clearInferredAxiomsCache();
    }

    @Override
    public void saveOntologyToTurtle(WSWRLOntology wswrlOntology, String path, float threshold) throws OWLOntologyStorageException, WSWRLBuiltInException {
        wswrlOntology.filterInferredAxioms(threshold);
        this.writeInferredAxiomsToOntology(wswrlOntology);

        OWLOntology ontology = wswrlOntology.getOWLOntology();
        File saveIn = new File(path);
        IRI fileIRI = IRI.create(saveIn.toURI());
        TurtleDocumentFormat turtleFormat = new TurtleDocumentFormat();
        turtleFormat.setDefaultPrefix(ontology.getOntologyID().getOntologyIRI().get().toString() + "/");
        this.owlOntologyManager.saveOntology(ontology, turtleFormat, fileIRI);
    }

}
