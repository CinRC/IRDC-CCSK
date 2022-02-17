package parser;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * An implementation of an iterable char stream. Self contained
 * to be able to provide the ability to "walk" along a string
 * and perform operations on the way. In my mind I picture it
 * as an ant or something "walking" along a (literal) string.
 */
public class StringWalker implements Iterable<Character>, CharSequence{
    private String stringBase;
    private int curPos = 0;
    private Direction direction = Direction.FORWARDS;
    private ArrayDeque<Character> memory;
    public int length;

    //Overloading ftw
    public StringWalker(String s) {
        new StringWalker(s, 0);
    }

    public StringWalker(String s, int pos){
        new StringWalker(s,pos,Direction.FORWARDS);
    }

    public StringWalker(String s, int pos, Direction dir){
        stringBase = s;
        length = s.length();
        this.curPos = pos;
        setDirection(dir);
        memory = new ArrayDeque<>();
    }

    /**
     * Retrieves the character that the walker is standing on
     * @return Character that the walker is standing on
     */
    public char read(){
        return charAt(curPos);
    }

    /**
     * Walks 1 character along the string, saving the *destination* char to memory
     */
    public void walk(){
        if (!canWalk())
            throw new IndexOutOfBoundsException(
                    String.format("String walker cannot walk to index %d with string length %d",
                            curPos, length()));
        curPos += getDirection().inc;
        memory.push(read());
    }

    /**
     * Reads walker memory. Does not clear
     * @return Memory as an ArrayDequeue
     */
    public ArrayDeque<Character> readMemory() {
        return memory;
    }

    /**
     *
     * @return True if the walker has more string to walk
     */
    public boolean canWalk(){
        return (curPos + getDirection().inc >= length()
        || curPos + getDirection().inc < 0);
    }

    /**
     * Clears memory
     */
    public void clearBuffer(){
        memory.clear();
    }

    /**
     * Gets current walking direction
     * @return Walker direction 0: Forwards, 1: Backwards
     */
    public Direction getDirection(){
        return direction;
    }

    /**
     * Set position of walker on string
     * @param i Position of the walker
     */
    public void setPos(int i){
        curPos = i;
        if (curPos >= length() || curPos < 0)
            throw new IndexOutOfBoundsException(
                    String.format("String walker cannot be set to index %d with string length %d",
                            curPos, length()));
    }

    /**
     * Changes direction of the walker
     * @param dir Direction
     */
    public void setDirection(Direction dir){
        direction = dir;
    }

    /**
     * Reverses the string that the walker is traversing,
     * preserving walker position and direction
     */
    @Deprecated
    public void reverseBase(){
        StringBuilder s = new StringBuilder();
        for (int i = this.stringBase.length()-1; i >= 0; i--)
            s.append(this.stringBase.charAt(i));
        this.stringBase = s.toString();
    }

    /**
     * Reverses given string
     * -Fundamentally unnecessary, but keeping for future
     * @param st String to reverse
     * @return Reverse of the given string
     */
    @Deprecated
    private String reverseString(String st){
        StringBuilder s = new StringBuilder();
        for (int i = st.length()-1; i >= 0; i--)
            s.append(st.charAt(i));
        return s.toString();
    }

    /**
     * Reverse walker position on string, ooOoooo -> ooooOoo
     * -Fundamentally unnecessary, but keeping for future
     */
    @Deprecated
    public void reverseWalker(){
        this.curPos = this.length - this.curPos;
    }

    /**
     * Switches direction of walker
     * @return Walker with reverse direction
     */
    public StringWalker reverseClone(){
        clearBuffer();
        return new StringWalker(stringBase,curPos,getDirection().reverse());
    }

    /**
     * Reverses walker direction
     */
    public void reverse(){
        clearBuffer();
        setDirection(getDirection().reverse());
    }

    //Impl methods
    @Override
    public Iterator<Character> iterator() {
        return new Iterator<>() {
            public boolean hasNext() {
                return curPos < length;
            }
            public Character next() {
                return stringBase.charAt(curPos++);
            }
        };
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return stringBase.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return stringBase.subSequence(start,end);
    }

    /**
     * Purely for convenience. Whether or not this is practical is highly debatable
     */
    private enum Direction{
        FORWARDS(1),
        BACKWARDS(-1);
        public int inc;
        Direction(int inc){ this.inc = inc; }
        Direction reverse(){
            return this == FORWARDS ? BACKWARDS : FORWARDS;
        }
    }

}
