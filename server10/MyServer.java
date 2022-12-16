package server10;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Iterator;
import server10.ChatThread;
public class MyServer {
	static HashMap<String, ChatThread> hash= new HashMap<String, ChatThread>(); //id�� ���� Ŭ���̾�Ʈ ����
	String friend[];
	// ���ӵ� ��� Ŭ���̾�Ʈ���� �޽��� msg �� ����
	public void broadcast(String msg) throws IOException {
		synchronized(hash){
				Iterator<String> keys = hash.keySet().iterator();
				while (keys.hasNext()){
					String key = keys.next();
					hash.get(key).sendMessage(msg);
				}
		}
	}
	public void friendcast(String id, String msg) throws IOException {//ģ��
		friend=id.split("/");
		for(int i=0; i<friend.length; i++) {
			hash.get(friend[i]).sendMessage(msg);
		}
	}
	public void unicast(String id, String msg) throws IOException {// �ӼӸ�
		hash.get(id).sendMessage(msg);
	}
	// ����� clientVector�� ����Ǿ� �ִ� Ŭ���̾�Ʈ ������ ����
	public void removeClient(String id, ChatThread client) {
		synchronized(hash){
			hash.remove(id);// hashmap���� ����
			System.gc();
		}
	}	
	// ó�� ����Ǿ��� �� clientVector�� �ش� Ŭ���̾�Ʈ�� ������ �߰�
	public void addClient(String id, ChatThread client) {
		synchronized(hash){
			hash.put(id, client); //hashmap�� �߰�
		}
	}
	//������ ���� ���� �޼ҵ�
	public static void main(String[] args) {
		//���� ����
		ServerSocket myServerSocket= null;
		MyServer myServer= new MyServer();
		try{
			myServerSocket= new ServerSocket(6666);
		}catch(IOException e){
			System.out.println(e.toString());
			System.exit(-1);
		}
		System.out.println("[���� ��� ����] "+ myServerSocket);
		try{
			//�ټ��� Ŭ���̾�Ʈ ������ ó���ϱ� ���� �ݺ������� ����
			while(true){
				//Ŭ���̾�Ʈ�� ���ӵǾ��� ��� �� Ŭ���̾�Ʈ�� ó���ϱ� ���� ChatThread ��ü ����
				ChatThread client= new ChatThread(myServer, myServerSocket.accept());
				//Ŭ���̾�Ʈ���� ���񽺸� �����ϱ� ���� ������ ����
				client.start();
				int clientnumber = hash.size()+1;
				System.out.println("[���� �����ڼ�] "+ clientnumber +"��");
			}
		}catch(IOException e){
			System.out.println(e.toString());
		}
	}
}
