package pageminroutegui;
//Brian Dorsey 2016

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import pageminroutegui.bHashMap.bucket;

public class wikiGraph implements java.io.Serializable {

    //Directory of serialized graph file.
    private final String graphFileDir = "/Users/admin/NetBeansProjects/pageMinRouteGUI/Resources/graph.ser";
    private static final long serialVersionUID = 5280; //for serialization

    public ArrayList<wikiPage> wikiPages = new ArrayList<>();//list of wikiPages in this graph
    private final int maxPages = 500;//maximum number of pages allowed.

    //wikiGraph constructor, loads each title from the file and builds new wikiPage
    public wikiGraph(String titlesdir) {
        String fileName = titlesdir;
        String line = null;
        int pos = 0;
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            wikiPage t = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.print(pos + ": ");
                t = new wikiPage("https://en.wikipedia.org/wiki/" + line);
                //System.out.println();
                t.loadLinks();
                System.out.println(t.title + "\t\t\t\t\t\t\t\tlinks: " + t.links.size());
                wikiPages.add(t);
                pos++;

                if (pos == this.maxPages) {
                    break;
                }
            }
            bufferedReader.close();
        } catch (Exception ex) {
        }

        this.drawEdges();
    }

    //Returns the number of pages in this wikiGraph.
    public int size() {
        return this.wikiPages.size();
    }

    //Method to connect pages that link to eachother.
    private void drawEdges() {
        for (wikiPage p : this.wikiPages) {
            ArrayList<wikiPage> childs = new ArrayList<>();
            ArrayList<String> visited = new ArrayList<>();
            for (String s : p.links) {
                if (!visited.contains(s)) {
                    wikiPage t = findWikiPageByTitle(s);
                    if (t != null) {
                        //System.out.println("drew edge: " + p.title + " to " + s);
                        p.childScores.add(compareSource(p, t));
                        childs.add(findWikiPageByTitle(s));
                        visited.add(s);
                    }
                }
            }
            if (p.children.isEmpty()) {
                childs.remove(p);
            }
            p.setChildren(childs);

        }
    }

    //Function returns the wikiPage with this title, or null if none found.
    public wikiPage findWikiPageByTitle(String title) {
        for (wikiPage p : wikiPages) {
            if (p.title.equals(title)) {
                return p;
            }
        }
        return null;
    }

    //Prints each wikiPage info, for debugging.
    public void printAllPages() {
        for (wikiPage p : this.wikiPages) {
            System.out.println(p.title + "\t\t#child: " + p.children.size());
            System.out.print("\t");
            for (wikiPage s : p.children) {
                System.out.print(s.title + "(" + p.childScores.get(p.children.indexOf(s)) + "), ");
            }
            System.out.println();
        }
    }

    //Function takes 2 wikiPages and compares their data using cosine similarity, returns double score.
    public double compareSource(wikiPage pageA, wikiPage pageB) { // Compares this objects source to the URL provided.
        int[] sourceA = new int[8000];
        int[] sourceB = new int[8000];

        int pos = 0;
        bucket[] temp = pageA.wordMap.getList();
        for (int x = 0; x < temp.length; x++) {
            if (temp[x] == null) {
                //out.println("ISSUE");
            } else {
                if (pageB.wordMap.containsKey(temp[x].key)) {
                    sourceB[pos] = pageB.wordMap.get(temp[x].key);
                } else {
                    sourceB[pos] = 0;
                }
                sourceA[pos] = temp[x].val;
                pos++;
            }
        }

        double dot = 0.0;
        double denom1 = 0.0;
        double denom2 = 0.0;

        for (int c = 0; c < sourceA.length; c++) {
            dot += sourceA[c] * sourceB[c];
            denom1 += sourceA[c] * sourceA[c];
            denom2 += sourceB[c] * sourceB[c];
        }

        return dot / (Math.sqrt(denom1) * Math.sqrt(denom2));
    }

    //PriorityQueue<wikiPage> queue = new PriorityQueue<wikiPage>();
    ArrayList<wikiPage> Svisited = new ArrayList<>();//pages visited by spanning tree operation.
    ArrayList<wikiPage> Squeue = new ArrayList<>();//next pages for spanning tree operation.

    //Finds a spanning tree starting from this wikiPage.
    public boolean findSpanningTree(wikiPage startPage) {
        if (!startPage.children.isEmpty()) {
            Svisited.add(startPage);
            findSpanning(startPage);
            Squeue.clear();
            Svisited.clear();
            return true;
        } else {
            return false;
            //System.out.println("Bad Start Point: " + startPage.title);
        }
    }

    private void findSpanning(wikiPage page) {
        for (wikiPage c : page.children) {
            if (!Svisited.contains(c)) {
                if (!page.title.equals(c.title)) {
                    //System.out.println(page.title + " -> " + c.title);
                    Squeue.add(c);
                    //queue.add(c);
                    Svisited.add(c);
                }
            }
        }
        if (!Squeue.isEmpty()) {
            wikiPage next = Squeue.get(0);
            Squeue.remove(0);
            //wikiPage next = queue.peek();
            findSpanning(next);
        } else {
            //System.out.println("Done.");
        }

    }

    ArrayList<wikiPage> Dvisited = new ArrayList<>();   //pages visited by the dijkstra operation.
    ArrayList<wikiPage> Dqueue = new ArrayList<>();     //next pages for the dijkstra operation.
    ArrayList<String> path = new ArrayList<>();         //shortest path found by dijkstra.

    //Takes a start, current, and end page and finds shortest path between start and end, dijkstra's algorithm.
    public void dijkstraMinPath(wikiPage start, wikiPage page, wikiPage B) {

        for (wikiPage c : page.children) {
            if ((page.tentative + page.childScores.get(page.children.indexOf(c))) > c.tentative) {
                c.tentative = (page.tentative + page.childScores.get(page.children.indexOf(c)));
            }

        }

        Dvisited.add(page);
        if (Dvisited.contains(B)) {
            Dvisited.clear();
            Dqueue.clear();
            //System.out.println(path.toString());
            //return path;
            return;
        }

        ArrayList<wikiPage> seen = new ArrayList<>();

        while (seen.size() < page.children.size()) {
            wikiPage shortest = null;
            double smallest = -2;
            for (wikiPage c : page.children) {
                //System.out.println(c.title + c.tentative);
                if (!seen.contains(c)) {
                    if (!Dvisited.contains(c)) {
                        if (c.tentative > smallest) {
                            smallest = c.tentative;
                            shortest = c;
                        }
                    }
                }
            }

            seen.add(shortest);
            //System.out.print(shortest.title + smallest + ", ");
            Dqueue.add(shortest);
        }

        wikiPage next = Dqueue.get(0);
        if (!Dvisited.contains(next)) {
            //System.out.println(next.title);
            //path.append(next.title);
            path.add(next.title);

        }

        Dqueue.remove(0);
        dijkstraMinPath(start, next, B);
        //return null;
    }

    //Saves the current wikiGraph to a serialized file, in location specified.
    public void save() throws IOException {
        try {
            FileOutputStream fileOut = new FileOutputStream(graphFileDir);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            System.out.println("ERROR: file NOT saved!");
        }
    }
}//end class
