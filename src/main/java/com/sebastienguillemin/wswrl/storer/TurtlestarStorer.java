package com.sebastienguillemin.wswrl.storer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.util.Set;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ModelFactory;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleNamespace;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Statements;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;

import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLAxiom;

public class TurtlestarStorer {
    private ModelFactory modelFactory;
    private ValueFactory factory;

    public TurtlestarStorer() {
        this.modelFactory = new DynamicModelFactory();
        this.factory = SimpleValueFactory.getInstance();
    }

    public void storeOntology(WSWRLOntology wswrlOntology)
            throws RDFHandlerException, UnsupportedRDFormatException, FileNotFoundException, URISyntaxException {
        org.semanticweb.owlapi.model.IRI baseIRI = wswrlOntology.getBaseIRI();
        String baseIRIString = (baseIRI == null) ? "" : baseIRI.toString();

        Model model = this.modelFactory.createEmptyModel();
        model.setNamespace(new SimpleNamespace("", baseIRIString));
        model.setNamespace(new SimpleNamespace("owl", "http://www.w3.org/2002/07/owl#"));
        model.setNamespace(new SimpleNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
        model.setNamespace(new SimpleNamespace("xml", "http://www.w3.org/XML/1998/namespace"));
        model.setNamespace(new SimpleNamespace("xsd", "http://www.w3.org/2001/XMLSchema#"));
        model.setNamespace(new SimpleNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));

        this.addOWLAxioms(wswrlOntology.getOWLAxioms(), model);
        this.addWSWRLAxioms(wswrlOntology.getWSWRLInferredAxioms(), model);

        Rio.write(model, new FileOutputStream(new File("test.ttls")), baseIRIString, RDFFormat.TURTLESTAR);
    }

    private void addOWLAxioms(Set<OWLAxiom> owlAxioms, Model model) {
        Statement statement;
        for (OWLAxiom axiom : owlAxioms) {
            // System.out.println(axiom);
            // TODO : gérer les autrs type d'assertions.
            statement = null;
            if (axiom instanceof OWLClassAssertionAxiom) {
                statement = this.getClassAxiom((OWLClassAssertionAxiom) axiom);
            } else if (axiom instanceof OWLObjectPropertyAssertionAxiom) {
                statement = this.getObjectPropertyAxiom((OWLObjectPropertyAssertionAxiom) axiom);
            } else if (axiom instanceof OWLDataPropertyAssertionAxiom) {
                statement = this.getDataPropertyAxiom((OWLDataPropertyAssertionAxiom) axiom);
            } else if (axiom instanceof OWLSymmetricObjectPropertyAxiom) {
                statement = this.getSymmetricObjectPropertyAxiom((OWLSymmetricObjectPropertyAxiom) axiom);
            } else if (axiom instanceof OWLDataPropertyRangeAxiom) {
                statement = this.getDataPropertyRangeAxiom((OWLDataPropertyRangeAxiom) axiom);
            } else if(axiom instanceof OWLDeclarationAxiom) {
                statement = this.getDeclarationAxiom((OWLDeclarationAxiom) axiom);

                if (statement == null) {
                    System.out.println("Writing axiom : '" + axiom + "' is not supported yet (ignoring).");
                    continue;
                }
            } else {
                System.out.println("Writing axiom : '" + axiom + "' is not supported yet (ignoring).");
                continue;
            }
            model.add(statement);
        }
    }

    private Statement getClassAxiom(OWLClassAssertionAxiom axiom) {
        Resource subject = Values.iri(axiom.getIndividual().asOWLNamedIndividual().getIRI().toString());
        Resource classIRI = Values.iri(axiom.getClassExpression().asOWLClass().getIRI().toString());
        return Statements.statement(this.factory, subject, RDF.TYPE, classIRI, null);
    }

    private Statement getObjectPropertyAxiom(OWLObjectPropertyAssertionAxiom axiom) {
        Resource subject = Values.iri(axiom.getSubject().asOWLNamedIndividual().getIRI().toString());
        IRI classIRI = Values.iri(axiom.getProperty().asOWLObjectProperty().getIRI().toString());
        Resource object = Values.iri(axiom.getObject().asOWLNamedIndividual().getIRI().toString());
        return Statements.statement(this.factory, subject, classIRI, object, null);
    }

    private Statement getDataPropertyAxiom(OWLDataPropertyAssertionAxiom axiom) {
        Resource subject = Values.iri(axiom.getSubject().asOWLNamedIndividual().getIRI().toString());
        IRI classIRI = Values.iri(axiom.getProperty().asOWLDataProperty().getIRI().toString());
        Literal object = Values.literal(axiom.getObject().getLiteral());
        return Statements.statement(this.factory, subject, classIRI, object, null);
    }

    private Statement getSymmetricObjectPropertyAxiom(OWLSymmetricObjectPropertyAxiom axiom) {
        Resource subject = Values.iri(axiom.getProperty().asOWLObjectProperty().getIRI().toString());
        return Statements.statement(subject, RDF.TYPE, OWL.SYMMETRICPROPERTY, null);
    }

    private Statement getDataPropertyRangeAxiom(OWLDataPropertyRangeAxiom axiom) {
        Resource subject  = Values.iri(axiom.getProperty().asOWLDataProperty().getIRI().toString());
        Resource object = Values.iri(axiom.getRange().asOWLDatatype().getIRI().toString());
        return Statements.statement(subject, RDFS.RANGE, object, null);
    }

    private Statement getDeclarationAxiom(OWLDeclarationAxiom axiom) {
        OWLEntity entity = axiom.getEntity();

        Resource subject = null;
        Resource object = null;
        if (entity.isOWLClass()) {
            subject = Values.iri(entity.asOWLClass().getIRI().toString());
            object = OWL.CLASS;
        } else if (entity.isOWLObjectProperty()) {
            subject = Values.iri(entity.asOWLObjectProperty().getIRI().toString());
            object = OWL.OBJECTPROPERTY;
        } else if (entity.isOWLDataProperty()) {
            subject = Values.iri(entity.asOWLDataProperty().getIRI().toString());
            object = OWL.DATATYPEPROPERTY;
        } else if (entity.isOWLNamedIndividual()) {
            subject = Values.iri(entity.asOWLNamedIndividual().getIRI().toString());
            object = OWL.NAMEDINDIVIDUAL;
        } else if (entity.isOWLDataProperty()) {
            subject = Values.iri(entity.asOWLDataProperty().getIRI().toString());
            object = OWL.DATATYPEPROPERTY;
        } else 
            return null;

        return Statements.statement(subject, RDF.TYPE, object, null);
    }

    private void addWSWRLAxioms(Set<WSWRLAxiom> wswrlInferredAxioms, Model model) {
        Statement statement;
        for (WSWRLAxiom axiom : wswrlInferredAxioms) {
            OWLAxiom owlAxiom = axiom.getAxiom();
            statement = null;
            if (owlAxiom instanceof OWLClassAssertionAxiom) {
                statement = this.getClassAxiom((OWLClassAssertionAxiom) owlAxiom);
            } else if (owlAxiom instanceof OWLObjectPropertyAssertionAxiom) {
                statement = this.getObjectPropertyAxiom((OWLObjectPropertyAssertionAxiom) owlAxiom);
            } else if (owlAxiom instanceof OWLDataPropertyAssertionAxiom) {
                statement = this.getDataPropertyAxiom((OWLDataPropertyAssertionAxiom) owlAxiom);
            }

            float confidence = axiom.getConfidence();
            model.add(Values.triple(statement),
                    Values.iri(
                            "http://www.semanticweb.org/guillemin/ontologies/2024/2/untitled-ontology-103/confidence"),
                    Values.literal(confidence), new Resource[0]);
        }
    }
}
