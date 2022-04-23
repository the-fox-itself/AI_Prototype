package Libraries;

import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class Methods {
    public final static Dimension FRAME_SIZE = new Dimension((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());

    final public static String SEPARATOR = System.getProperty("file.separator");
    public final static Font f90 = new Font("", Font.PLAIN, 90);
    public final static Font f80 = new Font("", Font.PLAIN, 80);
    public final static Font f70 = new Font("", Font.PLAIN, 70);
    public final static Font f60 = new Font("", Font.PLAIN, 60);
    public final static Font f50 = new Font("", Font.PLAIN, 50);
    public final static Font f45 = new Font("", Font.PLAIN, 45);
    public final static Font f40 = new Font("", Font.PLAIN, 40);
    public final static Font f35 = new Font("", Font.PLAIN, 35);
    public final static Font f32 = new Font("", Font.PLAIN, 32);
    public final static Font f30 = new Font("", Font.PLAIN, 30);
    public final static Font f25 = new Font("", Font.PLAIN, 25);
    public final static Font f20 = new Font("", Font.PLAIN, 20);
    public final static Font f17 = new Font("", Font.PLAIN, 17);
    public final static Font f15 = new Font("", Font.PLAIN, 15);

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public final static int NOTE_TYPE_DONE = 0;
    public final static int NOTE_TYPE_PROCESS = 1;
    public final static int NOTE_TYPE_WARNING = 2;
    public final static int NOTE_TYPE_ERROR = 3;
    public final static int NOTE_TYPE_INFO = 4;

    public final static String DEFAULT_NOTE_SENDER = "War_Of_Cards";

    public static void printNote(String sender, String note, int type) {
        switch (type) {
            case NOTE_TYPE_DONE:
                System.out.print("<"+sender+"> "+note+" has successfully done.\n");
                break;
            case NOTE_TYPE_PROCESS:
                System.out.print("<"+sender+"> "+note+"...\n");
                break;
            case NOTE_TYPE_WARNING:
                System.out.print("<"+sender+"> "+note+"!\n");
                break;
            case NOTE_TYPE_ERROR:
                System.out.print("<"+sender+"> -ERROR- "+note+" has failed!\n");
                break;
            case NOTE_TYPE_INFO:
                System.out.print("<"+sender+"> -info- "+note+".\n");
                break;
        }
    }
    public static void printNote(String note, int type) {
        switch (type) {
            case NOTE_TYPE_DONE:
                System.out.println("<"+DEFAULT_NOTE_SENDER+"> "+note+" has successfully done.");
                break;
            case NOTE_TYPE_PROCESS:
                System.out.println("<"+DEFAULT_NOTE_SENDER+"> "+note+"...");
                break;
            case NOTE_TYPE_WARNING:
                System.out.println("<"+DEFAULT_NOTE_SENDER+"> "+note+"!");
                break;
            case NOTE_TYPE_ERROR:
                System.out.println("<"+DEFAULT_NOTE_SENDER+"> -ERROR- "+note+" has failed!");
                break;
            case NOTE_TYPE_INFO:
                System.out.println("<"+DEFAULT_NOTE_SENDER+"> -info- "+note+".");
                break;
        }
    }

    public static void lNull(JFrame frame) {
        frame.setLayout(null);
    }
    public static void lBord(JFrame frame) {
        frame.setLayout(new BorderLayout());
    }
    public static void lBox(JFrame frame, int axis) {
        frame.setLayout(new BoxLayout(frame.getContentPane(), axis));
    }
    public static void visTrue(Component component) {
        component.setVisible(true);
//        printNote("Displaying component [isVisible:true, component:"+component+"]", NOTE_TYPE_DONE);
    }
    public static void visFalse(Component component) {
        component.setVisible(false);
//        printNote("Hiding component [isVisible:false, component:"+component+"]", NOTE_TYPE_DONE);
    }
    public static void setComponent(Container container, Component component, Font font, int x, int y, int width, int height) {
        container.add(component);
        component.setFont(font);
        component.setBounds(x, y, width, height);
        visFalse(component);
    }
    public static void setComponent(Container container, Component component, Font font, Color colorBackground, Color colorForeground, int x, int y, int width, int height) {
        container.add(component);
        component.setFont(font);
        component.setBackground(colorBackground);
        component.setForeground(colorForeground);
        component.setBounds(x, y, width, height);
        visFalse(component);
    }
    public static void setComponentByAvailablePlaceOnFrame(JFrame frame, Component component, Font font, int x, int y) {
        setComponentByAvailablePlaceOnFrame(frame, component, font, x, y, 0, 0);
    }
    public static void setComponentByAvailablePlaceOnFrame(JFrame frame, Component component, Font font, int x, int y, int widthPlus, int heightPlus) {
        component.setFont(font);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        frame.add(panel);
        panel.setBounds(-frame.getWidth(), -frame.getHeight(), frame.getWidth(), frame.getHeight());
        panel.add(component);
        frame.setEnabled(false);
        visTrue(frame);

        frame.add(component);
//        System.out.println(x + " " + y + " " + component.getWidth() + " " + component.getHeight());
        component.setBounds(x, y, component.getWidth()+widthPlus, component.getHeight()+heightPlus);

        frame.remove(panel);

        visFalse(component);
        frame.setEnabled(true);
    }
    public static int[] getNeededBoundsForComponent(Component component) {
        int[] returnInt = new int[2];

        JFrame frame = new JFrame();
        frame.setSize(1000, 1000);
        frame.setLayout(new FlowLayout());
        frame.add(component);
        frame.setEnabled(false);
        visTrue(frame);
        returnInt[0] = component.getWidth();
        returnInt[1] = component.getHeight();
        visFalse(frame);

        return returnInt;
    }


//    public static void makeRecentlySave() {
//        if (accountCurrent != null) {
//            try {
//                printNote("Making recently save", NOTE_TYPE_PROCESS);
//                FileOutputStream fileOutputStream = new FileOutputStream("saves"+SEPARATOR+"recentlySave.ser");
//                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//                String time = (LocalDateTime.now() + "").split("T")[0]; //2020-04-30T15:13:52.202096300
//                objectOutputStream.writeObject(accountCurrent.nick + "\n" + time);
//                System.out.println(accountCurrent.nick + "\n" + time);
//                objectOutputStream.close();
//                printNote("Making recently save", NOTE_TYPE_DONE);
//            } catch (Exception ex) {
//                printNote("Making recently save", NOTE_TYPE_ERROR);
//                ex.printStackTrace();
//            }
//        }
//    }
//    public static void deleteRecentlySave() {
//        try {
//            printNote("Deleting recently save", NOTE_TYPE_PROCESS);
//            FileOutputStream fileOutputStream = new FileOutputStream("saves"+SEPARATOR+"recentlySave.ser");
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
//            objectOutputStream.close();
//            printNote("Deleting recently save", NOTE_TYPE_DONE);
//        } catch (Exception ex) {
//            printNote("Deleting recently save", NOTE_TYPE_ERROR);
//            ex.printStackTrace();
//        }
//    }
//    public static boolean getRecentlySave() {
//        try {
//            printNote("Finding recently save", NOTE_TYPE_PROCESS);
//            FileInputStream fileInputStream = new FileInputStream("saves"+SEPARATOR+"recentlySave.ser");
//            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
//            String[] object = ((String) objectInputStream.readObject()).split("\n");
//            String name = object[0];
//            String time = object[1];
//            int year = Integer.parseInt(time.split("-")[0]);
//            int month = Integer.parseInt(time.split("-")[1]);
//            int day = Integer.parseInt(time.split("-")[2]);
//            int currentYear = Integer.parseInt((LocalDateTime.now()+"").split("T")[0].split("-")[0]);
//            int currentMonth = Integer.parseInt((LocalDateTime.now()+"").split("T")[0].split("-")[1]);
//            int currentDay = Integer.parseInt((LocalDateTime.now()+"").split("T")[0].split("-")[2]);
//            objectInputStream.close();
//
//            if ((currentDay - day < 5 && currentDay - day > -1 && currentYear == year && currentMonth == month) || (currentDay - day > -31 && currentDay - day < -25 && currentYear == year && currentMonth - month == 1)) {
//                FileInputStream fileInputStreamAccount = new FileInputStream("saves"+SEPARATOR+name+".ser");
//                ObjectInputStream objectInputStreamAccount = new ObjectInputStream(fileInputStreamAccount);
//                accountCurrent = (Account) objectInputStreamAccount.readObject();
//                objectInputStreamAccount.close();
//
//                registered = true;
//                printNote("Finding recently save [year:"+year+", month:"+month+", day:"+day+", nick:"+accountCurrent.nick+"]", NOTE_TYPE_DONE);
//                return true;
//            } else {
//                printNote("Finding recently save [year:"+year+", month:"+month+", day:"+day+", nick:"+accountCurrent.nick+"]", NOTE_TYPE_DONE);
//                printNote("Recently save has expired", NOTE_TYPE_INFO);
//                return false;
//            }
//        } catch (Exception ex) {
//            printNote("Finding recently save", NOTE_TYPE_ERROR);
//            ex.printStackTrace();
//        }
//        return false;
//    }

    public static JFrame getFrame(String name, Image icon, int width, int height, LayoutManager layout, Component locationRelativeTo, boolean isResizable) {
        JFrame returnFrame = new JFrame(name);
        returnFrame.setSize(width, height);
        returnFrame.setIconImage(icon);
        returnFrame.setLayout(layout);
        returnFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        returnFrame.setLocationRelativeTo(locationRelativeTo);
        returnFrame.setResizable(isResizable);

        return returnFrame;
    }
    public static JButton getButtonNullLayout(String text, Font font, JFrame frameAdd, int x, int y, int width, int height, ActionListener actionListener, Color background, Color foreground) {
        JButton returnButton = new JButton(text);
        returnButton.setFont(font);
        frameAdd.add(returnButton);
        returnButton.setBounds(x, y, width, height);
        returnButton.addActionListener(actionListener);
        returnButton.setBackground(background);
        returnButton.setForeground(foreground);
        visFalse(returnButton);

        return returnButton;
    }
    public static JLabel getLabelNullLayout(String text, Font font, JFrame frameAdd, int x, int y, Color foreground) {
        JLabel returnLabel = new JLabel(text);
        returnLabel.setFont(font);
        frameAdd.add(returnLabel, 1, 0);
        returnLabel.setBounds(x, y, returnLabel.getPreferredSize().width, returnLabel.getPreferredSize().height);
        returnLabel.setFont(font);
        returnLabel.setForeground(foreground);
        visFalse(returnLabel);

        return returnLabel;
    }
    public static JTextField getTextFieldNullLayout(String text, Font font, JFrame frameAdd, int x, int y, int width, int height, Color background, Color foreground) {
        JTextField returnText = new JTextField(text);
        returnText.setFont(font);
        frameAdd.add(returnText);
        returnText.setBounds(x, y, width, height);
        returnText.setBackground(background);
        returnText.setForeground(foreground);

        return returnText;
    }
    public static JTextArea getTextAreaNullLayout(String text, Font font, JFrame frameAdd, int x, int y, int width, int height, Color background, Color foreground) {
        JTextArea returnText = new JTextArea(text);
        returnText.setFont(font);
        frameAdd.add(returnText);
        returnText.setBounds(x, y, width, height);
        returnText.setBackground(background);
        returnText.setForeground(foreground);

        return returnText;
    }
    public static JList<String> getList(String[] listOfValues, Font font, int visibleRowsCount, int selectionMode, ListSelectionListener selectionListener) {
        JList<String> returnList = new JList<>(listOfValues);
        returnList.setFont(font);
        returnList.setVisibleRowCount(visibleRowsCount);
        returnList.setSelectionMode(selectionMode);
        returnList.addListSelectionListener(selectionListener);
        visFalse(returnList);

        return returnList;
    }
    public static JScrollPane getScrollPaneNullLayout(Component componentOn, Container container, int x, int y, int width, int height, int verticalScrollBarPolicy, int horizontalScrollBarPolicy) {
        JScrollPane returnScrollPane = new JScrollPane(componentOn);
        container.add(returnScrollPane);
        returnScrollPane.setBounds(x, y, width, height);
        returnScrollPane.setVerticalScrollBarPolicy(verticalScrollBarPolicy);
        returnScrollPane.setHorizontalScrollBarPolicy(horizontalScrollBarPolicy);
        visFalse(returnScrollPane);

        return returnScrollPane;
    }
    public static JComboBox<String> getComboBoxNullLayout(String[] listOfValues, Font font, Container container, int x, int y, int width, int height, ActionListener actionListener) {
        JComboBox<String> returnComboBox = new JComboBox<>(listOfValues);
        returnComboBox.setFont(font);
        container.add(returnComboBox);
        returnComboBox.setBounds(x, y, width, height);
        returnComboBox.addActionListener(actionListener);
        visFalse(returnComboBox);

        return returnComboBox;
    }
    public static JCheckBox getCheckBoxNullLayout(String text, Font font, Container container, int x, int y, int width, int height, ActionListener actionListener) {
        JCheckBox returnCheckBox = new JCheckBox(text);
        returnCheckBox.setFont(font);
        container.add(returnCheckBox);
        returnCheckBox.setBounds(x, y, width, height);
        returnCheckBox.addActionListener(actionListener);

        return returnCheckBox;
    }

    public static int GCD(int a,int b){
        return b == 0 ? a : GCD(b,a % b);
    }
    public static int LCM(int a,int b){
        return a / GCD(a,b) * b;
    }
    public static int LCM(int a, int b, int c) {
        return LCM(a,LCM(b,c));
    }

    public static String readLine() {
        System.out.print("<User> ");
        String returnString = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            returnString = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    public static Object getFromClient(ServerSocket serverSocket) {
        try {
            Socket socket = serverSocket.accept();
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            Object returnObject = inputStream.readObject();
            inputStream.close();
            return returnObject;
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    public static void sendToClient(ServerSocket serverSocket, Object send) {
        try {
            Socket socket = serverSocket.accept();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(send);
            objectOutputStream.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

//    public static Object getFromServer(String host, int port, String action) {
//        try {
//            printNote("Getting object from the server", NOTE_TYPE_PROCESS);
//            Socket socket = new Socket(host, port);
//            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
//            Object returnObject = inputStream.readObject();
//            inputStream.close();
//
//            printNote("Getting object [returnObject:"+returnObject+"] from the server [host:"+host+", port:"+port+"]", NOTE_TYPE_DONE);
//            return returnObject;
//        } catch (IOException | SecurityException | ClassNotFoundException ex) {
//            ex.printStackTrace();
//            switch (action) {
//                case "reg":
//                case "login":
//                    labelWarning.setFont(f17);
//                    labelWarning.setText("Сервер не отвечает!");
//                    visFalse(labelWarning);
//                    visTrue(labelWarning);
//                    break;
//                case "save":
//                    labelSaveAccount.setText("Сервер не отвечает!");
//                    break;
//                default:
//                    System.out.println("Сервер не отвечает!");
//                    break;
//            }
//            printNote("Getting object [host:"+host+", port:"+port+"] from the server", NOTE_TYPE_ERROR);
//            ex.printStackTrace();
//        }
//        return null;
//    }
//    public static void sendToServer(String host, int port, Object sendObject) {
//        try {
//            printNote("Sending object to the server", NOTE_TYPE_PROCESS);
//            Socket socket = new Socket(host, port);
//            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//            objectOutputStream.writeObject(sendObject);
//            objectOutputStream.close();
//
//            printNote("Sending object [sendObject:"+sendObject+"] to the server [host:"+host+", port:"+port+"]", NOTE_TYPE_DONE);
//        } catch (IOException ex) {
//            printNote("Sending object [sendObject:"+sendObject+"] to the server [host:"+host+", port:"+port+"]", NOTE_TYPE_ERROR);
//            ex.printStackTrace();
//        }
//    }

    public static void playSound(File soundFile) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);

            FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            vc.setValue(1);

            clip.setFramePosition(0);
            clip.start();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException exc) {
            exc.printStackTrace();
        }
    }
}