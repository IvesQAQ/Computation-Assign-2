/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

/**
 *
 * @author 琉小璃
 */
public class ComputationAssign2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        TsFileIO io = new TsFileIO();
        io.writeTs(io.readTS(), "tests.txt");
    }

}
