package com.sebastienguillemin.wswrl.rule.variable;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

import org.checkerframework.dataflow.qual.Deterministic;
import org.checkerframework.dataflow.qual.SideEffectFree;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnonymousIndividual;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLObjectVisitorEx;
import org.semanticweb.owlapi.model.SWRLObjectVisitor;
import org.semanticweb.owlapi.model.SWRLObjectVisitorEx;
import org.swrlapi.builtins.arguments.SQWRLCollectionVariableBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLAnnotationPropertyBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgumentType;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgumentVisitor;
import org.swrlapi.builtins.arguments.SWRLBuiltInArgumentVisitorEx;
import org.swrlapi.builtins.arguments.SWRLClassBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLClassExpressionBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLDataPropertyBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLDataPropertyExpressionBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLDatatypeBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLLiteralBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLMultiValueVariableBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLNamedBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLNamedIndividualBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLObjectPropertyBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLObjectPropertyExpressionBuiltInArgument;
import org.swrlapi.builtins.arguments.SWRLVariableBuiltInArgument;
import org.swrlapi.exceptions.SWRLAPIException;
import org.swrlapi.exceptions.SWRLBuiltInException;
import org.swrlapi.literal.OWLLiteralComparator;

import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLDVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLLiteralBuiltInVariable;
import com.sebastienguillemin.wswrl.core.rule.variable.WSWRLVariableDomain;
import com.sebastienguillemin.wswrl.exception.WSWRLBuiltInException;

/**
 * {@inheritDoc}
 * The code is taken from the SWRL API.
 */
public class DefaultWSWRLLiteralBuiltinVariable implements WSWRLLiteralBuiltInVariable {
    private static final Comparator<OWLLiteral> owlLiteralComparator = OWLLiteralComparator.COMPARATOR;
    private WSWRLDVariable wswrldVariable;
    private String boundVariableName = null;

    /**
     * Constructor.
     * 
     * @param wswrldVariable the built-in variable.
     */
    public DefaultWSWRLLiteralBuiltinVariable(WSWRLDVariable wswrldVariable) {
        this.wswrldVariable = wswrldVariable;
    }

    @Override
    public IRI getIRI() {
        return this.wswrldVariable.getIRI();
    }

    @Override
    public boolean isVariable() {
        return getSWRLBuiltInArgumentType() == SWRLBuiltInArgumentType.VARIABLE ||
                getSWRLBuiltInArgumentType() == SWRLBuiltInArgumentType.MULTI_VALUE_VARIABLE ||
                getSWRLBuiltInArgumentType() == SWRLBuiltInArgumentType.COLLECTION_VARIABLE;
    }

    @Override
    public SWRLVariableBuiltInArgument asVariable() {
        throw new SWRLAPIException("Not a SWRLVariableBuiltInArgument");
    }

    @Override
    public SWRLMultiValueVariableBuiltInArgument asMultiValueVariable() {
        throw new SWRLAPIException("Not a SWRLMultiVariableBuiltInArgument");
    }

    @Override
    public boolean wasBoundVariable() {
        return this.boundVariableName != null;
    }

    @Override
    public Optional<String> getBoundVariableName() {
        if (this.boundVariableName != null)
            return Optional.of(this.boundVariableName);
        else
            return Optional.<String>empty();
    }

    @Override
    public void setBoundVariableName(String boundVariableName) {
        this.boundVariableName = boundVariableName;
    }

    @Override
    public SQWRLCollectionVariableBuiltInArgument asCollectionVariable() throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SQWRLCollectionVariableBuiltInArgument.class.getName());
    }

    @Override
    public SWRLLiteralBuiltInArgument asSWRLLiteralBuiltInArgument() throws WSWRLBuiltInException {
        throw new WSWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLLiteralBuiltInArgument.class.getName());
    }

    @Override
    public SWRLNamedBuiltInArgument asSWRLNamedBuiltInArgument() throws SWRLBuiltInException {
        throw new SWRLBuiltInException(getClass().getName() + " is not an " + SWRLNamedBuiltInArgument.class.getName());
    }

    @Override
    public SWRLNamedIndividualBuiltInArgument asSWRLNamedIndividualBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLNamedIndividualBuiltInArgument.class.getName());
    }

    @Override
    public SWRLClassBuiltInArgument asSWRLClassBuiltInArgument() throws SWRLBuiltInException {
        throw new SWRLBuiltInException(getClass().getName() + " is not an " + SWRLClassBuiltInArgument.class.getName());
    }

    @Override
    public SWRLClassExpressionBuiltInArgument asSWRLClassExpressionBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLClassExpressionBuiltInArgument.class.getName());
    }

    @Override
    public SWRLObjectPropertyBuiltInArgument asSWRLObjectPropertyBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLObjectPropertyBuiltInArgument.class.getName());
    }

    @Override
    public SWRLObjectPropertyExpressionBuiltInArgument asSWRLObjectPropertyExpressionBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLObjectPropertyExpressionBuiltInArgument.class.getName());
    }

    @Override
    public SWRLDataPropertyBuiltInArgument asSWRLDataPropertyBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLDataPropertyBuiltInArgument.class.getName());
    }

    @Override
    public SWRLDataPropertyExpressionBuiltInArgument asSWRLDataPropertyExpressionBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLDataPropertyExpressionBuiltInArgument.class.getName());
    }

    @Override
    public SWRLDatatypeBuiltInArgument asSWRLDatatypeBuiltInArgument() throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLDatatypeBuiltInArgument.class.getName());
    }

    @Override
    public SWRLAnnotationPropertyBuiltInArgument asSWRLAnnotationPropertyBuiltInArgument()
            throws SWRLBuiltInException {
        throw new SWRLBuiltInException(
                getClass().getName() + " is not an " + SWRLAnnotationPropertyBuiltInArgument.class.getName());
    }

    @Override
    public boolean containsEntityInSignature(OWLEntity owlEntity) {
        return false; // TODO implement containsEntityInSignature
    }

    @Override
    public Set<OWLEntity> getSignature() {
        return Collections.<OWLEntity>emptySet(); // TODO implement getSignature
    }

    @Override
    public Set<OWLAnonymousIndividual> getAnonymousIndividuals() {
        return Collections.<OWLAnonymousIndividual>emptySet(); // TODO implement getAnonymousIndividuals
    }

    @Override
    public Set<OWLClass> getClassesInSignature() {
        return Collections.<OWLClass>emptySet(); // TODO implement getClassesInSignature
    }

    @Override
    public Set<OWLDataProperty> getDataPropertiesInSignature() {
        return Collections.<OWLDataProperty>emptySet(); // TODO implement getDataPropertiesInSignature
    }

    @Override
    public Set<OWLObjectProperty> getObjectPropertiesInSignature() {
        return Collections.<OWLObjectProperty>emptySet(); // TODO implement getObjectPropertiesInSignature
    }

    @Override
    public Set<OWLNamedIndividual> getIndividualsInSignature() {
        return Collections.<OWLNamedIndividual>emptySet(); // TODO implement getIndividualInSignature
    }

    @Override
    public Set<OWLDatatype> getDatatypesInSignature() {
        return Collections.<OWLDatatype>emptySet(); // TODO implement getDatatypesInSignature
    }

    @Override
    public Set<OWLClassExpression> getNestedClassExpressions() {
        return Collections.<OWLClassExpression>emptySet(); // TODO implement getNestedClassExpressions
    }

    @Override
    public boolean isTopEntity() {
        return false;
    }

    @Override
    public boolean isBottomEntity() {
        return false;
    }

    @Override
    public SWRLBuiltInArgumentType<?> getSWRLBuiltInArgumentType() {
        return SWRLBuiltInArgumentType.LITERAL;
    }

    @Override
    public OWLLiteral getLiteral() {
        return this.wswrldVariable.getValue();
    }

    @Override
    public OWLLiteral getValue() {
        return this.getLiteral();
    }

    @Override
    public void setValue(OWLLiteral value) {
        this.wswrldVariable.setValue(value);
    }

    @SideEffectFree
    @Deterministic
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DefaultWSWRLLiteralBuiltinVariable that = (DefaultWSWRLLiteralBuiltinVariable) o;

        return this.wswrldVariable.getValue().equals(that.getValue());
    }

    @SideEffectFree
    @Deterministic
    @Override
    public int hashCode() {
        return this.wswrldVariable.hashCode();
    }

    @SideEffectFree
    @Deterministic
    @Override
    public int compareTo(OWLObject o) {
        if (o == null)
            throw new NullPointerException();

        if (!(o instanceof SWRLLiteralBuiltInArgument))
            return -1;

        SWRLLiteralBuiltInArgument other = (SWRLLiteralBuiltInArgument) o;

        return owlLiteralComparator.compare(this.getLiteral(), other.getLiteral());
    }

    @Override
    public void accept(SWRLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(SWRLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(OWLObjectVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public <O> O accept(OWLObjectVisitorEx<O> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <T> T accept(SWRLBuiltInArgumentVisitorEx<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public void accept(SWRLBuiltInArgumentVisitor visitor) {
        visitor.visit(this);
    }

    @SideEffectFree
    @Override
    public String toString() {
        return this.wswrldVariable.getValue().getLiteral();
    }

    @Override
    public Set<OWLAnnotationProperty> getAnnotationPropertiesInSignature() {
        return Collections.<OWLAnnotationProperty>emptySet(); // TODO Implement
                                                              // getAnnotationPropertiesInSignature
    }

    @Override
    public WSWRLVariableDomain getDomain() {
        return this.wswrldVariable.getDomain();
    }
}
