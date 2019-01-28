package org.reactome.web.diagram.context.dialogs.molecules;

public class ExpressionCell_TemplateImpl implements org.reactome.web.diagram.context.dialogs.molecules.ExpressionCell.Template {
  
  public com.google.gwt.safehtml.shared.SafeHtml std(com.google.gwt.safehtml.shared.SafeHtml arg0) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div style=\"color:black\">");
    sb.append(arg0.asString());
    sb.append("</div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}

public com.google.gwt.safehtml.shared.SafeHtml exp(com.google.gwt.safecss.shared.SafeStyles arg0,com.google.gwt.safehtml.shared.SafeHtml arg1) {
StringBuilder sb = new java.lang.StringBuilder();
sb.append("<div style=\"");
sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0.asString()));
sb.append(" color:black\">");
sb.append(arg1.asString());
sb.append("</div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
