package kr.flab.wiki.core.domain.post.repository;


import kr.flab.wiki.core.domain.post.persistence.PostEntity;

import java.time.LocalDateTime;
import java.util.HashSet;

/**
 * Kotlin 의 class 는 기본이 상속 불가능 (public final). kotlin 언어 설계자들은 왜 그렇게 디자인을 했을까?
 * open 키워드 왠만하면 안 쓰는게 좋다.
 *
 * 하지만 open 키워드를 안 씀으로 인해서, Spring bean 도 못만들고, Mockito 의 Mock Proxy 도 못 만든다.
 *
 * 그럼 이 문제를 해결할 방법이 있을까?
 */
public class PostEntityRepositoryImpl implements PostEntityRepository {

    private final HashSet<PostEntity> database = new HashSet<>();

    @Override
    public PostEntity save(PostEntity post) {
        database.add(post);
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}
