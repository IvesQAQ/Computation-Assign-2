/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 * @author  15912741    Peter Wang
 *           15890117    Harry Yao
 */
public class ComputationAssign2 {

    /**
     * @param args the command line arguments
     */
    //inital node mapnode
    public TranSystem ts = new TranSystem();
    public ArrayList<MapNode> initnode;
    public ArrayList<MapNode> allNode;
    public ArrayList<MapNode> allNodeCopy;
    public ArrayList<MapNode> tempnode;
    public HashSet<MapNode> reachable;
    public boolean breakcheck = false;

    public boolean b = true;
    public boolean contain = false;
    public int index = 0;

    ComputationAssign2() {
        this(new TranSystem());
    }

    ComputationAssign2(TranSystem ts) {
        this.ts = ts;
        allNode = ts.allNode;
        allNodeCopy= new ArrayList<>();
        for (MapNode mapNode : ts.allNode) {
            this.allNodeCopy.add(mapNode);
        }
        initnode = ts.getInit();
        tempnode = new ArrayList<>();
        reachable = new HashSet<>();
    }

    public void DFS() {
        for (String formul : ts.formul) {
            long timeDurtaoin = System.nanoTime();
            b=true;
            for (MapNode mapNode : this.allNodeCopy) {
                this.allNode.add(mapNode);
            }
            while (!allNode.isEmpty() && b) {
                MapNode node = initnode.get(0);
                    node = nodenotinreachfromts();
                if (node != null) {
                    if (!node.next.isEmpty()) {
                        breakcheck = visit(node, formul);
                    } else {
                        allNode.remove(node);
                    }
                }
                for (MapNode mapNode : reachable) {
                    System.out.print(mapNode.name + " ");
                }
                System.out.print("\n");
                 if(!breakcheck)break;
            }
            System.out.println("Formular: " + formul);
            System.out.println("output: " + breakcheck + ((breakcheck) ? " (Yes)" : " (NO)") + "\n");
            System.out.println("Time use: " + (System.nanoTime() - timeDurtaoin)+" nanoseconds.");
            System.out.println("Used Memory：" +(Runtime.getRuntime().totalMemory() -  Runtime.getRuntime().freeMemory())/(1024 * 1024)); 
        }
    }

    public boolean visit(MapNode node, String formula) {
        tempnode.add(node);
        reachable.add(node);
        System.out.println("loop node " + node.name);
        do {
            if (formula.toLowerCase().contains("morethan1")) {
                int tempint=0;
                for (String string : tempnode.get(tempnode.size() - 1).AP) {
                   if(string.contains("2"))tempint++;
                }
                if(tempint>1)
                    b = b && false;
                else
                     b = b && true;
                 if(!b)return false;
            } else if (formula.toLowerCase().contains("or")) {
                String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("or");
                b = b && (tempnode.get(tempnode.size() - 1).AP.contains(form[0]) || tempnode.get(tempnode.size() - 1).AP.contains(form[1]));
                 if(!b)return false;
            } else if (formula.toLowerCase().contains("imp") && formula.toLowerCase().contains("not")) {
                String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("imp");
                if (form[0].contains("not")) {
                    //all false
                    b = b && (tempnode.get(tempnode.size() - 1).AP.contains(form[0].split("not")[0]) || tempnode.get(tempnode.size() - 1).AP.contains(form[1]));
                    if(!b)return false;
                } else {
                    //all true
                    b = b && (!tempnode.get(tempnode.size() - 1).AP.contains(form[0]) || !tempnode.get(tempnode.size() - 1).AP.contains(form[1].split("not")[1]));
                     if(!b){
                         return false;
                     }
                }
            } else if (formula.toLowerCase().contains("NOT")) {
                String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("not");
                b = b && !tempnode.get(tempnode.size() - 1).AP.contains(form[0]);
                 if(!b)return false;
            } else if (formula.toLowerCase().contains("and ")) {
                String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("and");
                b = b && (tempnode.get(tempnode.size() - 1).AP.contains(form[0]) && tempnode.get(tempnode.size() - 1).AP.contains(form[1]));
                 if(!b)return false;
            } else if (formula.toLowerCase().contains("imp ")) {
                String[] form = formula.toLowerCase().replaceAll("\\s+", "").split("imp");
                b = b && (!tempnode.get(tempnode.size() - 1).AP.contains(form[0]) || tempnode.get(tempnode.size() - 1).AP.contains(form[1]));
            }else {
                b = b && tempnode.get(tempnode.size() - 1).AP.contains(formula);
            }
            //get the last node in tempnode
            MapNode node1 = tempnode.get(tempnode.size() - 1);
            MapNode notinreach = getnodenotinreach(node1);

            if (notinreach.name == null) {
                tempnode.remove(node1);
            } else {
                if (notinreach.next.equals(null)) {
                    tempnode.remove(notinreach);
                } else {
                    tempnode.add(notinreach);
                    reachable.add(notinreach);
                }
            }

        } while (!tempnode.isEmpty() || !b);
        return true;
    }

    public MapNode getnodenotinreach(MapNode node) {
        MapNode temp = new MapNode();
        for (MapNode mapNode : node.next) {
            if (reachable.contains(mapNode)) {
                contain = true;
            } else {
                temp = mapNode;
                break;
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
        TsFileIO io = new TsFileIO();
        ComputationAssign2 ass2 = new ComputationAssign2(io.readTS());
        ass2.DFS();
        //reading txt file for implment task2
        ComputationAssign2 task2 = new ComputationAssign2(io.readTS("task2.txt"));
        task2.DFS();
    }

}
