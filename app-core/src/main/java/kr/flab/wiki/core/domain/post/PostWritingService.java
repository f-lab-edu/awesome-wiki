package kr.flab.wiki.core.domain.post;

public interface PostWritingService {
    /**
     * 글을 정상 등록했을 경우, 등록한 Post 모델을 반환합니다.
     * 이 때 반환되는 Post 의 `createdAt` 필드는 실제 저장된 시간으로 변경될 수 있습니다.
     *
     * 만약 등록에 실패한다면, 예외가 발생합니다.
     *   - 1분 전에 사용자가 글을 작성하고 또 작성을 시도하는 경우
     *   - 제목 또는 본문이 비어있는 경우
     *   - 제목의 길이가 10자 미만인 경우
     *   - 본문의 길이가 100자 이상인 경우
     * @throws RuntimeException
     */
    Post writePost(Post post);
}
