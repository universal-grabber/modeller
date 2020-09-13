package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CompressorTest {

    public static final String HTML_END = "</html>";
    StringBuilder htmlCache = new StringBuilder();

    public Set<String> tags = new HashSet<>();
    public Set<String> classes = new HashSet<>();
    public Set<String> attributes = new HashSet<>();
    public Set<String> attributesValues = new HashSet<>();

    public Map<String, Integer> table = new HashMap<>();
    public Huffman huffman = new Huffman();
    public StringBuilder data = new StringBuilder();
    public int index = 0;

    public static void main(String[] args) throws Exception {
        CompressorTest compressorTest = new CompressorTest();
        compressorTest.run();
    }

    int pl = 0;

    private void run() throws Exception {
        readFile();

//        huffman.store(table);

        try (FileOutputStream fos = new FileOutputStream("/home/taleh/b");
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            table.keySet().forEach(item -> {
                try {
                    bw.append(item);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        try (FileOutputStream fos = new FileOutputStream("/home/taleh/c");
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {

            bw.append(data.toString());
        }
    }

    private void readFile() throws IOException {
        try {
            try (FileInputStream fis = new FileInputStream("/home/taleh/a");
                 BufferedReader br = new BufferedReader(new InputStreamReader(fis))) {
                br.lines().forEach(line -> {
                    if (pl > 100000) {
                        throw new RuntimeException("asd");
                    }
                    if (line.contains(HTML_END)) {
                        pl++;
                        String leftPart = line.substring(0, line.indexOf(HTML_END) + HTML_END.length());
                        String rightPart = line.substring(line.indexOf(HTML_END) + HTML_END.length());
                        htmlCache.append(leftPart);
                        process(htmlCache.toString());
                        htmlCache = new StringBuilder();
                        htmlCache.append(rightPart);
                    } else {
                        htmlCache.append(line);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    long l = 0;
    long n = 0;

    private void process(String html) {
        Document document = Jsoup.parse(html);

        process(document);
        l += html.length();
        n++;

        System.out.println("Html size: " + html.length());
        System.out.println("Total html count: " + n);
        System.out.println("Total html size: " + l);
        System.out.println("Total strings count: " + table.size());
//        System.out.println("Total strings size: " + getTotalStringSize());
    }

    private Long getTotalStringSize() {
        long size = 0;

        for (String s : table.keySet()) {
            size += s.length();
        }

        return size;
    }

    private void process(Element element) {
        data.append(0);
        addString(element.tagName());
        tags.add(element.tagName());
        data.append(index);
        for (String s : element.classNames()) {
            addString(s);
            data.append(1);
            data.append(index);
            classes.add(s);
        }
        element.attributes().dataset().forEach((key, value) -> {
            addString(key);
            data.append(2);
            data.append(index);
            attributes.add(key);
            if (value != null) {
                addString(value);
                data.append(3);
                data.append(index);
                attributesValues.add(value);
            }
        });

        for (Node node : element.childNodes()) {
            if (node instanceof TextNode) {
                addString(((TextNode) node).text());
                data.append(4);
                data.append(index);
            } else if (node instanceof Element) {
                process((Element) node);
            } else {
                addString(node.toString());
                data.append(5);
                data.append(index);
            }
        }
        data.append(6);
    }

    private void addString(String string) {
//        string = string.trim();
        table.putIfAbsent(string, 0);
        table.put(string, table.get(string) + 1);
        index = table.size();
    }


}
