package codezilla.hendynest.model;

import java.util.List;

public record ResponseDto(
        String status,
        List<ResultDto> result
) {}

