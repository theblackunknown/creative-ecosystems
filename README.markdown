# About

Starting a sample application like McCormackBown's one which draw a picture by an bio ecosystem-like.
Goals have been kept easy for the first version but, incrementally, more and more features will be include.

# Features included in this sample

* Environnement
    * 2D map
    * Fixed-size & bounded
    * No resources management
    * No conditions management
    * Temporal Unit : number of evolution cycle
* Agent
    * Genome
        * Moving
            * directional vector
            * speed
        * Death _affected by its age_
            * Representated as a probability
        * Reproduction
            * _Mitosis_
            * Representated as a probability
        * Identifiant
            * Color _black_
            * Plain trail
* Genral rules
    * Mutation
        * Representated as a probability
        * Affect a gene by setting a new value within a certain range (dependant on the gene)
    * Normalisation
        * Tentative de normaliser chaque gène pour l'encodé sur une intervalle décimale `[-1,1]`

