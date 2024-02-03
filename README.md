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

Ali Zaid (101223823): UML Class and Sequence Digram, ReadMe

------------------------------------------------------------------------------------------------------------------------------------------

Iteration Description: 

This project implements an Elevator Control System using Java. It consists of three subsystems: Floor, Scheduler, and Elevator. The Floor subsystem reads events from an input file, sends them to the Scheduler, and receives responses. The Scheduler acts as a server, managing communication between the Floor and Elevator subsystems. The Elevator subsystem receives requests from the Scheduler, processes them, and sends back responses.

------------------------------------------------------------------------------------------------------------------------------------------

Files Description:

Box.java: Defines the Box class, which represents a message flow between subsystems.

Scheduler.java: Implements the Scheduler class, responsible for managing communication between the Floor and Elevator subsystems.

Floor.java: Implements the Floor class, which reads events from an input file and communicates with the Scheduler.

Elevator.java: Implements the Elevator class, which receives requests from the Scheduler and processes them.

Main.java: Contains the main method to start the Elevator Control System.

------------------------------------------------------------------------------------------------------------------------------------------

Test files instructions:
