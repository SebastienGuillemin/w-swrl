package com.sebastienguillemin.wswrl.core.rule.variable;

import org.semanticweb.owlapi.model.OWLLiteral;

/**
 * A WSWRL data variable. The variable value is an
 * {@link org.semanticweb.owlapi.model.OWLLiteral}.
 */
public interface WSWRLDVariable extends WSWRLTypedVariable<OWLLiteral> {}
