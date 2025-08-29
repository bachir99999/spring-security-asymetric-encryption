package com.bachir9.app.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProfileUpdateRequest {

    @NotBlank(message = "VALIDATION.PROFILE_UPDATE.FIRST_NAME.BLANK")
    @Size(min = 1, max = 50,  message = "VALIDATION.PROFILE_UPDATE.FIRST_NAME.SIZE")
    @Pattern(
            regexp = "^[\\p{L} '-]+$",
            message = "VALIDATION.PROFILE_UPDATE.FIRST_NAME.PATTERN"
    )
    @Schema(example = "Bachir")
    private String firstName;

    @NotBlank(message = "VALIDATION.PROFILE_UPDATE.LAST_NAME.BLANK")
    @Size(
            min = 1,
            max = 50,
            message = "VALIDATION.PROFILE_UPDATE.LAST_NAME.SIZE"
    )
    @Pattern(
            regexp = "^[\\p{L} '-]+$",
            message = "VALIDATION.PROFILE_UPDATE.LAST_NAME.PATTERN"
    )
    @Schema(example = "Benmalek")
    @Size(min = 2, max = 30)
    private String lastName;

    @NotNull(message = "VALIDATION.PROFILE_UPDATE.DATE_OF_BIRTH.NULL")
    @Past(message = "VALIDATION.PROFILE_UPDATE.DATE_OF_BIRTH.PAST")
    @Schema(example = "1945-05-28")
    private LocalDate dateOfBirth;

}
