package com.nttbank.microservices.authjwtserver.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserCreatedResponse {

  private String status;
  private String message;

}
