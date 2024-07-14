package cobo.auth.config.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secret: String,
) {
    private final val accessTokenValidTime = Duration.ofHours(2).toMillis()
    private final val refreshTokenValidTime = Duration.ofDays(7).toMillis()

    fun getId(token: String): Long{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body.get("id", java.lang.Long::class.java).toLong()
    }

    fun isAccessToken(token: String): Boolean{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJwt(token)
            .header["type"].toString() == "access_token"
    }

    fun getAccessToken(id: Int): String{
        return getJwtToken(id, "access_token", accessTokenValidTime)
    }

    fun getRefreshToken(id: Int): String{
        return getJwtToken(id, "refresh_token", refreshTokenValidTime)
    }

    fun getJwtToken(id: Int, type: String, tokenValidTime: Long): String{
        val claims = Jwts.claims()
        claims["id"] = id

        return Jwts.builder()
            .setHeaderParam("type", type)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }
}