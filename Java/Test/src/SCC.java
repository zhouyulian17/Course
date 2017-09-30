
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.Stack;

// StronglyConnectedComponents
public class SCC {
 private Vertex[] vertices;
 private boolean[] explored;
 private int[] times;
 private int n;
 private class Vertex {
  private ArrayList<Integer> tail;
  private ArrayList<Integer> head;
  public Vertex() {
   tail = new ArrayList<Integer>();
   head = new ArrayList<Integer>();
  }
  public void addTail(int n) {
   tail.add(n);
  }
  public void addHead(int n) {
   head.add(n);
  }
 }
 public SCC(Scanner scanner, int v) {
  n = v;
  vertices = new Vertex[n + 1];
  times = new int[n + 1];
  explored = new boolean[n + 1];
  for (int i = n; i > 0; i--) {
   vertices[i] = new Vertex();
  }
  Scanner sc = null;
  while (scanner.hasNextLine()) {
   sc = new Scanner(scanner.nextLine());
   int t = sc.nextInt();
   int h = sc.nextInt();
   vertices[t].addHead(h);
   vertices[h].addTail(t);
  }
 }

 public void revDFS() {
  Stack<Integer> s = new Stack<Integer>();
  Stack<Integer> t = new Stack<Integer>();
  int m = n;
  int k;
  int j = 1;
  while (m > 0) {
   if (!s.isEmpty()) {
    k = s.pop();
    t.push(k);
    if (!vertices[k].tail.isEmpty()) {
     for (Integer i: vertices[k].tail) {
      if (!explored[i]) {
       explored[i] = true;
       s.push(i);
      }
     }
    }
   }
   else {
    if (!explored[m]) {
     s.push(m);
     explored[m] = true;
     while(!t.isEmpty()) {
      times[j++] = t.pop();
     }
    }
    m--;
   }
  }
  if (!s.isEmpty()) {
   times[j++] = s.pop();
  }
  else {
   while (!t.isEmpty()) {
    times[j++] = t.pop();
   }
  }
 }
 
 public ArrayList<Integer> DFS() {
  revDFS();
  explored = new boolean[n + 1];
  Stack<Integer> s = new Stack<Integer>();
  Stack<Integer> t = new Stack<Integer>();
  ArrayList<Integer> list = new ArrayList<Integer>();
  int m = n;
  int k;
  while (m > 0) {
   if (!s.isEmpty()) {
    k = s.pop();
    t.push(k);
    if (!vertices[k].head.isEmpty()) {
     for (Integer i: vertices[k].head) {
      if (!explored[i]) {
       explored[i] = true;
       s.push(i);
      }
     }
    }
   }
   else {
    if (!explored[times[m]]) {
     s.push(times[m]);
     explored[times[m]] = true;
     list.add(t.size());
     t = new Stack<Integer>();
    }
    m--;
   }
  }
  if (!s.isEmpty()) list.add(s.size());
  else list.add(t.size());
  return list;
 }
 /*
 public int revDFS(int k, int time) {

  if (!explored[k]) {
   explored[k] = true;
   if (!vertices[k].tail.isEmpty()) {
    for(Integer i: vertices[k].tail) {
     time = revDFS(i, time);
    }
   }
   times[--time] = k;
   //System.out.printf("%d, %d", k, time);
   //System.out.println();
  }
  return time; 
 }
 public int DFS(int k, int size) {
  if (!explored[k]) {
   size++;
   explored[k] = true;
   if (!vertices[k].head.isEmpty()) {
    for(Integer i: vertices[k].head) {
     size = DFS(i, size);
    }
   }
  }
  return size;
 }
 public ArrayList<Integer> compute() {
  DFS();
  
  ArrayList<Integer> list = new ArrayList<Integer>();
  explored = new boolean[n + 1];
  while(m++ < n) {
   if(!explored[times[m]]) {
    int size = DFS(times[m], 0);
    list.add(size);
    //System.out.printf("%d, %d, %d", m, times[m], size);
    //System.out.println();
   }
  }
  return list;
 }
 */
 public static void main(String[] args) {
  Scanner sc = null;
  try {
   sc = new Scanner(new File("/Users/yulianzhou/Desktop/course/Java/Algorithms Design and Analysis, Part 1/src/connectedcomponents/SCC.txt"));
  } catch (FileNotFoundException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }
  //SSC ssc = new SSC(sc, 8);
  SCC ssc = new SCC(sc, 875714);
  System.out.println("read completed");
  long time1 = System.currentTimeMillis();
  ArrayList<Integer> list = ssc.DFS();
  System.out.println("compute completed");
  Collections.sort(list);
	long time2 = System.currentTimeMillis();
	System.out.println(time2 - time1);
  for (Integer i : list) {
   //System.out.println(i);
  }
 }
}