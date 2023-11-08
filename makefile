compile:
	javac -d bin/ src/main/java/CouncilMember.java src/main/java/PaxosServer.java src/main/java/RequestHandler.java src/main/java/tests/Test.java src/main/java/tests/LargeDelayTest.java src/main/java/tests/m2andm3GoesOfflineTest.java src/main/java/tests/m2andm3GoesOfflineTest.java src/main/java/tests/MinorityFailureTest.java src/main/java/tests/SmallDelayTest.java src/main/java/tests/TwoCouncillorsTest.java

clean:
	rm bin/main/java/*.class && rm bin/main/java/tests/*.class

server:
	java -cp bin main.java.PaxosServer

largedelaytest:
	java -cp bin main.java.tests.LargeDelayTest > ./logs/LargeDelayTestOutput.txt 

m2m3offlinetest:	
	java -cp bin main.java.tests.m2andm3GoesOfflineTest > ./logs/m2andm3GoesOfflineTestOutput.txt 

m2offlinetest:
	java -cp bin main.java.tests.m2GoesOfflineTest > ./logs/m2GoesOfflineTestOutput.txt 

minorityfailuretest:
	java -cp bin main.java.tests.MinorityFailureTest > ./logs/MinorityFailureTestOutput.txt 

smalldelaytest:
	java -cp bin main.java.tests.SmallDelayTest > ./logs/SmallDelayTestOutput.txt 

twocouncillorstest:
	java -cp bin main.java.tests.TwoCouncillorsTest > ./logs/TwoCouncillorsTestOutput.txt
