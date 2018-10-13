/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author
 */
public class ComputationAssign2 {

    /**
     * @param args the command line arguments
     */
    //inital node mapnode
    public TranSystem ts = new TranSystem();
    public ArrayList<MapNode> initnode;
    public ArrayList<MapNode> allNode1;
    public ArrayList<MapNode> tempnode;
    public ArrayList<MapNode> reachable;
    public ArrayList<MapNode> allNode;

    public boolean b = true;
    public boolean contain = false;

    ComputationAssign2() {
        allNode = ts.allNode;
        initnode = ts.getInit();
        tempnode = new ArrayList<>();
        allNode1 = allNode;
        reachable = new ArrayList<>();
    }

    public void DFS() {
//       Iterator<MapNode> mapiterator = initnode.iterator();
        while (!allNode1.isEmpty() && b) {
            MapNode node = initnode.get(0);
            if (!tempnode.isEmpty()) {
                node = nodenotinreachfromts();
            }
            if (!node.next.isEmpty()) {
                visit(node);
            }else{
                reachable.add(node);
                allNode1.remove(node);
            }
            for (MapNode mapNode : reachable) {
                System.out.println(mapNode.name);
            }System.out.println("\n");
        }
    }

    public void visit(MapNode node) {
        tempnode.add(node);
        reachable.add(node);
        do {
            //get the last node in tempnode
            MapNode node1 = tempnode.get(tempnode.size() - 1);
            MapNode notinreach = getnodenotinreach(node1);
            if (contain) {
                tempnode.remove(node);
                b = b && (node.AP.contains("a") || node.AP.contains("b"));
//                break;
            } else {
                MapNode node2 = notinreach;
                tempnode.add(node2);
                reachable.add(node2);
            }

        } while (tempnode.isEmpty() || !b);

    }

    public MapNode getnodenotinreach(MapNode node) {
        MapNode temp = new MapNode();
        for (MapNode mapNode : node.next) {
            if (reachable.contains(mapNode)) {
                contain = true;
                break;
            } else {
                temp = mapNode;
            }
        }
        return temp;
    }

    public MapNode nodenotinreachfromts() {
        for (MapNode mapNode : initnode) {
            if (!reachable.contains(mapNode)) {
                return mapNode;
            }
            if(mapNode.equals(allNode1.get(allNode1.size()-1))){
                allNode1.clear();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        ComputationAssign2 ass2 = new ComputationAssign2();
        ass2.DFS();

    }

}
