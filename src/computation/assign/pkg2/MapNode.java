/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.util.ArrayList;

/**
 *
 * @author 琉小璃
 */
public class MapNode {
    public String name;
    public ArrayList<MapNode> next;
    public ArrayList<String> AP;

<<<<<<< HEAD
    public MapNode(String name) {
        this.name = name;
=======
    public MapNode() {
>>>>>>> 9a41a478d8b0ea824fdd9e2468852f420bd5345f
        next = new ArrayList<>();
        AP = new ArrayList<>();
    }
    public MapNode(){
    }

    public void setNext(MapNode next) {
        this.next.add(next);
    }

    public void setAP(ArrayList<String> AP) {
        this.AP = AP;
    }

    public ArrayList<MapNode> getNext() {
        return next;
    }

    public ArrayList<String> getAP() {
        return AP;
    }
    
    
    
    
}
