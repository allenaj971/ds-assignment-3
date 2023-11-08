package main.java;

import java.io.*;
import java.net.Socket;

public class CouncilMember extends Thread {
    private int ID;
    private int highest_numbered_proposal; // Acceptance phase: the proposal number accepted by acceptor.
    private int no_of_promises_accepted;
    private int max_proposal_no_from_promises;
    private int accepted_vote; // Acceptance phase: number of members accepting vote.
    private int delay;

    private Socket psrequest;
    private DataInputStream readServerResponse;
    private DataOutputStream sendCouncilMemberRequest;

    public CouncilMember(int id) {
        no_of_promises_accepted = 0;
        max_proposal_no_from_promises = 0;
        highest_numbered_proposal = 0;
        accepted_vote = 0;
        ID = id;
        delay = 0;
    }

    public void delay(Integer milliseconds) {
        delay = milliseconds;
    }

    @Override
    public void run() {
        // since the server stores the Member's state, we need to get the state
        // every second so that the requests can be fulfilled.
        while (true) {
            try {
                Thread.sleep(50 + delay);
                psrequest = new Socket("127.0.0.1", 3000);
                readServerResponse = new DataInputStream(psrequest.getInputStream());
                sendCouncilMemberRequest = new DataOutputStream(psrequest.getOutputStream());

                // perform request to get state
                sendCouncilMemberRequest.writeUTF("ACTION:GETSTATE,FROM_MEMBER:" + ID);
                String serverRes = readServerResponse.readUTF();
                processState(serverRes);

            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public Integer getAcceptedVote() {
        return accepted_vote;
    }

    public String getValue(String requestBody, String key) {
        String[] req = requestBody.split(",");

        for (int i = 0; i < req.length; i++) {
            if (req[i].contains(key)) {
                // System.out.println("getValue " + key + " : " + req[i]);
                return req[i].split(":")[1];
            }
        }
        return null;
    }

    private void processState(String request) {
        if (!request.contains("null")) {
            switch (getValue(request, "ACTION")) {
                // phase 1 part 2: the acceptors receive a prepare request from the proposer
                // and must decide based on the proposal number whether to accept the
                // prepare request. if the acceptor accepts the prepare request, then they
                // send a promise back to the proposer.
                case "PREPARE":
                    // debug statement
                    System.out.println("Council member " + ID + " received prepare request from council member "
                            + getValue(request, "FROM_MEMBER") + " that contains proposal number "
                            + getValue(request, "PROPOSAL_NUMBER") + " & vote " + getValue(request, "VOTE"));

                    // check if prepare request contains a proposal number higher than
                    // current highest proposal number
                    if (Integer.parseInt(getValue(request, "PROPOSAL_NUMBER")) > highest_numbered_proposal) {
                        highest_numbered_proposal = Integer.parseInt(getValue(request, "PROPOSAL_NUMBER"));
                        // we send a promise with our highest_numbered_proposal that we have
                        // back to the proposer.
                        promise(highest_numbered_proposal, Integer.parseInt(getValue(request, "FROM_MEMBER")),
                                Integer.parseInt(getValue(request, "VOTE")));
                    }
                    break;
                // phase 2 part 1: the proposer increments the no_of_promises_accepted count.
                // if the majority of acceptors (i.e. at least 5, so no_of_promises_accepted
                // >= 5) response with promise_accepted, then the proposer sends an accept
                // request with the highest proposal number among its responses
                case "PROMISE_ACCEPTED":
                    System.out.println("Council member " + ID + " is accepting proposal from council member"
                            + getValue(request, "FROM_MEMBER") + " that contains proposal number "
                            + getValue(request, "PROPOSAL_NUMBER") + " & vote " + getValue(request,
                                    "VOTE"));

                    // increment no of promises
                    no_of_promises_accepted += 1;
                    System.out.println("Number of members accepting " + ID + "'s proposal: " + no_of_promises_accepted);
                    // store the highest-number proposal among the promises accepted to send
                    // in the accept request
                    max_proposal_no_from_promises = Integer
                            .parseInt(getValue(request, "PROPOSAL_NUMBER")) <= max_proposal_no_from_promises
                                    ? max_proposal_no_from_promises
                                    : Integer.parseInt(getValue(request, "PROPOSAL_NUMBER"));
                    // check if the majority of promises are accepted
                    if (no_of_promises_accepted >= 5) {
                        // reset the number of promises accepted
                        no_of_promises_accepted = 0;
                        // send the accept request to acceptors
                        accept(max_proposal_no_from_promises, Integer.parseInt(getValue(request,
                                "VOTE")));
                    }
                    break;
                // phase 2 part 2: when the acceptors receive an accept request
                // from a proposer, it accepts the vote in the proposal. (Unless
                // the acceptor has responded to a proposal greater than
                // highest_numbered_proposal)
                case "ACCEPT":
                    System.out.println("Council member " + ID + " is accepting vote from council member"
                            + getValue(request, "FROM_MEMBER") + " that contains proposal number "
                            + getValue(request, "PROPOSAL_NUMBER") + " & vote " + getValue(request,
                                    "VOTE"));

                    // to avoid responding to accept messages whose proposal numbers are lower
                    // than what is being currently proposed, we check that the proposal number in
                    // the
                    // accept request is greater than or equal to the highest numbered proposal
                    if (Integer.parseInt(getValue(request, "PROPOSAL_NUMBER")) >= highest_numbered_proposal) {
                        accepted_vote = Integer.parseInt(getValue(request, "VOTE"));
                    }
                    break;
                default:
                    break;
            }
        }
    }

    // phase 1 part 1: a proposer sends out a prepare request with the vote and
    // the proposal_number.
    public void prepare(int vote, int proposal_number) {
        try {
            System.out.println("Council member " + ID + " is sending proposal that contains proposal number "
                    + proposal_number + " & vote " + vote);

            psrequest = new Socket("127.0.0.1", 3000);
            sendCouncilMemberRequest = new DataOutputStream(psrequest.getOutputStream());

            sendCouncilMemberRequest.writeUTF(
                    "ACTION:PREPARE,FROM_MEMBER:" + ID + ",VOTE:" + vote + ",PROPOSAL_NUMBER:" + proposal_number);
            sendCouncilMemberRequest.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // phase 1 part 2: promise function that sends a response back to the
    // proposer.
    private void promise(int highest_proposal_no, int to_member, int vote) {
        try {
            System.out.println("Council member " + ID + " is sending promise that contains proposal number "
                    + highest_proposal_no + " & vote " + vote + " to council member " + to_member);

            psrequest = new Socket("127.0.0.1", 3000);
            sendCouncilMemberRequest = new DataOutputStream(psrequest.getOutputStream());

            sendCouncilMemberRequest.writeUTF(
                    "ACTION:PROMISE,FROM_MEMBER:" + ID + ",TO_MEMBER:" + to_member + ",PROPOSAL_NUMBER:"
                            + highest_proposal_no + ",VOTE:" + vote);
            sendCouncilMemberRequest.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // phase 2 part 1: accept function where the proposer sends an accept request to
    // the acceptors.
    private void accept(int proposal_number, int vote) {
        try {
            System.out.println("Council member " + ID + " is sending accept request that contains proposal number "
                    + proposal_number + " & vote " + vote);

            psrequest = new Socket("127.0.0.1", 3000);
            sendCouncilMemberRequest = new DataOutputStream(psrequest.getOutputStream());

            sendCouncilMemberRequest.writeUTF(
                    "ACTION:ACCEPT,FROM_MEMBER:" + ID + ",PROPOSAL_NUMBER:"
                            + proposal_number + ",VOTE:" + vote);
            sendCouncilMemberRequest.flush();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
