package com.google.gwt.user.client.ui;

public class ImageResourceRenderer_TemplateImpl implements com.google.gwt.user.client.ui.ImageResourceRenderer.Template {
  
  public com.google.gwt.safehtml.shared.SafeHtml image(com.google.gwt.safehtml.shared.SafeUri arg0,int arg1,int arg2) {
    StringBuilder sb = new java.lang.StringBuilder();
    sb.append("<img src='");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(arg0.asString()));
    sb.append("' border='0' width='");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(String.valueOf(arg1)));
    sb.append("' height='");
    sb.append(com.google.gwt.safehtml.shared.SafeHtmlUtils.htmlEscape(String.valueOf(arg2)));
    sb.append("'>");
return new com.google.gwt.safehtml.shared.OnlyToBeUsedInGeneratedCodeStringBlessedAsSafeHtml(sb.toString());
}
}
