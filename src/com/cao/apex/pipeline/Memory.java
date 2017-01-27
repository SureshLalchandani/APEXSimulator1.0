package com.cao.apex.pipeline;

import com.cao.apex.models.Instruction.MemoryDirection;
import com.cao.apex.models.SharedData;

public class Memory extends Stage {

	public Memory(APEXPipelineHandler handler) {
		super(handler);
	}

	@Override
	public boolean render() {

		if(instruction.isMemoryOperation()) {
			// In case of LOAD instruction
			if(instruction.getMemOp() == MemoryDirection.READ) {
				instruction.setDestResult(
						SharedData.getInstance().readMemoryForLocation(
								instruction.getMemoryAddres()));
			} else { //In case of STORE Instruction
				SharedData.getInstance().updateMemoryLocation(
						instruction.getMemoryAddres(), instruction.getDestResult());
			}
		}

		
//		if(dataBus != null)
//			dataBus.writeResult(instruction);

		
		return true;
	}
	
	@Override
	public void forwardData() {
		if(instruction != null)
		super.forwardData();
	}
	
	@Override
	public boolean canMoveToNext() {
		// TODO Auto-generated method stub
		return true;
	}
}
