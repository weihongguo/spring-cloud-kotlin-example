package com.example.account.security

import com.example.account.service.PermissionService
import com.example.account.service.UserService
import com.example.base.security.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*


@Configuration
@EnableWebSecurity
class AuthenticationConfig : BaseAuthenticationConfig() {

    @Autowired
    lateinit var accountAuthorizationService: AccountAuthorizationService

    override fun permitMatchers(): Array<String> {
        return arrayOf("/init", "/login", "/register", "/test/**")
    }

    override fun getAuthorizationService(): AuthorizationService {
        return accountAuthorizationService
    }
}

interface AccountAuthorizationService : AuthorizationService {
    fun getByJwt(jwt: String, module: String): Authorization?
}

@Service
class AccountAuthorizationServiceImpl : AccountAuthorizationService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var permissionService: PermissionService
    @Autowired
    lateinit var jwtConfig: JwtConfig

    override fun getByJwt(jwt: String): Authorization? {
        return getByJwt(jwt, "account")
    }

    override fun getByJwt(jwt: String, module: String): Authorization? {
        val jwtUser = parseJwt(jwtConfig, jwt) ?: return null
        val user = userService.getByJwtUser(jwtUser) ?: return null
        user.id?.let {
            val authorizationUser = AuthorizationUser(AuthorizationUserType.USER.value, it)
            val permissions = permissionService.listByUserIdAndModule(it, module)
            if (permissions.isNotEmpty()) {
                val permissionAuthorities: MutableList<PermissionAuthority> = ArrayList()
                for (permission in permissions) {
                    permissionAuthorities.add(PermissionAuthority(permission))
                }
                return Authorization(authorizationUser, permissionAuthorities)
            }
            return Authorization(authorizationUser)
        }
        return null
    }
}

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class PermissionConfig : BasePermissionConfig()

@RestControllerAdvice
class ExceptionHandler : BaseExceptionHandler()