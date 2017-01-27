package com.cao.apex.pipeline;

import com.cao.apex.buses.IForwardingDataBus;
import com.cao.apex.models.SharedData;

public class Writeback extends Stage {
	
	public IForwardingDataBus dataBus;

	public Writeback(APEXPipelineHandler handler) {
		super(handler);
	}

	@Override
	public boolean render() {
		
		if(instruction.isMemoryOperation()) return true;
		
		SharedData.getInstance().updateRegister(
				instruction.getDest(), instruction.getDestResult());
		
		if(dataBus != null)
			dataBus.writeResult(instruction);
		
		return true;
		

	}
	
	@Override
	public boolean canMoveToNext() {
		// TODO Auto-generated method stub
		return true;
	}

}
