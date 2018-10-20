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
    public ArrayList<MapNode> allNode;
    public ArrayList<MapNode> tempnode;
    public ArrayList<MapNode> reachable;

    public boolean b = true;
    public boolean contain = false;

    ComputationAssign2() {
        this(new TranSystem());
    }

    ComputationAssign2(TranSystem ts) {
        this.ts = ts;
        allNode = ts.allNode;
        initnode = ts.getInit();
        tempnode = new ArrayList<>();
        reachable = new ArrayList<>();
    }

    public void DFS() {
//       Iterator<MapNode> mapiterator = initnode.iterator();
        for (String formul : ts.formul) {
            while (!allNode.isEmpty() && b) {
                MapNode node = initnode.get(0);
                if (!tempnode.isEmpty()) {
                    node = nodenotinreachfromts();
                }
                if (node != null) {
                    if (!node.next.isEmpty()) {
                        visit(node, formul);
                    } else {
                        reachable.add(node);
                        allNode.remove(node);
                    }
                }
                for (MapNode mapNode : reachable) {
                    System.out.print(mapNode.name+" ");
                }
                System.out.print("\n");
            }
            System.out.println("Formular: "+formul);
            System.out.println("output: "+ b+((b)?" (Yes)":" (NO)")+"\n");
        }
    }

    public void visit(MapNode node,String formula) {
        tempnode.add(node);
        reachable.add(node);
        do {
            //get the last node in tempnode
            MapNode node1 = tempnode.get(tempnode.size() - 1);
            MapNode notinreach = getnodenotinreach(node1);
            if (contain  ) {
                tempnode.remove(node);
                if(formula.toLowerCase().contains("or")){
                    String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("or");
                    b = b && (node.AP.contains(form[0]) || node.AP.contains(form[1]));
                }else if (formula.toLowerCase().contains("imp") && formula.toLowerCase().contains("not")) {
                    String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("imp");
                    if (form[0].contains("not")) {
                        b = b && (node.AP.contains(form[0].split("not")[0]) || node.AP.contains(form[1]));
                    } else {
                        b = b && (!node.AP.contains(form[0]) || node.AP.contains(form[1].split("not")[0]));
                    }
                }else if(formula.toLowerCase().contains("NOT")){
                    String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("not");
                    b = b && !node.AP.contains(form[0]);
                }else if(formula.toLowerCase().contains("and ")){
                    String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("and");
                    b = b && (node.AP.contains(form[0]) && node.AP.contains(form[1]));
                }else if(formula.toLowerCase().contains("imp ")){
                    String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("imp");
                    b = b && (!node.AP.contains(form[0]) || node.AP.contains(form[1]));
                }else{
                     b = b && node.AP.contains(formula) ;
                }
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
        for (MapNode mapNode : allNode) {
            if (!reachable.contains(mapNode)) {
                return mapNode;
            }
            if (mapNode.equals(allNode.get(allNode.size() - 1))) {
                allNode.clear();
                break;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        TsFileIO io = new TsFileIO();
        ComputationAssign2 ass2 = new ComputationAssign2(io.readTS());
        ass2.DFS();
        
    }

}
