package kr.flab.wiki.core.domain;

import kr.flab.wiki.core.domain.user.User;

import java.time.LocalDateTime;

public interface TextBody extends Identification {
    //작성자
    User getWriter();
    //본문 : TEXT or CLOB
    String getText();
    //작성시간 : 서버 기준으로 이 글이 저장된 시간
    LocalDateTime getCreatedAt();
}
