package main.java;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class PaxosServer {
    private static Integer port = 3000;
    // private static Integer ID = 0;
    private static RequestHandler rh;
    // private static Integer noOfMembers = 0;
    // // initialise the paxos messaging server
    // public PaxosServer(int id, int p, int no_of_members) {
    //     ID = id;
    //     port = p;
    //     noOfMembers = no_of_members;
    // }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket sock = null;
        Map<Integer, Queue<String>> councilMemberState = new HashMap<Integer, Queue<String>>();

        for (int i = 1; i <= 9; i++) {
            councilMemberState.put(i, new LinkedList<>());
        }

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Paxos server has started on port " + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                sock = serverSocket.accept();
                rh = new RequestHandler(sock, councilMemberState);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
