package com.gdg_team9.SafePlate.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.gdg_team9.SafePlate.api.code.BaseErrorCode;
import com.gdg_team9.SafePlate.api.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{

    private BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
