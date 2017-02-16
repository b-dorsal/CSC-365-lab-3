package pageminroutegui;
//Brian Dorsey 2016

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class wikiPage implements java.io.Serializable {

    private static final long serialVersionUID = 5280; //for serialization

    //public double score = -1.0;
    public transient String url;    //url of page
    public String title;            //title of page
    public double tentative = 0;    //tentative score for dijkstra.
    
    public ArrayList<wikiPage> children = new ArrayList<>(); //pages this page links to.
    //public ArrayList<String> childrenTitles = new ArrayList<>();
    public List<Double> childScores = new ArrayList<>();    //scores of this page's links.
    public transient ArrayList<String> links = new ArrayList<>(); //links, raw loaded.

    public transient bHashMap wordMap = new bHashMap(8000); // map of words/frequencies.

    //Constructor, sets url, loads page data.
    public wikiPage(String u) throws IOException {
        this.url = u;
        this.loadPage();
    }

    //Loads data from webpage to map, gets title
    public void loadPage() {
        try {
            Document doc = Jsoup.connect(url).get();

            Elements title = doc.select("meta[property=og:title]");
            String t = doc.title();
            t = t.replaceAll(" - Wikipedia, the free encyclopedia", "");
            this.title = t;

            //int temp = 0;
            Elements paragraphs = doc.select("p");
            for (Element p : paragraphs) {
                String inputLine = p.text();
                String nextWord;
                while (inputLine.contains(" ")) {
                    nextWord = (inputLine.substring(0, inputLine.indexOf(' ')).trim()).toLowerCase();
                    inputLine = inputLine.substring(inputLine.indexOf(' ') + 1, inputLine.length()).trim();
                    nextWord = nextWord.trim();
                    nextWord = formatWord(nextWord);
                    if (!nextWord.equals("")) {
                        //System.out.print(nextWord);
                        this.wordMap.put(nextWord);
                        //temp++;
                    }
                }
            }
            //System.out.println(temp);
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex);
        }
    }

    //Function takes String, formats out extra chars. Returns new.
    private String formatWord(String w) { //Function to remove unwanted character from words.
        w = w.replace("(", "");
        w = w.replace(")", "");
        w = w.replace(".", "");
        w = w.replace(",", "");
        w = w.replace(":", "");
        w = w.replace("–", "");
        w = w.replace("/", "");
        w = w.replace(";", "");
        w = w.replace("\"", "");
        if (w.contains("[")) {
            w = w.substring(0, w.indexOf("["));
        }

        return w;
    }

    //Returns raw links this page has.
    public ArrayList<String> getLinks() {
        return this.links;
    }

    //Sets the child pages based on this pages links.
    public void setChildren(ArrayList<wikiPage> pages) {
        ArrayList<String> seen = new ArrayList<>();
        for (wikiPage p : pages) {
            if (!p.title.equals(this.title)) {
                if (!seen.contains(p.title)) {
                    seen.add(p.title);
                    this.children.add(p);
                }
            }
        }
    }

    //Prints this page's children, for debugging.
    public ArrayList<String> printChilds() {
        ArrayList<String> childs = new ArrayList<>();
        for (wikiPage p : children) {
            childs.add(p.title);
        }
        return childs;
    }

    //Loads the raw links this page has 
    public void loadLinks() throws IOException {
        Document doc = Jsoup.connect(this.url).get();
        Elements linksa = (Elements) doc.select("a[href]");

        int pos = 0;
        for (Element link : linksa) {
            String linkS = link.attr("abs:href");
            String raw = linkS.substring(linkS.indexOf("."), linkS.length());

            if (!linkS.contains(this.url)) {
                if (linkS.contains("en.wikipedia.org")) {
                    if (!raw.contains(":")) {
                        if (!linkS.contains("w/index.php")) {
                            if (!this.links.contains(linkS)) {
                                linkS = linkS.replace("https://en.wikipedia.org/wiki/", "");
                                linkS = linkS.replaceAll("_", " ");
                                if (linkS.contains("#")) {
                                    linkS = linkS.substring(0, linkS.indexOf("#"));
                                    //System.out.println(linkS);
                                }
                                this.links.add(linkS);
                                pos++;
                            }
                        }
                    }
                }
            }
        }
    }
}
