package cobo.chat.config.jwt

import cobo.chat.data.enum.RoleEnum
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val secret: String
) {

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
            .body.get("role", java.lang.Integer::class.java).toShort())
    }

    fun isAccessToken(token: String): Boolean{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .header["type"].toString() == "access_token"
    }
}