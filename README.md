# Elevator Control System - Group 5

Contained within this README file are task assignments, instructions and notes for each iteration of the SYSC 3303 project.

### Iteration 1

#### Work Assignments:

Name | Job
------------ | -------------
Peyman | Manages the Github, general coding.
Alex | Handles README and writing tasks, general coding.
Campbell | General coding. 
Alec | In charge of JUNIT test design.
Jiawei | Author of UML & Sequence diagrams, assisted with JUNIT test design.

#### Program Instructions:

After pulling the project from Github, run the main class in Eclipse to start the program. 

There is currently no UI so all output will be in the console. 

#### Classes:

Elevator: represents Elevator subsystem and recieves and send info to scheduler

Scheduler: represents the box following mutual exclusion concept to handle transfering data between floor and elevator

Floor: represents each floor object 

FloorSubystem: represents the floor subsystem to handle each floor

Main: represents as a client for testing/simulating the functionality of the project

#### Testing Instructions:

Test files are in the default package along with the rest of the Class files. 
All Files ending with the word "Test" is to be ran manually (right click, Run As -> JUnit Test)

NOTE: Test files will be separated into their own package in later iterations


