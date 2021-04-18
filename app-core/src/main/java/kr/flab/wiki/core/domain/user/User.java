package kr.flab.wiki.core.domain.user;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kr.flab.wiki.core.domain.Identification;
import kr.flab.wiki.core.domain.user.persistence.UserEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public interface User extends Identification {

    String getName();
    LocalDateTime getLastActiveAt();
    @NotNull
    User EMPTY = User.Companion.EMPTY;

    final class Companion {
        @NotNull
        private static final User EMPTY;

        @NotNull
        public final User getEMPTY() {
            return EMPTY;
        }

        private Companion() {
        }

        static {
            UserEntity user = new UserEntity();
            user.setName("");
            user.setLastActiveAt(LocalDateTime.MIN);
            EMPTY = user;
        }
    }
}
