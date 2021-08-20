package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.document.persistence.DocumentHistoryEntity
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface DocumentHistory {

    /**
     *
     * 만약 해당 문서의 제목이 "한국"에서 "대한민국"으로 바뀌었다면, Document 만으로 그 이력을 추적할 수 없으므로
     * (Document 에서는 식별자인 title 이 다르면 다른 객체이므로)
     * DocumentHistory 도메인이 필요하다.
     *
     * DocumentHistory 의 프로퍼티는 Document 의 프로퍼티와 일치하지 않을 수 있다.
     *
     * 현재 스펙에서 이력의 변경에 대한 사항은 없으므로 val 로 선언한다.
     *
     */

    val title: String

    val body: String

    val creator: User

    val createdAt: LocalDateTime
    companion object {
        fun newInstance(
            title: String,
            body: String,
            creator: User,
            createdAt: LocalDateTime
        ): DocumentHistory {
            return DocumentHistoryEntity(title, body, creator, createdAt)
        }
    }
}
