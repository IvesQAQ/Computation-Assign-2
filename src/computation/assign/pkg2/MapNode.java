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
    public ArrayList<String> name;
    public ArrayList<MapNode> next;
    public ArrayList<String> AP;

    public MapNode() {
        name = new ArrayList<>();
        next = new ArrayList<>();
        AP = new ArrayList<>();
    }
    
}