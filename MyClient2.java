import java.net.*;  
import java.io.*;  
public class MyClient2{  
public static void main(String args[]){  
Socket s= null;

try{  

int serverPort = 50000;
s= new Socket("localhost",serverPort);
BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
DataOutputStream out = new DataOutputStream(s.getOutputStream());  
BufferedReader br=new BufferedReader(new InputStreamReader(System.in)); 

out.write(("HELO\n").getBytes());   // sends HELO msg to server
String data = in.readLine();  
System.out.println("Recieved: "+ data);  

String username = System.getProperty("user.name");
out.write(("AUTH: " + username + "\n").getBytes()); // sends AUTH msg to server
data = in.readLine();
System.out.println("Recieved: " + data);

while(data!=("NONE\n"))
{
out.write(("REDY\n").getBytes());  // sends redy msg to server only when received ok from server for auth
data = in.readLine();  
System.out.println("Recieved: " + data);


out.write(("GETS All\n").getBytes()); // sends GET msg to server
String mess = in.readLine();
System.out.println("Recieved: " + mess);

out.write(("OK\n").getBytes()); // sends OK msg to server

String [] ArrOfMess = mess.split(" ",3);
System.out.println("Recieved: " + ArrOfMess[1]);

 int nRecs = Integer.valueOf(ArrOfMess[1]);


// int largest = recSize[0];
//for (int i = 0; i < nRecs; ++i){
//	for (int j= 0, j < recSize; ++j){
//	 if (recSize[j] > largest)
  //              largest = recSize[j];
	}
//}

//out.write(("OK\n").getBytes()); // sends OK msg to server
//String data = in.readLine();

//if (job = JOBN){
//out.write(("SCHD\n").getBytes());
//	}
} 

//out.write(("QUIT\n").getBytes()); // sends QUIT msg to server
//String data = in.readLine();  
//System.out.println("Recieved: " + data);


catch (UnknownHostException e){ System.out.println("Sock: " + e.getMessage());}

catch (EOFException e){ System.out.println("EOF: " + e.getMessage());}

catch (IOException e){ System.out.println("IO: " + e.getMessage());}

finally {if (s!=null) try {s.close();
}
catch (IOException e){System.out.println("close: " + e.getMessage());}}
}
}  
