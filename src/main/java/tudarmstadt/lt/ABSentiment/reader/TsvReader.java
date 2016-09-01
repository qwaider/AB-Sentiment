package tudarmstadt.lt.ABSentiment.reader;

import tudarmstadt.lt.ABSentiment.type.Document;
import tudarmstadt.lt.ABSentiment.type.Sentence;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * TSV input reader for tab separated input files.<br>
 * The input format is: ID &emsp; text &emsp; optional label
 */
public class TsvReader implements InputReader {

    private BufferedReader reader = null;
    private boolean checkedNext = false;
    private boolean hasNext = false;

    private Document currentDoc;
    private String line;


    /**
     * Creates a Reader using a file name
     * @param filename the path and filename of the input file
     */
    public TsvReader(String filename) {
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.getClass().getResourceAsStream(filename), "UTF-8"));
        } catch(Exception e) {
            System.err.println("File could not be opened: " +filename );
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public Document next() {
        if (!checkedNext) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        checkedNext = false;
        currentDoc = buildDocument(line);
        return currentDoc;
    }

    @Override
    public boolean hasNext() {
        if (!checkedNext) {
            checkedNext = true;
            try {
                line = reader.readLine();
                hasNext = (line != null && !line.isEmpty());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hasNext;
    }

    /**
     * Creats a {@link Document} from an input line.
     * @param line the input line
     * @return a {@link Document} with text, identifier and label
     */
    private Document buildDocument(String line) {
        Document doc = new Document();

        String[] documentFields = line.split("\\t");
        if (documentFields.length < 2 || documentFields.length > 3) {
            throw new IllegalArgumentException("The document should at least have 2 fields, with an optional label in the 3rd field!0");
        }
        doc.setDocumentId(documentFields[0]);
        doc.addSentence(new Sentence(documentFields[1]));

        if (documentFields.length == 3) {
            doc.setLabels(documentFields[2]);
        }
        return doc;


    }

    @Override
    public Iterator<Document> iterator() {
        return this;
    }
}
