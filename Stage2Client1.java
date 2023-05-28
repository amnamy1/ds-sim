import java.net.*;  
import java.io.*;  
class Stage2Client1{  
public static void main(String args[]){  

Socket s= null; //create socket 

try{

int serverPort = 50000;  //connect to ds-server
s= new Socket("localhost",serverPort); //initialize 
System.out.println("Port number: "+s.getPort());  

BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
DataOutputStream out=new DataOutputStream(s.getOutputStream());  

out.write(("HELO\n").getBytes());                                //send HELO msg to server
String serverReply = in.readLine();                                     //recieve OK 
//System.out.println("helo reply: " + serverReply);//11111111111

String username = System.getProperty("user.name");               
out.write(("AUTH " + username + "\n").getBytes());              //send AUTH msg to server 
serverReply = in.readLine();                                     //recieve OK 
//System.out.println("auth reply: " + serverReply);//11111111111


while(!serverReply.equals("NONE")){                              //While last message is not NONE

int nRecs = 0;
boolean BestSerFound = false;
String BestServer = "";
int serverID= 0;
String FirstServ = "";
String FirstServID = "";

out.write(("REDY\n").getBytes());                                //Send REDY to recieve a job	
serverReply = in.readLine();                                     //receive JOBN OR NONE
//System.out.println("redy reply: " + serverReply);//11111111111

String [] job  = serverReply.split(" ", -1); // arrr separating JOBTYPE
//System.out.println("JOB Type =" + job[0]);

if (job[0].equals("JCPL")){ // if job type is JCPL send redy again
}
 else if(!serverReply.equals("NONE")){  // if JOBN continue with while loop
String jobID = job[2]; // finding jobID

//System.out.println("checkkk111");

if (BestSerFound == false){
//System.out.println(BestSerFound);//11111111111
	String [] jobInfo = serverReply.split(" ",-1);
	int jobCore = Integer.parseInt(jobInfo[4]);
	String jobMemory = jobInfo[5];
	String jobDisk = jobInfo[6];	
	out.write(("GETS Capable " + jobCore + " " + jobMemory +" "+ jobDisk + "\n").getBytes()); //Send GETS Capable message

	serverReply = in.readLine();                             //Recieve DATA nRecs recSize	
	
//	System.out.println("GETS is: "+ serverReply);   //1111111111	

	String [] data  = serverReply.split(" ", -1);             //splits DATA into arr
	
	nRecs = Integer.parseInt(data[1]);                    //make split into Integer and stores into nRecs

//	System.out.println("nRecs is:"+ data[1]);        //111111111111         

	out.write(("OK\n").getBytes());                          //Send OK

//	System.out.println("latest msg is:"+ serverReply);//11111111111
	

	      
	for (int i = 0; i < nRecs; i++){ 
	serverReply = in.readLine();     //recieve records
		String [] arr3 = serverReply.split(" ", -1);   //splits the record read into arr3
//		System.out.println("Records: "+ serverReply);//11111111111
		int Rjob = Integer.parseInt(arr3[7]);
		int Wjob = Integer.parseInt(arr3[8]);
		int Tjob = Rjob + Wjob;
		int recCore = Integer.parseInt(arr3[4]);
		//System.out.println("no. of jobs = "+  Tjob);
		
		//if (BestSerFound = false){
	
		//System.out.println(jobCore + "and" + recCore);
		//if  (recCore >=  jobCore){
		//System.out.println("serv has enough cores" );
		//}
		
		//if  (Tjob == 0){
		//System.out.println("no waiting or running jobs" );
		//}
		
           		if  (BestServer == "" && recCore >=  jobCore) {                
				//System.out.println("looking for best server");
				BestServer = arr3[0];
				serverID = Integer.parseInt(arr3[1]);
				//System.out.println("bestserv = " +  BestServer + serverID);	
					if ( BestServer != "" ){
						BestSerFound = true;
					}
			}
		//}		
		if (i == 0){
			//System.out.println("looking for first server");
			FirstServ = arr3[0];
			FirstServID = arr3[1];
			//System.out.println("firstserv = " + FirstServ + FirstServID );			
		}
		
}  //end for 

   
out.write(("OK\n").getBytes());                          //Send OK
//System.out.println("check2: ");//11111111111
serverReply = in.readLine();                            //recieve . 
//System.out.println("check2.5 " + serverReply);//11111111111
 		
}//end bestserv

//System.out.println("checkkk1.5 " + BestServer + serverID);//11111111111

//System.out.println("check3: ");//11111111111
			
if ( job[0].equals("JOBN") & BestSerFound == false ){
	out.write(("SCHD "+jobID+" "+ FirstServ +" "+ FirstServID+"\n").getBytes()); //send SCHD 0 joon 0  
	//System.out.println("used first server");
	} //end if 
			
else if ( job[0].equals("JOBN") & BestSerFound == true ){
	out.write(("SCHD "+jobID+" "+ BestServer +" "+serverID+"\n").getBytes()); //send SCHD 0 joon 0  
	//System.out.println("used best server");
	} //end else 
	serverReply = in.readLine();   // recieve ok
//	System.out.println("last msg: " + serverReply);//11111111111
BestSerFound = false;
}
} // end WHILE

out.write(("QUIT\n").getBytes());                               //Send QUIT
serverReply = in.readLine();                                 //recieve QUIT
s.close();                                                    //close socket
}

				
catch (UnknownHostException e){ System.out.println("Sock: " + e.getMessage());}

catch (EOFException e){ System.out.println("EOF: " + e.getMessage());}

catch (IOException e){ System.out.println("IO: " + e.getMessage());}

finally {if (s!=null){ try {s.close();}
catch (IOException e){System.out.println("close: " + e.getMessage());}}
}}}  
