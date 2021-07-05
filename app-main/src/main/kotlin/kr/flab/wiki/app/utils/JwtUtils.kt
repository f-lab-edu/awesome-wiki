package kr.flab.wiki.app.utils

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import java.security.KeyPair
import java.util.Date

class JwtUtils private constructor() {
    companion object {
        private const val EXPIRATION_TIME: Long = 1 * 1 * 30 * 60 * 1000
        /**
         * 해당 키페어는 서버 시작할 때마다 재생성됩니다.
         * 추후에는 미리 생성한 후 설정파일(ex : application-*.yml)에서 읽어드릴 예정입니다.
         */
        private val KEY_PAIR: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

        fun generateJws(authentication: Authentication): String {
            val email = authentication.name
            val claims: Map<String, String> = mapOf("email" to email, "roles" to "")

            val currentTime = System.currentTimeMillis()

            return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(Date(currentTime))
                .setExpiration(Date(currentTime + EXPIRATION_TIME))
                .addClaims(claims)
                .signWith(KEY_PAIR.private)
                .compact()
        }
//        fun validateJws(jwsString: String): Jws<Claims>? {
//            val jws: Jws<Claims>
//            try {
//                jws = Jwts.parserBuilder()
//                    .setSigningKey(KEY_PAIR.public)
//                    .build()
//                    .parseClaimsJws(jwsString)
//            } catch (e: JwtException) {
//                return null
//            }
//            return jws
//        }
    }
}
