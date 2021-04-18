package kr.flab.wiki.core.domain.post;

import kr.flab.wiki.core.domain.TextBody;

public interface Post extends TextBody {
    String getTitle();
    int getDisplayCount();
}
