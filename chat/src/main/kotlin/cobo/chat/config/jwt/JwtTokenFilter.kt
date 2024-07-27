package cobo.chat.config.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtTokenFilter(
    @Value("\${jwt.secret}")
    private val secret: String
) {

    fun getStudentId(token: String): String?{
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token)
            .body.get("student_id", String::class.java)
    }
}