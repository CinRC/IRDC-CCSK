package me.gmx.parser;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An implementation of an iterable char stream. Self contained
 * to be able to provide the ability to "walk" along a string
 * and perform operations on the way. In my mind I picture it
 * as an ant or something "walking" along a (literal) string.
 */
public class StringWalker implements Iterable<Character>, CharSequence {
  private final LinkedList<Character> memory;
  private final Collection<Character> ignore = new HashSet<>();
  public int length;
  private String stringBase;
  private int curPos;
  private Direction direction = Direction.FORWARDS;

  //Overloading ftw
  public StringWalker(String s) {
    this.stringBase = s;
    length = s.length();
    this.curPos = -1;
    setDirection(Direction.FORWARDS);
    memory = new LinkedList<>();
  }

  public StringWalker(String s, int pos) {
    this.stringBase = s;
    length = s.length();
    this.curPos = pos;
    setDirection(Direction.FORWARDS);
    memory = new LinkedList<>(Collections.singleton(s.charAt(curPos)));
  }


  public StringWalker(String s, int pos, Direction dir) {
    this.stringBase = s;
    length = s.length();
    this.curPos = pos;
    setDirection(dir);
    memory = new LinkedList<>(Collections.singleton(s.charAt(curPos)));
  }

  public void setIgnore(Character... ch) {
    this.ignore.addAll(List.of(ch));
  }

  /**
   * Retrieves the character that the walker is standing on
   *
   * @return Character that the walker is standing on
   */
  public char read() {
    return charAt(curPos);
  }

  /**
   * Walks 1 character along the string, saving the *destination* char to memory
   */
  public void walk() {
    if (!canWalk()) {
      throw new IndexOutOfBoundsException(
          String.format("String walker cannot walk to index %d with string length %d",
              curPos, length()));
    }
    curPos += getDirection().inc;
    if (!ignore.contains(read())) {
      memory.addLast(read());
    }
  }

  /**
   * Overload walk method
   *
   * @param readMemory Should memory be stored?
   */
  public void walk(boolean readMemory) {
    if (!canWalk()) {
      throw new IndexOutOfBoundsException(
          String.format("String walker cannot walk to index %d with string length %d",
              curPos, length()));
    }
    curPos += getDirection().inc;
    if (readMemory) {
      if (!ignore.contains(read())) {
        memory.addLast(read());
      }
    }
  }

  /**
   * Looks ahead to the rest of the string it has to walk, in addition to its own memory
   *
   * @return The memory it would return if it were to walk until the end of the string
   */
  public String look() {
    return readMemory() + stringBase.substring(this.curPos + 1, this.length);
  }

  /**
   * Look at the next character
   *
   * @return Next character in order
   */
  public String peek() {
    return String.valueOf(charAt(curPos + getDirection().inc));
  }

  /**
   * Destroys the character it is currently standing on
   */
  public void stomp() {
    stringBase = stringBase.substring(0, curPos) + stringBase.substring(curPos + 1);
  }

  /**
   * Reads walker memory. Does not clear
   *
   * @return Memory as a string
   */
  public String readMemory() {
    StringBuilder sb = new StringBuilder();
    memory.forEach(sb::append);
    return sb.toString();
  }

  /**
   * @return True if the walker has more string to walk
   */
  public boolean canWalk() {
    return !(curPos + getDirection().inc >= length()
        || curPos + getDirection().inc < 0);
  }

  /**
   * Clears memory
   */
  public void clearMemory() {
    memory.clear();
  }

  /**
   * Gets current walking direction
   *
   * @return Walker direction 0: Forwards, 1: Backwards
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * Changes direction of the walker
   *
   * @param dir Direction
   */
  public void setDirection(Direction dir) {
    direction = dir;
  }

  /**
   * Set position of walker on string
   *
   * @param i Position of the walker
   */
  public void setPos(int i) {
    curPos = i;
    if (curPos >= length() || curPos < 0) {
      throw new IndexOutOfBoundsException(
          String.format("String walker cannot be set to index %d with string length %d",
              curPos, length()));
    }
  }

  /**
   * Reverses the string that the walker is traversing,
   * preserving walker position and direction
   */
  @Deprecated
  public void reverseBase() {
    StringBuilder s = new StringBuilder();
    for (int i = this.stringBase.length() - 1; i >= 0; i--) {
      s.append(this.stringBase.charAt(i));
    }
    this.stringBase = s.toString();
  }

  /**
   * Reverses given string
   * -Fundamentally unnecessary, but keeping for future
   *
   * @param st String to reverse
   * @return Reverse of the given string
   */
  @Deprecated
  private String reverseString(String st) {
    StringBuilder s = new StringBuilder();
    for (int i = st.length() - 1; i >= 0; i--) {
      s.append(st.charAt(i));
    }
    return s.toString();
  }

  /**
   * Reverse walker position on string, ooOoooo -> ooooOoo
   * -Fundamentally unnecessary, but keeping for future
   */
  @Deprecated
  public void reverseWalker() {
    this.curPos = this.length - this.curPos;
  }

  /**
   * Switches direction of walker
   *
   * @return Walker with reverse direction
   */
  public StringWalker reverseClone() {
    clearMemory();
    return new StringWalker(stringBase, curPos, getDirection().reverse());
  }


  /**
   * Reverses walker direction
   */
  public void reverse() {
    clearMemory();
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
    return this.stringBase.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return stringBase.subSequence(start, end);
  }

  /**
   * Purely for convenience. Whether or not this is practical is highly debatable
   */
  public enum Direction {
    FORWARDS(1),
    BACKWARDS(-1);
    public final int inc;

    Direction(int inc) {
      this.inc = inc;
    }

    Direction reverse() {
      return this == FORWARDS ? BACKWARDS : FORWARDS;
    }
  }

}
