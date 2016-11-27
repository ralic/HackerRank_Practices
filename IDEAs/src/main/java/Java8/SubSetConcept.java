package Java8;/**
 * Created by raliclo on 8/14/16.
 * Project Name : TestNG-1
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SubSetConcept {

    public static void main(String[] args) {
        List<String> set = Arrays.asList("A", "B", "C", "D");
        SubsetIterator<String> it =
                new SubsetIterator<String>(set);
        while (it.hasNext()) {
            System.out.println(it.next());
        }


        int[] test = new int[5];
        for (int i = 0; i < test.length; i++) {
            test[i] = (int) Math.floor(Math.random() * 20);
        }
        List<List<Integer>> testlist = subsets(test);
        System.out.println(testlist);
    }

    public static List<List<Integer>> subsets(int[] S) {
        ArrayList<List<Integer>> res = new ArrayList<List<Integer>>();
        if (S.length == 0) {
            res.add(new ArrayList<Integer>());
            return res;
        }
        Arrays.sort(S);
        int head = S[0];
        int[] rest = new int[S.length - 1];
        System.arraycopy(S, 1, rest, 0, S.length - 1);
        for (List<Integer> list : subsets(rest)) {
            List<Integer> newList = new LinkedList<Integer>();
            newList.add(head);
            newList.addAll(list);
            res.add(list);
            res.add(newList);
        }
        return res;
    }

    //How do I generate all subsets of a set in Java iteratively?

    public static class SubsetIterator<E> {
        private final List<E> set;
        private final long max;
        private int index;

        public SubsetIterator(List<E> originalList) {
            set = originalList;
            System.out.println("Set Size:" + set.size());
            max = (1 << set.size());
            index = 0;
            System.out.println("Max:" + max);
        }

        public boolean hasNext() {
            System.out.println("index:" + index);
            return index < max;
        }

        public List<E> next() {
            List<E> newSet = new ArrayList<E>();
            int flag = 1;
            for (E element : set) {
                System.out.println("Element:" + element + ",index:" + index + ",flag:" + flag + ",index & flag:" + (index & flag));
                if ((index & flag) != 0) {
                    newSet.add(element);
                    System.out.println("Add Element:" + element);
                }
                flag <<= 1;
            }
            ++index;
            return newSet;
        }

    }
}