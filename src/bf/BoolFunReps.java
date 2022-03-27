package bf;

/**
 * Methods to represent the enumerative encoding of bipermutive rules.
 * 
 * @author Luca Mariot
 * @version 1.0
 */

import java.math.BigInteger;
import java.util.Vector;

public class BoolFunReps {
    
    /**
     * Given the code of a bipermutive boolean function, returns its truth
     * table.
     * 
     * @param graphconf a boolean array representing the code of a bipermutive
     *                  boolean function.
     * @param nvar      the number of variables of the boolean function.
     * @return func     the decoded truth table of the bipermutive boolean
     *                  function.
     */
    public static boolean[] decodeBipFunc(boolean[] graphconf, int nvar) {
        
        int funclength = (int)Math.pow(2,nvar);
        boolean[] func = new boolean[funclength];
        int half = (int)Math.pow(2, nvar-1);
        
        for(int j=0; j<graphconf.length; j++) {
                
            //Instantiate nodes on the j-th graph
            func[2*j] = graphconf[j];
            func[(2*j)+half+1] = graphconf[j];
            func[(2*j)+1] = !graphconf[j];
            func[(2*j)+half] = !graphconf[j];

        }
        
        return func;
        
    }
    
    /**
     * Given the code of a center-permutive boolean function, returns its truth
     * table.
     * 
     * @param graphconf a boolean array representing the code of a bipermutive
     *                  boolean function.
     * @param nvar      the number of variables of the boolean function.
     * @return func     the decoded truth table of the bipermutive boolean
     *                  function.
     */
    public static boolean[] decodeCPermFunc(boolean[] graphconf, int nvar, int offset) {
        
        int funclength = (int)Math.pow(2,nvar);
        boolean[] func = new boolean[funclength];
        int half = (int)Math.pow(2, nvar-1);
        
        for(int j=0; j<graphconf.length; j++) {
                
            //Instantiate nodes on the j-th graph
            boolean[] binind = BinTools.dec2BinMod(j, nvar-1);
            boolean[] realind1 = new boolean[nvar];
            boolean[] realind2 = new boolean[nvar];
            //Put 0 in the middle of binind for realind1 and 1 in the middle for realind2
            for(int i=0; i<offset; i++) {
                
                realind1[i] = binind[i];
                realind2[i] = binind[i];
                
            }
            
            realind1[offset] = false;
            realind2[offset] = true;
            
            for(int i=offset+1; i<realind1.length; i++) {
                
                realind1[i] = binind[i-1];
                realind2[i] = binind[i-1];
                
            }
            
            //Convert realind1 and realind2 back to integers and set the former to graphconf[i] and the latter to 1 XOR graphconf[i]
            int ind1 = BinTools.bin2Dec(realind1);
            int ind2 = BinTools.bin2Dec(realind2);
            
            func[ind1] = graphconf[j];
            func[ind2] = true ^ graphconf[j];
                    
        }
        
        return func;
        
    }
    
    /**
     * Given a bipermutive boolean function, returns the binary configuration
     * of its graph.
     * 
     * @param boolfun   a BooleanFunction object representing a bipermutive
     *                  boolean function.
     * @return          the binary configuration of the graph of the function.
     */
    public static boolean[] encodeBipFunc(BooleanFunction boolfun) {
        
        int nvar = boolfun.getNvar();
        //System.out.println(nvar);
        boolean[] ttable = boolfun.getTtable();
        int ngraphs = (int)Math.pow(2,nvar-2);
        boolean[] graphconf = new boolean[ngraphs];
        //System.out.println(graphconf.length);
        
        for(int i=0; i<graphconf.length; i++) {
            
            //The i-th bit of the graph configuration is the value of the
            //function corresponding to the input built by setting the
            //leftmost and rightmost coordinates to 0, and the central
            //coordinates are the binary representation of i.
            boolean[] input = new boolean[nvar];
            boolean[] centr = BinTools.dec2BinMod(i, nvar-2);
            System.arraycopy(centr, 0, input, 1, centr.length);
            int decinput = BinTools.bin2Dec(input);
            graphconf[i] = ttable[decinput];
            
        }
        
        return graphconf;
        
    }
    
}
