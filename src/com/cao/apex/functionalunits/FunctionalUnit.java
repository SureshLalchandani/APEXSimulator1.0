package com.cao.apex.functionalunits;

import com.cao.apex.buses.IForwardingDataBus;
import com.cao.apex.models.Instruction;
import com.cao.apex.pipeline.Stage;

public abstract class FunctionalUnit {
	
	
	protected Instruction instruction;
	private IForwardingDataBus dataBus;
	private Stage stage;
	
	public void setInstruction(Instruction instruction) {
		this.instruction = instruction;
	}
	
	public void setDataBus(IForwardingDataBus dataBus) {
		this.dataBus = dataBus;
	}
	
	public IForwardingDataBus getDataBus() {
		return dataBus;
	}
	
	/**
	 * Method to perform appropriate functional unit operation.
	 */
	public abstract void run();
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public Stage getStage() {
		return stage;
	}

}
