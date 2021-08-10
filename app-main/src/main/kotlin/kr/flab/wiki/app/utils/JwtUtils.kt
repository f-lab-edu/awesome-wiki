package kr.flab.wiki.app.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.util.StringUtils
import java.security.KeyPair
import java.util.Date

class JwtUtils private constructor() {
    companion object {
        private const val EXPIRATION_TIME: Long = 1 * 1 * 30 * 60 * 1000

        /**
         * 해당 키페어는 서버 시작할 때마다 재생성됩니다.
         * 추후에는 미리 생성한 후 설정파일(ex : application-*.yml)에서 읽어들일 예정입니다.
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

        fun getEmailFromJws(jwsString: String): String {
            return Jwts.parserBuilder()
                .setSigningKey(KEY_PAIR.public)
                .build()
                .parseClaimsJws(jwsString)
                .body["email"]
                .toString()
        }

        fun validateJws(jwsString: String): Jws<Claims>? {
//            val jws: Jws<Claims>
//            try {
//                jws = Jwts.parserBuilder()
//                    .setSigningKey(KEY_PAIR.public)
//                    .build()
//                    .parseClaimsJws(jwsString)
//            } catch (e: ExpiredJwtException) {
//                return null
//            } catch (e: SignatureException) {
//                return null
//            } catch (e: MalformedJwtException) {
//                return null
//            } catch (e: UnsupportedJwtException) {
//                return null
//            } catch (e: IllegalArgumentException) {
//                return null
//            } catch (e: UnsupportedEncodingException) {
//                return null
//            }
            return Jwts.parserBuilder()
                .setSigningKey(KEY_PAIR.public)
                .build()
                .parseClaimsJws(jwsString)
        }

        fun parseJws(token: String): String? {
            if (StringUtils.hasText(token) and token.startsWith("Bearer ")) {
                return token.split(' ')[1]
            }
            return null
        }
    }
}
