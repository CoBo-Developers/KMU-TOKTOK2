package cobo.auth.config.jwt

import cobo.auth.data.enums.RoleEnum
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*
import javax.management.relation.Role

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

    fun getStudentId(token: String): String?{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body.get("student_id", String::class.java)
    }

    fun getRole(token: String): RoleEnum?{
        return RoleEnum.from(Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body.get("role", Short::class.java))
    }

    fun isAccessToken(token: String): Boolean{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .header["type"].toString() == "access_token"
    }

    fun getAccessToken(id: Int, studentId: String?, roleEnum: RoleEnum): String{
        return getJwtToken(id, "access_token", studentId, roleEnum, accessTokenValidTime)
    }

    fun getRefreshToken(id: Int, studentId: String?, roleEnum: RoleEnum): String{
        return getJwtToken(id, "refresh_token", studentId, roleEnum, refreshTokenValidTime)
    }

    fun getJwtToken(id: Int, type: String, studentId: String?, roleEnum: RoleEnum, tokenValidTime: Long): String{
        val claims = Jwts.claims()
        claims["id"] = id
        claims["student_id"] = studentId
        claims["role"] = roleEnum.value

        return Jwts.builder()
            .setHeaderParam("type", type)
            .setClaims(claims)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }
}