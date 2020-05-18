package com.example.account.security

import com.example.account.service.PermissionService
import com.example.account.service.UserService
import com.example.database.entity.Permission
import com.example.security.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.stereotype.Service
import java.util.*

@Configuration
@EnableWebSecurity
class AccountAuthenticationConfig : BaseAuthenticationConfig() {

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
        var jwtUser = parseJwt(jwtConfig, jwt) ?: return null
        var user = userService.getByJwtUser(jwtUser) ?: return null
        var authorizationUser = AuthorizationUser(AuthorizationUserType.USER.value, user.id!!)
        val permissionAuthorities: MutableList<PermissionAuthority> = ArrayList()
        val permissions: List<Permission> = permissionService.listByUserIdAndModule(user.id!!, module)
        if (permissions.isNotEmpty()) {
            for (permission in permissions) {
                val authority = PermissionAuthority(permission)
                permissionAuthorities.add(authority)
            }
        }
        return Authorization(authorizationUser, permissionAuthorities)
    }
}