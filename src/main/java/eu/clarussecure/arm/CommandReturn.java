package eu.clarussecure.arm;

public class CommandReturn {
    public final int returnValue;
    public final String returnInfo;

    public CommandReturn(int value, String info) {
        this.returnValue = value;
        this.returnInfo = info;
    }

    public int getReturnValue() {
        return this.returnValue;
    }

    public String getReturnInfo() {
        return this.returnInfo;
    }
}
