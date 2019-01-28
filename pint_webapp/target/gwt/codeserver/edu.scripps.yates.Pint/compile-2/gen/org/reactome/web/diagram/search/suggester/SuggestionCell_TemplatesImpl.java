package org.reactome.web.diagram.search.suggester;

public class SuggestionCell_TemplatesImpl implements org.reactome.web.diagram.search.suggester.SuggestionCell.Templates {
  
  public com.google.gwt.safehtml.shared.SafeHtml minCell(com.google.gwt.safehtml.shared.SafeHtml arg0,com.google.gwt.safehtml.shared.SafeHtml arg1) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\"><div style=\"float:left; margin-left: 5px\">");
    sb.append(arg0.asString());
    sb.append("</div><div style=\"float:left; margin-left:10px; width:260px\"><div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small\">");
    sb.append(arg1.asString());
    sb.append("</div></div></div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}

public com.google.gwt.safehtml.shared.SafeHtml cell(com.google.gwt.safehtml.shared.SafeHtml arg0,com.google.gwt.safehtml.shared.SafeHtml arg1,com.google.gwt.safehtml.shared.SafeHtml arg2) {
StringBuilder sb = new java.lang.StringBuilder();
sb.append("<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\"><div style=\"float:left;margin: 7px 0 0 5px\">");
sb.append(arg0.asString());
sb.append("</div><div style=\"float:left;margin-left:10px; width:260px\"><div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small\">");
sb.append(arg1.asString());
sb.append("</div><div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; margin-top:-2px; font-size:x-small;\">");
sb.append(arg2.asString());
sb.append("</div></div></div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
