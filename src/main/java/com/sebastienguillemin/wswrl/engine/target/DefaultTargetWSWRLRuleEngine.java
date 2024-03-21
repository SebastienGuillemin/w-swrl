package com.sebastienguillemin.wswrl.engine.target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

import com.sebastienguillemin.wswrl.core.engine.TargetWSWRLRuleEngine;
import com.sebastienguillemin.wswrl.core.ontology.WSWRLOntology;
import com.sebastienguillemin.wswrl.core.rule.WSWRLRule;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.core.rule.variable.VariableBinding;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIArgument;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLClassAtom;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLDataPropertyAtom;
import com.sebastienguillemin.wswrl.rule.atom.DefaultWSWRLObjectPropertyAtom;
import com.sebastienguillemin.wswrl.rule.variable.DefaultVariableBinding;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLDArgument;
import com.sebastienguillemin.wswrl.rule.variable.DefaultWSWRLIArgument;
public class DefaultTargetWSWRLRuleEngine implements TargetWSWRLRuleEngine {
    private WSWRLOntology wswrlOntology;

    private Hashtable<IRI, WSWRLIArgument> individuals;
    private Hashtable<IRI, List<WSWRLDataPropertyAtom>> dataProperties;
    private Hashtable<IRI, List<WSWRLObjectPropertyAtom>> objectProperties;

    public DefaultTargetWSWRLRuleEngine(WSWRLOntology WSWRLOntology) {
        this.wswrlOntology = WSWRLOntology;

        this.individuals = new Hashtable<>();
        this.dataProperties = new Hashtable<>();
        this.objectProperties = new Hashtable<>();
    }

    @Override
    public void runRuleEngine() {
        try {
            this.reset();
            this.processOntology();

            // Processing WSWRL rules.
            Set<WSWRLRule> wswrlRules = wswrlOntology.getWSWRLRules();
            for (WSWRLRule rule : wswrlRules) {
                if (!rule.isEnabled())
                    continue;

                Set<WSWRLAtom> body = rule.getBody();
                Set<WSWRLAtom> head = rule.getHead();

                Set<WSWRLAtom> allAtoms = new HashSet<>(body);
                allAtoms.addAll(head);

                Set<WSWRLVariable> variables = this.getAllVariables(allAtoms);

                for (VariableBinding binding : this.generateBindings(variables)) {
                    binding.bindVariables(this.individuals);
                    // Calculate rank weights
                    rule.calculateWeights();

                    // Evaluate
                    float confidence = rule.calculateConfidence();
                    System.out.println("Confidence : " + confidence);

                    // TODO: Store result

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reset() {
        this.individuals = new Hashtable<>();
    }

    private void processOntology() throws Exception {
        for (OWLAxiom axiom : this.wswrlOntology.getOWLAxioms()) {
            if (axiom.isOfType(AxiomType.DECLARATION)) {
                OWLEntity entity = ((OWLDeclarationAxiom) axiom).getEntity();

                if (entity.isOWLNamedIndividual())
                    this.addIndividual(new DefaultWSWRLIArgument((OWLNamedIndividual) entity));
            } else if (axiom.isOfType(AxiomType.DATA_PROPERTY_ASSERTION))
                this.handleDataPropertyAxiom((OWLDataPropertyAssertionAxiom) axiom);

            else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_ASSERTION))
                this.handleObjectPropertyAxiom((OWLObjectPropertyAssertionAxiom) axiom);

            else if (axiom.isOfType(AxiomType.CLASS_ASSERTION)) {
                OWLClassAssertionAxiom classAssertionAxiom = (OWLClassAssertionAxiom) axiom;

                OWLNamedIndividual owlIndividual = (OWLNamedIndividual) classAssertionAxiom.getIndividual();
                WSWRLIArgument wswrlIndividual = this.individuals.get(owlIndividual.getIRI());

                if (wswrlIndividual == null) {
                    wswrlIndividual = new DefaultWSWRLIArgument(owlIndividual);
                    this.addIndividual(wswrlIndividual);
                }

                wswrlIndividual.getWSWRLIndividual().addClass(new DefaultWSWRLClassAtom(classAssertionAxiom.getClassExpression(), (WSWRLIArgument) wswrlIndividual));
            }
        }
    }

    private void handleDataPropertyAxiom(OWLDataPropertyAssertionAxiom axiom) throws Exception {
        WSWRLIArgument subject = new DefaultWSWRLIArgument((OWLNamedIndividual) axiom.getSubject());
        WSWRLDArgument object = new DefaultWSWRLDArgument(axiom.getObject());

        WSWRLDataPropertyAtom dataProperty = new DefaultWSWRLDataPropertyAtom(axiom.getProperty().asOWLDataProperty(), subject, object);
        dataProperty.addPropertyToIndividual(subject.getWSWRLIndividual());

        this.addDataProperty(dataProperty);
    }

    private void handleObjectPropertyAxiom(OWLObjectPropertyAssertionAxiom axiom) throws Exception {
        WSWRLIArgument subject = new DefaultWSWRLIArgument((OWLNamedIndividual) axiom.getSubject());
        WSWRLIArgument object = new DefaultWSWRLIArgument((OWLNamedIndividual) axiom.getObject());

        WSWRLObjectPropertyAtom dataProperty = new DefaultWSWRLObjectPropertyAtom(axiom.getProperty(), subject, object);
        dataProperty.addPropertyToIndividual(subject.getWSWRLIndividual());

        this.addObjectProperty(dataProperty);
    }

    private void addIndividual(WSWRLIArgument individual) {
        IRI iri = individual.getWSWRLIndividual().getIRI();
        if (!this.individuals.containsKey(iri)) {
            this.individuals.put(iri, individual);
        }
    }

    private void addDataProperty(WSWRLDataPropertyAtom dataProperty) {
        IRI iri = dataProperty.getIRI();

        List<WSWRLDataPropertyAtom> atoms;
        if (!this.dataProperties.containsKey(iri)) {
            atoms = new ArrayList<>();
            this.dataProperties.put(iri, atoms);
        }
        else
            atoms = this.dataProperties.get(iri);

        atoms.add(dataProperty);
    }

    private void addObjectProperty(WSWRLObjectPropertyAtom objectProperty) {
        IRI iri = objectProperty.getIRI();
        
        List<WSWRLObjectPropertyAtom> atoms;
        if (!this.objectProperties.containsKey(iri)) {
            atoms = new ArrayList<>();
            this.objectProperties.put(iri, atoms);
        }
        else
            atoms = this.objectProperties.get(iri);

        atoms.add(objectProperty);
    }

    private Set<WSWRLVariable> getAllVariables(Set<WSWRLAtom> atoms) {
        Set<WSWRLVariable> variables = new HashSet<>();

        for (WSWRLAtom atom : atoms) {
            for (WSWRLVariable variable : atom.getVariables()) {
                variables.add(variable);
            }
        }

        return variables;
    }

    private List<VariableBinding> generateBindings(Set<WSWRLVariable> variables) {
        List<VariableBinding> bindings = this.generateIndividualBindings(variables);

        return bindings;
    }


    private List<VariableBinding> generateIndividualBindings(Set<WSWRLVariable> variables) {
        List<VariableBinding> bindings = new ArrayList<>();

        List<WSWRLVariable> individualVariables = variables.stream().filter(v -> v.getDomain() == WSWRLVariableDomain.INDIVIDUALS).collect(Collectors.toList());
        List<WSWRLIArgument> boundableIndividual = new ArrayList<>(this.individuals.values());
        int individualBindingPossiblities = (int) Math.pow(boundableIndividual.size(), individualVariables.size());

        System.out.println("Boundable individual count : " + boundableIndividual.size());
        System.out.println(individualBindingPossiblities + " individual binding possibilities");

        int individualPointer = 0;
        int change, count;
        for (int v = 0; v < individualVariables.size(); v++) {
            change = (int) Math.pow(Math.max(boundableIndividual.size(), individualVariables.size()), individualVariables.size() - (v + 1));
            
            count = 0;
            for (int i = 0; i < individualBindingPossiblities; i++) {
                VariableBinding binding;
                if (v == 0) {
                    binding = new DefaultVariableBinding();
                    bindings.add(binding);
                } else
                    binding = bindings.get(i);

                WSWRLVariable variableName = individualVariables.get(v);
                binding.bindIndividual(variableName, boundableIndividual.get(individualPointer).getWSWRLIndividual().getIRI());

                count = (++count) % change;
                if (count == 0)
                    individualPointer = (++individualPointer) % boundableIndividual.size();
            }
        }

        return bindings;
    }

    // private void bind(VariableBinding binding) {
    //     throw new UnsupportedOperationException("Unimplemented method 'bind'");
    // }
}
