/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package LingPipe;

/**
 *
 * @author Karim
 */

import com.aliasi.lm.TokenizedLM;
import com.aliasi.tokenizer.RegExTokenizerFactory;
import com.aliasi.tokenizer.Tokenization;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.*;

import java.util.*;
import java.io.*;

public class Util {
    
    public static void main(String[] args) throws IOException{
        String file = "Directory\\Transport\\TPT_1.txt";
//        File f = new File(file);
//        String [] s = f.list();
//        for(int i=0;i<s.length;i++){
//             System.out.println(s[i]);
//        }
       
        String text = Files.readFromFile(new File(file),"UTF-8");
        
        TokenizerFactory tokFact = new RegExTokenizerFactory("[\\p{L}\\p{N}_]+");
//        
        Tokenization tokenization = new Tokenization(text,tokFact);
TokenizedLM t = new TokenizedLM(tokFact, 1);
SortedSet s = t.frequentTermSet(1, 10);
        List<String> tokenList = tokenization.tokenList();
//        List<String> whitespaceList = tokenization.whitespaceList();
        String textTok = tokenization.text();
        /*x*/
        
        System.out.println("tokenList=" + tokenList);
//        System.out.println("whitespaceList=" + whitespaceList);
        System.out.println("textTok=|" + textTok + "|");
        
//        System.out.println();
//        DisplayTokens.displayTextPositions(text);
    }
}
