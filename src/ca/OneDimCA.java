package ca;

/**
 * Class representing a one-dimensional boolean cellular automaton
 * 
 * @author Luca Mariot
 */

import bf.*;

public class OneDimCA {
    
    private boolean[] cells;        //CA array
    private boolean[] rule;         //local rule truth table (represented in LSBF order).
    private int nbrsize;            //CA neighborhood size (= number of variables of the local rule)
    private int offset;             //center of the neighborhood (cell to update)

    /**
     * Class constructor. 
     * 
     * @param nCells    the number of the cells of the CA
     * @param rule      the local rule of the CA
     * @param nbrsize   the neighborhood size of the CA (number of variables of rules)
     */
    public OneDimCA(int nCells, boolean[] rule, int nbrsize) {
        
        cells = new boolean[nCells];
        this.rule = rule;
        this.nbrsize = nbrsize;

    }
    
    public OneDimCA(int nCells, boolean[] rule, int nbrsize, int offset) {
        
        cells = new boolean[nCells];
        this.rule = rule;
        this.nbrsize = nbrsize;
        this.offset = offset;

    }
    
    /**
     * Computes the transition function of a single cell of the CA, given
     * its neighborhood as input.
     * 
     * @param nbrhood   the neighborhood vector of the cell
     * @return          the value of the local rule corresponding to the vector nbrhood
     */
    private boolean applyLocalRule(boolean[] nbrhood) {
        
        //Return the value of the truth table array whose index corresponds to
        //the decimal encoding of the neighborhood.
        
        int index = BinTools.bin2Dec(nbrhood);

        return rule[index];

    }

    /**
     * Evolves the CA from its current configuration to the next one. In particular,
     * apply the local rule to each of the first m-n+1 cells of the
     * CA. The neighborhood of each cell is formed by itself and the n-1
     * cells to its right (like it was done in the EvoCOP/HOST submission)
     * 
     */
    public void evolveCA() {

        boolean[] nextConf = new boolean[cells.length-nbrsize+1];
        int m = cells.length;
        int n = nbrsize;
        
        //Check whether there are at least n cells to apply the local rule
        if(m >= n) {
            
            //Apply the local rule to each of the first m-n+1 cells
            //of the CA array
            for(int i=0; i<nextConf.length; i++) {

                    //Build the neighborhood of the i-th cell
                    boolean[] nbrhood = new boolean[nbrsize];
                    for(int j=0; j<nbrsize; j++) {

                        nbrhood[j] = cells[i+j];

                    }

                    //Update the state of the i-th cell by computing the local rule
                    nextConf[i] = applyLocalRule(nbrhood);
                
            }
            
        }

        //Set the new CA array of nCells-nbrsize+1 cells
        cells=nextConf;

    }

    //Getters and setters
    
    public boolean[] getCells() {
        return cells;
    }

    public void setCells(boolean[] cells) {
        this.cells = cells;
    }

    public boolean[] getRule() {
        return rule;
    }

    public void setRule(boolean[] rule) {
        this.rule = rule;
    }

    public int getNbrsize() {
        return nbrsize;
    }

    public void setNbrsize(int nbrsize) {
        this.nbrsize = nbrsize;
    }
    
    
}

