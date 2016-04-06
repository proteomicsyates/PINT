package edu.scripps.yates.dbindex.io;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FastaReader {

    public static final char FIRSTCHAROFDEFLINE = '>';
    public static final int DEFAULTSEQENCELENGTH = 1024;

    // Becareful, might need lots of memory
    public static List<Fasta> getFastaList(InputStream is) throws IOException {
        List fastaList = new LinkedList();
        for (Iterator<Fasta> fastas = getFastas(is); fastas.hasNext();) {
            fastaList.add(fastas.next());
        }
        return fastaList;
    }

    public static Iterator<Fasta> getFastas(String fastaFileName) throws IOException {
        FileInputStream fis = new FileInputStream(fastaFileName);
        System.out.println("==========Iterator=====" + fastaFileName);
        return getFastas(fis);
    }

    /**
     * Parse fasta file to get total number of fasta entries
     *
     * @param fastaFileName
     * @return total num of fasta entries
     * @throws IOException
     */
    public static int getNumberFastas(String fastaFileName) throws IOException {
        FileInputStream fis = new FileInputStream(fastaFileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));
        String line;
        int total = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (! line.isEmpty() && line.charAt(0) == FIRSTCHAROFDEFLINE) {
                ++total;
            }
        }
        br.close();
        return total;
    }

    public static Iterator<Fasta> getFastas(final InputStream is) throws IOException {
        return new Iterator() {
            private String lastLine = ""; // remember the last line read
            private BufferedReader br;

            {
                br = new BufferedReader(new InputStreamReader(is));
                // remove the potential empty lines and get the first defline
                while ((lastLine = br.readLine()) != null && lastLine.equals(""));

                if (lastLine.charAt(0) != FIRSTCHAROFDEFLINE) {
                    throw new IOException();
                }
            }

            public boolean hasNext() {
                return lastLine != null;
            }

            public Fasta next() {

                Fasta fasta = null;
                try {
                    fasta = getFasta();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return fasta;
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported");
            }

            private Fasta getFasta() throws IOException {

                final StringBuilder sb = new StringBuilder(DEFAULTSEQENCELENGTH);
                String defline = lastLine;

                // if the line read is a empty string, ignore it
                while ((lastLine = br.readLine()) != null && (lastLine.equals("")
                        || lastLine.charAt(0) != FIRSTCHAROFDEFLINE)) {
                    //System.out.println(lastLine);
                    if (!lastLine.equals("")) {
                        String line = lastLine.trim();
                        sb.append(line);
                    }
                }

                // the lastLine should be the defline
                // and sb.toString should be the sequence
                return new Fasta(defline, sb.toString());
            }

            @Override
            protected void finalize() throws IOException {
                try {
                    super.finalize();
                } catch (Throwable ex) {
                    Logger.getLogger(FastaReader.class.getName()).log(Level.SEVERE, null, ex);
                }

                br.close();
                //System.out.println("Finalized");
            }
        };
    }

    public static void main(String args[]) throws IOException {
        /*
         for (Iterator<Fasta> itr = FastaReader.getFastas(new FileInputStream(args[0])); itr.hasNext(); ) { 
         Fasta fasta = itr.next();
         String defline = fasta.getDefline();
         if(defline.contains("Escherichia coli")) {
         System.out.println(">" + defline);
         System.out.println(fasta.getSequence());
         }
         }
         */

        FastaReader.getFastas(System.in);

        for (Iterator itr = FastaReader.getFastas(System.in); itr.hasNext();) {
            Fasta fasta = (Fasta) itr.next();

            String defLine = fasta.getDefline();
            String seq = fasta.getSequence();
System.out.println(seq);
	}

if(true) return;

        int numEntries = 0;
        HashSet<String> accessions = new HashSet<String>(1000000);
        HashSet<String> sequestLikeAccs = new HashSet<String>(1000000);
        for (Iterator itr = FastaReader.getFastas(new FileInputStream(args[0])); itr.hasNext();) {
            Fasta fasta = (Fasta) itr.next();
//System.out.println(fasta.getSequestLikeAccession());
            numEntries++;

            String defLine = fasta.getDefline();
            String seq = fasta.getSequence();

            accessions.add(fasta.getSequestLikeAccession());
            String sequestlikeac = fasta.getSequestLikeAccession();
            if (sequestlikeac.length() > 40) {
                sequestlikeac = fasta.getSequestLikeAccession().substring(0, 41);
            }
            sequestLikeAccs.add(sequestlikeac);
        }

        System.out.println("In fasta file " + args[0] + ":");
        System.out.println("Number of protein entries: " + numEntries);
        System.out.println("Number of unique accessions: " + accessions.size());
        System.out.println("Number of unique SEQUEST like accessions: " + sequestLikeAccs.size());


        /*
         for (Iterator itr = FastaReader.getFastas(new FileInputStream(args[0])); itr.hasNext(); ) {
         Fasta fasta = (Fasta) itr.next();
         String defLine = fasta.getDefline();
         String seq = fasta.getSequence();
         if(defLine.startsWith("Rever")) {
         //System.out.println("Reversed: " + defLine);
         if(seq.endsWith("M")) {
         seq = seq.substring(0, seq.length() -1);
         } 
         } else {
         if(seq.startsWith("M")) {
         seq = seq.substring(1, seq.length());

         }
         //System.out.println("Regular: " + defLine);
         }
            
         System.out.println(">" + defLine);
         System.out.println(seq);
         }
         */

    }
}
