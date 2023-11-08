package main.java.tests;

import main.java.CouncilMember;

public class Test {
    /*
     * Your assignment will be marked out of 100 points, as following:
     * 
     * 20 points – Testing harness for the above scenarios + evidence that they work
     * (in the form of printouts)
     * 
     * 10 points - Paxos implementation works when two councillors send voting
     * proposals at the same time
     * 
     * 30 points – Paxos implementation works in the case where all M1-M9 have
     * immediate responses to voting queries
     * 
     * twoCouncillors() sends two councillors send voting proposals simulatenously
     * and M1 to M9 council members provide immediate responses to voting queries.
     * The vote will be 2.
     */
    public void twoCouncillors() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1 and m2 send voting proposals (i.e. send
        // prepare requests to all other nodes) simulatenously.
        m1.prepare(1, 1);
        // since m1 has proposal number 1 and m2 has proposal number
        // 2, m2's proposal is going to be accepted.
        m2.prepare(2, 2);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("twoCouncillors test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    }

    /*
     * 30 points – Paxos implementation works when M1 – M9 have responses to voting
     * queries suggested by several profiles (immediate response, small delay, large
     * delay and no response), including when M2 or M3 propose and then go offline
     * 
     * Since I have demonstrated immediate response in the twoCouncillors() test,
     * here I will demonstrate small delay in response by setting a small (between
     * 250-750 ms) random delay for all members. The vote will be 3
     */
    public void smallDelay() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1, m2 & m3 send voting proposals (i.e. send
        // prepare requests to all other nodes) simulatenously.
        m1.prepare(1, 1);
        m2.prepare(2, 2);
        m3.prepare(3, 3);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        Integer max = 750;
        Integer min = 250;
        try {
            // here each member will incur some random delay between 0.5 to 1 second
            m1.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m2.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m3.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m4.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m5.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m6.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m7.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m8.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m9.delay((int) Math.floor(Math.random() * (max - min + 1) + min));

            Thread.sleep(15000);
        } catch (Exception e) {
            System.err.println("smallDelay test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    };

    /*
     * 30 points – Paxos implementation works when M1 – M9 have responses to voting
     * queries suggested by several profiles (immediate response, small delay, large
     * delay and no response), including when M2 or M3 propose and then go offline
     * 
     * here I will demonstrate large delay in response by setting a large (between
     * 750-2500 ms) random delay for all members. The vote will be 3.
     */
    public void largeDelay() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1, m2 & m3 send voting proposals (i.e. send
        // prepare requests to all other nodes) simulatenously.
        m1.prepare(1, 1);
        m2.prepare(2, 2);
        m3.prepare(3, 3);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        Integer max = 2500;
        Integer min = 750;
        try {
            // here each member will incur some random delay between 1 to 3 seconds
            m4.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m5.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m6.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m7.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m8.delay((int) Math.floor(Math.random() * (max - min + 1) + min));
            m9.delay((int) Math.floor(Math.random() * (max - min + 1) + min));

            Thread.sleep(30000);
        } catch (Exception e) {
            System.err.println("largeDelay test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    };

    /*
     * 30 points – Paxos implementation works when M1 – M9 have responses to voting
     * queries suggested by several profiles (immediate response, small delay, large
     * delay and no response), including when M2 or M3 propose and then go offline
     * 
     * Here m2 will propose and go offline after it proposes.
     * If m2 goes offline, the system will be in limbo as m2
     * does not know that the system has accepted m2's proposal
     * so the vote value will be 0.
     */
    public void m2GoesOffline() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1 sends a voting proposal (i.e. send
        // prepare requests to all other nodes).
        m2.prepare(2, 2);
        // I add a delay
        // which simulates m2 going offline
        m2.delay(30000);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        try {
            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("m2GoesOffline test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    }

    /*
     * 30 points – Paxos implementation works when M1 – M9 have responses to voting
     * queries suggested by several profiles (immediate response, small delay, large
     * delay and no response), including when M2 or M3 propose and then go offline
     * 
     * Here m2 and m3 will propose and go offline after they propose.
     * If m2 & m3 go offline, the system will be in limbo as m2 or m3
     * don't know that the system has accepted so the vote value will be 0.
     */
    public void m2Andm3GoesOffline() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1, m2 & m3 send voting proposals (i.e. send
        // prepare requests to all other nodes) simulatenously.
        m2.prepare(2, 2);
        m3.prepare(3, 3);
        // I add a delay
        // which simulates m2 & m3 going offline
        m2.delay(30000);
        m3.delay(30000);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        try {

            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("m2Andm3GoesOffline test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    }

    /*
     * 30 points – Paxos implementation works when M1 – M9 have responses to voting
     * queries suggested by several profiles (immediate response, small delay, large
     * delay and no response), including when M2 or M3 propose and then go offline
     * 
     * Here a minority of nodes (m6, m7, m8, m9) go offline/fail. However, m1 - m5
     * are a majority, so they are able to come to a consensus. So the vote is 2.
     */
    public void minorityFailure() {
        // here I initialise the council members
        CouncilMember m1 = new CouncilMember(1);
        CouncilMember m2 = new CouncilMember(2);
        CouncilMember m3 = new CouncilMember(3);
        CouncilMember m4 = new CouncilMember(4);
        CouncilMember m5 = new CouncilMember(5);
        CouncilMember m6 = new CouncilMember(6);
        CouncilMember m7 = new CouncilMember(7);
        CouncilMember m8 = new CouncilMember(8);
        CouncilMember m9 = new CouncilMember(9);
        m1.start();
        m2.start();
        m3.start();
        m4.start();
        m5.start();
        m6.start();
        m7.start();
        m8.start();
        m9.start();

        // here m1 and m2 send voting proposals (i.e. send
        // prepare requests to all other nodes) simulatenously.
        m1.prepare(1, 1);
        // since m1 has proposal number 1 and m2 has proposal number
        // 2, m2's proposal is going to be accepted.
        m2.prepare(2, 2);

        // since the state is managed on the server and the
        // council members poll their state every 50ms,
        // provide the test some time to process the messages.
        try {
            // Here, council member m6, m7, m8 and m9
            // go offline
            m6.delay(10000);
            m7.delay(10000);
            m8.delay(10000);
            m9.delay(10000);

            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("minorityFailure test error: " + e.toString());
        }

        // this is the print out of the votes being accepted
        // which is vote for 2, as its proposal number is the highest
        System.out.println("m1 vote: " + m1.getAcceptedVote());
        System.out.println("m2 vote: " + m2.getAcceptedVote());
        System.out.println("m3 vote: " + m3.getAcceptedVote());
        System.out.println("m4 vote: " + m4.getAcceptedVote());
        System.out.println("m5 vote: " + m5.getAcceptedVote());
        System.out.println("m6 vote: " + m6.getAcceptedVote());
        System.out.println("m7 vote: " + m7.getAcceptedVote());
        System.out.println("m8 vote: " + m8.getAcceptedVote());
        System.out.println("m9 vote: " + m8.getAcceptedVote());

        // stop the threads for the council members
        m1.interrupt();
        m2.interrupt();
        m3.interrupt();
        m4.interrupt();
        m5.interrupt();
        m6.interrupt();
        m7.interrupt();
        m8.interrupt();
        m9.interrupt();
    };
}
