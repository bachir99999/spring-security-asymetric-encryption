package com.bachir9.app.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ChangePasswordRequest {
    @NotBlank(message = "VALIDATION.CHANGE_PASSWORD.OLD_PASSWORD.BLANK")
    @Size(min = 8,
            max = 72,
            message = "VALIDATION.CHANGE_PASSWORD.OLD_PASSWORD.SIZE"
    )
    @Schema(example = "pAssword1!_")
    private String oldPassword;

    @NotBlank(message = "VALIDATION.CHANGE_PASSWORD.PASSWORD.BLANK")
    @Size(min = 8,
            max = 72,
            message = "VALIDATION.CHANGE_PASSWORD.PASSWORD.SIZE"
    )
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W).*$",
            message = "VALIDATION.CHANGE_PASSWORD.PASSWORD.WEAK"
    )
    @Schema(example = "pAssword1!_")
    private String newPassword;

    @NotBlank(message = "VALIDATION.CHANGE_PASSWORD.CONFIRM_PASSWORD.BLANK")
    @Size(min = 8,
            max = 72,
            message = "VALIDATION.CHANGE_PASSWORD.CONFIRM_PASSWORD.SIZE"
    )
    @Schema(example = "pAssword1!_")
    private String confirmPassword;
}
