package com.r2s.mobile_store.application.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
    private Integer id;
    private String username;
    private String email;
    private String fullname;
    private String role;
}
