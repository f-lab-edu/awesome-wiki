package kr.flab.wiki.core.domain.user.business;

import kr.flab.wiki.core.domain.post.Post;
import kr.flab.wiki.core.domain.post.PostWritingService;
import kr.flab.wiki.core.domain.post.persistence.PostEntity;
import kr.flab.wiki.core.domain.post.repository.PostEntityRepository;

public class PostWritingServiceImpl implements PostWritingService {

    private final PostEntityRepository postRepository;

    public PostWritingServiceImpl(PostEntityRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public Post writePost(Post post) {
        return this.postRepository.save(PostEntity.from(post));
    }
}
