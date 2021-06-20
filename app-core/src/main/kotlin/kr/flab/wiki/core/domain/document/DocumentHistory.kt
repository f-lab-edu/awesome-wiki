package kr.flab.wiki.core.domain.document

import kr.flab.wiki.core.common.annotation.DomainModel
import kr.flab.wiki.core.domain.user.User
import java.time.LocalDateTime

@DomainModel
interface DocumentHistory {
    /**
     * DocumentHistory의 프로퍼티는 Document의 프로퍼티와 일치하지 않을 수 있다.
     *
     * 현재 스펙에서 이력의 변경에 대한 사항은 없으므로 val로 선언한다.
     */

    /**
     * masterTitle : Document 식별자
     *
     * History를 찾으려면 결국 Document 식별자를 알아야 하기 때문에 master document의 title을 추가했다.
     * 근데 이러면 document의 title이 변경될 때마다 update가 필요하게 된다.
     * 불필요한 update를 없애려면 document의 식별자는 고정된 값이 필요할 수도...?
     */
    val masterTitle: String

    val title: String

    val body: String

    val creator: User

    val createdAt: LocalDateTime

    val version: Long
}
