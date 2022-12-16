package server10;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;
import server10.ChatThread;
public class MyServer {
	static HashMap<String, ChatThread> hash= new HashMap<String, ChatThread>(); //id에 따른 클라이언트 정보
	String friend[];
	// 접속된 모든 클라이언트에게 메시지 msg 를 보냄
	public void broadcast(String msg) throws IOException {
		synchronized(hash){
				Iterator<String> keys = hash.keySet().iterator();
				while (keys.hasNext()){
					String key = keys.next();
					hash.get(key).sendMessage(msg);
				}
		}
	}
	public void friendcast(String id, String msg) throws IOException {//친구
		friend=id.split("/");
		for(int i=0; i<friend.length; i++) {
			hash.get(friend[i]).sendMessage(msg);
		}
	}
	public void unicast(String id, String msg) throws IOException {// 귓속말
		hash.get(id).sendMessage(msg);
	}
	// 종료시 clientVector에 저장되어 있는 클라이언트 정보를 제거
	public void removeClient(String id, ChatThread client) {
		synchronized(hash){
			hash.remove(id);// hashmap에서 제거
			System.gc();
		}
	}	
	// 처음 연결되었을 때 clientVector에 해당 클라이언트의 정보를 추가
	public void addClient(String id, ChatThread client) {
		synchronized(hash){
			hash.put(id, client); //hashmap에 추가
		}
	}
	//서버의 시작 메인 메소드
	public static void main(String[] args) {
		//서버 소켓
		ServerSocket myServerSocket= null;
		MyServer myServer= new MyServer();
		try{
			myServerSocket= new ServerSocket(6666);
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(-1);
		}
		System.out.println("[서버 대기 상태] "+ myServerSocket);
		try{
			//다수의 클라이언트 접속을 처리하기 위해 반복문으로 구현
			while(true){
				//클라이언트가 접속되었을 경우 이 클라이언트를 처리하기 위한 ChatThread 객체 생성
				ChatThread client= new ChatThread(myServer, myServerSocket.accept());
				//클라이언트에게 서비스를 제공하기 위한 쓰레드 동작
				client.start();
				int clientnumber = hash.size()+1;
				System.out.println("[현재 접속자수] "+ clientnumber +"명");
			}
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
}
