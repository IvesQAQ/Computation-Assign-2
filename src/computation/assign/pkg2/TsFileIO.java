/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package computation.assign.pkg2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author 1
 */
public class TsFileIO {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    
    private final String DEFAULT_TS_NAME = "DefaultTsInput.txt";
    private final String DEFAULT_OUT_NAME = "DefaultTsOutput.txt";

    public TsFileIO() {
    }

    /**
     * read TS from 指定位置或默认位置，为可选文件位置输入
     *
     * @return TranSystem
     */
    public TranSystem readTS() {
        return readTS(DEFAULT_TS_NAME);
    }

    public TranSystem readTS(String filePosition)  {
        System.out.println(ANSI_BLUE+"---------TS read begin----------"+ANSI_RESET);
        ArrayList<MapNode> TSList = new ArrayList<>();
        TranSystem TS = new TranSystem();
        boolean SInFirst = false;
        try {
            FileReader fr = new FileReader(filePosition);
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                sCurrentLine = sCurrentLine.replaceAll("\\s+", "");
                String[] div = sCurrentLine.split("=");

                if (div[0].contains("L(")) {
                    if (!SInFirst) throw new IndexOutOfBoundsException();
                    for (String str : sCurrentLine.split("},")) {
                        String[] L = str.replaceAll("[L(){}]", "").split("=");
                        TSList.stream().filter((i) -> (i.name.endsWith(L[0]))).forEachOrdered((i) -> {
                            System.out.println("L("+L[0]+") " + L[1]);
                            i.AP.addAll(Arrays.asList(L[1].split(",")));
                        });
                    }
                }

                switch (div[0]) {
                    case "S":
                        SInFirst = true;
                        System.out.println("S "+div[1]);
                        for (String i : div[1].replaceAll("[{}]", "").split(",")) {
                            MapNode node = new MapNode();
                            node.name = i;
                            TSList.add(node);
                        }
                        TS.allNode = TSList;
                        break;
                    case "Act":
                         if (!SInFirst) throw new IndexOutOfBoundsException();
                         System.out.println("Act "+div[1]);
                        break;
                    case "->":
                    case "−>":
                         if (!SInFirst) throw new IndexOutOfBoundsException();
                        System.out.println("-> " + div[1]);
                        for (String i : div[1].replaceAll("[{}]", "").split(">,<")) {
                            String[] str = i.replaceAll("[<>]", "").split(",\\S,");
                            TSList.stream().filter((nod) -> (str[0].equals(nod.name))).forEachOrdered((nod) -> {
                                TSList.stream().filter((mapNode) -> (str[1].equals(mapNode.name))).forEachOrdered((mapNode) -> {
                                    nod.next.add(mapNode);
                                });
                            });
                        }
                        break;
                    case "I":
                         if (!SInFirst) throw new IndexOutOfBoundsException();
                        System.out.println("I " + div[1]);
                        TSList.forEach((i) -> {
                            for (String string : div[1].replaceAll("[{}]", "").split(",")) {
                                if (i.name.equals(string)) {
                                    TS.init.add(i);
                                }
                            }
                });
                        break;
                    case "AP":
                         if (!SInFirst) throw new IndexOutOfBoundsException();
                         System.out.println("AP "+div[1]);
                        break;
                    case "Formula":
                    case "FORMULA":
                    case "formula":
                        System.out.println("formula " + div[1]);
                        for (String brack : div[1].replaceAll("\\s", "").split(",")) {
                            TS.formul.add(brack.replaceAll("[{}()]", ""));
                        }
                        break;
                }
            }
     
            fr.close();
            br.close();
            try {
                TS.init.get(0);
                System.out.println(ANSI_GREEN+"-------Transition System File Successfully read!--------"+ANSI_RESET);
                return TS;
            } catch (IndexOutOfBoundsException e) {
                System.err.println("Error: No initial Stats. Cannot DFS Exception. 错误：图没有开始点？（I） 没有开始点怎么DFS？   "+e);
                return null;
            }
        } catch (IOException exp) {
            System.err.println("File IO Error In <FileIO readTS> " + exp);
            return null;
        } catch (ArrayIndexOutOfBoundsException exp) {
            System.err.println("Index Error In <FileIO readTS> " + exp);
            return null;
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error: State not at first line of file Exception. 错误：能不能吧文件里面S放在第一行? 不然我就要抛S不在第一行Exception   ");
            return null;
        }
    }

    /**
     * 写出TS于默认位置，参数2为可选文件位置输出
     *
     * @param ts
     * @return boolean 是否成功输出
     */
    
    public boolean writeTs(TranSystem ts) {
        return writeTs(ts, DEFAULT_OUT_NAME);
    }
    
    public boolean writeTs(TranSystem ts,String propoerty) {
        return writeTs(ts, DEFAULT_OUT_NAME, "");
    }

    public boolean writeTs(TranSystem ts, String filePosit, String property) {
        try {
            FileOutputStream outSt = new FileOutputStream(filePosit);
            OutputStreamWriter writer = new OutputStreamWriter(outSt, "UTF-8");

            String temp = "S = {";
            for (MapNode nod : ts.allNode) {
                temp += nod.name + ", ";
            }
            temp = temp.substring(0, temp.length() - 2);
            temp += "}\n";

            temp += "Act = {}\n";

            temp += "-> = {";
            for (MapNode nod : ts.allNode) {
                for (MapNode nextNod : nod.next) {
                    temp += "<" + nod.name + ", ? ," + nextNod.name + ">, ";
                }
            }
            temp = temp.substring(0, temp.length() - 2);
            temp += "}\n";

            temp += "I = {";
            for (MapNode nod : ts.init) {
                temp += nod.name + ", ";
            }
            temp = temp.substring(0, temp.length() - 2);
            temp += "}\n";
            
            HashSet<String> allAP = new HashSet<>();
            ts.allNode.forEach((node) -> {
                node.AP.forEach((ap) -> {
                    allAP.add(ap);
                });
            });
            
            temp += "AP = {";
            temp = allAP.stream().map((string) -> string+", ").reduce(temp, String::concat);
            temp = temp.substring(0, temp.length() - 2);
            temp +="}\n";
            
            for (MapNode nod : ts.allNode) {
                temp += "L(" + nod.name + ") = {";
                for (String ap : nod.AP) {
                    temp += ap + ", ";
                }
                temp = temp.substring(0, temp.length() - 2);
                temp += "}, ";
            }
            temp = temp.substring(0, temp.length() - 2);

            if(!property.equals(""))
                temp+="\nFormula = {"+property+"}";
            
            writer.write(temp);
            System.out.println(ANSI_GREEN+"-------Transition System File Successfully Write!--------"+ANSI_RESET);
            writer.close();
            outSt.close();
            return true;
        } catch (FileNotFoundException | UnsupportedEncodingException exp) {
            System.err.println("File write Error In <FileIO writrTS> " + exp);
            return false;
        } catch (IOException exp) {
            System.err.println("File IO Error In <FileIO writrTS> " + exp);
            return false;
        }
    }
}
