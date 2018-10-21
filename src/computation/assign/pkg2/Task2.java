/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author  15912741    Peter Wang
 *           15890117    Harry Yao
 */
public class Task2 {
    
    private final static int DEFAULT_PROCESSNUM = 2;
    
    private int[] peoc;
    private int[] lev;
    private int[] states;// 0 means non-crit, 1 means wait, 2 means crit 
    private TranSystem TS;
    private Queue<int[]> nodesQueue;
    private ArrayList<int[]> depthP;

    public Task2(){
        this(DEFAULT_PROCESSNUM);
    }
    
    public Task2(int n) {
        this.TS = new TranSystem();
        
        this.states = new int[n];
        this.peoc = new int[n];
        this.lev = new int[n];
        nodesQueue = new LinkedList<>();
        depthP = new ArrayList<>();
        
        for (int i = 0; i < states.length; i++) {
            states[i] = 0;
            peoc[i] = 0;
            lev[i] = 1;
        }
    }
    

    public void setProcess(int[] process) {
        this.peoc = Arrays.copyOf(process, process.length);
    }
    
    public int[] getDepthP(){
        return depthP.remove(0);
    }
    
    public void addDepthP(int[] a){
        depthP.add(a);
    }
    
    public void exploreNodes(){
        addNodeToAllStates(states,peoc);
        int k = 0;
        int m = 0;
        while (!nodesQueue.isEmpty()) { 
            int[] parentState = nodesQueue.poll();
            System.out.println("-----------------------------");
            System.out.println(parentState[0]+"----state----"+parentState[1]);
            MapNode parentNode = TS.allNode.get(k++);
            int[] p = getDepthP();
            System.out.println(p[0]+","+p[1]+" ---pop up--"+(m-1));
            for (int i = 0; i < depthP.size(); i++) {
            int[] test = depthP.get(i);
            System.out.println(test[0]+","+test[1]+"----"+(m-1));
            }
            for (int i = 0; i < parentState.length; i++) {
               int[] l = p;
                int[] child = Arrays.copyOf(parentState, parentState.length);
                if(child[i] == 1){
                    setProcess(p);
                    if (checkRearchCrit(i)) {
                        child[i] = (child[i]+1) % 3;
                        MapNode childNode = addNodeToAllStates(child,peoc);
                        System.out.println("after"+depthP.size());
                        parentNode.next.add(childNode);
                    }
                }else{
                    if (child[i] == 2) {
                        peoc[i] = 0;
                    }
                    child[i] = (child[i]+1) % 3;
                    MapNode childNode = addNodeToAllStates(child, p);
                    System.out.println("after"+depthP.size());
                    parentNode.next.add(childNode);
                }
                System.out.println(peoc[0]+"---Processer--"+peoc[1]);
                System.out.println(child[0]+"----states----"+child[1]);
            }
        }
        TS.init.add(TS.allNode.get(0));
    }
    
    
    public MapNode addNodeToAllStates(int[] state, int[] p){
        int[] newp = Arrays.copyOf(p, p.length);
        String tempname="";
        for (int i : state) {
            tempname+=i+"-";
        }
        tempname = tempname.substring(0, tempname.length() - 1);
        MapNode node = new MapNode(tempname);
        for (int i : state) {
            node.AP.add(i+"");
        }
        MapNode check = checkInsideAllState(node);
        if (check == null) {
            TS.allNode.add(node);
            nodesQueue.add(state);
            
            addDepthP(newp);
            for (int i = 0; i < depthP.size(); i++) {
            int[] test = depthP.get(i);
            System.out.println(test[0]+","+test[1]);
        }
            check = node;
        }
        
        return check;
    }
    
    public MapNode checkInsideAllState(MapNode node){
        String name = node.name;
        if (!TS.allNode.isEmpty()) {
            for (MapNode n : TS.allNode) {
                if (n.name.equals(name)) {
                    return n;
                }
            }
        }
        return null;
    }
    
    public boolean checkRearchCrit(int n){
        for (int j = 1; j < peoc.length; j++) {
            peoc[n] = j;
            lev[j] = n;
            if (checkPassWait(n, j)) {
                return true;
            }
        }
        return false;
    }
    
   
    
    public boolean levelCheck(int j, int i){
        for (int k = 0; k < peoc.length; k++) {
            if(i != k && peoc[k] >= j){
               return false;
            }
        }
        return true;
    }
    
     public boolean checkPassWait(int i, int j){
        System.out.println(lev[j] != i);
        System.out.println(levelCheck(j, i));
        return lev[j] != i || levelCheck(j, i);
        
    }
    
     
     public void outputTStoFile(){
         TsFileIO io = new TsFileIO();
         io.writeTs(TS, "task2.txt","not more than 1 C");
     }
     
    public static void main(String[] args) {
        Task2 task2 = new Task2(2);
        task2.exploreNodes();
        
        System.out.println("\n------------------------------");
        for (int i = 0; i < task2.TS.allNode.size(); i++) {
            System.out.println(task2.TS.allNode.get(i).name);   
        }
        System.out.println("\nSize of this TS is: "+task2.TS.allNode.size());
        
        task2.outputTStoFile();
    }
}
