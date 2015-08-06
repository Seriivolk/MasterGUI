
package mstergui;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import jdk.nashorn.internal.runtime.Version;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.ui.RefineryUtilities;

public class AnalizeWIndow extends JFrame {
    JTextArea area;
    JTextArea area2;
    
    
    
    String textFromTextArea;
         
    AnalizeWIndow (){
                
        setSize(600, 400);
        setLocationRelativeTo(null);
        setTitle("Text input");
                
        JToolBar windowToolbar = new JToolBar();
        windowToolbar.setFloatable(false);
        
        Icon fileLoad =  new ImageIcon(getClass().getResource("resources/Load.png"));
        JButton openFileButton = new JButton("Load file",fileLoad);
        windowToolbar.add(openFileButton);         
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                fileChooser.setFileFilter(filter);
                int ret = fileChooser.showDialog(null, "Choose file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                
                InputStream in;
                try {
                    in = new BufferedInputStream(new FileInputStream(file));
                    String content = new Scanner(in,"CP1251").useDelimiter("\\A").next();
                    area.setText(content);
                    //System.out.println(content);
                }
                    catch (FileNotFoundException ex) {
                        Logger.getLogger(AnalizeWIndow.class.getName()).log(Level.SEVERE, null, ex);
                    }                   
                }
            }          
        });                
         
        
        Icon analyze =  new ImageIcon(getClass().getResource("resources/analyze.png"));
        JButton analyzeButton = new JButton("Analize",analyze);
        windowToolbar.add(analyzeButton);
        analyzeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               textFromTextArea = area.getText().replaceAll("”", "").replaceAll("„", ""); 
               if (textFromTextArea.length()>30){
                   
                    try {
                        List<String> myPoliticWords = fromDBToList(1);
                        List<String> myEconomicWords = fromDBToList(2);
                        List<String> mySportWords = fromDBToList(3);
                        List<String> myStopWords = fromDBToList(4);
                        List<String> myArticleWords = articleToList(textFromTextArea);
                        myArticleWords.removeAll(myStopWords);

                        List<String> myArticlePoliticWords = new ArrayList<String>(myArticleWords);
                        myArticlePoliticWords.retainAll(myPoliticWords);
                        int a = myArticlePoliticWords.size();
                        //System.out.println(myArticlePoliticWords);

                        List<String> myArticleEconomicWords = new ArrayList<String>(myArticleWords);
                        myArticleEconomicWords.retainAll(myEconomicWords);
                        int b = myArticleEconomicWords.size();
                        //System.out.println(myArticleEconomicWords);

                        List<String> myArticleSportWords = new ArrayList<String>(myArticleWords);
                        myArticleSportWords.retainAll(mySportWords);
                        int c = myArticleSportWords.size();
                        //System.out.println(myArticleSportWords);
                        
                        List<String> myArticleRemainingWords = new ArrayList<String>(myArticleWords);
                        myArticleRemainingWords.removeAll(myStopWords);
                        myArticleRemainingWords.removeAll(myArticlePoliticWords);
                        myArticleRemainingWords.removeAll(myEconomicWords);
                        myArticleRemainingWords.removeAll(myArticleSportWords);
                        
                        
                        area2.setText(null);
                        String[] articlewords= repeatedWords(myArticleRemainingWords);
                        for(String W: articlewords)
                            area2.append(W+"\n");
                        area2.append("\n");
                        
                        String[] articlepolitic= repeatedWords(myArticlePoliticWords);
                        area2.append("Politics are ( "+a+" ) as follow: \n");
                        for(String W: articlepolitic)
                            area2.append(W+"\n");                        
                        //area2.append("Politics sums: "+ a +"\n");
                        area2.append("\n");
                            
                        String[] articleeconomic= repeatedWords(myArticleEconomicWords);
                        area2.append("Economics are ( "+b+" ) as follow: \n");
                        for(String W: articleeconomic)
                            area2.append(W+"\n");                        
                        //area2.append("Economics sums: "+ b +"\n");
                        area2.append("\n");
                        
                        String[] articlesport= repeatedWords(myArticleSportWords);
                        area2.append("Sports are ( "+c+" ) as follow: \n");
                        for(String W: articlesport)
                            area2.append(W+"\n");                        
                            //area2.append("Sports sums: "+ c +"\n");
                       
                        
                        PieChart demo = new PieChart( "Analize result", a, b, c);
                        demo.setSize( 560 , 367 );
                        RefineryUtilities.centerFrameOnScreen( demo );
                        demo.setVisible( true );
                        demo.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                     

                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(AnalizeWIndow.class.getName()).log(Level.SEVERE, null, ex);
                    }     
               
               } else JOptionPane.showMessageDialog(null, "The article is very small");
            }
        });
        
        Icon fileSave =  new ImageIcon(getClass().getResource("resources/save.png"));
        JButton saveFileButton = new JButton("Save artcle",fileSave);
        windowToolbar.add(saveFileButton); 
        saveFileButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                 new ArticleDBForm(area.getText());
                 
            }
        });
                     
        add(windowToolbar, BorderLayout.NORTH);
        
        JPanel panel = new JPanel();
        GridLayout layout = new GridLayout(2, 1);
        panel.setLayout(layout);
                               
        JScrollPane spane = new JScrollPane();
        spane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area = new JTextArea();
        area.setLineWrap(true);        
        spane.getViewport().add(area);
        panel.add(spane);
        
        JScrollPane spane2 = new JScrollPane();
        spane2.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        area2 = new JTextArea("");
        area2.setEditable(false);
        area2.setBackground(Color.getColor("#E1DBDB"));
        area2.setLineWrap(true); 
        spane2.getViewport().add(area2);
        
        panel.add(spane2);
        add(panel);           
        
    }
        
    public List<String> articleToList (String str) throws FileNotFoundException{
        String getArticle = str; 
        InputStream in = new ByteArrayInputStream(getArticle.getBytes(Charset.forName("CP1251")));
        String article = new Scanner(in,"CP1251").useDelimiter("\\A").next();                  
        String list = article.replaceAll("\\w" ," ").replaceAll("\\p{Punct}", " ").replaceAll("\\p{Cntrl}", " ").replaceAll("—|«|»", " ").replaceAll("\\s+", " ").toLowerCase();
        List<String> myList = new ArrayList<>(Arrays.asList(list.split(" ")));       
        
        return myList;
    }
    
    public List<String> fromDBToList (int i){
    List<String> myList = new ArrayList();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs;
        
        String url = "jdbc:mysql://localhost:3306/masterdb";
        String user = "testuser";
        String password = "test623";
        
        try {
                      
            con = DriverManager.getConnection(url, user, password);
            
            pst = con.prepareStatement("SELECT * FROM masterdb.words where wordclass_id=" + i +";");
            rs = pst.executeQuery();
            
            
            while (rs.next()){
                int j = 0;
                myList.add(j, rs.getString(2));                
                j++;                         
            }
            
           
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Version.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    return myList;
    }    
    
    public String[] repeatedWords(List<String> alist){
        Map<String, Integer> repeatedWord = new HashMap<>();
                        Set<String> unique = new HashSet<>(alist);
                        unique.stream().forEach((key) -> {
                            repeatedWord.put(key , Collections.frequency(alist, key));
        });                         
                        
                        Set set=repeatedWord.entrySet();
                        Stream stream = set.stream();
                        Stream sorted = stream.sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));
                        Object[] toArray = sorted.toArray();
                        List<Object> asList = Arrays.asList(toArray);
                        String[] str = new String[asList.size()];
                        
                        int index =0;
                        for (Object value : asList) {
                            str[index] = String.valueOf( value );
                            index++;
                            } 
                        return str;    
    }    
}

