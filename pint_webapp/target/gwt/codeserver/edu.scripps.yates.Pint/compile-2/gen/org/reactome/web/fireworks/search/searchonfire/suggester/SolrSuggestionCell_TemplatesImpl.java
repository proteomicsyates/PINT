package org.reactome.web.fireworks.search.searchonfire.suggester;

public class SolrSuggestionCell_TemplatesImpl implements org.reactome.web.fireworks.search.searchonfire.suggester.SolrSuggestionCell.Templates {
  
  public com.google.gwt.safehtml.shared.SafeHtml minCell(com.google.gwt.safehtml.shared.SafeHtml arg0,com.google.gwt.safehtml.shared.SafeHtml arg1,java.lang.String arg2) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div title=\"");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(arg2));
    sb.append("\" style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis;\">&nbsp;&nbsp;");
    sb.append(arg0.asString());
    sb.append("&nbsp;&nbsp;<span>");
    sb.append(arg1.asString());
    sb.append("</span></div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}

public com.google.gwt.safehtml.shared.SafeHtml cell(com.google.gwt.safehtml.shared.SafeHtml arg0,com.google.gwt.safehtml.shared.SafeHtml arg1,com.google.gwt.safehtml.shared.SafeHtml arg2,java.lang.String arg3) {
StringBuilder sb = new java.lang.StringBuilder();
sb.append("<div title=\"");
sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(arg3));
sb.append("\" style=\"overflow:hidden; width:100%;\"><div style=\"float:left;margin: 7px 0 0 5px\">");
sb.append(arg0.asString());
sb.append("</div><div style=\"float:left;margin-left:10px; max-width:260px\"><div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; font-size:small\">");
sb.append(arg1.asString());
sb.append("</div><div style=\"overflow:hidden; white-space:nowrap; text-overflow:ellipsis; margin-top:-2px; font-size:x-small;\">");
sb.append(arg2.asString());
sb.append("</div></div></div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
