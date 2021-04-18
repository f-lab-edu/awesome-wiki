package kr.flab.wiki.core.domain.post;

import kr.flab.wiki.core.domain.StringRandomUtils;
import kr.flab.wiki.core.domain.post.persistence.PostEntity;
import kr.flab.wiki.core.domain.user.UserDomainTestUtils;

import java.time.LocalDateTime;

public class PostDomainTestUtils {
    public static PostEntity createRandomPost(){
        PostEntity post = new PostEntity();
        post.setTitle(StringRandomUtils.getFaker().animal().name());
        post.setDisplayCount((int)(Math.random()*10000));
        post.setWriter(UserDomainTestUtils.createRandomUserEntity());
        post.setText(StringRandomUtils.getFaker().lorem().sentence(10));
        post.setCreatedAt(LocalDateTime.now());
        return post;
    }
}
