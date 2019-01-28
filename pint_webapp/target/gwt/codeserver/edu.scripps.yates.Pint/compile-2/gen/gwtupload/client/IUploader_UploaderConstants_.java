package gwtupload.client;

public class IUploader_UploaderConstants_ implements gwtupload.client.IUploader.UploaderConstants {
  
  public java.lang.String uploaderActiveUpload() {
    return "There is already an active upload, try later.";
  }
  
  public java.lang.String uploaderAlreadyDone() {
    return "This file was already uploaded.";
  }
  
  public java.lang.String uploaderBlobstoreError() {
    return "It seems the application is configured to use GAE blobstore.\nThe server has raised an error while creating an Upload-Url\nBe sure thar you have enabled billing for this application in order to use blobstore.";
  }
  
  public java.lang.String uploaderBrowse() {
    return "Choose a file to upload ...";
  }
  
  public java.lang.String uploaderInvalidExtension() {
    return "Invalid file.\nOnly these types are allowed:\n";
  }
  
  public java.lang.String uploaderSend() {
    return "Send";
  }
  
  public java.lang.String uploaderServerError() {
    return "Invalid server response. Have you configured correctly your application in the server side?";
  }
  
  public java.lang.String submitError() {
    return "Unable to auto submit the form, it seems your browser has security issues with this feature.\n Developer Info: If you are using jsupload and you do not need cross-domain, try a version compiled with the standard linker?";
  }
  
  public java.lang.String uploaderServerUnavailable() {
    return "Unable to contact with the server: ";
  }
  
  public java.lang.String uploaderTimeout() {
    return "Timeout sending the file:\n perhaps your browser does not send files correctly,\n your session has expired,\n or there was a server error.\nPlease try again.";
  }
  
  public java.lang.String uploaderBadServerResponse() {
    return "Error uploading the file, the server response has a format which can not be parsed by the application.\n.";
  }
  
  public java.lang.String uploaderBlobstoreBilling() {
    return "Additional information: it seems that you are using blobstore, so in order to upload large files check that your application is billing enabled.";
  }
  
  public java.lang.String uploaderInvalidPathError() {
    return "Error you have typed an invalid file name, please select a valid one.";
  }
  
  public java.lang.String uploadLabelCancel() {
    return " ";
  }
  
  public java.lang.String uploadStatusCanceled() {
    return "Canceled";
  }
  
  public java.lang.String uploadStatusCanceling() {
    return "Canceling ...";
  }
  
  public java.lang.String uploadStatusDeleted() {
    return "Deleted";
  }
  
  public java.lang.String uploadStatusError() {
    return "Error";
  }
  
  public java.lang.String uploadStatusInProgress() {
    return "In progress";
  }
  
  public java.lang.String uploadStatusQueued() {
    return "Queued";
  }
  
  public java.lang.String uploadStatusSubmitting() {
    return "Submitting form ...";
  }
  
  public java.lang.String uploadStatusSuccess() {
    return "Done";
  }
}
