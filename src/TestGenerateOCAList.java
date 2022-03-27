

/**
 * Test class to check the orthogonality of the latin squares generated by
 * bipermutive rules of a given diameter
 * 
 * @author Luca Mariot
 */

import ca.*;
import bf.*;
import java.math.BigInteger;

public class TestGenerateOCAList {
    
    public static void main(String[] args) {
        
        if(args.length!= 3) {
            
            System.err.println("Usage: java lowlevelfunc.oa.TestOrthogLatSquareCA"
                                +" diameter start end");
            System.exit(1);
            
        }
        
        int n = Integer.parseInt(args[0]);
        int nvar = n;
        BigInteger start = new BigInteger(args[1]);
        BigInteger end = new BigInteger(args[2]);
        OneDimCA ca = new OneDimCA(2*(n-1), null, n, 0);
        BigInteger index = start;

        while(index.compareTo(end)==-1) {

            //Encode the current index as a pair of bipermutive rules.
            boolean[] pairconf = BinTools.dec2Bin(
                index, (int)Math.pow(2, n-1));
            boolean[] graphconf1 = new boolean[(int)Math.pow(2, n-2)];
            boolean[] graphconf2 = new boolean[(int)Math.pow(2, n-2)];
            System.arraycopy(pairconf, 0, graphconf1, 0, graphconf1.length);
            System.arraycopy(pairconf, graphconf1.length, graphconf2, 0, graphconf2.length);
            boolean[] biprule1 = BoolFunReps.decodeBipFunc(
                graphconf1, n);
            BigInteger biprulenum1 = BinTools.bin2DecBig(biprule1);
            boolean[] biprule2 = BoolFunReps.decodeBipFunc(
                graphconf2, n);
            BigInteger biprulenum2 = BinTools.bin2DecBig(biprule2);

            //Build the corresponding two Latin squares and check for orthogonality
            ca.setRule(biprule1);
            int[][] matrix1 = BuildOCASbox.buildSqMatCA(ca);
            //System.out.println(matrix1.length);
            
            ca.setRule(biprule2);
            int[][] matrix2 = BuildOCASbox.buildSqMatCA(ca);
            //System.out.println(matrix2.length);
            
            boolean orthog = BuildOCASbox.checkOrthogLatSquare(
                        matrix1, matrix2);
            
            //If orthogonal, compute nonlinearity
            //System.out.println(orthog);
            if(orthog) {
                
                BooleanFunction bf1 = new BooleanFunction(biprule1,n);
                CheckProp.computeNlin(bf1);
                    
                BooleanFunction bf2 = new BooleanFunction(biprule2,n);
                CheckProp.computeNlin(bf2);
                
                BooleanFunction gf1 = new BooleanFunction(graphconf1,n-2);
                CheckProp.computeNlin(gf1);
                    
                BooleanFunction gf2 = new BooleanFunction(graphconf2,n-2);
                CheckProp.computeNlin(gf2);
                
                //If nonlinear, print info
                if(bf1.getNlin()>0 || bf2.getNlin()>0) {
                    System.out.println("Rule f: "+biprulenum1+" NL 1: "+
                            bf1.getNlin()+" ; "+
                            "Rule g: "+biprulenum2+" NL 2: "+
                            bf2.getNlin());
                }
                
            }
            
            //Update counter
            index = index.add(BigInteger.ONE);
            //System.out.println(index);
            
        }
        
        
    }
    
}
