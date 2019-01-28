package com.google.gwt.user.cellview.client;

public class CellList_TemplateImpl implements com.google.gwt.user.cellview.client.CellList.Template {
  
  public com.google.gwt.safehtml.shared.SafeHtml div(int arg0,java.lang.String arg1,com.google.gwt.safehtml.shared.SafeHtml arg2) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<div __idx=\"");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(String.valueOf(arg0)));
    sb.append("\" class=\"");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(arg1));
    sb.append("\" style=\"outline:none;\" >");
    sb.append(arg2.asString());
    sb.append("</div>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
