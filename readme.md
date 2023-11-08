# Assignment 3: Paxos
## Welcome to the Adelaide Suburbs Council Election!
This year, Adelaide Suburbs Council is holding elections for council president. Any member of its nine person council is eligible to become council president.

Member M1 – M1 has wanted to be council president for a very long time. M1 is very chatty over social media and responds to emails/texts/calls almost instantly. It is as if M1 has an in-brain connection with their mobile phone!

Member M2 – M2 has also wanted to be council president for a very long time, except their very long time is longer than everybody else's. M2 lives in the Adelaide Hills and thus their internet connection is really poor, almost non-existent. Responses to emails come in very late, and sometimes only to one of the emails in the email thread, so it is unclear whether M2 has read/understood them all. However, M2 sometimes likes to work at Sheoak Café. When that happens, their responses are instant and M2 replies to all emails.

Member M3 – M3 has also wanted to be council president. M3 is not as responsive as M1, nor as late as M2, however sometimes emails completely do not get to M3. The other councillors suspect that it’s because sometimes M3 goes camping in the Coorong, completely disconnected from the world.

Members M4-M9 have no particular ambitions about council presidency and no particular preferences or animosities, so they will try to vote fairly. Their jobs keep them fairly busy and as such their response times  will vary.

How does voting happen: On the day of the vote, one of the councillors will send out an email/message to all councillors with a proposal for a president. A majority (half+1) is required for somebody to be elected president.

## YOUR TASK:
Write a program that implements a Paxos voting protocol for Suburbs Council President that is fault tolerant and resilient to various failure types, some of which are shown in the above. Communication happens via sockets*. You are responsible for the message design.

*It is strongly recommended that communication happen via sockets. Other possible solutions include RPC and inter-thread/process communication. They are needlessly complex for this solution and can lead to implementation headaches.

Assessment
Your assignment will be marked out of 100 points, as following:

10 points - Paxos implementation works when two councillors send voting proposals at the same time

30 points – Paxos implementation works in the case where all M1-M9 have immediate responses to voting queries

30 points – Paxos implementation works when M1 – M9 have responses to voting queries suggested by several profiles (immediate response, small delay, large delay and no response), including when M2 or M3 propose and then go offline

20 points – Testing harness for the above scenarios + evidence that they work (in the form of printouts)

10 points for the quality of your code

# Architecture
The architecture of my paxos implementation is a client-server model, where the clients are the council members and the server performs the message passing between council members. 

The message architecture is straightforward; it is a string that contains comma seperated key-value pairs. The key-value pairs are seperated by colons. Messages contain ACTION, which is the action that the council member would like to perform (PREPARE, PROMISE, PROMISE_ACCEPTED, or ACCEPT), FROM_MEMBER contains the sender's id, TO_MEMBER contains the recipient's id, PROPOSAL_NUMBER is the proposal value, and VOTE is the vote of the council members.

The server (PaxosServer.java) is a message bank for all 9 council members. The message bank is represented as a map of the council member id to a queue of messages. Each time a new request is performed, it goes to the request handler (RequestHandler.java). The request handler performs GETSTATE, PREPARE, PROMISE, & ACCEPT. GETSTATE allows the council members to see their messages from other council members. PREPARE copies the proposer's prepare request into all council members (who are also acceptors) message bank. PROMISE copies the acceptors' promise response (if the prepare request is successful) into the proposer's message bank. ACCEPT sends accept requests to acceptors.

The council members can only send prepare (voting) requests to the server. m1 to m3 are proposers (voters) and acceptors, and m4 to m9 are purely acceptors. Council members messages are polled from the server every 50ms using the GETSTATE request. m1 to m3 send prepare requests by calling prepare(), which sends a PREPARE request to the server. If acceptors receive a PREPARE request when they poll the server, they must compare the proposal number in the message compared their highest accepted proposal number. If so, then they send PROMISE messages to the proposer. Then the proposer receives a PROMISE_ACCEPTED to their message bank. If the majority of council members (at least 5) response with PROMISE_ACCEPTED, then the proposer sends ACCEPT requests to all acceptors. Once the acceptors get the ACCEPT request, they set their vote to the value in the ACCEPT message. 

# Testing
## 1. You must first compile all files by typing: make compile
## 2. To perform any of the tests, you must run the server by typing into the command line: make server
## 3. The outputs of the tests are in the logs folder, and the final vote of each member is always at the end of the file.
## 4. I have organised the tests to address the criteria outlined in the assessment: 20 points – Testing harness for the above scenarios + evidence that they work (in the form of printouts)
### a. 10 points - Paxos implementation works when two councillors send voting proposals at the same time & 30 points – Paxos implementation works in the case where all M1-M9 have immediate responses to voting queries
    i. To observe the result of two councillors sending voting proposals simulatenously, in a new terminal window, you must run: make twocouncillorstest. The test initialises council members M1 to M9 (who have immediate responses to voting queries i.e., no delay), and M1 and M2 vote (by running prepare()) for themselves. However, the proposal number of M2 is higher than M1's proposal number. Therefore, the vote finalises to 2, which is M2. The output for this is in the logs folder under TwoCouncillorsTestOutput.txt at the bottom of the text file. 

### b. 30 points – Paxos implementation works when M1 – M9 have responses to voting queries suggested by several profiles (immediate response, small delay, large delay and no response), including when M2 or M3 propose and then go offline
    i. To observe the test case where M1 - M9 have immediate responses, follow the previous point 4ai. 
    ii. To observe the test case where M1 - M9 have a randomised small delay between 250 to 750 ms, you must run: make smalldelaytest. The test initialises council members by assigning them random delay between 250 to 750 ms. Then M1, M2 and M3 vote (by sending prepare() request) and then the rest of the members respond in a delayed fashion. However, M3's vote has the highest proposal number, so the vote finalises to 3, which is M3. The output for this is in the logs folder under SmallDelayTestOutput.txt 
    iii. To observe the test case where M1 - M9 have a randomised large delay between 750 to 2500 ms, you must run: make largedelaytest. The test initialises council members by assigning them random delay between 750 to 2500 ms. Then M1, M2 and M3 vote (by sending prepare() request) and then the rest of the members respond in a delayed fashion. However, M3's vote has the highest proposal number, so the vote finalises to 3, which is M3. The output for this is in the logs folder under LargeDelayTestOutput.txt.
    iii. To observe the test case where M2 proposes and then goes offline, you must run: make m2offlinetest. After M2 proposes, it goes offline (I add a delay longer than the timer to interrupt the threads associated with the council members). Because M2 goes offline, when the other council members send back their promises to M2, M2 cannot process the promises. Therefore, no vote goes through and the final vote is 0, which is the default vote to vote for no council member.
    iv. To observe the test case where M2 & M3 propose and then goes offline, you must run: make m2m3offlinetest. After M2 and M3 propose, they go offline (I add a delay longer than the timer to interrupt the threads associated with the council members). Because M2 & M3 goes offline, when the other council members send back their promises to M2 & M3, M2 & M3 cannot process the promises. Therefore, no vote goes through and the final vote is 0, which is the default vote to vote for no council member.
### c. Additional tests:
    i. To observe the test case where the minority of council members fail (4 non-proposing nodes, M6 - M9), you must run: make minorityfailuretest. Another difference between this test and M2offline and M2M3offline test is that non-proposing nodes fail. This means that even though 4 nodes dies and non-proposing nodes fail, there should still be majority and votes can still be processed and come to an agreement. Since M1 to M5 (majority with at least 5 nodes) are avaliable, M1 to M3 can still propose (vote) and the avaliable nodes come to a consensus vote of 2, which is M2, since it has the highest proposal value. 