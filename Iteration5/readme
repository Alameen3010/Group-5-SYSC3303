# Group-5-SYSC3303
Designing and implementing  an elevator simulator system 

April 9, 2024 

SYSC 3303 - Project Group 5 - Iteration 5

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

Ilyes Outaleb (101185290): Scheduler Subsystem, Elevator Subsystem, Sequence Diagram

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

Ilyes Outaleb (101185290): Modified State Diagram and Modified Implementation of Transient Faults.

Rozba Hakam (101190098): Testing

Ali Zaid (101223823): Testing

------------------------------------------------------------------------------------------------------------------------------------------
Iteration 5: 
Alameen Alliu (101159780): User Interface, GUI Updater

Taran Basati (101161332): Testing and Measurements

Ilyes Outaleb (101185290): Implemented Capacity and Scalability and created ElevatorManager, provided User Interface values, readme.

Rozba Hakam (101190098): Diagrams and Final Report

Ali Zaid (101223823): User Interface and GUI Updater

------------------------------------------------------------------------------------------------------------------------------------------
Iteration Description: 
This iteration focuses on capacity where a certain threshold of passengers can embark on the elevator on any given time. Then, the user interface has 
to be created in order to visualize the execution of all previous iterations concluding this project.


------------------------------------------------------------------------------------------------------------------------------------------

Files Description:
Elevator.java: Java code that simulates the elevator and is responsible for transporting all the passengers on each floor.
ElevatorGUIUpdater.java: Java code that updates in real time the GUI with elevator values including, number of passengers, current floor, door status, faults and 
most importantly the metrics showing performance of each elevator.
ElevatorManger.java: Triggers all the elevators, allows for scalability of system and the retrieval of all elevator values.
ElevatorUserInterface.java: The user interface shows all the values made available by the Elevator Manager. It is continuously updated by the ElevatorGUIUpdater.java.
Floor.java: Simulates all the floors and is responsible for providing all the requests to the scheduler for scheduling.
Message.java: Class that is used to communicate between different ports. Serialization and deserialization is used with UDP.
Requests.txt: List of requests which are the input to the system which is read by the floor.
Scheduler.java: Manages all the elevators and provides them their respective requests to handle. Handle system faults.
UI: Base Java code to provide a basic template for the User Interface. Not used in the compilation.

------------------------------------------------------------------------------------------------------------------------------------------
Set up instructions: 
Scalabiltiy: Change the number of Elevators field in scheduler, elevatorManager and ElevatorUserInterface.

Set up Instructions Assuming IntelliG is used:
Put in a single "src" folder all java code except UI.
Then put outside the src folder but within the project folder the "Requests.txt"
Run the elevatorManager first then, the scheduler and Floor.

Alternative:
To see the output of each Elevator Individually. Go to "Current File" beside the run option -> Edit Configurations -> "+" -> "Application"
Put Name: Elevator1
"Main class" as Elevator
"Program arguments "Elevator1"
Repeat these steps for the number of elevator you wish to set for the system. See Scalability above.
Run each of these elevators in the run drop down menu.
Then run the scheduler and Floor subsystems.
You should see a seperate termianl for each elevator to easily monitor its elevator activity with ease and clarity.

------------------------------------------------------------------------------------------------------------------------------------------
Test files Instructions: 
All test files (3 total, denoted with 'Test' in file name) should have been placed in same project folder. 
Each Test class has tests for individual methods of each class. Also, assert print statements to test output.

