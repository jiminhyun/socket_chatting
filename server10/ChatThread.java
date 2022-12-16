package server10;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Random;
import java.util.StringTokenizer;

public class ChatThread extends Thread {
	MyServer myServer; // ChatServer ��ü
	Socket mySocket; // Ŭ���̾�Ʈ ����
	PrintWriter out; // ����� ��Ʈ��
	BufferedReader in;
	public ChatThread(MyServer server, Socket socket) { // ������
		super("ChatThread");
		myServer= server;
		mySocket= socket;
		out= null;
		in= null;
	}
	public void sendMessage(String msg) throws IOException { // �޽����� ����
		out.println(msg);
		out.flush();
	}
	public void disconnect() { // ������ ���� 
		try{
			out.flush();
			in.close();
			out.close();
			mySocket.close();
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
	public void run() { // ������ ���� 
		try{
			// ������ �̿��Ͽ� ����� ����� ��Ʈ���� ����
			out= new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			in= new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			while(true){ // Ŭ���̾�Ʈ ���� �޽����� ó���ϱ� ���� ��ٸ�
				String inLine= in.readLine(); // ��ٸ��� �ִٰ� Ŭ���̾�Ʈ�� ���� �޽����� �ִ� ��� �о����
				if(!inLine.equals("") && !inLine.equals(null)){
					 // Ŭ���̾�Ʈ�� ���� �޽����� Ȯ���Ͽ� ���� ������ ��� Ŭ���̾�Ʈ���� ��ε�ĳ��Ʈ
					messageProcess(inLine);
				}
			}
		}catch(Exception e){
			disconnect();
		}
	}
	// Ŭ���̾�Ʈ�� ���� �޽����� Ȯ���� �� ó��
	public void messageProcess(String msg) throws IOException {
		StringTokenizer st= new StringTokenizer(msg, "|");// ��Ģ�� ���� ���� �޽����� �и��Ͽ� Ȯ��
		String command= st.nextToken();
		if(command.equals("/a")) {
			String msg2 = "/x|";
			String id= st.nextToken();
			if(MyServer.hash.containsKey(id)) {
				out.println("��ȭ���� �ߺ��Ǿ����ϴ�.");
				disconnect();
			}else {
				myServer.broadcast("/c|"+id);
				myServer.addClient(id, this);
				synchronized(MyServer.hash){
					Iterator<String> keys = MyServer.hash.keySet().iterator();
					while (keys.hasNext()){
						String key = keys.next();
						String ary = key+"|";
						msg2=msg2+ary;
					}
				}//������� ���� ����
				myServer.broadcast(msg2);
			}
		}else if(command.equals("/d")) {
			Random rd = new Random();
			int dice2 = rd.nextInt(6)+1;
			String dice = Integer.toString(dice2);
			String info = st.nextToken();
			String id= st.nextToken();
			myServer.broadcast(id+">dice: "+dice+info);
		}else if(command.equals("/w")) {
			try {
			String whisperid= st.nextToken();
			String talk= st.nextToken(); 
			String id= st.nextToken();
			myServer.unicast(whisperid, id+">"+talk);
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}else if(command.equals("/f")) {//ģ��
			try {
			String talk= st.nextToken();
			String id= st.nextToken(); 
			String friend= st.nextToken();
			myServer.friendcast(friend, id+">"+talk);
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}else if(command.equals("/q")) {
			String msg2 = "/z|";
			String id= st.nextToken();
			myServer.broadcast("/q|"+id);
			myServer.removeClient(id, this);
			synchronized(MyServer.hash){
				Iterator<String> keys = MyServer.hash.keySet().iterator();
				while (keys.hasNext()){
					String key = keys.next();
					String ary = key+"|";
					msg2=msg2+ary;
				}
			}
			myServer.broadcast(msg2);
			disconnect();
		}else{
			try{
				String id= st.nextToken();
				myServer.broadcast(id+">"+command);
			}catch(IOException e){
				System.out.println(e.toString());
			}
		}
	
	}
}