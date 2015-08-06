package mstergui;

import com.mysql.jdbc.ResultSetMetaData;
import com.mysql.jdbc.Statement;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;


public class MsterGUI {  
    public static void main(String[] args) {
        MyFrame gui = new MyFrame();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setVisible(true); 
                        
        //JScrollPane guispane = new JScrollPane();          
        //guispane.getViewport();
        //gui.add(guispane);
    }
}
    
class MyFrame extends JFrame {    
    public MyFrame(){
        setSize(800, 600);
        setLocationRelativeTo(null);
        setTitle("Text analyzer");
        
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);   
        Icon refreshIcon = new ImageIcon(getClass().getResource("resources/refresh.png"));
        JButton refreshButton = new JButton("Refresh",refreshIcon);
        toolbar.add(refreshButton);
        
        JScrollPane spane = new JScrollPane();
        spane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        /*JButton but = new JButton("rel,a");
        spane.add(but);*/
        spane.getViewport().add(refreshTable());
                
        refreshButton.doClick();
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                spane.getViewport().add(refreshTable());
                
                /*String[] columnNames = {"Article_ID",
                        "Article Name",
                        "Article path",
                        "Article class",
                        "Creation date"};
                 JTable table = new JTable(refreshTable(), columnNames);
                 table.setFillsViewportHeight(true);
                 table.setVisible(true);
                 JScrollPane scrollPane = new JScrollPane(table);
                 add(scrollPane);*/
                
            }
        });       
        add(toolbar,BorderLayout.NORTH);
        add(spane);
        
        MyMenuBar menubar = new MyMenuBar();
        setJMenuBar(menubar);         
    }
    
   public JTable refreshTable(){
        ArrayList columnNames = new ArrayList();
        ArrayList data = new ArrayList();
        
        String url = "jdbc:mysql://localhost:3306/masterdb";
        String user = "testuser";
        String password = "test623";
        
         String sql = "SELECT id_article, articlename, articlepath, classname, articledate\n" +
                      "FROM masterdb.articles, masterdb.class where articleclass_id=masterdb.class.id_class\n" +
                      "order by id_article;";
         
         try (com.mysql.jdbc.Connection connection = (com.mysql.jdbc.Connection) DriverManager.getConnection( url, user, password );
            Statement stmt = (Statement) connection.createStatement();
            ResultSet rs = stmt.executeQuery( sql )) {
            ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
            int columns = md.getColumnCount();

            //  Get column names
            for (int i = 1; i <= columns; i++) {
                columnNames.add( md.getColumnName(i) );
            }

            //  Get row data
            while (rs.next()) {
                ArrayList row = new ArrayList(columns);
                String path;

                for (int i = 1; i <= columns; i++) {
                    if (i==3){
                        path = ((String)rs.getObject(i)).replaceAll(",", "\\\\");
                        row.add( path);
                    } else
                    row.add( rs.getObject(i) );
                }

                data.add( row );
            }
        }
        catch (SQLException e) {
            System.out.println( e.getMessage() );
        }
         
        Vector columnNamesVector = new Vector();
        Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++) {
            ArrayList subArray = (ArrayList) data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++) {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }

        for (int i = 0; i < columnNames.size(); i++ )
            columnNamesVector.add(columnNames.get(i));
        
        JTable table = new JTable(dataVector, columnNamesVector) {
            public Class getColumnClass(int column){
                for (int row = 0; row < getRowCount(); row++) {
                    Object o = getValueAt(row, column);

                    if (o != null){
                        return o.getClass();
                    }
                }
                return Object.class;
            }
        };
                       

     return table;       
    }
}



class MyMenuBar extends JMenuBar{
    public MyMenuBar(){      
                
        JMenu fileMenu = new JMenu("Menu");        
        add(fileMenu);
        JMenuItem newAction = new JMenuItem("Classify articles");
        JMenuItem openAction = new JMenuItem("Clustering articles");
        JMenuItem exitAction = new JMenuItem("Exit");
        fileMenu.add(newAction);
        fileMenu.add(openAction);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);  
        
        exitAction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        
        newAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AnalizeWIndow loadWindow = new AnalizeWIndow();
                loadWindow.setVisible(true);               
            }
        });
        
        openAction.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
            
        });
    }
}





