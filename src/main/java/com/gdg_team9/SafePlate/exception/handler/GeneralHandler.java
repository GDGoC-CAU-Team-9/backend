package com.gdg_team9.SafePlate.exception.handler;

import com.gdg_team9.SafePlate.api.code.BaseErrorCode;
import com.gdg_team9.SafePlate.exception.GeneralException;

public class GeneralHandler extends GeneralException {

    public GeneralHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
