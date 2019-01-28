package org.reactome.web.fireworks.search.fallback.suggester;

public class SuggestionCell_TemplatesImpl implements org.reactome.web.fireworks.search.fallback.suggester.SuggestionCell.Templates {
  
  public com.google.gwt.safehtml.shared.SafeHtml cell(com.google.gwt.safehtml.shared.SafeHtml arg0,com.google.gwt.safehtml.shared.SafeHtml arg1) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\">&nbsp;&nbsp;");
    sb.append(arg0.asString());
    sb.append("&nbsp;&nbsp;<span>");
    sb.append(arg1.asString());
    sb.append("</span></div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
