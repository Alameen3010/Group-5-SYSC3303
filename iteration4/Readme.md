# Group-5-SYSC3303
Designing and implementing  an elevator simulator system 

March 23, 2024 

SYSC 3303 - Project Group 5 - Iteration 4

------------------------------------------------------------------------------------------------------
Group members and tasks:

Iteration 1:
Alameen Alliu (101159780): Elevator Class

Taran Basati (101161332): JUnit Testing

Rozba Hakam (101190098): Floor Class 

Ilyes Outaleb (101185290): Main Class, Scheduler Class, Box Class, Floor Class 

Ali Zaid (101223823): UML Class and Sequence Diagram, ReadMe

------------------------------------------------------------------------------------------------------
Iteration 2:
Alameen Alliu (101159780): Elevator Subsystem

Taran Basati (101161332): JUnit Testing

Rozba Hakam (101190098): State Diagram, ReadMe

Ilyes Outaleb (101185290): Scheduler Subsystem, Elevator Subsystem

Ali Zaid (101223823): UML Class and Sequence Diagram, ReadMe

------------------------------------------------------------------------------------------------------
Iteration 3: 
Alameen Alliu (101159780): UDP integration

Taran Basati (101161332): JUnit Testing

Rozba Hakam (101190098): Testing and scheduling

Ilyes Outaleb (101185290): Fixed Iteration 2, UDP integration, Modified Floor, Scheduler and Elevator to implement scheduling algorithm, made Request class and Readme.

Ali Zaid (101223823): Pushed requests from Floor to Scheduler

------------------------------------------------------------------------------------------------------------------------------------------

Iteration 4: 
Alameen Alliu (101159780): Diagrams

Taran Basati (101161332): Modified Message, Scheduler and Elevator classes to implement Faults and Timing for States, made Readme.

Ilyes Outaleb (101185290): Modified Message, Scheduler and Elevator classes to implement Faults and Timing for States, made Readme.

Rozba Hakam (101190098): Testing

Ali Zaid (101223823): Testing

------------------------------------------------------------------------------------------------------------------------------------------

Iteration Description: 

This project iteration implements faults for the first time. The system can take in full system faults (hard faults) and door malfunctions (soft faults) in the Requests.csv file, and then handle them within the code correctly. Hard faults cause that elevator to shut down forever, and then the scheduler redistributes the requests accordingly. Soft faults are handled by the timing, so the door opening and door closing states take twice as long, to mimic a door having trouble working. Also, the system now has timings for all states, which is a step in the right direction for making the final system.

Add Elevator Steps:
To change the number of Elevators:
1) Add an else if statement in the main of elevator with new id.
2) Change the main for scheduler. Constant???
3) Add a send "empty" command to elevator // removed implemented by a for loop
4) Add a receiveElevatorResponse();	  // removed implemented by a for loop
5) Add a new terminal view matching argument in Elevator Main.

------------------------------------------------------------------------------------------------------------------------------------------

Files Description:

Message.java: Class representing the messages that are sent between the three subsystems. All imformation is obtained through this class.

Scheduler.java: Implements the Scheduler class, responsible for managing communication between the Floor and Elevator subsystems. Also, responsible for dispatching
the requests in the most effective way possible.

Floor.java: Implements the Floor class, which reads events from an input csv file and communicates with the Scheduler. It finally prints in the terminal the response
of the message initially sent. In this iteration the received message is a null message will all attributes equal to zero.

Elevator.java: Implements the Elevator class, which receives requests from the Scheduler and processes them thorugh its Array List. Controls each individual elevator as it works through its states and timings.

------------------------------------------------------------------------------------------------------------------------------------------
Set up instructions: Add all files with .java extension (4 total) into a single project folder. Also, add the Requests.txt into this folder. Run the Scheduler first, then each individual java application for the elevators, and finally the floor class. See the output in each of the terminals, with the given Requests.txt file, you may test the output and see how it differs with changing values in the Requests.txt input file.

Test files instructions: All test files (3 total, denoted with 'Test' in file name) should have been placed in same project folder. Each Test class has tests for individual methods of each class. Also, assert print statements to test output.
