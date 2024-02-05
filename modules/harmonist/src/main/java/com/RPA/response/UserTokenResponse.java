package com.RPA.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Schema(description = "Ответ c токеном доступа")
public record UserTokenResponse (
        @Schema(implementation = UserInfoResponse.class)
        UserInfoResponse userData,

        @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
        String jsonAuth
) { }
