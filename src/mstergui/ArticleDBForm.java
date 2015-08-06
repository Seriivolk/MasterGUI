package mstergui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class ArticleDBForm extends JFrame{
    
    String[] clases ={"","Politic","Economic","Sport"};
    String fromTexField;
    String fromComboBox;
    String bpath;
    String path;
    String relativePath;
    
    ArticleDBForm(String str){
        
        if (str.length()>20){
        
        JTextField textField = new JTextField(15);
        JComboBox comboBox = new JComboBox(clases);
        setTitle("Save form");
                      
        add(new JLabel("Set the article title:"));
        add(textField);
        
        add(new JLabel("Choose the article class                 "));
        add(comboBox);        
        
        JButton but=new JButton();
        but.setText("Save the article");
        add(but);
        but.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                fromTexField = textField.getText();
                fromComboBox = (String) comboBox.getSelectedItem(); 
                int i=0;
                
                if (fromTexField.length()>4 && fromComboBox!=""){  
                    switch (fromComboBox) {
                        case "Politic": i=1;
                        break;
                            case "Economic": i=2;
                            break;
                                case "Sport": i=3;
                                break;
                                    }
                    
                    }else JOptionPane.showMessageDialog(null, "article title or article class not filled");
                
                bpath = (fromTexField.replaceAll("\\p{Punct}", "").replaceAll(" ", "_"));
                
                if( bpath.length() > 70 ) {
                    path = bpath.substring(0,70)+".txt";
                    } else path = bpath+".txt";
                
                try {
                    relativePath=saveFile(str,path,i);
                } catch (IOException ex) {
                    Logger.getLogger(ArticleDBForm.class.getName()).log(Level.SEVERE, null, ex);
                }
                String newPath = relativePath.replaceAll("\\\\", ",");
                
                
                insertIntoDB1(fromTexField,newPath,i); 
                                
                /*System.out.println("Article title: " + fromTexField);
                System.out.println("Article class " + fromComboBox);
                System.out.println("Article bpath " + bpath);
                System.out.println("Article path " + path);
                System.out.println("Article path " + newPath);*/
                dispose();                
            }
        });
        
        setLayout(new FlowLayout());
		setSize(300,200);
                setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                
        } else JOptionPane.showMessageDialog(null, "The article is very small");
    
    }   
    
    private void insertIntoDB1(String article, String artpath, int clas) {
        String articleTitle = article;
        String articlePath = artpath;
        int i=clas;
        
        Connection connection = null;
        Statement stmt = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/masterdb", "testuser", "test623");
             
            stmt = connection.createStatement();
            stmt.execute("INSERT INTO `masterdb`.`articles` (`articlename`, `articlepath`, `articleclass_id`) VALUES ('"+articleTitle+"', '"+articlePath+"', '"+i+"');");
            JOptionPane.showMessageDialog(null, articleTitle+"\nSaved in DB");
        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                stmt.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}
    
    private String saveFile (String str, String pth, int i) throws IOException{
        Files file=null;
        String article = str;
        String localPath=pth;
        int b = i;
        Path path=null;
        
        if (b==1){
           path = Paths.get("src\\mstergui\\Articles\\Politic\\"+localPath); 
           }else if(b==2){
               path = Paths.get("src\\mstergui\\Articles\\Economic\\"+localPath);
               }else if(b==3){
                   path = Paths.get("src\\mstergui\\Articles\\Sport\\"+localPath);
                   }
        
        Path inputPath = path;
        Path fullPath = inputPath.toAbsolutePath();
        
         file.createFile(path);
         
         PrintWriter writer = new PrintWriter(path.toString(), "UTF-8");
         
         String[] lines = article.split("\n");
         for (int j=0; j<lines.length; j++)
             writer.println(lines[j]);
         writer.close();
         
         JOptionPane.showMessageDialog(null, fullPath+"\ncreated in file system");
         
         return fullPath.toString();
    }
}

