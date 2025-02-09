package com.meac.todolist_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class CustomizedExceptions  {

    @ResponseStatus(HttpStatus.CONFLICT)
   public static class EmailAlreadyExistsException extends RuntimeException {
       public EmailAlreadyExistsException(String message) {
           super(message);
       }
   }

   @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class BadRequestException extends RuntimeException {
        public BadRequestException(String message) {
            super(message);
        }
   }


}
