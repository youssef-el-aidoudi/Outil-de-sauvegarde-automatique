package MyClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Server.MyFile;



public class MyClient extends Thread {

	@Override
	public void run() {
		File[] fileToSend = new File[1];
	    ArrayList<File> filesToSend = new ArrayList<>();
	       
	       
			

	        JFrame jFrame = new JFrame("Client");
	        jFrame.setSize(450, 450);
	        jFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
	        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        JLabel title = new JLabel("Name");
	        title.setFont(new Font("Arial", Font.BOLD, 10));

	        JTextField field = new JTextField(20); 
	        field.setFont(new Font("Arial", Font.PLAIN, 10));
	        
	        JTextField field1 = new JTextField(20); 
	        field.setFont(new Font("Arial", Font.PLAIN, 10));
	        
	        JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        
	        JScrollPane scrollPane = new JScrollPane(panel);
	        scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        jFrame.add(scrollPane);
	        
	        JButton choosebutton = new JButton("Choose File");
	        choosebutton.setFont(new Font("Arial", Font.BOLD, 10));
	        
	        JButton button10 = new JButton("10s");
	        button10.setFont(new Font("Arial", Font.BOLD, 10));
	        
	        JButton remove = new JButton("Remove File");
	        remove.setFont(new Font("Arial", Font.BOLD, 10));
	        
	        
	        JButton button15 = new JButton("15s");
	        button15.setFont(new Font("Arial", Font.BOLD, 10));
	        
	        JLabel filename = new JLabel();
	        filename.setFont(new Font("Arial", Font.BOLD, 10));
	       

	        jFrame.add(title);
	        jFrame.add(field);
	        jFrame.add(field1);
	        jFrame.add(button10);
	        jFrame.add(button15);
	        jFrame.add(choosebutton);
	        jFrame.add(remove);
	        jFrame.add(filename);
	        jFrame.setVisible(true);
	        
	        
	           
	        
	        
	        
	        choosebutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                JFileChooser jFileChooser = new JFileChooser();
	                jFileChooser.setDialogTitle("Choose a file to send");
	                
	                if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
	                	
	                    fileToSend[0] = jFileChooser.getSelectedFile();
	                	filesToSend.add(fileToSend[0]);
	                	filename.setText("The file you want to send is: " + fileToSend[0].getName());
	                	
                       
	                	JLabel jFileName = new JLabel(fileToSend[0].getName());
	       	            jFileName.setFont(new Font("Arial",Font.BOLD, 20));
	       	            
	                	
	                	
	                    panel.add(jFileName);
	       	         
	                	
	                }
	                
	            }
	        });
	        
	        remove.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                for (int i = 0; i < filesToSend.size(); i++) {
	                    File file = filesToSend.get(i);
	                    if (file.getName().equals(field1.getText())) {
	                        filesToSend.remove(i);
	                        break;
	                    }
	                }
	                for (int i = 0; i < panel.getComponentCount(); i++) {
	                    Component comp = panel.getComponent(i);
	                    if (comp instanceof JLabel) {
	                        JLabel label = (JLabel) comp;
	                        if (label.getText().equals(field1.getText())) {
	                            panel.remove(i);
	                            panel.revalidate();
	                            panel.repaint();
	                            break;
	                        }
	                    }
	                }
	            }
	        });

	       
	        
	        Timer timer = new Timer();
	        
	        TimerTask task = new TimerTask() {

				@Override
				public void run() {
					if (filesToSend.isEmpty()) {
	                    filename.setText("Choose files First.");
	                } else {
	                    try {
	                    	
	                        for(int i = 0; i < filesToSend.size(); i++) {
	                        	//System.out.println(filesToSend.get(i).getName());
	                        	Socket socket = new Socket("localhost", 1234);
	                            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

	                       
	                                FileInputStream fileInputStream = new FileInputStream(filesToSend.get(i).getAbsolutePath());

	                                String fileName = field.getText() + "_" + filesToSend.get(i).getName(); 
	                                byte[] fileNameBytes = fileName.getBytes();

	                                byte[] fileContentBytes = new byte[(int) filesToSend.get(i).length()];
	                                fileInputStream.read(fileContentBytes);

	                                dataOutputStream.writeInt(fileNameBytes.length);
	                                dataOutputStream.write(fileNameBytes);

	                                dataOutputStream.writeInt(fileContentBytes.length);
	                                dataOutputStream.write(fileContentBytes);

	                               
	                        }
	                    } catch (IOException error) {
	                        error.printStackTrace();
	                    }
	                 
	                }
					
				}
	        	
	        };

	         
	         
	         button10.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent e) {
	                 
	            	 timer.scheduleAtFixedRate(task,0,10000);
	             }
	         });
	         
	         button15.addActionListener(new ActionListener() {
	             @Override
	             public void actionPerformed(ActionEvent e) {
	                 
	            	 timer.scheduleAtFixedRate(task,0, 15000);
	             }
	         });
		
	}




			
    		
    
    
    
 

    
   

	public static void main(String[] args) {
		
		
		MyClient clientThread = new MyClient();
        clientThread.start();
        
       

 }


}
	