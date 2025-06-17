import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.text.*;
import java.io.*;
import java.net.*;

public class Client implements ActionListener{

    // Custom JLabel for circular image
    class CircularImageLabel extends JLabel {
        public CircularImageLabel(ImageIcon image) {
            super(image);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            
            // Create circular clip
            Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
            g2.setClip(circle);
            
            // Draw the image
            super.paintComponent(g);
        }
    }

    //global declaration 
    JTextField msg;
    //to display the messages
    static JPanel textArea; 
    //to vertically align the messages one by one
    static Box vertical = Box.createVerticalBox();
    static DataOutputStream out;
    static JFrame fr = new JFrame();
    static JScrollPane scrollPane; // Add scroll pane declaration
    static JComboBox<String> userSelector = new JComboBox<>();


    //Constructor
    Client(){
        fr.setTitle("SpillTheTea");
        fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fr.setResizable(false);
        fr.setLayout(null);

        //Set application icon
        ImageIcon appIcon = new ImageIcon(ClassLoader.getSystemResource("images/icon.png"));
        Image appImg = appIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        fr.setIconImage(appImg);

        //Create the layout of the frame using JPanel
        JPanel head = new JPanel();
        head.setBackground(new Color(0x4a4a4a));
        head.setBounds(0,0,450,75);
        head.setLayout(null);
        fr.add(head);

        //Add back button
        JButton backButton = new JButton();
        ImageIcon backIcon = new ImageIcon(ClassLoader.getSystemResource("images/backarrow.png"));
        Image img = backIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        backButton.setIcon(new ImageIcon(img));
        backButton.setBounds(5, 20, 40, 40);
        backButton.setBackground(new Color(0, 0, 0, 0)); // Transparent background
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false); 
        backButton.setOpaque(false); 
        backButton.addActionListener(this);
        head.add(backButton);

        //to insert the default profile image in the panel
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/DefaultProfile.png"));
        Image i2 = i1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon i3 = new ImageIcon(i2);
        CircularImageLabel profile = new CircularImageLabel(i3);
        profile.setBounds(50, 13, 50, 50);
        head.add(profile);

        //to write the name of the user in the panel
        JLabel name = new JLabel("User 2");
        name.setBounds(110, 25, 100, 20);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Arial", Font.BOLD, 18));
        head.add(name);

        //to add phone and video call button using an image icon
        JButton phone = new JButton();
        ImageIcon phoneIcon = new ImageIcon(ClassLoader.getSystemResource("images/phone.png"));
        Image phoneImg = phoneIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        phone.setIcon(new ImageIcon(phoneImg));
        phone.setBounds(300, 20, 40, 40);
        phone.setBackground(new Color(0, 0, 0, 0));
        phone.setFocusPainted(false);
        phone.setBorderPainted(false);
        phone.setContentAreaFilled(false);
        phone.setOpaque(false);
        head.add(phone);

        JButton video = new JButton();
        ImageIcon videoIcon = new ImageIcon(ClassLoader.getSystemResource("images/video.png"));
        Image videoImg = videoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        video.setIcon(new ImageIcon(videoImg));
        video.setBounds(350, 20, 40, 40);
        video.setBackground(new Color(0, 0, 0, 0));
        video.setFocusPainted(false);
        video.setBorderPainted(false);
        video.setContentAreaFilled(false);
        video.setOpaque(false);
        head.add(video);

        //add a button for more options
        JButton more = new JButton();
        ImageIcon moreIcon = new ImageIcon(ClassLoader.getSystemResource("images/moreOptions.png"));
        Image moreImg = moreIcon.getImage().getScaledInstance(20, 40, Image.SCALE_SMOOTH);
        more.setIcon(new ImageIcon(moreImg));
        more.setBounds(405, 20, 20, 40);
        more.setBackground(new Color(0, 0, 0, 0));
        more.setFocusPainted(false);
        more.setBorderPainted(false);
        more.setContentAreaFilled(false);
        more.setOpaque(false);
        head.add(more);

        //new panel for the text functionality
        textArea = new JPanel();
        textArea.setLayout(new BoxLayout(textArea, BoxLayout.Y_AXIS));
        textArea.setBackground(new Color(0,0,0,0));
        
        // Create scroll pane and add textArea to it
        scrollPane = new JScrollPane(textArea);
        scrollPane.setBounds(5, 77, 425, 580);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        fr.add(scrollPane);

        //field for the user to type the 
        msg = new JTextField();
        msg.setBounds(5, 660, 350, 52);
        msg.setBackground(Color.WHITE);
        msg.setFont(new Font("SansSerif", Font.PLAIN, 18));
        fr.add(msg);

        //userSelector Initialization (moved from head)
        userSelector.setBounds(5, 620, 200, 30);
        userSelector.setFont(new Font("SansSerif", Font.PLAIN, 14));
        fr.add(userSelector);
        

        //add a send button
        JButton send = new JButton("Send");
        send.setBounds(357, 660, 72, 52 );
        send.setBackground(new Color(0x4a4a4a));
        send.setFont(new Font("SansSerif", Font.PLAIN, 16));
        send.setForeground(Color.WHITE);
        send.addActionListener(this); //to add an action on click 
        fr.add(send);


        //full frame 
        fr.setSize(450,750);
        fr.setLocation(800,50);

        //to change the background of the frame 
        fr.getContentPane().setBackground(new Color(0xF5F0E6));

        fr.setVisible(true);

        try{
            String userName = "User2"; // Assign a default username 

            if(userName ==  null || userName.trim().isEmpty()){
                JOptionPane.showMessageDialog(null,"Username cannot be empty.");
                return;
            }
            //a socket to connect to the server socket
            Socket sc = new Socket("127.0.0.1", 5500);
            DataInputStream in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());
            out.writeUTF(userName);

            //to receive messages infinitely using thread
            new Thread(() -> {
                try{
                    while (true) {
                        String txt = in.readUTF();
                        if(txt.startsWith("USERLIST:")){
                            String[] users = txt.substring(9).split(",");
                            Set<String> userSet = new TreeSet<>(Arrays.asList(users));
                            updateUserList(userSet, userName);
                        }else{
                            JPanel panel = formatLabel(txt, false);
                            JPanel left = new JPanel(new BorderLayout());
                            left.add(panel, BorderLayout.LINE_START);
                            vertical.add(left);
                            fr.validate();
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }).start();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton clickedButton = (JButton) e.getSource();
            if (clickedButton.getIcon() != null) {
                fr.dispose(); // Close current window
            }
        }

        try{
            String message = msg.getText();
            String rcvr = (String) userSelector.getSelectedItem();

            if(rcvr != null && !rcvr.isEmpty()){
                //to send the message
                String fullmsg = "@" + rcvr + ": " + message;
                out.writeUTF(fullmsg);

                //as add() doesn't take string for an argument
                JPanel output = formatLabel(message, true);

                //to display the sent message on the right side 
                textArea.setLayout(new BorderLayout());

                JPanel sentMsg = new JPanel(new BorderLayout());
                sentMsg.add(output, BorderLayout.LINE_END);
                vertical.add(sentMsg);
                vertical.add(Box.createVerticalStrut(10)); //with space between two messsages

                //add the messages to the panel
                textArea.add(vertical, BorderLayout.PAGE_START);

                // Scroll to bottom after new message
                SwingUtilities.invokeLater(() -> {
                    JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
                    verticalScrollBar.setValue(verticalScrollBar.getMaximum());
                });

                //the textArea should be clear after the text is sent
                msg.setText("");

                //to show the messages
                textArea.revalidate();
                textArea.repaint();
            }
        }catch(Exception ea){
            ea.printStackTrace();
        }
    }

    //to show messages in the box
    public static JPanel formatLabel(String message, boolean isSent){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // Create a temporary label to measure text width
        JLabel tempLabel = new JLabel(message);
        tempLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        FontMetrics metrics = tempLabel.getFontMetrics(tempLabel.getFont());
        int textWidth = metrics.stringWidth(message);
        
        // Set maximum width constraint only if text is longer than 200px
        if (textWidth > 200) {
            panel.setMaximumSize(new Dimension(250, Integer.MAX_VALUE));
            JLabel output = new JLabel("<html><body style='width: 250px'>" + message + "</body></html>");
            output.setFont(new Font("SansSerif", Font.PLAIN, 16));
            output.setBackground(isSent ? new Color(0xd0e6c9) : new Color(0xf5f0e6)); // Light green for sent, light gray for received
            output.setOpaque(true);
            output.setBorder(new EmptyBorder(10,10,10, 25));
            panel.add(output);
        } else {
            JLabel output = new JLabel(message);
            output.setFont(new Font("SansSerif", Font.PLAIN, 16));
            output.setBackground(isSent ? new Color(0xd0e6c9) : new Color(0xf5f0e6)); // Light green for sent, light gray for received
            output.setOpaque(true);
            output.setBorder(new EmptyBorder(10,10,10, 25));
            panel.add(output);
        }

        //to show the timestamp
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); 

        JLabel time = new JLabel();
        time.setText(sdf.format(cal.getTime()));
        time.setAlignmentX(Component.RIGHT_ALIGNMENT);

        panel.add(time);

        return panel;
    }
    public static void updateUserList(Set<String> users, String myUsername) {
        SwingUtilities.invokeLater(() -> {
            try{
                userSelector.removeAllItems();
                for (String user : users) {
                    if (!user.equals(myUsername)) {
                        userSelector.addItem(user);
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });
        System.out.println("Users received: " + users);
        System.out.println("ComboBox now has: " + userSelector.getItemCount() + " items");

    }
    

    public static void main(String[] args) {
        new Client();
    }
} 
