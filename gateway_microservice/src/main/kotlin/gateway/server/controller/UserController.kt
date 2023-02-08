package gateway.server.controller

import gateway.server.entity.User
import gateway.server.dto.UserRequest
import gateway.server.exception.RoleAlreadyExistsException
import gateway.server.exception.RoleNotEnoughException
import gateway.server.exception.UserNotExistsException
import gateway.server.exception.UsernameAlreadyExistsException
import gateway.server.service.interfaces.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping ("/api/gateway")
class UserController {
    @Autowired
    private lateinit var userService : IUserService

    @RequestMapping (value = ["/user"], method = [RequestMethod.GET])
    fun getAll (@RequestHeader token : String) : ResponseEntity <List<User>> {
        return try {
            userService.checkPermission(token, "admin")
            ResponseEntity(userService.getUsers(), HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/user"], method = [RequestMethod.POST])
    fun addUser (@RequestBody request : UserRequest) : ResponseEntity<Void> {
        return try {
            userService.addUser(request.username, request.password)
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : UsernameAlreadyExistsException) {
            ResponseEntity (HttpStatus.BAD_REQUEST)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/user/login"], method = [RequestMethod.POST])
    fun logIn (@RequestBody request : UserRequest) : ResponseEntity<String> {
        return try {
            val token : String = userService.logIn(request.username, request.password)
            ResponseEntity (token, HttpStatus.OK)
        } catch (exception : UserNotExistsException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/user/logout"], method = [RequestMethod.POST])
    fun logOut (@RequestHeader token : String) : ResponseEntity<Void> {
        return try {
            userService.logOut(token)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : UserNotExistsException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/user/pass"], method = [RequestMethod.PUT])
    fun logOut (@RequestHeader token : String, @RequestParam password : String) : ResponseEntity<Void> {
        return try {
            userService.changePassword(token, password)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : UserNotExistsException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/role"], method = [RequestMethod.POST])
    fun addRole (@RequestHeader token : String, @RequestParam role : String) : ResponseEntity<Void> {
        return try {
            userService.checkPermission(token, "admin")
            userService.addRole(role)
            ResponseEntity (HttpStatus.CREATED)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : RoleAlreadyExistsException) {
            ResponseEntity (HttpStatus.BAD_REQUEST)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/role"], method = [RequestMethod.GET])
    fun getAllRoles (@RequestHeader token : String) : ResponseEntity <List<String>> {
        return try {
            userService.checkPermission(token, "admin")
            ResponseEntity(userService.getRoles(), HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @RequestMapping (value = ["/user_role"], method = [RequestMethod.POST])
    fun attachRole (@RequestHeader token : String, @RequestParam userId : Int
                    , @RequestParam role : String) : ResponseEntity<Void> {
        return try {
            userService.checkPermission(token, "admin")
            userService.attachRole(userId, role)
            ResponseEntity (HttpStatus.OK)
        } catch (exception : RoleNotEnoughException) {
            ResponseEntity (HttpStatus.FORBIDDEN)
        } catch (exception : UserNotExistsException) {
            ResponseEntity (HttpStatus.BAD_REQUEST)
        } catch (exception : Exception) {
            ResponseEntity (HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}