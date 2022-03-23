package br.edu.ifpb.gugawag.so.sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Servidor2 {

    public static void main(String[] args) throws IOException {
        System.out.println("== Servidor ==");
        Path dir = Paths.get("C:\\Users\\rebek\\AndroidStudioProjects\\LayoutCor\\-nfs-sockets\\src\\br\\edu\\ifpb\\gugawag\\so\\Arquivos");

        // Configurando o socket
        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        // pegando uma referência do canal de saída do socket. Ao escrever nesse canal, está se enviando dados para o
        // servidor
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        // pegando uma referência do canal de entrada do socket. Ao ler deste canal, está se recebendo os dados
        // enviados pelo servidor
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // laço infinito do servidor
        while (true) {
           System.out.println("Cliente: " + socket.getInetAddress());
           String mensagem = dis.readUTF();
           DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*");

           List<String> arqs = new ArrayList<>();
            if (mensagem.equals("readdir")){
                for (Path file : stream) {
                    arqs.add(file.getFileName().toString());
                }
                dos.writeUTF(arqs.toString());                
            }  
            
            else if (mensagem.equals("create")){
                dos.writeUTF("Digite o nome do arquivo: ");
                String novoArq = dis.readUTF();          
                File file = new File(dir +"\\" + novoArq + ".txt");
                file.createNewFile();
                dos.writeUTF("Arquivo foi criado com sucesso"); 
            } 
            
            else if (mensagem.equals("remove")){
	            dos.writeUTF("Informe o nome do arquivo a ser removido: ");
	            String arqRmv = dis.readUTF() + ".txt";   		
	            for(Path i: stream) {
	                String nomeArq = i.getFileName().toString();
	                if(nomeArq.equals(arqRmv)) {
	                    i.toFile().delete();
	                    dos.writeUTF("O arquivo foi removido");                                
	                
	            }
            }
        }

        System.out.println(mensagem);
        }
        /*
         * Observe o while acima. Perceba que primeiro se lê a mensagem vinda do cliente (linha 29, depois se escreve
         * (linha 32) no canal de saída do socket. Isso ocorre da forma inversa do que ocorre no while do Cliente2,
         * pois, de outra forma, daria deadlock (se ambos quiserem ler da entrada ao mesmo tempo, por exemplo,
         * ninguém evoluiria, já que todos estariam aguardando.
         */
    }
}
