package at.ac.tuwien.ims.sinking.GameEngine;

public class Lane {
    public String name;
    public int height;
    public int start;
    public int end;

    public Lane(String inName, int inHeight, int inStart, int inEnd) {
        name = inName;
        height = inHeight;
        start = inStart;
        end = inEnd;
    }
}
