package org.cinrc.process.nodes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.cinrc.util.SetUtil;

public class RestrictionPlaceholder implements IRDCObject {

  private final List<Label> restrictions;

  public RestrictionPlaceholder(List<Label> restrictions){
    this.restrictions = new ArrayList<>(restrictions);
  }

  public void addRestriction(LabelNode... restriction){
    Collections.addAll(restrictions, restriction);
  }

  public List<Label> getRestrictions(){
    return restrictions;
  }

  public String represent(){
    return SetUtil.csvSet(getRestrictions());
  }

}
