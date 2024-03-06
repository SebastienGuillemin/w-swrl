# W-SWRL
This repository contains the implementation of the W-SWRL reasoner. W-SWRL is an extension of SWRL to deal with missing data and hierarchical body literals. W-SWRL are description logic horn clauses and support classical SWWRL rules elements (i.e., unary predicates, binary predicates, built-in and variables). Body literals of W-SWRL rules are assigned to ranks ranging from rank $r_0$ to $r_m$ ($m >= 0$), and the head literal is associated with a confidence indicator ranging in the interval [0; 1].

Ranks denote the importance of the body literals. Necessary literals (i.e., those which must be satisfied) are assigned to rank $r_0$ and the less important to rank $r_m$.

The head literal confidence indicates how much body literals are satisfied. This confidence allows to valuate the head literals even if data are missing and take into account the different influences of the body literals (expressed with weights).

We let the reader refer to the article [**COMMING SOON**] for more details.
