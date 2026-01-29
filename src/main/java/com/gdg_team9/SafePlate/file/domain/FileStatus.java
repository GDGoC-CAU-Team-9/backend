package com.gdg_team9.SafePlate.file.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileStatus {
    UPLOADING(false),
    UPLOADED(true),
    ERROR(false),
    ;

    private final boolean available;
}
