# W-SWRL
This repository contains an implementation of the W-SWRL reasoner. W-SWRL is an extension of SWRL that deals with missing data and hierarchical body literals. W-SWRL rules are description logic horn clauses that support classical SWRL rule elements (i.e., unary predicates, binary predicates, built-in, and variables). Body literals (also called body atoms) of W-SWRL rules are assigned to ranks ranging from rank $r_0$ to $r_m$ ($m \geq 0$). The head literal (also called the head atom) is associated with a confidence indicator ranging from 0 to 1.

Ranks denote the importance of the body literals. Necessary literals (i.e., those which must be satisfied) are assigned to rank $r_0$ and the less important to rank $r_m$.

The head literal confidence indicates how much body literals are satisfied. This confidence valuates the head literals even if data are missing and considers the different influences of the body literals (expressed with weights).

We let the reader refer to the article [**COMMING SOON**] for more details.

# Using this code
## Requirements
The code provided in this repository can be run using **Java 8 or higher**.

This project relies on [Maven (external link)](https://maven.apache.org/install.html) for compilation, execution, and managing dependencies.

## Run example
An example is available in the *src/main/java/com/sebastienguillemin/wswrl/App.java* file. To run it, run the following command:

```
mvn clean install exec:java
```

This example will load the ontology described in *src/main/resources/exampleontology.ttl*, create the WSWRL rule *concept1(?x)^1*concept1(?y)^differentFrom(?x,?y) -> linked(?x, ?y)* and store the result in a classical turtle file and a [turtle-star file](https://w3c.github.io/rdf-star/cg-spec/editors_draft.html).

# Implementation details
## JavaDoc
The project JavaDoc is available <a href="https://sebastienguillemin.github.io/w-swrl/">here</a>.

## Dependencies
This project depends on the following packages:

<!-- - A compléter ! -->
- The Stanford SWRLAPI drools engine implementation (version 2.1.2). *[Maven central](https://central.sonatype.com/artifact/edu.stanford.swrl/swrlapi-drools-engine)*
- Lombok (version 1.18.32) *[Maven central](https://central.sonatype.com/artifact/org.projectlombok/lombok)*

## Import notes
This implementation does not yet support some of the SWRLAPI features. Moreover, some OWL assertions have not yet been correctly serialised to Turtle*.

Feel free to propose some improvements and bug corrections.