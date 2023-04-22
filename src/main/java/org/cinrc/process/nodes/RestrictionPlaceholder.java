package org.cinrc.process.nodes;

import java.util.ArrayList;
import java.util.List;

public class RestrictionPlaceholder implements IRDCObject {

  private List<Label> restrictions;

  public RestrictionPlaceholder(List<Label> restrictions){
    this.restrictions = new ArrayList<>(restrictions);
  }

  public void addRestriction(LabelNode... restriction){
    for (LabelNode n : restriction)
      restrictions.add(n);
  }

  public List<Label> getRestrictions(){
    return restrictions;
  }

}
