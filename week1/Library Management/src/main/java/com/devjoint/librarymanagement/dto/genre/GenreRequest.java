package com.devjoint.librarymanagement.dto.genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenreRequest {

    @NotBlank(message = "Genre adı boş ola bilməz")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arasında olmalıdır")
    private String name;

    @Size(max = 500, message = "Açıqlama maksimum 500 simvol ola bilər")
    private String description;

}