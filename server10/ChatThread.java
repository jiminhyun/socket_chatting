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
	MyServer myServer; // ChatServer 객체
	Socket mySocket; // 클라이언트 소켓
	PrintWriter out; // 입출력 스트림
	BufferedReader in;
	public ChatThread(MyServer server, Socket socket) { // 생성자
		super("ChatThread");
		myServer= server;
		mySocket= socket;
		out= null;
		in= null;
	}
	public void sendMessage(String msg) throws IOException { // 메시지를 전송
		out.println(msg);
		out.flush();
	}
	public void disconnect() { // 연결을 종료 
		try{
			out.flush();
			in.close();
			out.close();
			mySocket.close();
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
	public void run() { // 쓰레드 시작 
		try{
			// 소켓을 이용하여 상대방과 입출력 스트림을 생성
			out= new PrintWriter(new OutputStreamWriter(mySocket.getOutputStream()));
			in= new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
			while(true){ // 클라이언트 보낸 메시지를 처리하기 위해 기다림
				String inLine= in.readLine(); // 기다리고 있다가 클라이언트가 보낸 메시지가 있는 경우 읽어들임
				if(!inLine.equals("") && !inLine.equals(null)){
					 // 클라이언트가 보낸 메시지를 확인하여 현재 접속한 모든 클라이언트에게 브로드캐스트
					messageProcess(inLine);
				}
			}
		}catch(Exception e){
			disconnect();
		}
	}
	// 클라이언트가 보낸 메시지를 확인한 후 처리
	public void messageProcess(String msg) throws IOException {
		StringTokenizer st= new StringTokenizer(msg, "|");// 규칙에 따라 받은 메시지를 분리하여 확인
		String command= st.nextToken();
		if(command.equals("/a")) {
			String msg2 = "/x|";
			String id= st.nextToken();
			if(MyServer.hash.containsKey(id)) {
				out.println("대화명이 중복되었습니다.");
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
				}//여기까지 문제 없음
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
		}else if(command.equals("/f")) {//친구
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