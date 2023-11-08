package main.java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class RequestHandler extends Thread {
    Socket socket;

    // pass in the paxos server socket
    public RequestHandler(Socket soc, Map<Integer, Queue<String>> CouncilMemberState) {
        this.socket = soc;
        try {
            DataInputStream readRequest = new DataInputStream(socket.getInputStream());
            DataOutputStream sendResponse = new DataOutputStream(socket.getOutputStream());

            sendResponse.writeUTF(processRequest(readRequest.readUTF(), CouncilMemberState));

            sendResponse.flush();
            sendResponse.close();
            readRequest.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getValue(String requestBody, String key) {
        if (requestBody != null) {
            String[] req = requestBody.split(",");

            for (int i = 0; i < req.length; i++) {
                if (req[i].contains(key)) {
                    return req[i].split(":")[1];
                }
            }
        }
        return key + ":null";
    }

    // here we process the requests from the council members
    // and disseminate the messages to the correct members
    public String processRequest(String req, Map<Integer, Queue<String>> CouncilMemberState) {
        if (getValue(req, "ACTION").contains("GETSTATE")) {
            // members can retrieve their state/messages.
            // that way they can
            // System.out.println("GETSTATE: " + req);
            Integer member = Integer.parseInt(getValue(req, "FROM_MEMBER"));
            if (CouncilMemberState.get(member).size() != 0) {
                return CouncilMemberState.get(member).poll();
            }
            return "ACTION:ERROR";
        }
        if (getValue(req, "ACTION").contains("PREPARE")) {
            System.out.println("PREPARE: " + req);
            // phase 1 part 1: the proposals are sent out to all acceptors council members
            for (int councilMember = 1; councilMember <= 9; councilMember++) {
                CouncilMemberState.get(councilMember).add(req);
            }
        }

        if (getValue(req, "ACTION").contains("PROMISE")) {
            System.out.println("PROMISE: " + req);
            // phase 1 part 2: if the acceptors see that the proposal value proposed by the
            // proposer is higher than their previously accepted proposal, they send a
            // PROMISE request to the proposer to not accept any new proposals with the
            // number lower than the one sent by the proposer.
            Integer proposer1 = Integer.parseInt(getValue(req, "TO_MEMBER"));
            CouncilMemberState.get(proposer1)
                    .add("ACTION:PROMISE_ACCEPTED,FROM_MEMBER:" + getValue(req, "FROM_MEMBER") +
                            ",TO_MEMBER:"
                            + getValue(req, "TO_MEMBER") + ",VOTE:" + getValue(req, "VOTE") +
                            ",PROPOSAL_NUMBER:"
                            + getValue(req, "PROPOSAL_NUMBER"));
        }

        if (getValue(req, "ACTION").contains("ACCEPT")) {
            System.out.println("ACCEPT: " + req);
            // phase 1 part 2: send the accept request to all members
            for (int i = 1; i <= 9; i++) {
                CouncilMemberState.get(i).add(req);
            }
        }

        return "ACTION:ERROR";

    }
}
