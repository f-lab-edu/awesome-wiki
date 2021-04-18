package kr.flab.wiki.core.domain.post.repository;

import kr.flab.wiki.core.domain.post.persistence.PostEntity;

import java.time.LocalDateTime;
import java.util.HashSet;

public class MockPostEntityRepository implements PostEntityRepository{

    private final HashSet<PostEntity> database = new HashSet<>();

    @Override
    public PostEntity save(PostEntity post) {
        database.add(post);
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }

    public boolean has(PostEntity post){
        return database.contains(post);
    }
}
