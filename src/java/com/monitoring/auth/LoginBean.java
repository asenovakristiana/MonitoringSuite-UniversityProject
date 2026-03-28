 package com.monitoring.auth;
 
 import java.io.Serializable;
 import javax.faces.application.FacesMessage;
 import javax.faces.bean.ManagedBean;
 import javax.faces.bean.SessionScoped;
 import javax.faces.context.FacesContext;
 
 
 
 
 
 
 
 
 
 
 
 @ManagedBean(name = "loginBean")
 @SessionScoped
 public class LoginBean
   implements Serializable
 {
   private String username;
   private String password;
   private boolean loggedIn;
   
   public String doLogin() {
     if ("admin".equals(this.username) && "admin".equals(this.password)) {
       this.loggedIn = true;
       return "/secured/home?faces-redirect=true";
     } 
     
     FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Грешни данни", "Невалидно име или парола"));
 
     
     return null;
   }
   
   public String doLogout() {
     FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
     return "/login?faces-redirect=true";
   }
   
   public boolean isLoggedIn() { return this.loggedIn; }
   public String getUsername() { return this.username; }
   public void setUsername(String username) { this.username = username; }
   public String getPassword() { return this.password; } public void setPassword(String password) {
     this.password = password;
   }
 }


