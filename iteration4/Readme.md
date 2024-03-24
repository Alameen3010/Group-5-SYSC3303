# Group-5-SYSC3303
Designing and implementing  an elevator simulator system 

March 12, 2024 

SYSC 3303 - Project Group 5 - Iteration 3

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

Iteration Description: 

This project implements an first UDP communication effectively translating the threads into processes that can run on seperate computers. In the process,
the request class was introduced to pass information. Then, all the requests were passed to the scheduler instead effectively processing all requests at once.
Finally, a "simple" algorithm was implemented that allowed to create as many elevators as requested and allocate an equal amount of requests per elevator.

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

Elevator.java: Implements the Elevator class, which receives requests from the Scheduler and processes them thorugh its Array List.

------------------------------------------------------------------------------------------------------------------------------------------
Set up instructions:
Add all files with .java extension (9 total) into a single project folder. Also, add the Requests.txt into this folder. Run the Main to see the output with the given Requests.txt file, you may test the output and see how it differs with changing values in the Requests.txt input file.

Test files instructions:
All test files (4 total, denoted with 'Test' in file name) should have been placed in same project folder. Each Test class has tests for individual methods of each class, excluding Main. *NOTE, in our testing, the tests ran more smoothly when 'run' button was pressed for individual methods within the test class, and not all at once*
