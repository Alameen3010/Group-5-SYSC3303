Group-5-SYSC3303
Adding state machines for scheduler and elevator subsystems, preparing for multi-elevator coordination to maximize passenger throughput in subsequent iterations.

February 17, 2024

SYSC 3303 - Project Group 5 - Iteration 2

Group members and tasks:

Alameen Alliu (101159780): Elevator Subsystem

Taran Basati (101161332): JUnit Testing

Rozba Hakam (101190098): State Diagram, ReadMe

Ilyes Outaleb (101185290): Scheduler Subsystem

Ali Zaid (101223823): UML Class and Sequence Diagram, ReadMe

Iteration Description:
This iteration focuses on adding state machines for the scheduler and elevator subsystems, assuming a single elevator setup, with plans to optimize passenger throughput through multi-elevator coordination in future iterations.

Files Description:
Scheduler.java: Manages and schedules requests from the floor subsystem to the elevator subsystem using a state machine.

Elevator.java: Represents an elevator subsystem with functionalities for handling elevator operations and door states through a state machine.

Set up instructions: Add all files with .java extension (2 classes + 3 test = 5 total) into a single project folder. Run Elevator.java and Scheduler.java, then simply follow console instrustions to simulate the system.

Test files instructions: All test files (3 total, denoted with 'Test' in file name) should have been placed in same project folder. Each Test class has tests for the individual methods of each class. Also, multiple tests for different phases of the state diagram are included. An Integration test class was created that combines both Elevator and Scheduler in one test class for a more broad, system wide test.
