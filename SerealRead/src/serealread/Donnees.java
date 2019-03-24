/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serealread;


import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import javax.comm.CommPortIdentifier;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;



/**
 *
 * @author zouhair
 */
   public  class Donnees implements Runnable, SerialPortEventListener{
    private String id="";
    private double t=0;
    private double l=0;
    private String id_b="";
    private double t_b=0;
    private double l_b=0;
    private String rfid="";
    static CommPortIdentifier portId;
    static Enumeration portList;
    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
    int cont=0;
    String str1,str2;

    public Donnees() {
    }

    public Donnees(String id, double t, double l,String rfid) {
        this.id = id;
        this.t = t;
        this.l = l;
        this.rfid=rfid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public String getRfid() {
        return rfid;
    }

    public void setRfid(String rfid) {
        this.rfid = rfid;
    }

    public String getId_b() {
        return id_b;
    }

    public void setId_b(String id_b) {
        this.id_b = id_b;
    }

    public double getT_b() {
        return t_b;
    }

    public void setT_b(double t_b) {
        this.t_b = t_b;
    }

    public double getL_b() {
        return l_b;
    }

    public void setL_b(double l_b) {
        this.l_b = l_b;
    }
    
    
    

public static String[] getPort(){
            @SuppressWarnings("unchecked")
         java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        int i = 0;
  
        
        String[] r = new String[5];
            
        while (portEnum.hasMoreElements() && i < 5) {
            if(i==0){
                 r[i] ="choix ...";
                 i++;
            }else{
            CommPortIdentifier portIdentifier = portEnum.nextElement();

            r[i] = portIdentifier.getName();//+  " - " +  getPortTypeName(portIdentifier.getPortType()) ;

             
            i++;}


        }  return r;
        
        
        
        
}

 public void SimpleRead(String com) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (portId.getName().equals(com)) {
                  
                    try {
                        
                        serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
                        
                    } catch (PortInUseException e) {
                        System.out.println(e);
                    }
                    try {
                        inputStream = serialPort.getInputStream();
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                    try {
                        serialPort.addEventListener(this);
                    } catch (TooManyListenersException e) {
                        System.out.println(e);
                    }
                    serialPort.notifyOnDataAvailable(true);
                    try {
                        serialPort.setSerialPortParams(9600,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                    } catch (UnsupportedCommOperationException e) {
                        System.out.println(e);
                    }
                    readThread = new Thread(this);
                    readThread.start();
                }
            }
        }

    }
 
     public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
     
         public static void waiting(int n) {

        long t0, t1;

        t0 = System.currentTimeMillis();

        do {
            t1 = System.currentTimeMillis();
        } while ((t1 - t0) < (n * 1000));
    }
         
            public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;
            case SerialPortEvent.DATA_AVAILABLE:
                byte[] readBuffer = new byte[60];

                try {
                    while (inputStream.available() > 4) {
              
                        inputStream.read(readBuffer);
                        cont++;
                        
                    }

                    String y = new String(readBuffer);
                    y=y.replaceAll("\\s+", "");
                    y=y.replaceAll("\\t+", "");
                  

                    
                   if(cont>1){
                       
                    //System.out.println(y);
                    String[] data= y.split("-");
                    if(data.length==4){
                    //System.out.println("envoi par badge **** length : "+data.length);
                    String id=data[0];
                   double t=Double.parseDouble(data[1]);
                   double l=Double.parseDouble(data[2]);
                   String rfid=data[3];
                    this.id_b=id;
                    this.l_b=l;
                    this.t_b=t;
                    this.rfid=rfid;
                    System.out.println("id: "+id);
                    System.out.println("T: "+t);
                    System.out.println("L: "+l);
                    System.out.println("RFID: "+rfid);
                    
                    
                   
                    }else if(data.length==3){
                   // System.out.println("evoi automatique ***** length : "+data.length);
                    String id=data[0];
                    double t=Double.parseDouble(data[1]);
                    double l=Double.parseDouble(data[2]);
                    this.id=id;
                    this.t=t;
                    this.l=l;
                    
                   // System.out.println("id: "+id);
                   // System.out.println("T: "+t);
                   // System.out.println("L: "+l);
                   
                    
                    
                    
                    
                
                    }
                   }
                    waiting(1);
                } catch (IOException e) {
                    System.out.println(e);
                }
                break;
        }
    }
            
         
}