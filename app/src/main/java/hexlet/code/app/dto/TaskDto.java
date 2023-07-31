package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @Size(min = 1)
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long authorId;

    private Long executorId;

    @NotNull
    private Long taskStatusId;

}
