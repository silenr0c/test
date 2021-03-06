
package org.apache.jackrabbit.cmis.ws.repository;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.1.3
 * Thu Dec 11 22:50:43 CET 2008
 * Generated source version: 2.1.3
 * 
 */

@WebFault(name = "constraintViolationException", targetNamespace = "http://www.cmis.org/2008/05")
public class ConstraintViolationException extends Exception {
    public static final long serialVersionUID = 20081211225043L;
    
    private org.apache.jackrabbit.cmis.ws.repository.ConstraintViolationExceptionType constraintViolationException;

    public ConstraintViolationException() {
        super();
    }
    
    public ConstraintViolationException(String message) {
        super(message);
    }
    
    public ConstraintViolationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstraintViolationException(String message, org.apache.jackrabbit.cmis.ws.repository.ConstraintViolationExceptionType constraintViolationException) {
        super(message);
        this.constraintViolationException = constraintViolationException;
    }

    public ConstraintViolationException(String message, org.apache.jackrabbit.cmis.ws.repository.ConstraintViolationExceptionType constraintViolationException, Throwable cause) {
        super(message, cause);
        this.constraintViolationException = constraintViolationException;
    }

    public org.apache.jackrabbit.cmis.ws.repository.ConstraintViolationExceptionType getFaultInfo() {
        return this.constraintViolationException;
    }
}
