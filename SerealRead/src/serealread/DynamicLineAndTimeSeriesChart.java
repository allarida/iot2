/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serealread;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javax.swing.Timer;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 * An example to show how we can create a dynamic chart.
*/
public class DynamicLineAndTimeSeriesChart extends ApplicationFrame implements ActionListener {

    private TimeSeries series,series2,series3,series4;
    private Donnees donnees= new Donnees();
    private Database database = new Database();

  
   

    /** Timer to refresh graph after every 1/4th of a second */
    private Timer timer = new Timer(500, this);

    /**
     * Constructs a new dynamic chart application.
     *
     * @param title  the frame title.
     */
    public DynamicLineAndTimeSeriesChart(final String title,String com) {

        super(title);
        
        
        donnees = new Donnees();
        donnees.SimpleRead(com);
        this.series = new TimeSeries("Température", Millisecond.class);
        this.series2 =new TimeSeries("Luminosité", Millisecond.class);
        this.series3 = new TimeSeries("Température", Millisecond.class);
        this.series4 =new TimeSeries("Luminosité", Millisecond.class);
        TimeSeriesCollection SeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection SeriesCollection2 = new TimeSeriesCollection();
        SeriesCollection.addSeries(series);
        SeriesCollection.addSeries(series2);
        SeriesCollection2.addSeries(series3);
        SeriesCollection2.addSeries(series4);
        JFreeChart chart = createChart(SeriesCollection,"arduino 1");
        JFreeChart chart2 = createChart(SeriesCollection2,"arduino 2");

        timer.setInitialDelay(1000);

        //Sets background color of chart
        chart.setBackgroundPaint(Color.LIGHT_GRAY);
        chart2.setBackgroundPaint(Color.PINK);
        //Created JPanel to show graph on screen
        JPanel content = new JPanel(new BorderLayout());
        JPanel content2 = new JPanel(new BorderLayout());
        //Created Chartpanel for chart area

        ChartPanel chartPanel = new ChartPanel(chart);
        ChartPanel chartPanel2 = new ChartPanel(chart2);
        //Added chartpanel to main panel

        content.add(chartPanel);
        content2.add(chartPanel2);
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));



        container.add(content);
        container.add(content2);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 400));
        chartPanel2.setPreferredSize(new java.awt.Dimension(500, 400));

 
        this.setContentPane(container);
        timer.start();

    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset,String titre) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            titre,
            "Temps",
            "Degré en c° ",
            dataset,
            true,
            true,
            false
        );
        final XYPlot plot = result.getXYPlot();

        plot.setBackgroundPaint(new Color(0xffffe0));
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.lightGray);

        ValueAxis xaxis = plot.getDomainAxis();
        xaxis.setAutoRange(true);
        

        //Domain axis would show data of 60 seconds for a time
        xaxis.setFixedAutoRange(60000.0);  // 60 seconds
        xaxis.setVerticalTickLabels(true);

        ValueAxis yaxis = plot.getRangeAxis();
        yaxis.setRange(0.0, 100.0);

        return result;
    }
    /**
     * Generates an random entry for a particular call made by time for every 1/4th of a second.
     *
     * @param e  the action event.
     */
    public void actionPerformed(final ActionEvent e) {
    JOptionPane jop1;

       if(!donnees.getRfid().equals("")){
            
        try {
            String username ="";
            username=database.getUsernameByRfid(donnees.getRfid());
            if(!username.equals("")){
                    final Millisecond now = new Millisecond();
                    String time =""+now;
                    boolean ok =database.addDonnees(time, ""+donnees.getL_b(), ""+donnees.getT_b(), donnees.getRfid(), donnees.getId_b());
            if(ok){
               jop1 = new JOptionPane();
               jop1.showMessageDialog(null, "Sauvegarde faite par  : "+username, "Sauvegarde !", JOptionPane.INFORMATION_MESSAGE);
            
            }else{
                System.out.println("erreur de sauvegarde ");
            }
                    
                    
             }else{
            
                System.out.println("utilisateur introuvable !!");
            }
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DynamicLineAndTimeSeriesChart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DynamicLineAndTimeSeriesChart.class.getName()).log(Level.SEVERE, null, ex);
        }
       donnees.setRfid("");
       }
      
       if(donnees.getId().equals("1")){
        this.series.add(new Millisecond(), donnees.getT());
        this.series2.add(new Millisecond(), 0.1*donnees.getL());
       }else if(donnees.getId().equals("2")){
        this.series3.add(new Millisecond(), donnees.getT());
        this.series4.add(new Millisecond(), 0.1*donnees.getL());
       }
       
    }

    /**
     * Starting point for the dynamic graph application.
     *
     * @param args  ignored.
     */

 

    
}
    