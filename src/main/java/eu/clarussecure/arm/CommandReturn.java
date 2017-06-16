/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.clarussecure.arm;

/**
 *
 * @author diegorivera
 */
public class CommandReturn{
	public final int returnValue;
	public final String returnInfo;

	public CommandReturn(int value, String info){
		this.returnValue = value;
		this.returnInfo = info;
	}

	public int getReturnValue(){
		return this.returnValue;
	}

	public String getReturnInfo(){
		return this.returnInfo;
	}
}
