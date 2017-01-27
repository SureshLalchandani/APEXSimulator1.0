package com.cao.apex.pipeline;

import com.cao.apex.functionalunits.ALU;
import com.cao.apex.functionalunits.Branch;
import com.cao.apex.models.Instruction;
import com.cao.apex.utility.Constants;

public class Execute extends Stage {

	private int stage = 0;

	public Execute(APEXPipelineHandler handler, int stage) {
		super(handler);
		requiredCycle = 2;
		this.stage = stage;
	}

	@Override
	public void updateInstruction(Instruction instruction) {
		super.updateInstruction(instruction);
		//cycleCount = 0;
	}

	@Override
	public boolean render() {

		//cycleCount += 1;

		if(stage == 2 && !instruction.isBranch()) {
			ALU alu = new ALU();
			alu.setInstruction(instruction);
			alu.run();

		} else if(stage == 1) {
			Branch branch = new Branch();
			branch.setStage(this);
			branch.setInstruction(instruction);
			branch.setDataBus(handler);
			branch.run();
		}

//		if(!instruction.isMemoryOperation())
//			if(stage == 2 && dataBus != null)
//				dataBus.writeResult(instruction);

		return true;
	}
	
	@Override
	public void forwardData() {
		if(instruction != null && !instruction.isMemoryOperation() && stage == 2)
		super.forwardData();
	}
	

	public boolean needMoreCycle() {
		return false;
	}


	@Override
	public boolean canMoveToNext() {
		if(instruction == null) return true;

		// TODO Auto-generated method stub
		return stage == 2 ? true : !instruction.getOpcode().equalsIgnoreCase(Constants.STORE) ? true : ( instruction.getDependencies() == null || instruction.getDependencies().size() == 0) ? true : false;
	}
}
