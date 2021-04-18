package kr.flab.wiki.core.domain.post.repository;

import kr.flab.wiki.core.domain.post.persistence.PostEntity;

public interface PostEntityRepository {
    PostEntity save(PostEntity post);
}
