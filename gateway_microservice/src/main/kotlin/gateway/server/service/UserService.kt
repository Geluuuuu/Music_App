package gateway.server.service

import com.fasterxml.jackson.databind.ObjectMapper
import gateway.server.dto.RoleList
import gateway.server.entity.User
import gateway.server.dto.UserList
import gateway.server.exception.*
import gateway.server.service.interfaces.IUserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate


@Service
class UserService : IUserService {
    @Value("\${links.api.user}")
    private lateinit var url : String

    private fun makeGetUsersRequest () : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:get_users></sample:get_users>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeAddUserRequest (username : String, password : String) : String{
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:add_user>\n" +
                "        <sample:username>${username}</sample:username>\n" +
                "        <sample:password>${password}</sample:password>\n" +
                "    </sample:add_user>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeAddRoleRequest (role : String) : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:add_role>\n" +
                "        <sample:role>${role}</sample:role>\n" +
                "    </sample:add_role>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeGetRolesRequest () : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:get_roles></sample:get_roles>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeAttachRole (idUser : Int, role : String) : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:add_role_user>\n" +
                "        <sample:id_user>${idUser}</sample:id_user>\n" +
                "        <sample:role_name>${role}</sample:role_name>\n" +
                "    </sample:add_role_user> \n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeLogInRequest (username: String, password: String) : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:log_in>\n" +
                "        <sample:username>${username}</sample:username>\n" +
                "        <sample:password>${password}</sample:password>\n" +
                "    </sample:log_in>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeCheckPermissionRequest (token : String, minRole : String) : String{
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:check_permission>\n" +
                "        <sample:token>${token}</sample:token>\n" +
                "        <sample:min_role>${minRole}</sample:min_role>\n" +
                "    </sample:check_permission>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeLogOutRequest (token : String) : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:log_out>\n" +
                "        <sample:token>${token}</sample:token>\n" +
                "    </sample:log_out>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    private fun makeChangePasswordRequest (token : String, password: String) : String {
        return "<?xml version=\"1.0\"?>\n" +
                "<soap11env:Envelope xmlns:soap11env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:sample=\"services.user.soap\">\n" +
                "  <soap11env:Body>\n" +
                "    <sample:change_password>\n" +
                "        <sample:token>${token}</sample:token>\n" +
                "        <sample:password>${password}</sample:password>\n" +
                "    </sample:change_password>\n" +
                "  </soap11env:Body>\n" +
                "</soap11env:Envelope>"
    }

    override fun getUsers(): List<User> {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeGetUsersRequest(), headers)

        val response : ResponseEntity<String> = restTemplate
                .exchange(url, HttpMethod.POST, entity, String :: class.java)

        val userListString : String = response.body!!.split("<tns:get_usersResult>")[1]
                .split("</tns:get_usersResult>")[0]
        val mapper = ObjectMapper()
        val listResponse: UserList = mapper.readValue(userListString, UserList::class.java)

        return listResponse.users
    }

    override fun addUser(username: String, password: String) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeAddUserRequest(username, password), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("BAD REQUEST"))
            throw UsernameAlreadyExistsException ("Exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()
    }

    override fun logIn(username: String, password: String) : String{
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeLogInRequest(username, password), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("FORBIDDEN"))
            throw UserNotExistsException("Not exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()

        return response.body!!.split("<tns:log_inResult>")[1]
            .split("</tns:log_inResult>")[0]
    }

    override fun logOut(token: String) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeLogOutRequest(token), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("FORBIDDEN"))
            throw UserNotExistsException("Not exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()
    }

    override fun changePassword(token: String, password: String) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeChangePasswordRequest(token, password), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("FORBIDDEN"))
            throw UserNotExistsException("Not exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()
    }

    override fun addRole(roleName: String) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeAddRoleRequest(roleName), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("BAD REQUEST"))
            throw RoleAlreadyExistsException("Exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()
    }

    override fun getRoles(): List<String> {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeGetRolesRequest(), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        val roleListString : String = response.body!!.split("<tns:get_rolesResult>")[1]
            .split("</tns:get_rolesResult>")[0]

        val mapper = ObjectMapper()
        val listResponse: RoleList = mapper.readValue(roleListString, RoleList::class.java)

        return listResponse.roles
    }

    override fun attachRole(userId: Int, role: String) {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeAttachRole(userId, role), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("BAD REQUEST"))
            throw UserNotExistsException ("Not exists")
        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()
    }

    override fun checkPermission(token: String, minRole: String): Int {
        val restTemplate = RestTemplate()

        val headers = HttpHeaders()

        val entity = HttpEntity <String> (makeCheckPermissionRequest(token, minRole), headers)

        val response : ResponseEntity<String> = restTemplate
            .exchange(url, HttpMethod.POST, entity, String :: class.java)

        if (response.body!!.contains("FORBIDDEN"))
            throw RoleNotEnoughException ("Role too low")

        if (response.body!!.contains("BAD REQUEST"))
            throw RoleNotExistsException("Not exists")

        if (response.body!!.contains("INTERNAL SERVER ERROR"))
            throw Exception()

        val userId : String = response.body!!.split("<tns:check_permissionResult>")[1]
            .split("</tns:check_permissionResult>")[0]

        return Integer.decode(userId)
    }
}