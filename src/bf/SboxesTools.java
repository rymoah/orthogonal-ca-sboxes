package bf;

/**
 * Class containing methods related to S-boxes
 * 
 * @author Luca Mariot
 */

public class SboxesTools {
    
     /**
     * Compute the component function of a S-box
     * 
     * @param sbox  boolean matrix specifying the S-box
     * @param v     boolean vector specifying the component
     * @return      the truth table of the component function selected by v 
     */
    public static boolean[] calcComponent(int nvar, boolean[][] sbox, boolean[] v) {
        
        //int nvar = sbox[0].length;
        int complength = (int)Math.pow(2, nvar);
        boolean[] compfunc = new boolean[complength];
        
        //Build the truth table of the component function as the scalar product
        //of the s-box by the vector v
        for(int i = 0; i<complength; i++) {
            
            boolean[] sboxout = sbox[i];
            compfunc[i] = BinTools.scalarProduct(v, sboxout);
            
        }
        
        return compfunc;
        
    }
    
    /**
     * Compute the nonlinearity of a S-box as the minimum nonlinearity of its
     * component functions.
     * 
     * @param sbox
     * @param v
     * @return 
     */
    public static int calcNlinSbox(boolean[][] sbox, int ncells) {
        
        int minnlin = 0;
        int m = sbox[0].length;
        int ncomp = (int)Math.pow(2, m);
        int lcomp = 0;  //counter for linear components
        
        for(int i=1; i<ncomp; i++) {
            
            boolean[] curcomp = BinTools.dec2BinMod(i, m);
            
            //Compute the component function selected by curcomp and
            //determine its nonlinearity
            boolean[] compfunc = calcComponent(ncells, sbox, curcomp);
            BooleanFunction bfcomp = new BooleanFunction(compfunc, ncells);
            CheckProp.computeNlin(bfcomp);
            
            if(i==1) {
                minnlin = bfcomp.getNlin();
            } else {
                
                if (bfcomp.getNlin() < minnlin) {
                    minnlin = bfcomp.getNlin();
                }
            
            }
            
            if(bfcomp.getNlin() == 0) {
                lcomp++;
                System.out.print("Component "+BinTools.bool2Bin(curcomp)+" -- NL:"+bfcomp.getNlin());
                System.out.println("");
            }
            
            
        }
        
        System.out.println("Number of linear components: "+lcomp);
        
        return minnlin;
        
    }
    
    /**
     * Compute the unbalancedness of a vectorial boolean function. Basically,
     * it sums the unbalancedness of all nonzero component functions of the
     * S-box.
     * 
     * @param sbox a vectorial boolean function
     * @return 
     */
    public static boolean computeUnbalancedness(int nvar, boolean[][] sbox) {

        boolean toRet = true;
        int m = sbox[0].length;
        int ncomp = (int)Math.pow(2, m);
        
        for(int i=1; i<ncomp; i++) {
            
            boolean[] curcomp = BinTools.dec2BinMod(i, m);
            
            //Compute the component function selected by curcomp and determine if it is balanced or not
            boolean[] compfunc = calcComponent(nvar, sbox, curcomp);
            if(!BinTools.isBalanced(compfunc)) {
                toRet = false;
            }
            
        }
        
        return toRet;
        
    }
    
}
