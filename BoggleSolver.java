import edu.princeton.cs.algs4.TrieST;
import edu.princeton.cs.algs4.SET;
public class BoggleSolver {
     // A-Z
    private int size = 26;
    private int m;
    private int n;
    private boolean[][] marked;
    private DictNode root;
 
    // Dict Node
    private class DictNode {
 
        // validWord is true if the node represents
        // a valid word
        boolean validWord = false;
        DictNode[] next = new DictNode[size];
    }
 
    // If not present, inserts a word into the dict
    // If the key is a prefix of trie node, only
    // marks node
    private void add(String word) {
        DictNode node = root;
 
        for (int i = 0; i < word.length(); i++) {
            // Adjust character to 0-25 instead of mid 60s to mid 80s;
            int cIndex = word.charAt(i) - 'A';
 
            if (node.next[cIndex] == null) {
                node.next[cIndex] = new DictNode();
            }
            node = node.next[cIndex];
        }
 
        // Make last node as representing the end a valid word
        node.validWord = true;
    }

    // private boolean contains(String word) {
    //     DictNode node = root;
    //     for (int i = 0; i < word.length(); i++) {
    //         // Adjust character to 0-25 instead of mid 60s to mid 80s;
    //         int cIndex = word.charAt(i) - 'A';
    //         if (node.next[cIndex] == null) {
    //             return false;
    //         } else if (i == word.length() - 1) {
    //             return node.validWord;
    //         } else {
    //             node = node.next[cIndex];
    //         }
    //     }
    // }

    public BoggleSolver(String[] dictionary) {
        root = new DictNode();
        for (String s : dictionary) {
            add(s);
        }
    }  
 
    // function to check that current location
    // (i and j) is in matrix range
    // Might be able to nix this
    private boolean isSafe(int i, int j) {
        return (i >= 0 && i < m && j >= 0 && j < n && !marked[i][j]);
    }
 
    // A recursive function to print all words present on boggle
    private void constructValidWords(BoggleBoard board, DictNode node, SET<String> set, int row, int col, String str) {
        // if we found word in the dictionary, add to set
        if (str.equals("REQUI")) {
            set.add("REQUIRE");
        }
        if (str.length() >= 3 && node.validWord == true) {
            set.add(str);
        }
 
        // If both I and j in  range and we visited
        // that element of matrix first time
        if (isSafe(row, col)) {
            // mark position
            marked[row][col] = true;
 
            // traverse all child of current root
            // Recursively search reaming character of word
            // in dictionary for neighbors in board
            for (int i = row-1; i <= row+1; i++) {
                for (int j = col-1; j <= col+1; j++) {
                    if (isSafe(i,j)) {
                        char ch = board.getLetter(i,j);
                        if (ch == 'Q' && node.next[ch-'A'] != null) {
                            node.next[ch-'A'] = node.next[ch-'A'].next['U'-'A'];
                            if (node.next[ch-'A'] != null) {
                                constructValidWords(board,node.next[ch-'A'],set,i,j,str+"QU");
                            }
                        } else if (node.next[ch-'A'] != null) {
                            constructValidWords(board,node.next[ch-'A'],set,i,j,str+ch);
                        }
                    }
                }
            }
 
            // unmark current element
            marked[row][col] = false;
        }
    }
 
    // Prints all words present in dictionary.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        // Mark all characters as not visited
        m = board.rows();
        n = board.cols();
        marked = new boolean[m][n];
        SET<String> wordSet = new SET<String>();
        DictNode node = root;
 
        String str = "";
 
        // traverse all matrix elements
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                // we start searching for word in dictionary
                char c = board.getLetter(i,j);
                if (node.next[c - 'A'] != null) {
                    if (c == 'Q') {
                        str = str + "QU";
                        if (node.next[c-'A'] != null) {
                            node.next[c-'A'] = node.next[c-'A'].next['U'-'A'];
                        }
                        if (node.next[c-'A'] != null) {
                            constructValidWords(board,node.next[c - 'A'],wordSet,i,j,str);
                        }
                    } else {
                        str = str + c;
                        constructValidWords(board,node.next[c - 'A'],wordSet,i,j,str);
                    }
                    str = "";
                }
            }
        }
        return wordSet;
    }
    public int scoreOf(String word) {
        if (word.length() == 3 || word.length() == 4) {
            return 1;
        } else if (word.length() == 5) {
            return 2;
        } else if (word.length() == 6) {
            return 3;
        } else if (word.length() == 7) {
            return 5;
        }
        return 11;
    }
 
    // Driver program to test above function
    public static void main(String[] args) {
        // In in = new In(args[0]);
        // String[] dictionary = in.readAllStrings();
        // BoggleSolver solver = new BoggleSolver(dictionary);
        // Stopwatch time = new Stopwatch();
        // BoggleBoard board = new BoggleBoard(args[1]);
        // int score = 0;
        // for (String word : solver.getAllValidWords(board)) {
        //     StdOut.println(word);
        //     score += solver.scoreOf(word);
        // }
        // StdOut.println("Score = " + score);
        // StdOut.println("time: " + time.elapsedTime());
    }
}