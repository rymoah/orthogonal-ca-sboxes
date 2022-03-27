package bf;

/**
 * Class which represents a generic boolean function, along with its
 * representations (truth table, ANF), cryptographic properties (balancedness,
 * algebraic degree, nonlinearity).
 * 
 * @author Luca Mariot
 * @version 1.0
 * 
 */

import java.math.BigInteger;

public class BooleanFunction {
    
    //Representation-related attributes.
    private BigInteger deccode;
    private int nvar;
    private int ninputs;
    private boolean[] ttable;
    
    //Transforms-related attributes.
    private int[] poltable;
    private int[] whcoeffs;    
    private int sprad;
    private boolean[] anfcoeffs;
    private String anfexpr;
    
    //Crypto-related attributes.
    private boolean isBalanced;
    private int algdeg;
    private int nlin;
    
    /** 
     * First constructor, decimal representation-based.
     * 
     * @param deccode   decimal representation code of the function.
     * @param nvar      number of variables of the function.
     */
    public BooleanFunction(BigInteger deccode, int nvar) {
        
        this.deccode = deccode;
        this.nvar = nvar;
        
        ninputs = (int)Math.pow(2,nvar);
        ttable = BinTools.dec2Bin(deccode, ninputs);
        poltable = BinTools.bin2Pol(ttable);
        
        whcoeffs = new int[ninputs];
        anfcoeffs = new boolean[ninputs];
        
    }
    
    /** 
     * Second constructor, truth table representation-based.
     * 
     * @param ttable    truth table of the function (LSBF order)
     * @param nvar      number of variables of the function.
     */
    public BooleanFunction(boolean[] ttable, int nvar) {
        
        this.ttable = ttable;
        this.nvar = nvar;
        
        ninputs = ttable.length;
        deccode = BinTools.bin2DecBig(ttable);
        poltable = BinTools.bin2Pol(ttable);
        
        whcoeffs = new int[ninputs];
        anfcoeffs = new boolean[ninputs];
        
    }
    
    //Getters.

    public int getAlgdeg() {
        return algdeg;
    }

    public boolean[] getAnfcoeffs() {
        return anfcoeffs;
    }

    public String getAnfexpr() {
        return anfexpr;
    }

    public BigInteger getDeccode() {
        return deccode;
    }

    public boolean isIsBalanced() {
        return isBalanced;
    }

    public int getNinputs() {
        return ninputs;
    }

    public int getNvar() {
        return nvar;
    }

    public int[] getPoltable() {
        return poltable;
    }

    public int getSprad() {
        return sprad;
    }

    public boolean[] getTtable() {
        return ttable;
    }

    public int[] getWhcoeffs() {
        return whcoeffs;
    }

    public int getNlin() {
        return nlin;
    }
    
    //Setters.

    public void setAlgdeg(int algdeg) {
        this.algdeg = algdeg;
    }

    public void setAnfcoeffs(boolean[] anfcoeffs) {
        this.anfcoeffs = anfcoeffs;
    }

    public void setAnfexpr(String anfexpr) {
        this.anfexpr = anfexpr;
    }

    public void setDeccode(BigInteger deccode) {
        this.deccode = deccode;
    }

    public void setIsBalanced(boolean isBalanced) {
        this.isBalanced = isBalanced;
    }

    public void setNinputs(int ninputs) {
        this.ninputs = ninputs;
    }

    public void setNvar(int nvar) {
        this.nvar = nvar;
    }

    public void setPoltable(int[] poltable) {
        this.poltable = poltable;
    }

    public void setSprad(int sprad) {
        this.sprad = sprad;
    }

    public void setTtable(boolean[] ttable) {
        this.ttable = ttable;
    }

    public void setWhcoeffs(int[] whcoeffs) {
        this.whcoeffs = whcoeffs;
    }

    public void setNlin(int nlin) {
        this.nlin = nlin;
    }
    
}
