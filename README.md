APEX Pipeline Simulator

1. Introduction
2. Implementation 
  a. Pipelining
  b. Forwarding
  c. Dependencies 
  d. Memories
  e. Registers
  f. Stalls
  g. FunctionalUnits
3. UserInterface 
4. Glossary


<b>Introduction</b>
Implement a cycle-by-cycle simulator for an in-order APEX pipeline with two different function units, as shown below:

![Alt text](https://raw.githubusercontent.com/TechiezHub/APEXSimulator1.0/master/Screen%20Shot%202017-10-21%20at%209.07.57%20PM.png "Pipelined Functional Units")


<b>Supported Instructions:</b>
· Register-to-register instructions: ADD, SUB, MOVC, MUL, AND, OR, EX- OR (all done on the Integer ALU in two cycles). You can assume that the result of multiplying two registers will fit into a single register.  
· MOVC <register> <literal>, moves literal value into specified register. The MOVC uses the ALU stages to add 0 to the literal and updates the destination register from the WB stage.  
· Memory instructions: LOAD, STORE - both using a literal offset.  
· Control flow instructions: BZ, BNZ, JUMP, BAL, HALT. Instructions following a BZ, BNZ, JUMP and BAL instruction in the pipeline should be flushed on a taken branch. The zero flag (Z) is set only by arithmetic instructions. You can assume that this flag is stored as an extension of the destination register for the instruction that generates the result. (This will simplify coding for Project 2!).  
  
  <b>Implementation Pipelining:</b>
Implemented a APEXPipelineHandler class which acts as an engine of the architecture and manages all the stages. It creates the chain of pipelined stages which is used to transfer the control to next stage from previous stage.
Implemented a Base class named as Stage, which is further inherited by all the stages i.e Fetch, Decode, Execute, Memory & Writeback. They all override a method named as “render”, declared in base class “Stage”. The method works in below manner-
1. Fetch–ItreadstheinstructionfromICache(explainedbelow)andcreates the object of Instruction Class. A high level instruction (in English) is decomposed and constructed an instruction object with Opcode, Destination, Source Registers, etc.
2. Decode–ItreadstheRegisterFile(StaticArrayunderSharedDataclass) and resolves the value of Source registers.
3. Execute–Itchecksforthetypeofinstructionfirstandthenitcreatedthe object of appropriate functional unit. Let’s say if the instruction is an arithmetic instruction then it will create instance of ALU Functional Unit (Class ALU) and if it’s branch instruction then it create instance of Branch Functional Unit (Class Branch). There is just one class implementation for the execute although there is 2 stages ALU. So, this class is pretty intelligent to handle the 2 stages, it maintains the stage number as tag. There will be 2 instances of Execute class in the pipeline
4. Memory–Thisstageisdifferentthenallpreviousstages.Thisalso implements the method render overridden from base class but it just takes care of memory instructions i.e STORE and LOAD. If its STORE it checks for memoryOperationType == Write and if its LOAD it checks for memoryOperationType == READ. So accordingly it performs the memory operation.
5. Writeback–Thisclassisresponsibleforwritingthecomputeddataformthe destination register to the RegisterFile. If the instruction is LOAD or Branch then WRITEBACK stage will perform No operation.


<b>Forwarding:</b>
I have implemented an Interface to create a DataBus named as IForwardDataBus. This interface is implemented by APEXPipeLineHandler and overrides method called as “writeResult”. Whenever an instruction is completed its 2nd stage and is about to enter to memory stage, the data set will be transferred to decode using this IForwardDataBus interface channel. Upon getting call, decode will check for the dependencies (Keeping list of Dependencies in instruction object) and resolves it.

<b>Dependencies:</b>
Implemented a class named ‘Dependency’ which keeps the information of Target instruction, register index over which dependency occurs, type of dependency (FLOW/ANTI/OUTPUT) this is just to replicate the original hardware and for future purpose. And the list of Dependency objects are kept in Instruction object.

<b>Memory:</b>
Created a SharedData singleton class which manages the Memory (int array of 4000 capacity).
Registers:
This is the implementation of RegisterFile in APEX. Same as the memory, registers are also managed using SharedData singleton class. It’s an integer array of 16 capacity, R0 to R15.
Stalls:
Stalls are handle in parallel to the logic of dependency. In decode stage every instruction waits if the dependency is not resolved.

<b>Functional Units:</b>
A base class implemented as FunctionalUnit, which will further be implemented by below two subclasses:
a. ALU–AfunctionalunitclassthattakescareofArithmeticoperations. b. Branch–Thisfunctionalunittakescareofbranchinstructions.

<b>User Interface:</b>

![Alt text](https://raw.githubusercontent.com/TechiezHub/APEXSimulator1.0/master/Screen%20Shot%202017-10-21%20at%209.08.08%20PM.png "Screen 1")


Button <b>“Choose File”</b> – This button shows a window to select a file of instructions to be executed.
Button <b>“Reset Simulator”</b> – This button is kept to reset the simulator in order to make it ready to be executed new set of instructions or may be the same one again.
Button <b>“Initialize”</b> – This button is used to read text file containing instructions and load into the ICache (Hashtable) mapped with ProgramCounter.
Button <b>“Simulate”</b> – This is the main button for this simulator. By clicking on this button we are actually starting executing the instructions. You can see that this button is followed with a textfield, currently showing “0”, this textfield accepts the count of cycles. This is the number of cycles instructions will pass through. If <b>“HALT”</b>
instruction came into execution before completing the number of cycles the process will be terminated.
<b>Memory section</b> – This section accepts the range of memory offsets to be displayed after execution is done.
Button <b>“Display”</b> – This button basically used to see the results.

![Alt text](https://raw.githubusercontent.com/TechiezHub/APEXSimulator1.0/master/Screen%20Shot%202017-10-21%20at%209.08.18%20PM.png "Screen 2")

After clicking on “Choose File” button, a dialog window appears as shown above, which prompts for choosing a file to be read.

![Alt text](https://raw.githubusercontent.com/TechiezHub/APEXSimulator1.0/master/Screen%20Shot%202017-10-21%20at%209.08.26%20PM.png "Screen 3")

Once the button <b>“Simulate”</b> is pressed and execution is completed, you can check the output by clicking on “Display” button. This will show the status of all cycles on Right Hand Side and Memory Values on Left Hand side.


<b>Glossary:</b>
ApexVirtualDevice – A class that binds the graphics with code. This is basically a
controller of Simulator.<br>
Initializer – This class is used to read the file for instructions.<br>
Constants – This is simply a class to declare application constants.<br>
Parser – Parser is class which helps in decomposing an instruction to better break down.<br>
Instruction – A model class to hold the information of an instruction<br>
Dependency – A model class which holds the information of dependent instructions.<br>
Once the dependency is resolved its getting removed.<br>
ICache – This class is the subclass of Hashtable<K,V> which is used to Map PC with Instruction and stored in a Key Value pair.<br>
Shared Data – This is a general utility to access the global data such as Register File, Memory and Program Counter.<br>
Functional Unit- This is a base class for any functional unit class.<br>
a. ALU–ThisisaderivedclassofFunctionalUnitwhichhandlesthe
Arithmetic instrictions.<br>
b. Branch–ThisisalsoaderivedclassofFunctionalUnitwhichhandles
the Branch instructions.<br>
ApexPipeLineHandler – This class is the main class where the pipeline mechanism is implemented.<br>
Stage- This is the base class for all stage classes.<br>
Fetch – This is for the fetch stage and derived from Stage class.<br>
Decode – This also inherits the Stage class.<br>
Execute– This also inherits the Stage class.<br>
Memory- This phase also inherits the class Stage.<br>
Writeback – This class is straightforward, it implements the logic to write values to register file. This is also a subtype of Stage class.<br>
