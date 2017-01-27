package com.cao.apex.pipeline;

import com.cao.apex.models.Dependency;
import com.cao.apex.models.SharedData;
import com.cao.apex.utility.Constants;

public class Decode extends Stage {

	public Decode(APEXPipelineHandler handler) {
		super(handler);
	}

	@Override
	public boolean render() {
		// TODO Auto-generated method stub

		instruction.setSrc1Data(
				SharedData.getInstance().readRegisterFileForRegister(instruction.getSrc1()));

		if(!instruction.containsLiteral())
			instruction.setSrc2Data(
					SharedData.getInstance().readRegisterFileForRegister(instruction.getSrc2()));


		if(instruction.isMemoryOperation()) {
			instruction.calculateMemoryAddress();
		}

		if(instruction.getOpcode().equalsIgnoreCase(Constants.STORE)) {
			instruction.setDestResult(SharedData.getInstance().readRegisterFileForRegister(instruction.getDest()));
		}

		//resolveDependecies();

		return true;
	}

	

	/**
	 * Check for the dependencies and add references of it.
	 */
	public void addDependecies() {
		// Execution Stage

		if(instruction == null || nextStage.instruction == null) return;

		Stage iNextStage = nextStage;
		
		if(instruction.getOpcode().equalsIgnoreCase(Constants.LOAD) || instruction.getOpcode().equalsIgnoreCase(Constants.STORE)) {
			while(iNextStage != null && iNextStage.instruction != null) {
				if(instruction.getOpcode().equalsIgnoreCase(Constants.LOAD) || instruction.getOpcode().equalsIgnoreCase(Constants.STORE)) {

					if((instruction.getOpcode().equalsIgnoreCase(Constants.STORE) && instruction.getDest() == iNextStage.instruction.getDest()) ||
							(iNextStage.instruction.getDest() == instruction.getSrc1() ||
									iNextStage.instruction.getDest() == instruction.getSrc2())) {
						Dependency dependency = new Dependency();

						dependency.setInstruction(iNextStage.instruction);
						dependency.setType(Dependency.Types.FLOW);
						dependency.setCanBeIgnored(false);
						dependency.setReg(iNextStage.instruction.getDest());

						instruction.addDependency(dependency);
					}
				}
				iNextStage = iNextStage.nextStage;
			}
			return;
		}

		if(instruction.getOpcode().equalsIgnoreCase(Constants.BNZ) || instruction.getOpcode().equalsIgnoreCase(Constants.BZ)) {

			Dependency dependency = new Dependency();

			dependency.setInstruction(nextStage.instruction);
			dependency.setType(Dependency.Types.FLOW);
			dependency.setCanBeIgnored(false);
			dependency.setReg(nextStage.instruction.getDest());

			instruction.addDependency(dependency);
			return;		
		} 

		
		iNextStage = nextStage;
		while(iNextStage != null && iNextStage.instruction != null) {
			if(iNextStage.instruction.getDest() == instruction.getSrc1() ||
					iNextStage.instruction.getDest() == instruction.getSrc2()) {
				Dependency dependency = new Dependency();

				dependency.setInstruction(iNextStage.instruction);
				dependency.setType(Dependency.Types.FLOW);
				dependency.setCanBeIgnored(false);
				dependency.setReg(iNextStage.instruction.getDest());

				instruction.addDependency(dependency);
			}
			iNextStage = iNextStage.nextStage;
		}
	}

	@Override
	public boolean canMoveToNext() {
		// TODO If the instruction is store send to first ALU Stage
		if(instruction == null) return true; 
		return handleStore() ||  ((instruction.getDependencies() == null || instruction.getDependencies().size() == 0)
				&& super.canMoveToNext());
	}

	private boolean handleStore() {
		if(instruction.getOpcode().equalsIgnoreCase(Constants.STORE)) {
			
			return instruction.getSrc1Data() != Constants.INVALID;
			
		}
		return false;
	}

}
