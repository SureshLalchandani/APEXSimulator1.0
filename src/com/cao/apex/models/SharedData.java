package com.cao.apex.models;

import java.util.Arrays;

import com.cao.apex.utility.Constants;

/**
 * Global data wrapper class
 * @author sureshlalchandani
 *
 */
public class SharedData {

	private int[] registerFile = new int[16];
	private int[] memory = new int[4000];
	private int[] specialRegisters = new int[3];
	private int pc;


	private static SharedData _instance;

	public static synchronized SharedData getInstance() {

		if(_instance == null) {
			return newInstance();
		}

		return _instance;
	}

	public static synchronized SharedData newInstance() {

		_instance = new SharedData();
		
		//Initialize all element in register file with 0
		_instance.registerFile = new int[16];
		Arrays.fill(_instance.registerFile, -1);
		
		//Initialize all element in memory with 0
		_instance.memory = new int[4000];
		Arrays.fill(_instance.memory, -1);
		
		_instance.pc = -1;
		
		return _instance;

	}

	public int readSpecialReg(int reg) {
		return reg >=0 && reg < specialRegisters.length ? specialRegisters[reg] : Constants.INVALID;
	}
	
	public void updateSplRegister(int reg, int value) {
		specialRegisters[reg] = value;
	}

	public int readRegisterFileForRegister(int reg) {
		return reg >=0 && reg < registerFile.length ? registerFile[reg] : Constants.INVALID;
	}
	
	public int[] getRegisterFile() {
		return registerFile;
	}

	public int readMemoryForLocation(int offset) {
		return offset >=0 && offset < memory.length ? memory[offset] : Constants.INVALID;
	}

	public void updateRegister(int reg, int value) {
		if(reg < 0 || reg > registerFile.length) return;
		registerFile[reg] = value;
	}

	public void updateMemoryLocation(int offset, int value) {
		if(offset < 0 || offset > memory.length) return;
		memory[offset] = value;
	}
	
	public int getPc() {
		return this.pc;

	}
	
	public void setPc(int pc) {
		this.pc = pc;
	}
	
	public void incrementPC() {
		pc += 4;
	}

}
