
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * @Question :
 * Given a string s and a dictionary of words dict, determine if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * For example, given
 * s = "leetcode",
 * dict = ["leet", "code"].
 *
 * Return true because "leetcode" can be segmented as "leet code".
 *
 * Subscribe to see which companies asked this question
 * @Status :
 */
package leet.pkg139.wordbreak;

import java.io.*;
import java.net.*;
import java.security.*;
import java.math.*;
import java.text.*;
import java.util.*;
import java.lang.*;
import java.time.*;
import java.applet.*;
import java.nio.*;
import java.beans.*;
import java.rmi.*;
import java.util.logging.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.function.*;
import java.nio.file.*;
import java.nio.charset.*;
import java.nio.file.spi.*;
import java.nio.file.attribute.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
//import import java.awt.*; // Disabled, because of List Class conflict
//import java.sql.*; // Disbled, because of Connection Class conflict

/**
 *
 * @author raliclo
 * @Java : java version "1.8.0_65"
 */
public class Leet139WordBreak {

    private static final Logger LOG = Logger.getLogger(Leet139WordBreak.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String s1 = "leetcode";
        Set<String> ss1 = new HashSet<String>();
        ss1.add("leet");
        ss1.add("code");
        String s2 = "abcd";
        Set<String> ss2 = new HashSet<String>();
        ss2.add("a");
        ss2.add("abc");
        ss2.add("b");
        ss2.add("cd");
        String s3 = "aaaaaaa";
        Set<String> ss3 = new HashSet<String>();
        ss3.add("aaaa");
        ss3.add("aa");
        String s4 = "fohhemkkaecojceoaejkkoedkofhmohkcjmkggcmnami";
        Set<String> ss4 = new HashSet<String>();
        String[] k = {"kfomka", "hecagbngambii", "anobmnikj", "c", "nnkmfelneemfgcl", "ah", "bgomgohl", "lcbjbg", "ebjfoiddndih", "hjknoamjbfhckb", "eioldlijmmla", "nbekmcnakif", "fgahmihodolmhbi", "gnjfe", "hk", "b", "jbfgm", "ecojceoaejkkoed", "cemodhmbcmgl", "j", "gdcnjj", "kolaijoicbc", "liibjjcini", "lmbenj", "eklingemgdjncaa", "m", "hkh", "fblb", "fk", "nnfkfanaga", "eldjml", "iejn", "gbmjfdooeeko", "jafogijka", "ngnfggojmhclkjd", "bfagnfclg", "imkeobcdidiifbm", "ogeo", "gicjog", "cjnibenelm", "ogoloc", "edciifkaff", "kbeeg", "nebn", "jdd", "aeojhclmdn", "dilbhl", "dkk", "bgmck", "ohgkefkadonafg", "labem", "fheoglj", "gkcanacfjfhogjc", "eglkcddd", "lelelihakeh", "hhjijfiodfi", "enehbibnhfjd", "gkm", "ggj", "ag", "hhhjogk", "lllicdhihn", "goakjjnk", "lhbn", "fhheedadamlnedh", "bin", "cl", "ggjljjjf", "fdcdaobhlhgj", "nijlf", "i", "gaemagobjfc", "dg", "g", "jhlelodgeekj", "hcimohlni", "fdoiohikhacgb", "k", "doiaigclm", "bdfaoncbhfkdbjd", "f", "jaikbciac", "cjgadmfoodmba", "molokllh", "gfkngeebnggo", "lahd", "n", "ehfngoc", "lejfcee", "kofhmoh", "cgda", "de", "kljnicikjeh", "edomdbibhif", "jehdkgmmofihdi", "hifcjkloebel", "gcghgbemjege", "kobhhefbbb", "aaikgaolhllhlm", "akg", "kmmikgkhnn", "dnamfhaf", "mjhj", "ifadcgmgjaa", "acnjehgkflgkd", "bjj", "maihjn", "ojakklhl", "ign", "jhd", "kndkhbebgh", "amljjfeahcdlfdg", "fnboolobch", "gcclgcoaojc", "kfokbbkllmcd", "fec", "dljma", "noa", "cfjie", "fohhemkka", "bfaldajf", "nbk", "kmbnjoalnhki", "ccieabbnlhbjmj", "nmacelialookal", "hdlefnbmgklo", "bfbblofk", "doohocnadd", "klmed", "e", "hkkcmbljlojkghm", "jjiadlgf", "ogadjhambjikce", "bglghjndlk", "gackokkbhj", "oofohdogb", "leiolllnjj", "edekdnibja", "gjhglilocif", "ccfnfjalchc", "gl", "ihee", "cfgccdmecem", "mdmcdgjelhgk", "laboglchdhbk", "ajmiim", "cebhalkngloae", "hgohednmkahdi", "ddiecjnkmgbbei", "ajaengmcdlbk", "kgg", "ndchkjdn", "heklaamafiomea", "ehg", "imelcifnhkae", "hcgadilb", "elndjcodnhcc", "nkjd", "gjnfkogkjeobo", "eolega", "lm", "jddfkfbbbhia", "cddmfeckheeo", "bfnmaalmjdb", "fbcg", "ko", "mojfj", "kk", "bbljjnnikdhg", "l", "calbc", "mkekn", "ejlhdk", "hkebdiebecf", "emhelbbda", "mlba", "ckjmih", "odfacclfl", "lgfjjbgookmnoe", "begnkogf", "gakojeblk", "bfflcmdko", "cfdclljcg", "ho", "fo", "acmi", "oemknmffgcio", "mlkhk", "kfhkndmdojhidg", "ckfcibmnikn", "dgoecamdliaeeoa", "ocealkbbec", "kbmmihb", "ncikad", "hi", "nccjbnldneijc", "hgiccigeehmdl", "dlfmjhmioa", "kmff", "gfhkd", "okiamg", "ekdbamm", "fc", "neg", "cfmo", "ccgahikbbl", "khhoc", "elbg", "cbghbacjbfm", "jkagbmfgemjfg", "ijceidhhajmja", "imibemhdg", "ja", "idkfd", "ndogdkjjkf", "fhic", "ooajkki", "fdnjhh", "ba", "jdlnidngkfffbmi", "jddjfnnjoidcnm", "kghljjikbacd", "idllbbn", "d", "mgkajbnjedeiee", "fbllleanknmoomb", "lom", "kofjmmjm", "mcdlbglonin", "gcnboanh", "fggii", "fdkbmic", "bbiln", "cdjcjhonjgiagkb", "kooenbeoongcle", "cecnlfbaanckdkj", "fejlmog", "fanekdneoaammb", "maojbcegdamn", "bcmanmjdeabdo", "amloj", "adgoej", "jh", "fhf", "cogdljlgek", "o", "joeiajlioggj", "oncal", "lbgg", "elainnbffk", "hbdi", "femcanllndoh", "ke", "hmib", "nagfahhljh", "ibifdlfeechcbal", "knec", "oegfcghlgalcnno", "abiefmjldmln", "mlfglgni", "jkofhjeb", "ifjbneblfldjel", "nahhcimkjhjgb", "cdgkbn", "nnklfbeecgedie", "gmllmjbodhgllc", "hogollongjo", "fmoinacebll", "fkngbganmh", "jgdblmhlmfij", "fkkdjknahamcfb", "aieakdokibj", "hddlcdiailhd", "iajhmg", "jenocgo", "embdib", "dghbmljjogka", "bahcggjgmlf", "fb", "jldkcfom", "mfi", "kdkke", "odhbl", "jin", "kcjmkggcmnami", "kofig", "bid", "ohnohi", "fcbojdgoaoa", "dj", "ifkbmbod", "dhdedohlghk", "nmkeakohicfdjf", "ahbifnnoaldgbj", "egldeibiinoac", "iehfhjjjmil", "bmeimi", "ombngooicknel", "lfdkngobmik", "ifjcjkfnmgjcnmi", "fmf", "aoeaa", "an", "ffgddcjblehhggo", "hijfdcchdilcl", "hacbaamkhblnkk", "najefebghcbkjfl", "hcnnlogjfmmjcma", "njgcogemlnohl", "ihejh", "ej", "ofn", "ggcklj", "omah", "hg", "obk", "giig", "cklna", "lihaiollfnem", "ionlnlhjckf", "cfdlijnmgjoebl", "dloehimen", "acggkacahfhkdne", "iecd", "gn", "odgbnalk", "ahfhcd", "dghlag", "bchfe", "dldblmnbifnmlo", "cffhbijal", "dbddifnojfibha", "mhh", "cjjol", "fed", "bhcnf", "ciiibbedklnnk", "ikniooicmm", "ejf", "ammeennkcdgbjco", "jmhmd", "cek", "bjbhcmda", "kfjmhbf", "chjmmnea", "ifccifn", "naedmco", "iohchafbega", "kjejfhbco", "anlhhhhg"};
        Stream.of(k).forEach(e -> ss4.add(e));
//        Stream.of(k).forEach(e -> System.out.println(e));
        try {
            /**
             * @param speedX to monitor program runtime
             */
            // TODO code application logic here
            runexec("echo This program is the solution of leetcode # 139");
            long speedX = System.currentTimeMillis();
//            System.out.println(wordBreak(s1, ss1));
//            System.out.println(wordBreak(s2, ss2));
//            System.out.println(wordBreak(s3, ss3));
//            System.out.println("Calls1=" + calls1);
//            System.out.println("Calls2=" + calls2);
//            calls1 = 0;
//            calls2 = 0;
            System.out.println(wordBreak(s4, ss4));
            System.out.println(wordBreak_uber(s4, ss4));
//            System.out.println("Calls1=" + calls1);
//            System.out.println("Calls2=" + calls2);
            System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");
        } catch (IOException ex) {
            Logger.getLogger(Leet139WordBreak.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static int calls1 = 0;
    public static int calls2 = 0;
    public static HashMap<String, Boolean> checked = new HashMap();

    public static boolean wordBreak(String s, Set<String> wordDict) {
//        System.out.println(s);
        if (checked.containsKey(s)) {
            return checked.get(s);
        }
        if (wordDict.size() > 1) {
            if (s.length() >= 0) {
                for (int i = 1; i < s.length(); i++) {
//                    System.out.println(s.subSequence(0, i));
//                    System.out.println(s.subSequence(i, s.length()));
                    if (wordDict.contains(s.subSequence(0, i))
                            && wordBreak((String) s.subSequence(i, s.length()), wordDict)) {
                        calls1++;
                        checked.putIfAbsent(s, Boolean.TRUE);
                        return true;
                    }
                }
            }
        }
        if (wordDict.size() == 1) {
            if (wordDict.contains(s)) {
                return true;
            }
        }
        if (!wordDict.contains(s)) {
            return false;
        }
        return true;
    }

    // Uber fast wordbreak
    public static boolean wordBreak_uber(String s, Set<String> wordDict) {
        boolean[] possible = new boolean[s.length()];
        if (wordDict.contains(s.substring(0, 1)) == true) {
            possible[0] = true;
        }
        for (int i = 1; i <= s.length() - 1; i++) {
            for (int j = i; j >= 0; j--) {
                if (j > 0 && possible[j - 1] == true && wordDict.contains(s.substring(j, i + 1)) == true) {
                    possible[i] = true;
                    break;
                } else if (j == 0 && wordDict.contains(s.substring(0, i + 1)) == true) {
                    possible[i] = true;
                    break;
                }
            }
        }
        return possible[s.length() - 1] == true;
    }

    /**
     * @param x for command line
     * @throws java.io.IOException
     */
    public static Object runexec(String x) throws IOException {
        Process p = Runtime.getRuntime().exec(x, null, null);
        Object ans = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        try {
            while ((line = input.readLine()) != null) {
                System.out.println(line);
                //   For Octave integration only 
                //   if (line.startsWith("ans =")) {
                //    System.out.println(line.split("=")[1].trim());
                //     ans = line.split("=")[1].trim();
                // }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();

        } catch (InterruptedException ex) {
            Logger.getLogger(Leet139WordBreak.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
}
