package ca;

/**
 * Compute the S-Box generated by two orthogonal Latin squares produced by 
 * bipermutive cellular automata.
 * 
 * @author Luca Mariot
 */
import bf.*;
import java.math.BigInteger;

public class BuildOCASbox {
    
    /**
     * Build a square matrix from a one-dimensional CA. Starting from a
     * configuration of 2*blocklen bits, the CA is iterated forward until a
     * configuration of blocklen bits is reached. The initial configuration is
     * partitioned in 2 vectors of equal length, A and B, determined by the
     * position of the initial block (parameter pos). The final configuration is
     * denoted by C. The matrix is then filled by considering the three blocks
     * respectively as row, column and value.
     * 
     * @param ca an instance of a one-dimensional CA
     * @return 
     */
    public static int[][] buildSqMatCA(OneDimCA ca) {
        
        int n = ca.getNbrsize();        //number of variables of the local rule
        //System.out.println(n);
        int block = n-1;                //size of the blocks used to index the matrix
        int m = 2*block;                //size of the initial configuration of the CA array
        
        int dim = (int)Math.pow(2,block);  //dimension (side) of the square matrix, 2^(n-1)
        int[][] matrix = new int[dim][dim];
        
        for(int i=0; i<(dim*dim); i++) {
            
            //Encode the current index in binary form, set it as a CA configuration and
            //partition it with respect to the position parameter.
            boolean[] initconf = BinTools.dec2BinMod(i, m);
            ca.setCells(initconf);
            boolean[] rowblock = new boolean[block];
            boolean[] colblock = new boolean[block];
            
            //Copy the first half of the CA array in the block encoding the row
            //of the matrix and the second half in the block encoding the column
            System.arraycopy(initconf, 0, rowblock, 0, block);
            System.arraycopy(initconf, block, colblock, 0, block);
            
            //Evolve the CA on the current configuration
            ca.evolveCA();
            
            //Convert rowblock, colblock and newconf in decimal numbers
            boolean[] newconf = ca.getCells();
            int row = BinTools.bin2Dec(rowblock);
            int col = BinTools.bin2Dec(colblock);
            int val = BinTools.bin2Dec(newconf);
            
            //Set the value in the matrix at the specified coordinates
            //NOTICE: we add 1 to the value so that the result ranges in the
            //set {1,...,2^(n-1)} instead of {0,...,2^(n-1)-1}
            matrix[row][col] = val+1;
            
        }
        
        return matrix;
        
    }
    
    public static boolean[][] buildSbox(OneDimCA ca1, OneDimCA ca2) {
        
        int nvar = ca1.getNbrsize();
        int calen = ca1.getCells().length;
        int inputlen = (int)Math.pow(2,calen);
        
        boolean[][] sbox = new boolean[inputlen][calen];
        
        //Build the Latin squares
        int[][] lsq1 = buildSqMatCA(ca1);
        int[][] lsq2 = buildSqMatCA(ca2);
        
        //Build the S-box
        for(int i=0; i<lsq1.length; i++) {
            
            for(int j=0; j<lsq1[i].length; j++) {
                
                boolean[] input1 = BinTools.dec2BinMod(i, nvar-1);
                boolean[] input2 = BinTools.dec2BinMod(j, nvar-1);
                
                //Concatenate the two inputs
                boolean[] input = new boolean[calen];
                System.arraycopy(input1, 0, input, 0, nvar-1);
                System.arraycopy(input2, 0, input, nvar-1, nvar-1);
                int inputind = BinTools.bin2Dec(input);
                        
                boolean[] out1 = BinTools.dec2BinMod(lsq1[i][j]-1, nvar-1);
                boolean[] out2 = BinTools.dec2BinMod(lsq2[i][j]-1, nvar-1);
                boolean[] out = new boolean[calen];
                
                //Concatenate the two outputs
                System.arraycopy(out1, 0, out, 0, nvar-1);
                System.arraycopy(out2, 0, out, nvar-1, nvar-1);
                
                //Assign the output to the S-xbox
                sbox[inputind] = out;
                
            }
            
        }
        
        return sbox;
        
    }
    
    /**
     * Check whether two latin squares are orthogonal, that is by superposing
     * them each ordered pair of symbols appears exactly one time.
     * @param ls1 first latin square
     * @param ls2 second latin square. Must be of the same dimension of ls1
     * @return true if ls1 and ls2 are orthogonal, false otherwise
     */
    public static boolean checkOrthogLatSquare(int[][] ls1, int[][] ls2) {
        
        boolean[][] mark = new boolean[ls1.length][ls1[0].length];
        
        for(int i=0; i<ls1.length; i++) {
            
            for(int j=0; j<ls1[i].length; j++) {
                
                //If the couple in position (i,j) has already been marked,
                //return false, otherwise mark it
                if(mark[ls1[i][j]-1][ls2[i][j]-1]) {
                    return false;
                } else {
                   mark[ls1[i][j]-1][ls2[i][j]-1] = true;                 
                }  
                
            }
            
        }
        
        return true;
        
    }
    
    public static void main(String[] args) {
        
        if(args.length != 3) {
            
            System.err.println("Usage: java lowlevelfunc.oa.BuildOCASbox nvar"
                    + " rule1 rule2");
            System.exit(1);
            
        }
        
        //read command line parameters
        int nvar = Integer.parseInt(args[0]);
        BigInteger rule1num = new BigInteger(args[1]);
        BigInteger rule2num = new BigInteger(args[2]);
        
        int tlength = (int)Math.pow(2, nvar);
        int ncells = 2*(nvar-1);
        
        boolean[] rule1 = BinTools.dec2Bin(rule1num, tlength);
        boolean[] rule2 = BinTools.dec2Bin(rule2num, tlength);
        
        OneDimCA ca1 = new OneDimCA(ncells, rule1, nvar);
        OneDimCA ca2 = new OneDimCA(ncells, rule2, nvar);
        
        boolean[][] sbox = buildSbox(ca1, ca2);
        
        //Print the S-box in integer form
        System.out.print("\nS-Box table: ");
        for(int j=0; j<sbox.length; j++) {

            int value = BinTools.bin2Dec(sbox[j]);
            System.out.print(value+" ");

        }
        System.out.println("\n");
        //Compute balancedness, nonlinearity and delta uniformity of the S-box
        boolean bal = SboxesTools.computeUnbalancedness(ncells, sbox);
        int nl = SboxesTools.calcNlinSbox(sbox, ncells);
        System.out.println("Balanced: "+bal);
        System.out.println("Nonlinearity: "+nl);
        
    }   
    
    
}