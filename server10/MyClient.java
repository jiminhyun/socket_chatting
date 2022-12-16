package server10;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.HashMap;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


import server10.MyClient;

public class MyClient extends JFrame implements ActionListener, Runnable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel jp;
	private Image background = new ImageIcon(MyClient.class.getResource("/Img/background.png")).getImage();
	private JLabel jLabel0 = new JLabel();
	private JLabel jLabel1 = new JLabel();
	private JTextField txtip = new JTextField();
    private JTextField txtid = new JTextField();
    private JButton btnconnect = new JButton();
    private JButton btncommand = new JButton();
    private JTextArea chatwindow = new JTextArea();
    private JScrollPane jScrollPane1 = new JScrollPane();
    private JScrollPane jScrollPane2 = new JScrollPane();
    private JScrollPane jScrollPane3 = new JScrollPane();
    private JTextField txtsend = new JTextField();
    private JLabel jLabel2 = new JLabel();
    private JLabel jLabel3 = new JLabel();
    private JLabel jLabel4 = new JLabel();
    private JLabel number = new JLabel();
    private JButton btnclose = new JButton();
    private JTextArea list = new JTextArea();
    private JTextArea list2 = new JTextArea();
    private JButton btncheck = new JButton();
    private BufferedReader in;
    private PrintWriter out;
    private Socket sc;
    static HashMap<String, String> friend = new HashMap<String, String>();
    int friendnumber=0; //ģ�� ��
    public MyClient() {
           try {
                  Init();
                  addListener();
           } catch (Exception e) {
                  e.printStackTrace();
           }
    }
    private void Init() throws Exception {
    	jp = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(background, 0, 0, null);
			}
		};
		   jp.setLayout(null);
		   jp.setBounds(0,0,660,610);
           this.setSize(new Dimension(660, 610));
           this.setTitle("���� ä�� ���α׷� NaTalk!");
           jLabel0.setText("IP:");
           jLabel0.setBounds(new Rectangle(15, 10, 45, 25));
           txtip.setBounds(new Rectangle(35, 10, 100, 25));
           jLabel1.setText("��ȭ��:");
           jLabel1.setBounds(new Rectangle(145, 10, 45, 25));
           txtid.setBounds(new Rectangle(190, 10, 135, 25));
           btnconnect.setText("����");
           btnconnect.setBounds(new Rectangle(330, 10, 60, 25));
           jScrollPane1.setBounds(new Rectangle(15, 40, 495, 490));
           txtsend.setBounds(new Rectangle(105, 535, 275, 25));
           jLabel2.setText("������ ���");
           jLabel2.setBounds(new Rectangle(545, 10, 75, 20));
           jLabel3.setText("ģ�� ���");
           jLabel3.setBounds(new Rectangle(550, 250, 75, 20));
           jLabel4.setText("�ο�:");
           jLabel4.setBounds(new Rectangle(530, 495, 35, 25));
           number.setText("0");
           number.setBounds(new Rectangle(565, 495, 50, 25));
           btnclose.setText("������");
           btnclose.setBounds(new Rectangle(530, 535, 100, 25));
           jScrollPane2.setBounds(new Rectangle(525, 40, 110, 200)); // ������ ���
           jScrollPane3.setBounds(new Rectangle(525, 280, 110, 200));
           btncheck.setText("ģ���� Ȯ��");
           btncheck.setBounds(new Rectangle(398, 10, 110, 25));
           btncommand.setText("��ɾ� ����");
           btncommand.setBounds(new Rectangle(398, 535, 110, 25));
           jp.add(btncheck, null);
           jp.add(list, null);
           jp.add(list2, null);
           jp.add(btnclose, null);
           jp.add(number, null);
           jp.add(jLabel4, null);
           jp.add(jLabel3, null);
           jp.add(jLabel2, null);
           jp.add(txtsend, null);
           jp.add(jLabel0, null);
           jp.add(txtip, null);
           jScrollPane1.getViewport().add(chatwindow, null);
           jScrollPane2.getViewport().add(list, null);
           jScrollPane3.getViewport().add(list2, null);
           jp.add(jScrollPane1, null);
           jp.add(jScrollPane2, null);
           jp.add(jScrollPane3, null);
           jp.add(btnconnect, null);
           jp.add(btncommand, null);
           jp.add(txtid, null);
           jp.add(jLabel1, null);
           this.getContentPane().add(jp);
           chatwindow.setEditable(false);
           list.setEditable(false);
           list2.setEditable(false);
           setResizable(false);
           setVisible(true);
    }
    public void addListener() {
    	txtid.addActionListener(this);
        txtsend.addActionListener(this);
        btncommand.addActionListener(this);
        btnconnect.addActionListener(this);
        btnclose.addActionListener(this);
        btncheck.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == txtid || e.getSource() == btnconnect) {
               //��ȭ�� �Է� �� ����
               if(txtid.getText().equals("")) {
                     JOptionPane.showMessageDialog(this, "��ȭ���� �Է��ϼ���");
                     txtid.requestFocus();
                     return;
               }
               try {
                     sc = new Socket(txtip.getText(), 6666);
                     in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
                     out = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()));
                     out.println("/a|" + txtid.getText());
                     out.flush();
                     new Thread(this).start();  //run()�� ȣ��
               } catch (Exception e2) {
                     System.out.println("���� ����:" + e2);
               }
        }else if(e.getSource() == txtsend) {
               //�޼��� ����, ���⼭ ���� ��ɾ� ����
               try {
            	   Calendar now = Calendar.getInstance();
            	   int hour = now.get(Calendar.HOUR_OF_DAY);
            	   int minute = now.get(Calendar.MINUTE);
            	   String info = "["+Integer.toString(hour)+":"+Integer.toString(minute)+"]";
                     //�޼���
            	   if(txtsend.getText().charAt(0) == '/') {//��ɾ� ���� �ڱ����� �����ֱ�
            		   chatwindow.append(txtsend.getText()+info+"\n");
            	   }
            	   if(txtsend.getText().charAt(1) == 'f' && txtsend.getText().charAt(2) == '|') {// ģ�� �ӼӸ� ó��
            		   String gat = "";
            		   Iterator<String> keys = friend.keySet().iterator();
            		   while (keys.hasNext()){
       					   String key = keys.next();
       					   gat = gat+key+"/";
       				   }
            		   out.println(txtsend.getText()+info+"|"+txtid.getText()+"|"+gat);
                       out.flush();
                       txtsend.setText("");
                       txtsend.requestFocus();
        		   }else if(txtsend.getText().charAt(1) == 'f' && txtsend.getText().charAt(2)
        				    == 'a' && txtsend.getText().charAt(3) == '|') {// ģ�� �ӼӸ� ó��
        			   StringTokenizer st= new StringTokenizer(txtsend.getText(), "|");
                       String command = st.nextToken();
                       String id = st.nextToken();
                       String gat = "";
                       list2.setText("");
                       friendnumber++;
                       friend.put(id,Integer.toString(friendnumber));
                       Iterator<String> keys = friend.keySet().iterator();
       				   while (keys.hasNext()){
       					   String key = keys.next();
       					   gat = gat+key+"\n";
       				   }
       				   list2.append(gat);
                       txtsend.setText("");
                       txtsend.requestFocus();
        		   }else if(txtsend.getText().charAt(1) == 'f' && txtsend.getText().charAt(2)
        				   == 'd' && txtsend.getText().charAt(3) == '|') {// ģ�� �ӼӸ� ó��
        			   StringTokenizer st= new StringTokenizer(txtsend.getText(), "|");
                       String command = st.nextToken();
                       String id = st.nextToken();
                       String gat = "";
                       list2.setText("");
                       friendnumber--;
                       friend.remove(id);
                       Iterator<String> keys = friend.keySet().iterator();
       				   while (keys.hasNext()){
       					   String key = keys.next();
       					   gat = gat+key+"\n";
       				   }
       				   list2.append(gat);
                       txtsend.setText("");
                       txtsend.requestFocus();
        		   }else {
        			   out.println(txtsend.getText()+info+"|"+txtid.getText());
                       out.flush();
                       txtsend.setText("");
                       txtsend.requestFocus();
        		   }
               } catch (Exception e2) {
                     System.out.println("�޼��� ���� ����:" + e2);
               }
        }else if(e.getSource() == btncheck) {
        	JOptionPane.showMessageDialog(this, "ģ����: "+friendnumber);
        }else if(e.getSource() == btncommand) {
        	JOptionPane.showMessageDialog(this,
        			"/w|�ӼӸ����|�ӼӸ�\n/f|ģ������ �ӼӸ�\n/fa|�̸����� ģ���߰�\n/fd|�̸����� ģ������\n/d|���̽� ������");
        }else if(e.getSource() == btnclose) {
               //������
               try {
                     out.println("/q|"+txtid.getText());
                     out.flush();
                     in.close();
                     out.close();
                     sc.close();
               } catch (Exception e2) {
                     System.out.println("������ ����:" + e2);
               } finally {
                     System.exit(0);
               }
        }
 }
 @Override
 public void run() {
        while (true) {
               try {
                     String msg = in.readLine();  //�����κ��� �޼��� ����
                     if(msg == null || msg.equals("")) return;
                     if(msg == "��ȭ���� �ߺ��Ǿ����ϴ�.") { //�����̳� ���ӿ��� ��ȭ�� �ߺ�
                    	 in.close();
                         out.close();
                         sc.close();
                     }else if(msg.charAt(0) == '/') {// �޽��� ��������
                    	 if(msg.charAt(1) == 'c') { //����
                    		 StringTokenizer st= new StringTokenizer(msg, "|");
                             String trash = st.nextToken();
                             String finalid = st.nextToken();
                             chatwindow.append(finalid + "���� �����߽��ϴ�.\n");
                    	 }else if(msg.charAt(1) == 'x') {//����� ��� ����
                    		 StringTokenizer st= new StringTokenizer(msg, "|");
                             int cnt=0;
                             String trash = st.nextToken();
                             list.setText("");
                             while (st.hasMoreTokens()) {
                            	 String id = st.nextToken();
                            	 list.append(id+"\n");
                            	 cnt++;
                             }
                             number.setText(String.valueOf(cnt));
                             txtip.setEditable(false);
                             txtid.setEditable(false);  //��ȭ�� �Է� �Ұ�
                             btnconnect.setEnabled(false);
                    	 }else if(msg.charAt(1) == 'q') { //����
                    		 StringTokenizer st= new StringTokenizer(msg, "|");
                             String trash = st.nextToken();
                             String finalid = st.nextToken();
                             chatwindow.append(finalid + "���� �����߽��ϴ�.\n");
                    	 }else if(msg.charAt(1) == 'z') {//����� ��� ����
                    		 StringTokenizer st= new StringTokenizer(msg, "|");
                             int cnt=0;
                             String trash = st.nextToken();
                             list.setText("");
                             while (st.hasMoreTokens()) {
                            	 String id = st.nextToken();
                            	 list.append(id+"\n");
                            	 cnt++;
                             }
                             number.setText(String.valueOf(cnt));
                    	 }
                     }else { //�Ϲ� �޼���
                    	 chatwindow.append(msg + "\n");//�ߺ��� ���⼭ Ȯ��
                     }
               }catch (Exception e) {
                         System.out.println("run err : " + e);
                   } 
               }
}
 
 public static void main(String args[]) {
        MyClient fr = new MyClient();
        fr.setLocation(200, 200);
        fr.setVisible(true);
        fr.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
 }
}