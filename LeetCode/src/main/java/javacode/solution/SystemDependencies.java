package javacode.solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SystemDependencies {
    public enum CMD {
        DEPEND("DEPEND"), INSTALL("INSTALL"), REMOVE("REMOVE"), LIST("LIST");
        private String s;

        CMD(String s) {
            this.s = s;
        }

        public String toString() {
            return s;
        }
    }

    List<String> res = new ArrayList<>();
    Map<String, Set<String>> depMap = new HashMap<>();
    Map<String, Set<String>> useMap = new HashMap<>();
    Map<String, Boolean> installed = new HashMap<>();

    List<String> process(List<String> commands) {

        for (String cmd : commands) {
            res.add(cmd);
            String[] token = cmd.split("[ ]+");
            switch (token[0]) {
            case "DEPEND":
                addDepend(token);
                break;
            case "INSTALL":
                install(token[1], true);
                break;
            case "REMOVE":
                uninstallExp(token[1]);
                break;
            case "LIST":
                list();
                break;
            default:
                break;
            }
        }

        return res;
    }

    private void addDepend(String[] token) {
        String pro = token[1];
        if (!depMap.containsKey(pro)) {
            depMap.put(pro, new HashSet<String>());
        }
        for (int i = 2; i < token.length; i++) {
            String dep = token[i];
            depMap.get(pro).add(dep);
            if (!useMap.containsKey(dep)) {
                useMap.put(dep, new HashSet<String>());
            }
            useMap.get(dep).add(pro);
        }
    }

    private void install(String pro, boolean explicit) {
        if (installed.containsKey(pro)) {
            if (explicit) {
                res.add(pro + " is already installed.");
            }
            return;
        }

        // -! depMap, useMap may not contain every item
        for (String dep : depMap.getOrDefault(pro, new HashSet<>())) {
            install(dep, false);
        }
        res.add("Installing " + pro);
        installed.put(pro, explicit);
    }

    private void uninstallExp(String pro) {
        if (!installed.containsKey(pro)) {
            res.add(pro + " is not installed.");
            return;
        }

        // check usage
        for (String use : useMap.getOrDefault(pro, new HashSet<>())) {
            if (installed.containsKey(use)) {
                res.add(pro + " is still needed.");
                return;
            }
        }

        // able to uninstall
        res.add("Removing " + pro);
        installed.remove(pro);
        for (String dep : depMap.getOrDefault(pro, new HashSet<>())) {
            uninstallImp(dep);
        }

    }

    private void uninstallImp(String pro) {
        if (installed.containsKey(pro) && installed.get(pro) == false) {
            for (String use : useMap.get(pro)) {
                if (installed.containsKey(use)) {
                    return;
                }
            }
            res.add("Removing " + pro);
            installed.remove(pro);
            for (String dep : depMap.getOrDefault(pro, new HashSet<>())) {
                uninstallImp(dep);
            }
        }
    }

    private void list() {
        for (String pro : installed.keySet()) {
            res.add(pro);
        }
    }

    @Test
    public void test() {
        List<String> input = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader("src/main/java/javacode/solution/input.txt");
            br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                input.add(sCurrentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();
                if (fr != null)
                    fr.close();
            } catch (IOException ex) {
            }
        }

        process(input);
        System.out.println(res);
    }
}