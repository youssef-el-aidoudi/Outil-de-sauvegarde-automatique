package Server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.*;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import MyClient.MyClient;




public class Server {
	
	
    static ArrayList<MyFile> myFiles = new ArrayList<>();
    static ArrayList<User> userNames = new ArrayList<>();
    
    
    
    
    public static String getFileExtension(String fileName) {
    	
    	int i = fileName.lastIndexOf('.');
    	
    	if (i > 0 ) {
    		return fileName.substring(+1);
    	}
    	else {
    		return "No extension found";
    	}
    	
    }
    
    public static JPanel findUserPanel(String userName) {
        for (User user : userNames) {
            if (user.getUserName().equals(userName)) {
                return user.getjPanel();
            }
        }
        
        return null;
    }
    
    public static JPanel findUserArchivePanel(String userName) {
        for (User user : userNames) {
            if (user.getUserName().equals(userName)) {
                return user.getjArchivePanel();
            }
        }
    
        
        return null;
    }
    
    public static User findUser(String userName) {
        for (User user : userNames) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
    
        
        return null;
    }
    
    public static MyFile findFile(String fileName) {
        for (MyFile myFile : myFiles) {
            if (myFile.getName().equals(fileName)) {
                return myFile;
            }
        }
    
        
        return null;
    }
   
    
    public static JFrame createFrame(String fileName, byte[] fileData, String fileExtension) {
    	
    	  JFrame jFrame = new JFrame("File Downloader");
    	  jFrame.setSize(400, 400);
    	  
    	  JPanel panel = new JPanel();
          panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
          
          JLabel title = new JLabel("File Downloader");
          title.setFont(new Font("Arial",Font.BOLD, 25));
          title.setBorder(new EmptyBorder(20, 0, 10, 0));
          title.setAlignmentX(Component.CENTER_ALIGNMENT);
          
          JLabel prompt = new JLabel("Are you sure you want to download " + fileName);
          prompt.setFont(new Font("Arial",Font.BOLD, 25));
          prompt.setBorder(new EmptyBorder(20, 0, 10, 0));
          prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
          	
          JButton yes = new JButton("Yes");
          yes.setFont(new Font("Arial", Font.BOLD, 20));
          yes.setPreferredSize(new Dimension(150, 75));
          
          JButton no = new JButton("No");
          no.setFont(new Font("Arial", Font.BOLD, 20));
          no.setPreferredSize(new Dimension(150, 75));
          
          JLabel fileContent = new JLabel("File Downloader");
          fileContent.setAlignmentX(Component.CENTER_ALIGNMENT);
          
          JPanel bPanel = new JPanel();
          bPanel.setBorder(new EmptyBorder(20, 0 , 10, 0));
          bPanel.add(yes);
          bPanel.add(no);
          
          if(fileExtension.equalsIgnoreCase("txt")) {
        	  fileContent.setText("<html>" + new String(fileData) + "</hmtl>");
          }
          else {
        	  fileContent.setIcon(new ImageIcon(fileData));
          }
          
          yes.addActionListener(new ActionListener() {
        	  @Override
      		public void actionPerformed(ActionEvent e) {
        		  
        		  File fileToDownload = new File(fileName);
        			try {
        				FileOutputStream  fileOutputStream = new FileOutputStream(fileToDownload);
        				fileOutputStream.write(fileData);
        				fileOutputStream.close();
        				
        				jFrame.dispose();
        			}catch(IOException error) {
        				error.printStackTrace();
        			}
      			
      			
      		}
          	
          });
          
	          
              no.addActionListener(new ActionListener() {
	        	 @Override
	            public void actionPerformed(ActionEvent e) {
	        		  
	        		  jFrame.dispose();
        	    }
	          });
              
              panel.add(title);
              panel.add(prompt);
              panel.add(fileContent);
              panel.add(bPanel);
              
              jFrame.add(panel);
              return jFrame;
        
        }
          	
    	
    	
		
    
    public static MouseListener getMyMouseListener() {
    	
    	return new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				
				JPanel panel = (JPanel) e.getSource();
				
				int fileId = Integer.parseInt(panel.getName());
				
				for(MyFile myFile: myFiles) {
					if(myFile.getId() == fileId) {
						JFrame preview = createFrame(myFile.getName(), myFile.getData(), myFile.getFileExtension());
						preview.setVisible(true);
						
						
					}
				}
				
			}
			

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
    	};
    		
    }
    

   public static void main(String[] args) throws IOException {
		
		 int fileId = 0;
		
		
		JFrame jFrame = new JFrame("Server");
        jFrame.setSize(400, 400);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS)); 
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       
        
        JLabel title = new JLabel("File Receiver");
        title.setFont(new Font("Arial",Font.BOLD, 25));
        title.setBorder(new EmptyBorder(20, 0, 10, 0));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        
       
        	
        jFrame.add(title);
        jFrame.setVisible(true);
  
        ServerSocket serverSocket = new ServerSocket(1234);
        
        while(true) {
        	
        	try {
        	Socket socket = serverSocket.accept();
        	
       
         	 
        	     
                    	 
                  		
                  	     DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                         int fileNameLenght = dataInputStream.readInt();
                         
                     
                     if(fileNameLenght > 0) {
                     	byte[] fileNameBytes = new byte[fileNameLenght];
                     	dataInputStream.readFully(fileNameBytes, 0 , fileNameBytes.length);
                     	String fileName = new String(fileNameBytes);
                     	String delimiter = "_";
                     	String[] parts = fileName.split(delimiter);
                     	String username = parts[0];
                     	String trueFileName = fileName.substring(fileName.indexOf(delimiter) + 1);
                     	
                     	if(userNames.isEmpty() || findUserPanel(username)==null ) {
                     		 
                     		JPanel panel = new JPanel();
                     		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                             
                             JScrollPane scrollPane = new JScrollPane(panel);
                             scrollPane.setVerticalScrollBarPolicy(scrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                             jFrame.add(scrollPane);
                             
                             userNames.add(new User(username,panel,null));
                             
                             int fileContentLength = dataInputStream.readInt();
                          	
                          	
                          	if(fileContentLength > 0 ) {
                          		 byte[] fileContentBytes = new byte[fileContentLength];
                          		 dataInputStream.readFully(fileContentBytes, 0 , fileContentLength);
                          		 
                          		 JPanel fileRow = new JPanel();
                                 fileRow.setLayout(new BoxLayout(fileRow, BoxLayout.Y_AXIS));
                                 JLabel jUserName = new JLabel(username);
                                 jUserName.setFont(new Font("Gothic",Font.BOLD, 30));
                                 
                          		  
                          		  JLabel jFileName = new JLabel(fileName);
                     	          jFileName.setFont(new Font("Arial",Font.BOLD, 20));
                          	        
                          	        if(getFileExtension(fileName).equalsIgnoreCase("txt")) {
                          	        	fileRow.setName(String.valueOf(fileId));
                          	        	fileRow.addMouseListener(getMyMouseListener());
                          	        	panel.add(jUserName);
                                        fileRow.add(jFileName);
                                         
                          	        	
                          	        	
                          	        	panel.add(fileRow);
                          	        	jFrame.validate();
                          	        	

                          	        
                          	        	
                          	        		
                          	         }
                          	         else {
                          	        	
                          	        	fileRow.setName(String.valueOf(fileId));
                          	        	fileRow.addMouseListener(getMyMouseListener());
                          	        	panel.add(jUserName);
                                        fileRow.add(jFileName);
                          	        	
                          	        	
                          	        	panel.add(fileRow);                         	        	
                          	        	jFrame.validate();
                          	        	
                          	        	
                          	        	
                          	        	
                          	    }	 
                          		
                          	     
                                      myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName), username));
                                      
                                      fileId++;
                                      
                                      
                      	       } 
                     		
                     	} else {
                     		    
                                int fileContentLength = dataInputStream.readInt();
                          	
                          	
                          	if(fileContentLength > 0 ) {
                          		 byte[] fileContentBytes = new byte[fileContentLength];
                          		 dataInputStream.readFully(fileContentBytes, 0 , fileContentLength);
                          		 
                          		 JPanel fileRow = new JPanel();
                                 fileRow.setLayout(new BoxLayout(fileRow, BoxLayout.Y_AXIS));
                                 
                                 
                          		  
                          		  JLabel jFileName = new JLabel(fileName);
                     	          jFileName.setFont(new Font("Arial",Font.BOLD, 20));
                          	        
                          	        if(getFileExtension(fileName).equalsIgnoreCase("txt")) {
                          	        	fileRow.setName(String.valueOf(fileId));
                          	        	fileRow.addMouseListener(getMyMouseListener());
                          	        	
                                        fileRow.add(jFileName);
                                         
                          	        	
                          	        	
                                        findUserPanel(username).add(fileRow);
                          	        	jFrame.validate();
                          	        	

                          	        
                          	        	
                          	        		
                          	         }
                          	         else {
                          	        	
                          	        	fileRow.setName(String.valueOf(fileId));
                          	        	fileRow.addMouseListener(getMyMouseListener());
                          	        	
                                        fileRow.add(jFileName);
                          	        	
                          	        	
                                        findUserPanel(username).add(fileRow);                         	        	
                          	        	jFrame.validate();
                          	        	
                          	        	
                          	        	
                          	    }	 
                          		
                          	      if(myFiles.contains(findFile(fileName))) {
                          	      if(findUserArchivePanel(username) == null) {
                          	    	    boolean labelFound = false;
                                		Component[] components = findUserPanel(username).getComponents(); 
                                		
                              	    for (Component component : components) {
                              	    	if (labelFound) { 
                              	          break;
                              	      }
                                        if (component instanceof JPanel) {
                                            JPanel nestedPanel = (JPanel) component;
                                            Component[] nestedComponents = nestedPanel.getComponents();
                                          for (Component nestedComponent : nestedComponents) {
                                            if (nestedComponent instanceof JLabel) {
                                                JLabel label = (JLabel) nestedComponent;
                                              if (label.getText().equals(fileName)) {
                                            	  labelFound = true;
                                            	String ArchiveName = "Archive" + username;
                                            	JPanel ArchivePanel = new JPanel();
                                                ArchivePanel.setLayout(new BoxLayout(ArchivePanel, BoxLayout.Y_AXIS));
                                                
                                                JLabel jUserName = new JLabel(ArchiveName);
                                                jUserName.setFont(new Font("Gothic",Font.BOLD, 30));
                                                
                                                findUser(username).setjArchivePanel(ArchivePanel);
                                                
                                                JScrollPane ArchivescrollPane = new JScrollPane(ArchivePanel);
                                                ArchivescrollPane.setVerticalScrollBarPolicy(ArchivescrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                                                jFrame.add(ArchivescrollPane);
                                                
                                                ArchivePanel.add(jUserName);
                                                
                                                JLabel ArchiveFile = new JLabel(label.getText());
                                                ArchiveFile.setFont(new Font("Arial",Font.BOLD, 20));
                                                
                                                JPanel ArchiveFileRow = new JPanel();
                                                ArchiveFileRow.setLayout(new BoxLayout(ArchiveFileRow, BoxLayout.Y_AXIS));
                                                
                                                findUserPanel(username).remove(component);
                                                findUserPanel(username).revalidate();
                                                findUserPanel(username).repaint();
                                                
                                                
                                                
                                                if(getFileExtension(label.getText()).equalsIgnoreCase("txt")) {
                                                	ArchiveFileRow.setName(String.valueOf(fileId));
                                                	ArchiveFileRow.addMouseListener(getMyMouseListener());
                                      	        	
                                                	ArchiveFileRow.add(ArchiveFile);
                                                	
                                                     
                                      	        	
                                      	        	
                                                	ArchivePanel.add(ArchiveFileRow);
                                                	jFrame.validate();
                                      	        	

                                      	        
                                      	        	
                                      	        		
                                      	         }
                                      	         else {
                                      	        	
                                      	        	ArchiveFileRow.setName(String.valueOf(fileId));
                                      	        	ArchiveFileRow.addMouseListener(getMyMouseListener());
                                      	        	
                                      	        	ArchiveFileRow.add(ArchiveFile);
                                      	        	
                                      	        	
                                      	        	ArchivePanel.add(ArchiveFileRow);                         	        	
                                      	        	jFrame.validate();
                                      	        	
                                      	        	
                                      	        	
                                      	        	
                                      	           }
                                                
                                                }
                                             }
                                         } 
                                	 }
                                 }
                              	  //System.out.println( findUserPanel(username).getComponentCount());    
                              }	else {
                            	   
                            	  Component[] components = findUserPanel(username).getComponents();
                            	  boolean labelFound = false;
                          	    for (Component component : components) {
                          	    	if (labelFound) { 
                            	          break;
                            	      }
                                    if (component instanceof JPanel) {
                                        JPanel nestedPanel = (JPanel) component;
                                        Component[] nestedComponents = nestedPanel.getComponents();
                                        for (Component nestedComponent : nestedComponents) {
                                        if (nestedComponent instanceof JLabel) {
                                            JLabel label = (JLabel) nestedComponent;
                                        if (label.getText().equals(fileName)) {
                                        	labelFound = true;
                                        	

                                            
                                            
                                            
                                            JLabel ArchiveFile = new JLabel(label.getText());
                                            ArchiveFile.setFont(new Font("Arial",Font.BOLD, 20));
                                            
                                            JPanel ArchiveFileRow = new JPanel();
                                            ArchiveFileRow.setLayout(new BoxLayout(ArchiveFileRow, BoxLayout.Y_AXIS));
                                            
                                            findUserPanel(username).remove(component);
                                            findUserPanel(username).revalidate();
                                            findUserPanel(username).repaint();
                                            
                                            
                                            
                                            if(getFileExtension(label.getText()).equalsIgnoreCase("txt")) {
                                            	ArchiveFileRow.setName(String.valueOf(fileId));
                                            	ArchiveFileRow.addMouseListener(getMyMouseListener());
                                  	        	
                                            	ArchiveFileRow.add(ArchiveFile);
                                                 
                                  	        	
                                  	        	
                                            	findUserArchivePanel(username).add(ArchiveFileRow);
                                            	jFrame.validate();
                                  	        	

                                  	        
                                  	        	
                                  	        		
                                  	         }
                                  	         else {
                                  	        	
                                  	        	ArchiveFileRow.setName(String.valueOf(fileId));
                                  	        	ArchiveFileRow.addMouseListener(getMyMouseListener());
                                  	        	
                                  	        	ArchiveFileRow.add(ArchiveFile);
                                  	        	
                                  	        	
                                  	        	findUserArchivePanel(username).add(ArchiveFileRow);                         	        	
                                  	        	jFrame.validate();
                                  	        	
                                  	        	
                                  	        	
                                  	        	
                                  	       }
                                            
                                          }
                                       }
                                    } 
                            	   }
                              }
                          	  //System.out.println( findUserPanel(username).getComponentCount());
                            }
                          	      
                          	        
                          	      }
                          			
                                      myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName), username));
                                      
                                      fileId++;
                                      
                      	       
                     		
                     	}
                          }
                     	
                       } 
                 
        	         
        	        	// System.out.println(myFiles.size());
        	         
                  	 
                
         	
        	
        	} catch (IOException e) {
                e.printStackTrace();
            }
        
        
        	
        	
        

	   }

    }

 }
