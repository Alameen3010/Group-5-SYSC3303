# Group-5-SYSC3303
Designing and implementing  an elevator simulator system 

February 3, 2024 

SYSC 3303 - Project Group 5 - Iteration 1

------------------------------------------------------------------------------------------------------
Group members and tasks:


Alameen Alliu (101159780): Elevator Class

Taran Basati (101161332): JUnit Testing

Rozba Hakam (101190098): Floor Class 

Ilyes Outaleb (101185290): Main Class, Scheduler Class, Box Class, Floor Class 

Ali Zaid (101223823): UML Class and Sequence Diagram, ReadMe

------------------------------------------------------------------------------------------------------------------------------------------

Iteration Description: 

This project implements an Elevator Control System using Java. It consists of three subsystems: Floor, Scheduler, and Elevator. The Floor subsystem reads events from an input file, sends them to the Scheduler, which then sends to the elevator. The elevator sends the response and the scheduler sends it back to the floor. The Scheduler acts as a server, managing communication between the Floor and Elevator subsystems. The Elevator subsystem receives requests from the Scheduler, processes them, and sends back responses.

------------------------------------------------------------------------------------------------------------------------------------------

Files Description:

Box.java: Defines the Box class, which represents a message flow between subsystems. Its responsible for synchronization using states. 0 being the initial one where the cycle is complete and waiting for a new request. 1 to send the message to the destionation. Then 2 to get the response from the destionation. Finally, 3 to get the response back to the source. This state then goes back to 0 and re-cycles for a new request. This prevents the methods from being run when they are supposed to creating a sonsistent control over the events. Finally, it prints all the steps of the communication process.

Scheduler.java: Implements the Scheduler class, responsible for managing communication between the Floor and Elevator subsystems. Contains two boxes and is responsible for sending and getting the message from floor to elevator in both directions.

Floor.java: Implements the Floor class, which reads events from an input csv file and communicates with the Scheduler. It finally prints in the terminal the response
of the message initially sent. In this iteration the input and output will be the same with a minor modification in the direction attribute.

Elevator.java: Implements the Elevator class, which receives requests from the Scheduler and processes them.

Main.java: Contains the main method to start the Elevator Control System. This initializes and starts all the threads of the program and sets creates the two shared boxes of this system.

------------------------------------------------------------------------------------------------------------------------------------------
Set up instructions:
Add all files with .java extension (9 total) into a single project folder. Also, add the Requests.txt into this folder. Run the Main to see the output with the given Requests.txt file, you may test the output and see how it differs with changing values in the Requests.txt input file.

Test files instructions:
All test files (4 total, denoted with 'Test' in file name) should have been placed in same project folder. Each Test class has tests for individual methods of each class, excluding Main. *NOTE, in our testing, the tests ran more smoothly when 'run' button was pressed for individual methods within the test class, and not all at once*
