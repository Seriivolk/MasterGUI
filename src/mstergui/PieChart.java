package mstergui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DefaultPieDataset;



public class PieChart extends JFrame {
    
    public PieChart(String title, int a, int b, int c ) {
        super(title);
        
        setContentPane(createDemoPanel( a, b, c ));
        
    }
    
    private PieDataset createDataset(int a, int b, int c) {
        DefaultPieDataset dataset = new DefaultPieDataset( );
        dataset.setValue( "Politics are" , new Double(a) );  
        dataset.setValue( "Economics are" , new Double( b ) );
        dataset.setValue( "Sports are" , new Double( c ) );  
        return dataset;    
    }
    
    private JFreeChart createChart( PieDataset dataset )
   {
      JFreeChart chart = ChartFactory.createPieChart(      
         "Analize result",  // chart title 
         dataset,        // data    
         true,           // include legend   
         true, 
         false);

      return chart;
   }
   
    public JPanel createDemoPanel( int a, int b, int c)
   {
      JFreeChart chart = createChart(createDataset( a, b, c ) );  
      return new ChartPanel( chart ); 
   }     
    
}