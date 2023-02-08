package gateway.server.service.interfaces

import gateway.server.entity.User

interface IUserService {
    fun getUsers () : List<User>
    fun addUser (username : String, password : String)
    fun logIn (username: String, password: String) : String
    fun logOut (token : String)
    fun changePassword (token : String, password : String)

    fun addRole (roleName : String)
    fun getRoles () : List<String>

    fun attachRole (userId : Int, role : String)
    fun checkPermission (token : String, minRole : String) : Int
}