/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.util.ArrayList;

/**
 *
 * @author  15912741    Peter Wang
 *           15890117    Harry Yao
 */
public class TranSystem {
    public ArrayList<MapNode> init;
    public ArrayList<MapNode> allNode;
    public ArrayList<String> formul;
    
    public TranSystem() {
        this.init = new ArrayList<>();
        this.allNode = new ArrayList<>();
        this.formul = new ArrayList<>();
    }

    public ArrayList<MapNode> getInit() {
        return init;
    }
    
    
}
