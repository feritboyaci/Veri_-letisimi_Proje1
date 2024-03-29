import java.net.*;
import java.io.*;
import java.util.*;

public class FileClient {

    private static Socket sock;
    private static String fileName;
    private static BufferedReader stdin;
    private static PrintStream os;

    public static void main(String[] args) throws IOException {
while(true) {
        try {
            sock = new Socket("78.190.190.120 ", 2020);
            stdin = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.err.println("Sunucuya bağlanilamiyor, daha sonra tekrar deneyin.");
            System.exit(1);
        }

        os = new PrintStream(sock.getOutputStream());

        try {
              switch (Integer.parseInt(selectAction())) {
            case 1:
                os.println("1");
                sendFile();
                continue;
            case 2:
                os.println("2");
                System.out.print("dosya giriniz: ");
                fileName = stdin.readLine();
                os.println(fileName);
                receiveFile(fileName);
                continue;
	   case 3:
		 sock.close();
		System.exit(1);
        }
        } catch (Exception e) {
            System.err.println("geçerli giriş yapınız");
        }

}
       
    }

    public static String selectAction() throws IOException {
        System.out.println("1. dosya gonder.");
        System.out.println("2. dosya al.");
	System.out.println("3. cik.");
        System.out.print("\nsecim yap: ");

        return stdin.readLine();
    }


    public static void sendFile() {
        try {
            System.out.print("dosya giriniz: ");
            fileName = stdin.readLine();

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];
	    if(!myFile.exists()) {
		System.out.println("dosya yok...");
		return;
		}

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);
		
            OutputStream os = sock.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File "+fileName+" sent to Server.");
        } catch (Exception e) {
            System.err.println("Exceptionnnn: "+e);
        }
    }

    public static void receiveFile(String fileName) {
        try {
            int bytesRead;
            InputStream in = sock.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(fileName);
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            in.close();

            System.out.println("File "+fileName+" received from Server.");
        } catch (IOException ex) {
		System.out.println("Exception: "+ex);
         }
    
}
}
