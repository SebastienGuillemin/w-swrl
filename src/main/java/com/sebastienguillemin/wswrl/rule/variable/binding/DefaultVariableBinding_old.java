// package com.sebastienguillemin.wswrl.rule.variable.binding;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.Hashtable;
// import java.util.List;
// import java.util.Map.Entry;
// import java.util.Set;
// import java.util.stream.Collectors;

// import org.semanticweb.owlapi.model.IRI;
// import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
// import org.semanticweb.owlapi.model.OWLLiteral;
// import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;

// import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLAtom;
// import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLClassAtom;
// import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLDataPropertyAtom;
// import com.sebastienguillemin.wswrl.core.rule.atom.WSWRLObjectPropertyAtom;
// import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
// import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIVariable;
// import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLIndividual;
// import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariable;
// import com.sebastienguillemin.wswrl.core.rule.variable.binding.VariableBinding;

// /**
//  * {@inheritDoc}
//  */
// public class DefaultVariableBinding implements VariableBinding {
//     // TODO : à supprimer.
//     public static long nextIndividualsBinding = 0, bindIndividuals = 0, processDataProperties = 0, nextDataBinding = 0,
//             bindDataVariables = 0;

//     private Set<WSWRLAtom> atoms;
//     private Hashtable<IRI, WSWRLIndividual> individuals;

//     private HashMap<WSWRLIVariable, List<WSWRLIndividual>> individualBindings; // Individual variable -> individuals.
//     private HashMap<IRI, Integer> individualPointers; // (Individual) Variables IRI -> Integer.

//     private HashMap<WSWRLIVariable, List<WSWRLIndividual>> objectIndividualBindings; // Individual variable -> individuals.
//     private HashMap<IRI, Integer> objectIndividualPointers; // (Individual) Variables IRI -> Integer.

//     private HashMap<WSWRLDVariable, List<OWLLiteral>> dataBindings; // Data variable -> litteral.
//     private HashMap<IRI, Integer> dataPointers; // (Data) Variables IRI -> Integer

//     public boolean dataLock;
//     public boolean objectIndividualsLock;
//     private int bindingPossibilities;
//     private int processedBindings;
//     private Set<WSWRLDataPropertyAtom> dataPropertyAtoms;
//     private Set<WSWRLObjectPropertyAtom> objectPropertyAtoms;
//     private Set<WSWRLClassAtom> classAtoms;

//     public DefaultVariableBinding(Set<WSWRLAtom> atoms, Hashtable<IRI, WSWRLIndividual> individuals,
//             Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
//         this.atoms = atoms;
//         this.individuals = individuals;

//         this.individualBindings = new HashMap<>();
//         this.individualPointers = new HashMap<>();

//         this.objectIndividualBindings = new HashMap<>();
//         this.objectIndividualPointers = new HashMap<>();

//         this.dataBindings = new HashMap<>();
//         this.dataPointers = new HashMap<>();

//         this.dataLock = false;
//         this.objectIndividualsLock = false;

//         this.bindingPossibilities = 1;

//         // Sorting predicates
//         this.classAtoms = this.atoms.stream().filter(atom -> (atom instanceof WSWRLClassAtom))
//                 .map(atom -> (WSWRLClassAtom) atom).collect(Collectors.toSet());
//         this.dataPropertyAtoms = this.atoms.stream()
//                 .filter(a -> a instanceof WSWRLDataPropertyAtom).map(a -> (WSWRLDataPropertyAtom) a)
//                 .collect(Collectors.toSet());

//         this.objectPropertyAtoms = this.atoms.stream()
//                 .filter(a -> a instanceof WSWRLObjectPropertyAtom).map(a -> (WSWRLObjectPropertyAtom) a)
//                 .collect(Collectors.toSet());

//         this.init(classToIndividuals);
//     }

//     @Override
//     public boolean hasNext() {
//         return this.processedBindings < this.bindingPossibilities;
//     }

//     @Override
//     public void nextBinding() {
//         long start = System.currentTimeMillis();
//         long check1, check2, check3, check4, check5;
//         if (this.dataLock) {
//             this.nextDataBinding();
//             check1 = System.currentTimeMillis();

//             this.bindDataVariables();
//             check2 = System.currentTimeMillis();

//             nextDataBinding += (check1 - start);
//             bindDataVariables += (check2 - check1);
//         } else  if(this.objectIndividualsLock) {
//             this.nextObjectIndividualsBinding();

//             this.bindObjectIndividuals();
//         } else {
//             this.nextIndividualsBinding();
//             check1 = System.currentTimeMillis();

//             this.bindIndividuals();
//             check2 = System.currentTimeMillis();

//             this.processObjectProperties();
//             this.nextObjectIndividualsBinding();
//             this.bindObjectIndividuals();

//             this.processDataProperties();
//             check3 = System.currentTimeMillis();

//             this.nextDataBinding();
//             check4 = System.currentTimeMillis();

//             this.bindDataVariables();
//             check5 = System.currentTimeMillis();

//             nextIndividualsBinding += (check1 - start);
//             bindIndividuals += (check2 - check1);
//             processDataProperties += (check3 - check2);
//             nextDataBinding += (check4 - check3);
//             bindDataVariables += (check5 - check4);
//         }

//         WSWRLIVariable iVariable;
//         WSWRLDVariable dVariable;
//         String bindings = "";
//         for (WSWRLVariable variable : this.atoms.stream().map(a -> a.getVariables()).flatMap(Set::stream).collect(Collectors.toSet())) {
//                 if (variable instanceof WSWRLIVariable) {
//                     iVariable = (WSWRLIVariable) variable;
//                     bindings += iVariable.getIRI().getFragment() + " -> "
//                             + ((iVariable.getValue() == null) ? "null" : iVariable.getValue().getIRI().getFragment()) + ", ";
//                 } else if (variable instanceof WSWRLDVariable) {
//                     dVariable = (WSWRLDVariable) variable;
//                     bindings += dVariable.getIRI().getFragment() + " -> " + dVariable.getValue().getLiteral() + ", ";
//                 }
//         }

//         System.out.println(bindings);

//         // System.exit(1);
//     }

//     private void processObjectProperties() {
//         this.objectIndividualBindings.clear();
//         this.objectIndividualPointers.clear();

//         // For each object property atoms
//         WSWRLIndividual subject;
//         List<WSWRLIndividual> objects;
//         for (WSWRLObjectPropertyAtom atom : this.objectPropertyAtoms) {
//             subject = atom.getSubject().getValue();
//             objects = new ArrayList<>();

//             for (OWLObjectPropertyAssertionAxiom assertion : subject.getObjectProperties(atom.getIRI()))
//                 objects.add(this.individuals.get(assertion.getObject().asOWLNamedIndividual().getIRI()));

//             this.objectIndividualBindings.put(atom.getObject(), objects);
//             this.objectIndividualPointers.put(atom.getObject().getIRI(), 0);
//         }

//         System.out.println("objectIndividualBindings -> " + this.objectIndividualBindings);

//         // Lock
//         this.objectIndividualsLock = true;
//     }

//     private void bindObjectIndividuals() {
//         WSWRLIVariable iVariable;
//         List<WSWRLIndividual> iValues;
//         for (Entry<WSWRLIVariable, List<WSWRLIndividual>> entry : this.objectIndividualBindings.entrySet()) {
//             iVariable = entry.getKey();
//             iValues = entry.getValue();
//             if (iValues.size() == 0)
//                 continue;

//             iVariable.setValue(iValues.get(this.objectIndividualPointers.get(iVariable.getIRI())));
//         }
//     }

//     private void nextObjectIndividualsBinding() {
//         int pointer = 0;
//         IRI variableIRI;
//         int valuesCount;
//         for (WSWRLIVariable variable : this.objectIndividualBindings.keySet()) {
//             valuesCount = this.objectIndividualBindings.get(variable).size();
//             if (valuesCount == 0)
//                 continue;

//             variableIRI = variable.getIRI();
//             pointer = this.objectIndividualPointers.get(variableIRI);
//             pointer = ++pointer % this.objectIndividualBindings.get(variable).size();
//             this.objectIndividualPointers.put(variableIRI, pointer);

//             if (pointer != 0)
//                 return;
//         }
//         this.objectIndividualsLock = false;
//     }

//     private void init(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
//         // Filtering individuals for class atoms.
//         this.processClassAtomIndividuals(classToIndividuals);

//         // Filtering individuals for object property atoms.
//         this.processObjectPropertiesIndividuals(classToIndividuals);

//         // Filtering individuals for data property atoms.
//         this.processDataropertiesIndividuals(classToIndividuals);

//         // Init individuals pointers.
//         this.initIndividiualsPointers();
//     }

//     private void processClassAtomIndividuals(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
//         IRI classAtomIRI;
//         for (WSWRLClassAtom classAtom : classAtoms) {
//             classAtomIRI = classAtom.getIRI();
//             for (WSWRLIVariable variable : classAtom.getVariables().stream().map(iv -> (WSWRLIVariable) iv)
//                     .collect(Collectors.toSet())) {
//                 if (classToIndividuals.containsKey(classAtomIRI))
//                     this.individualBindings.put(variable, new ArrayList<>(classToIndividuals.get(classAtomIRI)));
//                 else
//                     this.individualBindings.put(variable, new ArrayList<>());
//             }
//         }
//     }

//     private void processObjectPropertiesIndividuals(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
//         Set<WSWRLObjectPropertyAtom> objectPropertyAtomsToProcess = this.objectPropertyAtoms.stream()
//                 .filter(op -> !this.individualBindings.containsKey(op.getSubject()))
//                 .collect(Collectors.toSet());

//         List<WSWRLIndividual> individuals = new ArrayList<>(this.individuals.values());
//         for (WSWRLObjectPropertyAtom atom : objectPropertyAtomsToProcess) {
//             this.individualBindings.put(atom.getSubject(), individuals);
//         }
//     }

//     private void processDataropertiesIndividuals(Hashtable<IRI, Set<WSWRLIndividual>> classToIndividuals) {
//         Set<WSWRLDataPropertyAtom> dataPropertyAtomsToProcess = this.dataPropertyAtoms.stream()
//                 .filter(dp -> !this.individualBindings.containsKey(dp.getSubject()))
//                 .collect(Collectors.toSet());

//         List<WSWRLIndividual> individuals = new ArrayList<>(this.individuals.values());
//         for (WSWRLDataPropertyAtom atom : dataPropertyAtomsToProcess) {
//             this.individualBindings.put(atom.getSubject(), individuals);
//         }
//     }

//     private void initIndividiualsPointers() {
//         WSWRLIVariable variable;
//         List<WSWRLIndividual> individuals;
//         for (Entry<WSWRLIVariable, List<WSWRLIndividual>> entry : this.individualBindings.entrySet()) {
//             variable = entry.getKey();
//             individuals = entry.getValue();
//             this.individualPointers.put(variable.getIRI(), 0);

//             this.bindingPossibilities *= individuals.size();
//         }
//     }

//     private void bindIndividuals() {
//         WSWRLIVariable iVariable;
//         List<WSWRLIndividual> iValues;
//         for (Entry<WSWRLIVariable, List<WSWRLIndividual>> entry : this.individualBindings.entrySet()) {
//             iVariable = entry.getKey();
//             iValues = entry.getValue();
//             iVariable.setValue(iValues.get(this.individualPointers.get(iVariable.getIRI())));
//         }
//     }

//     private void bindDataVariables() {
//         WSWRLDVariable dVariable;
//         List<OWLLiteral> oValues;
//         for (Entry<WSWRLDVariable, List<OWLLiteral>> entry : this.dataBindings.entrySet()) {
//             dVariable = entry.getKey();
//             oValues = entry.getValue();

//             if (oValues == null || oValues.size() == 0)
//                 continue;
//             dVariable.setValue(oValues.get(this.dataPointers.get(dVariable.getIRI())));
//         }
//     }

//     private void nextIndividualsBinding() {
//         // If some data are not processed -> lock
//         if (this.dataLock)
//             return;

//         // Increase processed binding counter
//         this.processedBindings++;

//         // Next individual.
//         int pointer = 0;
//         IRI variableIRI;
//         for (WSWRLVariable variable : this.individualBindings.keySet()) {
//             variableIRI = variable.getIRI();
//             pointer = this.individualPointers.get(variableIRI);
//             pointer = ++pointer % this.individualBindings.get(variable).size();
//             this.individualPointers.put(variableIRI, pointer);

//             if (pointer != 0)
//                 break;
//         }
//     }

//     private void nextDataBinding() {
//         int pointer = 0;
//         IRI variableIRI;
//         int valuesCount;
//         for (WSWRLDVariable variable : this.dataBindings.keySet()) {
//             valuesCount = this.dataBindings.get(variable).size();
//             if (valuesCount == 0)
//                 continue;

//             variableIRI = variable.getIRI();
//             pointer = this.dataPointers.get(variableIRI);
//             pointer = ++pointer % this.dataBindings.get(variable).size();
//             this.dataPointers.put(variableIRI, pointer);

//             if (pointer != 0)
//                 return;
//         }
//         this.dataLock = false;
//     }

//     private void processDataProperties() {
//         this.dataBindings.clear();
//         this.dataPointers.clear();
//         // Get data properties

//         // For each data property atoms
//         List<OWLLiteral> literals;
//         WSWRLIndividual subject;
//         for (WSWRLDataPropertyAtom atom : this.dataPropertyAtoms) {
//             literals = new ArrayList<>();
//             subject = atom.getSubject().getValue();

//             // Get the subject data property assertions and add the object to the litterals
//             // list for the WSWRLDVariable.
//             for (OWLDataPropertyAssertionAxiom assertion : subject.getDataProperties(atom.getIRI()))
//                 literals.add(assertion.getObject());

//             this.dataBindings.put(atom.getObject(), literals);
//             this.dataPointers.put(atom.getObject().getIRI(), 0);
//         }

//         // Lock
//         this.dataLock = true;
//     }
// }
