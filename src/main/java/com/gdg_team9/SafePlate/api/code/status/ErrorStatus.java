package com.gdg_team9.SafePlate.api.code.status;

import com.gdg_team9.SafePlate.api.code.BaseErrorCode;
import com.gdg_team9.SafePlate.api.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {
    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "MEMBER4004", "이미 존재하는 이메일입니다."),

    AI_SERVER_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AI5000", "AI 서버에서 문제가 발생했습니다."),
    AI_CONNECT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "AI5001", "AI 서버와의 연결에 실패했습니다."),

    FILE_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "FILE4000", "준비되지 않은 파일입니다."),
    FILE_NOT_OWNED(HttpStatus.FORBIDDEN, "FILE4003", "파일의 소유자가 아닙니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE4004", "해당하는 파일이 없습니다."),


    TEAM_NOT_ASSIGNED(HttpStatus.FORBIDDEN, "GROUP4003", "그룹의 멤버가 아닙니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "GROUP4004", "그룹을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
